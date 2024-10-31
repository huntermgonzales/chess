package dataaccess.sqlDAO;

import com.google.gson.Gson;
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
        var statement = "INSERT INTO authData (authToken, username) VALUES (?, ?)";
        var authToken = authData.authToken();
        var username = authData.username();
        MySQLAccess.executeUpdate(statement, authToken, username);
    }

    @Override
    public void deleteAll() {

    }
}
