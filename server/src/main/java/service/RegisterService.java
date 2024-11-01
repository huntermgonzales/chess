package service;

import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import exceptions.DataAccessException;
import org.mindrot.jbcrypt.BCrypt;
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
        String hashedPassword = BCrypt.hashpw(request.password(), BCrypt.gensalt());;
        UserData userData = new UserData(request.username(), hashedPassword, request.email());
        userDAO.addUser(userData);
        AuthData authData = createAuthData(request.username());
        authDAO.addAuthData(authData);
        return new RegisterResult(authData.username(), authData.authToken());
    }
}
