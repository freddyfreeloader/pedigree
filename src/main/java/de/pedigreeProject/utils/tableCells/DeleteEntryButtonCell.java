package de.pedigreeProject.utils.tableCells;

import de.pedigreeProject.model.Person;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

/**
 * Provides a {@code TableCell} with a button to delete TableView items WITHOUT alert dialog.
 *
 * @see DeletePersonButtonCell
 */
public class DeleteEntryButtonCell extends TableCell<Person, Integer> {

    public DeleteEntryButtonCell() {
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

            button.setOnAction(e -> getTableView().getItems().remove(getIndex()));

            setGraphic(button);
        }
    }
}
