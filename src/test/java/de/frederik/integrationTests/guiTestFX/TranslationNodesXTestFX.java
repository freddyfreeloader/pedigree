package de.frederik.integrationTests.guiTestFX;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static de.pedigreeProject.kinship.StateOfRelation.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TranslationNodesXTestFX extends BaseTestFXClass {


    @Test
    @DisplayName("test spacing between labels: Buddenbrooks")
    void testTranslation() {
        createBuddenbrookPedigree();

        clickOn("Olly");
        // next is sibling
        Label gotthold = getLabelFromScrollPane("Gotthold");
        assertEquals(NEXT_IS_SIBLING.spacing, HBox.getMargin(gotthold).getRight());
        // next is no direct relative
        Label olly = getLabelFromScrollPane("Olly");
        assertEquals(NEXT_IS_NO_RELATIVE.spacing, HBox.getMargin(olly).getRight());
        // next label is spouse
        Label johann = getLabelFromScrollPane("Johann");
        assertEquals(NEXT_IS_SPOUSE.spacing, HBox.getMargin(johann).getRight());
        // next label is sibling of label before
        Label antoinette = getLabelFromScrollPane("Antoinette");
        assertEquals(BEFORE_IS_SIBLING_OF_NEXT.spacing, HBox.getMargin(antoinette).getRight());
        // is last label
        Label klothilde = getLabelFromScrollPane("Klothilde");
        assertEquals(LAST_INDEX.spacing, HBox.getMargin(klothilde).getRight());
    }

    @Test
    @DisplayName("test spacing between labels: simple list")
    void testTranslation_simpleFamily() {
        final String ME = "mainPerson";
        final String MY_BROTHER = "myBrother";
        final String MY_SISTER = "mySister";
        final String MY_SPOUSE = "mySpouse";
        final String MY_CHILD = "myChild";
        final String SIBLING_OF_SPOUSE = "siblingOfSpouse";

        createTranslationTestPedigree();

        clickOn(MY_CHILD);

        // next label is spouse
        Label meLabel = getLabelFromScrollPane(ME);
        assertEquals(NEXT_IS_SPOUSE.spacing, HBox.getMargin(meLabel).getRight());
        // next label is sibling of label before
        Label mySpouseLabel = getLabelFromScrollPane(MY_SPOUSE);
        assertEquals(BEFORE_IS_SIBLING_OF_NEXT.spacing, HBox.getMargin(mySpouseLabel).getRight());
        // next label is a sibling
        Label mySisterLabel = getLabelFromScrollPane(MY_SISTER);
        assertEquals(NEXT_IS_SIBLING.spacing, HBox.getMargin(mySisterLabel).getRight());
        // next is no direct relative
        Label myBrotherLabel = getLabelFromScrollPane(MY_BROTHER);
        assertEquals(NEXT_IS_NO_RELATIVE.spacing, HBox.getMargin(myBrotherLabel).getRight());
        // is last label
        Label siblingOfSpouseLabel = getLabelFromScrollPane(SIBLING_OF_SPOUSE);
        assertEquals(LAST_INDEX.spacing, HBox.getMargin(siblingOfSpouseLabel).getRight());
    }
}
