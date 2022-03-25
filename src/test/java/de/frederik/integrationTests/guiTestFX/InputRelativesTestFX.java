package de.frederik.integrationTests.guiTestFX;

import de.pedigreeProject.model.Person;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static de.frederik.integrationTests.guiTestFX.utils.NodesOfFxmls.*;
import static de.frederik.testUtils.testData.BaseFamily.*;
import static org.junit.jupiter.api.Assertions.*;

public class InputRelativesTestFX extends BaseTestFXClass {

    // Can't assert that error-Code pane appear because of a bug in TestFX https://github.com/TestFX/TestFX/issues/687
    // So only verify that person is still in personTable and not dropped to another table
    @Test
    @DisplayName("drop should not be possible")
    void errorLabels() {

        helper.addNewEntry(FATHER, FATHER_BIRTH);
        helper.addNewEntry(MOTHER, MOTHER_BIRTH);
        helper.addNewEntry(ME, ME_BIRTH);
        helper.addNewEntry(CHILD1, CHILD1_BIRTH);
        helper.addNewEntry(SPOUSE, SPOUSE_BIRTH);
        helper.addNewEntry(ALIEN, ALIEN_BIRTH);
        helper.addNewEntry(ALIENS_FATHER, ALIENS_FATHER_BIRTH);
        helper.addNewEntry(ALIENS_MOTHER, ALIENS_MOTHER_BIRTH);

        Person myFather = getPersonByGivenName(FATHER);
        Person myMother = getPersonByGivenName(MOTHER);
        Person me = getPersonByGivenName(ME);
        Person myChild1 = getPersonByGivenName(CHILD1);
        Person mySpouse = getPersonByGivenName(SPOUSE);
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

        helper.fireEditRelativesButton(ME, String.valueOf(ME_BIRTH));
        helper.personsTableHasOnlyThisMembers(myFather, myMother, myChild1, mySpouse, alien, aliensFather, aliensMother);

        // should fail because child is younger than me
        helper.dragAndDropToTable(CHILD1, PARENTS_TABLE);
        helper.personsTableHasOnlyThisMembers(myFather, myMother, myChild1, mySpouse, alien, aliensFather, aliensMother);
        helper.parentsTableHasOnlyThisMembers();

        // should fail because father ist older than me
        helper.dragAndDropToTable(FATHER, CHILDREN_TABLE);
        helper.personsTableHasOnlyThisMembers(myFather, myMother, myChild1, mySpouse, alien, aliensFather, aliensMother);
        helper.parentsTableHasOnlyThisMembers();

        // should pass
        helper.dragAndDropToTable(SPOUSE, SPOUSES_TABLE);
        helper.personsTableHasOnlyThisMembers(myFather, myMother, myChild1, alien, aliensFather, aliensMother);
        helper.spousesTableHasOnlyThisMembers(mySpouse);

        // should fail because only one spouse is allowed
        helper.dragAndDropToTable(ALIEN, SPOUSES_TABLE);
        helper.personsTableHasOnlyThisMembers(myFather, myMother, myChild1, alien, aliensFather, aliensMother);
        helper.spousesTableHasOnlyThisMembers(mySpouse);

        // should pass
        helper.dragAndDropToTable(CHILD1, CHILDREN_TABLE);
        helper.dragAndDropToTable(FATHER, PARENTS_TABLE);
        helper.dragAndDropToTable(MOTHER, PARENTS_TABLE);
        helper.personsTableHasOnlyThisMembers(alien, aliensFather, aliensMother);
        helper.parentsTableHasOnlyThisMembers(myFather, myMother);
        helper.spousesTableHasOnlyThisMembers(mySpouse);
        helper.childrenTableHasOnlyThisMembers(myChild1);

        type(KeyCode.ENTER);

        helper.fireEditRelativesButton(ALIEN, String.valueOf(ALIEN_BIRTH));
        helper.personsTableHasOnlyThisMembers(me, myFather, myMother, myChild1, mySpouse, aliensFather, aliensMother);

        //should pass
        helper.dragAndDropToTable(ALIENS_FATHER, PARENTS_TABLE);
        helper.dragAndDropToTable(ALIENS_MOTHER, PARENTS_TABLE);
        helper.personsTableHasOnlyThisMembers(me, myFather, myMother, myChild1, mySpouse);
        helper.parentsTableHasOnlyThisMembers(aliensFather, aliensMother);
        type(KeyCode.ENTER);

        helper.fireEditRelativesButton(ME, String.valueOf(ME_BIRTH));
        helper.personsTableHasOnlyThisMembers(alien, aliensFather, aliensMother);
        helper.parentsTableHasOnlyThisMembers(myFather, myMother);
        helper.spousesTableHasOnlyThisMembers(mySpouse);
        helper.childrenTableHasOnlyThisMembers(myChild1);

        // should fail because alien has two other parents
        helper.dragAndDropToTable(ALIEN, SIBLINGS_TABLE);
        helper.personsTableHasOnlyThisMembers(alien, aliensFather, aliensMother);
        helper.parentsTableHasOnlyThisMembers(myFather, myMother);
        helper.spousesTableHasOnlyThisMembers(mySpouse);
        helper.childrenTableHasOnlyThisMembers(myChild1);
    }


    @Test
    @DisplayName("the children of sibling should not be in persons table")
    void children_of_sibling_should_not_be_in_personsTable() {
        helper.addNewEntry(BROTHER, BROTHER_BIRTH);
        helper.addNewEntry(ME, ME_BIRTH);
        helper.addNewEntry(BROTHERS_CHILD, BROTHERS_CHILD_BIRTH);
        Person myBrother = getPersonByGivenName(BROTHER);
        Person myBrothersChild = getPersonByGivenName(BROTHERS_CHILD);

        helper.fireEditRelativesButton(ME, String.valueOf(ME_BIRTH));

        helper.personsTableHasOnlyThisMembers(myBrother, myBrothersChild);
        helper.dragAndDropToTable(BROTHER, SIBLINGS_TABLE);

        type(KeyCode.ENTER);
        helper.fireEditRelativesButton(BROTHER, String.valueOf(BROTHER_BIRTH));

        helper.dragAndDropToTable(BROTHERS_CHILD, CHILDREN_TABLE);

        type(KeyCode.ENTER);
        helper.fireEditRelativesButton(ME, String.valueOf(ME_BIRTH));
        helper.personsTableHasOnlyThisMembers();
    }

    @Test
    @DisplayName("basic test for drag&drop and delete buttons")
    void test_Table_Functions_DragAndDrop_DeleteFromTable() {

        helper.addNewEntry(FATHER, FATHER_BIRTH);
        helper.addNewEntry(MOTHER, MOTHER_BIRTH);
        helper.addNewEntry(BROTHER, BROTHER_BIRTH);
        helper.addNewEntry(ME, ME_BIRTH);
        helper.addNewEntry(SISTER, SISTER_BIRTH);
        helper.addNewEntry(CHILD1, CHILD1_BIRTH);
        helper.addNewEntry(CHILD2, CHILD2_BIRTH);
        helper.addNewEntry(SPOUSE, SPOUSE_BIRTH);

        Person myFather = getPersonByGivenName(FATHER);
        Person myMother = getPersonByGivenName(MOTHER);
        Person myBrother = getPersonByGivenName(BROTHER);
        Person me = getPersonByGivenName(ME);
        Person mySister = getPersonByGivenName(SISTER);
        Person myChild1 = getPersonByGivenName(CHILD1);
        Person myChild2 = getPersonByGivenName(CHILD2);
        Person mySpouse = getPersonByGivenName(SPOUSE);

        assertNotNull(myFather);
        assertNotNull(myMother);
        assertNotNull(myBrother);
        assertNotNull(me);
        assertNotNull(mySister);
        assertNotNull(myChild1);
        assertNotNull(myChild2);
        assertNotNull(mySpouse);

        helper.fireEditRelativesButton(ME, String.valueOf(ME_BIRTH));

        helper.personsTableHasOnlyThisMembers(myFather, myMother, myBrother, mySister, myChild1, myChild2, mySpouse);
        helper.parentsTableHasOnlyThisMembers();
        helper.spousesTableHasOnlyThisMembers();
        helper.siblingsTableHasOnlyThisMembers();
        helper.childrenTableHasOnlyThisMembers();

        helper.dragAndDropToTable(MOTHER, PARENTS_TABLE);

        helper.parentsTableHasOnlyThisMembers(myMother);
        helper.personsTableHasOnlyThisMembers(myFather, myBrother, mySister, myChild1, myChild2, mySpouse);

        helper.dragAndDropToTable(FATHER, PARENTS_TABLE);

        helper.parentsTableHasOnlyThisMembers(myFather, myMother);
        helper.personsTableHasOnlyThisMembers(myBrother, mySister, myChild1, myChild2, mySpouse);

        helper.fireDeleteButton(FATHER, "parentsGivenNameColumn");

        helper.parentsTableHasOnlyThisMembers(myMother);
        helper.personsTableHasOnlyThisMembers(myFather, myBrother, mySister, myChild1, myChild2, mySpouse);

        helper.fireDeleteButton(MOTHER, "parentsGivenNameColumn");

        helper.parentsTableHasOnlyThisMembers();
        helper.personsTableHasOnlyThisMembers(myFather, myMother, myBrother, mySister, myChild1, myChild2, mySpouse);

        helper.dragAndDropToTable(SPOUSE, SPOUSES_TABLE);

        helper.spousesTableHasOnlyThisMembers(mySpouse);
        helper.personsTableHasOnlyThisMembers(myFather, myMother, myBrother, mySister, myChild1, myChild2);

        helper.fireDeleteButton(SPOUSE, "spousesGivenNameColumn");

        helper.spousesTableHasOnlyThisMembers();
        helper.personsTableHasOnlyThisMembers(myFather, myMother, myBrother, mySister, myChild1, myChild2, mySpouse);

        helper.dragAndDropToTable(BROTHER, SIBLINGS_TABLE);

        helper.siblingsTableHasOnlyThisMembers(myBrother);
        helper.personsTableHasOnlyThisMembers(myFather, myMother, mySister, myChild1, myChild2, mySpouse);

        helper.dragAndDropToTable(SISTER, SIBLINGS_TABLE);

        helper.siblingsTableHasOnlyThisMembers(myBrother, mySister);
        helper.personsTableHasOnlyThisMembers(myFather, myMother, myChild1, myChild2, mySpouse);

        helper.fireDeleteButton(BROTHER, "siblingsGivenNameColumn");

        helper.siblingsTableHasOnlyThisMembers(mySister);
        helper.personsTableHasOnlyThisMembers(myFather, myMother, myBrother, myChild1, myChild2, mySpouse);

        helper.fireDeleteButton(SISTER, "siblingsGivenNameColumn");

        helper.siblingsTableHasOnlyThisMembers();
        helper.personsTableHasOnlyThisMembers(myFather, myMother, myBrother, mySister, myChild1, myChild2, mySpouse);

        helper.dragAndDropToTable(CHILD1, CHILDREN_TABLE);

        helper.childrenTableHasOnlyThisMembers(myChild1);
        helper.personsTableHasOnlyThisMembers(myFather, myMother, myBrother, mySister, myChild2, mySpouse);

        helper.dragAndDropToTable(CHILD2, CHILDREN_TABLE);

        helper.childrenTableHasOnlyThisMembers(myChild1, myChild2);
        helper.personsTableHasOnlyThisMembers(myFather, myMother, myBrother, mySister, mySpouse);

        helper.fireDeleteButton(CHILD1, "childrenGivenNameColumn");

        helper.childrenTableHasOnlyThisMembers(myChild2);
        helper.personsTableHasOnlyThisMembers(myFather, myMother, myBrother, mySister, myChild1, mySpouse);

        helper.fireDeleteButton(CHILD2, "childrenGivenNameColumn");

        helper.childrenTableHasOnlyThisMembers();
        helper.personsTableHasOnlyThisMembers(myFather, myMother, myBrother, mySister, myChild1, myChild2, mySpouse);
    }

    @Test
    @DisplayName("test if tables and model are synchronized correctly")
    void addRelatives() {
        helper.addNewEntry(GRANDFATHER, GRANDFATHER_BIRTH);
        helper.addNewEntry(GRANDMOTHER, GRANDMOTHER_BIRTH);
        helper.addNewEntry(FATHER, FATHER_BIRTH);
        helper.addNewEntry(MOTHER, MOTHER_BIRTH);
        helper.addNewEntry(BROTHER, BROTHER_BIRTH);
        helper.addNewEntry(ME, ME_BIRTH);
        helper.addNewEntry(SISTER, SISTER_BIRTH);
        helper.addNewEntry(CHILD1, CHILD1_BIRTH);
        helper.addNewEntry(CHILD2, CHILD2_BIRTH);

        Person grandfather = getPersonByGivenName(GRANDFATHER);
        Person grandmother = getPersonByGivenName(GRANDMOTHER);
        Person father = getPersonByGivenName(FATHER);
        Person mother = getPersonByGivenName(MOTHER);
        Person brother = getPersonByGivenName(BROTHER);
        Person me = getPersonByGivenName(ME);
        Person sister = getPersonByGivenName(SISTER);
        Person myChild1 = getPersonByGivenName(CHILD1);
        Person myChild2 = getPersonByGivenName(CHILD2);

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
        helper.fireEditRelativesButton(GRANDFATHER, String.valueOf(GRANDFATHER_BIRTH));

        helper.personsTableHasOnlyThisMembers(grandmother, father, mother, brother, me, sister, myChild1, myChild2);
        helper.parentsTableHasOnlyThisMembers();
        helper.spousesTableHasOnlyThisMembers();
        helper.siblingsTableHasOnlyThisMembers();
        helper.childrenTableHasOnlyThisMembers();

        helper.dragAndDropToTable(GRANDMOTHER, SPOUSES_TABLE);
        helper.dragAndDropToTable(FATHER, CHILDREN_TABLE);

        helper.personsTableHasOnlyThisMembers(mother, brother, me, sister, myChild1, myChild2);
        helper.parentsTableHasOnlyThisMembers();
        helper.spousesTableHasOnlyThisMembers(grandmother);
        helper.siblingsTableHasOnlyThisMembers();
        helper.childrenTableHasOnlyThisMembers(father);

        type(KeyCode.ENTER);

        // GRANDMOTHER:
        helper.fireEditRelativesButton(GRANDMOTHER, String.valueOf(GRANDMOTHER_BIRTH));

        helper.personsTableHasOnlyThisMembers(father, mother, brother, me, sister, myChild1, myChild2);
        helper.parentsTableHasOnlyThisMembers();
        helper.spousesTableHasOnlyThisMembers(grandfather);
        helper.siblingsTableHasOnlyThisMembers();
        helper.childrenTableHasOnlyThisMembers();

        helper.dragAndDropToTable(FATHER, CHILDREN_TABLE);

        helper.personsTableHasOnlyThisMembers(mother, brother, me, sister, myChild1, myChild2);
        helper.parentsTableHasOnlyThisMembers();
        helper.spousesTableHasOnlyThisMembers(grandfather);
        helper.siblingsTableHasOnlyThisMembers();
        helper.childrenTableHasOnlyThisMembers(father);

        type(KeyCode.ENTER);

        // FATHER:
        helper.fireEditRelativesButton(FATHER, String.valueOf(FATHER_BIRTH));

        helper.personsTableHasOnlyThisMembers(mother, brother, me, sister, myChild1, myChild2);
        helper.parentsTableHasOnlyThisMembers(grandmother, grandfather);
        helper.spousesTableHasOnlyThisMembers();
        helper.siblingsTableHasOnlyThisMembers();
        helper.childrenTableHasOnlyThisMembers();

        helper.dragAndDropToTable(MOTHER, SPOUSES_TABLE);
        helper.dragAndDropToTable(BROTHER, CHILDREN_TABLE);
        helper.dragAndDropToTable(ME, CHILDREN_TABLE);
        helper.dragAndDropToTable(SISTER, CHILDREN_TABLE);

        helper.personsTableHasOnlyThisMembers(myChild1, myChild2);
        helper.parentsTableHasOnlyThisMembers(grandmother, grandfather);
        helper.spousesTableHasOnlyThisMembers(mother);
        helper.siblingsTableHasOnlyThisMembers();
        helper.childrenTableHasOnlyThisMembers(brother, me, sister);

        type(KeyCode.ENTER);

        // MOTHER:
        helper.fireEditRelativesButton(MOTHER, String.valueOf(MOTHER_BIRTH));

        helper.personsTableHasOnlyThisMembers(brother, me, sister, myChild1, myChild2);
        helper.parentsTableHasOnlyThisMembers();
        helper.spousesTableHasOnlyThisMembers(father);
        helper.siblingsTableHasOnlyThisMembers();
        helper.childrenTableHasOnlyThisMembers();

        helper.dragAndDropToTable(BROTHER, CHILDREN_TABLE);
        helper.dragAndDropToTable(ME, CHILDREN_TABLE);
        helper.dragAndDropToTable(SISTER, CHILDREN_TABLE);

        helper.personsTableHasOnlyThisMembers(myChild1, myChild2);
        helper.parentsTableHasOnlyThisMembers();
        helper.spousesTableHasOnlyThisMembers(father);
        helper.siblingsTableHasOnlyThisMembers();
        helper.childrenTableHasOnlyThisMembers(brother, me, sister);

        type(KeyCode.ENTER);

        // ME:
        helper.fireEditRelativesButton(ME, String.valueOf(ME_BIRTH));

        helper.personsTableHasOnlyThisMembers(myChild1, myChild2);
        helper.parentsTableHasOnlyThisMembers(father, mother);
        helper.spousesTableHasOnlyThisMembers();
        helper.siblingsTableHasOnlyThisMembers(brother, sister);
        helper.childrenTableHasOnlyThisMembers();

        helper.dragAndDropToTable(CHILD1, CHILDREN_TABLE);
        helper.dragAndDropToTable(CHILD2, CHILDREN_TABLE);

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
