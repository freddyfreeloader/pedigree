package de.frederik.unitTests.jUnitTests.database.connection;

import de.pedigreeProject.model.Pedigree;
import de.pedigreeProject.model.Person;
import de.frederik.testUtils.DatabaseName;
import de.pedigreeProject.database.DatabaseConnectionSqlite;
import de.pedigreeProject.database.GatewayFactory;
import de.pedigreeProject.database.PedigreeGateway;
import de.pedigreeProject.database.PersonGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doThrow;

/**
 * These tests provoke RuntimeExceptions by manipulating the {@code Connection.createStatement()}
 * or {@code Connection.prepareStatement()} methods to simulate invalid SQL or invalid Connection.
 */
@ExtendWith(MockitoExtension.class)
public class InvalidConnection {

    static GatewayFactory gatewayFactory;
    PedigreeGateway pedigreeGateway;
    PersonGateway personGateway;
    @Spy
    Connection connection = new DatabaseConnectionSqlite(DatabaseName.TEST.toString()).getConnection();

    @BeforeEach
    void setUp() {
        gatewayFactory = new GatewayFactory(connection);
        personGateway = gatewayFactory.getPersonGateway();
        pedigreeGateway = gatewayFactory.getPedigreeGateway();
    }

    @Test
    void createTables_fails() throws SQLException {
        doThrow(new SQLException()).when(connection).createStatement();
        assertThrows(RuntimeException.class, () -> gatewayFactory = new GatewayFactory(connection));
    }

    @Test
    void createPedigree_fails() throws SQLException {
        //noinspection MagicConstant
        doThrow(new SQLException()).when(connection).prepareStatement(anyString(), anyInt());
        assertThrows(RuntimeException.class, () -> pedigreeGateway.createPedigree("newPedigree", ""));
    }

    @Test
    void readPedigree_fails() throws SQLException {
        doThrow(new SQLException()).when(connection).createStatement();
        assertThrows(RuntimeException.class, () -> pedigreeGateway.readPedigrees());
    }

    @Test
    void updatePedigree_fails() throws SQLException {
        doThrow(new SQLException()).when(connection).prepareStatement(anyString());
        Pedigree pedigree = new Pedigree(1, "Test", "", LocalDateTime.now());
        assertThrows(RuntimeException.class, () -> pedigreeGateway.updatePedigree(pedigree, "Changed", ""));
    }

    @Test
    void deletePedigree_fails() throws SQLException {
        doThrow(new SQLException()).when(connection).createStatement();
        Pedigree pedigree = new Pedigree(1, "Test", "", LocalDateTime.now());
        assertThrows(RuntimeException.class, () -> pedigreeGateway.deletePedigree(pedigree));
    }

    @Test
    void createPerson_fails() throws SQLException {
        //noinspection MagicConstant
        doThrow(new SQLException()).when(connection).prepareStatement(anyString(), anyInt());
        Pedigree pedigree = new Pedigree(1, "Test", "", LocalDateTime.now());
        assertThrows(RuntimeException.class, () -> personGateway.createPerson(pedigree, "test", "", null));
    }

    @Test
    void personAlreadyExists_fails() throws SQLException {
        doThrow(new SQLException()).when(connection).prepareStatement(anyString());
        Pedigree pedigree = new Pedigree(1, "Test", "", LocalDateTime.now());
        assertThrows(RuntimeException.class, () -> personGateway.createPerson(pedigree, "test", "", null));
    }

    @Test
    void readPersons_fails() throws SQLException {
        doThrow(new SQLException()).when(connection).createStatement();
        Pedigree pedigree = new Pedigree(1, "Test", "", LocalDateTime.now());
        assertThrows(RuntimeException.class, () -> personGateway.readPersons(pedigree));
    }

    @Test
    void updatePersonsData() throws SQLException {
        Person person = new Person(1, 1, "Test", "", null);
        doThrow(new SQLException()).when(connection).prepareStatement(anyString());
        assertThrows(RuntimeException.class, () -> personGateway.updatePersonsData(person, "test", "", null));
    }


    @Test
    void delete_fails() throws SQLException {
        doThrow(new SQLException()).when(connection).prepareStatement(anyString());
        assertThrows(RuntimeException.class, () -> personGateway.deletePerson(new Person(1, 1, "", "", null)));
    }

    @Test
    void updateRelatives_fails() throws SQLException {
        doThrow(new SQLException()).when(connection).createStatement();
        assertThrows(RuntimeException.class, () -> personGateway.updateRelatives(new Person(1, 1, "", "", null)));
    }
}

