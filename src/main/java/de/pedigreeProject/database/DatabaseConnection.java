package de.pedigreeProject.database;

import java.sql.Connection;

/**
 * The Interface to get the database connection for the application.
 */
public interface DatabaseConnection {
    /**
     * Gets the <code>Connection</code> object to work with database.
     *
     * @return the <code>Connection</code> object
     * @throws RuntimeException if database name is invalid
     */
    Connection getConnection();
}
