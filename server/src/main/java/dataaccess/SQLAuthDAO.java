package dataaccess;

import model.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO {

    @Override
    public void deleteAuthData(String authToken) throws DataAccessException {
        var statement = "DELETE FROM authData WHERE authToken=?";
        MySQLAccess.executeUpdate(statement, authToken);
    }

    @Override
    public AuthData getAuthData(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM authData WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuthData(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Error: unable to get data from database");
        }
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
    public void deleteAll() throws DataAccessException {
        var statement = "TRUNCATE authData";
        MySQLAccess.executeUpdate(statement);
    }

    private AuthData readAuthData(ResultSet rs) throws SQLException {
        var authToken = rs.getString("authToken");
        var username = rs.getString("username");
        return new AuthData(authToken, username);
    }

}
