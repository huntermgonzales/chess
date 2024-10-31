import dataaccess.AuthDAO;
import dataaccess.exceptions.DataAccessException;
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
    void addTwoRetrieveFirstAuthData() throws DataAccessException {
        AuthDAO authDAO = new SQLAuthDAO();
        String authToken1 = UUID.randomUUID().toString();
        AuthData authData1 = new AuthData(authToken1, "username1");
        authDAO.addAuthData(authData1);
        String authToken2 = UUID.randomUUID().toString();
        AuthData authData2 = new AuthData(authToken2, "username2");
        authDAO.addAuthData(authData2);
        Assertions.assertEquals(authData1, authDAO.getAuthData(authToken1));
    }

    @Test
    void addDuplicateAuthToken() throws DataAccessException {
        AuthDAO authDAO = new SQLAuthDAO();
        String authToken = UUID.randomUUID().toString();
        authDAO.addAuthData(new AuthData(authToken, "username"));
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.addAuthData(new AuthData(authToken, "username1")));
    }

    @Test
    void deleteAuthData() throws DataAccessException {
        AuthDAO authDAO = new SQLAuthDAO();
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, "username");
        authDAO.addAuthData(authData);
        authDAO.deleteAuthData(authToken);
        Assertions.assertNull(authDAO.getAuthData(authToken));
    }
}
