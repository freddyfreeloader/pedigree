package de.frederik.integrationTests.guiTestFX;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static de.frederik.integrationTests.guiTestFX.utils.NodesOfFxmls.*;
import static de.frederik.testUtils.testData.BaseFamily.*;
import static org.hamcrest.core.IsNot.not;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

public class MoveLabelsTestFX extends BaseTestFXClass {

    @Test
    @DisplayName("test function of move buttons")
    void testMoveLabels() {
        createBaseFamilyPedigree();

        clickOn(helper.getItemOfTableview(TABLE, ME));

        moveToLabel(BROTHER); // brother is on left edge, so only moveRight button should appear
        verifyThat(MOVE_RIGHT, isVisible());
        verifyThat(MOVE_LEFT, not(isVisible()));

        helper.fireButton(MOVE_RIGHT);

        moveToLabel(BROTHER); // brother is now in the middle, so both move buttons should appear
        verifyThat(MOVE_RIGHT, isVisible());
        verifyThat(MOVE_LEFT, isVisible());

        helper.fireButton(MOVE_RIGHT);

        moveToLabel(BROTHER); // brother is on the right edge, so only moveLeft button should appear
        verifyThat(MOVE_RIGHT, not(isVisible()));
        verifyThat(MOVE_LEFT, isVisible());

        helper.fireButton(MOVE_LEFT);

        moveToLabel(BROTHER);// brother is now in the middle, so both move buttons should appear
        verifyThat(MOVE_RIGHT, isVisible());
        verifyThat(MOVE_LEFT, isVisible());

        clickOn(helper.getItemOfTableview(TABLE, MOTHER));

        moveToLabel(MOTHER); // mother has no neighbours, so no move button should appear
        verifyThat(MOVE_RIGHT, not(isVisible()));
        verifyThat(MOVE_LEFT, not(isVisible()));
    }

    private void moveToLabel(String labelText) {
        moveTo(helper.getLabelFromPaneWithText(SCROLL_PANE, labelText));
    }
}
