package service;

import Results.CreateGameResult;
import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.LocalMemory;
import model.GameData;
import requests.CreateGameRequest;

public class CreateGameService extends Service{


    public CreateGameResult createGame(CreateGameRequest createGameRequest, String authToken) throws DataAccessException {
        authorize(authToken);

        int gameID = 1 + gameDAO.getNumberOfGames();
        GameData newGame = new GameData(gameID, null, null, createGameRequest.gameName(), new ChessGame());
        gameDAO.addGame(newGame);
        return new CreateGameResult(gameID);
    }
}
