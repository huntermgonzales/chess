import dataaccess.*;
import exceptions.DataAccessException;
import server.Server;

public class Main {
    public static void main(String[] args) throws DataAccessException {

        int port = 8080;
        if (args.length >= 1) {
            port = Integer.parseInt(args[0]);
        }

        if (args.length >= 2 && args[1].equalsIgnoreCase("SQL")) {
            DataaccessConfig.initialize(true);
        } else {
            DataaccessConfig.initialize(false);
        }

        Server server = new Server();
        server.run(port);
    }
}