package server;

import handler.*;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (req, res) -> new RegisterHandler().handleRegister(req, res));
        Spark.post("/session", (req, res) -> new LoginHandler().handleLogin(req, res));
        Spark.delete("/session", (req, res) -> new LogoutHandler().handleLogout(req, res));
        Spark.post("/game", (req, res) -> new CreateGameHandler().handleCreateGame(req, res));
        Spark.get("/game", (req, res) -> new ListGamesHandler().handleListGames(req, res));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
