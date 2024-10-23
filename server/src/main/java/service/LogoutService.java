package service;

import results.LogoutResult;
import dataaccess.DataAccessException;

public class LogoutService extends Service{


    public LogoutResult logout(String authToken) throws DataAccessException {
        authorize(authToken); //throws exception
        authDAO.deleteAuthData(authToken);
        return new LogoutResult();
    }
}
