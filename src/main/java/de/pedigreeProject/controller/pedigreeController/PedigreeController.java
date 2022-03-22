package de.pedigreeProject.controller.pedigreeController;

import de.pedigreeProject.utils.alerts.MyAlerts;
import de.pedigreeProject.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * An abstract class for controller classes to create or update Pedigrees.<br>
 * It supports the fxml for the view and common methods:<br>
 * <ul>
 *     <li><code>cancel()</code> - closes the stage</li>
 *     <li>{@link PedigreeController#validateAndSave} - validates the TextField inputs and persists into database</li>
 *     </ul>
 * <p>
 * to implement in subclasses:<br>
 * <ul>
 *     <li>{@link PedigreeController#save } - to persist changes</li>
 *     <li>{@link PedigreeController#init()} - optional to implement, extends the initialize method</li>
 * </ul>
 *  Implementations: {@link NewPedigreeController} , {@link UpdatePedigreeController}
 */
@SuppressWarnings("unused")
public abstract class PedigreeController {

//    final static Logger logger = LogManager.getLogger(PedigreeController.class.getName());

    @FXML
    TextField titleTextField;
    @FXML
    TextField descriptionTextField;
    @FXML
    Button saveButton;
    @FXML
    Button cancelButton;

    final Model model;

    /**
     * Constructor for abstract class PedigreeController
     *
     * @param model the Model object
     */
    public PedigreeController(Model model) {
        Objects.requireNonNull(model);
        this.model = model;
    }

    /**
     * Called automatically after construction, defines the behaviour of the buttons <br>
     * and calls the {@link #init()} method.
     */
    @SuppressWarnings("unused")
    public void initialize() {
        saveButton.setOnAction(this::validateAndSave);
        cancelButton.setOnAction(this::cancel);
        init();
    }

    /**
     * Method to extend the <code>initialize()</code> method in the abstract basis class.
     */
    abstract void init();

    /**
     * Saves the new pedigree or changes of the pedigree into database.<br>
     * It is called after the validation of the TextField inputs.
     * Must return <code>false</code> if new or changed title already exists in database,
     * so validation could return an alert to the user.
     *
     * @param title       the title of the pedigree
     * @param description an optional description of the pedigree
     * @return <code>true</code> if saving/changing was successful or <code>false</code> if a pedigree with same title already exists
     * @throws RuntimeException if underlying SQL statements or Connection object are invalid
     */
    abstract boolean save(String title, String description);

    /**
     * Closes this stage, called by pressing cancel button.
     *
     * @param actionEvent passed by the button event
     */
    @FXML
    private void cancel(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }

    /**
     * <p>Validates the TextFields and persists the pedigree in the database.<br>
     * <p>It is called by pressing the save button</p>
     * <ul>
     *     <li>Text inputs are stripped</li>
     *     <li>the title must not be blank or null (returns with an alert)</li>
     *     <li>the title must not be already exists in database (returns with an alert)</li>
     *
     * @param actionEvent used to get the Stage object to close
     */
    @FXML
    private void validateAndSave(ActionEvent actionEvent) {
        if (StringUtils.isBlank(titleTextField.getText())) {
            MyAlerts.showTitleIsBlankAlert();
            return;
        }
        String strippedTitle = titleTextField.getText().strip();
        String strippedDescription = descriptionTextField.getText().strip();
        try {

            boolean saveWasSuccessful = save(strippedTitle, strippedDescription);
            if (saveWasSuccessful) {
                cancel(actionEvent);
            } else {
                MyAlerts.showNameExistsAlert();
            }
        }catch (RuntimeException e) {
            MyAlerts.showActionCouldNotWorkAlert();
        }
    }
}
