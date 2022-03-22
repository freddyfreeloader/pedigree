package de.frederik.testUtils.testData;

import de.pedigreeProject.model.Person;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides a family for testing.<br>
 * Each member has filled relativesLists.
 * <ul>
 * <li>"oldest", "", Year.of(2000)</li>
 * <li>"middle", "", Year.of(2001)</li>
 * <li>"youngest", "", Year.of(2002)</li>
 * </ul>
 */
public class ThreeSiblings implements TestPersons {

    private static int countId;
    private static List<Person> persons;

    /**
     * @see ThreeSiblings
     */
    @Override
    public List<Person> getPersons() {
        countId = 0;
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

        Person oldest = createPerson("oldest", 2000);
        Person middle = createPerson( "middle", 2001);
        Person youngest = createPerson("youngest", 2002);

        oldest.addSibling(middle, youngest);
        middle.addSibling(oldest, youngest);
        youngest.addSibling(middle, oldest);
    }
}
