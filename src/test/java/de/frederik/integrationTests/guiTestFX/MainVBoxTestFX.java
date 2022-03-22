package de.frederik.integrationTests.guiTestFX;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Year;

public class MainVBoxTestFX extends BaseTestFXClass {

    @Test
    @DisplayName("test id table content is synchronized with labels of view")
    void labels_should_have_same_names() {

        helper.addNewEntry("grandfather", "", Year.of(1938));
        helper.addNewEntry("grandmother", "", Year.of(1940));
        helper.addNewEntry("father", "", Year.of(1968));
        helper.addNewEntry("mother", "", Year.of(1970));
        helper.addNewEntry("brother", "", Year.of(1988));
        helper.addNewEntry("me", "", Year.of(1990));
        helper.addNewEntry("sister", "", Year.of(1992));
        helper.addNewEntry("myChild1", "", Year.of(2010));
        helper.addNewEntry("myChild2", "", Year.of(2012));

        sleep(100);
        int i = -1;
        helper.checkLabels(++i, "grandfather \n*1938");
        helper.checkLabels(++i, "grandmother \n*1940");
        helper.checkLabels(++i, "father \n*1968");
        helper.checkLabels(++i, "mother \n*1970");
        helper.checkLabels(++i, "brother \n*1988");
        helper.checkLabels(++i, "me \n*1990");
        helper.checkLabels(++i, "sister \n*1992");
        helper.checkLabels(++i, "myChild1 \n*2010");
        helper.checkLabels(++i, "myChild2 \n*2012");
    }
}
