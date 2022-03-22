package de.frederik.integrationTests.guiTestFX;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

import static de.frederik.integrationTests.guiTestFX.utils.NodesOfFxmls.*;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.jupiter.api.Assertions.*;


public class TranslationNodesYTestFX extends BaseTestFXClass {

    @Test
    void translateNodes() {
        createBaseFamilyPedigree();

        clickOn("mainPerson");
        assertFalse(mostlyOneLabelIsTranslated(), "no node should be translated");

        clickOn(MENU_VIEW).clickOn(MENU_AGE_CHECK_BOX);
        assertTrue(mostlyOneLabelIsTranslated(), "mostly one node should be translated");

        Set<Label> labels = getLabelsFromScrollPane();
        labels.forEach(label -> {
            double translation = HBox.getMargin(label).getTop();
            String firstWordOfLabel = label.getText().substring(0, label.getText().indexOf(' '));
            switch (firstWordOfLabel) {
                case "myGrandfather", "myFather", "myBrother", "myChild1" -> assertEquals(0.0, translation, "oldest persons in each generation should not be translated");
                case "myGrandmother", "myMother", "mainPerson", "mySister", "myChild2" -> assertTrue(translation > 0.0, "persons who are younger than oldest person in generation should be translated");
            }
        });

        clickOn("myMother");
        assertTrue(mostlyOneLabelIsTranslated(), "mostly one node should be translated");
        // remove translations
        clickOn(MENU_VIEW).clickOn(MENU_AGE_CHECK_BOX);
        assertFalse(mostlyOneLabelIsTranslated(), "no node should be translated");

        clickOn("myBrother");
        assertFalse(mostlyOneLabelIsTranslated(), "no node should be translated");
    }

    @Test
    void translateNodes_Persons_Year_is_Null() {
        final String PERSON_WITHOUT_YEAR = "personWithoutYear";
        final String YOUNGER_PERSON = "youngerPerson";
        final String OLDER_PERSON = "olderPerson";

        helper.addNewEntry(PERSON_WITHOUT_YEAR, "", null);
        helper.addNewEntry(YOUNGER_PERSON, "", Year.of(2000));
        helper.addNewEntry(OLDER_PERSON, "", Year.of(1950));

        Button addRelativesButtonOfMe = lookup(helper.isButtonInTableRow(ADD_RELATIVES_CSS, PERSON_WITHOUT_YEAR, "")).queryButton();
        Platform.runLater(addRelativesButtonOfMe::fire);
        interrupt();

        helper.dragAndDropToTable(YOUNGER_PERSON, SIBLINGS_TABLE);
        helper.dragAndDropToTable(OLDER_PERSON, SIBLINGS_TABLE);
        type(KeyCode.ENTER);

        clickOn(PERSON_WITHOUT_YEAR).clickOn(MENU_VIEW).clickOn(MENU_AGE_CHECK_BOX);

        assertTrue(mostlyOneLabelIsTranslated(), "mostly one node should be translated");
        Set<Label> labels = getLabelsFromScrollPane();
        labels.forEach(label -> {
            double translation = HBox.getMargin(label).getTop();
            String firstWordOfLabel = label.getText().substring(0, label.getText().indexOf(' '));
            switch (firstWordOfLabel) {
                case PERSON_WITHOUT_YEAR, OLDER_PERSON -> assertEquals(0.0, translation, "Person with year==null and oldest person should not be translated");
                case YOUNGER_PERSON -> assertTrue(translation > 0.0, "persons who are younger should be translated");
            }
        });
    }

    private static final String SIBLINGS_TABLE = "#siblingsTable";
}
