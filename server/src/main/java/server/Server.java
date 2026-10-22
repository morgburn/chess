package server;

import com.google.gson.Gson;
import dataaccess.*;
import handler.ClearHandler;
import handler.GameHandler;
import handler.UserHandler;
import io.javalin.*;
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

        MemoryUserDAO users = new MemoryUserDAO();
        MemoryGameDAO games = new MemoryGameDAO();
        MemoryAuthDAO auths = new MemoryAuthDAO();

        UserService userService = new UserService(users, auths);
        UserHandler userHandler = new UserHandler(userService);

        GameService gameService = new GameService(games, auths);
        GameHandler gameHandler = new GameHandler(gameService);

        ClearService clearService = new ClearService(users, games, auths);
        ClearHandler clearHandler = new ClearHandler(clearService);

        javalin.post("/user", userHandler::register);
        javalin.post("/session", userHandler::login);
        javalin.delete("/session", userHandler::logout);

        javalin.get("/game", gameHandler::listGames);
        javalin.post("/game", gameHandler::createGame);
        javalin.put("/game", gameHandler::joinGame);

        javalin.delete("/db", clearHandler::clear);

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
