package dataaccess;

import dataaccess.exceptions.DataAccessException;
import model.GameData;

import java.util.List;

public interface GameDAO extends DataAccess{
    public List<GameData> listAllGames();

    public Integer addGame(GameData gameData) throws DataAccessException;

    public GameData getGame(int gameID);

    public void updateGameData(GameData gameData);

}
