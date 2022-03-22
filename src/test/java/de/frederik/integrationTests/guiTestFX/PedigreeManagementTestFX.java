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

    @Test
    @DisplayName("change title and description of pedigree, verify model and labels")
    void changeTitle() {

        String changedTitle = "New Title Of Pedigree";
        String changedDescription = "New Description";

        changePedigreeNameInPedigreeVBox(changedTitle, changedDescription);

        verifyThat(PEDIGREE_TITEL_LABEL, LabeledMatchers.hasText(changedTitle));
        verifyThat(PEDIGREE_DESCRIPTION_LABEL, LabeledMatchers.hasText(changedDescription));

        assertEquals(changedTitle, model.getCurrentPedigree().getTitle());
        assertEquals(changedDescription, model.getCurrentPedigree().getDescription());
    }
    @Test
    @DisplayName("change description of pedigree")
    void changeDescription() {

        String changedTitle = model.getCurrentPedigree().getTitle();
        String changedDescription = "New Description";

        changePedigreeNameInPedigreeVBox(changedTitle, changedDescription);

        verifyThat(PEDIGREE_TITEL_LABEL, LabeledMatchers.hasText(changedTitle));
        verifyThat(PEDIGREE_DESCRIPTION_LABEL, LabeledMatchers.hasText(changedDescription));

        assertEquals(changedTitle, model.getCurrentPedigree().getTitle());
        assertEquals(changedDescription, model.getCurrentPedigree().getDescription());
    }

    @Test
    @DisplayName("change to an empty title should fail, verify alert dialog")
    void changeTitle_InvalidInput_EmptyTitle() {
        String oldTitle = getTitleOfPedigreeLabel();
        String oldDescription = getDescriptionOfPedigreeLabel();

        String changedTitle = "";
        String changedDescription = "New Description";

        changePedigreeNameInPedigreeVBox(changedTitle, changedDescription);

        helper.verifyAlertDialogAndPressEnter(ResourceBundle.getBundle("alerts", Locale.getDefault()).getString("title.is.blank"));

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
        String existingPedigree1 = "Already existing Pedigree 1";
        String existingPedigree2 = "Already existing Pedigree 2";

        modelCreateNewPedigree(existingPedigree1);
        modelCreateNewPedigree(existingPedigree2);

        changePedigreeNameInPedigreeVBox(existingPedigree1, "");

        helper.verifyAlertDialogAndPressEnter(ResourceBundle.getBundle("alerts", Locale.getDefault()).getString("name.already.exists"));

        helper.fireButton(CANCEL_NEW_PEDIGREE);
    }

    @Test
    @DisplayName("basic test to create new pedigree")
    void createNewPedigree() {
        String title = "New Title Of Pedigree";
        String description = "New Description";

        createNewPedigreeInMenu(title, description);

        String titelLabel = getTitleOfPedigreeLabel();
        String descriptionLabel = getDescriptionOfPedigreeLabel();

        assertEquals(title, model.getCurrentPedigree().getTitle());
        assertEquals(description, model.getCurrentPedigree().getDescription());
        assertEquals(titelLabel, title);
        assertEquals(descriptionLabel, description);
    }

    @Test
    @DisplayName("create new pedigree with empty title should fail, verify alert dialog")
    void createNewPedigree_InvalidInput_EmptyTitle() {
        String title = "";
        String description = "New Description";

        createNewPedigreeInMenu(title, description);

        helper.verifyAlertDialogAndPressEnter(ResourceBundle.getBundle("alerts", Locale.getDefault()).getString("title.is.blank"));

        helper.fireButton(CANCEL_NEW_PEDIGREE);
    }

    @Test
    @DisplayName("create new pedigree with already existing title name should fail, verify alert dialog")
    void createNewPedigree_InvalidInput_TitleAlreadyExists() {
        String existingPedigree1 = "Already existing Pedigree 1";
        String changedDescription = "New Description";

        modelCreateNewPedigree(existingPedigree1);

        createNewPedigreeInMenu(existingPedigree1, changedDescription);
        helper.verifyAlertDialogAndPressEnter(ResourceBundle.getBundle("alerts", Locale.getDefault()).getString("name.already.exists"));

        helper.fireButton(CANCEL_NEW_PEDIGREE);
    }

    @Test
    @DisplayName("recent menu should show existing pedigrees and open by click")
    void testMenu_Recent() {
        final String DEFAULT_PEDIGREE = ResourceBundle.getBundle("pedigree").getString("default.pedigree.title");
        final String FIRST_TEST_PEDIGREE = "first test pedigree";
        final String SECOND_TEST_PEDIGREE = "second test pedigree";

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
        final String DEFAULT_PEDIGREE = ResourceBundle.getBundle("pedigree").getString("default.pedigree.title");
        final String FIRST_TEST_PEDIGREE = "first test pedigree";
        final String SECOND_TEST_PEDIGREE = "second test pedigree";

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
        helper.verifyAlertDialogAndPressEnter("");
        type(KeyCode.ENTER);

        verifyThat(PEDIGREE_TITEL_LABEL, LabeledMatchers.hasText(FIRST_TEST_PEDIGREE));
        clickOn(MENU_FILE).clickOn(MENU_RECENT);
        assertTrue(verifyLabelIsInMenu(DEFAULT_PEDIGREE));
        assertTrue(verifyLabelIsInMenu(FIRST_TEST_PEDIGREE));
        assertFalse(verifyLabelIsInMenu(SECOND_TEST_PEDIGREE));

        clickOn(MENU_DELETE);
        helper.verifyAlertDialogAndPressEnter("");
        type(KeyCode.ENTER);

        verifyThat(PEDIGREE_TITEL_LABEL, LabeledMatchers.hasText(DEFAULT_PEDIGREE));
        clickOn(MENU_FILE).clickOn(MENU_RECENT);
        assertTrue(verifyLabelIsInMenu(DEFAULT_PEDIGREE));
        assertFalse(verifyLabelIsInMenu(FIRST_TEST_PEDIGREE));
        assertFalse(verifyLabelIsInMenu(SECOND_TEST_PEDIGREE));

        clickOn(MENU_DELETE);
        helper.verifyAlertDialogAndPressEnter("");

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
