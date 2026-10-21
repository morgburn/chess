package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;

import java.util.Collection;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;
    private int currentGameID = 1;

    public GameService (GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public Collection<GameData> listGames(ListGamesRequest listGamesRequest) {
        if (listGamesRequest == null) {
            throw new BadRequestException("Error: bad request");
        }
        String authToken = listGamesRequest.authToken();
        if (authToken == null || authToken.isBlank()) {
            throw new BadRequestException("Error: bad request");
        }

        AuthData auth = authDAO.getAuth(authToken);
        if (auth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        return gameDAO.listGames();
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        if (createGameRequest == null) {
            throw new BadRequestException("Error: bad request");
        }
        String authToken = createGameRequest.authToken();
        if (authToken == null || authToken.isBlank()) {
            throw new BadRequestException("Error: bad request");
        }

        AuthData auth = authDAO.getAuth(authToken);
        if (auth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        int gameID = currentGameID;
        GameData newGame = new GameData(gameID, null, null, createGameRequest.gameName(), new ChessGame());
        currentGameID++;

        gameDAO.createGame(newGame);

        return new CreateGameResult(gameID);
    }
}
