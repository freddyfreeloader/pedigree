package de.pedigreeProject.utils.gui_utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * <p>Is an convenience injector/factory for all {@link Stage} objects of the application to centralize Stage creating.<br>
 * The file name and title of each Stage are persists in static final fields,
 * so caller only have to pass the controller class as constructor parameter.</p>
 * <p>The {@code Stage}s are initialized with {@code Stage.initModality(Modality.APPLICATION_MODAL)}</p>
 */
public interface StageInjector {
    /**
     * Generates a {@link Stage} object and calls the {@code show()} method.
     */
    void show();

    /**
     * Generates a {@link Stage} object and calls the {@code showAndWait()} method.
     */
    void showAndWait();

    /**
     * Gets the generated {@link Stage}.
     *
     * @return the stage object
     */
    Stage getStage();

    /**
     * Gets the {@link Parent} from injected {@code FXMLLoader} <b>BEFORE</b> the {@link Stage} is initialized.
     * <p>This method provides only the {@code Parent} object that is returned from the {@link FXMLLoader#load()} ,<br>
     * with injected Controller class.<br>
     * So the caller can build his own {@link javafx.scene.Scene} and {@code Stage}.</P>
     *
     * @return the {@code Parent} of the injected {@code FXMLLoader}
     */
    Parent getParent();
}
