import dataaccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.LoginService;

public class ServiceTests {

    @Test
    public void loginServicUserNoExist() {
        LocalMemory localMemory = new LocalMemory();
        LoginService loginService = new LoginService(localMemory);
        Assertions.assertThrows(DataAccessException.class, () -> {
            loginService.login("myUsername", "myPassword");
        });

    }

    @Test
    void addAuthDataUserNoExist() {
        LocalMemory localMemory = new LocalMemory();
        LoginService loginService = new LoginService(localMemory);
        Assertions.assertThrows(DataAccessException.class, () -> {
            loginService.createAuthData("unusedUsername");
        });
    }
}
