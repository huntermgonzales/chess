package dataaccess;

public class DataaccessConfig {
    private static DataaccessConfig instance;
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private UserDAO userDAO;

    private DataaccessConfig(boolean useSQLMemory) {
        if (useSQLMemory) {
            this.authDAO = new SQLAuthDAO();
            this.gameDAO = new SQLGameDAO();
            this.userDAO = new SQLUserDAO();
        } else {
            this.authDAO = new MemoryAuthDAO();
            this.gameDAO = new MemoryGameDAO();
            this.userDAO = new MemoryUserDAO();
        }
    }

    public static void initialize(boolean useSQLMemory) {
        if (!isInitialized()) {
            instance = new DataaccessConfig(useSQLMemory);
        }
    }

    public static DataaccessConfig getInstance() {
        if (instance == null) {
            instance = new DataaccessConfig(true);
        }
        return instance;
    }

    public static boolean isInitialized() {
        return instance != null;
    }

    public AuthDAO getAuthDAO() {
        return authDAO;
    }


    public GameDAO getGameDAO() {
        return gameDAO;
    }


    public UserDAO getUserDAO() {
        return userDAO;
    }

}
