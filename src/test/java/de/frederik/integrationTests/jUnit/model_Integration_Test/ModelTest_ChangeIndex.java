package de.frederik.integrationTests.jUnit.model_Integration_Test;

import de.frederik.testUtils.testData.TestDatabase;
import de.pedigreeProject.model.Pedigree;
import de.pedigreeProject.model.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest_ChangeIndex extends ModelTest {

    @Test
    @DisplayName("changeIndexOfPerson():  sorted generation lists should be updated")
    void changeIndexOfPerson_updateGenerationsLists() {
        Pedigree pedigree = TestDatabase.getThreeSiblings();
        model.replacePedigree(pedigree);
        Person oldest = model.getPersons().stream().filter(person -> person.getGivenName().equals("oldest")).findFirst().orElseThrow();
        Person middle = model.getPersons().stream().filter(person -> person.getGivenName().equals("middle")).findFirst().orElseThrow();
        Person youngest = model.getPersons().stream().filter(person -> person.getGivenName().equals("youngest")).findFirst().orElseThrow();

        model.setCurrentPerson(oldest);

        // start order
        checkOrder(oldest, middle, youngest);

        // one position right ->true
        assertTrue(model.changeIndexOfPerson(oldest, 1));
        checkOrder(middle, oldest, youngest);

        // one position right ->true
        assertTrue(model.changeIndexOfPerson(oldest, 1));
        checkOrder(middle, youngest, oldest);

        // one position right ->false (out of bounds)
        assertFalse(model.changeIndexOfPerson(oldest, 1));
        checkOrder(middle, youngest, oldest);

        // one position left ->true
        assertTrue(model.changeIndexOfPerson(oldest, -1));
        checkOrder(middle, oldest, youngest);

        // two positions left -> false (out of bounds)
        assertFalse(model.changeIndexOfPerson(oldest, -2));
        checkOrder(middle, oldest, youngest);
    }

    private void checkOrder(Person oldest, Person middle, Person youngest) {
        List<Person> list = model.getGenerationsListsSorted().get(0);
        assertEquals(oldest, list.get(0));
        assertEquals(middle, list.get(1));
        assertEquals(youngest, list.get(2));
    }
}