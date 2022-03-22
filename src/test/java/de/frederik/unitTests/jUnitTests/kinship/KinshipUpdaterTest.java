package de.frederik.unitTests.jUnitTests.kinship;

import de.pedigreeProject.kinship.CloseKinshipUpdater;
import de.pedigreeProject.kinship.KinshipUpdater;
import de.pedigreeProject.model.Person;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class KinshipUpdaterTest {

    KinshipUpdater kinshipUpdater = new CloseKinshipUpdater();

    @Test
    @DisplayName("adding a spouse to person should add person to spouses spousesList")
    void updateSpouse_AddSpouse() {

        Person husband = createPerson(1);
        Person wife = createPerson(2);
        husband.addSpouse(wife);

        kinshipUpdater.updateKinship(husband, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(husband.getSpouses()), new ArrayList<>());

        List<Person> actualList = new ArrayList<>();
        actualList.addAll(husband.getSpouses());
        actualList.addAll(wife.getSpouses());

        assertEquals(List.of(wife, husband), actualList, "wife should have husband in her spousesList");
    }

    @Test
    @DisplayName("removing a spouse from person should remove person from spouses spousesList")
    void updateSpouse_RemoveSpouse() {

        Person husband = createPerson(1);
        Person wife = createPerson(2);
        husband.addSpouse(wife);
        wife.addSpouse(husband);

        kinshipUpdater.updateKinship(husband, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        List<Person> actualList = new ArrayList<>();
        actualList.addAll(husband.getSpouses());
        actualList.addAll(wife.getSpouses());

        assertEquals(Collections.emptyList(), actualList, "neither husband nor wife should have spouses ");
    }

    @Test
    @DisplayName("adding Parents: check transitive updates")
    void updateParents_AddParents() {

        Person person = createPerson(1);
        Person mother = createPerson(2);
        Person father = createPerson(3);
        Person childOfMother = createPerson(4);
        Person childOfFather = createPerson(5);

        person.addParent(mother);
        person.addParent(father);
        mother.addChild(childOfMother);
        father.addChild(childOfFather);

        kinshipUpdater.updateKinship(person, new ArrayList<>(person.getParents()), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        // person has parents and children
        assertEquals(Set.of(mother, father), person.getParents());
        assertEquals(Set.of(), person.getChildren());
        assertEquals(Set.of(), person.getSpouses());
        assertEquals(Set.of(childOfFather, childOfMother), person.getSiblings());
        // mother get person as child
        assertEquals(Set.of(), mother.getParents());
        assertEquals(Set.of(childOfMother, person), mother.getChildren());
        assertEquals(Set.of(), mother.getSpouses());
        assertEquals(Set.of(), mother.getSiblings());
        // father gets person as child
        assertEquals(Set.of(), father.getParents());
        assertEquals(Set.of(childOfFather, person), father.getChildren());
        assertEquals(Set.of(), father.getSpouses());
        assertEquals(Set.of(), father.getSiblings());
        // childOfFather gets father and person as sibling
        assertEquals(Set.of(father), childOfFather.getParents());
        assertEquals(Set.of(), childOfFather.getChildren());
        assertEquals(Set.of(), childOfFather.getSpouses());
        assertEquals(Set.of(person), childOfFather.getSiblings());
        // childOfMother gets mother and person as sibling
        assertEquals(Set.of(mother), childOfMother.getParents());
        assertEquals(Set.of(), childOfMother.getChildren());
        assertEquals(Set.of(), childOfMother.getSpouses());
        assertEquals(Set.of(person), childOfMother.getSiblings());
    }

    @Test
    @DisplayName("remove Parents: check relative lists")
    void updateParents_RemoveParents() {
        Person person = createPerson(1);
        Person mother = createPerson(2);
        Person father = createPerson(3);
        Person childOfMother = createPerson(4);
        Person childOfFather = createPerson(5);

        person.addParent(mother);
        person.addParent(father);
        mother.addChild(childOfMother);
        father.addChild(childOfFather);

        // firstly create relationship, see Test: updateParents_AddParents
        kinshipUpdater.updateKinship(person, new ArrayList<>(person.getParents()), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        // secondly remove only parents
        kinshipUpdater.updateKinship(person, new ArrayList<>(), new ArrayList<>(person.getChildren()), new ArrayList<>(person.getSpouses()), new ArrayList<>(person.getSiblings()));

        // person should have no more parents
        assertEquals(Set.of(), person.getParents());
        assertEquals(Set.of(), person.getChildren());
        assertEquals(Set.of(), person.getSpouses());
        assertEquals(Set.of(childOfFather, childOfMother), person.getSiblings());
        // mother should not have person as child, but still his other child
        assertEquals(Set.of(), mother.getParents());
        assertEquals(Set.of(childOfMother), mother.getChildren());
        assertEquals(Set.of(), mother.getSpouses());
        assertEquals(Set.of(), mother.getSiblings());
        // father should not have person as child, but still his other child
        assertEquals(Set.of(), father.getParents());
        assertEquals(Set.of(childOfFather), father.getChildren());
        assertEquals(Set.of(), father.getSpouses());
        assertEquals(Set.of(), father.getSiblings());
        // childOfFather: kinship between child of Mother and child of father to their parents should NOT be changed
        assertEquals(Set.of(father), childOfFather.getParents());
        assertEquals(Set.of(), childOfFather.getChildren());
        assertEquals(Set.of(), childOfFather.getSpouses());
        assertEquals(Set.of(person), childOfFather.getSiblings());
        // childOfMother: kinship between child of Mother and child of father to their parents should NOT be changed
        assertEquals(Set.of(mother), childOfMother.getParents());
        assertEquals(Set.of(), childOfMother.getChildren());
        assertEquals(Set.of(), childOfMother.getSpouses());
        assertEquals(Set.of(person), childOfMother.getSiblings());
    }

    @Test
    @DisplayName("If father gets children, then children are siblings to each other")
    void updateChildren_AddChildren() {

        Person father = createPerson(1);
        Person child1 = createPerson(2);
        Person child2 = createPerson(3);

        father.addChild(child1, child2);

        kinshipUpdater.updateKinship(father, new ArrayList<>(), new ArrayList<>(father.getChildren()), new ArrayList<>(), new ArrayList<>());
        // father have two children
        assertEquals(Set.of(child1, child2), father.getChildren());
        // children have got father
        assertEquals(Set.of(father), child1.getParents());
        assertEquals(Set.of(father), child2.getParents());
        // children are siblings
        assertEquals(Set.of(child2), child1.getSiblings());
        assertEquals(Set.of(child1), child2.getSiblings());
    }

    @Test
    @DisplayName("Children stay siblings to each other, if father remove his children")
    void updateChildren_RemoveChildren() {
        Person father = createPerson(1);
        Person child1 = createPerson(2);
        Person child2 = createPerson(3);

        father.addChild(child1, child2);

        // firstly add children, see test updateChildren_AddChildren()
        kinshipUpdater.updateKinship(father, new ArrayList<>(), new ArrayList<>(father.getChildren()), new ArrayList<>(), new ArrayList<>());
        // secondly remove Children
        kinshipUpdater.updateKinship(father, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        // father is removed
        assertEquals(Set.of(), child1.getParents());
        assertEquals(Set.of(), child2.getParents());
        // children are still siblings
        assertEquals(Set.of(child1), child2.getSiblings());
        assertEquals(Set.of(child2), child1.getSiblings());
    }

    @Test
    @DisplayName("siblings list should not be updated transitive")
    void updateSiblings_AddSiblings() {

        Person person = createPerson(1);
        Person sibling1 = createPerson(2);
        Person sibling2 = createPerson(3);

        person.addSibling(sibling1, sibling2);

        kinshipUpdater.updateKinship(person, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(person.getSiblings()));

        assertEquals(Set.of(sibling1, sibling2), person.getSiblings());
        assertEquals(Set.of(person), sibling2.getSiblings());
        assertEquals(Set.of(person), sibling1.getSiblings());
    }

    @Test
    @DisplayName("")
    void updateSiblings_RemoveSiblings() {
        Person person = createPerson(1);
        Person sibling1 = createPerson(2);
        Person sibling2 = createPerson(3);
        person.addSibling(sibling1, sibling2);

        // firstly add siblings, see test updateSiblings_AddSiblings()
        kinshipUpdater.updateKinship(person, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(person.getSiblings()));

        // secondly remove siblings
        kinshipUpdater.updateKinship(person, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        assertEquals(Set.of(), person.getSiblings());
        assertEquals(Set.of(), sibling1.getSiblings());
        assertEquals(Set.of(), sibling2.getSiblings());
    }

    private Person createPerson(int id) {

        return new Person(id, 1, "person"+id, "", null);
    }
}