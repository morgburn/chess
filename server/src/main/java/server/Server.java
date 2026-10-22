package server;

import com.google.gson.Gson;
import dataaccess.*;
import handler.GameHandler;
import handler.UserHandler;
import io.javalin.*;
import io.javalin.http.Context;
import service.*;

import java.util.Map;

public class Server {

    private final Javalin javalin;

    public Server() {

        // Register your endpoints and exception handlers here.
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .exception(BadRequestException.class, (exception, ctx) -> {
                    ctx.status(400);
                    ctx.contentType("application/json");
                    ctx.result(new Gson().toJson(Map.of("message", "Error: bad request")));
                })
                .exception(UnauthorizedException.class, (exception, ctx) -> {
                    ctx.status(401);
                    ctx.contentType("application/json");
                    ctx.result(new Gson().toJson(Map.of("message", "Error: unauthorized")));
                })
                .exception(AlreadyTakenException.class, (exception, ctx) -> {
                    ctx.status(403);
                    ctx.contentType("application/json");
                    ctx.result(new Gson().toJson(Map.of("message", "Error: already taken")));
                })
                .exception(DataAccessException.class, (exception, ctx) -> {
                    ctx.status(500);
                    ctx.contentType("application/json");
                    ctx.result(new Gson().toJson(Map.of("message", "Error: " + exception.getMessage())));
                });

        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();

        UserService userService = new UserService(userDAO, authDAO);
        UserHandler userHandler = new UserHandler(userService);

        GameService gameService = new GameService(gameDAO, authDAO);
        GameHandler gameHandler = new GameHandler(gameService);

        javalin.post("/user", userHandler::register);
        javalin.post("/session", userHandler::login);
        javalin.delete("/session", userHandler::logout);

        javalin.get("/game", gameHandler::listGames);
        javalin.post("/game", gameHandler::createGame);
        javalin.put("/game", gameHandler::joinGame);

        javalin.delete("/db", Server::clearDb);

    }

    private static void clearDb(Context ctx) {
        var r = Map.of();
        var json = new Gson();
        ctx.json(json.toJson(r));
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
