import results.ListGamesResult;
import results.RegisterResult;
import chess.ChessGame;
import dataaccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.*;
import service.*;

public class LocalMemServiceTests {

    @BeforeEach
    void beforeEach() throws DataAccessException {
        new ClearService().clearAll();
    }

    @Test
    public void registerOnce() {
        RegisterService registerService = new RegisterService();
        Assertions.assertDoesNotThrow(() -> {
            registerService.register(new RegisterRequest("usernameNoInUse", "password", "email"));
        });

    }

    @Test
    void registerSameUsernameTwice() {
        RegisterService registerService = new RegisterService();
        Assertions.assertDoesNotThrow(() -> {
            registerService.register(new RegisterRequest("username1", "password1", "email1"));
        });
        Assertions.assertThrows(AlreadyTakenException.class, () -> {
            registerService.register(new RegisterRequest("username1", "password2", "email2"));
        });
    }

    @Test
    public void loginUserNoExist() {
        LoginService loginService = new LoginService();
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            loginService.login(new LoginRequest("myUsername", "myPassword"));
        });
    }

    @Test
    public void loginInvalidPassword() throws DataAccessException {
        RegisterService registerService = new RegisterService();
        LoginService loginService = new LoginService();
        registerService.register(new RegisterRequest("username", "password", "email"));
        Assertions.assertThrows(UnauthorizedException.class, () ->{
            loginService.login(new LoginRequest("username", "wrongPassword"));
        });
    }

    @Test
    public void loginSuccess() throws DataAccessException {
        RegisterService registerService = new RegisterService();
        LoginService loginService = new LoginService();
        registerService.register(new RegisterRequest("username", "password", "email"));
        Assertions.assertDoesNotThrow(() ->{
            loginService.login(new LoginRequest("username", "password"));
        });
    }

    @Test
    public void logoutInvalid() {
        LogoutService logoutService = new LogoutService();
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            logoutService.logout("notValidAuthToken");
        });
    }

    @Test
    void logoutSuccess() throws DataAccessException {
        RegisterService registerService = new RegisterService();
        LogoutService logoutService = new LogoutService();
        RegisterResult registerResult = registerService.register(new RegisterRequest("myUsername", "password", "email"));
        logoutService.logout(registerResult.authToken());
    }

    @Test
    void logoutCannotLogoutTwice() throws DataAccessException {
        RegisterService registerService = new RegisterService();
        LogoutService logoutService = new LogoutService();
        RegisterResult registerResult = registerService.register(new RegisterRequest("myUsername", "password", "email"));
        logoutService.logout(registerResult.authToken());
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            logoutService.logout(registerResult.authToken());
        });
    }

    @Test
    void createGameUnauthorizedUser() {
        CreateGameService gameService = new CreateGameService();
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            gameService.createGame(new CreateGameRequest("myGame"), "not a real token");
        });
    }

    @Test
    void createGameSuccess() throws DataAccessException {
        RegisterService registerService = new RegisterService();
        CreateGameService createGameService = new CreateGameService();
        RegisterResult registerResult = registerService.register(new RegisterRequest("username", "password", "email"));
        createGameService.createGame(new CreateGameRequest("newGame"), registerResult.authToken());
    }

    @Test
    void listGamesUnauthorizedUser() {
        ListGameService listGameService = new ListGameService();
        Assertions.assertThrows(UnauthorizedException.class, () -> {
           listGameService.listGames("not a real token");
        });
    }

    @Test
    void list5Games() throws DataAccessException {
        RegisterService registerService = new RegisterService();
        CreateGameService createGameService = new CreateGameService();
        ListGameService listGameService = new ListGameService();
        RegisterResult registerResult = registerService.register(new RegisterRequest("username", "password", "email"));
        createGameService.createGame(new CreateGameRequest("Game1"), registerResult.authToken());
        createGameService.createGame(new CreateGameRequest("Game2"), registerResult.authToken());
        createGameService.createGame(new CreateGameRequest("Game3"), registerResult.authToken());
        createGameService.createGame(new CreateGameRequest("Game4"), registerResult.authToken());
        createGameService.createGame(new CreateGameRequest("Game5"), registerResult.authToken());
        ListGamesResult games = listGameService.listGames(registerResult.authToken());
        Assertions.assertEquals(5, games.games().size());
    }

    @Test
    void joinGameSuccess() throws DataAccessException {
        RegisterService registerService = new RegisterService();
        CreateGameService createGameService = new CreateGameService();
        JoinGameService joinGameService = new JoinGameService();
        RegisterResult registerResult = registerService.register(new RegisterRequest("username", "password", "email"));
        createGameService.createGame(new CreateGameRequest("Game1"), registerResult.authToken());
        joinGameService.joinGame(registerResult.authToken(), new JoinGameRequest(ChessGame.TeamColor.BLACK, 1));
    }

    @Test
    void joinGameUnauthorized() throws DataAccessException {
        RegisterService registerService = new RegisterService();
        CreateGameService createGameService = new CreateGameService();
        JoinGameService joinGameService = new JoinGameService();
        RegisterResult registerResult = registerService.register(new RegisterRequest("username", "password", "email"));
        createGameService.createGame(new CreateGameRequest("Game1"), registerResult.authToken());
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            joinGameService.joinGame("not a real authToken", new JoinGameRequest(ChessGame.TeamColor.BLACK, 1));
        });
    }

    @Test
    void joinGameAddSameColorTwice() throws DataAccessException {
        RegisterService registerService = new RegisterService();
        CreateGameService createGameService = new CreateGameService();
        JoinGameService joinGameService = new JoinGameService();
        RegisterResult registerResult1 = registerService.register(new RegisterRequest("username1", "password", "email"));
        RegisterResult registerResult2 = registerService.register(new RegisterRequest("username2", "password", "email"));
        createGameService.createGame(new CreateGameRequest("Game1"), registerResult1.authToken());
        joinGameService.joinGame(registerResult1.authToken(), new JoinGameRequest(ChessGame.TeamColor.BLACK, 1));
        Assertions.assertThrows(AlreadyTakenException.class, () -> {
            joinGameService.joinGame(registerResult2.authToken(), new JoinGameRequest(ChessGame.TeamColor.BLACK, 1));
        });
    }

    @Test
    void joinGameGameDoesNotExist() throws DataAccessException {
        RegisterService registerService = new RegisterService();
        CreateGameService createGameService = new CreateGameService();
        JoinGameService joinGameService = new JoinGameService();
        RegisterResult registerResult = registerService.register(new RegisterRequest("username", "password", "email"));
        createGameService.createGame(new CreateGameRequest("Game1"), registerResult.authToken());
        Assertions.assertThrows(BadRequestException.class, () -> {
            joinGameService.joinGame(registerResult.authToken(), new JoinGameRequest(ChessGame.TeamColor.BLACK, 2));
        });
    }

    @Test
    void clearAll() throws DataAccessException {
        joinGameSuccess();
        ClearService clearService = new ClearService();
        clearService.clearAll();

    }
}
