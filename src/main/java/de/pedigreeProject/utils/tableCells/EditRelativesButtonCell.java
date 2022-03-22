package de.pedigreeProject.utils.tableCells;

import de.pedigreeProject.controller.inputRelatives.InputRelativesController;
import de.pedigreeProject.utils.gui_utils.StageInjectorService;
import de.pedigreeProject.model.Model;
import de.pedigreeProject.model.Person;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import org.kordamp.ikonli.javafx.FontIcon;
/**
 * Provides a {@code TableCell} with a button to open {@link InputRelativesController}.
 */
public class EditRelativesButtonCell extends TableCell<Person, Integer> {

    private final Model model;

    public EditRelativesButtonCell(Model model) {
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
            button.setGraphic(new FontIcon());
            button.setAlignment(Pos.CENTER_LEFT);
            button.getStyleClass().addAll("edit-relatives-button");

            Person person = getTableView().getItems().get(getIndex());

            button.setOnAction(e -> new StageInjectorService(new InputRelativesController(model, person)).showAndWait());
            setGraphic(button);
        }
    }
}
