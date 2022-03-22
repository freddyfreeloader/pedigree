package de.frederik.unitTests.jUnitTests.kinship.validator;

import de.pedigreeProject.kinship.KinshipValidator;
import de.pedigreeProject.kinship.StrongKinshipValidator;
import de.pedigreeProject.model.Person;
import de.frederik.testUtils.testData.TestDatabase;
import de.frederik.testUtils.testData.csvTestData.CreateTestData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
@Disabled("to expensive")
class KinshipValidatorTest {

    static List<Person> buddenbrooks;
    static KinshipValidator kinshipValidator;

    @BeforeAll
    static void createData() {
        buddenbrooks = TestDatabase.getBuddenbrooks();
        CreateTestData.createTestDataIfNotExist();
        kinshipValidator = new StrongKinshipValidator();
    }

    @ParameterizedTest(name = "{index} Add {0} as child of {1} should create error code: {2}")
    @CsvFileSource(files = "csvTestData/couldBeAChild.csv")
    void couldBeChild(String aspirantName, String personName, boolean expected, String errorMessage) {

        Person aspirant = getPersonByName(aspirantName);
        Person person = getPersonByName(personName);
        assumeTrue(aspirant != null && person != null);

        assertEquals(expected, kinshipValidator.aspirantCouldBeChildOfPerson(aspirant, person).isPresent(), errorMessage);
    }

    @ParameterizedTest(name = "{index} Add {0} as parent of {1} should create error code: {2}")
    @CsvFileSource(files = "csvTestData/couldBeAParent.csv")
    void couldBeParent(String aspirantName, String personName, boolean expected, String errorMessage) {

        Person aspirant = getPersonByName(aspirantName);
        Person person = getPersonByName(personName);
        assumeTrue(aspirant != null && person != null);

        assertEquals(expected, kinshipValidator.aspirantCouldBeParentOfPerson(aspirant, person).isPresent(), errorMessage);
    }

    @ParameterizedTest(name = "{index} Add {0} as sibling of {1} should create error code: {2}")
    @CsvFileSource(files = "csvTestData/couldBeASibling.csv")
    void couldBeSibling(String aspirantName, String personName, boolean expected, String errorMessage) {

        Person aspirant = getPersonByName(aspirantName);
        Person person = getPersonByName(personName);
        assumeTrue(aspirant != null && person != null);

        assertEquals(expected, kinshipValidator.aspirantCouldBeSiblingOfPerson(aspirant, person).isPresent(), errorMessage);
    }

    @ParameterizedTest(name = "{index} Add {0} as spouse of {1} should create error code: {2}")
    @CsvFileSource(files = "csvTestData/couldBeASpouse.csv")
    void couldBeSpouse(String aspirantName, String personName, boolean expected, String errorMessage) {

        Person aspirant = getPersonByName(aspirantName);
        Person person = getPersonByName(personName);
        assumeTrue(aspirant != null && person != null);

        assertEquals(expected, kinshipValidator.aspirantCouldBeSpouseOfPerson(aspirant, person).isPresent(), errorMessage);
    }

    private Person getPersonByName(String fullName) {
        return buddenbrooks.stream().filter(person1 -> person1.toString().equals(fullName)).findFirst().orElse(null);
    }
}