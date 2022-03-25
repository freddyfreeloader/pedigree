package de.frederik.integrationTests.guiTestFX;

import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static de.frederik.integrationTests.guiTestFX.utils.NodesOfFxmls.MENU_AGE_CHECK_BOX;
import static de.frederik.integrationTests.guiTestFX.utils.NodesOfFxmls.MENU_VIEW;
import static de.frederik.testUtils.testData.BaseFamily.*;
import static org.junit.jupiter.api.Assertions.*;

public class TranslationNodesYTestFX extends BaseTestFXClass {

    private final static String NO_TRANSLATION = "no node should be translated";
    private final static String TRANSLATION = "mostly one node should be translated";
    private final static String M_OLDEST = "Person with year==null and oldest person should not be translated.";
    private final static String M_YOUNGER = "persons who are younger than oldest person in same generation should be translated";

    @Test
    void translateNodes() {
        createBaseFamilyPedigree();

        clickOn(ME);
        assertFalse(helper.mostlyOneLabelIsTranslated(), NO_TRANSLATION);

        clickOn(MENU_VIEW).clickOn(MENU_AGE_CHECK_BOX);
        assertTrue(helper.mostlyOneLabelIsTranslated(), TRANSLATION);

        Set<Label> labels = helper.getLabelsFromScrollPane();
        labels.forEach(label -> {
            double translation = getTranslation(label);
            switch (getGivenName(label)) {
                case GRANDFATHER, FATHER, BROTHER, CHILD1 -> assertEquals(0.0, translation, M_OLDEST);
                case GRANDMOTHER, MOTHER, ME, SISTER, CHILD2 -> assertTrue(translation > 0.0, M_YOUNGER);
            }
        });

        clickOn(MOTHER);
        assertTrue(helper.mostlyOneLabelIsTranslated(), TRANSLATION);
        // remove translations
        clickOn(MENU_VIEW).clickOn(MENU_AGE_CHECK_BOX);
        assertFalse(helper.mostlyOneLabelIsTranslated(), NO_TRANSLATION);

        clickOn(BROTHER);
        assertFalse(helper.mostlyOneLabelIsTranslated(), NO_TRANSLATION);
    }

    @Test
    void translateNodes_Persons_Year_is_Null() {
        final String PERSON_WITHOUT_YEAR = "personWithoutYear";
        final String YOUNGER_PERSON = "youngerPerson";
        final String OLDER_PERSON = "olderPerson";

        helper.addNewEntry(PERSON_WITHOUT_YEAR);
        helper.addNewEntry(YOUNGER_PERSON, 2000);
        helper.addNewEntry(OLDER_PERSON, 1950);

        helper.fireEditRelativesButton(PERSON_WITHOUT_YEAR, "");

        helper.dragAndDropToTable(YOUNGER_PERSON, SIBLINGS_TABLE);
        helper.dragAndDropToTable(OLDER_PERSON, SIBLINGS_TABLE);
        type(KeyCode.ENTER);

        clickOn(PERSON_WITHOUT_YEAR).clickOn(MENU_VIEW).clickOn(MENU_AGE_CHECK_BOX);

        assertTrue(helper.mostlyOneLabelIsTranslated(), TRANSLATION);

        Set<Label> labels = helper.getLabelsFromScrollPane();
        labels.forEach(label -> {
            double translation = getTranslation(label);
            switch (getGivenName(label)) {
                case PERSON_WITHOUT_YEAR, OLDER_PERSON -> assertEquals(0.0, translation, M_OLDEST);
                case YOUNGER_PERSON -> assertTrue(translation > 0.0, M_YOUNGER);
            }
        });
    }

    private double getTranslation(Label label) {
        return HBox.getMargin(label).getTop();
    }

    private String getGivenName(Label label) {
        return label.getText().substring(0, label.getText().indexOf(' '));
    }

    private static final String SIBLINGS_TABLE = "#siblingsTable";
}
