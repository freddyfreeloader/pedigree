package de.pedigreeProject.utils.alerts;

import javafx.scene.control.Alert;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Convenience class to centralize common alerts.
 */
public class MyAlerts {


    static final ResourceBundle resourceBundle = ResourceBundle.getBundle("alerts", Locale.getDefault());

    private MyAlerts() {
    }

    public static void showFileNotFoundAlert() {
        showAlert("file.not.exists");
    }

    public static void showActionCouldNotWorkAlert() {
        showAlert("action.could.not.work");
    }

    public static void showNameExistsAlert() {
        showAlert("name.already.exists");
    }

    public static void showTitleIsBlankAlert() {
        showAlert("title.is.blank");
    }

    public static void showNameIsBlankAlert() {
        showAlert("name.is.blank");
    }

    public static void showInvalidYearAlert() {
        showAlert("invalid.year");
    }

    private static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, resourceBundle.getString(message));
        alert.showAndWait();
    }
}
