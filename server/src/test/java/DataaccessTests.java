import dataaccess.AuthDAO;
import dataaccess.exceptions.DataAccessException;
import dataaccess.sqlDAO.DatabaseManager;
import dataaccess.sqlDAO.MySQLAccess;
import dataaccess.sqlDAO.SQLAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class DataaccessTests {

    @BeforeAll
    static void initializeSQLServer() throws DataAccessException {
        new MySQLAccess();
    }

    @Test
    void addAuthData() throws DataAccessException {
        AuthDAO authDAO = new SQLAuthDAO();
        authDAO.addAuthData(new AuthData(UUID.randomUUID().toString(), "username"));
    }
}
