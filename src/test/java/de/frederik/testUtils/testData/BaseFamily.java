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

    public final static String GRANDFATHER = "myGrandfather";
    public final static int GRANDFATHER_BIRTH = 1930;
    public final static String GRANDMOTHER = "myGrandmother";
    public final static int GRANDMOTHER_BIRTH = 1932;
    public final static String FATHER = "myFather";
    public final static int FATHER_BIRTH = 1960;
    public final static String MOTHER = "myMother";
    public final static int MOTHER_BIRTH = 1962;
    public final static String ME = "mainPerson";
    public final static int ME_BIRTH = 1992;
    public final static String BROTHER = "myBrother";
    public final static int BROTHER_BIRTH = 1990;
    public final static String SISTER = "mySister";
    public final static int SISTER_BIRTH = 1994;
    public final static String CHILD1 = "myChild1";
    public final static int CHILD1_BIRTH = 2020;
    public final static String CHILD2 = "myChild2";
    public final static int CHILD2_BIRTH = 2022;

    public final static String SPOUSE = "mySpouse";
    public final static int SPOUSE_BIRTH = 1991;
    public final static String BROTHERS_CHILD = "myBrothersChild";
    public final static int BROTHERS_CHILD_BIRTH = 2022;

    public final static String ALIEN = "an alien";
    public final static int ALIEN_BIRTH = 1966;
    public final static String ALIENS_FATHER = "aliensFather";
    public final static int ALIENS_FATHER_BIRTH = 1933;
    public final static String ALIENS_MOTHER = "aliensMother";
    public final static int ALIENS_MOTHER_BIRTH = 1932;

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

        Person grandfather = createPerson(GRANDFATHER, GRANDFATHER_BIRTH);
        Person grandmother = createPerson(GRANDMOTHER, GRANDMOTHER_BIRTH);

        Person father = createPerson(FATHER, FATHER_BIRTH);
        Person mother = createPerson(MOTHER, MOTHER_BIRTH);

        Person brother = createPerson(BROTHER, BROTHER_BIRTH);
        Person me = createPerson(ME, ME_BIRTH);
        Person sister = createPerson(SISTER, SISTER_BIRTH);

        Person myChild1 = createPerson(CHILD1, CHILD1_BIRTH);
        Person myChild2 = createPerson(CHILD2, CHILD2_BIRTH);

        grandfather.addChild(father);
        grandfather.addSpouse(grandmother);

        grandmother.addChild(father);
        grandmother.addSpouse(grandfather);

        father.addParent(grandfather, grandmother);
        father.addSpouse(mother);
        father.addChild(brother, me, sister);

        mother.addSpouse(father);
        mother.addChild(brother, me, sister);

        brother.addParent(father, mother);
        brother.addSibling(me, sister);

        me.addParent(father, mother);
        me.addSibling(brother, sister);
        me.addChild(myChild1, myChild2);

        sister.addParent(father, mother);
        sister.addSibling(brother, me);

        myChild1.addParent(me);
        myChild1.addSibling(myChild2);

        myChild2.addParent(me);
        myChild2.addSibling(myChild1);
    }
}
