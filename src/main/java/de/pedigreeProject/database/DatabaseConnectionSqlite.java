package de.pedigreeProject.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A SQLite database connection
 */
public class DatabaseConnectionSqlite implements DatabaseConnection {

    private static final Logger logger = LogManager.getLogger(DatabaseConnectionSqlite.class.getName());

    private final String database;

    /**
     * A SQLite database connection
     *
     * @param database the name of the database
     */
    public DatabaseConnectionSqlite(String database) {
        this.database = database;
    }

    /**
     * Gets a <code>Connection</code> to a SQLite database with database name of constructor argument.
     *
     * @return a <code>Connection</code> to SQLite database
     * @throws RuntimeException if database name is invalid
     */
    @Override
    public Connection getConnection() {
        Connection connection;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + database);
        } catch (SQLException e) {
            logger.error("Can't connect to jdbc:sqlite database! provided database name :" + database, e);
            throw new RuntimeException(e);
        }
        return connection;
    }
}
