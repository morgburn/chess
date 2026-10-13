package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {
    private final Map<String, AuthData> allAuthData = new HashMap<>();

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
        String authToken = auth.authToken();
        if (allAuthData.containsKey(authToken)) {
            throw new DataAccessException("authToken already exists");
        }
        allAuthData.put(authToken, auth);
    }

    @Override
    public AuthData getAuth(String authToken) {
        return (allAuthData.get(authToken));
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        if (allAuthData.containsKey(authToken)) {
            throw new DataAccessException("authToken doesn't exist");
        }
        allAuthData.remove(authToken);
    }

    @Override
    public void clear() {
        allAuthData.clear();
    }
}
