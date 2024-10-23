package dataaccess;

import model.GameData;

import java.util.List;

public class MemoryGameDAO implements GameDAO{

    LocalMemory localMemory = LocalMemory.getInstance();

    @Override
    public List<GameData> listAllGames() {
        return localMemory.getAllGames();
    }

    @Override
    public void addGame(GameData gameData) {
        localMemory.createGameData(gameData);
    }

    @Override
    public GameData getGame(int gameID) {
        return localMemory.getGameData(gameID);
    }

    @Override
    public void updateGameData(GameData gameData) {
        localMemory.updateGameData(gameData);
    }

    @Override
    public int getNumberOfGames() {
        return localMemory.getNumberOfGames();
    }

    @Override
    public void deleteAll() {
        localMemory.deleteAllGameData();
    }
}
