package client;

import org.junit.jupiter.api.*;
import server.Server;

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
        ResponseException exception = assertThrows(ResponseException.class, () -> facade.register("player1", "password1", "p12@email.com"));
        assertEquals(403, exception.statusCode());
    }

}
