package de.frederik.unitTests.jUnitTests.kinship;

import de.frederik.testUtils.testData.TestDatabase;
import de.pedigreeProject.kinship.GeneticKinshipCalculator;
import de.pedigreeProject.model.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GeneticKinshipCalculatorTest {

    @ParameterizedTest(name = "{index} testing kinship of {0} ")
    @MethodSource("de.frederik.testUtils.testData.TestDatabase#getRandomPersons")
    @DisplayName("Calculator: test case with random persons")
    void getRelatives_RandomPersons(Person person) {
        Set<Person> expectedSet = computeExpectedPersonsSet(person);
        assertEquals(expectedSet, new GeneticKinshipCalculator().getRelatives(person), "sets should be equal of " + person);
    }
    /**
     * Pedigree of Buddenbrooks:
     * @see <a href="http://buddenbrookhaus.de/file/stb_fam_buddenbrook.pdf">buddenbrookhaus.de</a>
     */
    @ParameterizedTest(name = "{index} testing kinship of {0} ")
    @MethodSource("de.frederik.testUtils.testData.TestDatabase#getBuddenbrooks")
    @DisplayName("Calculator: test case with Buddenbrooks")
    void getRelatives_Buddenbrooks(Person person) {
        Set<Person> expectedSet = computeExpectedPersonsSet(person);
        assertEquals(expectedSet, new GeneticKinshipCalculator().getRelatives(person), "sets should be equal of " + person);
    }

    private Set<Person> computeExpectedPersonsSet(Person person) {
        // all persons in parental line are related by blood with person
        Set<Person> parentalLine = new HashSet<>();
        collectParentalLineOfPersonToList(person, parentalLine);
        // all siblings of person are related by blood with person
        Set<Person> siblings = new HashSet<>();
        collectAllSiblingsOfPersonToList(person, siblings);
        // all siblings of persons in parental line are related by blood to person
        parentalLine.forEach(parent -> collectAllSiblingsOfPersonToList(parent, siblings));
        // all persons in children's line are related by blood with person
        Set<Person> childrenLine = new HashSet<>();
        collectChildrenLineOfPersonToList(person, childrenLine);
        // whole children's line of persons siblings and parental line are related by blood with person
        parentalLine.addAll(siblings);
        parentalLine.forEach(parent -> collectChildrenLineOfPersonToList(parent, childrenLine));

        parentalLine.addAll(childrenLine);
        parentalLine.add(person);
        return parentalLine;
    }

    private void collectParentalLineOfPersonToList(Person person, Set<Person> parentalLine) {
        for (Person parent : person.getParents()) {
            collectParentalLineOfPersonToList(parent, parentalLine);
            parentalLine.add(parent);
        }
    }

    private void collectAllSiblingsOfPersonToList(Person person, Set<Person> siblings) {
        siblings.addAll(person.getSiblings());
    }

    private void collectChildrenLineOfPersonToList(Person person, Set<Person> childrenLine) {
        person.getChildren().forEach(child -> {
            collectChildrenLineOfPersonToList(child, childrenLine);
            childrenLine.add(child);
        });
    }
    @Test
    @DisplayName("Calculator: test computing of generation")
    void testGeneration() {
        List<Person> baseFamily = TestDatabase.getBaseFamily();

        for (Person person : baseFamily) {

            Map<Person, Integer> expectedMap = createExpectedMap(person);
            assertEquals(expectedMap, new GeneticKinshipCalculator().getPersonGenerationMap(person), "Maps should be equal of " + person);
            assertTrue(expectedMap.keySet().containsAll(new GeneticKinshipCalculator().getRelatives(person)));
        }
    }

    private Map<Person, Integer> createExpectedMap(Person person) {

        final int generationGrandParents;
        final int generationParents;
        final int generationMainPerson;
        final int generationChildren;

        switch (person.getGivenName()) {
            case "myGrandfather", "myGrandmother" -> {
                generationGrandParents = 0;
                generationParents = 1;
                generationMainPerson = 2;
                generationChildren = 3;
            }
            case "myFather", "myMother" -> {
                generationGrandParents = -1;
                generationParents = 0;
                generationMainPerson = 1;
                generationChildren = 2;
            }
            case "myBrother", "mainPerson", "mySister" -> {
                generationGrandParents = -2;
                generationParents = -1;
                generationMainPerson = 0;
                generationChildren = 1;
            }
            default -> {//"myChild1" and "myChild2"
                generationGrandParents = -3;
                generationParents = -2;
                generationMainPerson = -1;
                generationChildren = 0;
            }
        }
        return getMapOfGenerations(person, generationGrandParents, generationParents, generationMainPerson, generationChildren);
    }

    private Map<Person, Integer> getMapOfGenerations(Person person, int generationGrandParents, int generationParents, int generationMainPerson, int generationChildren) {
        Set<Person> personSet = computeExpectedPersonsSet(person);
        Map<Person, Integer> baseFamilyMap = new HashMap<>();

        personSet.stream().filter(p -> p.getGivenName().equals("myGrandfather")).findFirst().ifPresent(p -> baseFamilyMap.put(p, generationGrandParents));
        personSet.stream().filter(p -> p.getGivenName().equals("myGrandmother")).findFirst().ifPresent(p -> baseFamilyMap.put(p, generationGrandParents));

        personSet.stream().filter(p -> p.getGivenName().equals("myFather")).findFirst().ifPresent(p -> baseFamilyMap.put(p, generationParents));
        personSet.stream().filter(p -> p.getGivenName().equals("myMother")).findFirst().ifPresent(p -> baseFamilyMap.put(p, generationParents));

        personSet.stream().filter(p -> p.getGivenName().equals("myBrother")).findFirst().ifPresent(p -> baseFamilyMap.put(p, generationMainPerson));
        personSet.stream().filter(p -> p.getGivenName().equals("mainPerson")).findFirst().ifPresent(p -> baseFamilyMap.put(p, generationMainPerson));
        personSet.stream().filter(p -> p.getGivenName().equals("mySister")).findFirst().ifPresent(p -> baseFamilyMap.put(p, generationMainPerson));

        personSet.stream().filter(p -> p.getGivenName().equals("myChild1")).findFirst().ifPresent(p -> baseFamilyMap.put(p, generationChildren));
        personSet.stream().filter(p -> p.getGivenName().equals("myChild2")).findFirst().ifPresent(p -> baseFamilyMap.put(p, generationChildren));

        return baseFamilyMap;
    }
}