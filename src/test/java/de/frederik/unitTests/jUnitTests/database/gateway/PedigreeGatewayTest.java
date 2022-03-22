package de.frederik.unitTests.jUnitTests.database.gateway;

import de.pedigreeProject.model.Pedigree;
import de.pedigreeProject.model.Person;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PedigreeGatewayTest extends Gateway_TestCase {


    @Test
    @DisplayName("createPedigree(): test correct insertion of data")
    void createPedigree_Valid_Input() throws InterruptedException {
        Pedigree pedigree = pedigreeGateway.createPedigree("Test", "test").orElse(null);

        assertNotNull(pedigree);
        assertEquals("Test", pedigree.getTitle());
        assertEquals("test", pedigree.getDescription());
        Thread.sleep(1);
        assertTrue(pedigree.getTimeStamp().isBefore(LocalDateTime.now()));
    }

    @ParameterizedTest(name = "title is <{0}>")
    @NullAndEmptySource
    @DisplayName("createPedigree(): null or empty title should fail")
    void createPedigree_Invalid_Input_NullAndEmpty(String title) {

        assertThrows(IllegalArgumentException.class, () -> pedigreeGateway.createPedigree(title, "test"));
    }

    @Test
    @DisplayName("createPedigree(): if title already exists returns Optional.empty()")
    void createPedigree_Invalid_Input_SameTitle() {
        Pedigree pedigree = addPedigreeToDatabase();

        assertSame(Optional.empty(), pedigreeGateway.createPedigree(pedigree.getTitle(), pedigree.getDescription()));
    }

    @Test
    @DisplayName("readPedigrees(): created pedigrees should be read from database")
    void readPedigrees() {
        Pedigree pedigree1 = addPedigreeToDatabase("1");
        Pedigree pedigree2 = addPedigreeToDatabase("2");

        assertTrue(pedigreeGateway.readPedigrees().containsAll(Arrays.asList(pedigree1, pedigree2)));
    }

    @Test
    @DisplayName("updatePedigree(): valid input")
    void updatePedigree_ValidInput() {
        Pedigree pedigree = addPedigreeToDatabase();

        assertTrue(pedigreeGateway.updatePedigree(pedigree, "TestChanged", "testChanged"));

        List<Pedigree> pedigreeList = pedigreeGateway.readPedigrees();
        assertEquals(pedigree, pedigreeList.get(0));

        assertEquals("TestChanged", pedigreeList.get(0).getTitle());
        assertEquals("testChanged", pedigreeList.get(0).getDescription());
    }

    @Test
    @DisplayName("updatePedigree(): doesn't throw an IllegalArgument if title/description not changed")
    void updatePedigree_Data_Has_Not_Changed() {
        Pedigree pedigree = addPedigreeToDatabase();

        assertDoesNotThrow(() -> pedigreeGateway.updatePedigree(pedigree, pedigree.getTitle(), pedigree.getDescription()));
    }

    @ParameterizedTest(name = "title is <{0}>")
    @NullAndEmptySource
    @DisplayName("updatePedigree(): null or empty title should fail")
    void updatePedigree_InvalidInput_BlankOrNullTitle(String title) {
        Pedigree pedigree = addPedigreeToDatabase();

        assertThrows(IllegalArgumentException.class, () -> pedigreeGateway.updatePedigree(pedigree, title, "test"));
    }

    @Test
    @DisplayName("updatePedigree(): title already exists in database")
    void updatePedigree_InvalidInput_NameAlreadyExists() {
        Pedigree pedigree1 = addPedigreeToDatabase("1");
        Pedigree pedigree2 = addPedigreeToDatabase("2");

        assertFalse(pedigreeGateway.updatePedigree(pedigree1, pedigree2.getTitle(), pedigree2.getDescription()));
    }

    @Test
    @DisplayName("updatePedigree(): pedigree must not be null, throws NullPointerException")
    void updatePedigree_InvalidInput_nullPedigree() {

        assertThrows(NullPointerException.class, () -> pedigreeGateway.updatePedigree(null, "Test", "test"));
    }

    @Test
    @DisplayName("deletePedigree(): persons of pedigree should deleted")
    void deletePedigree() {
        Pedigree pedigree = addPedigreeToDatabase();
        Person person = createTestPerson();

        pedigreeGateway.deletePedigree(pedigree);

        assertTrue(pedigreeGateway.readPedigrees().isEmpty());
        assertFalse(personGateway.readPersons(pedigree).contains(person));
    }

    @NotNull
    private Pedigree addPedigreeToDatabase(String... optionalSuffix) {
        String suffix = "";
        if (optionalSuffix != null && optionalSuffix.length > 0) {
            suffix = optionalSuffix[0];
        }
        Pedigree pedigree = pedigreeGateway.createPedigree("Test" + suffix, "test").orElse(null);
        assertNotNull(pedigree);
        assertTrue(pedigreeGateway.readPedigrees().contains(pedigree));

        return pedigree;
    }
}