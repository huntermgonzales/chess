package dataaccess.sqlDAO;

import dataaccess.AuthDAO;
import dataaccess.exceptions.DataAccessException;
import model.AuthData;

public class SQLAuthDAO implements AuthDAO {
    @Override
    public void deleteAuthData(String authToken) throws DataAccessException {

    }

    @Override
    public AuthData getAuthData(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void addAuthData(AuthData authData) throws DataAccessException {

    }

    @Override
    public void deleteAll() {

    }
}
