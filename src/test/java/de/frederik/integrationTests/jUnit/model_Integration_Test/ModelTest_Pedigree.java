package de.frederik.integrationTests.jUnit.model_Integration_Test;

import de.pedigreeProject.model.Pedigree;
import de.pedigreeProject.model.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static de.frederik.testUtils.ConstantsForTesting.CHANGED_DESCRIPTION;
import static de.frederik.testUtils.ConstantsForTesting.FIRST_TEST_PEDIGREE;
import static org.junit.jupiter.api.Assertions.*;

class ModelTest_Pedigree extends ModelTest {

    @Test
    @DisplayName("createNewPedigree() : valid input, tests all lists")
    void createNewPedigree_ValidInput() {
        Pedigree pedigree = model.createNewPedigree(FIRST_TEST_PEDIGREE, CHANGED_DESCRIPTION).orElse(null);

        assertNotNull(pedigree);

        assertEquals(pedigree.getTitle(), FIRST_TEST_PEDIGREE);
        assertEquals(pedigree.getDescription(), CHANGED_DESCRIPTION);
        assertTrue(pedigree.getId() != 0);

        assertTrue(model.getPedigrees().contains(pedigree));
        assertSame(model.getCurrentPedigree(), pedigree);
        assertTrue(model.getPersons().isEmpty());
        assertTrue(model.getGenerationsListsSorted().isEmpty());
        assertTrue(pedigreeGateway.readPedigrees().contains(pedigree));
    }

    @ParameterizedTest(name = ("[{index}] createNewPedigree(< {0} >) should throw IllegalArgumentException"))
    @NullAndEmptySource
    @DisplayName("createNewPedigree(): invalid input")
    void createNewPedigree_InvalidInput(String title) {

        assertThrows(IllegalArgumentException.class, () -> model.createNewPedigree(title, ""));
    }

    @Test
    @DisplayName("createNewPedigree() with already existing titel should return Optional.empty()")
    void createNewPedigree_Title_already_Exists() {
        Pedigree pedigree = createTestPedigree();
        assertTrue(model.createNewPedigree(pedigree.getTitle(), "").isEmpty());
    }

    @Test
    @DisplayName("createNewPedigree(): new pedigree should be current pedigree ")
    void createNewPedigree_currentPedigree() {
        Pedigree pedigree1 = createTestPedigree("1");
        assertSame(pedigree1, model.getCurrentPedigree());

        Pedigree pedigree2 = createTestPedigree("2");
        assertSame(pedigree2, model.getCurrentPedigree());

        Pedigree pedigree3 = createTestPedigree("3");
        assertSame(pedigree3, model.getCurrentPedigree());
    }

    @Test
    @DisplayName("createNewPedigree(): time stamp is consecutively ")
    void createNewPedigree_consecutively() {
        Pedigree pedigree1 = createTestPedigree("1");
        Pedigree pedigree2 = createTestPedigree("2");
        Pedigree pedigree3 = createTestPedigree("3");
        Pedigree pedigree4 = createTestPedigree("4");
        Pedigree pedigree5 = createTestPedigree("5");
        Pedigree pedigree6 = createTestPedigree("6");

        assertTimeStampIsConsecutively(pedigree1, pedigree2, pedigree3, pedigree4, pedigree5, pedigree6);
    }

    @Test
    @DisplayName("updatePedigreeTitleAndDescription() : valid input")
    void updatePedigreeTitleAndDescription_ValidInput() {
        Pedigree pedigree = createTestPedigree();

        model.updatePedigreeTitleAndDescription(FIRST_TEST_PEDIGREE, CHANGED_DESCRIPTION);

        Pedigree currentPedigree = model.getCurrentPedigree();
        assertSame(pedigree, currentPedigree);
        assertEquals(FIRST_TEST_PEDIGREE, currentPedigree.getTitle());
        assertEquals(CHANGED_DESCRIPTION, currentPedigree.getDescription());
    }

    @Test
    @DisplayName("updatePedigreeTitleAndDescription() : change only description")
    void updatePedigreeTitleAndDescription_changeDescription() {
        Pedigree pedigree = createTestPedigree();
        String unchangedTitle = pedigree.getTitle();

        model.updatePedigreeTitleAndDescription(unchangedTitle, CHANGED_DESCRIPTION);

        Pedigree currentPedigree = model.getCurrentPedigree();
        assertSame(pedigree, currentPedigree);
        assertEquals(unchangedTitle, currentPedigree.getTitle());
        assertEquals(CHANGED_DESCRIPTION, currentPedigree.getDescription());
    }

    @Test
    @DisplayName("updatePedigreeTitleAndDescription() : empty title should throw IllegalArgumentException, pedigree should not have changed")
    void updatePedigreeTitleAndDescription_InvalidInput() {
        Pedigree pedigree = createTestPedigree();
        String oldTitle = pedigree.getTitle();
        String oldDescription = pedigree.getDescription();

        assertThrows(IllegalArgumentException.class, () -> model.updatePedigreeTitleAndDescription("", CHANGED_DESCRIPTION));
        Pedigree currentPedigree = model.getCurrentPedigree();
        assertEquals(pedigree, currentPedigree);
        assertEquals(oldTitle, currentPedigree.getTitle());
        assertEquals(oldDescription, currentPedigree.getDescription());
    }

    @Test
    @DisplayName("replacePedigree() : currentPedigree should be updated")
    void replacePedigree() {
        Pedigree firstPedigree = createTestPedigree("1");
        Pedigree secondPedigree = createTestPedigree("2");

        model.replacePedigree(firstPedigree);
        assertSame(model.getCurrentPedigree(), firstPedigree);
        model.replacePedigree(secondPedigree);
        assertSame(model.getCurrentPedigree(), secondPedigree);
    }

    @Test
    @DisplayName("replacePedigree() : persons list should be updated")
    void replacePedigree_Check_If_Person_Lists_Are_Updated() {
        Pedigree pedigree1 = createTestPedigree("1");
        Person person1 = createTestPerson("1");

        Pedigree pedigree2 = createTestPedigree("2");
        Person person2 = createTestPerson("2");

        model.replacePedigree(pedigree1);
        assertEquals(List.of(person1), model.getPersons());

        model.replacePedigree(pedigree2);
        assertEquals(List.of(person2), model.getPersons());
    }

    @Test
    @DisplayName("current pedigree: if current pedigree changed, persons list should change too")
    void changeCurrentPedigree_personsList() {
        Pedigree pedigree1 = createTestPedigree("1");
        Person person1 = createTestPerson("1");
        Person person2 = createTestPerson("2");
        Person person3 = createTestPerson("3");

        assertEquals(pedigree1, model.getCurrentPedigree());
        assertEquals(List.of(person1, person2, person3), model.getPersons());
        assertEquals(List.of(person1, person2, person3), personGateway.readPersons(pedigree1));

        Pedigree pedigree2 = createTestPedigree("2");
        Person person4 = createTestPerson("4");
        Person person5 = createTestPerson("5");
        Person person6 = createTestPerson("6");

        assertEquals(pedigree2, model.getCurrentPedigree());
        assertEquals(List.of(person4, person5, person6), model.getPersons());
        assertEquals(List.of(person1, person2, person3), personGateway.readPersons(pedigree1));
        assertEquals(List.of(person4, person5, person6), personGateway.readPersons(pedigree2));
    }

    @Test
    @DisplayName("deletePedigree(): pedigrees should be deleted from pedigree list and database")
    void deletePedigree_syncListAndDatabase() {
        Pedigree pedigree1 = createTestPedigree("1");
        Pedigree pedigree2 = createTestPedigree("2");
        Pedigree defaultPedigree = getDefaultPedigree();

        assertEquals(List.of(defaultPedigree, pedigree1, pedigree2), model.getPedigrees());
        assertEquals(List.of(defaultPedigree, pedigree1, pedigree2), pedigreeGateway.readPedigrees());

        model.deletePedigree();
        assertEquals(List.of(defaultPedigree, pedigree1), model.getPedigrees());
        assertEquals(List.of(defaultPedigree, pedigree1), pedigreeGateway.readPedigrees());

        model.deletePedigree();
        assertEquals(List.of(defaultPedigree), model.getPedigrees());
        assertEquals(List.of(defaultPedigree), pedigreeGateway.readPedigrees());
    }

    @Test
    @DisplayName("deletePedigree(): last updated/created pedigree should be new current pedigree")
    void deletePedigree_testTimeStamp() {

        Pedigree pedigree1 = createTestPedigree("1");
        Pedigree pedigree2 = createTestPedigree("2");
        Pedigree pedigree3 = createTestPedigree("3");
        Pedigree pedigree4 = createTestPedigree("4");

        Pedigree defaultPedigree = getDefaultPedigree();
        assertTimeStampIsConsecutively(defaultPedigree, pedigree1, pedigree2, pedigree3, pedigree4);

        // delete current pedigree4
        model.deletePedigree();
        assertEquals(pedigree3, model.getCurrentPedigree());
        // delete current pedigree3
        model.deletePedigree();
        assertEquals(pedigree2, model.getCurrentPedigree());
        // delete current pedigree2
        model.deletePedigree();
        assertEquals(pedigree1, model.getCurrentPedigree());
        // delete current pedigree1
        model.deletePedigree();
        assertEquals(defaultPedigree, model.getCurrentPedigree());
    }

    @Test
    @DisplayName("deletePedigree(): default pedigree should be created, if last user pedigree is deleted")
    void deletePedigree_defaultPedigree() {
        Pedigree userPedigree1 = createTestPedigree("1");
        Pedigree defaultPedigree = getDefaultPedigree();

        modelAndDatabaseHaveOnlyPedigrees(defaultPedigree, userPedigree1);

        // first delete old default pedigree:
        model.setCurrentPedigree(defaultPedigree);
        model.deletePedigree();
        modelAndDatabaseHaveOnlyPedigrees(userPedigree1);

        // second delete user pedigree
        model.deletePedigree();
        Pedigree newDefaultPedigree = getDefaultPedigree();
        modelAndDatabaseHaveOnlyPedigrees(newDefaultPedigree);
        assertEquals(newDefaultPedigree, model.getCurrentPedigree());
    }

    @Test
    @DisplayName("deletePedigree() : persons of same pedigree should be deleted ")
    void deletePedigree_PersonsShouldBeDeleted() {
        Pedigree pedigree = createTestPedigree();
        Person person1 = createTestPerson("1");
        Person person2 = createTestPerson("2");
        Person person3 = createTestPerson("3");

        assertTrue(model.getPedigrees().contains(pedigree));
        assertTrue(model.getPersons().containsAll(Arrays.asList(person1, person2, person3)));
        assertTrue(pedigreeGateway.readPedigrees().contains(pedigree));

        model.deletePedigree();

        assertFalse(model.getPedigrees().contains(pedigree));
        assertFalse(model.getPersons().containsAll(Arrays.asList(person1, person2, person3)));
        assertFalse(pedigreeGateway.readPedigrees().contains(pedigree));
    }

    @Test
    @DisplayName("deletePedigree() : persons of different pedigree should not be deleted ")
    void deletePedigree_PersonsShouldNotBeDeleted() {
        Pedigree pedigree1 = createTestPedigree("1");
        Person person1 = createTestPerson("1");
        Person person2 = createTestPerson("2");
        Person person3 = createTestPerson("3");

        assertEquals(List.of(person1, person2, person3), model.getPersons());
        assertEquals(List.of(person1, person2, person3), personGateway.readPersons(pedigree1));

        Pedigree pedigree2 = createTestPedigree("2");
        Person person4 = createTestPerson("4");
        Person person5 = createTestPerson("5");
        Person person6 = createTestPerson("6");

        assertEquals(List.of(person4, person5, person6), model.getPersons());
        assertEquals(List.of(person4, person5, person6), personGateway.readPersons(pedigree2));

        model.deletePedigree();

        // assert pedigree2 and its persons 4, 5, 6 are deleted from database...
        assertEquals(List.of(), personGateway.readPersons(pedigree2));

        // ...but pedigree1 and its persons 1, 2, 3 still exist
        assertEquals(List.of(person1, person2, person3), personGateway.readPersons(pedigree1));
        assertEquals(List.of(person1, person2, person3), model.getPersons());

        // delete remaining persons
        model.deletePedigree();
        assertEquals(List.of(), model.getPersons());
        assertEquals(List.of(), personGateway.readPersons(pedigree1));
    }

    private void modelAndDatabaseHaveOnlyPedigrees(Pedigree... pedigrees) {
        assertEquals(List.of(pedigrees), model.getPedigrees());
        assertEquals(List.of(pedigrees), pedigreeGateway.readPedigrees());
    }

    private void assertTimeStampIsConsecutively(Pedigree... pedigrees) {
        for (int i = 0; i < pedigrees.length - 1; i++) {
            LocalDateTime first = pedigrees[i].getTimeStamp();
            LocalDateTime second = pedigrees[i + 1].getTimeStamp();
            assertFalse(first.isAfter(second) || first.equals(second), "first time should be before second. first: " + first + " second: " + second);
        }
    }

    private Pedigree getDefaultPedigree() {
        String defaultPedigreeTitle = ResourceBundle.getBundle("pedigree").getString("default.pedigree.title");
        Pedigree defaultPedigree = model.getPedigrees().stream().filter(pedigree -> pedigree.getTitle().contains(defaultPedigreeTitle)).findAny().orElse(null);
        assertNotNull(defaultPedigree, "default pedigree should not be null");
        return defaultPedigree;
    }

    private Person createTestPerson(String... suffix) {
        Person testPerson = model.createPerson("TestPerson" + Arrays.toString(suffix), "", null).orElse(null);
        assertNotNull(testPerson);
        return testPerson;
    }

    /**
     * Creates a pedigree with title "TestPedigree" + optional suffix,  and description = "this is a test pedigree".<br>
     * Tests AssumesTrue(pedigree!=null)
     *
     * @param suffix a String to extend title of pedigree
     * @return a test pedigree
     */
    private Pedigree createTestPedigree(String... suffix) {
        String suffixString = "";
        if (suffix != null && suffix.length > 0) {

            suffixString = suffix[0];
        }
        String title = "TestPedigree" + suffixString;
        String description = "this is a test pedigree";
        Pedigree pedigree = model.createNewPedigree(title, description).orElse(null);
        assertNotNull(pedigree, "created pedigree should not be null");
        return pedigree;
    }
}