package service;

import Results.RegisterResult;
import dataaccess.*;
import model.AuthData;
import model.UserData;
import requests.RegisterRequest;

public class RegisterService extends Service {

    public RegisterResult register(RegisterRequest request) throws DataAccessException {
        if (userDAO.getUserData(request.username()) != null) {
            throw new AlreadyTakenException("Error: already taken");
        }
        UserData userData = new UserData(request.username(), request.password(), request.email());
        userDAO.addUser(userData);
        AuthData authData = createAuthData(request.username());
        authDAO.addAuthData(authData);
        return new RegisterResult(authData.username(), authData.authToken());
    }
}
