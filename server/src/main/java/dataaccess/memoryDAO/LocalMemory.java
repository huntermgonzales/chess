package dataaccess.memoryDAO;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalMemory {
    private Map<String, AuthData> authDataMap = new HashMap<>(); //string AuthToken is key
    private Map<String, UserData> userDataMap = new HashMap<>(); //string username is key
    private Map<Integer, GameData> gameDataMap = new HashMap<>(); //int gameID is key
    int nextGameID = 1;

    private static LocalMemory instance;

    static LocalMemory getInstance() {
        if (instance == null) {
            instance = new LocalMemory();
        }
        return instance;
    }

    public void createAuthData(AuthData authData) {
        authDataMap.put(authData.authToken(), authData);
    }

    public AuthData getAuthData(String authToken) {
        return authDataMap.get(authToken);
    }

    public void deleteAuthData(String authToken) {
        authDataMap.remove(authToken);
    }

    public void deleteAllAuthData() {
        authDataMap.clear();
    }



    public void createUserData(UserData userData) {
        userDataMap.put(userData.username(), userData);
    }

    public UserData getUserData(String username) {
        return userDataMap.get(username);
    }

    public void deleteAllUserData() {
        userDataMap.clear();
    }


    public Integer createGameData(GameData gameData) {
        Integer gameID = nextGameID;
        nextGameID++;
        GameData newGameData = new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(),
                gameData.gameName(), gameData.game());
        gameDataMap.put(newGameData.gameID(), newGameData);
        return gameID;
    }

    public GameData getGameData(int gameID) {
        return gameDataMap.get(gameID);
    }

    public List<GameData> getAllGames() {
        List<GameData> games = new ArrayList<>();
        games.addAll(gameDataMap.values());
        return games;
    }

    public void updateGameData(GameData gameData) {
        gameDataMap.remove(gameData.gameID());
        gameDataMap.put(gameData.gameID(), gameData);
    }

    public int getNumberOfGames() {
        return gameDataMap.size();
    }

    public void deleteAllGameData() {
        gameDataMap.clear();
    }

}
