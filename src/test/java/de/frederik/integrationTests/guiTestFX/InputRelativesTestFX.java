package de.frederik.integrationTests.guiTestFX;

import de.pedigreeProject.model.Person;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.List;

import static de.frederik.integrationTests.guiTestFX.utils.NodesOfFxmls.*;
import static org.junit.jupiter.api.Assertions.*;

public class InputRelativesTestFX extends BaseTestFXClass {

    private static final String MY_GRANDFATHER = "myGrandfather";
    private static final String MY_GRANDMOTHER = "myGrandmother";
    private static final String MY_FATHER = "myFather";
    private static final String MY_MOTHER = "myMother";
    private static final String ME = "me";
    private static final String MY_CHILD1 = "myChild1";
    private static final String MY_CHILD2 = "myChild2";
    private static final String MY_SPOUSE = "mySpouse";
    private static final String MY_BROTHER = "myBrother";
    private static final String MY_BROTHERS_CHILD = "myBrothersChild";
    private static final String MY_SISTER = "mySister";

    private static final String ALIEN = "alien";
    private static final String ALIENS_FATHER = "aliensFather";
    private static final String ALIENS_MOTHER = "aliensMother";

    // Can't assert that error-Code pane appear because of a bug in TestFX https://github.com/TestFX/TestFX/issues/687
    // So only verify that person is still in personTable and not dropped to another table
    @Test
    @DisplayName("drop should not be possible")
    void errorLabels() {

        helper.addNewEntry(MY_FATHER, "", Year.of(1968));
        helper.addNewEntry(MY_MOTHER, "", Year.of(1970));
        helper.addNewEntry(ME, "", Year.of(1990));
        helper.addNewEntry(MY_CHILD1, "", Year.of(2010));
        helper.addNewEntry(MY_SPOUSE, "", Year.of(1991));
        helper.addNewEntry(ALIEN, "", Year.of(1966));
        helper.addNewEntry(ALIENS_FATHER, "", Year.of(1933));
        helper.addNewEntry(ALIENS_MOTHER, "", Year.of(1932));

        Person myFather = getPersonByGivenName(MY_FATHER);
        Person myMother = getPersonByGivenName(MY_MOTHER);
        Person me = getPersonByGivenName(ME);
        Person myChild1 = getPersonByGivenName(MY_CHILD1);
        Person mySpouse = getPersonByGivenName(MY_SPOUSE);
        Person alien = getPersonByGivenName(ALIEN);
        Person aliensFather = getPersonByGivenName(ALIENS_FATHER);
        Person aliensMother = getPersonByGivenName(ALIENS_MOTHER);

        assertNotNull(myFather);
        assertNotNull(myMother);
        assertNotNull(me);
        assertNotNull(myChild1);
        assertNotNull(mySpouse);
        assertNotNull(alien);
        assertNotNull(aliensFather);
        assertNotNull(aliensMother);

        helper.fireEditRelativesButton(ME, "1990");
        helper.personsTableHasOnlyThisMembers(myFather, myMother, myChild1, mySpouse, alien, aliensFather, aliensMother);

        // should fail because child is younger than me
        helper.dragAndDropToTable(MY_CHILD1, PARENTS_TABLE);
        helper.personsTableHasOnlyThisMembers(myFather, myMother, myChild1, mySpouse, alien, aliensFather, aliensMother);
        helper.parentsTableHasOnlyThisMembers();

        // should fail because father ist older than me
        helper.dragAndDropToTable(MY_FATHER, CHILDREN_TABLE);
        helper.personsTableHasOnlyThisMembers(myFather, myMother, myChild1, mySpouse, alien, aliensFather, aliensMother);
        helper.parentsTableHasOnlyThisMembers();

        // should pass
        helper.dragAndDropToTable(MY_SPOUSE, SPOUSES_TABLE);
        helper.personsTableHasOnlyThisMembers(myFather, myMother, myChild1, alien, aliensFather, aliensMother);
        helper.spousesTableHasOnlyThisMembers(mySpouse);

        // should fail because only one spouse is allowed
        helper.dragAndDropToTable(ALIEN, SPOUSES_TABLE);
        helper.personsTableHasOnlyThisMembers(myFather, myMother, myChild1, alien, aliensFather, aliensMother);
        helper.spousesTableHasOnlyThisMembers(mySpouse);

        // should pass
        helper.dragAndDropToTable(MY_CHILD1, CHILDREN_TABLE);
        helper.dragAndDropToTable(MY_FATHER, PARENTS_TABLE);
        helper.dragAndDropToTable(MY_MOTHER, PARENTS_TABLE);
        helper.personsTableHasOnlyThisMembers(alien, aliensFather, aliensMother);
        helper.parentsTableHasOnlyThisMembers(myFather, myMother);
        helper.spousesTableHasOnlyThisMembers(mySpouse);
        helper.childrenTableHasOnlyThisMembers(myChild1);

        type(KeyCode.ENTER);

        helper.fireEditRelativesButton(ALIEN, "1966");
        helper.personsTableHasOnlyThisMembers(me, myFather, myMother, myChild1, mySpouse, aliensFather, aliensMother);

        //should pass
        helper.dragAndDropToTable(ALIENS_FATHER, PARENTS_TABLE);
        helper.dragAndDropToTable(ALIENS_MOTHER, PARENTS_TABLE);
        helper.personsTableHasOnlyThisMembers(me, myFather, myMother, myChild1, mySpouse);
        helper.parentsTableHasOnlyThisMembers(aliensFather, aliensMother);
        type(KeyCode.ENTER);

        helper.fireEditRelativesButton(ME, "1990");
        helper.personsTableHasOnlyThisMembers(alien, aliensFather, aliensMother);
        helper.parentsTableHasOnlyThisMembers(myFather, myMother);
        helper.spousesTableHasOnlyThisMembers(mySpouse);
        helper.childrenTableHasOnlyThisMembers(myChild1);

        // should fail because alien has two other parents
        helper.dragAndDropToTable("1966", SIBLINGS_TABLE);
        helper.personsTableHasOnlyThisMembers(alien, aliensFather, aliensMother);
        helper.parentsTableHasOnlyThisMembers(myFather, myMother);
        helper.spousesTableHasOnlyThisMembers(mySpouse);
        helper.childrenTableHasOnlyThisMembers(myChild1);
    }


    @Test
    @DisplayName("the children of sibling should not be in persons table")
    void children_of_sibling_should_not_be_in_personsTable() {
        helper.addNewEntry(MY_BROTHER, "", Year.of(1988));
        helper.addNewEntry(ME, "", Year.of(1990));
        helper.addNewEntry(MY_BROTHERS_CHILD, "", Year.of(2000));
        Person myBrother = getPersonByGivenName(MY_BROTHER);
        Person myBrothersChild = getPersonByGivenName(MY_BROTHERS_CHILD);

        helper.fireEditRelativesButton(ME, "1990");

        helper.personsTableHasOnlyThisMembers(myBrother, myBrothersChild);
        helper.dragAndDropToTable(MY_BROTHER, SIBLINGS_TABLE);

        type(KeyCode.ENTER);
        helper.fireEditRelativesButton(MY_BROTHER, "1988");

        helper.dragAndDropToTable(MY_BROTHERS_CHILD, CHILDREN_TABLE);

        type(KeyCode.ENTER);
        helper.fireEditRelativesButton(ME, "1990");
        helper.personsTableHasOnlyThisMembers();
    }

    @Test
    @DisplayName("basic test for drag&drop and delete buttons")
    void test_Table_Functions_DragAndDrop_DeleteFromTable() {

        helper.addNewEntry(MY_FATHER, "", Year.of(1968));
        helper.addNewEntry(MY_MOTHER, "", Year.of(1970));
        helper.addNewEntry(MY_BROTHER, "", Year.of(1988));
        helper.addNewEntry(ME, "", Year.of(1990));
        helper.addNewEntry(MY_SISTER, "", Year.of(1992));
        helper.addNewEntry(MY_CHILD1, "", Year.of(2010));
        helper.addNewEntry(MY_CHILD2, "", Year.of(2012));
        helper.addNewEntry(MY_SPOUSE, "", Year.of(1991));

        Person myFather = getPersonByGivenName(MY_FATHER);
        Person myMother = getPersonByGivenName(MY_MOTHER);
        Person myBrother = getPersonByGivenName(MY_BROTHER);
        Person me = getPersonByGivenName(ME);
        Person mySister = getPersonByGivenName(MY_SISTER);
        Person myChild1 = getPersonByGivenName(MY_CHILD1);
        Person myChild2 = getPersonByGivenName(MY_CHILD2);
        Person mySpouse = getPersonByGivenName(MY_SPOUSE);

        assertNotNull(myFather);
        assertNotNull(myMother);
        assertNotNull(myBrother);
        assertNotNull(me);
        assertNotNull(mySister);
        assertNotNull(myChild1);
        assertNotNull(myChild2);
        assertNotNull(mySpouse);

        helper.fireEditRelativesButton(ME, "1990");

        helper.personsTableHasOnlyThisMembers(myFather, myMother, myBrother, mySister, myChild1, myChild2, mySpouse);
        helper.parentsTableHasOnlyThisMembers();
        helper.spousesTableHasOnlyThisMembers();
        helper.siblingsTableHasOnlyThisMembers();
        helper.childrenTableHasOnlyThisMembers();

        helper.dragAndDropToTable(MY_MOTHER, PARENTS_TABLE);

        helper.parentsTableHasOnlyThisMembers(myMother);
        helper.personsTableHasOnlyThisMembers(myFather, myBrother, mySister, myChild1, myChild2, mySpouse);

        helper.dragAndDropToTable(MY_FATHER, PARENTS_TABLE);

        helper.parentsTableHasOnlyThisMembers(myFather, myMother);
        helper.personsTableHasOnlyThisMembers(myBrother, mySister, myChild1, myChild2, mySpouse);

        helper.fireDeleteButton(MY_FATHER, "parentsGivenNameColumn");

        helper.parentsTableHasOnlyThisMembers(myMother);
        helper.personsTableHasOnlyThisMembers(myFather, myBrother, mySister, myChild1, myChild2, mySpouse);

        helper.fireDeleteButton(MY_MOTHER, "parentsGivenNameColumn");

        helper.parentsTableHasOnlyThisMembers();
        helper.personsTableHasOnlyThisMembers(myFather, myMother, myBrother, mySister, myChild1, myChild2, mySpouse);

        helper.dragAndDropToTable(MY_SPOUSE, SPOUSES_TABLE);

        helper.spousesTableHasOnlyThisMembers(mySpouse);
        helper.personsTableHasOnlyThisMembers(myFather, myMother, myBrother, mySister, myChild1, myChild2);

        helper.fireDeleteButton(MY_SPOUSE, "spousesGivenNameColumn");

        helper.spousesTableHasOnlyThisMembers();
        helper.personsTableHasOnlyThisMembers(myFather, myMother, myBrother, mySister, myChild1, myChild2, mySpouse);

        helper.dragAndDropToTable(MY_BROTHER, SIBLINGS_TABLE);

        helper.siblingsTableHasOnlyThisMembers(myBrother);
        helper.personsTableHasOnlyThisMembers(myFather, myMother, mySister, myChild1, myChild2, mySpouse);

        helper.dragAndDropToTable(MY_SISTER, SIBLINGS_TABLE);

        helper.siblingsTableHasOnlyThisMembers(myBrother, mySister);
        helper.personsTableHasOnlyThisMembers(myFather, myMother, myChild1, myChild2, mySpouse);

        helper.fireDeleteButton(MY_BROTHER, "siblingsGivenNameColumn");

        helper.siblingsTableHasOnlyThisMembers(mySister);
        helper.personsTableHasOnlyThisMembers(myFather, myMother, myBrother, myChild1, myChild2, mySpouse);

        helper.fireDeleteButton(MY_SISTER, "siblingsGivenNameColumn");

        helper.siblingsTableHasOnlyThisMembers();
        helper.personsTableHasOnlyThisMembers(myFather, myMother, myBrother, mySister, myChild1, myChild2, mySpouse);

        helper.dragAndDropToTable(MY_CHILD1, CHILDREN_TABLE);

        helper.childrenTableHasOnlyThisMembers(myChild1);
        helper.personsTableHasOnlyThisMembers(myFather, myMother, myBrother, mySister, myChild2, mySpouse);

        helper.dragAndDropToTable(MY_CHILD2, CHILDREN_TABLE);

        helper.childrenTableHasOnlyThisMembers(myChild1, myChild2);
        helper.personsTableHasOnlyThisMembers(myFather, myMother, myBrother, mySister, mySpouse);

        helper.fireDeleteButton(MY_CHILD1, "childrenGivenNameColumn");

        helper.childrenTableHasOnlyThisMembers(myChild2);
        helper.personsTableHasOnlyThisMembers(myFather, myMother, myBrother, mySister, myChild1, mySpouse);

        helper.fireDeleteButton(MY_CHILD2, "childrenGivenNameColumn");

        helper.childrenTableHasOnlyThisMembers();
        helper.personsTableHasOnlyThisMembers(myFather, myMother, myBrother, mySister, myChild1, myChild2, mySpouse);
    }

    @Test
    @DisplayName("test if tables and model are synchronized correctly")
    void addRelatives() {

        helper.addNewEntry(MY_GRANDFATHER, "", Year.of(1938));
        helper.addNewEntry(MY_GRANDMOTHER, "", Year.of(1940));
        helper.addNewEntry(MY_FATHER, "", Year.of(1968));
        helper.addNewEntry(MY_MOTHER, "", Year.of(1970));
        helper.addNewEntry(MY_BROTHER, "", Year.of(1988));
        helper.addNewEntry(ME, "", Year.of(1990));
        helper.addNewEntry(MY_SISTER, "", Year.of(1992));
        helper.addNewEntry(MY_CHILD1, "", Year.of(2010));
        helper.addNewEntry(MY_CHILD2, "", Year.of(2012));

        Person grandfather = getPersonByGivenName(MY_GRANDFATHER);
        Person grandmother = getPersonByGivenName(MY_GRANDMOTHER);
        Person father = getPersonByGivenName(MY_FATHER);
        Person mother = getPersonByGivenName(MY_MOTHER);
        Person brother = getPersonByGivenName(MY_BROTHER);
        Person me = getPersonByGivenName(ME);
        Person sister = getPersonByGivenName(MY_SISTER);
        Person myChild1 = getPersonByGivenName(MY_CHILD1);
        Person myChild2 = getPersonByGivenName(MY_CHILD2);

        assertNotNull(grandfather);
        assertNotNull(grandmother);
        assertNotNull(father);
        assertNotNull(mother);
        assertNotNull(brother);
        assertNotNull(me);
        assertNotNull(sister);
        assertNotNull(myChild1);
        assertNotNull(myChild2);

        // GRANDFATHER:
        helper.fireEditRelativesButton(MY_GRANDFATHER, "1938");

        helper.personsTableHasOnlyThisMembers(grandmother, father, mother, brother, me, sister, myChild1, myChild2);
        helper.parentsTableHasOnlyThisMembers();
        helper.spousesTableHasOnlyThisMembers();
        helper.siblingsTableHasOnlyThisMembers();
        helper.childrenTableHasOnlyThisMembers();

        helper.dragAndDropToTable(MY_GRANDMOTHER, SPOUSES_TABLE);
        helper.dragAndDropToTable(MY_FATHER, CHILDREN_TABLE);

        helper.personsTableHasOnlyThisMembers(mother, brother, me, sister, myChild1, myChild2);
        helper.parentsTableHasOnlyThisMembers();
        helper.spousesTableHasOnlyThisMembers(grandmother);
        helper.siblingsTableHasOnlyThisMembers();
        helper.childrenTableHasOnlyThisMembers(father);

        type(KeyCode.ENTER);

        // GRANDMOTHER:
        helper.fireEditRelativesButton(MY_GRANDMOTHER, "1940");

        helper.personsTableHasOnlyThisMembers(father, mother, brother, me, sister, myChild1, myChild2);
        helper.parentsTableHasOnlyThisMembers();
        helper.spousesTableHasOnlyThisMembers(grandfather);
        helper.siblingsTableHasOnlyThisMembers();
        helper.childrenTableHasOnlyThisMembers();

        helper.dragAndDropToTable(MY_FATHER, CHILDREN_TABLE);

        helper.personsTableHasOnlyThisMembers(mother, brother, me, sister, myChild1, myChild2);
        helper.parentsTableHasOnlyThisMembers();
        helper.spousesTableHasOnlyThisMembers(grandfather);
        helper.siblingsTableHasOnlyThisMembers();
        helper.childrenTableHasOnlyThisMembers(father);

        type(KeyCode.ENTER);

        // FATHER:
        helper.fireEditRelativesButton(MY_FATHER, "1968");

        helper.personsTableHasOnlyThisMembers(mother, brother, me, sister, myChild1, myChild2);
        helper.parentsTableHasOnlyThisMembers(grandmother, grandfather);
        helper.spousesTableHasOnlyThisMembers();
        helper.siblingsTableHasOnlyThisMembers();
        helper.childrenTableHasOnlyThisMembers();

        helper.dragAndDropToTable(MY_MOTHER, SPOUSES_TABLE);
        helper.dragAndDropToTable(MY_BROTHER, CHILDREN_TABLE);
        helper.dragAndDropToTable(ME, CHILDREN_TABLE);
        helper.dragAndDropToTable(MY_SISTER, CHILDREN_TABLE);

        helper.personsTableHasOnlyThisMembers(myChild1, myChild2);
        helper.parentsTableHasOnlyThisMembers(grandmother, grandfather);
        helper.spousesTableHasOnlyThisMembers(mother);
        helper.siblingsTableHasOnlyThisMembers();
        helper.childrenTableHasOnlyThisMembers(brother, me, sister);

        type(KeyCode.ENTER);

        // MOTHER:
        helper.fireEditRelativesButton(MY_MOTHER, "1970");

        helper.personsTableHasOnlyThisMembers(brother, me, sister, myChild1, myChild2);
        helper.parentsTableHasOnlyThisMembers();
        helper.spousesTableHasOnlyThisMembers(father);
        helper.siblingsTableHasOnlyThisMembers();
        helper.childrenTableHasOnlyThisMembers();

        helper.dragAndDropToTable(MY_BROTHER, CHILDREN_TABLE);
        helper.dragAndDropToTable(ME, CHILDREN_TABLE);
        helper.dragAndDropToTable(MY_SISTER, CHILDREN_TABLE);

        helper.personsTableHasOnlyThisMembers(myChild1, myChild2);
        helper.parentsTableHasOnlyThisMembers();
        helper.spousesTableHasOnlyThisMembers(father);
        helper.siblingsTableHasOnlyThisMembers();
        helper.childrenTableHasOnlyThisMembers(brother, me, sister);

        type(KeyCode.ENTER);

        // ME:
        helper.fireEditRelativesButton(ME, "1990");

        helper.personsTableHasOnlyThisMembers(myChild1, myChild2);
        helper.parentsTableHasOnlyThisMembers(father, mother);
        helper.spousesTableHasOnlyThisMembers();
        helper.siblingsTableHasOnlyThisMembers(brother, sister);
        helper.childrenTableHasOnlyThisMembers();

        helper.dragAndDropToTable(MY_CHILD1, CHILDREN_TABLE);
        helper.dragAndDropToTable(MY_CHILD2, CHILDREN_TABLE);

        helper.personsTableHasOnlyThisMembers();
        helper.parentsTableHasOnlyThisMembers(father, mother);
        helper.spousesTableHasOnlyThisMembers();
        helper.siblingsTableHasOnlyThisMembers(brother, sister);
        helper.childrenTableHasOnlyThisMembers(myChild1, myChild2);

        type(KeyCode.ENTER);

        // assert sync with model:
        verifyRelativesLists(grandfather, 0, 1, 0, 1);
        assertTrue(grandfather.getSpouses().contains(grandmother));
        assertTrue(grandfather.getChildren().contains(father));

        verifyRelativesLists(grandmother, 0, 1, 0, 1);
        assertTrue(grandmother.getSpouses().contains(grandfather));
        assertTrue(grandfather.getChildren().contains(father));

        verifyRelativesLists(father, 2, 1, 0, 3);
        assertTrue(father.getParents().containsAll(List.of(grandfather, grandmother)));
        assertTrue(father.getSpouses().contains(mother));
        assertTrue(father.getChildren().containsAll(List.of(brother, me, sister)));

        verifyRelativesLists(mother, 0, 1, 0, 3);
        assertTrue(mother.getSpouses().contains(father));
        assertTrue(mother.getChildren().containsAll(List.of(brother, me, sister)));

        verifyRelativesLists(brother, 2, 0, 2, 0);
        assertTrue(brother.getParents().containsAll(List.of(father, mother)));
        assertTrue(brother.getSiblings().containsAll(List.of(me, sister)));

        verifyRelativesLists(sister, 2, 0, 2, 0);
        assertTrue(sister.getParents().containsAll(List.of(father, mother)));
        assertTrue(sister.getSiblings().containsAll(List.of(me, brother)));

        verifyRelativesLists(me, 2, 0, 2, 2);
        assertTrue(me.getParents().containsAll(List.of(father, mother)));
        assertTrue(me.getSiblings().containsAll(List.of(sister, brother)));
        assertTrue(me.getChildren().containsAll(List.of(myChild1, myChild2)));

        verifyRelativesLists(myChild1, 1, 0, 1, 0);
        assertTrue(myChild1.getParents().contains(me));
        assertTrue(myChild1.getSiblings().contains(myChild2));

        verifyRelativesLists(myChild2, 1, 0, 1, 0);
        assertTrue(myChild2.getParents().contains(me));
        assertTrue(myChild2.getSiblings().contains(myChild1));
    }

    private void verifyRelativesLists(Person person, int parents, int spouses, int siblings, int children) {
        assertEquals(parents, person.getParents().size());
        assertEquals(spouses, person.getSpouses().size());
        assertEquals(siblings, person.getSiblings().size());
        assertEquals(children, person.getChildren().size());
    }
}
