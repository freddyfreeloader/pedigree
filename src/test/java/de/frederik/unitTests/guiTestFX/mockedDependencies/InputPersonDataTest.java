package de.frederik.unitTests.guiTestFX.mockedDependencies;

import javafx.application.Platform;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.ResourceBundle;

import static de.frederik.integrationTests.guiTestFX.utils.NodesOfFxmls.*;
import static de.frederik.integrationTests.guiTestFX.utils.NodesOfFxmls.SAVE_BUTTON_PERSON_DATA;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class InputPersonDataTest extends ModelWithMockedDependencies{
    /**
     * Tests if RuntimeException is being caught and alert dialog is shown.
     */
    @Test
    void createNewPersonFail() {
        when(personGatewayMock.createPerson(any(), anyString(), anyString(), any())).thenThrow(new RuntimeException());

        helper.fireButton(ADD_PERSON_BUTTON);
        interrupt();
        helper.fillTextField(GIVEN_NAME_TF, "Test");
        helper.fillTextField(FAMILY_NAME_TF, "Test");
        helper.fireButton(SAVE_BUTTON_PERSON_DATA);
        interrupt();
        helper.verifyAlertDialogAndPressEnter(ResourceBundle.getBundle("alerts", Locale.getDefault()).getString("action.could.not.work"));


        Platform.runLater(() -> assertThrows(RuntimeException.class, () -> clickOn(SAVE_BUTTON_PERSON_DATA)));
    }
}
