import dataaccess.AuthDAO;
import dataaccess.exceptions.DataAccessException;
import dataaccess.sqlDAO.DatabaseManager;
import dataaccess.sqlDAO.MySQLAccess;
import dataaccess.sqlDAO.SQLAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;

public class DataaccessTests {

    @BeforeAll
    static void initializeSQLServer() throws DataAccessException {
        new MySQLAccess();
    }

    @BeforeEach
    void setUp() throws DataAccessException {
        new SQLAuthDAO().deleteAll();
    }

    @Test
    void deleteAll() throws DataAccessException {
        new SQLAuthDAO().deleteAll();
    }

    @Test
    void addAuthData() throws DataAccessException {
        AuthDAO authDAO = new SQLAuthDAO();
        authDAO.addAuthData(new AuthData(UUID.randomUUID().toString(), "username"));
    }

    @Test
    void addAndRetrieveAuthData() throws DataAccessException {
        AuthDAO authDAO = new SQLAuthDAO();
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, "username");
        authDAO.addAuthData(authData);
        Assertions.assertEquals(authData, authDAO.getAuthData(authToken));
    }

    @Test
    void addDuplicateAuthToken() throws DataAccessException {
        AuthDAO authDAO = new SQLAuthDAO();
        String authToken = UUID.randomUUID().toString();
        authDAO.addAuthData(new AuthData(authToken, "username"));
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.addAuthData(new AuthData(authToken, "username1")));
    }
}
