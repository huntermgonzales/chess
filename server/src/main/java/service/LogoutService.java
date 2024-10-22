package service;

import dataaccess.DataAccessException;
import dataaccess.LocalMemory;
import model.AuthData;
import requests.LogoutRequest;

public class LogoutService extends Service{


    public void logout(LogoutRequest logoutRequest) throws DataAccessException {
        authorize(logoutRequest.authToken()); //throws exception
        authDAO.deleteAuthData(logoutRequest.authToken());
    }
}
