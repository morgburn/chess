package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.GameData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {

    @Test
    void clear() throws DataAccessException {
        MemoryUserDAO users = new MemoryUserDAO();
        MemoryGameDAO games = new MemoryGameDAO();
        MemoryAuthDAO auths = new MemoryAuthDAO();
        ClearService clearService = new ClearService(users, games, auths);

        GameData game1 = new GameData(1, null, null, "Fake Game 1", new ChessGame());
        games.createGame(game1);
        GameData game2 = new GameData(2, null, null, "Fake Game 2", new ChessGame());
        games.createGame(game2);

        //assert there are 2 games

    }
}