package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
    void createGameSuccess() {
    }

    @Test
    void createGameNoGameName() {
    }

    @Test
    void createGameInvalidToken() {
    }

    @Test
    void createGameBadRequestNull() {
    }

    @Test
    void createGameBadRequestEmptyToken() {
    }

    @Test
    void joinGameWhiteSuccess() {
    }

    @Test
    void joinGameBlackSuccess() {
    }

    @Test
    void joinGameBadRequestNull() {
    }

    @Test
    void joinGameBadRequestInvalidGameID() {
    }

    @Test
    void joinGameDoesNotExist() {
    }

    @Test
    void joinGameWhiteTaken() {
    }

    @Test
    void joinGameBlackTaken() {
    }
}