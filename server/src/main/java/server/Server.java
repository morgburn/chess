package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.*;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import service.AlreadyTakenException;
import service.BadRequestException;
import service.UnauthorizedException;

import java.util.Map;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .exception(BadRequestException.class, (exception, ctx) -> {
                    ctx.status(400);
                    ctx.json(Map.of("message", "Error: bad request"));
                })
                .exception(UnauthorizedException.class, (exception, ctx) -> {
                    ctx.status(401);
                    ctx.json(Map.of("message", "Error: unauthorized"));
                })
                .exception(AlreadyTakenException.class, (exception, ctx) -> {
                    ctx.status(403);
                    ctx.json(Map.of("message", "Error: already taken"));
                })
                .exception(DataAccessException.class, (exception, ctx) -> {
                    ctx.status(500);
                    ctx.json(Map.of("message", "Error: " + exception.getMessage()));
                });

        // Register your endpoints and exception handlers here.
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
