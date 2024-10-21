package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;

public class RegisterService extends Service {

    public RegisterService(LocalMemory localMemory) {
        super(localMemory);
    }

    public AuthData register(String username, String password, String email) throws DataAccessException {
        if (userDAO.getUserData(username) != null) {
            throw new DataAccessException("Error: already taken");
        }
        UserData userData = new UserData(username, password, email);
        userDAO.addUser(userData);
        AuthData authData = createAuthData(username);
        authDAO.addAuthData(authData);
        return authData;
    }
}
