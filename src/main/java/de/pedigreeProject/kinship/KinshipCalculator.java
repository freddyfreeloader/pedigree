package de.pedigreeProject.kinship;

import de.pedigreeProject.model.Person;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

/**
 * Calculates the kinship of a person and the generation.
 */
public interface KinshipCalculator {
    /**
     * Gets a Set of relationships of a person
     * @return a Set of related persons
     */
    Set<Person> getRelatives(@NotNull final Person me);

    /**
     * Gets a Map with Person and its calculated generation as an Integer.<br>
     * The older the generation the lower is the value of the Integer.
     *
     * @return a Map of persons with corresponding generation
     */
    Map<Person, Integer> getPersonGenerationMap(@NotNull final Person me);
    }

