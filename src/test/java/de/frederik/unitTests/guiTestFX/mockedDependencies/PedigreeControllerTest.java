package de.frederik.unitTests.guiTestFX.mockedDependencies;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ResourceBundle;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class PedigreeControllerTest extends ModelWithMockedDependencies {


    @Test
    @DisplayName("Tests if RuntimeException is being caught and alert dialog is shown.")
    void createNewPedigreeFail() {
        when(pedigreeGatewayMock.createPedigree(anyString(), anyString())).thenThrow(new RuntimeException());
        helper.createNewPedigreeInMenu("new pedigree", "");
        String alertMessage = ResourceBundle.getBundle("alerts").getString("action.could.not.work");

        helper.verifyAlertDialogAndPressEnter(alertMessage);
    }
}
