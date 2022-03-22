package de.frederik.testUtils;

import de.pedigreeProject.database.DatabaseConnectionSqlite;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Provides methods to delete all records or to drop all tables.
 */
public class TestDatabaseCleaner {
    final static Logger logger = LogManager.getLogger(TestDatabaseCleaner.class.getName());

    private final Connection conn;

    public TestDatabaseCleaner() {
        this.conn = new DatabaseConnectionSqlite(DatabaseName.TEST.toString()).getConnection();
    }

    public void deleteRecords() {
        try (Statement stmt = conn.createStatement()) {

            stmt.addBatch("BEGIN;");

            stmt.addBatch("DELETE FROM Pedigrees");
            stmt.addBatch("DELETE FROM Persons");
            stmt.addBatch("DELETE FROM Siblings");
            stmt.addBatch("DELETE FROM Parents");
            stmt.addBatch("DELETE FROM Children");
            stmt.addBatch("DELETE FROM Spouses");

            stmt.addBatch("COMMIT;");
            stmt.executeBatch();

        } catch (SQLException e) {
            logger.error("database error " , e);
        }
    }
    @SuppressWarnings("unused")
    public void dropTables() {
        try (Statement stmt = conn.createStatement()) {

            stmt.addBatch("BEGIN;");

            stmt.addBatch("DROP TABLE Pedigrees");
            stmt.addBatch("DROP TABLE Persons");
            stmt.addBatch("DROP TABLE Siblings");
            stmt.addBatch("DROP TABLE Parents");
            stmt.addBatch("DROP TABLE Children");
            stmt.addBatch("DROP TABLE Spouses");

            stmt.addBatch("COMMIT;");
            stmt.executeBatch();

        } catch (SQLException e) {
            logger.error("database error " , e);
        }
    }

    public static void main(String[] args) {
        TestDatabaseCleaner cleaner = new TestDatabaseCleaner(); cleaner.deleteRecords();
    }
}
