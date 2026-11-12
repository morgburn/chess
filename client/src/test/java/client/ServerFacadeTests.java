package client;

import org.junit.jupiter.api.*;
import server.Server;

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

}
