package service;

import results.JoinGameResult;
import chess.ChessGame;
import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import exceptions.DataAccessException;
import model.AuthData;
import model.GameData;
import requests.JoinGameRequest;

import java.util.Objects;

public class JoinGameService extends Service{


    private GameData addPlayer(ChessGame.TeamColor playerColor, GameData gameData, AuthData authData) throws DataAccessException {
        GameData newGameData;
        if (playerColor == ChessGame.TeamColor.BLACK) {
            if (!Objects.equals(gameData.blackUsername(), null) && !gameData.blackUsername().equals(authData.username())) {
                throw new AlreadyTakenException("Error: already taken");
            }
            newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), authData.username(), gameData.gameName(), gameData.game());
        } else if (playerColor == ChessGame.TeamColor.WHITE) {
            if (!Objects.equals(gameData.whiteUsername(), null) && !gameData.whiteUsername().equals(authData.username())) {
                throw new AlreadyTakenException("Error: already taken");
            }
            newGameData = new GameData(gameData.gameID(), authData.username(), gameData.blackUsername(), gameData.gameName(), gameData.game());
        } else {
            throw new BadRequestException("Error: bad request");
        }
        return newGameData;
    }

    public JoinGameResult joinGame(String authToken, JoinGameRequest request) throws DataAccessException {
        AuthData authData = authorize(authToken);
        if (gameDAO.getGame(request.gameID()) == null) {
            throw new BadRequestException("Error: bad request");
        }
        GameData gameData = gameDAO.getGame(request.gameID());
        gameData = addPlayer(request.playerColor(), gameData, authData);
        gameDAO.updateGameData(gameData);
        return new JoinGameResult();
    }

}
