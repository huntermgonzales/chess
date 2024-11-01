package service;

import results.ClearResult;
import dataaccess.exceptions.DataAccessException;

public class ClearService extends Service{

    public ClearResult clearAll () throws DataAccessException {
        gameDAO.deleteAll();
        authDAO.deleteAll();
        userDAO.deleteAll();
        return new ClearResult();
    }

}
