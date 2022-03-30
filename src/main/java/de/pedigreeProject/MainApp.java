package de.pedigreeProject;

import de.pedigreeProject.controller.MainModelController;
import de.pedigreeProject.database.DatabaseConnectionSqlite;
import de.pedigreeProject.database.GatewayFactory;
import de.pedigreeProject.kinship.*;
import de.pedigreeProject.model.Model;
import de.pedigreeProject.utils.IndexChanger;
import de.pedigreeProject.utils.gui_utils.StageInjectorService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Stream;


public class MainApp extends Application {
    final static Logger logger = LogManager.getLogger(MainApp.class.getName());

    static String databaseName = "./Data/pedigree.db";

    public MainApp() {
    }

    @Override
    public void start(Stage stage) throws IOException {
        Path path = Path.of(".");
//        Files.list(path).forEach(path1 -> System.out.println(path1));
        try (Stream<Path> paths = Files.walk(Paths.get("."))) {
            paths.filter(file -> file.toString().endsWith(".fxml"))
                    .forEach(System.out::println);
        }
        if (!Files.exists(Path.of("./Data"))) {

            Files.createDirectory(Paths.get("./Data"));
        }
        Model model = dependencyInjection(databaseName);
        try {
            new StageInjectorService(new MainModelController(model)).showAndWait();
        } catch (RuntimeException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ResourceBundle.getBundle("alerts", Locale.getDefault()).getString("database"));
            alert.showAndWait();
            logger.error("Database Error: <" + databaseName + ">", e);
            Platform.exit();
        }
    }

    private Model dependencyInjection(String databaseName) {
        Model model = null;
        try {
            Connection connection = new DatabaseConnectionSqlite(databaseName).getConnection();
            GatewayFactory gatewayFactory = new GatewayFactory(connection);
            KinshipUpdater kinshipUpdater = new CloseKinshipUpdater();
            KinshipSorter kinshipSorter = new KinshipSorterImpl();
            KinshipCalculator kinshipCalculator = new GeneticKinshipCalculator();
            IndexChanger indexChanger = new IndexChanger();

            model = new Model(gatewayFactory, kinshipSorter, kinshipUpdater, kinshipCalculator, indexChanger);
        } catch (RuntimeException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ResourceBundle.getBundle("alerts", Locale.getDefault()).getString("start"));
            alert.showAndWait();
            logger.error("Can't inject! provided database name: <" + databaseName + ">", e);
            Platform.exit();
        }
        return model;
    }

    public static void main(String[] args) {
        if (args != null && args.length == 1) {
            databaseName = args[0];
        }
        launch();
    }
}