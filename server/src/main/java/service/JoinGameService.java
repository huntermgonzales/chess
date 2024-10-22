package service;

import chess.ChessGame;
import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.LocalMemory;
import model.AuthData;
import model.GameData;
import requests.JoinGameRequest;

public class JoinGameService extends Service{


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

    public void joinGame(JoinGameRequest request) throws DataAccessException {
        AuthData authData = authorize(request.authToken());
        if (gameDAO.getGame(request.gameID()) == null) {
            throw new BadRequestException("Error: bad request");
        }
        GameData gameData = gameDAO.getGame(request.gameID());
        gameData = addPlayer(request.playerColor(), gameData, authData);
        gameDAO.updateGameData(gameData);
    }

}
