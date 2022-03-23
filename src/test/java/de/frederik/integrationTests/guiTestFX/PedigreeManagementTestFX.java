package de.frederik.integrationTests.guiTestFX;

import com.sun.javafx.scene.control.LabeledText;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;
import org.testfx.robot.Motion;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import static de.frederik.integrationTests.guiTestFX.utils.NodesOfFxmls.*;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

public class PedigreeManagementTestFX extends BaseTestFXClass {

    private final static Logger logger = LogManager.getLogger("tests");

    final String DEFAULT_PEDIGREE = ResourceBundle.getBundle("pedigree").getString("default.pedigree.title");
    final String FIRST_TEST_PEDIGREE = "first test pedigree";
    final String SECOND_TEST_PEDIGREE = "second test pedigree";
    final String CHANGED_TITLE = "New Title Of Pedigree";
    final String EMPTY_TITLE = "";
    final String CHANGED_DESCRIPTION = "New Description";
    final String EXISTING_PEDIGREE1 = "Already existing Pedigree 1";
    final String EXISTING_PEDIGREE2 = "Already existing Pedigree 2";

    final String DELETE_ALERT = ResourceBundle.getBundle("alerts").getString("delete.pedigree");
    final String TITLE_IS_BLANK_ALERT = ResourceBundle.getBundle("alerts", Locale.getDefault()).getString("title.is.blank");
    final String TITLE_ALREADY_EXISTS_ALERT = ResourceBundle.getBundle("alerts", Locale.getDefault()).getString("name.already.exists");

    @Test
    @DisplayName("change title and description of pedigree, verify model and labels")
    void changeTitle() {

        changePedigreeNameInPedigreeVBox(CHANGED_TITLE, CHANGED_DESCRIPTION);

        verifyThat(PEDIGREE_TITEL_LABEL, LabeledMatchers.hasText(CHANGED_TITLE));
        verifyThat(PEDIGREE_DESCRIPTION_LABEL, LabeledMatchers.hasText(CHANGED_DESCRIPTION));

        assertEquals(CHANGED_TITLE, model.getCurrentPedigree().getTitle());
        assertEquals(CHANGED_DESCRIPTION, model.getCurrentPedigree().getDescription());
    }

    @Test
    @DisplayName("change only description of pedigree")
    void changeDescription() {

        String unchangedTitle = model.getCurrentPedigree().getTitle();

        changePedigreeNameInPedigreeVBox(unchangedTitle, CHANGED_DESCRIPTION);

        verifyThat(PEDIGREE_TITEL_LABEL, LabeledMatchers.hasText(unchangedTitle));
        verifyThat(PEDIGREE_DESCRIPTION_LABEL, LabeledMatchers.hasText(CHANGED_DESCRIPTION));

        assertEquals(unchangedTitle, model.getCurrentPedigree().getTitle());
        assertEquals(CHANGED_DESCRIPTION, model.getCurrentPedigree().getDescription());
    }

    @Test
    @DisplayName("change to an empty title should fail, verify alert dialog")
    void changeTitle_InvalidInput_EmptyTitle() {
        String oldTitle = getTitleOfPedigreeLabel();
        String oldDescription = getDescriptionOfPedigreeLabel();

        changePedigreeNameInPedigreeVBox(EMPTY_TITLE, CHANGED_DESCRIPTION);

        helper.verifyAlertDialogAndPressEnter(TITLE_IS_BLANK_ALERT);

        assertEquals(oldTitle, model.getCurrentPedigree().getTitle());
        assertEquals(oldDescription, model.getCurrentPedigree().getDescription());

        helper.fireButton(CANCEL_NEW_PEDIGREE);
    }

    @Test
    @DisplayName("change title to same values should pass")
    void changeTitle_SameTitle() {
        String oldTitle = getTitleOfPedigreeLabel();
        String oldDescription = getDescriptionOfPedigreeLabel();

        changePedigreeNameInPedigreeVBox(oldTitle, oldDescription);

        assertEquals(oldTitle, model.getCurrentPedigree().getTitle());
        assertEquals(oldDescription, model.getCurrentPedigree().getDescription());
    }

    @Test
    @DisplayName("change title of pedigree to already existing pedigree should fail")
    void changeTitle_InvalidInput_TitleExistAlready() {
        modelCreateNewPedigree(EXISTING_PEDIGREE1);
        modelCreateNewPedigree(EXISTING_PEDIGREE2);

        changePedigreeNameInPedigreeVBox(EXISTING_PEDIGREE1, "");

        helper.verifyAlertDialogAndPressEnter(TITLE_ALREADY_EXISTS_ALERT);

        helper.fireButton(CANCEL_NEW_PEDIGREE);
    }

    @Test
    @DisplayName("basic test to create new pedigree")
    void createNewPedigree() {
        createNewPedigreeInMenu(CHANGED_TITLE, CHANGED_DESCRIPTION);

        String titelLabel = getTitleOfPedigreeLabel();
        String descriptionLabel = getDescriptionOfPedigreeLabel();

        assertEquals(CHANGED_TITLE, model.getCurrentPedigree().getTitle());
        assertEquals(CHANGED_DESCRIPTION, model.getCurrentPedigree().getDescription());
        assertEquals(titelLabel, CHANGED_TITLE);
        assertEquals(descriptionLabel, CHANGED_DESCRIPTION);
    }

    @Test
    @DisplayName("create new pedigree with empty title should fail, verify alert dialog")
    void createNewPedigree_InvalidInput_EmptyTitle() {
        createNewPedigreeInMenu(EMPTY_TITLE, CHANGED_DESCRIPTION);

        helper.verifyAlertDialogAndPressEnter(TITLE_IS_BLANK_ALERT);

        helper.fireButton(CANCEL_NEW_PEDIGREE);
    }

    @Test
    @DisplayName("create new pedigree with already existing title name should fail, verify alert dialog")
    void createNewPedigree_InvalidInput_TitleAlreadyExists() {
        modelCreateNewPedigree(EXISTING_PEDIGREE1);

        createNewPedigreeInMenu(EXISTING_PEDIGREE1, CHANGED_DESCRIPTION);
        helper.verifyAlertDialogAndPressEnter(TITLE_ALREADY_EXISTS_ALERT);

        helper.fireButton(CANCEL_NEW_PEDIGREE);
    }

    @Test
    @DisplayName("recent menu should show existing pedigrees and open by click")
    void testMenu_Recent() {

        verifyThat(PEDIGREE_TITEL_LABEL, LabeledMatchers.hasText(DEFAULT_PEDIGREE));

        clickOn(MENU_FILE).clickOn(MENU_RECENT);
        clickLabelInMenu(DEFAULT_PEDIGREE);
        createTestPedigree(FIRST_TEST_PEDIGREE);

        verifyThat(PEDIGREE_TITEL_LABEL, LabeledMatchers.hasText(FIRST_TEST_PEDIGREE));

        createTestPedigree(SECOND_TEST_PEDIGREE);

        verifyThat(PEDIGREE_TITEL_LABEL, LabeledMatchers.hasText(SECOND_TEST_PEDIGREE));

        clickOn(MENU_FILE).clickOn(MENU_RECENT);
        clickLabelInMenu(DEFAULT_PEDIGREE);

        verifyThat(PEDIGREE_TITEL_LABEL, LabeledMatchers.hasText(DEFAULT_PEDIGREE));

        clickOn(MENU_FILE).clickOn(MENU_RECENT);
        clickLabelInMenu(FIRST_TEST_PEDIGREE);

        verifyThat(PEDIGREE_TITEL_LABEL, LabeledMatchers.hasText(FIRST_TEST_PEDIGREE));

        clickOn(MENU_FILE).clickOn(MENU_RECENT);
        clickLabelInMenu(SECOND_TEST_PEDIGREE);

        verifyThat(PEDIGREE_TITEL_LABEL, LabeledMatchers.hasText(SECOND_TEST_PEDIGREE));
    }

    @Test
    @DisplayName("delete pedigree: check synchronisation of pedigree Label and recent menu")
    void deletePedigree() {

        verifyThat(PEDIGREE_TITEL_LABEL, LabeledMatchers.hasText(DEFAULT_PEDIGREE));

        createTestPedigree(FIRST_TEST_PEDIGREE);

        verifyThat(PEDIGREE_TITEL_LABEL, LabeledMatchers.hasText(FIRST_TEST_PEDIGREE));

        createTestPedigree(SECOND_TEST_PEDIGREE);

        verifyThat(PEDIGREE_TITEL_LABEL, LabeledMatchers.hasText(SECOND_TEST_PEDIGREE));

        clickOn(MENU_FILE).clickOn(MENU_RECENT);
        assertTrue(verifyLabelIsInMenu(DEFAULT_PEDIGREE));
        assertTrue(verifyLabelIsInMenu(FIRST_TEST_PEDIGREE));
        assertTrue(verifyLabelIsInMenu(SECOND_TEST_PEDIGREE));

        clickOn(MENU_DELETE);
        helper.verifyAlertDialogAndPressEnter(DELETE_ALERT);
        type(KeyCode.ENTER);

        verifyThat(PEDIGREE_TITEL_LABEL, LabeledMatchers.hasText(FIRST_TEST_PEDIGREE));
        clickOn(MENU_FILE).clickOn(MENU_RECENT);
        assertTrue(verifyLabelIsInMenu(DEFAULT_PEDIGREE));
        assertTrue(verifyLabelIsInMenu(FIRST_TEST_PEDIGREE));
        assertFalse(verifyLabelIsInMenu(SECOND_TEST_PEDIGREE));

        clickOn(MENU_DELETE);
        helper.verifyAlertDialogAndPressEnter(DELETE_ALERT);
        type(KeyCode.ENTER);

        verifyThat(PEDIGREE_TITEL_LABEL, LabeledMatchers.hasText(DEFAULT_PEDIGREE));
        clickOn(MENU_FILE).clickOn(MENU_RECENT);
        assertTrue(verifyLabelIsInMenu(DEFAULT_PEDIGREE));
        assertFalse(verifyLabelIsInMenu(FIRST_TEST_PEDIGREE));
        assertFalse(verifyLabelIsInMenu(SECOND_TEST_PEDIGREE));

        clickOn(MENU_DELETE);
        helper.verifyAlertDialogAndPressEnter(DELETE_ALERT);

        type(KeyCode.ENTER);
        // if default pedigree is deleted, another new default pedigree is created
        verifyThat(PEDIGREE_TITEL_LABEL, LabeledMatchers.hasText(DEFAULT_PEDIGREE));
        clickOn(MENU_FILE).clickOn(MENU_RECENT);
        assertTrue(verifyLabelIsInMenu(DEFAULT_PEDIGREE));
        assertFalse(verifyLabelIsInMenu(FIRST_TEST_PEDIGREE));
        assertFalse(verifyLabelIsInMenu(SECOND_TEST_PEDIGREE));
    }

    private void createNewPedigreeInMenu(String title, String description) {
        clickOn(MENU_FILE).clickOn(MENU_NEW_PEDIGREE);

        TextField titleTF = lookup(TITLE_TF).queryAs(TextField.class);
        TextField descriptionTF = lookup(DESCRIPTION_TF).queryAs(TextField.class);

        changeTitleAndDescription(titleTF, descriptionTF, title, description);

        verifyThat(TITLE_TF, hasText(title));
        verifyThat(DESCRIPTION_TF, hasText(description));

        helper.fireButton(SAVE_NEW_PEDIGREE);
    }

    private void changePedigreeNameInPedigreeVBox(String title, String description) {
        clickOn(PEDIGREE_VBOX).interrupt();
        TextField titleTF = lookup(TITLE_TF).queryAs(TextField.class);
        TextField descriptionTF = lookup(DESCRIPTION_TF).queryAs(TextField.class);

        changeTitleAndDescription(titleTF, descriptionTF, title, description);

        verifyThat(TITLE_TF, TextInputControlMatchers.hasText(title));
        verifyThat(DESCRIPTION_TF, TextInputControlMatchers.hasText(description));

        helper.fireButton(SAVE_NEW_PEDIGREE);
        interrupt();
    }

    private String getTitleOfPedigreeLabel() {
        Label titleLabel = lookup(PEDIGREE_TITEL_LABEL).queryAs(Label.class);
        return titleLabel.getText();
    }

    private String getDescriptionOfPedigreeLabel() {
        Label descriptionLabel = lookup(PEDIGREE_DESCRIPTION_LABEL).queryAs(Label.class);
        return descriptionLabel.getText();
    }

    private void clickLabelInMenu(String textOfLabel) {
        Set<LabeledText> nodesInRecentMenu = lookup(textOfLabel).lookup(instanceOf(LabeledText.class)).queryAll();

        nodesInRecentMenu.forEach(menuNode -> {
            if (menuNode.getParent().toString().contains("ContextMenu")) {
                clickOn(menuNode, Motion.HORIZONTAL_FIRST);
            }
        });
    }

    private boolean verifyLabelIsInMenu(String pedigreeTitle) {
        Set<LabeledText> nodesInRecentMenu = lookup(pedigreeTitle).lookup(instanceOf(LabeledText.class)).queryAll();

        return nodesInRecentMenu.stream()
                .anyMatch(menuNode ->
                        menuNode.getParent().toString().contains("ContextMenu"));
    }

    private void createTestPedigree(String title) {
        clickOn(MENU_FILE).clickOn(MENU_NEW_PEDIGREE);
        interrupt();
        TextField titleTF = lookup(TITLE_TF).queryAs(TextField.class);
        TextField descriptionTF = lookup(DESCRIPTION_TF).queryAs(TextField.class);
        changeTitleAndDescription(titleTF, descriptionTF, title, "");
        helper.fireButton(SAVE_NEW_PEDIGREE);
    }

    private void modelCreateNewPedigree(String title) {
        Platform.runLater(() -> model.createNewPedigree(title, ""));
        interrupt();
    }

    private void changeTitleAndDescription(TextField titleTF, TextField descriptionTF, String title, String description) {
        Platform.runLater(() -> {
            titleTF.setText(title);
            descriptionTF.setText(description);
        });
        interrupt();
    }
}
