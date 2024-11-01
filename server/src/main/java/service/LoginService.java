package service;

import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import org.mindrot.jbcrypt.BCrypt;
import results.LoginResult;
import model.AuthData;
import model.UserData;
import requests.LoginRequest;

import java.util.Objects;

public class LoginService extends Service{


    public LoginResult login(LoginRequest request) throws DataAccessException {
        if (userDAO.getUserData(request.username()) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        UserData userData = userDAO.getUserData(request.username());
        if (!BCrypt.checkpw(request.password(), userData.password())) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        AuthData authData = createAuthData(request.username());
        authDAO.addAuthData(authData);
        return new LoginResult(authData.username(), authData.authToken());

    }


}
