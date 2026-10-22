package handler;

import com.google.gson.Gson;
import io.javalin.http.Context;
import service.GameService;
import service.ListGamesRequest;

public class GameHandler {
    private final GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public void listGames(Context ctx) {
        Gson gson = new Gson();
        ListGamesRequest listGamesRequest = gson.fromJson(ctx.body(), ListGamesRequest.class);

        gameService.listGames(listGamesRequest);
        ctx.status(200);
        ctx.result(gson.toJson(listGamesRequest));
    }
}
