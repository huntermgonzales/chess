package service;

import Results.LogoutResult;
import dataaccess.DataAccessException;
import requests.LogoutRequest;

public class LogoutService extends Service{


    public LogoutResult logout(String authToken) throws DataAccessException {
        authorize(authToken); //throws exception
        authDAO.deleteAuthData(authToken);
        return new LogoutResult();
    }
}
