package de.frederik.testUtils.testData;

import de.pedigreeProject.model.Person;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides a family for testing.<br>
 * Each member has filled relativesLists.
 * <ul>
 * <li>"myGrandfather", "", Year.of(1930)</li>
 * <li>"myGrandmother", "", Year.of(1932)</li>
 * <li>"myFather", "", Year.of(1960)</li>
 * <li>"myMother", "", Year.of(1962)</li>
 * <li>"myBrother", "", Year.of(1990)</li>
 * <li>"mainPerson", "", Year.of(1992)</li>
 * <li>"mySister", "", Year.of(1994)</li>
 * <li>"myChild1", "", Year.of(2020)</li>
 * <li>"myChild2", "", Year.of(2022)</li>
 * </ul>
 */
public class BaseFamily implements TestPersons {

    private static int countId;
    private static List<Person> persons;

    /**
     * @see BaseFamily
     */
    @Override
    public List<Person> getPersons() {
        countId = 1;
        persons = new ArrayList<>();
        createPersonsList();

        return persons;
    }
    private static Person createPerson(String givenName, int yearOfBirth) {

        Person person = new Person(++countId, 1, givenName, "", Year.of(yearOfBirth));
        persons.add(person);
        return person;
    }

    private static void createPersonsList() {

        Person grandfather = createPerson("myGrandfather", 1930);
        Person grandmother = createPerson( "myGrandmother", 1932);

        Person father = createPerson("myFather", 1960);
        Person mother = createPerson("myMother", 1962);

        Person brother = createPerson("myBrother", 1990);
        Person me = createPerson("mainPerson", 1992);
        Person sister = createPerson("mySister", 1994);

        Person myChild1 = createPerson("myChild1", 2020);
        Person myChild2 = createPerson("myChild2", 2022);

        grandfather.addChild(father);
        grandmother.addChild(father);
        grandfather.addSpouse(grandmother);
        grandmother.addSpouse(grandfather);

        father.addParent(grandfather, grandmother);
        father.addSpouse(mother);
        father.addChild(brother, me, sister);
        mother.addSpouse(father);
        mother.addChild(brother, me, sister);

        brother.addParent(father, mother);
        me.addParent(father, mother);
        sister.addParent(father, mother);
        brother.addSibling(me, sister);
        me.addSibling(brother, sister);
        sister.addSibling(brother, me);

        me.addChild(myChild1, myChild2);
        myChild1.addParent(me);
        myChild2.addParent(me);
        myChild1.addSibling(myChild2);
        myChild2.addSibling(myChild1);
    }
}
