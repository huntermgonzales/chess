package webSocket;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataaccessConfig;
import dataaccess.GameDAO;
import exceptions.DataAccessException;
import exceptions.UnauthorizedException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;
import websocket.messages.ServerNotification;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    AuthDAO authDAO;
    GameDAO gameDAO;

    public WebSocketHandler() {
        this.authDAO = DataaccessConfig.getInstance().getAuthDAO();
        this.gameDAO = DataaccessConfig.getInstance().getGameDAO();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        try {
            switch (command.getCommandType()) {
                case CONNECT -> joinGame(command.getAuthToken(), command.getGameID(), session);
                case LEAVE -> leaveGame(command.getAuthToken(), command.getGameID(), session);
                case MAKE_MOVE -> makeMove(command.getAuthToken(), command.getGameID(), message, session);
                case RESIGN -> resign(command.getAuthToken(), command.getGameID(), session);
            }
        }catch (DataAccessException e) {
            session.getRemote().sendString("Unable to connect");
        }
    }


    private void joinGame(String authToken, int gameID, Session session) throws DataAccessException, IOException {
        verifyData(authToken, gameID, session);
        connections.add(authToken, gameID, session);

        //Load Game notification
        LoadGameMessage message = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        GameData gameData = gameDAO.getGame(gameID);
        message.setGame(gameData.game());
        connections.broadcast(authToken, ConnectionManager.BroadcastReceivers.SELF, message, gameID);

        //send notification to everyone else
        String username = authDAO.getAuthData(authToken).username();
        ServerNotification notification = createServerNotification(gameData, username);
        connections.broadcast(authToken, ConnectionManager.BroadcastReceivers.ALL_BUT_SELF, notification, gameID);
    }

    private ServerNotification createServerNotification(GameData gameData, String username) {
        ServerNotification notification = new ServerNotification(ServerMessage.ServerMessageType.NOTIFICATION);
        String color;
        if (gameData.blackUsername().equals(username) && gameData.whiteUsername().equals(username)) {
            color  = "both players";
        } else if (gameData.blackUsername().equals(username)) {
            color = "black";
        } else if(gameData.whiteUsername().equals(username)) {
            color = "white";
        } else {
            color = "an observer";
        }
        notification.addMessage(String.format("%s has joined the game as %s", username, color));
        return notification;
    }

    private void leaveGame(String authToken, int gameID, Session session) throws DataAccessException, IOException {
        if (verifyData(authToken, gameID, session)) {
            return;
        }

        AuthData authData = authDAO.getAuthData(authToken);
        GameData gameData = gameDAO.getGame(gameID);
        GameData updatedGame = updateUsers(gameID, gameData, authData);
        gameDAO.updateGameData(updatedGame);

        //create a message
        ServerNotification notification = new ServerNotification(ServerMessage.ServerMessageType.NOTIFICATION);
        notification.addMessage(String.format("%s has left the game", authData.username()));
        connections.broadcast(authToken, ConnectionManager.BroadcastReceivers.ALL_BUT_SELF, notification, gameID);

        connections.remove(authToken, gameID);
    }

    private void makeMove(String authToken, int gameID, String message, Session session) throws IOException, DataAccessException {
        if (verifyData(authToken, gameID, session)) {
            return;
        }
        MakeMoveCommand command = new Gson().fromJson(message, MakeMoveCommand.class);
        AuthData authData = authDAO.getAuthData(authToken);
        ChessMove chessMove = command.getChessMove();

        GameData gameData = gameDAO.getGame(gameID);
        ChessGame game = gameData.game();

        if (game.isGameFinished()) {
            String errorMessage = "Error: the game is already over";
            sendErrorMessage(errorMessage, authToken, gameID);
            return;
        }

        String username = authData.username();
        ChessGame.TeamColor color = null;
        if (gameData.blackUsername().equals(username)) {
            color = ChessGame.TeamColor.BLACK;
        } else if(gameData.whiteUsername().equals(username)) {
            color = ChessGame.TeamColor.WHITE;
        }
        ChessGame.TeamColor teamColorBeingMoved = game.getBoard().getPiece(chessMove.getStartPosition()).getTeamColor();
        if (teamColorBeingMoved != color) {
            String errorMessage = "Error: it is not your turn";
            sendErrorMessage(errorMessage, authToken, gameID);
            return;
        }
        try {
            game.makeMove(chessMove);
        } catch (InvalidMoveException e) {
            String errorMessage = "Error: Invalid move";
            sendErrorMessage(errorMessage, authToken, gameID);
            return;
        }
        GameData updatedGameData = new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(),
                gameData.gameName(), game);
        gameDAO.updateGameData(updatedGameData);

        LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        loadGameMessage.setGame(game);
        connections.broadcast(authToken, ConnectionManager.BroadcastReceivers.EVERYONE, loadGameMessage, gameID);

        ServerNotification serverNotification = new ServerNotification(ServerMessage.ServerMessageType.NOTIFICATION);

        serverNotification.addMessage(String.format("%s has moved %s to %s", authData.username(),
                chessMove.getStartPosition().toString(), chessMove.getEndPosition().toString()));
        connections.broadcast(authToken, ConnectionManager.BroadcastReceivers.ALL_BUT_SELF, serverNotification, gameID);
    }

    public void resign(String authToken, int gameID, Session session) throws IOException, DataAccessException {
        if (verifyData(authToken, gameID, session)) {
            return;
        }
        AuthData authData = authDAO.getAuthData(authToken);
        GameData gameData = gameDAO.getGame(gameID);
        ChessGame game = gameData.game();

        if (game.isGameFinished()) {
            String errorMessage = "Error: the game is already over";
            sendErrorMessage(errorMessage, authToken, gameID);
            return;
        }

        String username = authData.username();
        if (!(gameData.blackUsername().equals(username) || gameData.whiteUsername().equals(username))) {
            String message = "Error: you cannot resign if you are not playing";
            sendErrorMessage(message, authToken, gameID);
        }

        ServerNotification notification = new ServerNotification(ServerMessage.ServerMessageType.NOTIFICATION);
        notification.addMessage(String.format("%s has resigned", username));
        connections.broadcast(authToken, ConnectionManager.BroadcastReceivers.EVERYONE, notification, gameID);

        game.setGameFinished(true);
    }

    //returns true if the data threw an error, and false otherwise
    private boolean verifyData(String authToken, int gameID, Session session) throws IOException, DataAccessException {
        //checks if gameID is valid
        if (gameDAO.getGame(gameID) == null) {
            connections.add(authToken, gameID, session);
            String message = "Error: invalid game ID";
            sendErrorMessage(message, authToken, gameID);
            connections.remove(authToken, gameID);
            return true;
        }

        //checks if the authToken is valid
        AuthData authData = authDAO.getAuthData(authToken);
        if (authData == null) {
            connections.add(authToken, gameID, session);
            String message = "Error: you do not have permission to do this";
            sendErrorMessage(message, authToken, gameID);
            connections.remove(authToken, gameID);
            return true;
        }


        return false;
    }

    private GameData updateUsers(int gameID, GameData gameData, AuthData authData) {
        GameData updatedGame;
        //figures out which color the requesting player is
        if (Objects.equals(gameData.whiteUsername(), authData.username())) {
            updatedGame = new GameData(gameID, null, gameData.blackUsername(), gameData.gameName(), gameData.game());
        } else if (Objects.equals(gameData.blackUsername(), authData.username())) {
            updatedGame = new GameData(gameID, gameData.whiteUsername(), null, gameData.gameName(), gameData.game());
        } else {
            updatedGame = gameData;  //observing player does not need to be removed
        }
        return updatedGame;
    }

    private void sendErrorMessage(String message, String authToken, int gameID) throws IOException {
        ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR);
        errorMessage.setErrorMessage(message);
        connections.broadcast(authToken, ConnectionManager.BroadcastReceivers.SELF, errorMessage, gameID);
    }

}
