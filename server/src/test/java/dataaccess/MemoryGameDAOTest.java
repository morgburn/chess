package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class MemoryGameDAOTest {

    private MemoryGameDAO gameDAO;
    private GameData fakeGame;

    @BeforeEach
    void setUp() {
        gameDAO = new MemoryGameDAO();
        fakeGame = new GameData(123, null, null, "New Game", new ChessGame());
    }

    @Test
    void createGameSuccess() {
        assertDoesNotThrow(() -> gameDAO.createGame(fakeGame));
        GameData newGame = gameDAO.getGame(123);
        assertNotNull(newGame);
        assertEquals(fakeGame, newGame);
    }

    @Test
    void createGameAlreadyExists() {
        assertDoesNotThrow(() -> gameDAO.createGame(fakeGame));
        GameData sameGame = new GameData(123, "cosmo1", "cosmo2", "cosmoGame", new ChessGame());
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            gameDAO.createGame(sameGame);
        });
        assertEquals("gameID already exists", exception.getMessage());
    }

    @Test
    void getGameSuccess() {
        assertDoesNotThrow(() -> gameDAO.createGame(fakeGame));
        GameData newGame = gameDAO.getGame(123);
        assertNotNull(newGame);
        assertEquals(123, newGame.gameID());
    }

    @Test
    void getGameDoesNotExist() {
        GameData newGame = gameDAO.getGame(321);
        assertNull(newGame);
    }

    @Test
    void listGamesNormal() {
        assertDoesNotThrow(() -> gameDAO.createGame(fakeGame));
        assertDoesNotThrow(() -> gameDAO.createGame(new GameData(321, "cosmo1", "cosmo2", "cosmoGame", new ChessGame())));
        assertDoesNotThrow(() -> gameDAO.createGame(new GameData(1, "me", "you", "Our Game", new ChessGame())));
        Collection<GameData> allGames = gameDAO.listGames();
        assertEquals(3, allGames.size());
    }

    @Test
    void listGamesEmpty() {
        Collection<GameData> allGames = gameDAO.listGames();
        assertTrue(allGames.isEmpty());
    }

    @Test
    void updateGameWhite() {
        assertDoesNotThrow(() -> gameDAO.createGame(fakeGame));
        assertDoesNotThrow(() -> gameDAO.updateGame(123, ChessGame.TeamColor.WHITE, "newWhitePlayer"));
        GameData updatedGame = gameDAO.getGame(123);
        assertEquals("newWhitePlayer", updatedGame.whiteUsername());
        assertNull(updatedGame.blackUsername());
    }

    @Test
    void updateGameBlack() {
        assertDoesNotThrow(() -> gameDAO.createGame(fakeGame));
        assertDoesNotThrow(() -> gameDAO.updateGame(123, ChessGame.TeamColor.BLACK, "newBlackPlayer"));
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
    void clear() {
        assertDoesNotThrow(() -> gameDAO.createGame(fakeGame));
        assertDoesNotThrow(() -> gameDAO.createGame(new GameData(24, "bob", "pat", "Bob and Pat Game", new ChessGame())));
        assertNotNull(gameDAO.getGame(123));
        assertNotNull(gameDAO.getGame(24));
        gameDAO.clear();
        assertNull(gameDAO.getGame(123));
        assertNull(gameDAO.getGame(24));
    }
}