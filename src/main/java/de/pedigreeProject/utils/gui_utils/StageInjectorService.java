package de.pedigreeProject.utils.gui_utils;

import de.pedigreeProject.MainApp;
import de.pedigreeProject.utils.alerts.MyAlerts;
import de.pedigreeProject.controller.inputRelatives.InputRelativesController;
import de.pedigreeProject.controller.MainModelController;
import de.pedigreeProject.controller.pedigreeController.NewPedigreeController;
import de.pedigreeProject.controller.pedigreeController.UpdatePedigreeController;
import de.pedigreeProject.controller.personDataController.NewPersonDataController;
import de.pedigreeProject.controller.personDataController.UpdatePersonDataController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * <p>Is an injector for all the stages of the application.
 * The file name and title of each Stage are persists in static final fields,
 * so caller only have to pass the controller class as constructor parameter.</p>
 * <p>Note that this StageInjector is not a 'real' injector because it depends on the full initialized and already injected Controller class.<br>
 * Further implementations could change to inject the Controllers not by constructor but by usage of <code>setUserData</code> and <code>getUserData</code> methods.</p>
 *
 * <p>It divides internally between fxml files with included controller and fxml files without controller.<br>
 * Developer have to use the {@link #getParentBySetController} method for fxml WITHOUT controller,
 * and the {@link #getParentByControllerFactory} method for fxml WITH included controller. </p>
 */
public class StageInjectorService implements StageInjector {

    final static Logger logger = LogManager.getLogger(StageInjectorService.class.getName());
    /**
     * The controller class of the view
     */
    private final Object controllerClass;
    /**
     * The {@code Stage} to return with this {@code StageInjector},<br>
     * is not initialized if only {@link #getParent()} is called!
     */
    private Stage stage;
    /**
     * The parent {@code Node} of the {@code Stage}.
     */
    private Parent parent;
    /**
     * The title of the {@code Stage}.
     */
    private String title;

    static final ResourceBundle resourceBundle = ResourceBundle.getBundle("pedigree", getSystemLocale());

    private static final String MAIN_STAGE = "pedigree.fxml";
    private static final String MAIN_STAGE_TITLE = resourceBundle.getString("stage.title.main");

    private static final String DATA_INPUT = "dataInput.fxml";
    private static final String DATA_INPUT_TITLE = resourceBundle.getString("stage.title.personsData");

    private static final String INPUT_RELATIVES = "inputRelatives.fxml";
    private static final String INPUT_RELATIVES_TITLE = resourceBundle.getString("stage.title.kinship");

    private static final String NEW_PEDIGREE = "dialogNewPedigree.fxml";
    private static final String NEW_PEDIGREE_TITLE = "";

    /**
     * @param controllerClass the controller class of the view/fxml, not null
     * @see StageInjectorService
     */
    public StageInjectorService(Object controllerClass) {
        Objects.requireNonNull(controllerClass);
        this.controllerClass = controllerClass;
    }

    /**
     * @see StageInjector#getStage()
     */
    @Override
    public Stage getStage() {
        if (stage == null) {
            generateStage();
        }
        return stage;
    }

    /**
     * @see StageInjector#show()
     */
    @Override
    public void show() {
        if (stage == null) {
            generateStage();
        }
        stage.show();
    }

    /**
     * @see StageInjector#showAndWait()
     */
    @Override
    public void showAndWait() {
        if (stage == null) {
            generateStage();
        }
        stage.showAndWait();
    }

    /**
     * @see StageInjector#getParent()
     */
    @Override
    public Parent getParent() {
        if (parent == null) {
            generateParent();
        }
        return parent;
    }

    /**
     * Gets the generated Parent and title to build a {@code Stage}.<br>
     * Sets the Modality of the Stage to APPLICATION_MODAL
     */
    private void generateStage() {
        if (parent == null) {
            generateParent();
        }
        if (title == null) {
            getTitle();
        }
        if (parent==null) {
            System.out.println("Parent is null! Application can't start");
        }
        Scene scene = new Scene(parent);
        stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
    }

    /**
     * <p>Looks up the controller class to initialize {@link #title} </p>
     */
    private void getTitle() {

        if (controllerClass instanceof MainModelController) {
            title = MAIN_STAGE_TITLE;
        } else if (controllerClass instanceof UpdatePersonDataController) {
            title = DATA_INPUT_TITLE;
        } else if (controllerClass instanceof NewPersonDataController) {
            title = DATA_INPUT_TITLE;
        } else if (controllerClass instanceof UpdatePedigreeController) {
            title = NEW_PEDIGREE_TITLE;
        } else if (controllerClass instanceof NewPedigreeController) {
            title = NEW_PEDIGREE_TITLE;
        } else if (controllerClass instanceof InputRelativesController) {
            title = INPUT_RELATIVES_TITLE;
        }
    }

    /**
     * <p>Looks up the instance of controller class and calls the corresponding parent Node factory method
     * ({@link #getParentByControllerFactory} or {@link #getParentBySetController})
     * to initialize field {@link #parent}.</p>
     */
    private void generateParent() {
        try {
            if (controllerClass instanceof MainModelController) {
                parent = getParentByControllerFactory(MAIN_STAGE, controllerClass);
            } else if (controllerClass instanceof UpdatePersonDataController) {
                parent = getParentBySetController(DATA_INPUT, controllerClass);
            } else if (controllerClass instanceof NewPersonDataController) {
                parent = getParentBySetController(DATA_INPUT, controllerClass);
            } else if (controllerClass instanceof UpdatePedigreeController) {
                parent = getParentBySetController(NEW_PEDIGREE, controllerClass);
            } else if (controllerClass instanceof NewPedigreeController) {
                parent = getParentBySetController(NEW_PEDIGREE, controllerClass);
            } else if (controllerClass instanceof InputRelativesController) {
                parent = getParentByControllerFactory(INPUT_RELATIVES, controllerClass);
            }
        } catch (IllegalStateException | IOException e) {
            logger.error("can't open fxml file!" + e);
            MyAlerts.showFileNotFoundAlert();
        }
    }

    /**
     * <p>Returns a {@link Parent} of the fxml file, if there is NO fx:controller inside.</p>
     *
     * @param fxml_file  the fxml location of the file
     * @param controller the controller class of the fxml
     * @return a {@code Parent} object to set in a {@code Scene}
     * @throws IOException if fxml file is not found
     */
    private Parent getParentBySetController(String fxml_file, Object controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(fxml_file));
        fxmlLoader.setController(controller);
        fxmlLoader.setResources(resourceBundle);

        return fxmlLoader.load();
    }

    /**
     * <p>Returns a {@link Parent} of the fxml file, if there is a fx:controller inside already.</p>
     *
     * @param fxml_file  the fxml location of the file
     * @param controller the controller class of the fxml
     * @return a {@code Parent} object to set in a {@code Scene}
     * @throws IOException if fxml file is not found
     */
    private Parent getParentByControllerFactory(String fxml_file, Object controller) throws IOException {
        Callback<Class<?>, Object> controllerFactory = controllerType -> {
            if (controllerType == controller.getClass()) {
                return controllerClass;
            } else {
                logger.error("Unexpected controller class: " + controllerType.getName());
                throw new IllegalStateException("Unexpected controller class: " + controllerType.getName());
            }
        };
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(fxml_file));
        fxmlLoader.setControllerFactory(controllerFactory);
        fxmlLoader.setResources(resourceBundle);

        return fxmlLoader.load();
    }
    private static Locale getSystemLocale(){
        String country = System.getProperty("user.country");
        return new Locale(String.format("%s_%s", country.toLowerCase(),country.toUpperCase()));
    }
}
