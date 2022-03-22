package de.pedigreeProject.kinship;

import de.pedigreeProject.model.Person;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Comparator.*;

/**
 * Sorts a  Map (provided from the {@link GeneticKinshipCalculator}) with persons as Key and generation (Integer) as Value to Lists . <br>
 * Each List contains persons of the same generation (Integer value),<br>
 * the lists are ordered by generation (the oldest generation -> first list, the youngest generation -> last list). <br>
 * <p>Furthermore each List internally is sorted by:
 * <ul>
 *     <li>first List (oldest generation) ist sorted by age , spouses are direct neighbours, siblings are grouped</li>
 *     <li>the next Lists depends on the sort order of the list one generation older:</li>
 *     <ul>
 *         <li>Iterates through the List and adds firstly the oldest child and child spouse, then next child and spouse and so on</li>
 *         <li>if some persons left they were added to the end of the list, sorted by age</li>
 *     </ul>
 * </ul>
 */
public class KinshipSorterImpl implements KinshipSorter {

    private Map<Person, Integer> personWithGenerationMap;

    private final List<List<Person>> sortedLists = new ArrayList<>();

    /**
     * Generates the Lists and returns as {@code List<List<Person>>}
     *
     * @return the sorted Lists
     */
    @Override
    public List<List<Person>> getSortedLists(final Map<Person, Integer> relatives) {
        personWithGenerationMap = relatives;
        sortedLists.clear();
        generateSortedGenerations();

        return sortedLists;
    }

    /**
     * Constructor to get the sorted Map of relatives
     *
     * @see KinshipSorterImpl
     */
    public KinshipSorterImpl() {}


    private void generateSortedGenerations() {

        sortListForEldestGeneration();

        createListsForEachGeneration();

        sortRemainingGenerationsAndFillLists();
    }


    private void sortListForEldestGeneration() {

        List<Person> eldestSortedByYearOfBirth = getEldestSortedByYearOfBirth();

        List<Person> eldestTotallySorted = getEldestSorted(eldestSortedByYearOfBirth);

        sortedLists.add(eldestTotallySorted);
    }

    @NotNull
    private List<Person> getEldestSortedByYearOfBirth() {
        int eldestGeneration = personWithGenerationMap.values().stream().mapToInt(integer -> integer).min().orElse(0);

        return personWithGenerationMap.entrySet().stream()
                .filter(personIntegerEntry -> personIntegerEntry.getValue() == (eldestGeneration))
                .sorted(Map.Entry.comparingByKey(comparing(person -> person.getYearOfBirth().orElse(null), nullsLast(naturalOrder()))))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @NotNull
    private List<Person> getEldestSorted(List<Person> eldestSortedByYearOfBirth) {
        List<Person> eldestSorted = new ArrayList<>();

        eldestSortedByYearOfBirth.forEach(person -> {
            addPersonToList(person, eldestSorted);
            addSpousesToList(person, eldestSorted);
            addSiblingsAndTheirSpousesToList(person, eldestSorted);
        });

        //if somebody is missing...(persons that were not  found  because of missing or incomplete entries in database
        eldestSortedByYearOfBirth.stream().filter(person -> !eldestSorted.contains(person)).forEach(eldestSorted::add);
        return eldestSorted;
    }

    private void createListsForEachGeneration() {

        int eldestGeneration = personWithGenerationMap.values().stream().mapToInt(integer -> integer).min().orElse(0);
        int youngestGeneration = personWithGenerationMap.values().stream().mapToInt(integer -> integer).max().orElse(0);

        for (int i = (eldestGeneration + 1); i <= youngestGeneration; i++) {

            sortedLists.add(new ArrayList<>());
        }
    }

    private void sortRemainingGenerationsAndFillLists() {
        for (int i = 0; i < sortedLists.size() - 1; i++) {

            List<Person> currentGeneration = sortedLists.get(i);
            List<Person> oneGenerationYounger = sortedLists.get(i + 1);
            int nextGeneration = personWithGenerationMap.get(currentGeneration.get(0)) + 1;

            currentGeneration.forEach(person -> addChildrenAndTheirSpousesToList(person, oneGenerationYounger));

            // add missing persons...(persons that were not  found by addChildrenAndTh...() because of missing or incomplete entries in database
            addMissingPersonsToOneGenerationYounger(oneGenerationYounger, nextGeneration);
        }
    }

    private void addMissingPersonsToOneGenerationYounger(List<Person> oneGenerationYounger, int nextGeneration) {
        List<Person> missingPersons = personWithGenerationMap.entrySet().stream()
                .filter(personIntegerEntry -> personIntegerEntry.getValue() == nextGeneration)
                .map(Map.Entry::getKey)
                .sorted(comparing(person -> person.getYearOfBirth().orElse(null), nullsLast(naturalOrder())))
                .collect(Collectors.toList());

        missingPersons.stream().filter(person -> !oneGenerationYounger.contains(person)).forEach(oneGenerationYounger::add);
    }

    private void addPersonToList(Person person, List<Person> list) {

        if (!list.contains(person)) {
            list.add(person);
        }
    }

    private void addSpousesToList(Person person, List<Person> list) {

        if (!person.getSpouses().isEmpty()) {
            list.addAll(person.getSpouses().stream()
                    .filter(personWithGenerationMap::containsKey)//only add spouses if already in map
                    .filter(p -> !list.contains(p))// no duplicates
                    .sorted(comparing(p -> p.getYearOfBirth().orElse(null), nullsLast(naturalOrder())))
                    .collect(Collectors.toList()));
        }
    }

    private void addSiblingsAndTheirSpousesToList(Person person, List<Person> list) {

        if (!person.getSiblings().isEmpty()) {

            addPersonsAndTheirSpousesToList(person.getSiblings(), list);
        }
    }

    private void addChildrenAndTheirSpousesToList(Person person, List<Person> list) {

        if (!person.getChildren().isEmpty()) {

            addPersonsAndTheirSpousesToList(person.getChildren(), list);
        }
    }

    private void addPersonsAndTheirSpousesToList(Set<Person> persons, List<Person> list) {

        persons.stream()
                .filter(p -> !list.contains(p))// no duplicates
                .sorted(comparing(person -> person.getYearOfBirth().orElse(null), nullsLast(naturalOrder())))
                .collect(Collectors.toList())
                .forEach(person -> {
                    list.add(person);
                    addSpousesToList(person, list);
                });
    }
}
