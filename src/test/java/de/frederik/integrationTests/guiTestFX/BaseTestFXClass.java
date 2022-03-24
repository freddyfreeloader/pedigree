package de.frederik.integrationTests.guiTestFX;

import de.frederik.integrationTests.guiTestFX.utils.TestFxHelperMethods;
import de.frederik.testUtils.DatabaseName;
import de.frederik.testUtils.TestDatabaseCleaner;
import de.frederik.testUtils.testData.TestDatabase;
import de.pedigreeProject.controller.MainModelController;
import de.pedigreeProject.database.DatabaseConnectionSqlite;
import de.pedigreeProject.database.GatewayFactory;
import de.pedigreeProject.database.PedigreeGateway;
import de.pedigreeProject.database.PersonGateway;
import de.pedigreeProject.kinship.*;
import de.pedigreeProject.model.Model;
import de.pedigreeProject.model.Pedigree;
import de.pedigreeProject.model.Person;
import de.pedigreeProject.utils.IndexChanger;
import de.pedigreeProject.utils.gui_utils.StageInjector;
import de.pedigreeProject.utils.gui_utils.StageInjectorService;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;

import java.sql.Connection;

@ExtendWith(MockitoExtension.class)
abstract class BaseTestFXClass extends ApplicationTest {

    Model model;
    static GatewayFactory gatewayFactory;

    MainModelController controller;
    PedigreeGateway pedigreeGateway;
    PersonGateway personGateway;

    static TestFxHelperMethods helper = new TestFxHelperMethods();
    static TestDatabaseCleaner cleaner = new TestDatabaseCleaner();


    @Start
    public void start(Stage stage) {
        cleaner.deleteRecords();

        Connection connection = new DatabaseConnectionSqlite(DatabaseName.TEST.toString()).getConnection();
        gatewayFactory = new GatewayFactory(connection);
        pedigreeGateway = gatewayFactory.getPedigreeGateway();
        personGateway = gatewayFactory.getPersonGateway();

        KinshipUpdater kinshipUpdater = new CloseKinshipUpdater();
        KinshipSorter kinshipSorter = new KinshipSorterImpl();
        KinshipCalculator kinshipCalculator = new GeneticKinshipCalculator();
        IndexChanger indexChanger = new IndexChanger();

        model = new Model(gatewayFactory, kinshipSorter, kinshipUpdater, kinshipCalculator, indexChanger);

        controller = Mockito.spy(new MainModelController(model));
        StageInjector stageInjector = new StageInjectorService(controller);
        Stage stage1 = stageInjector.getStage();
        stage1.show();
        stage1.toFront();
    }

    // helper methods:

    @Nullable
    Person getPersonByGivenName(String givenName) {
        return model.getPersons().stream()
                .filter(person -> person.getGivenName().equals(givenName))
                .findFirst()
                .orElse(null);
    }

    void createBaseFamilyPedigree() {
        createAndReplaceTestPedigree(TestDatabase.getPedigreeOfBaseFamily());
    }

    void createBuddenbrookPedigree() {
        createAndReplaceTestPedigree(TestDatabase.getPedigreeOfBuddenbrook());
    }

    void createTranslationTestPedigree() {
        createAndReplaceTestPedigree(TestDatabase.getTranslationTestPedigree());
    }

    void createAndReplaceTestPedigree(Pedigree pedigree) {
        Platform.runLater(() -> model.replacePedigree(pedigree));
        interrupt();
    }
}