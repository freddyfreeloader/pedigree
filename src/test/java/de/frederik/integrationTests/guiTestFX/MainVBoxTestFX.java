package de.frederik.integrationTests.guiTestFX;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static de.frederik.testUtils.testData.BaseFamily.*;

public class MainVBoxTestFX extends BaseTestFXClass {

    @Test
    @DisplayName("test if table content is synchronized with labels of view")
    void labels_should_have_same_names() {

        helper.addNewEntry(GRANDFATHER, GRANDFATHER_BIRTH);
        helper.addNewEntry(GRANDMOTHER, GRANDMOTHER_BIRTH);
        helper.addNewEntry(FATHER, FATHER_BIRTH);
        helper.addNewEntry(MOTHER, MOTHER_BIRTH);
        helper.addNewEntry(BROTHER, BROTHER_BIRTH);
        helper.addNewEntry(ME, ME_BIRTH);
        helper.addNewEntry(SISTER, SISTER_BIRTH);
        helper.addNewEntry(CHILD1, CHILD1_BIRTH);
        helper.addNewEntry(CHILD2, CHILD2_BIRTH);

        sleep(100);
        int i = -1;
        helper.checkLabels(++i, GRANDFATHER, String.valueOf(GRANDFATHER_BIRTH));
        helper.checkLabels(++i, GRANDMOTHER, String.valueOf(GRANDMOTHER_BIRTH));
        helper.checkLabels(++i, FATHER, String.valueOf(FATHER_BIRTH));
        helper.checkLabels(++i, MOTHER, String.valueOf(MOTHER_BIRTH));
        helper.checkLabels(++i, BROTHER, String.valueOf(BROTHER_BIRTH));
        helper.checkLabels(++i, ME, String.valueOf(ME_BIRTH));
        helper.checkLabels(++i, SISTER, String.valueOf(SISTER_BIRTH));
        helper.checkLabels(++i, CHILD1, String.valueOf(CHILD1_BIRTH));
        helper.checkLabels(++i, CHILD2, String.valueOf(CHILD2_BIRTH));
    }
}
