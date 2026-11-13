package client;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import server.Server;
import service.AlreadyTakenException;

import java.util.Objects;

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
    public void clearServer() throws ResponseException {facade.clear();}

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
        facade.register("player1", "password", "p1@email.com").authToken();
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

}
