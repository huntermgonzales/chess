import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.LoginService;
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
    void loginInvalidPassword() {
    }

    @Test
    void addAuthDataUserNoExist() {
        LoginService loginService = new LoginService(localMemory);
        Assertions.assertThrows(DataAccessException.class, () -> {
            loginService.createAuthData("unusedUsername");
        });
    }

    @Test
    void addAuthDataUserExists() {
        LoginService loginService = new LoginService(localMemory);
        UserData userData = new UserData("newUsername", "password", "email");
        localMemory.createUserData(userData);
        Assertions.assertDoesNotThrow(() -> {
            loginService.createAuthData("newUsername");
        });
    }
}
