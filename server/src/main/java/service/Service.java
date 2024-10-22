package service;

import dataaccess.*;
import model.AuthData;

import java.util.UUID;

public abstract class Service {

    LocalMemory localMemory;
    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;

    public Service(LocalMemory localMemory) {
        this.localMemory = localMemory;
        this.userDAO = new MemoryUserDAO(localMemory);
        this.authDAO = new MemoryAuthDAO(localMemory);
        this.gameDAO = new MemoryGameDAO(localMemory);
    }

    protected AuthData createAuthData(String username) throws DataAccessException {

        if (localMemory.getUserData(username) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        String authToken = UUID.randomUUID().toString();
        return new AuthData(authToken, username);
    }

    protected AuthData authorize(String authToken) throws DataAccessException {
        if (authDAO.getAuthData(authToken) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return authDAO.getAuthData(authToken);
    }
}
