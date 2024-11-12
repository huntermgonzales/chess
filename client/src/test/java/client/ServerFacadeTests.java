package client;

import chess.ChessGame;
import exceptions.ResponseException;
import model.AuthData;
import org.junit.jupiter.api.*;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.CreateGameResponse;
import responses.ListGameResponse;
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
        AuthData response = Assertions.assertDoesNotThrow(() -> serverFacade.register(request));
        Assertions.assertEquals(username, response.username());
        Assertions.assertTrue(response.authToken().length() > 10);
    }

    @Test
    void registerDuplicate() throws ResponseException {
        var request = new RegisterRequest("username", "password", "email");
        serverFacade.register(request);
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.register(request));
    }


    @Test
    void loginSuccess() throws ResponseException {
        String username  = "username";
        String password = "password";
        var registerRequest = new RegisterRequest(username, password, "email");
        serverFacade.register(registerRequest);
        var loginRequest = new LoginRequest(username, password);
        AuthData response = Assertions.assertDoesNotThrow( () -> serverFacade.login(loginRequest));
        Assertions.assertTrue(response.authToken().length() > 10);
    }

    @Test
    void loginUserNotExist() {
        var loginRequest = new LoginRequest("badUsername", "password");
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.login(loginRequest));
    }

    @Test
    void logoutUserSuccess() throws ResponseException {
        var request = new RegisterRequest("username", "password", "email");
        AuthData response = serverFacade.register(request);
        Assertions.assertDoesNotThrow(() -> serverFacade.logout(response.authToken()));
    }

    @Test
    void logoutInvalidAuth() {
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.logout("This is not an authToken"));
    }

    @Test
    void logoutTwice() throws ResponseException {
        var request = new RegisterRequest("username", "password", "email");
        AuthData response = serverFacade.register(request);
        serverFacade.logout(response.authToken());
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.logout(response.authToken()));
    }

    @Test
    void createGameSuccess() throws ResponseException {
        var registerRequest = new RegisterRequest("username", "password", "email");
        AuthData authData = serverFacade.register(registerRequest);
        CreateGameRequest createGameRequest = new CreateGameRequest("my game");
        CreateGameResponse response =  Assertions.assertDoesNotThrow(
                () -> serverFacade.createGame(createGameRequest, authData.authToken()));
        Assertions.assertNotNull(response);
    }

    @Test
    void createGameNotAllowed() {
        CreateGameRequest createGameRequest = new CreateGameRequest("my game");
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.createGame(createGameRequest, "fake auth"));
    }

    @Test
    void listGameSuccess() throws ResponseException {
        var registerRequest = new RegisterRequest("username", "password", "email");
        AuthData authData = serverFacade.register(registerRequest);
        CreateGameRequest createGameRequest = new CreateGameRequest("my game");
        serverFacade.createGame(createGameRequest, authData.authToken());
        ListGameResponse response = Assertions.assertDoesNotThrow( () -> serverFacade.listGames(authData.authToken()));
        Assertions.assertFalse(response.games().isEmpty());
    }

    @Test
    void joinGameSuccess() throws ResponseException {
        var registerRequest = new RegisterRequest("username", "password", "email");
        AuthData authData = serverFacade.register(registerRequest);
        CreateGameRequest createGameRequest = new CreateGameRequest("my game");
        CreateGameResponse createGameResponse =  serverFacade.createGame(createGameRequest, authData.authToken());
        JoinGameRequest request = new JoinGameRequest(ChessGame.TeamColor.WHITE, createGameResponse.gameID());
        Assertions.assertDoesNotThrow(() -> serverFacade.joinGame(request, authData.authToken()));
    }

    @Test
    void joinAsWhiteTwice() throws ResponseException {
        var registerRequest1 = new RegisterRequest("username", "password", "email");
        AuthData authData1 = serverFacade.register(registerRequest1);
        var registerRequest2 = new RegisterRequest("user2", "password", "email2");
        AuthData authData2 = serverFacade.register(registerRequest2);
        CreateGameRequest createGameRequest = new CreateGameRequest("my game");
        CreateGameResponse createGameResponse =  serverFacade.createGame(createGameRequest, authData1.authToken());
        JoinGameRequest request = new JoinGameRequest(ChessGame.TeamColor.WHITE, createGameResponse.gameID());
        serverFacade.joinGame(request, authData1.authToken());
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.joinGame(request, authData2.authToken()));
    }

}
