package de.pedigreeProject.database;

import de.pedigreeProject.model.Person;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * The abstract basis class for CRUD operations to the database.
 * It creates the tables in the database and gets the Connection object.
 */
public abstract class Gateway {

    final String PEDIGREES = "Pedigrees";
    final String PERSONS = "Persons";
    final String SPOUSES = "Spouses";
    final String SIBLINGS = "Siblings";
    final String PARENTS = "Parents";
    final String CHILDREN = "Children";

    final Connection conn;

    /**
     * Constructor for abstract class Gateway. It gets the Connection object to create the tables for the application.
     * <p>Tables:</p>
     * <ul>
     *     <li><b>Pedigrees</b><br>
     *     - pedigreeId INTEGER PRIMARY KEY AUTOINCREMENT<br>
     *     - title TEXT UNIQUE<br>
     *     - description TEXT<br>
     *     - timeStamp TEXT<br>
     *     </li>
     *     <li><b>Persons</b><br>
     *     - personId INTEGER PRIMARY KEY AUTOINCREMENT<br>
     *     - pedigreeId INTEGER<br>
     *     - givenName TEXT<br>
     *     - familyName TEXT<br>
     *     - yearOfBirth INTEGER<br>
     *     - UNIQUE(givenName,familyName, yearOfBirth)<br>
     *     </li>
     *     <li><b>Parents</b><br>
     *     - ID INTEGER PRIMARY KEY AUTOINCREMENT<br>
     *     - personId INTEGER<br>
     *     - parentId INTEGER<br>
     *     - FOREIGN KEY (personId) REFERENCES persons(personId) ON DELETE CASCADE<br>
     *     - FOREIGN KEY (parentId) REFERENCES persons(personId) ON DELETE CASCADE<br>
     *     - UNIQUE(personId, parentId)<br>
     *     </li>
     *      <li><b>Children</b><br>
     *     - ID INTEGER PRIMARY KEY AUTOINCREMENT<br>
     *     - personId INTEGER<br>
     *     - childId INTEGER<br>
     *     - FOREIGN KEY (personId) REFERENCES persons(personId) ON DELETE CASCADE<br>
     *     - FOREIGN KEY (childId) REFERENCES persons(personId) ON DELETE CASCADE<br>
     *     - UNIQUE(personId, childId)<br>
     *     </li>
     *     <li><b>Siblings</b><br>
     *     - ID INTEGER PRIMARY KEY AUTOINCREMENT<br>
     *     - personId INTEGER<br>
     *     - siblingId INTEGER<br>
     *     - FOREIGN KEY (personId) REFERENCES persons(personId) ON DELETE CASCADE<br>
     *     - FOREIGN KEY (siblingId) REFERENCES persons(personId) ON DELETE CASCADE<br>
     *     - UNIQUE(personId, siblingId)<br>
     *     </li>
     *     <li><b>Spouses</b><br>
     *     - ID INTEGER PRIMARY KEY AUTOINCREMENT<br>
     *     - personId INTEGER<br>
     *     - spouseId INTEGER<br>
     *     - FOREIGN KEY (personId) REFERENCES persons(personId) ON DELETE CASCADE<br>
     *     - FOREIGN KEY (spouseId) REFERENCES persons(personId) ON DELETE CASCADE<br>
     *     - UNIQUE(personId, spouseId)<br>
     *     </li>
     * </ul>
     *
     * @param connection the Connection object
     * @throws RuntimeException if Connection object or SQL statements are invalid
     */
    public Gateway(Connection connection)  {
        this.conn = connection;
        createTables();
    }

    private void createTables()  {

        String sqlPedigrees = """
                CREATE TABLE IF NOT EXISTS""" + " " + PEDIGREES + " " + """
                (pedigreeId INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT UNIQUE,
                description TEXT,
                timeStamp TEXT);""";

        String sqlPersonData = """
                CREATE TABLE IF NOT EXISTS""" + " " + PERSONS + " " + """
                (personId INTEGER PRIMARY KEY AUTOINCREMENT,
                pedigreeId INTEGER,
                givenName TEXT,
                familyName TEXT,
                yearOfBirth INTEGER,
                UNIQUE(givenName,familyName, yearOfBirth));""";

        String sqlParents = """
                CREATE TABLE IF NOT EXISTS""" + " " + PARENTS + " " + """
                (ID INTEGER PRIMARY KEY AUTOINCREMENT,
                personId INTEGER,
                parentId INTEGER,
                FOREIGN KEY (personId) REFERENCES persons(personId) ON DELETE CASCADE,
                FOREIGN KEY (parentId) REFERENCES persons(personId) ON DELETE CASCADE,
                UNIQUE(personId, parentId));""";

        String sqlChildren = """
                CREATE TABLE IF NOT EXISTS""" + " " + CHILDREN + " " + """
                (ID INTEGER PRIMARY KEY AUTOINCREMENT,
                personId INTEGER,
                childId INTEGER,
                FOREIGN KEY (personId) REFERENCES persons(personId) ON DELETE CASCADE,
                FOREIGN KEY (childId) REFERENCES persons(personId) ON DELETE CASCADE,
                UNIQUE(personId, childId));""";


        String sqlSiblings = """
                CREATE TABLE IF NOT EXISTS""" + " " + SIBLINGS + " " + """
                (ID INTEGER PRIMARY KEY AUTOINCREMENT,
                personId INTEGER,
                siblingId INTEGER,
                FOREIGN KEY (personId) REFERENCES persons(personId) ON DELETE CASCADE,
                FOREIGN KEY (siblingId) REFERENCES persons(personId) ON DELETE CASCADE,
                UNIQUE(personId, siblingId));""";

        String sqlSpouses = """
                CREATE TABLE IF NOT EXISTS""" + " " + SPOUSES + " " + """
                (ID INTEGER PRIMARY KEY AUTOINCREMENT,
                personId INTEGER,
                spouseId INTEGER,
                FOREIGN KEY (personId) REFERENCES persons(personId) ON DELETE CASCADE,
                FOREIGN KEY (spouseId) REFERENCES persons(personId) ON DELETE CASCADE,
                UNIQUE(personId, spouseId));""";

        try (Statement statement = conn.createStatement()) {

            statement.addBatch("BEGIN;");

            statement.addBatch(sqlPedigrees);
            statement.addBatch(sqlPersonData);
            statement.addBatch(sqlSpouses);
            statement.addBatch(sqlParents);
            statement.addBatch(sqlChildren);
            statement.addBatch(sqlSiblings);

            statement.addBatch("COMMIT;");

            statement.executeBatch();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    Optional<Person> getPersonById(int id, final List<Person> persons) {
        return persons.stream().filter(p -> p.getId() == id).findFirst();
    }
}
