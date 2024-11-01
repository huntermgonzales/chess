package dataaccess;

import model.AuthData;

public interface AuthDAO extends DataAccess{

    void deleteAuthData(String authToken) throws DataAccessException;
    AuthData getAuthData(String authToken) throws DataAccessException;
    void addAuthData(AuthData authData) throws DataAccessException;
}
