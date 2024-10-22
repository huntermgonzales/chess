import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.*;

import java.util.List;

public class LocalMemServiceTests {

    LocalMemory localMemory = new LocalMemory();

    @BeforeEach
    void beforeEach() {
        localMemory.deleteAllAuthData();
        localMemory.deleteAllGameData();
        localMemory.deleteAllUserData();
    }

    @Test
    public void RegisterOnce() {
        RegisterService registerService = new RegisterService(localMemory);
        Assertions.assertDoesNotThrow(() -> {
            registerService.register("usernameNoInUse", "password", "email");
        });

    }

    @Test
    void RegisterSameUsernameTwice() {
        RegisterService registerService = new RegisterService(localMemory);
        Assertions.assertDoesNotThrow(() -> {
            registerService.register("username1", "password1", "email1");
        });
        Assertions.assertThrows(DataAccessException.class, () -> {
            registerService.register("username1", "password2", "email2");
        });
    }

    @Test
    public void loginUserNoExist() {
        LoginService loginService = new LoginService(localMemory);
        Assertions.assertThrows(DataAccessException.class, () -> {
            loginService.login("myUsername", "myPassword");
        });
    }

    @Test
    public void loginInvalidPassword() throws DataAccessException {
        RegisterService registerService = new RegisterService(localMemory);
        LoginService loginService = new LoginService(localMemory);
        registerService.register("username", "password", "email");
        Assertions.assertThrows(DataAccessException.class, () ->{
            loginService.login("username", "wrongPassword");
        });
    }

    @Test
    public void loginSuccess() throws DataAccessException {
        RegisterService registerService = new RegisterService(localMemory);
        LoginService loginService = new LoginService(localMemory);
        registerService.register("username", "password", "email");
        Assertions.assertDoesNotThrow(() ->{
            loginService.login("username", "password");
        });
    }

    @Test
    public void logoutInvalid() throws DataAccessException {
        LogoutService logoutService = new LogoutService(localMemory);
        Assertions.assertThrows(DataAccessException.class, () -> {
            logoutService.logout("notValidAuthToken");
        });
    }

    @Test
    void logoutSuccess() throws DataAccessException {
        RegisterService registerService = new RegisterService(localMemory);
        LogoutService logoutService = new LogoutService(localMemory);
        AuthData authData = registerService.register("myUsername", "password", "email");
        logoutService.logout(authData.authToken());
    }

    @Test
    void logoutCannotLogoutTwice() throws DataAccessException {
        RegisterService registerService = new RegisterService(localMemory);
        LogoutService logoutService = new LogoutService(localMemory);
        AuthData authData = registerService.register("myUsername", "password", "email");
        logoutService.logout(authData.authToken());
        Assertions.assertThrows(DataAccessException.class, () -> {
            logoutService.logout(authData.authToken());
        });
    }

    @Test
    void CreateGameUnauthorizedUser() {
        CreateGameService gameService = new CreateGameService(localMemory);
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.createGame("not a real token", "myGame");
        });
    }

    @Test
    void CreateGameSuccess() throws DataAccessException {
        RegisterService registerService = new RegisterService(localMemory);
        CreateGameService createGameService = new CreateGameService(localMemory);
        AuthData authData = registerService.register("username", "password", "email");
        createGameService.createGame(authData.authToken(), "newGame");
    }

    @Test
    void ListGamesUnauthorizedUser() {
        ListGameService listGameService = new ListGameService(localMemory);
        Assertions.assertThrows(DataAccessException.class, () -> {
           listGameService.listGames("not a real token");
        });
    }

    @Test
    void List5Games() throws DataAccessException {
        RegisterService registerService = new RegisterService(localMemory);
        CreateGameService createGameService = new CreateGameService(localMemory);
        ListGameService listGameService = new ListGameService(localMemory);
        AuthData authData = registerService.register("username", "password", "email");
        createGameService.createGame(authData.authToken(), "Game1");
        createGameService.createGame(authData.authToken(), "Game2");
        createGameService.createGame(authData.authToken(), "Game3");
        createGameService.createGame(authData.authToken(), "Game4");
        createGameService.createGame(authData.authToken(), "Game5");
        List<GameData> games= listGameService.listGames(authData.authToken());
        Assertions.assertEquals(5, games.size());
    }

    @Test
    void JoinGameSuccess() throws DataAccessException {
        RegisterService registerService = new RegisterService(localMemory);
        CreateGameService createGameService = new CreateGameService(localMemory);
        JoinGameService joinGameService = new JoinGameService(localMemory);
        AuthData authData = registerService.register("username", "password", "email");
        createGameService.createGame(authData.authToken(), "Game1");
        joinGameService.joinGame(authData.authToken(), ChessGame.TeamColor.BLACK, 1);
    }

    @Test
    void JoinGameUnauthorized() throws DataAccessException {
        RegisterService registerService = new RegisterService(localMemory);
        CreateGameService createGameService = new CreateGameService(localMemory);
        JoinGameService joinGameService = new JoinGameService(localMemory);
        AuthData authData = registerService.register("username", "password", "email");
        createGameService.createGame(authData.authToken(), "Game1");
        Assertions.assertThrows(DataAccessException.class, () -> {
            joinGameService.joinGame("not a real authToken", ChessGame.TeamColor.BLACK, 1);
        });
    }

    @Test
    void JoinGameAddSameColorTwice() throws DataAccessException {
        RegisterService registerService = new RegisterService(localMemory);
        CreateGameService createGameService = new CreateGameService(localMemory);
        JoinGameService joinGameService = new JoinGameService(localMemory);
        AuthData authData1 = registerService.register("username1", "password", "email");
        AuthData authData2 = registerService.register("username2", "password", "email");
        createGameService.createGame(authData1.authToken(), "Game1");
        joinGameService.joinGame(authData1.authToken(), ChessGame.TeamColor.BLACK, 1);
        Assertions.assertThrows(DataAccessException.class, () -> {
            joinGameService.joinGame(authData2.authToken(), ChessGame.TeamColor.BLACK, 1);
        });
    }

    @Test
    void JoinGameGameDoesNotExist() throws DataAccessException {
        RegisterService registerService = new RegisterService(localMemory);
        CreateGameService createGameService = new CreateGameService(localMemory);
        JoinGameService joinGameService = new JoinGameService(localMemory);
        AuthData authData = registerService.register("username", "password", "email");
        createGameService.createGame(authData.authToken(), "Game1");
        Assertions.assertThrows(DataAccessException.class, () -> {
            joinGameService.joinGame(authData.authToken(), ChessGame.TeamColor.BLACK, 2);
        });
    }
}
