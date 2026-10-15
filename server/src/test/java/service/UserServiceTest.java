package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private MemoryUserDAO users;
    private MemoryAuthDAO auths;
    private UserService service;

    @BeforeEach
    void setUP() {
        users = new MemoryUserDAO();
        auths = new MemoryAuthDAO();
        service = new UserService(users, auths);
    }

    @Test
    void registerSuccess() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("cosmo", "12345", "cosmo@fakeemail.com");
        RegisterResult registerResult = service.register(registerRequest);

        String username = registerResult.username();
        assertEquals("cosmo", username);

        String authToken = registerResult.authToken();
        assertNotNull(authToken);
        assertFalse(authToken.isBlank());
    }

    @Test
    void registerDuplicateUsername() throws DataAccessException {
        users.createUser(new UserData("cosmo", "password5", "cougar@email.com"));
        RegisterRequest registerRequest = new RegisterRequest("cosmo", "12345", "cosmo@fakeemail.com");

    }
}