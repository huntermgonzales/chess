package dataaccess.sqlDAO;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.GameDAO;
import dataaccess.exceptions.DataAccessException;
import model.AuthData;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLGameDAO implements GameDAO {

    @Override
    public List<GameData> listAllGames() throws DataAccessException {
        List<GameData> games = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, gameJson FROM gameData";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        games.add(readGameData(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Error: unable to get data from database");
        }
        return games;
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
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, gameJson FROM gameData WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGameData(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Error: unable to get data from database");
        }
        return null;
    }

    private GameData readGameData(ResultSet rs) throws SQLException {
        var gameID = rs.getInt("gameID");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");
        var gameJson = rs.getString("gameJson");
        var game = new Gson().fromJson(gameJson, ChessGame.class);
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }

    @Override
    public void updateGameData(GameData gameData) throws DataAccessException {
        var statement = """
                        UPDATE gameData
                        SET whiteUsername = ?, blackUsername = ?, gameName = ?, gameJson = ?
                        WHERE gameID = ?;
                        """;
        var gameJson = new Gson().toJson(gameData.game());
        int numRowsUpdated = MySQLAccess.executeUpdate(statement, gameData.whiteUsername(), gameData.blackUsername(),
                gameData.gameName(), gameJson, gameData.gameID());

        if (numRowsUpdated == 0) {
            throw new DataAccessException("Error: gameData does not exist");
        }
    }


    @Override
    public void deleteAll() throws DataAccessException {
        var statement = "TRUNCATE gameData";
        MySQLAccess.executeUpdate(statement);
    }
}
