package de.frederik.integrationTests.guiTestFX;

import de.pedigreeProject.model.Person;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.matcher.control.TableViewMatchers;

import java.time.Year;
import java.util.ResourceBundle;

import static de.frederik.integrationTests.guiTestFX.utils.NodesOfFxmls.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TableViewMatchers.hasNumRows;

public class MainTableViewTestFX extends BaseTestFXClass {


    @Test
    @DisplayName("main table should show content and sort correctly")
    void tableView_Content_And_SortOrder() {

        TableView<Person> table = lookup(TABLE).queryTableView();
        assertNotNull(table);
        verifyThat(table, hasNumRows(0));

        helper.addNewEntry(GIVEN_NAME1, FAMILY_NAME1, Year.of(Integer.parseInt(YEAR_OF_BIRTH1)));

        verifyThat(table, hasNumRows(1));
        Person person = table.getItems().get(0);
        verifyPersonIsInSync(person, GIVEN_NAME1, FAMILY_NAME1, YEAR_OF_BIRTH1);

        helper.addNewEntry(GIVEN_NAME2, FAMILY_NAME2, Year.of(Integer.parseInt(YEAR_OF_BIRTH2)));

        verifyThat(table, hasNumRows(2));
        Person person2 = table.getItems().get(1);
        verifyPersonIsInSync(person2, GIVEN_NAME2, FAMILY_NAME2, YEAR_OF_BIRTH2);

        helper.addNewEntry(GIVEN_NAME, FAMILY_NAME, Year.of(Integer.parseInt(YEAR_OF_BIRTH)));

        verifyThat(table, hasNumRows(3));

        //  person3 should be at first position because of the sort order of tableview
        Person person3 = table.getItems().get(0);

        verifyPersonIsInSync(person3, GIVEN_NAME, FAMILY_NAME, YEAR_OF_BIRTH);
    }

    private void verifyPersonIsInSync(Person person, String givenName, String familyName, String yearOfBirth) {
        assertEquals(person.getGivenName(), givenName);
        assertEquals(person.getFamilyName(), familyName);
        assertEquals(person.getYearOfBirth().orElse(null), Year.of(Integer.parseInt(yearOfBirth)));
    }

    @Test
    @DisplayName("test function of buttons in table row")
    void tableView_RowButtons() {

        helper.addNewEntry(GIVEN_NAME1, FAMILY_NAME1, Year.of(Integer.parseInt(YEAR_OF_BIRTH1)));
        helper.addNewEntry(GIVEN_NAME2, FAMILY_NAME2, Year.of(Integer.parseInt(YEAR_OF_BIRTH2)));
        helper.fireEditRelativesButton(GIVEN_NAME1, FAMILY_NAME1);

        helper.verifyStageIsShowing(INPUT_RELATIVES_ROOT);

        type(KeyCode.ENTER);
        helper.fireEditPersonButton(GIVEN_NAME1, FAMILY_NAME1);

        helper.verifyStageIsShowing(DATA_INPUT_ROOT);

        helper.fireButton(CANCEL_BUTTON_PERSON_DATA);
        helper.fireDeleteButton(GIVEN_NAME1, FAMILY_NAME1);

        helper.verifyAlertDialogAndPressEnter(ResourceBundle.getBundle("alerts").getString("delete.person"));
        verifyThat(TABLE, hasNumRows(1));

        helper.fireDeleteButton(GIVEN_NAME2, FAMILY_NAME2);

        helper.verifyAlertDialogAndPressEnter(ResourceBundle.getBundle("alerts").getString("delete.person"));
        verifyThat(TABLE, hasNumRows(0));
    }

    @Test
    @DisplayName("Selected person should be selected in pedigree view and vice-versa")
    void selectedPerson() {
        helper.addNewEntry(GIVEN_NAME1, FAMILY_NAME1, null);
        helper.addNewEntry(GIVEN_NAME2, FAMILY_NAME2, null);
        helper.fireEditRelativesButton(GIVEN_NAME1, FAMILY_NAME1);
        helper.dragAndDropToTable(GIVEN_NAME2, PARENTS_TABLE);
        push(KeyCode.ENTER);

        // if clicked on person in table, person label in pedigree view is selected
        clickOn(GIVEN_NAME1).interrupt();
        Label firstPersonLabel = helper.getLabelFromScrollPane(GIVEN_NAME1);
        Label secondPersonLabel = helper.getLabelFromScrollPane(GIVEN_NAME2);
        assertTrue(firstPersonLabel.getStyleClass().contains("selected-person"));
        assertFalse(secondPersonLabel.getStyleClass().contains("selected-person"));

        clickOn(GIVEN_NAME2).interrupt();
        firstPersonLabel = helper.getLabelFromScrollPane(GIVEN_NAME1);
        secondPersonLabel = helper.getLabelFromScrollPane(GIVEN_NAME2);
        assertFalse(firstPersonLabel.getStyleClass().contains("selected-person"));
        assertTrue(secondPersonLabel.getStyleClass().contains("selected-person"));

        // if clicked on person label, person in tableView is selected
        clickOn(firstPersonLabel);
        TableView<Person> table = lookup(TABLE).queryTableView();
        assertEquals(GIVEN_NAME1, table.getSelectionModel().getSelectedItems().get(0).getGivenName());

        secondPersonLabel = helper.getLabelFromScrollPane(GIVEN_NAME2);
        clickOn(secondPersonLabel);
        table = lookup(TABLE).queryTableView();
        assertEquals(GIVEN_NAME2, table.getSelectionModel().getSelectedItems().get(0).getGivenName());
    }
}
