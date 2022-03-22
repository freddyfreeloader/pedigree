package de.frederik.integrationTests.guiTestFX;

import de.frederik.testUtils.DatabaseName;
import de.frederik.testUtils.TestDatabaseCleaner;
import de.pedigreeProject.controller.MainModelController;
import de.pedigreeProject.controller.inputRelatives.InputRelativesController;
import de.pedigreeProject.controller.pedigreeController.NewPedigreeController;
import de.pedigreeProject.controller.pedigreeController.UpdatePedigreeController;
import de.pedigreeProject.controller.personDataController.NewPersonDataController;
import de.pedigreeProject.controller.personDataController.UpdatePersonDataController;
import de.pedigreeProject.database.DatabaseConnectionSqlite;
import de.pedigreeProject.database.GatewayFactory;
import de.pedigreeProject.database.PedigreeGateway;
import de.pedigreeProject.database.PersonGateway;
import de.pedigreeProject.utils.gui_utils.StageInjector;
import de.pedigreeProject.utils.gui_utils.StageInjectorService;
import de.pedigreeProject.kinship.CloseKinshipUpdater;
import de.pedigreeProject.kinship.GeneticKinshipCalculator;
import de.pedigreeProject.kinship.KinshipSorterImpl;
import de.pedigreeProject.model.Model;
import de.pedigreeProject.model.Pedigree;
import de.pedigreeProject.model.Person;
import de.pedigreeProject.utils.IndexChanger;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@ExtendWith(MockitoExtension.class)
class StageInjectorServiceTestFX extends ApplicationTest {

    Model model;
    static GatewayFactory gatewayFactory;

    PedigreeGateway pedigreeGateway;
    PersonGateway personGateway;
    Pedigree testPedigree;
    Person person;
    static TestDatabaseCleaner cleaner = new TestDatabaseCleaner();

    @Start
    public void start(Stage stage) {
        cleaner.deleteRecords();
        Connection connection = new DatabaseConnectionSqlite(DatabaseName.TEST.toString()).getConnection();
        gatewayFactory = new GatewayFactory(connection);
        pedigreeGateway = gatewayFactory.getPedigreeGateway();
        personGateway = gatewayFactory.getPersonGateway();
        model = new Model(gatewayFactory, new KinshipSorterImpl(), new CloseKinshipUpdater(), new GeneticKinshipCalculator(), new IndexChanger());

        testPedigree = pedigreeGateway.createPedigree("Test", "").orElse(null);
        assumeTrue(testPedigree != null);

        person = personGateway.createPerson(testPedigree, "TestPersons", "", null).orElse(null);
        assumeTrue(person != null);

        StageInjector stageInjector = new StageInjectorService(new MainModelController(model));
        assertNotNull(stageInjector);
        Stage stage1 = stageInjector.getStage();
        stage1.show();
        stage1.toFront();
    }

    @AfterEach
    void tearDown() {
        pedigreeGateway.deletePedigree(testPedigree);
    }

    @Test
    void illegalController_null() {
        Platform.runLater(() -> {
            assertThrows(NullPointerException.class, () -> new StageInjectorService(null));
        });
    }

    @Test
    void getParentWithoutStage() {
        Platform.runLater(() -> {
            StageInjector stageInjector = new StageInjectorService(new InputRelativesController(model, person));
            assertNotNull(stageInjector);

            assertTrue(stageInjector.getParent() instanceof AnchorPane);
        });
    }

    @Test
    void show() {
        InputRelativesController mockedController = Mockito.mock(InputRelativesController.class);

        Platform.runLater(() -> {
            StageInjector stageInjector = new StageInjectorService(mockedController);

            stageInjector.show();
            assertTrue(stageInjector.getStage().isShowing());
        });
    }

    @Test
    void getStage_InputRelativesStage() {
        Platform.runLater(() -> {
            StageInjector stageInjector = new StageInjectorService(new InputRelativesController(model, person));
            assertNotNull(stageInjector);

            Stage stage = stageInjector.getStage();
            assertNotNull(stage);

            assertTrue(stageInjector.getParent() instanceof AnchorPane);

            stageInjector.show();
            assertTrue(stage.isShowing());

            stage.close();
        });
    }

    @Test
    void getStage_UpdatePersonsStage() {
        Platform.runLater(() -> {
            StageInjector stageInjector = new StageInjectorService(new UpdatePersonDataController(model, person));
            assertNotNull(stageInjector);

            Stage stage = stageInjector.getStage();
            assertNotNull(stage);

            assertTrue(stageInjector.getParent() instanceof AnchorPane);

            stageInjector.show();
            assertTrue(stage.isShowing());

            stage.close();
        });
    }

    @Test
    void getStage_InputPersonsStage() {
        Platform.runLater(() -> {
            StageInjector stageInjector = new StageInjectorService(new NewPersonDataController(model));
            assertNotNull(stageInjector);

            Stage stage = stageInjector.getStage();
            assertNotNull(stage);

            assertTrue(stageInjector.getParent() instanceof AnchorPane);

            stageInjector.show();
            assertTrue(stage.isShowing());

            stage.close();
        });
    }

    @Test
    void getStage_NewPedigreeStage() {
        Platform.runLater(() -> {
            StageInjector stageInjector = new StageInjectorService(new NewPedigreeController(model));
            assertNotNull(stageInjector);

            Stage stage = stageInjector.getStage();
            assertNotNull(stage);

            assertTrue(stageInjector.getParent() instanceof StackPane);

            stageInjector.show();
            assertTrue(stage.isShowing());

            stage.close();
        });
    }

    @Test
    void getStage_UpdatePedigreeStage() {
        Platform.runLater(() -> {
            StageInjector stageInjector = new StageInjectorService(new UpdatePedigreeController(model));
            assertNotNull(stageInjector);

            Stage stage = stageInjector.getStage();
            assertNotNull(stage);

            assertTrue(stageInjector.getParent() instanceof StackPane);

            stageInjector.show();
            assertTrue(stage.isShowing());
            stage.close();
        });

    }

    @Test
    void getStage_MainStage() {
        Platform.runLater(() -> {
            StageInjector stageInjector = new StageInjectorService(new MainModelController(model));
            assertNotNull(stageInjector);

            Stage stage = stageInjector.getStage();
            assertNotNull(stage);

            assertTrue(stageInjector.getParent() instanceof AnchorPane);

            stageInjector.show();
            assertTrue(stage.isShowing());
            stage.close();
        });
    }
}