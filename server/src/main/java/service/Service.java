package service;

import dataaccess.*;
import model.AuthData;

import java.util.UUID;

public abstract class Service {
    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;


    public Service() {
        this.userDAO = new MemoryUserDAO();
        this.authDAO = new MemoryAuthDAO();
        this.gameDAO = new MemoryGameDAO();
    }

    protected AuthData createAuthData(String username) throws DataAccessException {

        if (userDAO.getUserData(username) == null) {
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
