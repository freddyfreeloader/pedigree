package de.frederik.testUtils.testData.csvTestData;

import de.pedigreeProject.model.Person;

import java.util.ArrayList;
import java.util.List;

public class CouldBeSpouse extends TestCSV_Factory {

    private final List<String[]> stringArray= new ArrayList<>();

    private Person person;
    private Person aspirant;


    public CouldBeSpouse(final List<Person> persons) {

        generateArray(persons);
    }

    @Override
    public List<String[]> getStringArray() {
        return this.stringArray;
    }

    private void  generateArray(final List<Person> persons) {

        for (int i = 0; i < persons.size(); i++) {
            person = persons.get(i);

            for (Person p : persons) {
                this.aspirant = p;
                if (isEqual.test(aspirant, person)) {
                    addArrayToList( true, "Persons are equal.");
                }
                if (isMarried.test(person)) {
                    addArrayToList( true, aspirant + " is already spouse of " + person);
                }
                if (isMarried.test(aspirant) && !aspirant.getSpouses().contains(person)) {
                    addArrayToList( true, aspirant + " is already married with " + aspirant.getSpouses());
                }
                if (aspirantIsInParentalLineOfPerson.test(aspirant, person)) {
                    addArrayToList( true, aspirant + " is in parental line of " + person);
                }
                if (aspirantIsInParentalLineOfPerson.test(person, aspirant)) {
                    addArrayToList( true, person + " is in parental line of " + aspirant);
                }
                if (aspirantIsInChildrenLineOfPerson.test(person, aspirant)) {
                    addArrayToList( true, person + " is in children's line of " + aspirant);
                }
                if (aspirantIsInChildrenLineOfPerson.test(aspirant, person)) {
                    addArrayToList( true, aspirant + " is in children's line of " + person);
                }
                if (isSibling.test(aspirant, person) || isSibling.test(person, aspirant) ) {
                    addArrayToList( true, aspirant + " is sibling of " + person);
                }
                if (isSibling.test(aspirant, person) || isSibling.test(person, aspirant) ) {
                    addArrayToList( true, aspirant + " is sibling of " + person);
                }
                // Check if method could return null
                if (aspirant != person && !isMarried.test(person) && aspirant.toString().equals("Alien Alien")) {
                    addArrayToList( false, "Aspirant is alien");
                }
            }
        }
    }

    private void addArrayToList( boolean boo, String additionalMessage) {
        String emptyOrNot = boo ? "" : "NOT";
        String LIST_NAME = "spouses list";

        String[] array = new String[4];
        array[0] = aspirant.toString();
        array[1] = person.toString();
        array[2] = String.valueOf(boo);
        array[3] = "Adding '" + aspirant + "' to " + LIST_NAME +" of '" + person + "' should " + emptyOrNot + " fail" +
                "\nbecause " + additionalMessage;

        stringArray.add(array);
    }
}
