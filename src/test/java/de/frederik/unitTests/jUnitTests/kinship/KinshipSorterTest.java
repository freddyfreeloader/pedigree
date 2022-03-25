package de.frederik.unitTests.jUnitTests.kinship;

import de.frederik.testUtils.testData.TestDatabase;
import de.pedigreeProject.kinship.KinshipSorter;
import de.pedigreeProject.kinship.KinshipSorterImpl;
import de.pedigreeProject.model.Person;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static de.frederik.testUtils.testData.BuddenbrooksData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class KinshipSorterTest {
    static List<Person> buddenbrooks = new ArrayList<>();


    @BeforeAll
    static void createData() {
        buddenbrooks = TestDatabase.getBuddenbrooks();
    }

    /**
     * Pedigree of Buddenbrooks:
     *
     * @see <a href="http://buddenbrookhaus.de/file/stb_fam_buddenbrook.pdf">http://buddenbrookhaus.de</a>
     */
    @Test
    @DisplayName("Sorter: test sort order with Buddenbrooks pedigree from oldest person")
    void getSortedList_BuddenbrooksFromTop() {

        List<List<Person>> expectedList = getExpectedOrderedList(JOHAN.toString());
        Map<Person, Integer> personIntegerMap = getPersonGeneration(JOHAN.toString());
        KinshipSorter kinshipSorter = new KinshipSorterImpl();

        assertEquals(expectedList, kinshipSorter.getSortedLists(personIntegerMap));
    }

    /**
     * Pedigree of Buddenbrooks:
     *
     * @see <a href="http://buddenbrookhaus.de/file/stb_fam_buddenbrook.pdf">http://buddenbrookhaus.de</a>
     */
    @Test
    @DisplayName("Sorter: test sort order with Buddenbrooks pedigree from youngest person")
    void getSortedList_BuddenbrooksFromBottom() {

        List<List<Person>> expectedList = getExpectedOrderedList(ELISABETH_WEINSCHENK.toString());
        Map<Person, Integer> personIntegerMap = getPersonGeneration(ELISABETH_WEINSCHENK.toString());
        KinshipSorter kinshipSorter = new KinshipSorterImpl();

        assertEquals(expectedList, kinshipSorter.getSortedLists(personIntegerMap));
    }

    @Test
    @DisplayName("Sorter: test sort order with Buddenbrooks pedigree from middle")
    void getSortedList_BuddenbrooksFromMiddle() {

        List<List<Person>> expectedList = getExpectedOrderedList(ANTONIE.toString());
        Map<Person, Integer> personIntegerMap = getPersonGeneration(ANTONIE.toString());
        KinshipSorter kinshipSorter = new KinshipSorterImpl();

        assertEquals(expectedList, kinshipSorter.getSortedLists(personIntegerMap));
    }

    private Map<Person, Integer> getPersonGeneration(String fullName) {

        Map<Person, Integer> map = new HashMap<>();

        if (fullName.equals(ANTONIE.toString())) {

            map.put(getPersonByFullName(JOHAN.toString()), -3);
            map.put(getPersonByFullName(JOHANN.toString()), -2);
            map.put(getPersonByFullName(ELISABETH_WEINSCHENK.toString()), 2);
            map.put(getPersonByFullName(CATHARINA.toString()), -2);
            map.put(getPersonByFullName(BERNHARD.toString()), -2);
            map.put(getPersonByFullName(ANTOINETTE.toString()), -2);
            map.put(getPersonByFullName(LEBERECHT.toString()), -2);
            map.put(getPersonByFullName(OLLY.toString()), -1);
            map.put(getPersonByFullName(GOTTHOLD.toString()), -1);
            map.put(getPersonByFullName(JUSTUS.toString()), -1);
            map.put(getPersonByFullName(KLOTHILDE.toString()), -1);
            map.put(getPersonByFullName(ELISABETH_KROEGER.toString()), -1);
            map.put(getPersonByFullName(GISELA.toString()), 1);
            map.put(getPersonByFullName(JOHANN_JEAN.toString()), -1);
            map.put(getPersonByFullName(JAKOB.toString()), 0);
            map.put(getPersonByFullName(FREDERIKE.toString()), 0);
            map.put(getPersonByFullName(CHRISTIAN.toString()), 0);
            map.put(getPersonByFullName(PFIFFI.toString()), 0);
            map.put(getPersonByFullName(ANTONIE.toString()), 0);
            map.put(getPersonByFullName(JUERGEN.toString()), 0);
            map.put(getPersonByFullName(HENRIETTE.toString()), 0);
            map.put(getPersonByFullName(THOMAS.toString()), 0);
            map.put(getPersonByFullName(CLARA.toString()), 0);
            map.put(getPersonByFullName(HANNO.toString()), 1);
            map.put(getPersonByFullName(ERIKA.toString()), 1);
        }
        if (fullName.equals(ELISABETH_WEINSCHENK.toString())) {

            map.put(getPersonByFullName(JOHAN.toString()), -5);
            map.put(getPersonByFullName(JOHANN.toString()), -4);
            map.put(getPersonByFullName(CATHARINA.toString()), -4);
            map.put(getPersonByFullName(BERNHARD.toString()), -4);
            map.put(getPersonByFullName(LEBERECHT.toString()), -4);
            map.put(getPersonByFullName(ANTOINETTE.toString()), -4);
            map.put(getPersonByFullName(OLLY.toString()), -3);
            map.put(getPersonByFullName(GOTTHOLD.toString()), -3);
            map.put(getPersonByFullName(JUSTUS.toString()), -3);
            map.put(getPersonByFullName(KLOTHILDE.toString()), -3);
            map.put(getPersonByFullName(ELISABETH_KROEGER.toString()), -3);
            map.put(getPersonByFullName(JOHANN_JEAN.toString()), -3);
            map.put(getPersonByFullName(BENDIX.toString()), -2);
            map.put(getPersonByFullName(FREDERIKE.toString()), -2);
            map.put(getPersonByFullName(CHRISTIAN.toString()), -2);
            map.put(getPersonByFullName(ANTONIE.toString()), -2);
            map.put(getPersonByFullName(JAKOB.toString()), -2);
            map.put(getPersonByFullName(JUERGEN.toString()), -2);
            map.put(getPersonByFullName(HENRIETTE.toString()), -2);
            map.put(getPersonByFullName(THOMAS.toString()), -2);
            map.put(getPersonByFullName(CLARA.toString()), -2);
            map.put(getPersonByFullName(PFIFFI.toString()), -2);
            map.put(getPersonByFullName(ERIKA.toString()), -1);
            map.put(getPersonByFullName(HANNO.toString()), -1);
            map.put(getPersonByFullName(GISELA.toString()), -1);
            map.put(getPersonByFullName(HUGO.toString()), -1);
            map.put(getPersonByFullName(ELISABETH_WEINSCHENK.toString()), 0);
        }
        if (fullName.equals(JOHAN.toString())) {

            map.put(getPersonByFullName(JOHAN.toString()), 0);
            map.put(getPersonByFullName(JOHANN.toString()), 1);
            map.put(getPersonByFullName(BERNHARD.toString()), 1);
            map.put(getPersonByFullName(OLLY.toString()), 2);
            map.put(getPersonByFullName(GOTTHOLD.toString()), 2);
            map.put(getPersonByFullName(KLOTHILDE.toString()), 2);
            map.put(getPersonByFullName(JOHANN_JEAN.toString()), 2);
            map.put(getPersonByFullName(CLARA.toString()), 3);
            map.put(getPersonByFullName(FREDERIKE.toString()), 3);
            map.put(getPersonByFullName(CHRISTIAN.toString()), 3);
            map.put(getPersonByFullName(ANTONIE.toString()), 3);
            map.put(getPersonByFullName(HENRIETTE.toString()), 3);
            map.put(getPersonByFullName(THOMAS.toString()), 3);
            map.put(getPersonByFullName(PFIFFI.toString()), 3);
            map.put(getPersonByFullName(ERIKA.toString()), 4);
            map.put(getPersonByFullName(GISELA.toString()), 4);
            map.put(getPersonByFullName(HANNO.toString()), 4);
            map.put(getPersonByFullName(ELISABETH_WEINSCHENK.toString()), 5);
        }
        //  HashMap has no natural order, but if the map are not changed
        //  the result of reading the map is nearly every time in the same order.
        // Hence, to prevent an order of persons shuffle map.
        List<Person> list = new ArrayList<>(map.keySet());
        Collections.shuffle(list);

        Map<Person, Integer> shuffleMap = new HashMap<>();
        list.forEach(k -> shuffleMap.put(k, map.get(k)));
        return shuffleMap;
    }

    private List<List<Person>> getExpectedOrderedList(String person) {
        List<List<Person>> sortedList = new ArrayList<>();
        if (person.equals(JOHAN.toString())) {

            List<Person> generation0 = new ArrayList<>();
            generation0.add(getPersonByFullName(JOHAN.toString()));
            sortedList.add(generation0);

            List<Person> generation1 = new ArrayList<>();
            generation1.add(getPersonByFullName(JOHANN.toString()));
            generation1.add(getPersonByFullName(BERNHARD.toString()));
            sortedList.add(generation1);

            List<Person> generation2 = new ArrayList<>();
            generation2.add(getPersonByFullName(GOTTHOLD.toString()));
            generation2.add(getPersonByFullName(JOHANN_JEAN.toString()));
            generation2.add(getPersonByFullName(OLLY.toString()));
            generation2.add(getPersonByFullName(KLOTHILDE.toString()));
            sortedList.add(generation2);

            List<Person> generation3 = new ArrayList<>();
            generation3.add(getPersonByFullName(FREDERIKE.toString()));
            generation3.add(getPersonByFullName(HENRIETTE.toString()));
            generation3.add(getPersonByFullName(PFIFFI.toString()));
            generation3.add(getPersonByFullName(THOMAS.toString()));
            generation3.add(getPersonByFullName(ANTONIE.toString()));
            generation3.add(getPersonByFullName(CHRISTIAN.toString()));
            generation3.add(getPersonByFullName(CLARA.toString()));
            sortedList.add(generation3);

            List<Person> generation4 = new ArrayList<>();
            generation4.add(getPersonByFullName(HANNO.toString()));
            generation4.add(getPersonByFullName(ERIKA.toString()));
            generation4.add(getPersonByFullName(GISELA.toString()));
            sortedList.add(generation4);

            List<Person> generation5 = new ArrayList<>();
            generation5.add(getPersonByFullName(ELISABETH_WEINSCHENK.toString()));
            sortedList.add(generation5);
        }

        if (person.equals(ELISABETH_WEINSCHENK.toString())) {

            List<Person> generation0 = new ArrayList<>();
            generation0.add(getPersonByFullName(JOHAN.toString()));
            sortedList.add(generation0);

            List<Person> generation1 = new ArrayList<>();
            generation1.add(getPersonByFullName(JOHANN.toString()));
            generation1.add(getPersonByFullName(ANTOINETTE.toString()));
            generation1.add(getPersonByFullName(BERNHARD.toString()));
            generation1.add(getPersonByFullName(LEBERECHT.toString()));
            generation1.add(getPersonByFullName(CATHARINA.toString()));
            sortedList.add(generation1);

            List<Person> generation2 = new ArrayList<>();
            generation2.add(getPersonByFullName(GOTTHOLD.toString()));
            generation2.add(getPersonByFullName(JOHANN_JEAN.toString()));
            generation2.add(getPersonByFullName(ELISABETH_KROEGER.toString()));
            generation2.add(getPersonByFullName(OLLY.toString()));
            generation2.add(getPersonByFullName(KLOTHILDE.toString()));
            generation2.add(getPersonByFullName(JUSTUS.toString()));
            sortedList.add(generation2);

            List<Person> generation3 = new ArrayList<>();
            generation3.add(getPersonByFullName(FREDERIKE.toString()));
            generation3.add(getPersonByFullName(HENRIETTE.toString()));
            generation3.add(getPersonByFullName(PFIFFI.toString()));
            generation3.add(getPersonByFullName(THOMAS.toString()));
            generation3.add(getPersonByFullName(ANTONIE.toString()));
            generation3.add(getPersonByFullName(BENDIX.toString()));
            generation3.add(getPersonByFullName(CHRISTIAN.toString()));
            generation3.add(getPersonByFullName(CLARA.toString()));
            generation3.add(getPersonByFullName(JUERGEN.toString()));
            generation3.add(getPersonByFullName(JAKOB.toString()));
            sortedList.add(generation3);

            List<Person> generation4 = new ArrayList<>();
            generation4.add(getPersonByFullName(HANNO.toString()));
            generation4.add(getPersonByFullName(ERIKA.toString()));
            generation4.add(getPersonByFullName(HUGO.toString()));
            generation4.add(getPersonByFullName(GISELA.toString()));
            sortedList.add(generation4);

            List<Person> generation5 = new ArrayList<>();
            generation5.add(getPersonByFullName(ELISABETH_WEINSCHENK.toString()));
            sortedList.add(generation5);
        }
        if (person.equals(ANTONIE.toString())) {

            List<Person> generation0 = new ArrayList<>();
            generation0.add(getPersonByFullName(JOHAN.toString()));
            sortedList.add(generation0);

            List<Person> generation1 = new ArrayList<>();
            generation1.add(getPersonByFullName(JOHANN.toString()));
            generation1.add(getPersonByFullName(ANTOINETTE.toString()));
            generation1.add(getPersonByFullName(BERNHARD.toString()));
            generation1.add(getPersonByFullName(LEBERECHT.toString()));
            generation1.add(getPersonByFullName(CATHARINA.toString()));
            sortedList.add(generation1);

            List<Person> generation2 = new ArrayList<>();
            generation2.add(getPersonByFullName(GOTTHOLD.toString()));
            generation2.add(getPersonByFullName(JOHANN_JEAN.toString()));
            generation2.add(getPersonByFullName(ELISABETH_KROEGER.toString()));
            generation2.add(getPersonByFullName(OLLY.toString()));
            generation2.add(getPersonByFullName(KLOTHILDE.toString()));
            generation2.add(getPersonByFullName(JUSTUS.toString()));
            sortedList.add(generation2);

            List<Person> generation3 = new ArrayList<>();
            generation3.add(getPersonByFullName(FREDERIKE.toString()));
            generation3.add(getPersonByFullName(HENRIETTE.toString()));
            generation3.add(getPersonByFullName(PFIFFI.toString()));
            generation3.add(getPersonByFullName(THOMAS.toString()));
            generation3.add(getPersonByFullName(ANTONIE.toString()));
            generation3.add(getPersonByFullName(CHRISTIAN.toString()));
            generation3.add(getPersonByFullName(CLARA.toString()));
            generation3.add(getPersonByFullName(JUERGEN.toString()));
            generation3.add(getPersonByFullName(JAKOB.toString()));
            sortedList.add(generation3);

            List<Person> generation4 = new ArrayList<>();
            generation4.add(getPersonByFullName(HANNO.toString()));
            generation4.add(getPersonByFullName(ERIKA.toString()));
            generation4.add(getPersonByFullName(GISELA.toString()));
            sortedList.add(generation4);

            List<Person> generation5 = new ArrayList<>();
            generation5.add(getPersonByFullName(ELISABETH_WEINSCHENK.toString()));
            sortedList.add(generation5);
        }
        return sortedList;
    }

    private @Nullable Person getPersonByFullName(String fullName) {
        try {
            return buddenbrooks.stream()
                    .filter(person -> person.toString().equals(fullName))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(fullName + " is not provided!"));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}