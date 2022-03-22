package de.pedigreeProject.kinship;

import de.pedigreeProject.model.Person;

import java.util.List;
import java.util.Map;

/**
 * Sorts persons to different Lists.
 */
public interface KinshipSorter {
    /**
     * Returns the sorted Lists.
     * @return the sorted Lists
     */
    List<List<Person>> getSortedLists(final Map<Person, Integer> relatives);
}
