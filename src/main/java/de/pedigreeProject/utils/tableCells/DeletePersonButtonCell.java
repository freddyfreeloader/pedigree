package de.pedigreeProject.utils.tableCells;

import de.pedigreeProject.model.Model;
import de.pedigreeProject.model.Person;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Provides a {@code TableCell} with a button to delete TableView items WITH alert dialog.
 */
public class DeletePersonButtonCell extends TableCell<Person, Integer> {

    private final Model model;

    public DeletePersonButtonCell(Model model) {
        this.model = model;
    }

    @Override
    public void updateItem(Integer item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(null);
            Button button = new Button();
            button.getStyleClass().addAll("close-button");

            setGraphic(button);

            button.setOnAction(e -> {
                Person person = getTableView().getItems().get(getIndex());

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, ResourceBundle.getBundle("alerts", Locale.getDefault()).getString("delete.person"));
                Optional<ButtonType> opt = alert.showAndWait();

                opt.ifPresent(buttonType -> {
                    if (buttonType.equals(ButtonType.OK)) {
                            model.deletePerson(person);
                    }
                });
            });
        }
    }
}