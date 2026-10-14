package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.*;

public class MemoryGameDAO implements GameDAO {
    private final Map<Integer, GameData> allGameData = new HashMap<>();

    @Override
    public void createGame(GameData game) throws DataAccessException {
        int gameID = game.gameID();
        if (allGameData.containsKey(gameID)) {
            throw new DataAccessException("gameID already exists");
        }
        allGameData.put(gameID, game);
    }

    @Override
    public GameData getGame(int gameID) {
        return allGameData.get(gameID);
    }

    @Override
    public Collection<GameData> listGames() {
        return new ArrayList<>(allGameData.values());
    }

    @Override
    public void updateGame(int gameID, ChessGame.TeamColor playerColor, String username) throws DataAccessException {
        if (!allGameData.containsKey(gameID)) {
            throw new DataAccessException("Game doesn't exist");
        }
        GameData data = allGameData.get(gameID);
        String newBlackUsername;
        String newWhiteUsername;
        if (playerColor == ChessGame.TeamColor.BLACK) {
            newBlackUsername = username;
            newWhiteUsername = data.whiteUsername();
        } else {
            newBlackUsername = data.blackUsername();
            newWhiteUsername = username;
        }
        GameData newGameData = new GameData(gameID, newWhiteUsername, newBlackUsername, data.gameName(), data.game());
        allGameData.put(gameID, newGameData);
    }

    @Override
    public void clear() { allGameData.clear(); }
}
