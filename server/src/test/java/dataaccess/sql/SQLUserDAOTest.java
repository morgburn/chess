package dataaccess.sql;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class SQLUserDAOTest {

    @Test
    void createUserTable() {
        assertDoesNotThrow(SQLUserDAO::new);
    }

}
