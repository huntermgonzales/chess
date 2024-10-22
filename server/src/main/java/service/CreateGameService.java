package service;

import Results.CreateGameResult;
import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.LocalMemory;
import model.GameData;
import requests.CreateGameRequest;

public class CreateGameService extends Service{

    private int currentGameID = 0;


    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        authorize(createGameRequest.authToken());
        currentGameID++;
        int gameID = currentGameID;
        GameData newGame = new GameData(gameID, null, null, createGameRequest.gameName(), new ChessGame());
        gameDAO.addGame(newGame);
        return new CreateGameResult(currentGameID);
    }
}
