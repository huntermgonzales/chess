package dataaccess;

import exceptions.DataAccessException;
import model.GameData;

import java.util.List;

public interface GameDAO extends DataAccess{
    public List<GameData> listAllGames() throws DataAccessException;

    public Integer addGame(GameData gameData) throws DataAccessException;

    public GameData getGame(int gameID) throws DataAccessException;

    public void updateGameData(GameData gameData) throws DataAccessException;

}
