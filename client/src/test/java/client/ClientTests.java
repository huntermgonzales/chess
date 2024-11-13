package client;

import exceptions.ResponseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.Server;
import server.ServerFacade;

public class ClientTests {

    private static Server server;
    static ChessClient client;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String url = "http://localhost:" + server.port();
        client = new ChessClient(url);
    }

    @AfterAll
    static void afterAll() {
        server.stop();
    }

    @Test
    void registerUser() {
        String result = Assertions.assertDoesNotThrow(() -> client.register("user1", "password", "email"));
        Assertions.assertEquals("Successfully registered\n", result);
    }

    @Test
    void invalidRegisterInput() {
        Assertions.assertThrows(ResponseException.class, () -> client.register("user2", "not enough"));
    }
}
