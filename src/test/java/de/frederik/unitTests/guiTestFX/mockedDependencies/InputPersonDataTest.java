package de.frederik.unitTests.guiTestFX.mockedDependencies;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ResourceBundle;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class InputPersonDataTest extends ModelWithMockedDependencies {

    @Test
    @DisplayName("Tests if RuntimeException is being caught and alert dialog is shown.")
    void createNewPersonFail() {
        when(personGatewayMock.createPerson(any(), anyString(), anyString(), any())).thenThrow(new RuntimeException());
        helper.addNewEntry("Test", "", null);

        String alertMessage = ResourceBundle.getBundle("alerts").getString("action.could.not.work");
        helper.verifyAlertDialogAndPressEnter(alertMessage);
    }
}
