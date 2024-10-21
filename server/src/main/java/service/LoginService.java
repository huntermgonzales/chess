package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class LoginService {
    LocalMemory localMemory = new LocalMemory();
    public LoginService(LocalMemory localMemory) {
        this.localMemory = localMemory;
    }

    public void login(String username, String password) throws DataAccessException {
        UserDAO userDAO = new MemoryUserDAO(localMemory);
        UserData userData = userDAO.getUserData(username);
    }

    public AuthData createAuthData(String username) throws DataAccessException {
        if (localMemory.getUserData(username) == null) {
            throw new DataAccessException("user does not exist");
        }
        String authToken = UUID.randomUUID().toString();
        return new AuthData(authToken, username);
    }
}
