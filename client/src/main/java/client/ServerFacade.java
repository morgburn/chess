package client;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.Map;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;
    private String authToken;

    public ServerFacade(int port) {
        serverUrl = String.format("http://localhost:%d", port);
    }

    public AuthData register(String username, String password, String email) throws ResponseException {
        UserData user = new UserData(username, password, email);
        var request = buildRequest("POST", "/user", user);
        var response = sendRequest(request);
        AuthData result = handleResponse(response, AuthData.class);
        authToken = result.authToken();
        return result;
    }

    public AuthData login(String username, String password) throws ResponseException {
        UserData user = new UserData(username, password, null);
        var request = buildRequest("POST", "/session", user);
        var response = sendRequest(request);
        AuthData result = handleResponse(response, AuthData.class);
        authToken = result.authToken();
        return result;
    }

    public void logout() throws ResponseException {
        var request = buildRequest("DELETE", "/session", null);
        var response = sendRequest(request);
        handleResponse(response, null);
        authToken = null;
    }

    public int createGame(String gameName) throws ResponseException {
        Map<String, String> body = new HashMap<>();
        body.put("gameName", gameName);
        var request = buildRequest("POST", "/game", body);
        var response = sendRequest(request);
        var result = handleResponse(response, GameData.class);
        return result.gameID();
    }

    public void joinGame(ChessGame.TeamColor teamColor, int gameID) throws ResponseException {
        Map<String, Object> body = new HashMap<>();
        body.put("playerColor", teamColor);
        body.put("gameID", gameID);
        var request = buildRequest("PUT", "/game", body);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public void clear() throws ResponseException {
        authToken = null;
        var request = buildRequest("DELETE", "/db", null);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        if (authToken != null) {
            request.setHeader("authorization", authToken);
        }
        return request.build();
    }

    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException("Can't connect to server", 500);
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null && !body.isBlank()) {
                var map = new Gson().fromJson(body, HashMap.class);
                if (map.get("message") != null) {
                    String message = map.get("message").toString();
                    throw new ResponseException(message, status);
                }
            }

            throw new ResponseException("Request failed", status);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status) {
        return status == 200;
    }
}
