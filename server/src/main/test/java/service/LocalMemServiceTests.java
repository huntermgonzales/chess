import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.LoginRequest;
import requests.RegisterRequest;
import service.*;

import java.util.List;

public class LocalMemServiceTests {

    @BeforeEach
    void beforeEach() {
        new ClearService().clearAll();
    }

    @Test
    public void RegisterOnce() {
        RegisterService registerService = new RegisterService();
        Assertions.assertDoesNotThrow(() -> {
            registerService.register(new RegisterRequest("usernameNoInUse", "password", "email"));
        });

    }

    @Test
    void RegisterSameUsernameTwice() {
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
        AuthData authData = registerService.register(new RegisterRequest("myUsername", "password", "email"));
        logoutService.logout(authData.authToken());
    }

    @Test
    void logoutCannotLogoutTwice() throws DataAccessException {
        RegisterService registerService = new RegisterService();
        LogoutService logoutService = new LogoutService();
        AuthData authData = registerService.register(new RegisterRequest("myUsername", "password", "email"));
        logoutService.logout(authData.authToken());
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            logoutService.logout(authData.authToken());
        });
    }

    @Test
    void CreateGameUnauthorizedUser() {
        CreateGameService gameService = new CreateGameService();
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            gameService.createGame(new CreateGameRequest("not a real token", "myGame"));
        });
    }

    @Test
    void CreateGameSuccess() throws DataAccessException {
        RegisterService registerService = new RegisterService();
        CreateGameService createGameService = new CreateGameService();
        AuthData authData = registerService.register(new RegisterRequest("username", "password", "email"));
        createGameService.createGame(new CreateGameRequest(authData.authToken(), "newGame"));
    }

    @Test
    void ListGamesUnauthorizedUser() {
        ListGameService listGameService = new ListGameService();
        Assertions.assertThrows(UnauthorizedException.class, () -> {
           listGameService.listGames("not a real token");
        });
    }

    @Test
    void List5Games() throws DataAccessException {
        RegisterService registerService = new RegisterService();
        CreateGameService createGameService = new CreateGameService();
        ListGameService listGameService = new ListGameService();
        AuthData authData = registerService.register(new RegisterRequest("username", "password", "email"));
        createGameService.createGame(new CreateGameRequest(authData.authToken(), "Game1"));
        createGameService.createGame(new CreateGameRequest(authData.authToken(), "Game2"));
        createGameService.createGame(new CreateGameRequest(authData.authToken(), "Game3"));
        createGameService.createGame(new CreateGameRequest(authData.authToken(), "Game4"));
        createGameService.createGame(new CreateGameRequest(authData.authToken(), "Game5"));
        List<GameData> games= listGameService.listGames(authData.authToken());
        Assertions.assertEquals(5, games.size());
    }

    @Test
    void JoinGameSuccess() throws DataAccessException {
        RegisterService registerService = new RegisterService();
        CreateGameService createGameService = new CreateGameService();
        JoinGameService joinGameService = new JoinGameService();
        AuthData authData = registerService.register(new RegisterRequest("username", "password", "email"));
        createGameService.createGame(new CreateGameRequest(authData.authToken(), "Game1"));
        joinGameService.joinGame(new JoinGameRequest(authData.authToken(), ChessGame.TeamColor.BLACK, 1));
    }

    @Test
    void JoinGameUnauthorized() throws DataAccessException {
        RegisterService registerService = new RegisterService();
        CreateGameService createGameService = new CreateGameService();
        JoinGameService joinGameService = new JoinGameService();
        AuthData authData = registerService.register(new RegisterRequest("username", "password", "email"));
        createGameService.createGame(new CreateGameRequest(authData.authToken(), "Game1"));
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            joinGameService.joinGame(new JoinGameRequest("not a real authToken", ChessGame.TeamColor.BLACK, 1));
        });
    }

    @Test
    void JoinGameAddSameColorTwice() throws DataAccessException {
        RegisterService registerService = new RegisterService();
        CreateGameService createGameService = new CreateGameService();
        JoinGameService joinGameService = new JoinGameService();
        AuthData authData1 = registerService.register(new RegisterRequest("username1", "password", "email"));
        AuthData authData2 = registerService.register(new RegisterRequest("username2", "password", "email"));
        createGameService.createGame(new CreateGameRequest(authData1.authToken(), "Game1"));
        joinGameService.joinGame(new JoinGameRequest(authData1.authToken(), ChessGame.TeamColor.BLACK, 1));
        Assertions.assertThrows(AlreadyTakenException.class, () -> {
            joinGameService.joinGame(new JoinGameRequest( authData2.authToken(), ChessGame.TeamColor.BLACK, 1));
        });
    }

    @Test
    void JoinGameGameDoesNotExist() throws DataAccessException {
        RegisterService registerService = new RegisterService();
        CreateGameService createGameService = new CreateGameService();
        JoinGameService joinGameService = new JoinGameService();
        AuthData authData = registerService.register(new RegisterRequest("username", "password", "email"));
        createGameService.createGame(new CreateGameRequest(authData.authToken(), "Game1"));
        Assertions.assertThrows(BadRequestException.class, () -> {
            joinGameService.joinGame(new JoinGameRequest(authData.authToken(), ChessGame.TeamColor.BLACK, 2));
        });
    }

    @Test
    void ClearAll() throws DataAccessException {
        JoinGameSuccess();
        ClearService clearService = new ClearService();
        clearService.clearAll();

    }
}
