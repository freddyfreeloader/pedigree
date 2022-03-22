package de.frederik.testUtils;

/**
 * Enum to provide the database names for testing.
 */
public enum DatabaseName {
    TEST("testDatabase.db"),
    PRODUCTION("pedigree.db");

    String databaseName;

    @Override
    public String toString() {
        return  databaseName;
    }

    DatabaseName(String databaseName) {
        this.databaseName=databaseName;
    }
}
