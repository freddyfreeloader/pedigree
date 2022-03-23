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

        fireEditRelativesButton(ME, "1990");
        personsTableHaveOnlyThisMembers(myFather, myMother, myChild1, mySpouse, alien, aliensFather, aliensMother);

        // should fail because child is younger than me
        helper.dragAndDropToTable(MY_CHILD1, PARENTS_TABLE);
        personsTableHaveOnlyThisMembers(myFather, myMother, myChild1, mySpouse, alien, aliensFather, aliensMother);
        parentsTableHaveOnlyThisMembers();

        // should fail because father ist older than me
        helper.dragAndDropToTable(MY_FATHER, CHILDREN_TABLE);
        personsTableHaveOnlyThisMembers(myFather, myMother, myChild1, mySpouse, alien, aliensFather, aliensMother);
        parentsTableHaveOnlyThisMembers();

        // should pass
        helper.dragAndDropToTable(MY_SPOUSE, SPOUSES_TABLE);
        personsTableHaveOnlyThisMembers(myFather, myMother, myChild1, alien, aliensFather, aliensMother);
        spousesTableHaveOnlyThisMembers(mySpouse);

        // should fail because only one spouse is allowed
        helper.dragAndDropToTable(ALIEN, SPOUSES_TABLE);
        personsTableHaveOnlyThisMembers(myFather, myMother, myChild1, alien, aliensFather, aliensMother);
        spousesTableHaveOnlyThisMembers(mySpouse);

        // should pass
        helper.dragAndDropToTable(MY_CHILD1, CHILDREN_TABLE);
        helper.dragAndDropToTable(MY_FATHER, PARENTS_TABLE);
        helper.dragAndDropToTable(MY_MOTHER, PARENTS_TABLE);
        personsTableHaveOnlyThisMembers(alien, aliensFather, aliensMother);
        parentsTableHaveOnlyThisMembers(myFather, myMother);
        spousesTableHaveOnlyThisMembers(mySpouse);
        childrenTableHaveOnlyThisMembers(myChild1);

        type(KeyCode.ENTER);

        fireEditRelativesButton(ALIEN, "1966");
        personsTableHaveOnlyThisMembers(me, myFather, myMother, myChild1, mySpouse, aliensFather, aliensMother);

        //should pass
        helper.dragAndDropToTable(ALIENS_FATHER, PARENTS_TABLE);
        helper.dragAndDropToTable(ALIENS_MOTHER, PARENTS_TABLE);
        personsTableHaveOnlyThisMembers(me, myFather, myMother, myChild1, mySpouse);
        parentsTableHaveOnlyThisMembers(aliensFather, aliensMother);
        type(KeyCode.ENTER);

        fireEditRelativesButton(ME, "1990");
        personsTableHaveOnlyThisMembers(alien, aliensFather, aliensMother);
        parentsTableHaveOnlyThisMembers(myFather, myMother);
        spousesTableHaveOnlyThisMembers(mySpouse);
        childrenTableHaveOnlyThisMembers(myChild1);

        // should fail because alien has two other parents
        helper.dragAndDropToTable("1966", SIBLINGS_TABLE);
        personsTableHaveOnlyThisMembers(alien, aliensFather, aliensMother);
        parentsTableHaveOnlyThisMembers(myFather, myMother);
        spousesTableHaveOnlyThisMembers(mySpouse);
        childrenTableHaveOnlyThisMembers(myChild1);
    }


    @Test
    @DisplayName("the children of sibling should not be in persons table")
    void children_of_sibling_should_not_be_in_personsTable() {
        helper.addNewEntry(MY_BROTHER, "", Year.of(1988));
        helper.addNewEntry(ME, "", Year.of(1990));
        helper.addNewEntry(MY_BROTHERS_CHILD, "", Year.of(2000));
        Person myBrother = getPersonByGivenName(MY_BROTHER);
        Person myBrothersChild = getPersonByGivenName(MY_BROTHERS_CHILD);

        fireEditRelativesButton(ME, "1990");

        personsTableHaveOnlyThisMembers(myBrother, myBrothersChild);
        helper.dragAndDropToTable(MY_BROTHER, SIBLINGS_TABLE);

        type(KeyCode.ENTER);
        fireEditRelativesButton(MY_BROTHER, "1988");

        helper.dragAndDropToTable(MY_BROTHERS_CHILD, CHILDREN_TABLE);

        type(KeyCode.ENTER);
        fireEditRelativesButton(ME, "1990");
        personsTableHaveOnlyThisMembers();
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

        fireEditRelativesButton(ME, "1990");

        personsTableHaveOnlyThisMembers(myFather, myMother, myBrother, mySister, myChild1, myChild2, mySpouse);
        parentsTableHaveOnlyThisMembers();
        spousesTableHaveOnlyThisMembers();
        siblingsTableHaveOnlyThisMembers();
        childrenTableHaveOnlyThisMembers();

        helper.dragAndDropToTable(MY_MOTHER, PARENTS_TABLE);

        parentsTableHaveOnlyThisMembers(myMother);
        personsTableHaveOnlyThisMembers(myFather, myBrother, mySister, myChild1, myChild2, mySpouse);

        helper.dragAndDropToTable(MY_FATHER, PARENTS_TABLE);

        parentsTableHaveOnlyThisMembers(myFather, myMother);
        personsTableHaveOnlyThisMembers(myBrother, mySister, myChild1, myChild2, mySpouse);

        fireDeleteButton(MY_FATHER, "parentsGivenNameColumn");

        parentsTableHaveOnlyThisMembers(myMother);
        personsTableHaveOnlyThisMembers(myFather, myBrother, mySister, myChild1, myChild2, mySpouse);

        fireDeleteButton(MY_MOTHER, "parentsGivenNameColumn");

        parentsTableHaveOnlyThisMembers();
        personsTableHaveOnlyThisMembers(myFather, myMother, myBrother, mySister, myChild1, myChild2, mySpouse);

        helper.dragAndDropToTable(MY_SPOUSE, SPOUSES_TABLE);

        spousesTableHaveOnlyThisMembers(mySpouse);
        personsTableHaveOnlyThisMembers(myFather, myMother, myBrother, mySister, myChild1, myChild2);

        fireDeleteButton(MY_SPOUSE, "spousesGivenNameColumn");

        spousesTableHaveOnlyThisMembers();
        personsTableHaveOnlyThisMembers(myFather, myMother, myBrother, mySister, myChild1, myChild2, mySpouse);

        helper.dragAndDropToTable(MY_BROTHER, SIBLINGS_TABLE);

        siblingsTableHaveOnlyThisMembers(myBrother);
        personsTableHaveOnlyThisMembers(myFather, myMother, mySister, myChild1, myChild2, mySpouse);

        helper.dragAndDropToTable(MY_SISTER, SIBLINGS_TABLE);

        siblingsTableHaveOnlyThisMembers(myBrother, mySister);
        personsTableHaveOnlyThisMembers(myFather, myMother, myChild1, myChild2, mySpouse);

        fireDeleteButton(MY_BROTHER, "siblingsGivenNameColumn");

        siblingsTableHaveOnlyThisMembers(mySister);
        personsTableHaveOnlyThisMembers(myFather, myMother, myBrother, myChild1, myChild2, mySpouse);

        fireDeleteButton(MY_SISTER, "siblingsGivenNameColumn");

        siblingsTableHaveOnlyThisMembers();
        personsTableHaveOnlyThisMembers(myFather, myMother, myBrother, mySister, myChild1, myChild2, mySpouse);

        helper.dragAndDropToTable(MY_CHILD1, CHILDREN_TABLE);

        childrenTableHaveOnlyThisMembers(myChild1);
        personsTableHaveOnlyThisMembers(myFather, myMother, myBrother, mySister, myChild2, mySpouse);

        helper.dragAndDropToTable(MY_CHILD2, CHILDREN_TABLE);

        childrenTableHaveOnlyThisMembers(myChild1, myChild2);
        personsTableHaveOnlyThisMembers(myFather, myMother, myBrother, mySister, mySpouse);

        fireDeleteButton(MY_CHILD1, "childrenGivenNameColumn");

        childrenTableHaveOnlyThisMembers(myChild2);
        personsTableHaveOnlyThisMembers(myFather, myMother, myBrother, mySister, myChild1, mySpouse);

        fireDeleteButton(MY_CHILD2, "childrenGivenNameColumn");

        childrenTableHaveOnlyThisMembers();
        personsTableHaveOnlyThisMembers(myFather, myMother, myBrother, mySister, myChild1, myChild2, mySpouse);
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
        fireEditRelativesButton(MY_GRANDFATHER, "1938");

        personsTableHaveOnlyThisMembers(grandmother, father, mother, brother, me, sister, myChild1, myChild2);
        parentsTableHaveOnlyThisMembers();
        spousesTableHaveOnlyThisMembers();
        siblingsTableHaveOnlyThisMembers();
        childrenTableHaveOnlyThisMembers();

        helper.dragAndDropToTable(MY_GRANDMOTHER, SPOUSES_TABLE);
        helper.dragAndDropToTable(MY_FATHER, CHILDREN_TABLE);

        personsTableHaveOnlyThisMembers(mother, brother, me, sister, myChild1, myChild2);
        parentsTableHaveOnlyThisMembers();
        spousesTableHaveOnlyThisMembers(grandmother);
        siblingsTableHaveOnlyThisMembers();
        childrenTableHaveOnlyThisMembers(father);

        type(KeyCode.ENTER);

        // GRANDMOTHER:
        fireEditRelativesButton(MY_GRANDMOTHER, "1940");

        personsTableHaveOnlyThisMembers(father, mother, brother, me, sister, myChild1, myChild2);
        parentsTableHaveOnlyThisMembers();
        spousesTableHaveOnlyThisMembers(grandfather);
        siblingsTableHaveOnlyThisMembers();
        childrenTableHaveOnlyThisMembers();

        helper.dragAndDropToTable(MY_FATHER, CHILDREN_TABLE);

        personsTableHaveOnlyThisMembers(mother, brother, me, sister, myChild1, myChild2);
        parentsTableHaveOnlyThisMembers();
        spousesTableHaveOnlyThisMembers(grandfather);
        siblingsTableHaveOnlyThisMembers();
        childrenTableHaveOnlyThisMembers(father);

        type(KeyCode.ENTER);

        // FATHER:
        fireEditRelativesButton(MY_FATHER, "1968");

        personsTableHaveOnlyThisMembers(mother, brother, me, sister, myChild1, myChild2);
        parentsTableHaveOnlyThisMembers(grandmother, grandfather);
        spousesTableHaveOnlyThisMembers();
        siblingsTableHaveOnlyThisMembers();
        childrenTableHaveOnlyThisMembers();

        helper.dragAndDropToTable(MY_MOTHER, SPOUSES_TABLE);
        helper.dragAndDropToTable(MY_BROTHER, CHILDREN_TABLE);
        helper.dragAndDropToTable(ME, CHILDREN_TABLE);
        helper.dragAndDropToTable(MY_SISTER, CHILDREN_TABLE);

        personsTableHaveOnlyThisMembers(myChild1, myChild2);
        parentsTableHaveOnlyThisMembers(grandmother, grandfather);
        spousesTableHaveOnlyThisMembers(mother);
        siblingsTableHaveOnlyThisMembers();
        childrenTableHaveOnlyThisMembers(brother, me, sister);

        type(KeyCode.ENTER);

        // MOTHER:
        fireEditRelativesButton(MY_MOTHER, "1970");

        personsTableHaveOnlyThisMembers(brother, me, sister, myChild1, myChild2);
        parentsTableHaveOnlyThisMembers();
        spousesTableHaveOnlyThisMembers(father);
        siblingsTableHaveOnlyThisMembers();
        childrenTableHaveOnlyThisMembers();

        helper.dragAndDropToTable(MY_BROTHER, CHILDREN_TABLE);
        helper.dragAndDropToTable(ME, CHILDREN_TABLE);
        helper.dragAndDropToTable(MY_SISTER, CHILDREN_TABLE);

        personsTableHaveOnlyThisMembers(myChild1, myChild2);
        parentsTableHaveOnlyThisMembers();
        spousesTableHaveOnlyThisMembers(father);
        siblingsTableHaveOnlyThisMembers();
        childrenTableHaveOnlyThisMembers(brother, me, sister);

        type(KeyCode.ENTER);

        // ME:
        fireEditRelativesButton(ME, "1990");

        personsTableHaveOnlyThisMembers(myChild1, myChild2);
        parentsTableHaveOnlyThisMembers(father, mother);
        spousesTableHaveOnlyThisMembers();
        siblingsTableHaveOnlyThisMembers(brother, sister);
        childrenTableHaveOnlyThisMembers();

        helper.dragAndDropToTable(MY_CHILD1, CHILDREN_TABLE);
        helper.dragAndDropToTable(MY_CHILD2, CHILDREN_TABLE);

        personsTableHaveOnlyThisMembers();
        parentsTableHaveOnlyThisMembers(father, mother);
        spousesTableHaveOnlyThisMembers();
        siblingsTableHaveOnlyThisMembers(brother, sister);
        childrenTableHaveOnlyThisMembers(myChild1, myChild2);

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
