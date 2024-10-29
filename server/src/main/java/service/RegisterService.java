package service;

import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.DataAccessException;
import results.RegisterResult;
import model.AuthData;
import model.UserData;
import requests.RegisterRequest;

public class RegisterService extends Service {

    public RegisterResult register(RegisterRequest request) throws DataAccessException {
        if (userDAO.getUserData(request.username()) != null) {
            throw new AlreadyTakenException("Error: already taken");
        }
        if (request.username() == null || request.password() == null || request.email() == null) {
            throw new BadRequestException("Error: bad request");
        }
        UserData userData = new UserData(request.username(), request.password(), request.email());
        userDAO.addUser(userData);
        AuthData authData = createAuthData(request.username());
        authDAO.addAuthData(authData);
        return new RegisterResult(authData.username(), authData.authToken());
    }
}
