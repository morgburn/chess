package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {
    private final Map<String, UserData> allUserData = new HashMap<>();

    @Override
    public void createUser(UserData user) throws DataAccessException {
        String username = user.username();
        if (allUserData.containsKey(username)) {
            throw new DataAccessException("User already exists");
        }
        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        UserData hashedUser = new UserData(username, hashedPassword, user.email());
        allUserData.put(username, hashedUser);
    }

    @Override
    public UserData getUser(String username) {
        return allUserData.get(username);
    }

    @Override
    public void clear() {
        allUserData.clear();
    }
}
