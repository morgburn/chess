package handler;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import model.GameData;
import service.*;

import java.util.Collection;
import java.util.Map;

public class GameHandler {
    private final GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public void listGames(Context ctx) {
        String authToken = ctx.header("Authorization");
        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);

        Collection<GameData> games = gameService.listGames(listGamesRequest);

        Gson gson = new Gson();
        ctx.status(200);
        ctx.contentType("application/json");
        ctx.result(gson.toJson(Map.of("games", games)));
    }

    public void createGame(Context ctx) throws DataAccessException {
        Gson gson = new Gson();
        String authToken = ctx.header("Authorization");
        String gameName = gson.fromJson(ctx.body(), CreateGameRequest.class).gameName();
        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, gameName);

        CreateGameResult createGameResult = gameService.createGame(createGameRequest);
        ctx.status(200);
        ctx.result(gson.toJson(createGameResult));
    }

    public void joinGame(Context ctx) throws DataAccessException {
        Gson gson = new Gson();
        String authToken = ctx.header("Authorization");
        ChessGame.TeamColor playerColor = gson.fromJson(ctx.body(), JoinGameRequest.class).playerColor();
        int gameID = gson.fromJson(ctx.body(), JoinGameRequest.class).gameID();
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, playerColor, gameID);

        gameService.joinGame(joinGameRequest);
        ctx.status(200);
        ctx.result(gson.toJson(joinGameRequest));
    }
}
