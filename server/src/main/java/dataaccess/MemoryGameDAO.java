package dataaccess;

import model.GameData;

import java.util.List;

public class MemoryGameDAO implements GameDAO{

    LocalMemory localMemory;
    public MemoryGameDAO(LocalMemory localMemory) {
        this.localMemory = localMemory;
    }

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
    public void deleteAll() {
        localMemory.deleteAllGameData();
    }
}