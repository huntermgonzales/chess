import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.LoginService;
import service.LogoutService;
import service.RegisterService;

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
}
