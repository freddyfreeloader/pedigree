package de.frederik.unitTests.guiTestFX.mockedDependencies;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ResourceBundle;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ExceptionTest extends ModelWithMockedDependencies {

    private final String alertMessage = ResourceBundle.getBundle("alerts").getString("action.could.not.work");

    @Test
    @DisplayName("PersonDataController(): Tests if RuntimeException is being caught and alert dialog is shown.")
    void createNewPersonFail() {
        when(personGatewayMock.createPerson(any(), anyString(), anyString(), any())).thenThrow(new RuntimeException());
        helper.addNewEntry("Test");

        helper.verifyAlertDialogAndPressEnter(alertMessage);
    }

    @Test
    @DisplayName("PedigreeController(): Tests if RuntimeException is being caught and alert dialog is shown.")
    void createNewPedigreeFail() {
        when(pedigreeGatewayMock.createPedigree(anyString(), anyString())).thenThrow(new RuntimeException());
        helper.createNewPedigreeInMenu("new pedigree", "");

        helper.verifyAlertDialogAndPressEnter(alertMessage);
    }
}
