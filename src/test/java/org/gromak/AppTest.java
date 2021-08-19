package org.gromak;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AppTest {
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void shouldGetJdbcConnection() throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/my_db?useSSL=false&amp&serverTimezone=UTC", "bestuser", "bestuser")) {
            Assertions.assertTrue(connection.isValid(1));
            assertFalse(connection.isClosed());
        }
    }
}
