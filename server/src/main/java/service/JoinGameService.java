package service;

import chess.ChessGame;
import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.LocalMemory;
import model.AuthData;
import model.GameData;

public class JoinGameService extends Service{
    public JoinGameService(LocalMemory localMemory) {
        super(localMemory);
    }

    private GameData addPlayer(ChessGame.TeamColor playerColor, GameData gameData, AuthData authData) throws DataAccessException {
        GameData newGameData;
        if (playerColor == ChessGame.TeamColor.BLACK) {
            if (gameData.blackUsername() != null && !gameData.blackUsername().equals(authData.username())) {
                throw new AlreadyTakenException("Error: already taken");
            }
            newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), authData.username(), gameData.gameName(), gameData.game());
        } else {
            if (gameData.whiteUsername() != null && !gameData.whiteUsername().equals(authData.username())) {
                throw new AlreadyTakenException("Error: already taken");
            }
            newGameData = new GameData(gameData.gameID(), authData.username(), gameData.blackUsername(), gameData.gameName(), gameData.game());
        }
        return newGameData;
    }

    public void joinGame(String authToken, ChessGame.TeamColor playerColor, int gameID) throws DataAccessException {
        AuthData authData = authorize(authToken);
        if (gameDAO.getGame(gameID) == null) {
            throw new BadRequestException("Error: bad request");
        }
        GameData gameData = gameDAO.getGame(gameID);
        gameData = addPlayer(playerColor, gameData, authData);
        gameDAO.updateGameData(gameData);
    }

}
