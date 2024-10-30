import dataaccess.exceptions.DataAccessException;
import dataaccess.sqlDAO.DatabaseManager;
import dataaccess.sqlDAO.MySQLAccess;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DataaccessTests {

    @Test
    void initializeSQLServer() throws DataAccessException {
        new MySQLAccess();
    }
}
