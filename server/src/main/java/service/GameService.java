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
        String gameName = createGameRequest.gameName();
        if (authToken == null || authToken.isBlank() || gameName == null || gameName.isBlank()) {
            throw new BadRequestException("Error: bad request");
        }

        AuthData auth = authDAO.getAuth(authToken);
        if (auth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        int gameID = currentGameID;
        GameData newGame = new GameData(gameID, null, null, gameName, new ChessGame());
        currentGameID++;

        gameDAO.createGame(newGame);

        return new CreateGameResult(gameID);
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws DataAccessException {
        if (joinGameRequest == null) {
            throw new BadRequestException("Error: bad request");
        }

        String authToken = joinGameRequest.authToken();
        ChessGame.TeamColor playerColor = joinGameRequest.playerColor();
        int gameID = joinGameRequest.gameID();
        if (authToken == null || authToken.isBlank() || playerColor == null || gameID <= 0) {
            throw new BadRequestException("Error: bad request");
        }

        AuthData auth = authDAO.getAuth(authToken);
        if (auth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        GameData game = gameDAO.getGame(gameID);
        if (game == null) {
            throw new BadRequestException("Error: bad request");
        }

        if (playerColor == ChessGame.TeamColor.WHITE) {
            if (game.whiteUsername() != null) {
                throw new AlreadyTakenException("Error: already taken");
            }
        } else {
            if (game.blackUsername() != null) {
                throw new AlreadyTakenException("Error: already taken");
            }
        }

        gameDAO.updateGame(gameID, playerColor, auth.username());
    }
}
