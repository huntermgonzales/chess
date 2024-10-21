package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.LocalMemory;
import model.GameData;

public class CreateGameService extends Service{

    private int currentGameID = 0;

    public CreateGameService(LocalMemory localMemory) {
        super(localMemory);
    }

    public int createGame(String authToken, String gameName) throws DataAccessException {
        authorize(authToken);
        currentGameID++;
        int gameID = currentGameID;
        GameData newGame = new GameData(gameID, null, null, gameName, new ChessGame());
        gameDAO.addGame(newGame);
        return currentGameID;
    }
}
