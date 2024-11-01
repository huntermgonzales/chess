import chess.*;
import dataaccess.*;
import dataaccess.memoryDAO.MemoryAuthDAO;
import dataaccess.memoryDAO.MemoryGameDAO;
import dataaccess.memoryDAO.MemoryUserDAO;
import dataaccess.sqlDAO.SQLAuthDAO;
import dataaccess.sqlDAO.SQLGameDAO;
import dataaccess.sqlDAO.SQLUserDAO;
import server.Server;

public class Main {
    public static void main(String[] args) {

        int port = 8080;
        if (args.length >= 1) {
            port = Integer.parseInt(args[0]);
        }

        if (args.length >= 2 && args[1].equals("sql")) {
            DataaccessConfig.initialize(true);
        } else {
            DataaccessConfig.initialize(false);
        }

        Server server = new Server();
        server.run(port);
    }
}