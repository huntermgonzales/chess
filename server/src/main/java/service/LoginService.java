package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import requests.LoginRequest;

import java.util.UUID;

public class LoginService extends Service{


    public AuthData login(LoginRequest request) throws DataAccessException {
        if (userDAO.getUserData(request.username()) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        UserData userData = userDAO.getUserData(request.username());
        if (userData.password() != request.password()) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        AuthData authData = createAuthData(request.username());
        authDAO.addAuthData(authData);
        return authData;

    }


}
