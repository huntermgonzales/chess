package service;

import results.ListGamesResult;
import dataaccess.DataAccessException;

public class ListGameService extends Service{


    public ListGamesResult listGames(String authToken) throws DataAccessException {
        authorize(authToken);
        return new ListGamesResult(gameDAO.listAllGames());
    }
}
