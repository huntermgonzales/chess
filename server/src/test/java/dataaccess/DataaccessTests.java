package dataaccess;

import chess.ChessGame;
import chess.InvalidMoveException;
import exceptions.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

public class DataaccessTests {

    @BeforeAll
    static void initializeSQLServer() throws DataAccessException {
        new MySQLAccess();
    }

    @BeforeEach
    void setUp() throws DataAccessException {
        new SQLAuthDAO().deleteAll();
        new SQLUserDAO().deleteAll();
        new SQLGameDAO().deleteAll();
    }


    @Test
    void addAuthData() throws DataAccessException {
        AuthDAO authDAO = new SQLAuthDAO();
        authDAO.addAuthData(new AuthData(UUID.randomUUID().toString(), "username"));
    }

    @Test
    void addAndRetrieveAuthData() throws DataAccessException {
        AuthDAO authDAO = new SQLAuthDAO();
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, "username");
        authDAO.addAuthData(authData);
        Assertions.assertEquals(authData, authDAO.getAuthData(authToken));
    }

    @Test
    void addTwoRetrieveFirstAuthData() throws DataAccessException {
        AuthDAO authDAO = new SQLAuthDAO();
        String authToken1 = UUID.randomUUID().toString();
        AuthData authData1 = new AuthData(authToken1, "username1");
        authDAO.addAuthData(authData1);
        String authToken2 = UUID.randomUUID().toString();
        AuthData authData2 = new AuthData(authToken2, "username2");
        authDAO.addAuthData(authData2);
        Assertions.assertEquals(authData1, authDAO.getAuthData(authToken1));
    }

    @Test
    void addDuplicateAuthToken() throws DataAccessException {
        AuthDAO authDAO = new SQLAuthDAO();
        String authToken = UUID.randomUUID().toString();
        authDAO.addAuthData(new AuthData(authToken, "username"));
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.addAuthData(new AuthData(authToken, "username1")));
    }

    @Test
    void deleteAuthData() throws DataAccessException {
        AuthDAO authDAO = new SQLAuthDAO();
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, "username");
        authDAO.addAuthData(authData);
        authDAO.deleteAuthData(authToken);
        Assertions.assertNull(authDAO.getAuthData(authToken));
    }

    @Test
    void addUserData() throws DataAccessException {
        UserDAO userDAO = new SQLUserDAO();
        Assertions.assertDoesNotThrow(() -> userDAO.addUser(new UserData("username", "password", "email")));
    }

    @Test
    void addAndGetUserData() throws DataAccessException {
        UserDAO userDAO = new SQLUserDAO();
        String username = "username";
        String password = BCrypt.hashpw("password", BCrypt.gensalt());
        String email = "email";
        UserData userData = new UserData(username, password, email);
        userDAO.addUser(userData);
        UserData returnedUserData = userDAO.getUserData(username);
        Assertions.assertEquals(userData, returnedUserData);
    }

    @Test
    void addGame() {
        GameDAO gameDAO = new SQLGameDAO();
        GameData gameData = new GameData(null, null, null, "game", new ChessGame());
        Assertions.assertDoesNotThrow(() ->gameDAO.addGame(gameData));
    }

    @Test
    void addAndRetrieveGame() throws DataAccessException {
        GameDAO gameDAO = new SQLGameDAO();
        String gameName = "game";
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(null, null, null, gameName, game);
        int gameID = gameDAO.addGame(gameData);
        gameData = new GameData(gameID, null, null, gameName, game);
        Assertions.assertEquals(gameData, gameDAO.getGame(gameID));
    }

    @Test
    void addGameNullName() {
        GameDAO gameDAO = new SQLGameDAO();
        GameData gameData = new GameData(1, null, null, null, new ChessGame());
        Assertions.assertThrows(DataAccessException.class, ()->gameDAO.addGame(gameData));
    }

    @Test
    void editGame() throws DataAccessException, InvalidMoveException {
        GameDAO gameDAO = new SQLGameDAO();
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(null, null, null, "game", game);
        int gameID = gameDAO.addGame(gameData);
        Assertions.assertDoesNotThrow(() -> gameDAO.updateGameData(new GameData(gameID, "user1",
                null, "game", game)));
    }

    @Test
    void editGameDoesNotExist() {
        GameDAO gameDAO = new SQLGameDAO();
        GameData gameData = new GameData(1, null, null, "game", new ChessGame());
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.updateGameData(gameData));
    }

    @Test
    void listOneGame() throws DataAccessException {
        GameDAO gameDAO = new SQLGameDAO();
        String gameName = "game";
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(null, null, null, gameName, game);
        int gameID = gameDAO.addGame(gameData);
        gameData = new GameData(gameID, null, null, gameName, game);
        Assertions.assertEquals(gameData, gameDAO.listAllGames().getFirst());
    }

    @Test
    void listTwoGames() throws DataAccessException {
        GameDAO gameDAO = new SQLGameDAO();
        String gameName = "game";
        ChessGame game = new ChessGame();
        GameData gameData1 = new GameData(null, null, null, gameName, game);
        int gameID1 = gameDAO.addGame(gameData1);
        gameData1 = new GameData(gameID1, null, null, gameName, game);
        GameData gameData2 = new GameData(null, null, null, gameName, game);
        int gameID2 = gameDAO.addGame(gameData2);
        gameData2 = new GameData(gameID2, null, null, gameName, game);
        Assertions.assertEquals(gameData1, gameDAO.listAllGames().getFirst());
        Assertions.assertEquals(gameData2, gameDAO.listAllGames().get(1));
    }

    @Test
    void listGamesEmpty() {
        GameDAO gameDAO = new SQLGameDAO();
        Assertions.assertDoesNotThrow(() -> gameDAO.listAllGames());
    }
}
