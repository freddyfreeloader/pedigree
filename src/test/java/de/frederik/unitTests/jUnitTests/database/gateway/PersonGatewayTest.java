package de.frederik.unitTests.jUnitTests.database.gateway;

import de.frederik.testUtils.testData.TestDatabase;
import de.pedigreeProject.model.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PersonGatewayTest extends Gateway_TestCase {


    @ParameterizedTest(name = "[{index}] given name: <{0}>, family name: <{1}>, year: <{2}> ")
    @MethodSource("validPersons")
    @DisplayName("Creating person with valid arguments should pass, strings should be stripped.")
    void createPerson_ValidInput(String givenName, String familyName, Year yearOfBirth) {
        Person person = personGateway.createPerson(testPedigree, givenName, familyName, yearOfBirth).orElse(null);
        assertNotNull(person);

        assertEquals(givenName.strip(), person.getGivenName());
        assertEquals(familyName.strip(), person.getFamilyName());
        assertEquals(yearOfBirth, person.getYearOfBirth().orElse(null));
        assertTrue(person.getId() != 0);
    }

    public static Stream<Arguments> validPersons() {
        return Stream.of(
                Arguments.of("Given", "Family", Year.of(2000)),
                Arguments.of("", "Family", Year.of(2000)),
                Arguments.of("", "Family", null),
                Arguments.of("Given", "", Year.of(2000)),
                Arguments.of("Given", "", null),
                Arguments.of("  Given2    ", "   ", null)
        );
    }

    @ParameterizedTest(name = "[{index}] given name: <{0}>, family name: <{1}>, year: <{2}> ")
    @MethodSource("invalidPersons")
    @DisplayName("Creating person with invalid arguments should fail.")
    void createPerson_InvalidInput(String givenName, String familyName, Year yearOfBirth) {
        assertThrows(IllegalArgumentException.class, () -> personGateway.createPerson(testPedigree, givenName, familyName, yearOfBirth));
    }

    public static Stream<Arguments> invalidPersons() {
        return Stream.of(
                Arguments.of("", "", null),
                Arguments.of("   ", "   ", null),
                Arguments.of("", "   ", null),
                Arguments.of("   ", "", null),
                Arguments.of("", "", Year.of(2000)),
                Arguments.of(null, null, null)
        );
    }

    @Test
    @DisplayName("Creating a person with already existing given name, family name, year of birth should fail.")
    void createPerson_Double_Should_Fail() {
        Person person = createTestPerson();
        assertTrue(personGateway.createPerson(testPedigree, person.getGivenName(), person.getFamilyName(), person.getYearOfBirth().orElse(null)).isEmpty());
    }

    @Test
    @DisplayName("Creating a person with already existing given name, family name but different year of birth should pass.")
    void createPerson_Double_DifferentBirth() {
        Person person = createTestPerson();
        Year year = person.getYearOfBirth().orElse(null);
        Year changedYear = year == null ? Year.of(2000) : year.plusYears(1);

        assertTrue(personGateway.createPerson(testPedigree, person.getGivenName(), person.getFamilyName(), changedYear).isPresent());
    }

    @Test
    @DisplayName("readPersons(): created person should be read")
    void readPerson_singlePerson() {
        Person person = createTestPerson();
        // check identity
        assertNotSame(person, personGateway.readPersons(testPedigree).get(0));
        // check equals
        assertTrue(personGateway.readPersons(testPedigree).contains(person));
    }

    @Test
    @DisplayName("readPersons() : read relatives")
    void readPersons() {
        List<Person> testPersons = TestDatabase.getBaseFamily();
        List<Person> createdPersons = new ArrayList<>();
        testPersons.forEach(person -> createdPersons.add(personGateway.createPerson(testPedigree, person.getGivenName(), person.getFamilyName(), person.getYearOfBirth().orElse(null)).orElse(null)));
        List<Person> actualList = personGateway.readPersons(testPedigree);

        assertEquals(createdPersons, actualList);
    }

    @Test
    @DisplayName("readPersons(null):  should throw NullPointer")
    void readPersons_null() {

        assertThrows(NullPointerException.class, () -> personGateway.readPersons(null));
    }

    @Test
    @DisplayName("updatePersonsData():  should mutate person object")
    void updatePersonsPersonData() {
        Person person = personGateway.createPerson(testPedigree, "given", "family", Year.of(2000)).orElse(null);

        personGateway.updatePersonsData(person, "changed", "changed", Year.of(2020));

        assertEquals("changed", person.getGivenName());
        assertEquals("changed", person.getFamilyName());
        assertEquals(Year.of(2020), person.getYearOfBirth().orElse(null));
    }

    @ParameterizedTest(name = "[{index}] given name: <{0}>, family name: <{1}>, year: <{2}> ")
    @MethodSource("invalidPersons")
    @DisplayName("updatePersonsData(): invalid arguments should fail.")
    void updatePerson_InvalidInput(String givenName, String familyName, Year yearOfBirth) {
        Person person = createTestPerson();
        assertThrows(IllegalArgumentException.class, () -> personGateway.updatePersonsData(person, givenName, familyName, yearOfBirth));
    }

    @Test
    @DisplayName("updatePersonsData(): already existing record should fail.")
    void updatePersonsPersonData_Name_Already_Exists() {
        Person person = createTestPerson("1");
        Person person2 = createTestPerson("2");

        assertFalse(personGateway.updatePersonsData(person, person2.getGivenName(), person2.getFamilyName(), person2.getYearOfBirth().orElse(null)));
    }

    @Test
    @DisplayName("updateRelatives(): each relative list should be updated")
    void updateRelatives_basicTest() {
        Person person = createTestPerson("1");
        Person father = createTestPerson("2");
        Person spouse = createTestPerson("3");
        Person child = createTestPerson("4");
        Person sibling = createTestPerson("5");

        person.addParent(father);
        person.addSibling(sibling);
        person.addSpouse(spouse);
        person.addChild(child);

        personGateway.updateRelatives(person);

        List<Person> updatedPersons = personGateway.readPersons(testPedigree);

        Person personUpdated = updatedPersons.stream().filter(p -> p.getGivenName().equals(person.getGivenName())).findFirst().orElse(null);
        Person fatherUpdated = updatedPersons.stream().filter(p -> p.getGivenName().equals(father.getGivenName())).findFirst().orElse(null);
        Person spouseUpdated = updatedPersons.stream().filter(p -> p.getGivenName().equals(spouse.getGivenName())).findFirst().orElse(null);
        Person childUpdated = updatedPersons.stream().filter(p -> p.getGivenName().equals(child.getGivenName())).findFirst().orElse(null);
        Person siblingUpdated = updatedPersons.stream().filter(p -> p.getGivenName().equals(sibling.getGivenName())).findFirst().orElse(null);

        assertTrue(personUpdated.getParents().contains(fatherUpdated));
        assertTrue(personUpdated.getSpouses().contains(spouseUpdated));
        assertTrue(personUpdated.getSiblings().contains(siblingUpdated));
        assertTrue(personUpdated.getChildren().contains(childUpdated));
    }

    @Test
    @DisplayName("updateRelatives(): relatives should be updated")
    void updateRelatives() {

        List<Person> testPersons = TestDatabase.getBaseFamily();
        testPersons.forEach(person -> personGateway.createPerson(testPedigree, person.getGivenName(), person.getFamilyName(), person.getYearOfBirth().orElse(null)));

        List<Person> personsBeforeUpdate = personGateway.readPersons(testPedigree);
        Person mainPerson = personsBeforeUpdate.stream().filter(person -> person.getGivenName().equals("mainPerson")).findFirst().orElse(null);
        assertNotNull(mainPerson);

        mainPerson.getParents().clear();
        mainPerson.getSpouses().clear();
        mainPerson.getSiblings().clear();
        mainPerson.getChildren().clear();

        assertTrue(personGateway.updateRelatives(mainPerson));

        List<Person> personsAfterUpdate = personGateway.readPersons(testPedigree);

        Person meUpdated = personsAfterUpdate.stream().filter(person -> person.getGivenName().equals("mainPerson")).findFirst().orElse(null);
        assertNotNull(meUpdated);

        assertTrue(mainPerson.getParents().isEmpty());
        assertTrue(mainPerson.getSpouses().isEmpty());
        assertTrue(mainPerson.getSiblings().isEmpty());
        assertTrue(mainPerson.getChildren().isEmpty());
    }

    @Test
    @DisplayName("updateRelatives(null) should return false")
    void updateRelatives_NullPerson() {

        assertFalse(personGateway.updateRelatives(null));
    }

    @Test
    @DisplayName("deletePerson(): single person")
    void deletePerson() {
        Person person = createTestPerson();
        assertTrue(personGateway.readPersons(testPedigree).contains(person));

        personGateway.deletePerson(person);

        assertFalse(personGateway.readPersons(testPedigree).contains(person));
    }

    @Test
    @DisplayName("deletePerson(null) should return false")
    void deletePerson_NullPerson() {

        assertFalse(personGateway.deletePerson(null));
    }

    @Test
    @DisplayName("deletePerson(): should return false, if person doesn't exist in database")
    void deletePerson_PersonNotExists() {
        Person person = new Person(-1, 1, "Test", "", null);
        assertFalse(personGateway.readPersons(testPedigree).contains(person));

        assertFalse(personGateway.deletePerson(person));
    }
}