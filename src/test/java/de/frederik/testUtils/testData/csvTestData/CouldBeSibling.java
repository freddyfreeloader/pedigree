package de.frederik.testUtils.testData.csvTestData;

import de.pedigreeProject.model.Person;

import java.util.ArrayList;
import java.util.List;

public class CouldBeSibling extends TestCSV_Factory {

    private final List<String[]> stringArray = new ArrayList<>();

    private Person person;
    private Person aspirant;

    public CouldBeSibling(final List<Person> persons) {

        generateArray(persons);
    }

    @Override
    public List<String[]> getStringArray() {
        return stringArray;
    }

    private void generateArray(final List<Person> persons) {


        for (int i = 0; i < persons.size(); i++) {
            person = persons.get(i);

            for (Person p : persons) {
                this.aspirant = p;
                if (isEqual.test(aspirant, person)) {
                    getArray(true, "Persons are equal.");
                }
                if (aspirantIsSpouseOfPerson.test(aspirant, person) || aspirantIsSpouseOfPerson.test(person, aspirant)) {
                    getArray(true, aspirant + " is already spouse of " + person);
                }
                if (isSibling.test(person, aspirant)) {
                    getArray(true, aspirant + " is already sibling of " + person);
                }
                if (aspirantIsInParentalLineOfPerson.test(aspirant, person) || aspirantIsInParentalLineOfPerson.test(person, aspirant)) {
                    getArray(true, aspirant + " is in parental line of " + person);
                }
                if (aspirantIsInChildrenLineOfPerson.test(aspirant, person) || aspirantIsInChildrenLineOfPerson.test(person, aspirant)) {
                    getArray(true, aspirant + " is in children's line of " + person);
                }
                if (hasTwoParents.test(person) && hasTwoParents.test(aspirant) && !hasOneCommonParent.test(aspirant, person)) {
                    getArray(true, person + " has already two other parents : " + person.getParents() + "\n" +
                            aspirant + " has already two other parents : " + aspirant.getParents());
                }
                // Check if method could return null
                if (aspirant != person && aspirant.toString().equals("Alien Alien")) {
                    getArray(false, "Aspirant is alien");

                }
            }
        }
    }

    private void getArray(boolean boo, String additionalMessage) {
        String emptyOrNot = boo ? "" : "NOT";
        String LIST_NAME = "siblings list";

        String[] array = new String[4];
        array[0] = aspirant.toString();
        array[1] = person.toString();
        array[2] = String.valueOf(boo);
        array[3] = "Adding '" + aspirant + "' to " + LIST_NAME + " of '" + person + "' should " + emptyOrNot + " fail" +
                "\nbecause " + additionalMessage;
        stringArray.add(array);
    }
}
