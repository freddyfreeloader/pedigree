package de.pedigreeProject.kinship;

import de.pedigreeProject.model.Person;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Computes the genetic kinship to a given person (ME) .<br>
 * Provides a {@code HashMap<Person, Integer>} with the related persons and their generations.
 * The generation of ME is 0, each generation older 1--, each generation younger 1++.<br>
 * The calculator assumes that all persons are correctly related and there is no overlap of the generations.
 */
public class GeneticKinshipCalculator implements KinshipCalculator {


    private final Map<Person, Integer> personGenerationMap = new HashMap<>();
    /**
     * The person whose genetic kinship are searched for
     */
    private Person me;

    /**
     * @see GeneticKinshipCalculator
     */
    public GeneticKinshipCalculator() {

    }

    @Override
    public Map<Person, Integer> getPersonGenerationMap(@NotNull final Person me) {
        personGenerationMap.clear();
        this.me = me;
        collectRelatives();
        return personGenerationMap;
    }

    /**
     * Gets a Set of persons who are genetic related to person me (inklusive me).
     *
     * @return a Set of person who are genetic related
     */
    @Override
    public Set<Person> getRelatives(@NotNull final Person me) {
        this.me = me;
        collectRelatives();
        return personGenerationMap.keySet();
    }

    private void collectRelatives() {

        collectParentsLineAndTheirSiblings(me);

        collectMySiblings();

        Set<Person> personSet = new HashSet<>(personGenerationMap.keySet());
        personSet.forEach(this::collectChildrenLine);
    }

    private void collectParentsLineAndTheirSiblings(@NotNull final Person person) {

        personGenerationMap.putIfAbsent(person, 0);

        int currentGeneration = personGenerationMap.get(person);

        Set<Person> parents = person.getParents();
        parents.forEach(parent -> {
            personGenerationMap.put(parent, currentGeneration - 1);
            parent.getSiblings().forEach(sibling -> personGenerationMap.put(sibling, currentGeneration - 1));
        });

        parents.forEach(this::collectParentsLineAndTheirSiblings);
    }

    private void collectMySiblings() {

        me.getSiblings().forEach(sibling -> personGenerationMap.putIfAbsent(sibling, 0));
    }

    private void collectChildrenLine(final Person person) {

        int currentGeneration = personGenerationMap.get(person);

        person.getChildren().forEach(child -> {
            personGenerationMap.put(child, currentGeneration + 1);
            collectChildrenLine(child);
        });
    }
}
