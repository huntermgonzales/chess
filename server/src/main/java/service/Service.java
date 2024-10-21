package service;

import dataaccess.*;
import model.AuthData;

import java.util.UUID;

public abstract class Service {

    LocalMemory localMemory;
    UserDAO userDAO;
    AuthDAO authDAO;

    public Service(LocalMemory localMemory) {
        this.localMemory = localMemory;
        this.userDAO = new MemoryUserDAO(localMemory);
        this.authDAO = new MemoryAuthDAO(localMemory);
    }

    public AuthData createAuthData(String username) throws DataAccessException {

        if (localMemory.getUserData(username) == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        String authToken = UUID.randomUUID().toString();
        return new AuthData(authToken, username);
    }

    public AuthData authorize(String authToken) throws DataAccessException {
        if (authDAO.getAuthData(authToken) == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        return authDAO.getAuthData(authToken);
    }
}
