package dataaccess;

import model.AuthData;

public interface AuthDAO extends DataAccess{

    public void deleteAuthData(String authToken) throws DataAccessException;
    public AuthData getAuthData(String username) throws DataAccessException;
    public void addAuthData(AuthData authData) throws DataAccessException;
}
