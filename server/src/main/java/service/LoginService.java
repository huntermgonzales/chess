package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class LoginService extends Service{

    public LoginService(LocalMemory localMemory) {
        super(localMemory);
    }

    public AuthData login(String username, String password) throws DataAccessException {
        if (userDAO.getUserData(username) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        UserData userData = userDAO.getUserData(username);
        if (userData.password() != password) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        AuthData authData = createAuthData(username);
        authDAO.addAuthData(authData);
        return authData;

    }


}
