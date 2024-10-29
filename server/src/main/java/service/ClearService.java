package service;

import results.ClearResult;
import dataaccess.exceptions.DataAccessException;

public class ClearService extends Service{

    public ClearResult clearAll () throws DataAccessException {
        gameDAO.deleteAll();
        authDAO.deleteAll();
        userDAO.deleteAll();
        if (gameDAO.getNumberOfGames() != 0) {
            throw new DataAccessException("Error: unable to clear database");
        }
        return new ClearResult();
    }

}
