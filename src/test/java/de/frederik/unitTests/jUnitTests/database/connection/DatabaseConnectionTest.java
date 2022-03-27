package de.frederik.unitTests.jUnitTests.database.connection;

import de.pedigreeProject.database.DatabaseConnection;
import de.pedigreeProject.database.DatabaseConnectionSqlite;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DatabaseConnectionTest {
    final static Logger logger = LogManager.getLogger("testLogger");

    @Test
    @DisplayName("try database connection to <testDatabase.db>")
    void getConnection() {

        DatabaseConnection connection = new DatabaseConnectionSqlite("testDatabase.db");

        assertNotNull(connection.getConnection());
    }

    @Test
    @DisplayName("try database connection to <:::> should fail")
    void getConnection_Failed() {
//        logger.info("testing failing connection");

        DatabaseConnection connection = new DatabaseConnectionSqlite(":::");

        assertThrows(RuntimeException.class, connection::getConnection);
    }
}