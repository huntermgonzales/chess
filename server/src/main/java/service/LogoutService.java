package service;

import dataaccess.DataAccessException;
import dataaccess.LocalMemory;
import model.AuthData;

public class LogoutService extends Service{


    public void logout(String authToken) throws DataAccessException {
        authorize(authToken); //throws exception
        authDAO.deleteAuthData(authToken);
    }
}
