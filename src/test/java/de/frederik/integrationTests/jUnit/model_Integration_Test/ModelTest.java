package de.frederik.integrationTests.jUnit.model_Integration_Test;

import de.frederik.testUtils.DatabaseName;
import de.frederik.testUtils.TestDatabaseCleaner;
import de.pedigreeProject.database.DatabaseConnectionSqlite;
import de.pedigreeProject.database.GatewayFactory;
import de.pedigreeProject.database.PedigreeGateway;
import de.pedigreeProject.database.PersonGateway;
import de.pedigreeProject.kinship.*;
import de.pedigreeProject.model.Model;
import de.pedigreeProject.utils.IndexChanger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;

abstract class ModelTest {

    static Model model;
    static GatewayFactory gatewayFactory;
    static PedigreeGateway pedigreeGateway;
    static PersonGateway personGateway;
    static TestDatabaseCleaner cleaner = new TestDatabaseCleaner();
    static KinshipSorter kinshipSorter;
    static KinshipUpdater kinshipUpdater;
    static KinshipCalculator kinshipCalculator;
    static IndexChanger indexChanger;

    @BeforeAll
    static void createData() {
        Connection connection = new DatabaseConnectionSqlite(DatabaseName.TEST.toString()).getConnection();
        gatewayFactory = new GatewayFactory(connection);
        pedigreeGateway = gatewayFactory.getPedigreeGateway();
        personGateway = gatewayFactory.getPersonGateway();
        kinshipSorter = new KinshipSorterImpl();
        kinshipUpdater = new CloseKinshipUpdater();
        kinshipCalculator = new GeneticKinshipCalculator();
        indexChanger = new IndexChanger();
    }

    @BeforeEach
    void setUp() {
        model = new Model(gatewayFactory, kinshipSorter, kinshipUpdater, kinshipCalculator, indexChanger);
    }

    @AfterEach
    void cleanUpDatabase() {
        cleaner.deleteRecords();
    }
}