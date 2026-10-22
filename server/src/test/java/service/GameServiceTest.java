package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    private MemoryGameDAO games;
    private MemoryAuthDAO auths;
    private GameService service;

    @BeforeEach
    void setUp() {
        games = new MemoryGameDAO();
        auths = new MemoryAuthDAO();
        service = new GameService(games, auths);
    }

    @Test
    void listGamesSuccess() throws DataAccessException {
        GameData game1 = new GameData(1, null, null, "Fake Game 1", new ChessGame());
        games.createGame(game1);
        GameData game2 = new GameData(2, null, null, "Fake Game 2", new ChessGame());
        games.createGame(game2);

        auths.createAuth(new AuthData("fakeAuth", "cosmo"));
        ListGamesRequest listGamesRequest = new ListGamesRequest("fakeAuth");

        Collection<GameData> gamesList = service.listGames(listGamesRequest);

        assertEquals(2, gamesList.size());
        assertTrue(gamesList.contains(game1));
        assertTrue(gamesList.contains(game2));
    }

    @Test
    void listGamesInvalidToken() {
        ListGamesRequest listGamesRequest = new ListGamesRequest("fakeAuth");
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> service.listGames(listGamesRequest));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void listGamesNoGames() throws DataAccessException {
        auths.createAuth(new AuthData("fakeAuth", "cosmo"));
        ListGamesRequest listGamesRequest = new ListGamesRequest("fakeAuth");

        Collection<GameData> gamesList = service.listGames(listGamesRequest);

        assertEquals(0, gamesList.size());
    }

    @Test
    void listGamesBadRequestNull() {
        ListGamesRequest listGamesRequest = new ListGamesRequest(null);
        BadRequestException exception = assertThrows(BadRequestException.class, () -> service.listGames(listGamesRequest));
        assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    void listGamesBadRequestEmptyToken() {
        ListGamesRequest listGamesRequest = new ListGamesRequest("");
        BadRequestException exception = assertThrows(BadRequestException.class, () -> service.listGames(listGamesRequest));
        assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    void createGameSuccess() throws DataAccessException {
        auths.createAuth(new AuthData("fakeAuth", "cosmo"));
        CreateGameRequest createGameRequest = new CreateGameRequest("fakeAuth", "Game 1");
        CreateGameResult createGameResult = service.createGame(createGameRequest);
        assertEquals(1, createGameResult.gameID());
        assertEquals("Game 1", games.getGame(1).gameName());
        assertNull(games.getGame(1).whiteUsername());
        assertNull(games.getGame(1).blackUsername());

        auths.createAuth(new AuthData("anotherFakeAuth", "cosmo2"));
        createGameRequest = new CreateGameRequest("anotherFakeAuth", "Game 2");
        createGameResult = service.createGame(createGameRequest);
        assertEquals(2, createGameResult.gameID());
        assertEquals("Game 2", games.getGame(2).gameName());
        assertNull(games.getGame(2).whiteUsername());
        assertNull(games.getGame(2).blackUsername());
    }

    @Test
    void createGameNoGameName() throws DataAccessException {
        auths.createAuth(new AuthData("fakeAuth", "cosmo"));
        CreateGameRequest createGameRequest = new CreateGameRequest("fakeAuth", "");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> service.createGame(createGameRequest));
        assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    void createGameInvalidToken() {
        CreateGameRequest createGameRequest = new CreateGameRequest("fakeAuth", "Game 1");
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> service.createGame(createGameRequest));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void createGameBadRequestNull() {
        CreateGameRequest createGameRequest = new CreateGameRequest(null, "Game");
        BadRequestException exception = assertThrows(BadRequestException.class, () -> service.createGame(createGameRequest));
        assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    void createGameBadRequestEmptyToken() {
        CreateGameRequest createGameRequest = new CreateGameRequest("", "Game");
        BadRequestException exception = assertThrows(BadRequestException.class, () -> service.createGame(createGameRequest));
        assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    void joinGameWhiteSuccess() throws DataAccessException {
        auths.createAuth(new AuthData("fakeAuth", "user1"));
        games.createGame(new GameData(1, null, "user2", "fakeGame", new ChessGame()));
        JoinGameRequest joinGameRequest = new JoinGameRequest("fakeAuth", ChessGame.TeamColor.WHITE, 1);
        service.joinGame(joinGameRequest);

        assertEquals("user1", games.getGame(1).whiteUsername());
        assertEquals("user2", games.getGame(1).blackUsername());
    }

    @Test
    void joinGameBlackSuccess() throws DataAccessException {
        auths.createAuth(new AuthData("fakeAuth", "user2"));
        games.createGame(new GameData(5, null, null, "fakeGame", new ChessGame()));
        JoinGameRequest joinGameRequest = new JoinGameRequest("fakeAuth", ChessGame.TeamColor.BLACK, 5);
        service.joinGame(joinGameRequest);

        assertNull(games.getGame(5).whiteUsername());
        assertEquals("user2", games.getGame(5).blackUsername());
    }

    @Test
    void joinGameBadRequestNull() {
        JoinGameRequest joinGameRequest = new JoinGameRequest(null, ChessGame.TeamColor.WHITE, 1);
        BadRequestException exception = assertThrows(BadRequestException.class, () -> service.joinGame(joinGameRequest));
        assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    void joinGameInvalidToken() {
        JoinGameRequest joinGameRequest = new JoinGameRequest("fakeAuth", ChessGame.TeamColor.WHITE, 5);
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> service.joinGame(joinGameRequest));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void joinGameBadRequestInvalidGameID() throws DataAccessException {
        auths.createAuth(new AuthData("fakeAuth", "cosmo"));
        JoinGameRequest joinGameRequest = new JoinGameRequest("fakeAuth", ChessGame.TeamColor.WHITE, -1);
        BadRequestException exception = assertThrows(BadRequestException.class, () -> service.joinGame(joinGameRequest));
        assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    void joinGameDoesNotExist() throws DataAccessException {
        auths.createAuth(new AuthData("fakeAuth", "cosmo"));
        games.createGame(new GameData(1, null, null, "fakeGame", new ChessGame()));
        JoinGameRequest joinGameRequest = new JoinGameRequest("fakeAuth", ChessGame.TeamColor.BLACK, 5);
        BadRequestException exception = assertThrows(BadRequestException.class, () -> service.joinGame(joinGameRequest));
        assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    void joinGameWhiteTaken() throws DataAccessException {
        auths.createAuth(new AuthData("fakeAuth", "user2"));
        games.createGame(new GameData(3, "user1", null, "fakeGame", new ChessGame()));
        JoinGameRequest joinGameRequest = new JoinGameRequest("fakeAuth", ChessGame.TeamColor.WHITE, 3);
        AlreadyTakenException exception = assertThrows(AlreadyTakenException.class, () -> service.joinGame(joinGameRequest));
        assertEquals("Error: already taken", exception.getMessage());
    }

    @Test
    void joinGameBlackTaken() throws DataAccessException {
        auths.createAuth(new AuthData("fakeAuth", "user2"));
        games.createGame(new GameData(7, "user1", "user3", "fakeGame", new ChessGame()));
        JoinGameRequest joinGameRequest = new JoinGameRequest("fakeAuth", ChessGame.TeamColor.BLACK, 7);
        AlreadyTakenException exception = assertThrows(AlreadyTakenException.class, () -> service.joinGame(joinGameRequest));
        assertEquals("Error: already taken", exception.getMessage());
    }
}