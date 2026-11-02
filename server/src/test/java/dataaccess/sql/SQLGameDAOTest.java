package dataaccess.sql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class SQLGameDAOTest {

    private SQLGameDAO gameDAO;
    private GameData fakeGame;

    @BeforeEach
    void setUp() throws DataAccessException {
        gameDAO = new SQLGameDAO();
        gameDAO.clear();
        fakeGame = new GameData(1, null, null, "New Game", new ChessGame());
    }

    @Test
    void createGameSuccess() throws DataAccessException {
        int gameID = gameDAO.createGame(fakeGame);
        GameData newGame = gameDAO.getGame(gameID);
        assertNotNull(newGame);
        assertEquals(gameID, newGame.gameID());
        assertEquals(fakeGame.whiteUsername(), newGame.whiteUsername());
        assertEquals(fakeGame.blackUsername(), newGame.blackUsername());
        assertEquals(fakeGame.gameName(), newGame.gameName());
        assertEquals(fakeGame.game(), newGame.game());
    }

    @Test
    void createGameNull() {
        GameData nullGame = new GameData(1, "cosmo1", "cosmo2", null, new ChessGame());
        assertThrows(DataAccessException.class, () -> gameDAO.createGame(nullGame));
    }

    @Test
    void getGameSuccess() throws DataAccessException {
        gameDAO.createGame(fakeGame);
        GameData newGame = gameDAO.getGame(1);
        assertNotNull(newGame);
        assertEquals(1, newGame.gameID());
    }

    @Test
    void getGameDoesNotExist() throws DataAccessException {
        GameData newGame = gameDAO.getGame(1);
        assertNull(newGame);
    }

    @Test
    void listGamesNormal() throws DataAccessException {
        gameDAO.createGame(fakeGame);
        gameDAO.createGame(new GameData(1, "cosmo1", "cosmo2", "cosmoGame", new ChessGame()));
        gameDAO.createGame(new GameData(1, "me", "you", "Our Game", new ChessGame()));
        Collection<GameData> allGames = gameDAO.listGames();
        assertEquals(3, allGames.size());
        Collection<String> gameNames = allGames.stream().map(GameData::gameName).toList();
        assertTrue(gameNames.contains("New Game"));
        assertTrue(gameNames.contains("cosmoGame"));
        assertTrue(gameNames.contains("Our Game"));
    }

    @Test
    void listGamesEmpty() throws DataAccessException {
        Collection<GameData> allGames = gameDAO.listGames();
        assertTrue(allGames.isEmpty());
    }

    @Test
    void updateGameWhite() throws DataAccessException {
        gameDAO.createGame(fakeGame);
        gameDAO.updateGame(123, ChessGame.TeamColor.WHITE, "newWhitePlayer");
        GameData updatedGame = gameDAO.getGame(123);
        assertEquals("newWhitePlayer", updatedGame.whiteUsername());
        assertNull(updatedGame.blackUsername());
    }

    @Test
    void updateGameBlack() throws DataAccessException {
        gameDAO.createGame(fakeGame);
        gameDAO.updateGame(123, ChessGame.TeamColor.BLACK, "newBlackPlayer");
        GameData updatedGame = gameDAO.getGame(123);
        assertEquals("newBlackPlayer", updatedGame.blackUsername());
        assertNull(updatedGame.whiteUsername());
    }

    @Test
    void updateGameDoesNotExist() {
        DataAccessException deleteException = assertThrows(DataAccessException.class, () -> gameDAO.updateGame(5, ChessGame.TeamColor.WHITE, "myUsername"));
        assertEquals("Game doesn't exist", deleteException.getMessage());
    }

    @Test
    void clear() throws DataAccessException {
        gameDAO.createGame(fakeGame);
        gameDAO.createGame(new GameData(24, "bob", "pat", "Bob and Pat Game", new ChessGame()));
        assertNotNull(gameDAO.getGame(123));
        assertNotNull(gameDAO.getGame(24));
        gameDAO.clear();
        assertNull(gameDAO.getGame(123));
        assertNull(gameDAO.getGame(24));
    }
}