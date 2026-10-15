package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemoryAuthDAOTest {

    private MemoryAuthDAO authDAO;
    private AuthData fakeAuth;

    @BeforeEach
    void setUp() {
        authDAO = new MemoryAuthDAO();
        fakeAuth = new AuthData("123456789", "cosmo");
    }

    @Test
    void createAuthSuccess() {
        assertDoesNotThrow(() -> authDAO.createAuth(fakeAuth));
        AuthData newAuth = authDAO.getAuth("123456789");
        assertNotNull(newAuth);
        assertEquals(fakeAuth, newAuth);
    }

    @Test
    void createAuthAlreadyExists() {
        assertDoesNotThrow(() -> authDAO.createAuth(fakeAuth));
        AuthData sameAuth = new AuthData("123456789", "cosmo2");
        DataAccessException exception = assertThrows(DataAccessException.class, () -> authDAO.createAuth(sameAuth));
        assertEquals("authToken already exists", exception.getMessage());
    }

    @Test
    void getAuthSuccess() {
        assertDoesNotThrow(() -> authDAO.createAuth(fakeAuth));
        AuthData newAuth = authDAO.getAuth("123456789");
        assertNotNull(newAuth);
        assertEquals("123456789", newAuth.authToken());
    }

    @Test
    void getAuthDoesNotExist() {
        AuthData newAuth = authDAO.getAuth("123456789");
        assertNull(newAuth);
    }

    @Test
    void deleteAuthSuccess() {
        assertDoesNotThrow(() -> authDAO.createAuth(fakeAuth));
        assertNotNull(authDAO.getAuth("123456789"));
        assertDoesNotThrow(() -> authDAO.deleteAuth("123456789"));
        assertNull(authDAO.getAuth("123456789"));
    }

    @Test
    void deleteAuthNoTokenToDelete() {
        DataAccessException deleteException = assertThrows(DataAccessException.class, () -> authDAO.deleteAuth("123456789"));
        assertEquals("authToken doesn't exist", deleteException.getMessage());
    }

    @Test
    void clear() {
        assertDoesNotThrow(() -> authDAO.createAuth(fakeAuth));
        assertDoesNotThrow(() -> authDAO.createAuth(new AuthData("987654321", "cosmo2")));
        assertNotNull(authDAO.getAuth("123456789"));
        assertNotNull(authDAO.getAuth("987654321"));
        authDAO.clear();
        assertNull(authDAO.getAuth("123456789"));
        assertNull(authDAO.getAuth("987654321"));
    }
}