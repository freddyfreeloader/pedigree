package de.frederik.unitTests.jUnitTests.database.gateway;

import de.frederik.testUtils.DatabaseName;
import de.frederik.testUtils.TestDatabaseCleaner;
import de.pedigreeProject.database.DatabaseConnectionSqlite;
import de.pedigreeProject.database.GatewayFactory;
import de.pedigreeProject.database.PedigreeGateway;
import de.pedigreeProject.database.PersonGateway;
import de.pedigreeProject.model.Pedigree;
import de.pedigreeProject.model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class Gateway_TestCase {
    static GatewayFactory gatewayFactory;
    static PedigreeGateway pedigreeGateway;
    static PersonGateway personGateway;
    static Pedigree testPedigree;

    static TestDatabaseCleaner cleaner;

    @BeforeAll
    static void setUpBeforeAll() {
        Connection connection = new DatabaseConnectionSqlite(DatabaseName.TEST.toString()).getConnection();
        cleaner = new TestDatabaseCleaner();
        gatewayFactory = new GatewayFactory(connection);
        pedigreeGateway = gatewayFactory.getPedigreeGateway();
        personGateway = gatewayFactory.getPersonGateway();
        testPedigree = new Pedigree(0, "testPedigree", "", LocalDateTime.now());
    }

    @BeforeEach
    void setUp() {
        cleaner.deleteRecords();
    }

    @AfterEach
    void tearDown() {
        cleaner.deleteRecords();
    }

    /**
     * Convenience method to create a person in database with only given name.
     *  assert(created person != null)
     * @param givenName the given name of the test person
     * @return the {@code Person} from the database
     */
    Person createTestPerson(String givenName) {

        Person person = personGateway.createPerson(testPedigree, givenName, "", null).orElse(null);
        assertNotNull(person);

        return person;
    }
    /**
     * Convenience method to create a person in database with an optional suffix. <br>
     *  assert(created person != null)
     * @param suffix the given name of the test person
     * @return the {@code Person} from the database
     */
    Person createTestPerson(String... suffix) {
        String suff = "";
        if (suffix != null && suffix.length > 0) {
            suff = suffix[0];
        }
        Person person = personGateway.createPerson(testPedigree, "TestGivenName" + suff, "TestFamilyName", null).orElse(null);
        assertNotNull(person);
        return person;
    }
}