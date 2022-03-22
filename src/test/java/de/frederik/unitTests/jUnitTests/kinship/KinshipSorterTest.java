package de.frederik.unitTests.jUnitTests.kinship;

import de.pedigreeProject.kinship.KinshipSorter;
import de.pedigreeProject.kinship.KinshipSorterImpl;
import de.pedigreeProject.model.Person;
import de.frederik.testUtils.testData.TestDatabase;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KinshipSorterTest {
    static List<Person> buddenbrooks = new ArrayList<>();


    @BeforeAll
    static void createData() {
        buddenbrooks = TestDatabase.getBuddenbrooks();
    }

    /**
     *   Pedigree of Buddenbrooks:
     * @see <a href="http://buddenbrookhaus.de/file/stb_fam_buddenbrook.pdf">http://buddenbrookhaus.de</a>
     */
    @Test
    @DisplayName("Sorter: test sort order with Buddenbrooks pedigree from oldest person")
    void getSortedList_BuddenbrooksFromTop() {

        List<List<Person>> expectedList = getExpectedOrderedList("Johan Buddenbrook");
        Map<Person, Integer> personIntegerMap = getPersonGeneration("Johan Buddenbrook");
        KinshipSorter kinshipSorter = new KinshipSorterImpl();

        assertEquals(expectedList, kinshipSorter.getSortedLists(personIntegerMap));
    }

    /**
     *   Pedigree of Buddenbrooks:
     * @see <a href="http://buddenbrookhaus.de/file/stb_fam_buddenbrook.pdf">http://buddenbrookhaus.de</a>
     */
    @Test
    @DisplayName("Sorter: test sort order with Buddenbrooks pedigree from youngest person")
    void getSortedList_BuddenbrooksFromBottom() {

        List<List<Person>> expectedList = getExpectedOrderedList("Elisabeth Weinschenk");
        Map<Person, Integer> personIntegerMap = getPersonGeneration("Elisabeth Weinschenk");
        KinshipSorter kinshipSorter = new KinshipSorterImpl();

        assertEquals(expectedList, kinshipSorter.getSortedLists(personIntegerMap));
    }

    @Test
    @DisplayName("Sorter: test sort order with Buddenbrooks pedigree from middle")
    void getSortedList_BuddenbrooksFromMiddle() {

        List<List<Person>> expectedList = getExpectedOrderedList("Antonie (Tony) Buddenbrook");
        Map<Person, Integer> personIntegerMap = getPersonGeneration("Antonie (Tony) Buddenbrook");
        KinshipSorter kinshipSorter = new KinshipSorterImpl();

        assertEquals(expectedList, kinshipSorter.getSortedLists(personIntegerMap));
    }

    private Map<Person, Integer> getPersonGeneration(String fullName) {

        Map<Person, Integer> map = new HashMap<>();

        if (fullName.equals("Antonie (Tony) Buddenbrook")) {

            map.put(getPersonByFullName("Johan Buddenbrook"), -3);
            map.put(getPersonByFullName("Johann Buddenbrook"), -2);
            map.put(getPersonByFullName("Elisabeth Weinschenk"), 2);
            map.put(getPersonByFullName("Catharina Kroeger"), -2);
            map.put(getPersonByFullName("Bernhard Buddenbrook"), -2);
            map.put(getPersonByFullName("Antoinette Duchamps"), -2);
            map.put(getPersonByFullName("Leberecht Kroeger"), -2);
            map.put(getPersonByFullName("Olly Buddenbrook"), -1);
            map.put(getPersonByFullName("Gotthold Buddenbrook"), -1);
            map.put(getPersonByFullName("Justus Kroeger"), -1);
            map.put(getPersonByFullName("Klothilde Buddenbrook"), -1);
            map.put(getPersonByFullName("Elisabeth (Bethsy) Kroeger"), -1);
            map.put(getPersonByFullName("Gisela Buddenbrook"), 1);
            map.put(getPersonByFullName("Johann (Jean) Buddenbrook"), -1);
            map.put(getPersonByFullName("Jakob Kroeger"), 0);
            map.put(getPersonByFullName("Frederike Buddenbrook"), 0);
            map.put(getPersonByFullName("Christian Buddenbrook"), 0);
            map.put(getPersonByFullName("Pfiffi Buddenbrook"), 0);
            map.put(getPersonByFullName("Antonie (Tony) Buddenbrook"), 0);
            map.put(getPersonByFullName("Juergen Kroeger"), 0);
            map.put(getPersonByFullName("Henriette Buddenbrook"), 0);
            map.put(getPersonByFullName("Thomas Buddenbrook"), 0);
            map.put(getPersonByFullName("Clara Buddenbrook"), 0);
            map.put(getPersonByFullName("Hanno Buddenbrook"), 1);
            map.put(getPersonByFullName("Erika Gruenlich"), 1);
        }
        if (fullName.equals("Elisabeth Weinschenk")) {

            map.put(getPersonByFullName("Johan Buddenbrook"), -5);
            map.put(getPersonByFullName("Johann Buddenbrook"), -4);
            map.put(getPersonByFullName("Catharina Kroeger"), -4);
            map.put(getPersonByFullName("Bernhard Buddenbrook"), -4);
            map.put(getPersonByFullName("Leberecht Kroeger"), -4);
            map.put(getPersonByFullName("Antoinette Duchamps"), -4);
            map.put(getPersonByFullName("Olly Buddenbrook"), -3);
            map.put(getPersonByFullName("Gotthold Buddenbrook"), -3);
            map.put(getPersonByFullName("Justus Kroeger"), -3);
            map.put(getPersonByFullName("Klothilde Buddenbrook"), -3);
            map.put(getPersonByFullName("Elisabeth (Bethsy) Kroeger"), -3);
            map.put(getPersonByFullName("Johann (Jean) Buddenbrook"), -3);
            map.put(getPersonByFullName("Bendix Gruenlich"), -2);
            map.put(getPersonByFullName("Frederike Buddenbrook"), -2);
            map.put(getPersonByFullName("Christian Buddenbrook"), -2);
            map.put(getPersonByFullName("Antonie (Tony) Buddenbrook"), -2);
            map.put(getPersonByFullName("Jakob Kroeger"), -2);
            map.put(getPersonByFullName("Juergen Kroeger"), -2);
            map.put(getPersonByFullName("Henriette Buddenbrook"), -2);
            map.put(getPersonByFullName("Thomas Buddenbrook"), -2);
            map.put(getPersonByFullName("Clara Buddenbrook"), -2);
            map.put(getPersonByFullName("Pfiffi Buddenbrook"), -2);
            map.put(getPersonByFullName("Erika Gruenlich"), -1);
            map.put(getPersonByFullName("Hanno Buddenbrook"), -1);
            map.put(getPersonByFullName("Gisela Buddenbrook"), -1);
            map.put(getPersonByFullName("Hugo Weinschenk"), -1);
            map.put(getPersonByFullName("Elisabeth Weinschenk"), 0);
        }
        if (fullName.equals("Johan Buddenbrook")) {

            map.put(getPersonByFullName("Johan Buddenbrook"), 0);
            map.put(getPersonByFullName("Johann Buddenbrook"), 1);
            map.put(getPersonByFullName("Bernhard Buddenbrook"), 1);
            map.put(getPersonByFullName("Olly Buddenbrook"), 2);
            map.put(getPersonByFullName("Gotthold Buddenbrook"), 2);
            map.put(getPersonByFullName("Klothilde Buddenbrook"), 2);
            map.put(getPersonByFullName("Johann (Jean) Buddenbrook"), 2);
            map.put(getPersonByFullName("Clara Buddenbrook"), 3);
            map.put(getPersonByFullName("Frederike Buddenbrook"), 3);
            map.put(getPersonByFullName("Christian Buddenbrook"), 3);
            map.put(getPersonByFullName("Antonie (Tony) Buddenbrook"), 3);
            map.put(getPersonByFullName("Henriette Buddenbrook"), 3);
            map.put(getPersonByFullName("Thomas Buddenbrook"), 3);
            map.put(getPersonByFullName("Pfiffi Buddenbrook"), 3);
            map.put(getPersonByFullName("Erika Gruenlich"), 4);
            map.put(getPersonByFullName("Gisela Buddenbrook"), 4);
            map.put(getPersonByFullName("Hanno Buddenbrook"), 4);
            map.put(getPersonByFullName("Elisabeth Weinschenk"), 5);
        }
        //  HashMap has no natural order, but if the map are not changed
        //  the result of reading the map is nearly every time in the same order.
        // Hence, to prevent an order of persons shuffle map.
        List<Person> list = new ArrayList<>(map.keySet());
        Collections.shuffle(list);

        Map<Person, Integer> shuffleMap = new HashMap<>();
        list.forEach(k->shuffleMap.put(k, map.get(k)));
        return shuffleMap;
    }

    private List<List<Person>> getExpectedOrderedList(String person) {
        List<List<Person>> sortedList = new ArrayList<>();
            if (person.equals("Johan Buddenbrook")) {

                List<Person> generation0 = new ArrayList<>();
                generation0.add(getPersonByFullName("Johan Buddenbrook"));
                sortedList.add(generation0);

                List<Person> generation1 = new ArrayList<>();
                generation1.add(getPersonByFullName("Johann Buddenbrook"));
                generation1.add(getPersonByFullName("Bernhard Buddenbrook"));
                sortedList.add(generation1);

                List<Person> generation2 = new ArrayList<>();
                generation2.add(getPersonByFullName("Gotthold Buddenbrook"));
                generation2.add(getPersonByFullName("Johann (Jean) Buddenbrook"));
                generation2.add(getPersonByFullName("Olly Buddenbrook"));
                generation2.add(getPersonByFullName("Klothilde Buddenbrook"));
                sortedList.add(generation2);

                List<Person> generation3 = new ArrayList<>();
                generation3.add(getPersonByFullName("Frederike Buddenbrook"));
                generation3.add(getPersonByFullName("Henriette Buddenbrook"));
                generation3.add(getPersonByFullName("Pfiffi Buddenbrook"));
                generation3.add(getPersonByFullName("Thomas Buddenbrook"));
                generation3.add(getPersonByFullName("Antonie (Tony) Buddenbrook"));
                generation3.add(getPersonByFullName("Christian Buddenbrook"));
                generation3.add(getPersonByFullName("Clara Buddenbrook"));
                sortedList.add(generation3);

                List<Person> generation4 = new ArrayList<>();
                generation4.add(getPersonByFullName("Hanno Buddenbrook"));
                generation4.add(getPersonByFullName("Erika Gruenlich"));
                generation4.add(getPersonByFullName("Gisela Buddenbrook"));
                sortedList.add(generation4);

                List<Person> generation5 = new ArrayList<>();
                generation5.add(getPersonByFullName("Elisabeth Weinschenk"));
                sortedList.add(generation5);
            }

            if (person.equals("Elisabeth Weinschenk")) {

                List<Person> generation0 = new ArrayList<>();
                generation0.add(getPersonByFullName("Johan Buddenbrook"));
                sortedList.add(generation0);

                List<Person> generation1 = new ArrayList<>();
                generation1.add(getPersonByFullName("Johann Buddenbrook"));
                generation1.add(getPersonByFullName("Antoinette Duchamps"));
                generation1.add(getPersonByFullName("Bernhard Buddenbrook"));
                generation1.add(getPersonByFullName("Leberecht Kroeger"));
                generation1.add(getPersonByFullName("Catharina Kroeger"));
                sortedList.add(generation1);

                List<Person> generation2 = new ArrayList<>();
                generation2.add(getPersonByFullName("Gotthold Buddenbrook"));
                generation2.add(getPersonByFullName("Johann (Jean) Buddenbrook"));
                generation2.add(getPersonByFullName("Elisabeth (Bethsy) Kroeger"));
                generation2.add(getPersonByFullName("Olly Buddenbrook"));
                generation2.add(getPersonByFullName("Klothilde Buddenbrook"));
                generation2.add(getPersonByFullName("Justus Kroeger"));
                sortedList.add(generation2);

                List<Person> generation3 = new ArrayList<>();
                generation3.add(getPersonByFullName("Frederike Buddenbrook"));
                generation3.add(getPersonByFullName("Henriette Buddenbrook"));
                generation3.add(getPersonByFullName("Pfiffi Buddenbrook"));
                generation3.add(getPersonByFullName("Thomas Buddenbrook"));
                generation3.add(getPersonByFullName("Antonie (Tony) Buddenbrook"));
                generation3.add(getPersonByFullName("Bendix Gruenlich"));
                generation3.add(getPersonByFullName("Christian Buddenbrook"));
                generation3.add(getPersonByFullName("Clara Buddenbrook"));
                generation3.add(getPersonByFullName("Juergen Kroeger"));
                generation3.add(getPersonByFullName("Jakob Kroeger"));
                sortedList.add(generation3);

                List<Person> generation4 = new ArrayList<>();
                generation4.add(getPersonByFullName("Hanno Buddenbrook"));
                generation4.add(getPersonByFullName("Erika Gruenlich"));
                generation4.add(getPersonByFullName("Hugo Weinschenk"));
                generation4.add(getPersonByFullName("Gisela Buddenbrook"));
                sortedList.add(generation4);

                List<Person> generation5 = new ArrayList<>();
                generation5.add(getPersonByFullName("Elisabeth Weinschenk"));
                sortedList.add(generation5);
            }
            if (person.equals("Antonie (Tony) Buddenbrook")) {

                List<Person> generation0 = new ArrayList<>();
                generation0.add(getPersonByFullName("Johan Buddenbrook"));
                sortedList.add(generation0);

                List<Person> generation1 = new ArrayList<>();
                generation1.add(getPersonByFullName("Johann Buddenbrook"));
                generation1.add(getPersonByFullName("Antoinette Duchamps"));
                generation1.add(getPersonByFullName("Bernhard Buddenbrook"));
                generation1.add(getPersonByFullName("Leberecht Kroeger"));
                generation1.add(getPersonByFullName("Catharina Kroeger"));
                sortedList.add(generation1);

                List<Person> generation2 = new ArrayList<>();
                generation2.add(getPersonByFullName("Gotthold Buddenbrook"));
                generation2.add(getPersonByFullName("Johann (Jean) Buddenbrook"));
                generation2.add(getPersonByFullName("Elisabeth (Bethsy) Kroeger"));
                generation2.add(getPersonByFullName("Olly Buddenbrook"));
                generation2.add(getPersonByFullName("Klothilde Buddenbrook"));
                generation2.add(getPersonByFullName("Justus Kroeger"));
                sortedList.add(generation2);

                List<Person> generation3 = new ArrayList<>();
                generation3.add(getPersonByFullName("Frederike Buddenbrook"));
                generation3.add(getPersonByFullName("Henriette Buddenbrook"));
                generation3.add(getPersonByFullName("Pfiffi Buddenbrook"));
                generation3.add(getPersonByFullName("Thomas Buddenbrook"));
                generation3.add(getPersonByFullName("Antonie (Tony) Buddenbrook"));
                generation3.add(getPersonByFullName("Christian Buddenbrook"));
                generation3.add(getPersonByFullName("Clara Buddenbrook"));
                generation3.add(getPersonByFullName("Juergen Kroeger"));
                generation3.add(getPersonByFullName("Jakob Kroeger"));
                sortedList.add(generation3);

                List<Person> generation4 = new ArrayList<>();
                generation4.add(getPersonByFullName("Hanno Buddenbrook"));
                generation4.add(getPersonByFullName("Erika Gruenlich"));
                generation4.add(getPersonByFullName("Gisela Buddenbrook"));
                sortedList.add(generation4);

                List<Person> generation5 = new ArrayList<>();
                generation5.add(getPersonByFullName("Elisabeth Weinschenk"));
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
        }catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}