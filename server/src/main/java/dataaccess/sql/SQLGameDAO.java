package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import model.GameData;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static dataaccess.sql.SQLUpdate.executeUpdate;

public class SQLGameDAO implements GameDAO {
    public SQLGameDAO() throws DataAccessException {
        DatabaseInitializer.configureDatabase();
    }

    @Override
    public int createGame(GameData game) throws DataAccessException {
        var statement = "INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        String json = new Gson().toJson(game.game());
        return executeUpdate(statement, game.whiteUsername(), game.blackUsername(), game.gameName(), json);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games WHERE gameID=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to read data: %s", ex.getMessage()), ex);
        }
        return null;
    }

    @NotNull
    private static GameData readGame(ResultSet rs) throws SQLException {
        var foundGameID = rs.getInt("gameID");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");
        var game = rs.getString("game");
        ChessGame newGame = new Gson().fromJson(game, ChessGame.class);
        return new GameData(foundGameID, whiteUsername, blackUsername, gameName, newGame);
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        Collection<GameData> gameList = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        gameList.add(readGame(rs));
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to read data: %s", ex.getMessage()), ex);
        }
        return gameList;
    }

    @Override
    public void updateGame(int gameID, ChessGame.TeamColor playerColor, String username) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE games";
        executeUpdate(statement);
    }
}
