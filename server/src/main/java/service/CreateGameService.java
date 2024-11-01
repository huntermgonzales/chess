package service;

import results.CreateGameResult;
import chess.ChessGame;
import dataaccess.DataAccessException;
import model.GameData;
import requests.CreateGameRequest;

public class CreateGameService extends Service{


    public CreateGameResult createGame(CreateGameRequest createGameRequest, String authToken) throws DataAccessException {
        authorize(authToken);
        GameData newGame = new GameData(null, null, null, createGameRequest.gameName(), new ChessGame());
        int gameID = gameDAO.addGame(newGame);
        return new CreateGameResult(gameID);
    }
}
