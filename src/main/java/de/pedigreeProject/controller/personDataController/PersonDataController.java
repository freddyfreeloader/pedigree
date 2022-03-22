package de.pedigreeProject.controller.personDataController;

import de.pedigreeProject.model.Model;
import de.pedigreeProject.utils.alerts.MyAlerts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;

import java.time.Year;
import java.time.format.DateTimeParseException;

/**
 * An abstract class for controller classes to create or update <code>Person</code> objects.<br>
 * It supports the fxml for the view and common methods:<br>
 * <ul>
 *     <li><code>cancel()</code> - closes the stage</li>
 *     <li>{@link #validateAndSave} - validates the TextField inputs and persists into database</li>
 *     </ul>
 * <p>
 * to implement in subclasses:<br>
 * <ul>
 *     <li>{@link #init()} - (optional) to extend the initialize method</li>
 *     <li>{@link #save } - to persist changes</li>
 * </ul>
 * Implementations: {@link NewPersonDataController}, {@link UpdatePersonDataController}
 */
public abstract class PersonDataController {

//    static final Logger logger = LogManager.getLogger(PersonDataController.class);

    @FXML
    TextField givenNameTF;
    @FXML
    TextField familyNameTF;
    @FXML
    TextField yearOfBirthTF;
    @FXML
    Button saveButton;
    @SuppressWarnings("unused")
    @FXML
    Button cancelButton;

    final Model model;

    /**
     * Constructor for abstract class PersonDataController
     *
     * @param model the Model object
     */
    public PersonDataController(Model model) {
        this.model = model;
    }

    /**
     * Called automatically after construction, defines the behaviour of the buttons <br>
     * and call the {@link #init()} method.
     */
    @SuppressWarnings("unused")
    @FXML
    private void initialize() {

        saveButton.setOnAction(this::validateAndSave);
        cancelButton.setOnAction(this::cancel);
        init();
    }

    /**
     * Method to extend the <code>initialize()</code> method in the abstract basis class.
     */
    abstract void init();

    /**
     * Validates the TextFields and persists the person in the database.<br>
     * <p>It is called by pressing the save button</p>
     * <ul>
     *     <li>TextFields inputs are stripped</li>
     *     <li>given name and family are validated to not be both blank/null(returns with an alert)</li>
     *     <li>Year of birth is validated (returns with an alert)</li>
     *     <li>if {@link #save} method returns false,an alert dialog will be shown.</li>
     *
     * @param actionEvent used to get the Stage object to close
     */
    private void validateAndSave(ActionEvent actionEvent) {

        String year = yearOfBirthTF.getText().strip();
        String givenName = givenNameTF.getText().strip();
        String familyName = familyNameTF.getText().strip();

        if (StringUtils.isAllBlank(givenName, familyName)) {
            MyAlerts.showNameIsBlankAlert();
            return;
        }

        Year yearOfBirth = null;
        // yearOfBirth is allowed to be null
        if (year != null && !year.isBlank()) {
            try {
                yearOfBirth = Year.parse(year);

            } catch (DateTimeParseException dateTimeParseException) {
                MyAlerts.showInvalidYearAlert();
                yearOfBirthTF.requestFocus();
                return;
            }
        }
        try {
            boolean successful = save(givenName, familyName, yearOfBirth);
            if (!successful) {
                MyAlerts.showNameExistsAlert();
                return;
            }
        } catch (RuntimeException e) {
            MyAlerts.showActionCouldNotWorkAlert();
        }
        cancel(actionEvent);
    }

    /**
     * Saves the new person or changes of the person data into database.<br>
     * It is called after the validation of the TextField inputs.
     * Must return <code>false</code> if a person with same given name, family name and year of birth already exists in database,
     * so validation could return an alert to the user.
     *
     * @param givenName   the given name of the person, may be null
     * @param familyName  the family name of the person, may be null
     * @param yearOfBirth the year of birth of the person, may be null or empty (=unknown)
     * @return <code>true</code> if saving/changing was successful or <code>false</code> if a person with same given/family name and year of birth already exists
     * @throws RuntimeException if underlying SQL statements or Connection object are invalid
     */
    abstract boolean save(String givenName, String familyName, Year yearOfBirth);

    /**
     * Closes the stage, activated by cancel button.
     *
     * @param actionEvent passed by cancel button
     */
    @FXML
    private void cancel(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }
}