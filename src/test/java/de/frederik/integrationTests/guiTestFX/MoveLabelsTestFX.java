package de.frederik.integrationTests.guiTestFX;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static de.frederik.integrationTests.guiTestFX.utils.NodesOfFxmls.*;
import static org.hamcrest.core.IsNot.not;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

public class MoveLabelsTestFX extends BaseTestFXClass {

    @Test
    @DisplayName("test function of move buttons")
    void testMoveLabels() {
        createBaseFamilyPedigree();

        clickOn(helper.getItemOfTableview(TABLE, "mainPerson"));

        moveToLabel("myBrother");
        verifyThat(MOVE_RIGHT, isVisible());
        verifyThat(MOVE_LEFT, not(isVisible()));

        helper.fireButton(MOVE_RIGHT);

        moveToLabel("myBrother");
        verifyThat(MOVE_RIGHT, isVisible());
        verifyThat(MOVE_LEFT, isVisible());

        helper.fireButton(MOVE_RIGHT);

        moveToLabel("myBrother");
        verifyThat(MOVE_RIGHT, not(isVisible()));
        verifyThat(MOVE_LEFT, isVisible());

        helper.fireButton(MOVE_LEFT);
        moveToLabel("myBrother");

        verifyThat(MOVE_RIGHT, isVisible());
        verifyThat(MOVE_LEFT, isVisible());

        clickOn(helper.getItemOfTableview(TABLE, "myMother"));

        moveToLabel("myMother");
        verifyThat(MOVE_RIGHT, not(isVisible()));
        verifyThat(MOVE_LEFT, not(isVisible()));
    }

    private void moveToLabel(String labelText) {
        moveTo(helper.getLabelFromPaneWithText(SCROLL_PANE, labelText));
    }
}
