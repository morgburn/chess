package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;

import java.util.Collection;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

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
}
