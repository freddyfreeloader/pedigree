package de.frederik.integrationTests.guiTestFX;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.matcher.base.NodeMatchers;

import static de.frederik.integrationTests.guiTestFX.utils.NodesOfFxmls.MENU_EXIT;
import static de.frederik.integrationTests.guiTestFX.utils.NodesOfFxmls.MENU_FILE;
import static org.mockito.Mockito.doNothing;
import static org.testfx.api.FxAssert.verifyThat;

@ExtendWith(MockitoExtension.class)
public class ExitTestFX extends BaseTestFXClass {


    /*
     * Don't know a way to test if platform is exited.
     * So only check visibility.
     */
    @Test
    @DisplayName("exit menu should be visible")
    void test_exit() {
        doNothing().when(controller).exit();
        clickOn(MENU_FILE);
        verifyThat(MENU_EXIT, NodeMatchers.isVisible());
        clickOn(MENU_EXIT);
    }
}
