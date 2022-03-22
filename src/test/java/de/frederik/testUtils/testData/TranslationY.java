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

        Person mySpouse = createPerson("mySpouse", 1988);
        Person siblingOfSpouse = createPerson("siblingOfSpouse", 1999);

        Person me = createPerson("mainPerson", 1985);
        Person myBrother = createPerson("myBrother", 1995);
        Person mySister = createPerson("mySister", 1990);

        Person myChild = createPerson("myChild", 2000);

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
