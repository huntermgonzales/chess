package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.LocalMemory;
import model.GameData;

import java.util.List;

public class ListGameService extends Service{


    public List<GameData> listGames(String authToken) throws DataAccessException {
        authorize(authToken);
        return gameDAO.listAllGames();
    }
}
