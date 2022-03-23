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
import de.pedigreeProject.kinship.CloseKinshipUpdater;
import de.pedigreeProject.kinship.GeneticKinshipCalculator;
import de.pedigreeProject.kinship.KinshipSorterImpl;
import de.pedigreeProject.model.Model;
import de.pedigreeProject.model.Pedigree;
import de.pedigreeProject.model.Person;
import de.pedigreeProject.utils.IndexChanger;
import de.pedigreeProject.utils.gui_utils.StageInjector;
import de.pedigreeProject.utils.gui_utils.StageInjectorService;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StageInjectorServiceTestFX extends ApplicationTest {

    private Model model;
    private PedigreeGateway pedigreeGateway;
    private Pedigree testPedigree;
    private Person person;
    private static final TestDatabaseCleaner CLEANER = new TestDatabaseCleaner();

    @Start
    public void start(Stage stage) {
        CLEANER.deleteRecords();

        Connection connection = new DatabaseConnectionSqlite(DatabaseName.TEST.toString()).getConnection();
        GatewayFactory gatewayFactory = new GatewayFactory(connection);
        pedigreeGateway = gatewayFactory.getPedigreeGateway();
        PersonGateway personGateway = gatewayFactory.getPersonGateway();
        model = new Model(gatewayFactory, new KinshipSorterImpl(), new CloseKinshipUpdater(), new GeneticKinshipCalculator(), new IndexChanger());

        testPedigree = pedigreeGateway.createPedigree("Test", "").orElse(null);
        assertNotNull(testPedigree);
        person = personGateway.createPerson(testPedigree, "TestPersons", "", null).orElse(null);
        assertNotNull(person);

        StageInjector stageInjector = new StageInjectorService(new MainModelController(model));
        assertNotNull(stageInjector);
        stage = stageInjector.getStage();
        stage.show();
        stage.toFront();
    }

    @AfterEach
    void tearDown() {
        pedigreeGateway.deletePedigree(testPedigree);
    }

    @Test
    @DisplayName("illegal controller class input")
    void illegalController_null() {
        Platform.runLater(() -> assertThrows(NullPointerException.class, () -> new StageInjectorService(null)));
    }

    @Test
    @DisplayName("get parent node of controller class")
    void getParentWithoutStage() {
        Platform.runLater(() -> {
            StageInjector stageInjector = new StageInjectorService(new InputRelativesController(model, person));
            assertNotNull(stageInjector);

            assertTrue(stageInjector.getParent() instanceof AnchorPane);
        });
    }

    @Test
    @DisplayName("test show()")
    void show() {
        InputRelativesController mockedController = Mockito.mock(InputRelativesController.class);

        Platform.runLater(() -> {
            StageInjector stageInjector = new StageInjectorService(mockedController);

            stageInjector.show();
            assertTrue(stageInjector.getStage().isShowing());
        });
    }

    @Test
    @DisplayName("InputRelativesController()")
    void getStage_InputRelativesStage() {
        Platform.runLater(() -> {
            StageInjector stageInjector = new StageInjectorService(new InputRelativesController(model, person));
            assertNotNull(stageInjector);

            Stage stage = stageInjector.getStage();
            assertNotNull(stage);

            assertTrue(stageInjector.getParent() instanceof AnchorPane);

            assertStageIsShowingAndClose(stageInjector, stage);
        });
    }

    @Test
    @DisplayName("UpdatePersonDataController()")
    void getStage_UpdatePersonsStage() {
        Platform.runLater(() -> {
            StageInjector stageInjector = new StageInjectorService(new UpdatePersonDataController(model, person));
            assertNotNull(stageInjector);

            Stage stage = stageInjector.getStage();
            assertNotNull(stage);

            assertTrue(stageInjector.getParent() instanceof AnchorPane);

            assertStageIsShowingAndClose(stageInjector, stage);
        });
    }

    @Test
    @DisplayName("NewPersonDataController()")
    void getStage_InputPersonsStage() {
        Platform.runLater(() -> {
            StageInjector stageInjector = new StageInjectorService(new NewPersonDataController(model));
            assertNotNull(stageInjector);

            Stage stage = stageInjector.getStage();
            assertNotNull(stage);

            assertTrue(stageInjector.getParent() instanceof AnchorPane);

            assertStageIsShowingAndClose(stageInjector, stage);
        });
    }

    @Test
    @DisplayName("NewPedigreeController()")
    void getStage_NewPedigreeStage() {
        Platform.runLater(() -> {
            StageInjector stageInjector = new StageInjectorService(new NewPedigreeController(model));
            assertNotNull(stageInjector);

            Stage stage = stageInjector.getStage();
            assertNotNull(stage);

            assertTrue(stageInjector.getParent() instanceof StackPane);

            assertStageIsShowingAndClose(stageInjector, stage);
        });
    }

    @Test
    @DisplayName("UpdatePedigreeController()")
    void getStage_UpdatePedigreeStage() {
        Platform.runLater(() -> {
            StageInjector stageInjector = new StageInjectorService(new UpdatePedigreeController(model));
            assertNotNull(stageInjector);

            Stage stage = stageInjector.getStage();
            assertNotNull(stage);

            assertTrue(stageInjector.getParent() instanceof StackPane);

            assertStageIsShowingAndClose(stageInjector, stage);
        });
    }

    @Test
    @DisplayName("MainModelController()")
    void getStage_MainStage() {
        Platform.runLater(() -> {
            StageInjector stageInjector = new StageInjectorService(new MainModelController(model));
            assertNotNull(stageInjector);

            Stage stage = stageInjector.getStage();
            assertNotNull(stage);

            assertTrue(stageInjector.getParent() instanceof AnchorPane);

            assertStageIsShowingAndClose(stageInjector, stage);
        });
    }

    private void assertStageIsShowingAndClose(StageInjector stageInjector, Stage stage) {
        stageInjector.show();
        assertTrue(stage.isShowing());
        stage.close();
    }
}