package de.frederik.integrationTests.guiTestFX.utils;

import com.sun.javafx.scene.control.LabeledText;
import de.pedigreeProject.model.Person;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.hamcrest.CoreMatchers;
import org.jetbrains.annotations.NotNull;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.base.WindowMatchers;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TableViewMatchers;
import org.testfx.robot.Motion;

import java.time.Year;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import static de.frederik.integrationTests.guiTestFX.utils.NodesOfFxmls.*;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.*;
import static org.testfx.matcher.base.WindowMatchers.isShowing;
import static org.testfx.matcher.control.TableViewMatchers.*;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

/**
 * Provides helper methods for GUI-Tests
 */
public class TestFxHelperMethods extends ApplicationTest {

    /**
     * Fills the TextField with the text.
     *
     * @param textFieldNode the fx:id of the node as '#XXX' String
     * @param text          the string to insert
     */
    public void fillTextField(String textFieldNode, String text) {
        TextField textField = lookup(textFieldNode).queryAs(TextField.class);
        Platform.runLater(() -> textField.setText(text));
        interrupt();
    }

    /**
     * Calls Button.fire() on the node
     *
     * @param fxId the fx:id of the node as '#XXX' String
     */
    public void fireButton(String fxId) {
        Button button = lookup(fxId).queryButton();
        fireButton(button);
    }

    /**
     * Calls Button.fire() on the button
     *
     * @param button the Button to fire
     */
    public void fireButton(Button button) {
        Platform.runLater(button::fire);
        interrupt();
    }

    /**
     * Verifies if a DialogPane with given contentText is shown, closes the dialog with ENTER.
     *
     * @param contentText the contentText of the alert
     */
    public void verifyAlertDialogAndPressEnter(String contentText) {
        verifyAlertDialog(contentText);
        type(KeyCode.ENTER);
    }

    /**
     * Verifies if a DialogPane with given contentText is shown, closes the dialog with ESCAPE.
     *
     * @param contentText the contentText of the alert
     */
    public void verifyAlertDialogAndPressEscape(String contentText) {
        verifyAlertDialog(contentText);
        type(KeyCode.ESCAPE);
    }
    private void verifyAlertDialog(String contentText) {
        Node dialog = lookup(".dialog-pane").query();
        final DialogPane dialogPane = (DialogPane) dialog.getScene().getRoot();
        assertTrue(dialogPane.getContentText().contains(contentText));
        verifyThat("OK", isVisible());
    }

    /**
     * Searches for a Label in the given Pane with the given text String.
     *
     * @param paneToSearch the fx:id of the Pane to search
     * @param textToSearch the String in the Label to look for
     * @return the founded Label or null
     */
    public Label getLabelFromPaneWithText(String paneToSearch, String textToSearch) {
        Node pane = lookup(paneToSearch).query();
        Set<Label> labels = from(pane).lookup(instanceOf(Label.class)).queryAll();
        Optional<Label> optionalLabel = labels.stream().filter(lb -> lb.getText().contains(textToSearch)).findFirst();
        assertTrue(optionalLabel.isPresent());
        return optionalLabel.get();
    }

    /**
     * Searches in the given Stage for a TableColumn with given String
     *
     * @param stage          the Stage where to search for
     * @param stringToSearch the String in the TableColumn
     * @return a {@code Predicate<Node>}
     */
    @NotNull
    public Predicate<Node> getTableColumn(Stage stage, String stringToSearch) {
        return node1 -> node1.getScene().getWindow() == stage &&
                node1.toString().contains(stringToSearch) &&
                node1.getStyleClass().contains("table-cell") &&
                node1.toString().contains("TableColumn");
    }

    /**
     * Searches for a TableColumn with given String within the same Stage of the given table
     *
     * @param table        the fx:id of the table to search from as '#XXX'
     * @param textToSearch the text to search
     * @return a {@code Node} (TableColumn) where the String and table matches
     */
    @SuppressWarnings("rawtypes")
    public Node getItemOfTableview(String table, String textToSearch) {
        TableView tableView = lookup(table).queryAs(TableView.class);
        Stage stage = (Stage) tableView.getScene().getWindow();
        return lookup(getTableColumn(stage, textToSearch)).query();
    }

    /**
     * Returns a {@code Predicate<Node>} that verifies if the Node contains the given CSS-Selector and <br>
     * contains both given strings in the object graph (in the TableRow)
     *
     * @param cssSelector      the CSS-Selector of the Node (Button)
     * @param tableRowElement1 first String to search in the TableRow
     * @param tableRowElement2 second String to search in the TableRow
     * @return a {@code Predicate<Node>} if arguments are found
     */
    @NotNull
    public Predicate<Node> isButtonInTableRow(String cssSelector, String tableRowElement1, String tableRowElement2) {

        return node -> node.toString().contains(cssSelector)
                && node.getParent().getParent().getChildrenUnmodifiable().toString().contains(tableRowElement1)
                && node.getParent().getParent().getChildrenUnmodifiable().toString().contains(tableRowElement2);
    }

    /**
     * Convenience method to drag a {@code TableView} item to another table within the same stage.
     *
     * @param source      the string to search in the source table
     * @param targetTable the {@code TableView} to drop the item
     */
    public void dragAndDropToTable(String source, String targetTable) {
        Node tableview = lookup(targetTable).query();
        Stage stage = (Stage) tableview.getScene().getWindow();
        Node node = lookup(getTableColumn(stage, source)).query();
        drag(node, MouseButton.PRIMARY).interact(() -> dropTo(targetTable));
    }

    /**
     * Verifies that the {@code Label} in the main-view has the given text if clicked on the given index in the main table.
     *
     * @param index   the index in the main table to click
     * @param strings the string in the label
     */
    public void checkLabels(int index, String... strings) {
        Node tableRow = lookup(".table-row-cell").nth(index).query();
        clickOn(tableRow);
        Label label = getLabelFromScrollPane(strings[0]);

        for (String text : strings) {
            verifyThat(label, LabeledMatchers.hasText(CoreMatchers.containsString(text)));
        }
    }

    /**
     * Convenience method to insert a new entry in the main table.<br>
     * Year of birth is null.<br>
     * Fires 'add entry button', inserts arguments and types ENTER.
     *
     * @param givenName  the given name to insert
     * @param familyName the family name to insert
     */
    public void addNewEntry(String givenName, String familyName) {
        addNewEntry(givenName, familyName, null);
    }

    /**
     * Convenience method to insert a new entry in the main table.<br>
     * Family name is empty, year of birth is null.<br>
     * Fires 'add entry button', inserts arguments and types ENTER.
     *
     * @param givenName the given name to insert
     */
    public void addNewEntry(String givenName) {
        addNewEntry(givenName, "", null);
    }

    /**
     * Convenience method to insert a new entry in the main table.<br>
     * Family name is empty.<br>
     * Fires 'add entry button', inserts arguments and types ENTER.
     *
     * @param givenName   the given name to insert
     * @param yearOfBirth the year of birth to insert
     */
    public void addNewEntry(String givenName, Year yearOfBirth) {
        addNewEntry(givenName, "", yearOfBirth);
    }

    /**
     * Convenience method to insert a new entry in the main table.<br>
     * Family name is empty.<br>
     * Fires 'add entry button', inserts arguments and types ENTER.
     *
     * @param givenName   the given name to insert
     * @param yearOfBirth the year of birth to insert
     */
    public void addNewEntry(String givenName, int yearOfBirth) {
        addNewEntry(givenName, "", Year.of(yearOfBirth));
    }

    /**
     * Convenience method to insert a new entry in the main table.
     * Fires 'add entry button', inserts arguments and types ENTER.
     *
     * @param givenName   the given name to insert
     * @param familyName  the family name to insert
     * @param yearOfBirth the year of birth to insert
     */
    public void addNewEntry(String givenName, String familyName, Year yearOfBirth) {
        Button addEntryButton = lookup(ADD_PERSON_BUTTON).queryButton();
        Platform.runLater(addEntryButton::fire);
        interrupt();
        Platform.runLater(() -> {

            TextField givenNameTF = lookup(GIVEN_NAME_TF).queryAs(TextField.class);
            TextField familyNameTF = lookup(FAMILY_NAME_TF).queryAs(TextField.class);
            TextField yearOfBirthTF = lookup(YEAR_OF_BIRTH_TF).queryAs(TextField.class);
            givenNameTF.setText(givenName);
            familyNameTF.setText(familyName);
            if (yearOfBirth != null) {
                yearOfBirthTF.setText(yearOfBirth.toString());
            }
        });
        interrupt();
        type(KeyCode.ENTER);
    }

    /**
     * Convenience method to create a new pedigree via menu.
     * Description is empty.
     * @param title the title of pedigree
     */
    public void createTestPedigree(String title) {
        createNewPedigreeInMenu(title, "");
    }

    /**
     * Convenience method to create a new pedigree via menu.
     *
     * @param title       the title of pedigree
     * @param description the description of pedigree
     */
    public void createNewPedigreeInMenu(String title, String description) {
        clickOn(MENU_FILE).clickOn(MENU_NEW_PEDIGREE);
        changePedigreeName(title, description);
    }

    /**
     * Convenience method to change pedigree title and description.
     *
     * @param title       the title of pedigree
     * @param description the description of pedigree
     */
    public void changePedigreeNameInPedigreeVBox(String title, String description) {
        clickOn(PEDIGREE_VBOX).interrupt();
        changePedigreeName(title, description);
    }

    private void changePedigreeName(String title, String description) {
        TextField titleTF = lookup(TITLE_TF).queryAs(TextField.class);
        TextField descriptionTF = lookup(DESCRIPTION_TF).queryAs(TextField.class);

        changeTitleAndDescriptionOfPedigree(titleTF, descriptionTF, title, description);

        verifyThat(TITLE_TF, hasText(title));
        verifyThat(DESCRIPTION_TF, hasText(description));

        fireButton(SAVE_NEW_PEDIGREE);
        interrupt();
    }

    private void changeTitleAndDescriptionOfPedigree(TextField titleTF, TextField descriptionTF, String title, String description) {
        Platform.runLater(() -> {
            titleTF.setText(title);
            descriptionTF.setText(description);
        });
        interrupt();
    }

    public String getTitleOfPedigreeLabel() {
        Label titleLabel = lookup(PEDIGREE_TITEL_LABEL).queryAs(Label.class);
        return titleLabel.getText();
    }

    public String getDescriptionOfPedigreeLabel() {
        Label descriptionLabel = lookup(PEDIGREE_DESCRIPTION_LABEL).queryAs(Label.class);
        return descriptionLabel.getText();
    }

    /**
     * Searches for a Label with given text in Menu and click it.
     *
     * @param textOfLabel the text of the label to search for
     */
    public void clickLabelInMenu(String textOfLabel) {
        Set<LabeledText> nodesInRecentMenu = lookup(textOfLabel).lookup(instanceOf(LabeledText.class)).queryAll();

        nodesInRecentMenu.forEach(menuNode -> {
            if (menuNode.getParent().toString().contains("ContextMenu")) {
                clickOn(menuNode, Motion.HORIZONTAL_FIRST);
            }
        });
    }

    public boolean verifyTextIsInRecentMenu(String pedigreeTitle) {
        Set<LabeledText> nodesInRecentMenu = lookup(pedigreeTitle).lookup(instanceOf(LabeledText.class)).queryAll();

        return nodesInRecentMenu.stream()
                .anyMatch(menuNode ->
                        menuNode.getParent().toString().contains("ContextMenu"));
    }


    public void fillAllTextFieldsAndSave(String givenName, String familyName, String yearOfBirth) {
        fillTextField(GIVEN_NAME_TF, givenName);
        fillTextField(FAMILY_NAME_TF, familyName);
        fillTextField(YEAR_OF_BIRTH_TF, yearOfBirth);
        fireButton(SAVE_BUTTON_PERSON_DATA);
    }

    public void fireEditPersonButton(String givenName, String familyName) {
        Button editButton = lookup(isButtonInTableRow(EDIT_SELECTOR, givenName, familyName)).queryButton();
        fireButton(editButton);
    }

    public Set<Label> getLabelsFromScrollPane() {
        Node scrollPane = lookup(SCROLL_PANE).query();
        return from(scrollPane).lookup(instanceOf(Label.class)).queryAll();
    }

    public Label getLabelFromScrollPane(String identifier) {
        Set<Label> labels = getLabelsFromScrollPane();
        return labels.stream().filter(label -> label.getText().contains(identifier)).findFirst().orElse(null);
    }

    public boolean mostlyOneLabelIsTranslated() {
        Set<Label> labels = getLabelsFromScrollPane();
        return labels.stream().anyMatch(label -> HBox.getMargin(label).getTop() > 0);
    }

    public void fireEditRelativesButton(String givenName, String yearOfBirth) {
        Button editButton = lookup(isButtonInTableRow(ADD_RELATIVES_CSS, givenName, yearOfBirth)).queryButton();
        fireButton(editButton);
        interrupt();
    }

    public void fireDeleteButton(String givenName, String tableRowElement) {
        Button deleteButton = lookup(isButtonInTableRow(CLOSE_BUTTON_RELATIVES, givenName, tableRowElement)).queryButton();
        Platform.runLater(deleteButton::fire);
        interrupt();
    }

    public void verifyStageIsShowing(String stageRoot) {
        verifyThat(stageRoot, isNotNull());
        Node stage = lookup(stageRoot).query();
        verifyThat(window(stage), isShowing());
    }

    public void personsTableHasOnlyThisMembers(Person... persons) {
        verifyThat(PERSONS_TABLE, hasNumRows(persons.length));
        Arrays.stream(persons).forEach(person -> verifyThat(PERSONS_TABLE, hasTableCell(person)));
    }

    public void parentsTableHasOnlyThisMembers(Person... persons) {
        verifyThat(PARENTS_TABLE, hasNumRows(persons.length));
        Arrays.stream(persons).forEach(person -> verifyThat(PARENTS_TABLE, hasTableCell(person)));
    }

    public void spousesTableHasOnlyThisMembers(Person... persons) {
        verifyThat(SPOUSES_TABLE, hasNumRows(persons.length));
        Arrays.stream(persons).forEach(person -> verifyThat(SPOUSES_TABLE, hasTableCell(person)));
    }

    public void siblingsTableHasOnlyThisMembers(Person... persons) {
        verifyThat(SIBLINGS_TABLE, hasNumRows(persons.length));
        Arrays.stream(persons).forEach(person -> verifyThat(SIBLINGS_TABLE, hasTableCell(person)));
    }

    public void childrenTableHasOnlyThisMembers(Person... persons) {
        verifyThat(CHILDREN_TABLE, hasNumRows(persons.length));
        Arrays.stream(persons).forEach(person -> verifyThat(CHILDREN_TABLE, hasTableCell(person)));
    }
}
