package dataaccess.sql;

import dataaccess.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SQLUserDAOTest {

    private SQLUserDAO userDAO;
    private UserData fakeUser;

    @BeforeEach
    void setUp() throws DataAccessException {
        userDAO = new SQLUserDAO();
        userDAO.clear();
        fakeUser = new UserData("cosmo", "cosmo123", "cosmo@byu.example.com");
    }

    @Test
    void createUserTable() {
        assertDoesNotThrow(SQLUserDAO::new);
    }

    @Test
    void testCreateUserSuccess() throws DataAccessException {
        userDAO.createUser(fakeUser);
        UserData newUser = userDAO.getUser("cosmo");
        assertNotNull(newUser);
        assertEquals(fakeUser, newUser);
    }

}
