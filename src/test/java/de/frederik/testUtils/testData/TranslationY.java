package de.frederik.testUtils.testData;

import de.pedigreeProject.model.Person;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides a family for testing translation of labels.<br>
 * Each member has filled relativesLists.
 * <ul>
 * <li>"mySpouse", "", Year.of(1988)</li>
 * <li>"siblingOfSpouse", "", Year.of(1999)</li>
 * <li>"myBrother", "", Year.of(1995)</li>
 * <li>"mainPerson", "", Year.of(1985)</li>
 * <li>"mySister", "", Year.of(1990)</li>
 * <li>"myChild", "", Year.of(2000)</li>
 * </ul>
 */
public class TranslationY implements TestPersons {

    public static final String ME = "mainPerson";
    public static final int ME_BIRTH = 1985;
    public static final String MY_BROTHER = "myBrother";
    public static final int MY_BROTHER_BIRTH = 1995;
    public static final String MY_SISTER = "mySister";
    public static final int MY_SISTER_BIRTH = 1990;
    public static final String MY_SPOUSE = "mySpouse";
    public static final int MY_SPOUSE_BIRTH = 1988;
    public static final String MY_CHILD = "myChild";
    public static final int MY_CHILD_BIRTH = 2000;
    public static final String SIBLING_OF_SPOUSE = "siblingOfSpouse";
    public static final int SIBLING_OF_SPOUSE_BIRTH = 1999;

    private static int countId;
    private static List<Person> persons;

    /**
     * @see TranslationY
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

        Person mySpouse = createPerson(MY_SPOUSE, MY_SPOUSE_BIRTH);
        Person siblingOfSpouse = createPerson(SIBLING_OF_SPOUSE, SIBLING_OF_SPOUSE_BIRTH);

        Person me = createPerson(ME, ME_BIRTH);
        Person myBrother = createPerson(MY_BROTHER, MY_BROTHER_BIRTH);
        Person mySister = createPerson(MY_SISTER, MY_SISTER_BIRTH);

        Person myChild = createPerson(MY_CHILD, MY_CHILD_BIRTH);

        me.addSibling(myBrother, mySister);
        me.addChild(myChild);
        me.addSpouse(mySpouse);
        myBrother.addSibling(me, mySister);
        mySister.addSibling(me, myBrother);
        mySpouse.addSpouse(me);
        mySpouse.addChild(myChild);
        mySpouse.addSibling(siblingOfSpouse);
        myChild.addParent(me, mySpouse);
    }
}
