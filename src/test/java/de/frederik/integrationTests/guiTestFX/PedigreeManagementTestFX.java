package de.frederik.integrationTests.guiTestFX;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static de.frederik.integrationTests.guiTestFX.utils.NodesOfFxmls.*;
import static de.frederik.testUtils.ConstantsForTesting.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

public class PedigreeManagementTestFX extends BaseTestFXClass {

    private final static Logger logger = LogManager.getLogger("tests");

    @Test
    @DisplayName("change title and description of pedigree, verify model and labels")
    void changeTitle() {

        helper.changePedigreeNameInPedigreeVBox(CHANGED_TITLE, CHANGED_DESCRIPTION);

        verifyThat(PEDIGREE_TITEL_LABEL, hasText(CHANGED_TITLE));
        verifyThat(PEDIGREE_DESCRIPTION_LABEL, hasText(CHANGED_DESCRIPTION));

        assertEquals(CHANGED_TITLE, model.getCurrentPedigree().getTitle());
        assertEquals(CHANGED_DESCRIPTION, model.getCurrentPedigree().getDescription());
    }

    @Test
    @DisplayName("change only description of pedigree")
    void changeDescription() {

        String unchangedTitle = model.getCurrentPedigree().getTitle();

        helper.changePedigreeNameInPedigreeVBox(unchangedTitle, CHANGED_DESCRIPTION);

        verifyThat(PEDIGREE_TITEL_LABEL, hasText(unchangedTitle));
        verifyThat(PEDIGREE_DESCRIPTION_LABEL, hasText(CHANGED_DESCRIPTION));

        assertEquals(unchangedTitle, model.getCurrentPedigree().getTitle());
        assertEquals(CHANGED_DESCRIPTION, model.getCurrentPedigree().getDescription());
    }

    @Test
    @DisplayName("change to an empty title should fail, verify alert dialog")
    void changeTitle_InvalidInput_EmptyTitle() {
        String oldTitle = helper.getTitleOfPedigreeLabel();
        String oldDescription = helper.getDescriptionOfPedigreeLabel();

        helper.changePedigreeNameInPedigreeVBox(EMPTY_TITLE, CHANGED_DESCRIPTION);

        helper.verifyAlertDialogAndPressEnter(TITLE_IS_BLANK_ALERT);

        assertThat(oldTitle).isEqualTo(model.getCurrentPedigree().getTitle());
        assertThat(oldDescription).isEqualTo(model.getCurrentPedigree().getDescription());

        helper.fireButton(CANCEL_NEW_PEDIGREE);
    }

    @Test
    @DisplayName("change title to same values should pass")
    void changeTitle_SameTitle() {
        String oldTitle = helper.getTitleOfPedigreeLabel();
        String oldDescription = helper.getDescriptionOfPedigreeLabel();

        helper.changePedigreeNameInPedigreeVBox(oldTitle, oldDescription);

        assertEquals(oldTitle, model.getCurrentPedigree().getTitle());
        assertEquals(oldDescription, model.getCurrentPedigree().getDescription());
    }

    @Test
    @DisplayName("change title of pedigree to already existing pedigree should fail")
    void changeTitle_InvalidInput_TitleExistAlready() {
        modelCreateNewPedigree(EXISTING_PEDIGREE1);
        modelCreateNewPedigree(EXISTING_PEDIGREE2);

        helper.changePedigreeNameInPedigreeVBox(EXISTING_PEDIGREE1, "");

        helper.verifyAlertDialogAndPressEnter(TITLE_ALREADY_EXISTS_ALERT);

        helper.fireButton(CANCEL_NEW_PEDIGREE);
    }

    @Test
    @DisplayName("basic test to create new pedigree")
    void createNewPedigree() {
        helper.createNewPedigreeInMenu(CHANGED_TITLE, CHANGED_DESCRIPTION);

        String titelLabel = helper.getTitleOfPedigreeLabel();
        String descriptionLabel = helper.getDescriptionOfPedigreeLabel();

        assertEquals(CHANGED_TITLE, model.getCurrentPedigree().getTitle());
        assertEquals(CHANGED_DESCRIPTION, model.getCurrentPedigree().getDescription());
        assertEquals(titelLabel, CHANGED_TITLE);
        assertEquals(descriptionLabel, CHANGED_DESCRIPTION);
    }

    @Test
    @DisplayName("create new pedigree with empty title should fail, verify alert dialog")
    void createNewPedigree_InvalidInput_EmptyTitle() {
        helper.createNewPedigreeInMenu(EMPTY_TITLE, CHANGED_DESCRIPTION);

        helper.verifyAlertDialogAndPressEnter(TITLE_IS_BLANK_ALERT);

        helper.fireButton(CANCEL_NEW_PEDIGREE);
    }

    @Test
    @DisplayName("create new pedigree with already existing title name should fail, verify alert dialog")
    void createNewPedigree_InvalidInput_TitleAlreadyExists() {
        modelCreateNewPedigree(EXISTING_PEDIGREE1);

        helper.createNewPedigreeInMenu(EXISTING_PEDIGREE1, CHANGED_DESCRIPTION);
        helper.verifyAlertDialogAndPressEnter(TITLE_ALREADY_EXISTS_ALERT);

        helper.fireButton(CANCEL_NEW_PEDIGREE);
    }

    @Test
    @DisplayName("recent menu should show existing pedigrees and open by click")
    void testMenu_Recent() {

        verifyThat(PEDIGREE_TITEL_LABEL, hasText(DEFAULT_PEDIGREE));

        clickOn(MENU_FILE).clickOn(MENU_RECENT);
        helper.clickLabelInMenu(DEFAULT_PEDIGREE);
        helper.createTestPedigree(FIRST_TEST_PEDIGREE);

        verifyThat(PEDIGREE_TITEL_LABEL, hasText(FIRST_TEST_PEDIGREE));

        helper.createTestPedigree(SECOND_TEST_PEDIGREE);

        verifyThat(PEDIGREE_TITEL_LABEL, hasText(SECOND_TEST_PEDIGREE));

        clickOn(MENU_FILE).clickOn(MENU_RECENT);
        helper.clickLabelInMenu(DEFAULT_PEDIGREE);

        verifyThat(PEDIGREE_TITEL_LABEL, hasText(DEFAULT_PEDIGREE));

        clickOn(MENU_FILE).clickOn(MENU_RECENT);
        helper.clickLabelInMenu(FIRST_TEST_PEDIGREE);

        verifyThat(PEDIGREE_TITEL_LABEL, hasText(FIRST_TEST_PEDIGREE));

        clickOn(MENU_FILE).clickOn(MENU_RECENT);
        helper.clickLabelInMenu(SECOND_TEST_PEDIGREE);

        verifyThat(PEDIGREE_TITEL_LABEL, hasText(SECOND_TEST_PEDIGREE));
    }

    @Test
    @DisplayName("delete pedigree: check synchronisation of pedigree Label and recent menu")
    void deletePedigree() {

        verifyThat(PEDIGREE_TITEL_LABEL, hasText(DEFAULT_PEDIGREE));

        helper.createTestPedigree(FIRST_TEST_PEDIGREE);

        verifyThat(PEDIGREE_TITEL_LABEL, hasText(FIRST_TEST_PEDIGREE));

        helper.createTestPedigree(SECOND_TEST_PEDIGREE);

        verifyThat(PEDIGREE_TITEL_LABEL, hasText(SECOND_TEST_PEDIGREE));

        clickOn(MENU_FILE).clickOn(MENU_RECENT);
        assertTrue(helper.verifyLabelIsInMenu(DEFAULT_PEDIGREE));
        assertTrue(helper.verifyLabelIsInMenu(FIRST_TEST_PEDIGREE));
        assertTrue(helper.verifyLabelIsInMenu(SECOND_TEST_PEDIGREE));

        clickOn(MENU_DELETE);
        helper.verifyAlertDialogAndPressEnter(DELETE_ALERT);
        type(KeyCode.ENTER);

        verifyThat(PEDIGREE_TITEL_LABEL, hasText(FIRST_TEST_PEDIGREE));
        clickOn(MENU_FILE).clickOn(MENU_RECENT);
        assertTrue(helper.verifyLabelIsInMenu(DEFAULT_PEDIGREE));
        assertTrue(helper.verifyLabelIsInMenu(FIRST_TEST_PEDIGREE));
        assertFalse(helper.verifyLabelIsInMenu(SECOND_TEST_PEDIGREE));

        clickOn(MENU_DELETE);
        helper.verifyAlertDialogAndPressEnter(DELETE_ALERT);
        type(KeyCode.ENTER);

        verifyThat(PEDIGREE_TITEL_LABEL, hasText(DEFAULT_PEDIGREE));
        clickOn(MENU_FILE).clickOn(MENU_RECENT);
        assertTrue(helper.verifyLabelIsInMenu(DEFAULT_PEDIGREE));
        assertFalse(helper.verifyLabelIsInMenu(FIRST_TEST_PEDIGREE));
        assertFalse(helper.verifyLabelIsInMenu(SECOND_TEST_PEDIGREE));

        clickOn(MENU_DELETE);
        helper.verifyAlertDialogAndPressEnter(DELETE_ALERT);

        type(KeyCode.ENTER);
        // if default pedigree is deleted, another new default pedigree is created
        verifyThat(PEDIGREE_TITEL_LABEL, hasText(DEFAULT_PEDIGREE));
        clickOn(MENU_FILE).clickOn(MENU_RECENT);
        assertTrue(helper.verifyLabelIsInMenu(DEFAULT_PEDIGREE));
        assertFalse(helper.verifyLabelIsInMenu(FIRST_TEST_PEDIGREE));
        assertFalse(helper.verifyLabelIsInMenu(SECOND_TEST_PEDIGREE));
    }

    private void modelCreateNewPedigree(String title) {
        Platform.runLater(() -> model.createNewPedigree(title, ""));
        interrupt();
    }
}
