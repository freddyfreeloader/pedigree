package de.frederik.integrationTests.jUnit.model_Integration_Test;

import de.pedigreeProject.model.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class ModelTest_PersonData extends ModelTest {


    @ParameterizedTest(name = "{index} Create person with given name: <{0}>, family name: <{1}>, year of birth: {2} should throw an IllegalArgumentException")
    @MethodSource("personsForCreateNewPersonTest_IllegalArguments")
    @DisplayName("createPerson(): illegal arguments")
    void createNewPerson_IllegalArguments(String givenName, String familyName, Year yearOfBirth) {

        assertThrows(IllegalArgumentException.class, () -> model.createPerson(givenName, familyName, yearOfBirth));
    }

    public static Stream<Arguments> personsForCreateNewPersonTest_IllegalArguments() {
        return Stream.of(
                //givenName, familyName, yearOfBirth
                Arguments.of("", "", null),
                Arguments.of("   ", "", null),
                Arguments.of("", "  ", null),
                Arguments.of("   ", "  ", null),
                Arguments.of("   ", null, null),
                Arguments.of(null, "  ", null),
                Arguments.of(null, null, Year.of(2000))
        );
    }

    @ParameterizedTest(name = "{index} Create person with given name: <{0}>, family name: <{1}>, year of birth: {2} should return Optional.empty()")
    @MethodSource("personsForCreateNewPersonTest_Optional_Empty")
    @DisplayName("createPerson(): name already exists")
    void createNewPerson_Name_Already_Exists(String givenName, String familyName, Year yearOfBirth) {
        model.createPerson("Test", "", null);
        model.createPerson(null, "Test", null);

        assertFalse(model.createPerson(givenName, familyName, yearOfBirth).isPresent());
    }

    public static Stream<Arguments> personsForCreateNewPersonTest_Optional_Empty() {
        return Stream.of(
                //givenName, familyName, yearOfBirth
                Arguments.of("Test", null, null),
                Arguments.of("Test   ", null, null),
                Arguments.of("    Test", null, null),
                Arguments.of("   Test   ", null, null),
                Arguments.of(null, "Test", null),
                Arguments.of(null, "Test   ", null),
                Arguments.of(null, "   Test", null),
                Arguments.of(null, "   Test   ", null)
        );
    }

    @Test
    @DisplayName("deletePerson(): sync model and database")
    void deletePerson() {

        Person testPerson = createTestPerson();

        model.deletePerson(testPerson);

        assertFalse(model.getPersons().contains(testPerson), "person should be deleted from personsList");
        assertFalse(personGateway.readPersons(model.getCurrentPedigree()).contains(testPerson), "person should be deleted from database");
    }

    @Test
    @DisplayName("deletePerson(): if deleted person was current person, then current person should be null")
    void deletePerson_CurrentPerson_Should_Be_Null() {

        Person testPerson = createTestPerson();
        model.setCurrentPerson(testPerson);

        model.deletePerson(testPerson);

        assertNull(model.getCurrentPerson(), "current person should be null");
    }

    @Test
    @DisplayName("updatePersonData() : valid input")
    void updatePersonData_Change_Should_Not_Fail() {

        Person testPerson = createTestPerson();
        String changedName = "changedName";
        Year changedYear = Year.of(2001);

        model.updatePersonData(testPerson, changedName, changedName, changedYear);

        // person should be updated
        assertEquals(testPerson.getGivenName(), changedName);
        assertEquals(testPerson.getFamilyName(), changedName);
        assertEquals(testPerson.getYearOfBirth().orElse(null), changedYear);

        // person in model.getPersons() should be updated (same object)
        Person updated = model.getPersons().stream().filter(person -> person.getGivenName().equals(changedName)).findFirst().orElse(null);
        assertEquals(testPerson, updated);
        assertSame(testPerson, updated);
    }

    @Test
    @DisplayName("updatePersonData() : person=null throws NullPointerException")
    void updatePersonData_Null_Person() {
        assertThrows(NullPointerException.class, () -> model.updatePersonData(null, "Test", null, null));
    }

    @Test
    @DisplayName("updatePersonData() : invalid input - person already exists")
    void updatePersonData_ChangeNameToExistingNameShouldFail() {

        Person person1 = createTestPerson("TestGivenName1", "testFamilyName1", Year.of(2000));
        Person person2 = createTestPerson("TestGivenName2", "testFamilyName2", Year.of(2000));

        assertFalse(model.updatePersonData(person1, person2.getGivenName(), person2.getFamilyName(), person2.getYearOfBirth().orElse(null)));
    }

    @Test
    @DisplayName("updatePersonData() : invalid input - given name and family name both are blank")
    void updatePersonData_Blank_Name() {
        Person person1 = createTestPerson();

        assertThrows(IllegalArgumentException.class, () -> model.updatePersonData(person1, "", "", null));
    }

    @Test
    void updateRelatives() {

        Person me = createTestPerson("me", "", null);
        Person parent1 = createTestPerson("parent1", "", null);
        Person parent2 = createTestPerson("parent2", "", null);
        Person spouse = createTestPerson("spouse", "", null);
        Person sibling = createTestPerson("sibling", "", null);
        Person child = createTestPerson("child", "", null);
        model.setCurrentPerson(me);

        model.updateRelatives(me, List.of(parent1, parent2), List.of(child), List.of(spouse), List.of(sibling));

        // assert parent-me kinship are updated
        assertTrue(parent1.getChildren().contains(me));
        assertTrue(me.getParents().contains(parent1));

        assertTrue(parent2.getChildren().contains(me));
        assertTrue(me.getParents().contains(parent2));

        // assert spouse-me kinship are updated
        assertTrue(spouse.getSpouses().contains(me));
        assertTrue(me.getSpouses().contains(spouse));

        // assert siblings-me kinship are updated
        assertTrue(sibling.getSiblings().contains(me));
        assertTrue(me.getSiblings().contains(sibling));

        // assert child-me kinship are updated
        assertTrue(child.getParents().contains(me));
        assertTrue(me.getChildren().contains(child));
    }

    /**
     * Creates a person with 'givenName', 'familyName', 2000.<br>
     * AssumeTrue that person != null and model.getPersons().contains(testPerson)
     *
     * @return person object to test
     */
    private Person createTestPerson() {

        Person testPerson = model.createPerson("givenName", "familyName", Year.of(2000)).orElse(null);

        assertNotNull(testPerson, "Created Person should be not null but was not.");
        assertTrue(model.getPersons().contains(testPerson), "Created person should be in models persons list, but was not.");

        return testPerson;
    }

    /**
     * Creates a person with passed arguments.<br>
     * AssumeTrue that person != null and model.getPersons().contains(testPerson)
     *
     * @return person object to test
     */
    private Person createTestPerson(String givenName, String familyName, Year yearOfBirth) {

        Person testPerson = model.createPerson(givenName, familyName, yearOfBirth).orElse(null);

        assertNotNull(testPerson, "Created Person should be not null but was not");
        assumeTrue(model.getPersons().contains(testPerson), "Created person should be in models persons list, but was not.");

        return testPerson;
    }
}