package client;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Server;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    public void clearServer() throws ResponseException {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    void registerSuccess() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void registerFailAlreadyRegistered() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        ResponseException exception = assertThrows(ResponseException.class,
                () -> facade.register("player1", "password1", "p12@email.com"));
        assertEquals(403, exception.statusCode());
    }

    @Test
    void loginSuccess() throws Exception {
        var registerData = facade.register("player1", "password", "p1@email.com");
        var loginData = facade.login("player1", "password");
        assertTrue(loginData.authToken().length() > 10);
        assertEquals("player1", loginData.username());
        assertNotEquals(registerData.authToken(), loginData.authToken());
    }

    @Test
    void loginNotRegistered() {
        ResponseException exception = assertThrows(ResponseException.class,
                () -> facade.login("player1", "password"));
        assertEquals(401, exception.statusCode());
    }

    @Test
    void loginWrongPassword() throws ResponseException {
        facade.register("player1", "password", "p1@email.com");
        ResponseException exception = assertThrows(ResponseException.class,
                () -> facade.login("player1", "wrongPassword"));
        assertEquals(401, exception.statusCode());
    }

    @Test
    void logoutSuccess() throws ResponseException {
        facade.register("player1", "password", "p1@email.com");
        assertDoesNotThrow(() -> facade.logout());
    }

    @Test
    void logoutNotRegistered() {
        ResponseException exception = assertThrows(ResponseException.class,
                () -> facade.logout());
        assertEquals(401, exception.statusCode());
    }

    @Test
    void createGameSuccess() throws ResponseException {
        facade.register("player1", "password", "p1@email.com");
        int returnedGameID = facade.createGame("testGame");
        assertTrue(returnedGameID > 0);
    }

    @Test
    void createGameNotRegistered() {
        ResponseException exception = assertThrows(ResponseException.class,
                () -> facade.createGame("testGame"));
        assertEquals(401, exception.statusCode());
    }

    @Test
    void joinGameSuccess() throws ResponseException {
        facade.register("player1", "password", "p1@email.com");
        int returnedGameID = facade.createGame("testGame");
        assertDoesNotThrow(() -> facade.joinGame(ChessGame.TeamColor.WHITE, returnedGameID));
    }

    @Test
    void joinGameSuccessBothPlayers() throws ResponseException {
        facade.register("player1", "password", "p1@email.com");
        facade.register("player2", "password2", "p2@email.com");
        int returnedGameID = facade.createGame("testGame");
        assertDoesNotThrow(() -> facade.joinGame(ChessGame.TeamColor.WHITE, returnedGameID));
        assertDoesNotThrow(() -> facade.joinGame(ChessGame.TeamColor.BLACK, returnedGameID));
    }

    @Test
    void joinGameAlreadyTaken() throws ResponseException {
        facade.register("player1", "password", "p1@email.com");
        int returnedGameID = facade.createGame("testGame");
        facade.joinGame(ChessGame.TeamColor.WHITE, returnedGameID);
        ResponseException exception = assertThrows(ResponseException.class,
                () -> facade.joinGame(ChessGame.TeamColor.WHITE, returnedGameID));
        assertEquals(403, exception.statusCode());
    }

    @Test
    void joinGameDoesNotExist() throws ResponseException {
        facade.register("player1", "password", "p1@email.com");
        ResponseException exception = assertThrows(ResponseException.class,
                () -> facade.joinGame(ChessGame.TeamColor.WHITE, 1));
        assertEquals(400, exception.statusCode());
    }

    @Test
    void listGamesSuccess() throws ResponseException {
        facade.register("player1", "password", "p1@email.com");
        facade.createGame("testGame");
        facade.createGame("anotherGame");
        facade.createGame("lastGame");
        Collection<GameData> games = facade.listGames();
        assertEquals(3, games.size());
        Collection<String> gameNames = new ArrayList<>();
        for (GameData game : games) {
            gameNames.add(game.gameName());
        }
        assertTrue(gameNames.contains("testGame"));
        assertTrue(gameNames.contains("anotherGame"));
        assertTrue(gameNames.contains("lastGame"));
    }

    @Test
    void listGamesAfterJoin() throws ResponseException {
        facade.register("player1", "password", "p1@email.com");
        facade.createGame("testGame");
        int newGameID = facade.createGame("anotherGame");
        facade.joinGame(ChessGame.TeamColor.WHITE, newGameID);
        Collection<GameData> games = facade.listGames();
        assertEquals(2, games.size());
        for (GameData game : games) {
            if (game.gameID() == newGameID) {
                assertEquals("player1", game.whiteUsername());
                assertNull(game.blackUsername());
            }
        }
    }

    @Test
    void listGamesNoGames() throws ResponseException {
        facade.register("player1", "password", "p1@email.com");
        Collection<GameData> games = facade.listGames();
        assertEquals(0, games.size());
    }

    @Test
    void listGamesNotLoggedIn() throws ResponseException {
        facade.register("player1", "password", "p1@email.com");
        facade.logout();
        ResponseException exception = assertThrows(ResponseException.class, () -> facade.listGames());
        assertEquals(401, exception.statusCode());
    }

    @Test
    void clearUserSuccess() throws ResponseException {
        facade.register("player1", "password", "p1@email.com");
        facade.clear();
        ResponseException exception = assertThrows(ResponseException.class,
                () -> facade.login("player1", "password"));
        assertEquals(401, exception.statusCode());
    }

    @Test
    void clearGameSuccess() throws ResponseException {
        facade.register("player1", "password", "p1@email.com");
        facade.createGame("testGame");
        facade.createGame("anotherGame");
        facade.clear();
        facade.register("player1", "password", "p1@email.com");
        Collection<GameData> games = facade.listGames();
        assertEquals(0, games.size());
    }
}
