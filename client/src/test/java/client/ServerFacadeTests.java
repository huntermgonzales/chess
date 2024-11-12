package client;

import exceptions.ResponseException;
import org.junit.jupiter.api.*;
import requests.RegisterRequest;
import server.Server;
import server.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String url = "http://localhost:" + server.port();
        serverFacade = new ServerFacade(url);
    }

    @BeforeEach
    void setUp() throws ResponseException {
        serverFacade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerSuccess() {
        String username  = "username";
        var request = new RegisterRequest(username, "password", "email");
        var response = Assertions.assertDoesNotThrow(() -> serverFacade.register(request));
        Assertions.assertEquals(username, response.username());
    }

    @Test
    void registerDuplicate() throws ResponseException {
        var request = new RegisterRequest("username", "password", "email");
        serverFacade.register(request);
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.register(request));
    }


}
