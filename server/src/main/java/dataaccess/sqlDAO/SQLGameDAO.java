package dataaccess.sqlDAO;

import com.google.gson.Gson;
import dataaccess.GameDAO;
import dataaccess.exceptions.DataAccessException;
import model.GameData;

import java.util.List;

public class SQLGameDAO implements GameDAO {

    @Override
    public List<GameData> listAllGames() {
        return List.of();
    }

    @Override
    public Integer addGame(GameData gameData) throws DataAccessException {
        var statement = "INSERT INTO gameData (whiteUsername, blackUsername, gameName, gameJson) VALUES (?, ?, ?, ?)";
        var gameJson = new Gson().toJson(gameData.game());
        var id = MySQLAccess.executeUpdate(statement, gameData.whiteUsername(), gameData.blackUsername(),
                gameData.gameName(), gameJson);
        return id;
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public void updateGameData(GameData gameData) {

    }

    @Override
    public void deleteAll() throws DataAccessException {
        var statement = "TRUNCATE gameData";
        MySQLAccess.executeUpdate(statement);
    }
}
