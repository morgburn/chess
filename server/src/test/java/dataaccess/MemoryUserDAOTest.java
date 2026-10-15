package dataaccess;

import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemoryUserDAOTest {

    private MemoryUserDAO userDAO;
    private UserData fakeUser;

    @BeforeEach
    void newDAO() {
        userDAO = new MemoryUserDAO();
        fakeUser = new UserData("cosmo", "cosmo123", "cosmo@byu.example.com");
    }

    @Test
    void testCreateUserSuccess() throws DataAccessException {
        userDAO.createUser(fakeUser);
        UserData newUser = userDAO.getUser("cosmo");
        assertNotNull(newUser);
        assertEquals(fakeUser, newUser);
    }

    @Test
    void testCreateUserDuplicateUsername() throws DataAccessException {
        userDAO.createUser(fakeUser);
        UserData sameUser = new UserData("cosmo", "cosmo321", "cosmo@cosmo.com");
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            userDAO.createUser(sameUser);
        });
        assertEquals("User already exists", exception.getMessage());
    }

    @Test
    void testGetUserSuccess() throws DataAccessException {
        userDAO.createUser(fakeUser);
        UserData newUser = userDAO.getUser("cosmo");
        assertNotNull(newUser);
        assertEquals("cosmo", newUser.username());
    }

    @Test
    void testGetUserNull() {
        UserData newUser = userDAO.getUser("cosmo");
        assertNull(newUser);
    }

    @Test
    void testClear() throws DataAccessException {
        userDAO.createUser(fakeUser);
        userDAO.createUser(new UserData("cosmo2", "cosmo321", "cosmo@cosmo.byu"));
        assertNotNull(userDAO.getUser("cosmo"));
        assertNotNull(userDAO.getUser("cosmo2"));
        userDAO.clear();
        assertNull(userDAO.getUser("cosmo"));
        assertNull(userDAO.getUser("cosmo2"));
    }
}