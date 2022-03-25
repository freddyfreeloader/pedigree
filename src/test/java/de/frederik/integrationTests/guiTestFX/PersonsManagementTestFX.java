package de.frederik.integrationTests.guiTestFX;

import de.pedigreeProject.model.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static de.frederik.integrationTests.guiTestFX.utils.NodesOfFxmls.*;
import static de.frederik.testUtils.ConstantsForTesting.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isFocused;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.TableViewMatchers.hasNumRows;
import static org.testfx.matcher.control.TableViewMatchers.hasTableCell;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

@ExtendWith(MockitoExtension.class)
public class PersonsManagementTestFX extends BaseTestFXClass {


    @Test
    @DisplayName("basic test visibility nodes of stage")
    void basicTest() {
        helper.fireButton(ADD_PERSON_BUTTON);

        verifyThat(GIVEN_NAME_TF, isVisible());
        verifyThat(FAMILY_NAME_TF, isVisible());
        verifyThat(YEAR_OF_BIRTH_TF, isVisible());
        verifyThat(SAVE_BUTTON_PERSON_DATA, isVisible());
        verifyThat(CANCEL_BUTTON_PERSON_DATA, isVisible());

        helper.fireButton(CANCEL_BUTTON_PERSON_DATA);
    }

    @Test
    @DisplayName("add new Person: valid input should synchronized with model and tableView")
    void addNewPerson_validInput() {
        helper.addNewEntry(GIVEN_NAME, FAMILY_NAME, YEAR_OF_BIRTH);

        Person person = getPersonByGivenName(GIVEN_NAME);
        assertNotNull(person);
        assertEquals(GIVEN_NAME, person.getGivenName());
        assertEquals(FAMILY_NAME, person.getFamilyName());
        assertEquals(YEAR_OF_BIRTH, person.getYearOfBirth().orElse(null));
        verifyThat(TABLE, hasNumRows(1));
        verifyThat(TABLE, hasTableCell(GIVEN_NAME));
    }

    @Test
    @DisplayName("addNewPerson() invalid input: empty full name , verify alert dialog")
    void addNewPerson_invalid_empty_name() {
        helper.addNewEntry("", "", null);

        helper.verifyAlertDialogAndPressEnter(NAME_IS_BLANK_ALERT);

        helper.fireButton(CANCEL_BUTTON_PERSON_DATA);
    }

    @Test
    @DisplayName("addNewPerson() invalid input: invalid year of birth, verify alert dialog")
    void addNewPerson_invalid_year() {
        helper.fireButton(ADD_PERSON_BUTTON);
        helper.fillAllTextFieldsAndSave("Test", "Test", "invalid Year");

        helper.verifyAlertDialogAndPressEnter(INVALID_YEAR_ALERT);
        verifyThat(YEAR_OF_BIRTH_TF, isFocused());

        helper.fireButton(CANCEL_BUTTON_PERSON_DATA);
    }

    @Test
    @DisplayName("addNewPerson() invalid input: person already exists in database, verify alert dialog")
    void addNewPerson_invalid_person_already_exists() {
        helper.addNewEntry(GIVEN_NAME, FAMILY_NAME, YEAR_OF_BIRTH);
        helper.addNewEntry(GIVEN_NAME, FAMILY_NAME, YEAR_OF_BIRTH);

        helper.verifyAlertDialogAndPressEnter(NAME_ALREADY_EXISTS_ALERT);

        helper.fireButton(CANCEL_BUTTON_PERSON_DATA);
    }

    @Test
    @DisplayName("updatePersonData(): basic test text field ")
    void updatePersonData_textFields() {
        helper.addNewEntry(GIVEN_NAME, FAMILY_NAME, YEAR_OF_BIRTH);

        helper.fireEditPersonButton(GIVEN_NAME, FAMILY_NAME);

        verifyThat(GIVEN_NAME_TF, hasText(GIVEN_NAME));
        verifyThat(FAMILY_NAME_TF, hasText(FAMILY_NAME));
        verifyThat(YEAR_OF_BIRTH_TF, hasText(YEAR_OF_BIRTH.toString()));

        helper.fireButton(CANCEL_BUTTON_PERSON_DATA);
    }

    @Test
    @DisplayName("updatePersonData(): valid input, verify sync with table and model ")
    void updatePersonData_valid_input() {
        helper.addNewEntry(GIVEN_NAME, FAMILY_NAME, YEAR_OF_BIRTH);

        Person person = getPersonByGivenName(GIVEN_NAME);
        assertNotNull(person);
        assertEquals(GIVEN_NAME, person.getGivenName());
        assertEquals(FAMILY_NAME, person.getFamilyName());
        assertEquals(YEAR_OF_BIRTH, person.getYearOfBirth().orElse(null));
        verifyThat(TABLE, hasNumRows(1));
        verifyThat(TABLE, hasTableCell(GIVEN_NAME));

        helper.fireEditPersonButton(GIVEN_NAME, FAMILY_NAME);
        helper.fillAllTextFieldsAndSave(GIVEN_NAME1, FAMILY_NAME1, YEAR_OF_BIRTH1.toString());

        assertEquals(GIVEN_NAME1, person.getGivenName());
        assertEquals(FAMILY_NAME1, person.getFamilyName());
        assertEquals(YEAR_OF_BIRTH1, person.getYearOfBirth().orElse(null));

        verifyThat(TABLE, hasNumRows(1));
        verifyThat(TABLE, hasTableCell(GIVEN_NAME1));
    }

    @Test
    @DisplayName("update person data person already exists, verify alert dialog")
    void updatePersonData_Invalid_Entry() {
        helper.addNewEntry(GIVEN_NAME, FAMILY_NAME, YEAR_OF_BIRTH);
        helper.addNewEntry(GIVEN_NAME2, FAMILY_NAME2, YEAR_OF_BIRTH2);

        helper.fireEditPersonButton(GIVEN_NAME2, FAMILY_NAME2);
        helper.fillAllTextFieldsAndSave(GIVEN_NAME, FAMILY_NAME, YEAR_OF_BIRTH.toString());

        helper.verifyAlertDialogAndPressEnter(NAME_ALREADY_EXISTS_ALERT);
    }
}