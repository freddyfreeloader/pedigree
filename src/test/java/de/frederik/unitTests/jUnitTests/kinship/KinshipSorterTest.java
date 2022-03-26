package de.frederik.unitTests.jUnitTests.kinship;

import de.frederik.testUtils.testData.BuddenbrooksData;
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

        List<List<Person>> expectedList = getExpectedOrderedList(JOHAN);
        Map<Person, Integer> personIntegerMap = getPersonGeneration(JOHAN);
        KinshipSorter kinshipSorter = new KinshipSorterImpl();

        assertEquals(expectedList, kinshipSorter.getSortedLists(personIntegerMap));
    }

    @Test
    @DisplayName("Sorter: test sort order with Buddenbrooks pedigree from youngest person")
    void getSortedList_BuddenbrooksFromBottom() {

        List<List<Person>> expectedList = getExpectedOrderedList(ELISABETH_WEINSCHENK);
        Map<Person, Integer> personIntegerMap = getPersonGeneration(ELISABETH_WEINSCHENK);
        KinshipSorter kinshipSorter = new KinshipSorterImpl();

        assertEquals(expectedList, kinshipSorter.getSortedLists(personIntegerMap));
    }

    @Test
    @DisplayName("Sorter: test sort order with Buddenbrooks pedigree from middle")
    void getSortedList_BuddenbrooksFromMiddle() {

        List<List<Person>> expectedList = getExpectedOrderedList(ANTONIE);
        Map<Person, Integer> personIntegerMap = getPersonGeneration(ANTONIE);
        KinshipSorter kinshipSorter = new KinshipSorterImpl();

        assertEquals(expectedList, kinshipSorter.getSortedLists(personIntegerMap));
    }

    private Map<Person, Integer> getPersonGeneration(BuddenbrooksData person) {

        Map<Person, Integer> map = new HashMap<>();

        if (person.equals(ANTONIE)) {

            map.putAll(getMapOfGeneration(-3, JOHAN));
            map.putAll(getMapOfGeneration(-2, JOHANN, CATHARINA, BERNHARD, ANTOINETTE, LEBERECHT));
            map.putAll(getMapOfGeneration(-1, OLLY, GOTTHOLD, JUSTUS, KLOTHILDE, ELISABETH_KROEGER, JOHANN_JEAN));
            map.putAll(getMapOfGeneration(0, JAKOB, FREDERIKE, CHRISTIAN, PFIFFI, ANTONIE, JUERGEN, HENRIETTE, THOMAS, CLARA));
            map.putAll(getMapOfGeneration(1, HANNO, GISELA, ERIKA));
            map.putAll(getMapOfGeneration(2, ELISABETH_WEINSCHENK));

        }
        if (person.equals(ELISABETH_WEINSCHENK)) {

            map.putAll(getMapOfGeneration(-5, JOHAN));
            map.putAll(getMapOfGeneration(-4, JOHANN, CATHARINA, BERNHARD, LEBERECHT, ANTOINETTE));
            map.putAll(getMapOfGeneration(-3, OLLY, GOTTHOLD, JUSTUS, KLOTHILDE, ELISABETH_KROEGER, JOHANN_JEAN));
            map.putAll(getMapOfGeneration(-2, BENDIX, FREDERIKE, CHRISTIAN, ANTONIE, JAKOB, JUERGEN, HENRIETTE, THOMAS, CLARA, PFIFFI));
            map.putAll(getMapOfGeneration(-1, ERIKA, HANNO, GISELA, HUGO));
            map.putAll(getMapOfGeneration(0, ELISABETH_WEINSCHENK));

        }
        if (person.equals(JOHAN)) {
            map.putAll(getMapOfGeneration(0, JOHAN));
            map.putAll(getMapOfGeneration(1, JOHANN, BERNHARD));
            map.putAll(getMapOfGeneration(2, OLLY, GOTTHOLD, KLOTHILDE, JOHANN_JEAN));
            map.putAll(getMapOfGeneration(3, CLARA, FREDERIKE, CHRISTIAN, ANTONIE, HENRIETTE, THOMAS, PFIFFI));
            map.putAll(getMapOfGeneration(4, ERIKA, GISELA, HANNO));
            map.putAll(getMapOfGeneration(5, ELISABETH_WEINSCHENK));
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

    private Map<Person, Integer> getMapOfGeneration(int generation, BuddenbrooksData... persons) {
        Map<Person, Integer> map = new HashMap<>();
        for (BuddenbrooksData person : persons) {
            map.put(getPersonByFullName(person), generation);
        }
        return map;
    }

    private List<List<Person>> getExpectedOrderedList(BuddenbrooksData person) {
        List<List<Person>> sortedList = new ArrayList<>();
        if (person.equals(JOHAN)) {

            sortedList.add(getGenerationList(JOHAN));
            sortedList.add(getGenerationList(JOHANN, BERNHARD));
            sortedList.add(getGenerationList(GOTTHOLD, JOHANN_JEAN, OLLY, KLOTHILDE));
            sortedList.add(getGenerationList(FREDERIKE, HENRIETTE, PFIFFI, THOMAS, ANTONIE, CHRISTIAN, CLARA));
            sortedList.add(getGenerationList(HANNO, ERIKA, GISELA));
            sortedList.add(getGenerationList(ELISABETH_WEINSCHENK));
        }
        if (person.equals(ELISABETH_WEINSCHENK)) {

            sortedList.add(getGenerationList(JOHAN));
            sortedList.add(getGenerationList(JOHANN, ANTOINETTE, BERNHARD, LEBERECHT, CATHARINA));
            sortedList.add(getGenerationList(GOTTHOLD, JOHANN_JEAN, ELISABETH_KROEGER, OLLY, KLOTHILDE, JUSTUS));
            sortedList.add(getGenerationList(FREDERIKE, HENRIETTE, PFIFFI, THOMAS, ANTONIE, BENDIX, CHRISTIAN, CLARA, JUERGEN, JAKOB));
            sortedList.add(getGenerationList(HANNO, ERIKA, HUGO, GISELA));
            sortedList.add(getGenerationList(ELISABETH_WEINSCHENK));
        }
        if (person.equals(ANTONIE)) {

            sortedList.add(getGenerationList(JOHAN));
            sortedList.add(getGenerationList(JOHANN, ANTOINETTE, BERNHARD, LEBERECHT, CATHARINA));
            sortedList.add(getGenerationList(GOTTHOLD, JOHANN_JEAN, ELISABETH_KROEGER, OLLY, KLOTHILDE, JUSTUS));
            sortedList.add(getGenerationList(FREDERIKE, HENRIETTE, PFIFFI, THOMAS, ANTONIE, CHRISTIAN, CLARA, JUERGEN, JAKOB));
            sortedList.add(getGenerationList(HANNO, ERIKA, GISELA));
            sortedList.add(getGenerationList(ELISABETH_WEINSCHENK));
        }
        return sortedList;
    }

    private List<Person> getGenerationList(BuddenbrooksData... persons) {
        List<Person> generation = new ArrayList<>();
        for (BuddenbrooksData person : persons) {
            generation.add(getPersonByFullName(person));
        }
        return generation;
    }

    private @Nullable Person getPersonByFullName(BuddenbrooksData fullName) {
        try {
            return buddenbrooks.stream()
                    .filter(person -> person.toString().equals(fullName.toString()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(fullName.toString() + " is not provided!"));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}