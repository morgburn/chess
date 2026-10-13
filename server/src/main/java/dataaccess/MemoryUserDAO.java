package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {
    private Map<String, UserData> allUserData = new HashMap<>();

    @Override
    public void createUser(UserData user) throws DataAccessException {
        String username = user.username();
        if (allUserData.containsKey(username)) {
            throw new DataAccessException("User already exists");
        }
            allUserData.put(username, user);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return allUserData.get(username);
    }

    @Override
    public void clear() {
        allUserData.clear();
    }
}
