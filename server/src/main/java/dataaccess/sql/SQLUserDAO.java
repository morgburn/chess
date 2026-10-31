package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.UserDAO;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static dataaccess.sql.SQLUpdate.executeUpdate;

public class SQLUserDAO implements UserDAO {
    public SQLUserDAO() throws DataAccessException {
        DatabaseInitializer.configureDatabase();
    }

    @Override
    public void createUser(UserData users) throws DataAccessException {
        if (getUser(users.username()) != null) {
            throw new DataAccessException("User already exists");
        }
        String hashedPassword = BCrypt.hashpw(users.password(), BCrypt.gensalt());
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        executeUpdate(statement, users.username(), hashedPassword, users.email());
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM users WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        var foundUsername = rs.getString("username");
                        var password = rs.getString("password");
                        var email = rs.getString("email");
                        return new UserData(foundUsername, password, email);
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to read data: %s", ex.getMessage()), ex);
        }
        return null;
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE users";
        executeUpdate(statement);
    }
}
