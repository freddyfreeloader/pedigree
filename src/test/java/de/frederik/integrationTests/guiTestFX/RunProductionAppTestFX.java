package de.frederik.integrationTests.guiTestFX;

import de.frederik.testUtils.DatabaseName;
import de.frederik.testUtils.TestDatabaseCleaner;
import de.pedigreeProject.MainApp;
import de.pedigreeProject.controller.MainModelController;
import de.pedigreeProject.database.DatabaseConnectionSqlite;
import de.pedigreeProject.database.GatewayFactory;
import de.pedigreeProject.kinship.CloseKinshipUpdater;
import de.pedigreeProject.kinship.GeneticKinshipCalculator;
import de.pedigreeProject.kinship.KinshipSorterImpl;
import de.pedigreeProject.model.Model;
import de.pedigreeProject.utils.IndexChanger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.NodeMatchers;

import java.io.IOException;
import java.sql.Connection;
import java.util.ResourceBundle;

import static org.testfx.api.FxAssert.verifyThat;

public class RunProductionAppTestFX extends ApplicationTest {

    private Model model;
    private static final TestDatabaseCleaner CLEANER = new TestDatabaseCleaner();

    @Start
    public void start(Stage stage) throws IOException {
        CLEANER.deleteRecords();
        Connection connection = new DatabaseConnectionSqlite(DatabaseName.PRODUCTION.toString()).getConnection();
        GatewayFactory gatewayFactory = new GatewayFactory(connection);
        model = new Model(gatewayFactory, new KinshipSorterImpl(), new CloseKinshipUpdater(), new GeneticKinshipCalculator(), new IndexChanger());

        Callback<Class<?>, Object> controllerFactory = controllerType -> {
            if (controllerType == MainModelController.class) {
                return new MainModelController(model);
            } else {
                throw new IllegalStateException("Unexpected controller class: " + controllerType.getName());
            }
        };
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("pedigree.fxml"));
        fxmlLoader.setControllerFactory(controllerFactory);
        ResourceBundle bundle = ResourceBundle.getBundle("pedigree");
        fxmlLoader.setResources(bundle);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(bundle.getString("stage.title.main"));
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @Test
    @DisplayName("app should run with production database")
    void windowIsShown() {
        verifyThat("#mainPane", NodeMatchers.isVisible());
    }
}
