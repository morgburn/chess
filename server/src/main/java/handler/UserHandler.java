package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.*;

public class UserHandler {
    private final UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public void register(Context ctx) throws DataAccessException {
        Gson gson = new Gson();
        RegisterRequest registerRequest = gson.fromJson(ctx.body(), RegisterRequest.class);
        RegisterResult registerResult = userService.register(registerRequest);
        ctx.status(200);
        ctx.json(registerResult);
    }

    public void login(Context ctx) throws DataAccessException {
        Gson gson = new Gson();
        LoginRequest loginRequest = gson.fromJson(ctx.body(), LoginRequest.class);
        LoginResult loginResult = userService.login(loginRequest);
        ctx.status(200);
        ctx.json(loginResult);
    }

}
