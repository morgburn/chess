package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {

    @Test
    void clearSuccess() throws DataAccessException {
        MemoryUserDAO users = new MemoryUserDAO();
        MemoryGameDAO games = new MemoryGameDAO();
        MemoryAuthDAO auths = new MemoryAuthDAO();
        UserService userService = new UserService(users, auths);
        GameService gameService = new GameService(games, auths);
        ClearService clearService = new ClearService(users, games, auths);

        //make 2 games and check that they were made
        GameData game1 = new GameData(1, null, null, "Fake Game 1", new ChessGame());
        games.createGame(game1);
        GameData game2 = new GameData(2, null, null, "Fake Game 2", new ChessGame());
        games.createGame(game2);
        auths.createAuth(new AuthData("fakeAuth", "cosmo"));
        ListGamesRequest listGamesRequest = new ListGamesRequest("fakeAuth");
        Collection<GameData> gamesList = gameService.listGames(listGamesRequest);
        assertEquals(2, gamesList.size());
        assertTrue(gamesList.contains(game1));
        assertTrue(gamesList.contains(game2));

        //create a user and check that it was made and authToken was made
        RegisterRequest registerRequest = new RegisterRequest("cougar", "12345", "cosmo@fakeemail.com");
        RegisterResult registerResult = userService.register(registerRequest);
        String username = registerResult.username();
        assertEquals("cougar", username);
        String authToken = registerResult.authToken();
        assertNotNull(authToken);
        assertFalse(authToken.isBlank());

        clearService.clear();

        LoginRequest loginRequest = new LoginRequest("cougar", "12345");
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> userService.login(loginRequest));
        assertEquals("Error: unauthorized", exception.getMessage());

        auths.createAuth(new AuthData("fakeAuth", "cosmo"));
        listGamesRequest = new ListGamesRequest("fakeAuth");
        gamesList = gameService.listGames(listGamesRequest);
        assertEquals(0, gamesList.size());
    }
}