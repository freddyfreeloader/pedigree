package de.frederik.unitTests.guiTestFX.mockedDependencies;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.ResourceBundle;

import static de.frederik.integrationTests.guiTestFX.utils.NodesOfFxmls.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class PedigreeControllerTest extends ModelWithMockedDependencies {


    /**
     * Tests if RuntimeException is being caught and alert dialog is shown.
     */
    @Test
    void createNewPedigreeFail() {
        when(pedigreeGatewayMock.createPedigree(anyString(), anyString())).thenThrow(new RuntimeException());


        clickOn(MENU_FILE).clickOn(MENU_NEW_PEDIGREE);

        TextField titleTF = lookup(TITLE_TF).queryAs(TextField.class);
        TextField descriptionTF = lookup(DESCRIPTION_TF).queryAs(TextField.class);

        String changedTitle = "New Title Of Pedigree";
        String changedDescription = "New Description";

        changeTitleAndDescription(titleTF, descriptionTF, changedTitle, changedDescription);

        helper.fireButton(SAVE_NEW_PEDIGREE);

        interrupt();
        try {
            helper.verifyAlertDialogAndPressEnter(ResourceBundle.getBundle("alerts", Locale.getDefault()).getString("action.could.not.work"));
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains(" "));
        }
    }

    private void changeTitleAndDescription(TextField titleTF, TextField descriptionTF, String title, String description) {
        Platform.runLater(() -> {
            titleTF.setText(title);
            descriptionTF.setText(description);
        });
        interrupt();
    }
}
