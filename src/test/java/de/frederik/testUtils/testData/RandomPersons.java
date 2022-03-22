package de.frederik.testUtils.testData;

import de.pedigreeProject.model.Person;
import org.jetbrains.annotations.Nullable;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Creator for a list of persons who are partly related to each other.
 * May be used for test cases with large data.
 */
public class RandomPersons implements TestPersons {

    @Override
    public List<Person> getPersons() {
        return createRandomTestPersons();
    }

    private List<Person> createRandomTestPersons() {
        int numberOfParents = 50;
        int numberOfChildren = 50;
        // create n new persons for parental line
        List<Person> personsForParentalLine = new ArrayList<>();
        for (int i = 0; i <= numberOfParents; i++) {
            Person person = new Person(i, 1, "Parent nr.".concat(String.valueOf(i)),
                    "",
                    Year.of(ThreadLocalRandom.current().nextInt(1800, 2020)));
            personsForParentalLine.add(person);
        }

        // create n new persons for children's line of siblings
        List<Person> personsForSiblingsChildrensLine = new ArrayList<>();
        for (int i = numberOfParents + 1; i <= numberOfParents + numberOfChildren; i++) {
            Person person = new Person(i, 1,
                    "Child nr.".concat(String.valueOf(i)),
                    "",
                    null);
            personsForSiblingsChildrensLine.add(person);
        }

        List<Person> finalPersonsList = new ArrayList<>();
        List<Person> siblingsOfParents = new ArrayList<>();

        Person me = getAndRemoveRandomPersonFromList(personsForParentalLine);

        createParentline(me, personsForParentalLine, finalPersonsList, siblingsOfParents);

        createChildrensLine(finalPersonsList, personsForSiblingsChildrensLine, siblingsOfParents);

        return finalPersonsList;
    }


    /**
     * Moves recursively persons from the {@code personsForParentalLine} list to the {@code finalPersonList}.<br>
     * Each person gets randomly 0-2 parents and 0-1 siblings.<br>
     * For each parent this method will be called again.<br>
     * The siblings were added to the siblingsOfParents list, used by {@link #createChildrensLine}.<br>
     *
     * @param me person that will get 0,1 or 2 parents and 0 or 1 sibling
     * @param personsForParentalLine list from where parents and siblings are taken from
     * @param finalPersonsList the final list
     * @param siblingsOfParents list where siblings of parent are saved
     */
    private static void createParentline(Person me, List<Person> personsForParentalLine, List<Person> finalPersonsList, List<Person> siblingsOfParents) {
        if (me != null) {
            finalPersonsList.add(me);
            Person parent1 = getAndRemoveRandomPersonFromList(personsForParentalLine);
            Person parent2 = getAndRemoveRandomPersonFromList(personsForParentalLine);
            Person siblingOfMe = getAndRemoveRandomPersonFromList(personsForParentalLine);
            if (parent1 != null) {

                me.addParent(parent1);
                parent1.addChild(me);
                if (siblingOfMe != null) {
                    finalPersonsList.add(siblingOfMe);
                    siblingsOfParents.add(siblingOfMe);
                    parent1.addChild(siblingOfMe);
                    siblingOfMe.addParent(parent1);
                    me.addSibling(siblingOfMe);
                    siblingOfMe.addSibling(me);
                }
            }
            if (parent1 != null && parent2 != null) {
                me.addParent(parent2);
                parent2.addChild(me);
                parent1.addSpouse(parent2);
                parent2.addSpouse(parent1);
                if (siblingOfMe != null) {
                    parent2.addChild(siblingOfMe);
                    siblingOfMe.addParent(parent2);
                }
            }
            me.getParents().forEach(parent -> createParentline(parent, personsForParentalLine, finalPersonsList, siblingsOfParents));
        }
    }

    /**
     * Iterates through {@code siblingsOfParents} list (created in {@link #createParentline}) and adds 0-1 children to each member.
     *
     */
    private static void createChildrensLine(List<Person> finalPersonsList, List<Person> personsForSiblingsChildrensLine, List<Person> siblingsOfParents) {
        Person person = getAndRemoveRandomPersonFromList(siblingsOfParents);
        if (person != null) {
            Person randomChild = getAndRemoveRandomPersonFromList(personsForSiblingsChildrensLine);

            if (randomChild != null) {

                finalPersonsList.add(randomChild);
                person.addChild(randomChild);
                randomChild.addParent(person);
            }
            createChildrensLine(finalPersonsList, personsForSiblingsChildrensLine, siblingsOfParents);
        }
    }

    private static @Nullable Person getAndRemoveRandomPersonFromList(List<Person> personList) {
        int size = personList.size();
        if (size > 0) {
            return personList.remove(ThreadLocalRandom.current().nextInt(0, size));
        } else {
            return null;
        }
    }
}
