package client;

import chess.ChessBoard;
import chess.ChessGame;
import exceptions.ResponseException;
import model.AuthData;
import model.GameData;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.ListGameResponse;
import server.ServerFacade;
import ui.ChessBoardArtist;
import ui.UserStatus;

import java.util.Arrays;
import java.util.List;

public class ChessClient {
    private final String url;
    private final ServerFacade server;
    private UserStatus status = UserStatus.SIGNED_OUT;
    private String authToken = null;


    public ChessClient(String serverUrl) {
        url = serverUrl;
        server = new ServerFacade(url);
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
                default -> help();
            };
        } catch (ResponseException ex) {
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
        throw new ResponseException(400, "Error: you were not logged in");
    }

    public String createGame(String... params) throws ResponseException {
        if (status == UserStatus.SIGNED_OUT) {
            throw new ResponseException(400, "Error: you are not logged in");
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
            throw new ResponseException(400, "You are not signed in");
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

    public String joinGame(String... params) throws ResponseException {
        if (status != UserStatus.SIGNED_IN) {
            throw new ResponseException(400, "You are not signed in");
        }
        if (params.length >= 2) {
            ChessGame.TeamColor playerColor = switch (params[0].toLowerCase()) {
                case "black" -> ChessGame.TeamColor.BLACK;
                case "white" -> ChessGame.TeamColor.WHITE;
                default -> throw new ResponseException(400, "team color must be black or white");
            };
            int gameID = getGameIDToInt(params[1]);
            server.joinGame(new JoinGameRequest(playerColor, gameID), authToken);
            //change status to in game
            ChessBoard board = new ChessBoard();
            board.resetBoard();
            String boards = "\n" + new ChessBoardArtist().drawBoard(board, ChessGame.TeamColor.WHITE) + "\n" +
                    new ChessBoardArtist().drawBoard(board, ChessGame.TeamColor.BLACK) + "\n";
            return boards;
        }
        throw new ResponseException(400, "Expected: <black|white> <Game ID>");
    }

    public String observeGame(String... params) throws ResponseException {
        if (status != UserStatus.SIGNED_IN) {
            throw new ResponseException(400, "You are not signed in");
        }
        if (params.length >= 1) {
            int gameID = getGameIDToInt(params[0]);
            ChessBoard board = new ChessBoard();
            board.resetBoard();
            String boards = "\n" + new ChessBoardArtist().drawBoard(board, ChessGame.TeamColor.WHITE) + "\n";
            return boards;
        }
        throw new ResponseException(400, "Expected: <gameID>");
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
                    To play a game:             "play" <Black|White> <game ID>
                    To observe a game:          "observe" <game ID>
                    """;
        }
        throw new ResponseException(400, "Something went wrong");
    }

}
