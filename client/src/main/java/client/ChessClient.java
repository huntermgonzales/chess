package client;

import chess.*;
import exceptions.ResponseException;
import model.AuthData;
import model.GameData;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.ListGameResponse;
import server.ServerFacade;
import server.WebSocketFacade;
import ui.ChessBoardArtist;
import ui.UserStatus;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ChessClient {
    private final String url;
    private final ServerFacade server;
    private WebSocketFacade webSocket;
    private UserStatus status = UserStatus.SIGNED_OUT;
    private String authToken = null;
    private ChessGame game;
    private ChessGame.TeamColor color = null;


    public ChessClient(String serverUrl) {
        url = serverUrl;
        server = new ServerFacade(url);
    }

    public void saveGameData(ChessGame game) {
        this.game = game;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "logout" -> logout();
                case "quit", "q" -> "quit";
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "leave" -> leaveGame();
                case "redraw" -> redrawBoard();
                case "highlight" -> highlightPossibleMoves(params);
                case "move" -> makeMove(params);
                case "resign" -> resign();
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (status != UserStatus.SIGNED_OUT) {
            throw new ResponseException(400, "You are already signed in");
        }
        if (params.length >= 3) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            AuthData authData = server.register(new RegisterRequest(username, password, email));
            authToken = authData.authToken();
            status = UserStatus.SIGNED_IN;
            return "Successfully registered";
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }

    public String login(String... params) throws ResponseException {
        if (status != UserStatus.SIGNED_OUT) {
            throw new ResponseException(400, "You are already signed in");
        }
        if (params.length == 2) {
            String username = params[0];
            String password = params[1];
            AuthData authData = server.login(new LoginRequest(username, password));
            authToken = authData.authToken();
            status = UserStatus.SIGNED_IN;
            return "Successfully logged in";
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }

    public String logout() throws ResponseException {
        if (status != UserStatus.SIGNED_OUT && authToken != null) {
            server.logout(authToken);
            authToken = null;
            status = UserStatus.SIGNED_OUT;
            return "Successfully logged out";
        }
        throw new ResponseException(400, "Error: you were not logged in or are currently playing a game");
    }

    public String createGame(String... params) throws ResponseException {
        if (status != UserStatus.SIGNED_IN) {
            throw new ResponseException(400, "Error: you are not logged in or are already in a game");
        }
        if (params.length >= 1) {
            String gameName = params[0];
            server.createGame(new CreateGameRequest(gameName), authToken);
            return "Successfully created game " + gameName;
        }
        throw new ResponseException(400, "expected: <gameName>");
    }

    public String listGames() throws ResponseException {
        if (status != UserStatus.SIGNED_IN) {
            throw new ResponseException(400, "You are not signed in or are already in a game");
        }
        ListGameResponse response = server.listGames(authToken);
        List<GameData> games = response.games();
        StringBuilder outputString =  new StringBuilder();
        int count = 1;
        for (GameData game: games) {
            outputString.append(count).append(": ").append(game.toString()).append("\n");
            count++;
        }
        return outputString.toString();
    }


    private int getGameIDToInt(String gameIDString) throws ResponseException {
        int gameID;
        try {
            gameID = Integer.parseInt(gameIDString);
        } catch (NumberFormatException e) {
            throw new ResponseException(400, "Expected integer for game ID but got string");
        }
        List<GameData> games = server.listGames(authToken).games();
        if (gameID > games.size() || gameID < 1) {
            throw new ResponseException(400, "invalid gameID");
        }
        return games.get(gameID - 1).gameID();
    }

    public String joinGame(String... params) throws Exception {
        if (status != UserStatus.SIGNED_IN) {
            throw new ResponseException(400, "You are not signed in or are already in a game");
        }
        if (params.length >= 2) {
            color = switch (params[0].toLowerCase()) {
                case "black" -> ChessGame.TeamColor.BLACK;
                case "white" -> ChessGame.TeamColor.WHITE;
                default -> throw new ResponseException(400, "team color must be black or white");
            };
            int gameID = getGameIDToInt(params[1]);
            server.joinGame(new JoinGameRequest(color, gameID), authToken);
            status = UserStatus.PLAYING_GAME;
            webSocket = new WebSocketFacade(url, this);
            webSocket.joinGame(gameID, authToken, color);
            return "Successfully joined game as " + color;
        }
        throw new ResponseException(400, "Expected: <black|white> <Game ID>");
    }

    public String observeGame(String... params) throws Exception {
        if (status != UserStatus.SIGNED_IN) {
            throw new ResponseException(400, "You are not signed in or are already in a game");
        }
        if (params.length >= 1) {
            int gameID = getGameIDToInt(params[0]);
            webSocket = new WebSocketFacade(url, this);
            webSocket.joinGame(gameID, authToken, null);
            status = UserStatus.OBSERVING_GAME;
            return "You are now observing the game";
        }
        throw new ResponseException(400, "Expected: <gameID>");
    }

    public String leaveGame() throws Exception {
        if (status != UserStatus.PLAYING_GAME && status != UserStatus.OBSERVING_GAME) {
            throw new ResponseException(400, "You cannot leave a game you are not playing or observing");
        }
        webSocket.leaveGame(authToken);
        status = UserStatus.SIGNED_IN;
        return "You have left the game";
    }

    public String redrawBoard() throws ResponseException {
        if (status == UserStatus.PLAYING_GAME || status == UserStatus.OBSERVING_GAME) {
            return new ChessBoardArtist().drawBoard(game.getBoard(), color);
        }
        throw new ResponseException(400, "You must be in a game to redraw the board");
    }

    public String highlightPossibleMoves(String... params) throws ResponseException {
        if (params.length != 1) {
            throw new ResponseException(400, "please format your move without spaces. ie: highlight 2D");
        }else if (status != UserStatus.PLAYING_GAME && status != UserStatus.OBSERVING_GAME) {
            throw new ResponseException(400, "You must be playing or observing a game to highlight moves");
        }
        ChessPosition position = convertToChessPosition(params[0]);
        return new ChessBoardArtist().highlightBoard(game, color, position);
    }

    public String makeMove(String... params) throws Exception {
        if (params.length < 2) {
            throw new ResponseException(400, "Please format your move like this: move 2D 3D");
        }else if (status != UserStatus.PLAYING_GAME) {
            throw new ResponseException(400, "You must be playing a game to make a move");
        }else if (game.getTeamTurn() != color) {
            throw new ResponseException(400, "You can only make a move on your turn");
        }


        ChessPosition startPosition = convertToChessPosition(params[0]);
        ChessPosition endPosition = convertToChessPosition(params[1]);
        ChessPiece.PieceType promotionType;
        String promotionString;
        if (params.length > 2) {
            promotionString = params[2];
            promotionType = ChessPiece.stringToPieceType(promotionString);
        } else {
            promotionType = null;
        }
        ChessMove move = new ChessMove(startPosition, endPosition, promotionType);

        ChessPiece piece = game.getBoard().getPiece(move.getStartPosition());
        if (piece == null || piece.getTeamColor() != game.getTeamTurn()) {
            throw new ResponseException(400, "invalid move");
        }

        Collection<ChessMove> possibleMoves = game.validMoves(move.getStartPosition());
        if (!possibleMoves.contains(move)) {
            throw new ResponseException(400, "invalid move");
        }

        webSocket.makeMove(authToken, move);
        return "your have moved " + startPosition + " to " + endPosition;
    }

    private ChessPosition convertToChessPosition(String input) throws ResponseException {
        if (input.length() != 2) {
            throw new ResponseException(400, "please format your move like the following: highlight 2D");
        }
        //This will cast the characters given to be a number
        int row = input.charAt(0) - '0';
        int col = input.charAt(1) - 'a' + 1;

        if (row < 1 || row > 8 || col < 1 || col > 8) {
            throw new ResponseException(400, "please format your move like the following: highlight 2D");
        }
        return new ChessPosition(row, col);
    }

    public String resign() throws Exception {
        if (status != UserStatus.PLAYING_GAME) {
            throw new ResponseException(400, "you can only resign if you are playing a game");
        }
        webSocket.resign(authToken);
        return "";
    }


    public String help() throws ResponseException {
        if (status == UserStatus.SIGNED_OUT) {
            return """
                    To see this menu:               "h", "help"
                    To register as a new user:      "register" <username> <password> <email>
                    To login as an existing user:   "login" <username> <password>
                    To exit the program:            "q", "quit"
                    """;
        } else if (status == UserStatus.SIGNED_IN) {
            return """
                    To see this menu:           "h", "help"
                    To logout:                  "logout"
                    To create a game:           "create" <game name>
                    To list all created games:  "list"
                    To join a game:             "join" <Black|White> <game ID>
                    To observe a game:          "observe" <game ID>
                    """;
        } else if (status == UserStatus.PLAYING_GAME) {
            return """
                    To see this menu:           "h", "help"
                    To redraw the chess board:  "redraw"
                    To Leave the game:          "leave"
                    To resign:                  "resign"
                    To highlight legal moves:   "highlight" <chess position>
                    To make a move:             "move" <start position> <end position>
                    
                    Note: The positions should be input as number then letter.
                    For example you can type "move 2D 4D" to move the white pawn.
                    Additionally, if you are promoting your pawn, specify what type
                    you want the pawn to become so you can type "move 7e 8e queen"
                    """;
        } else if (status == UserStatus.OBSERVING_GAME) {
            return """
                    To see this menu:           "h", "help"
                    To redraw the chess board:  "redraw"
                    To leave the game:          "leave"
                    To highlight legal moves:   "highlight" <chess position>
                    
                    Note: The positions should be input as number then letter.
                    For example you can type "highlight 2D" to show moves for the white pawn
                    """;
        }
        throw new ResponseException(400, "Something went wrong");
    }

}
