package de.frederik.integrationTests.guiTestFX;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.matcher.base.NodeMatchers;

import static org.hamcrest.core.IsNot.not;
import static org.testfx.api.FxAssert.verifyThat;
import static de.frederik.integrationTests.guiTestFX.utils.NodesOfFxmls.*;

public class MoveLabelsTestFX extends BaseTestFXClass {

    @Test
    @DisplayName("test funktion of move buttons")
    void testMoveLabels() {
        createBaseFamilyPedigree();

        clickOn(helper.getItemOfTableview(TABLE, "mainPerson"));

        moveTo(helper.getLabelFromPaneWithText(SCROLL_PANE, "myBrother"));
        verifyThat(MOVE_RIGHT, NodeMatchers.isVisible());
        verifyThat(MOVE_LEFT, not(NodeMatchers.isVisible()));

        helper.fireButton(MOVE_RIGHT);
        moveTo(helper.getLabelFromPaneWithText(SCROLL_PANE, "myBrother"));
        verifyThat(MOVE_RIGHT, NodeMatchers.isVisible());
        verifyThat(MOVE_LEFT, NodeMatchers.isVisible());

        helper.fireButton(MOVE_RIGHT);

        moveTo(helper.getLabelFromPaneWithText(SCROLL_PANE, "myBrother"));
        verifyThat(MOVE_RIGHT, not(NodeMatchers.isVisible()));
        verifyThat(MOVE_LEFT, NodeMatchers.isVisible());

        helper.fireButton(MOVE_LEFT);
        moveTo(helper.getLabelFromPaneWithText(SCROLL_PANE, "myBrother"));

        verifyThat(MOVE_RIGHT, NodeMatchers.isVisible());
        verifyThat(MOVE_LEFT, NodeMatchers.isVisible());

        clickOn(helper.getItemOfTableview(TABLE, "myMother"));

        moveTo(helper.getLabelFromPaneWithText(SCROLL_PANE, "myMother"));
        verifyThat(MOVE_RIGHT, not(NodeMatchers.isVisible()));
        verifyThat(MOVE_LEFT, not(NodeMatchers.isVisible()));
    }
}
