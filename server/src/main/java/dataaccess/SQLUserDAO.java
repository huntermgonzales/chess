package dataaccess;

import exceptions.DataAccessException;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {

    @Override
    public void addUser(UserData userData) throws DataAccessException {
        var statement = "INSERT INTO userData (username, hashedPassword, email) VALUES (?, ?, ?)";
        var username = userData.username();
        var password = userData.password();
        var email = userData.email();
        MySQLAccess.executeUpdate(statement, username, password, email);
    }

    @Override
    public UserData getUserData(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, HashedPassword, email FROM userData WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUserData(rs);
                    } else {
                        throw new DataAccessException("Error: data does not exist");
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Error: unable to get data from database");
        }
    }

    @Override
    public void deleteAll() throws DataAccessException {
        var statement = "TRUNCATE userData";
        MySQLAccess.executeUpdate(statement);
    }

    private UserData readUserData(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var password = rs.getString("hashedPassword");
        var email = rs.getString("email");
        return new UserData(username, password, email);
    }
}
