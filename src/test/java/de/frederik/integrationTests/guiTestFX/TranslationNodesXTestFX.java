package de.frederik.integrationTests.guiTestFX;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static de.pedigreeProject.kinship.StateOfRelation.*;
import static de.frederik.testUtils.testData.TranslationY.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TranslationNodesXTestFX extends BaseTestFXClass {

    @Test
    @DisplayName("test spacing between labels: Buddenbrooks")
    void testTranslation() {
        createBuddenbrookPedigree();
        clickOn("Olly");

        assertEquals(NEXT_IS_SIBLING.spacing, rightMarginOf("Gotthold"));
        assertEquals(NEXT_IS_NO_RELATIVE.spacing, rightMarginOf("Olly"));
        assertEquals(NEXT_IS_SPOUSE.spacing, rightMarginOf("Johann"));
        assertEquals(BEFORE_IS_SIBLING_OF_NEXT.spacing, rightMarginOf("Antoinette"));
        assertEquals(LAST_INDEX.spacing, rightMarginOf("Klothilde"));
    }

    @Test
    @DisplayName("test spacing between labels: simple list")
    void testTranslation_simpleFamily() {

        createTranslationTestPedigree();
        clickOn(MY_CHILD);

        assertEquals(NEXT_IS_SPOUSE.spacing, rightMarginOf(ME));
        assertEquals(BEFORE_IS_SIBLING_OF_NEXT.spacing, rightMarginOf(MY_SPOUSE));
        assertEquals(NEXT_IS_SIBLING.spacing, rightMarginOf(MY_SISTER));
        assertEquals(NEXT_IS_NO_RELATIVE.spacing, rightMarginOf(MY_BROTHER));
        assertEquals(LAST_INDEX.spacing, rightMarginOf(SIBLING_OF_SPOUSE));
    }

    private double rightMarginOf(String GivenName) {
        Label gotthold = helper.getLabelFromScrollPane(GivenName);
        return HBox.getMargin(gotthold).getRight();
    }
}
