package de.frederik.integrationTests.guiTestFX.utils;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.LabeledMatchers;

import java.time.Year;
import java.util.Set;
import java.util.function.Predicate;

import static de.frederik.integrationTests.guiTestFX.utils.NodesOfFxmls.*;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;

/**
 * Provides helper methods for GUI-Tests
 */
public class TestFxHelperMethods extends ApplicationTest {

    /**
     * Fills the TextField Node with the text.
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
        Platform.runLater(button::fire);
        interrupt();
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
        Node dialog = lookup(".dialog-pane").query();
        final DialogPane dialogPane = (DialogPane) dialog.getScene().getRoot();
        assertTrue(dialogPane.getContentText().contains(contentText));
        verifyThat("OK", NodeMatchers.isVisible());
        type(KeyCode.ENTER);
    }

    /**
     * Verifies if a DialogPane with given contentText is shown, closes the dialog with ESCAPE.
     *
     * @param contentText the contentText of the alert
     */
    public void verifyAlertDialogAndPressEscape(String contentText) {
        Node dialog = lookup(".dialog-pane").query();
        final DialogPane dialogPane = (DialogPane) dialog.getScene().getRoot();
        assertTrue(dialogPane.getContentText().contains(contentText));
        verifyThat("OK", NodeMatchers.isVisible());
        type(KeyCode.ESCAPE);
    }

    /**
     * Searches for a Label in the given Pane with the given text String.
     *
     * @param paneToSearch the fx:id of the pane to search
     * @param textToSearch the string in the Label to look for
     * @return the founded Label or null
     */
    public Label getLabelFromPaneWithText(String paneToSearch, String textToSearch) {
        Node pane = lookup(paneToSearch).query();
        Set<Label> labels = from(pane).lookup(instanceOf(Label.class)).queryAll();
        return labels.stream().filter(lb -> lb.getText().contains(textToSearch)).findFirst().orElse(null);
    }

    /**
     * Searches in the given Stage for a TableColumn with given string
     *
     * @param stage          the Stage where to search for
     * @param stringToSearch the string in the TableColumn
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
     * Searches for a TableColumn with given string within the same stage of the given table
     *
     * @param table        the fx:id of the table to search from as '#XXX'
     * @param textToSearch the text to search
     * @return a {@code Node} (TableColumn) where the string and table matches
     */
    @SuppressWarnings("rawtypes")
    public Node getItemOfTableview(String table, String textToSearch) {
        TableView tableView = lookup(table).queryAs(TableView.class);
        Stage stage = (Stage) tableView.getScene().getWindow();
        return lookup(getTableColumn(stage, textToSearch)).query();
    }

    /**
     * Returns a {@code Predicate<Node>} that verifies if the Node contains the given CSS-Selector and <br>
     * contains both given strings in the object graph (in th TableRow)
     *
     * @param cssSelector      the CSS-Selector of the Node (Button)
     * @param tableRowElement1 first string to search in the TableRow
     * @param tableRowElement2 first string to search in the TableRow
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
     * @param index       the index in the main table to click
     * @param nameAndYear the string in the label
     */
    public void checkLabels(int index, String nameAndYear) {
        VBox mainVbox = lookup(MAIN_VBOX).queryAs(VBox.class);
        assertNotNull(mainVbox);
        Node tableRow = lookup(".table-row-cell").nth(index).query();
        clickOn(tableRow);
        HBox hbox = (HBox) mainVbox.getChildren().get(0);
        Label label = (Label) hbox.getChildren().get(0);

        verifyThat(label, LabeledMatchers.hasText(nameAndYear));
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
}
