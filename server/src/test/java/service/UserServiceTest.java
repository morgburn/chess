package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.UserData;
import org.eclipse.jetty.util.log.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private MemoryUserDAO users;
    private UserService service;

    @BeforeEach
    void setUP() {
        users = new MemoryUserDAO();
        MemoryAuthDAO auths = new MemoryAuthDAO();
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
    void registerDuplicateUsername() throws AlreadyTakenException, DataAccessException {
        users.createUser(new UserData("cosmo", "password5", "cougar@email.com"));
        RegisterRequest registerRequest = new RegisterRequest("cosmo", "12345", "cosmo@fakeemail.com");
        AlreadyTakenException exception = assertThrows(AlreadyTakenException.class, () -> service.register(registerRequest));
        assertEquals("Error: already taken", exception.getMessage());
    }

    @Test
    void registerBadRequestEmptyStrings() throws BadRequestException {
        RegisterRequest registerRequest = new RegisterRequest("", "", "");
        BadRequestException exception = assertThrows(BadRequestException.class, () -> service.register(registerRequest));
        assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    void registerBadRequestNull() throws BadRequestException {
        RegisterRequest registerRequest = new RegisterRequest(null, null, null);
        BadRequestException exception = assertThrows(BadRequestException.class, () -> service.register(registerRequest));
        assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    void loginSuccess() throws DataAccessException {
        users.createUser(new UserData("cosmo", "12345", "cougar@email.com"));

        LoginRequest loginRequest = new LoginRequest("cosmo", "12345");
        LoginResult loginResult = service.login(loginRequest);

        String username = loginResult.username();
        assertEquals("cosmo", username);

        String authToken = loginResult.authToken();
        assertNotNull(authToken);
        assertFalse(authToken.isBlank());
    }

    @Test
    void loginUserDoesNotExist() {
        LoginRequest loginRequest = new LoginRequest("cosmo", "12345");
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> service.login(loginRequest));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void loginWrongPassword() throws DataAccessException {
        users.createUser(new UserData("cosmo", "12345", "cougar@email.com"));

        LoginRequest loginRequest = new LoginRequest("cosmo", "123");
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> service.login(loginRequest));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void loginBadRequestEmptyStrings() throws BadRequestException {
        LoginRequest loginRequest = new LoginRequest("", "");
        BadRequestException exception = assertThrows(BadRequestException.class, () -> service.login(loginRequest));
        assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    void loginBadRequestNull() throws BadRequestException {
        LoginRequest loginRequest = new LoginRequest(null, null);
        BadRequestException exception = assertThrows(BadRequestException.class, () -> service.login(loginRequest));
        assertEquals("Error: bad request", exception.getMessage());
    }
}