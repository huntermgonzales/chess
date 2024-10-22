package dataaccess;

import model.GameData;

import java.util.List;

public interface GameDAO extends DataAccess{
    public List<GameData> listAllGames();

    public void addGame(GameData gameData);

    public GameData getGame(int gameID);

    public void updateGameData(GameData gameData);
}
