package de.frederik.integrationTests.guiTestFX;

import de.pedigreeProject.model.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.TableViewMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;

import java.util.Locale;
import java.util.ResourceBundle;

import static de.frederik.integrationTests.guiTestFX.utils.NodesOfFxmls.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.testfx.api.FxAssert.verifyThat;

@ExtendWith(MockitoExtension.class)
public class PersonsManagementTestFX extends BaseTestFXClass {

    @Test
    @DisplayName("basic test visibility nodes of stage")
    void basicTest() {
        helper.fireButton(ADD_PERSON_BUTTON);

        verifyThat(GIVEN_NAME_TF, NodeMatchers.isVisible());
        verifyThat(FAMILY_NAME_TF, NodeMatchers.isVisible());
        verifyThat(YEAR_OF_BIRTH_TF, NodeMatchers.isVisible());
        verifyThat(SAVE_BUTTON_PERSON_DATA, NodeMatchers.isVisible());
        verifyThat(CANCEL_BUTTON_PERSON_DATA, NodeMatchers.isVisible());

        helper.fireButton(CANCEL_BUTTON_PERSON_DATA);
    }

    @Test
    @DisplayName("add new Person: valid input should synchronized with model and tableView")
    void addNewPerson_validInput() {
        addNewPerson(GIVEN_NAME, FAMILY_NAME, YEAR_OF_BIRTH);

        Person person = getPersonByGivenName(GIVEN_NAME);
        assertNotNull(person);
        assertEquals(GIVEN_NAME, person.getGivenName());
        assertEquals(FAMILY_NAME, person.getFamilyName());
        assertEquals(YEAR_OF_BIRTH, person.getYearOfBirth().orElse(null).toString());
        verifyThat(TABLE, TableViewMatchers.hasNumRows(1));
        verifyThat(TABLE, TableViewMatchers.hasTableCell(GIVEN_NAME));
    }

    @Test
    @DisplayName("addNewPerson() invalid input: empty full name , verify alert dialog")
    void addNewPerson_invalid_empty_name() {
        addNewPerson("", "", "");

        helper.verifyAlertDialogAndPressEnter(ResourceBundle.getBundle("alerts", Locale.getDefault()).getString("name.is.blank"));

        helper.fireButton(CANCEL_BUTTON_PERSON_DATA);
    }

    @Test
    @DisplayName("addNewPerson() invalid input: invalid year of birth, verify alert dialog")
    void addNewPerson_invalid_year() {
        addNewPerson(GIVEN_NAME, FAMILY_NAME, "invalidYear");

        helper.verifyAlertDialogAndPressEnter(ResourceBundle.getBundle("alerts", Locale.getDefault()).getString("invalid.year"));
        verifyThat(YEAR_OF_BIRTH_TF, NodeMatchers.isFocused());

        helper.fireButton(CANCEL_BUTTON_PERSON_DATA);
    }

    @Test
    @DisplayName("addNewPerson() invalid input: person already exists in database, verify alert dialog")
    void addNewPerson_invalid_person_already_exists() {
        addNewPerson(GIVEN_NAME, FAMILY_NAME, YEAR_OF_BIRTH);
        addNewPerson(GIVEN_NAME, FAMILY_NAME, YEAR_OF_BIRTH);

        helper.verifyAlertDialogAndPressEnter(ResourceBundle.getBundle("alerts", Locale.getDefault()).getString("name.already.exists"));

        helper.fireButton(CANCEL_BUTTON_PERSON_DATA);
    }

    @Test
    @DisplayName("updatePersonData(): basic test text field ")
    void updatePersonData_textFields() {
        addNewPerson(GIVEN_NAME, FAMILY_NAME, YEAR_OF_BIRTH);

        fireEditPersonButton(GIVEN_NAME, FAMILY_NAME);

        verifyThat(GIVEN_NAME_TF, TextInputControlMatchers.hasText(GIVEN_NAME));
        verifyThat(FAMILY_NAME_TF, TextInputControlMatchers.hasText(FAMILY_NAME));
        verifyThat(YEAR_OF_BIRTH_TF, TextInputControlMatchers.hasText(YEAR_OF_BIRTH));

        helper.fireButton(CANCEL_BUTTON_PERSON_DATA);
    }

    @Test
    @DisplayName("updatePersonData(): valid input, verify sync with table and model ")
    void updatePersonData_valid_input() {
        addNewPerson(GIVEN_NAME, FAMILY_NAME, YEAR_OF_BIRTH);

        Person person = getPersonByGivenName(GIVEN_NAME);
        assertNotNull(person);
        assertEquals(GIVEN_NAME, person.getGivenName());
        assertEquals(FAMILY_NAME, person.getFamilyName());
        assertEquals(YEAR_OF_BIRTH, person.getYearOfBirth().orElse(null).toString());
        verifyThat(TABLE, TableViewMatchers.hasNumRows(1));
        verifyThat(TABLE, TableViewMatchers.hasTableCell(GIVEN_NAME));

        fireEditPersonButton(GIVEN_NAME, FAMILY_NAME);
        fillAllTextFieldsAndSave(GIVEN_NAME1, FAMILY_NAME1, YEAR_OF_BIRTH1);

        assertEquals(GIVEN_NAME1, person.getGivenName());
        assertEquals(FAMILY_NAME1, person.getFamilyName());
        assertEquals(YEAR_OF_BIRTH1, person.getYearOfBirth().orElse(null).toString());

        verifyThat(TABLE, TableViewMatchers.hasNumRows(1));
        verifyThat(TABLE, TableViewMatchers.hasTableCell(GIVEN_NAME1));
    }

    @Test
    @DisplayName("update person data person already exists, verify alert dialog")
    void updatePersonData_Invalid_Entry() {
        addNewPerson(GIVEN_NAME, FAMILY_NAME, YEAR_OF_BIRTH);
        addNewPerson(GIVEN_NAME2, FAMILY_NAME2, YEAR_OF_BIRTH2);

        fireEditPersonButton(GIVEN_NAME2, FAMILY_NAME2);
        fillAllTextFieldsAndSave(GIVEN_NAME, FAMILY_NAME, YEAR_OF_BIRTH);

        helper.verifyAlertDialogAndPressEnter(ResourceBundle.getBundle("alerts", Locale.getDefault()).getString("name.already.exists"));
    }
}