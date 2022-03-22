package de.pedigreeProject.utils;

import de.pedigreeProject.model.Person;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

import java.util.*;

/**
 * Provides a method to compute vertical translation of Nodes, depends on the year of birth of the person.
 * @see #translateNodes
 */
public class VerticalTranslator {

    private final Map<Person, Node> mapOfNodes;
    private final List<List<Person>> listOfGenerations;
    private double additional_spacing_factor;

    public VerticalTranslator(Map<Person, Node> mapOfNodes, List<List<Person>> listOfGenerations) {
        this.mapOfNodes = Objects.requireNonNull(mapOfNodes);
        this.listOfGenerations = Objects.requireNonNull(listOfGenerations);
    }

    /**
     * 1. get the oldest person of each generation <br>
     * 2. get difference to the oldest person in each generation separately (personsYearOfBirth - oldestYear )<br>
     * 3. multiply the result with the ADDITIONAL_SPACING_FACTOR to increase the visible spacing<br>
     * 4. set HBox.Margin#top to the result
     *
     * @param additional_spacing_factor to increase the visible spacing
     */
    public void translateNodes(final double additional_spacing_factor) {
        this.additional_spacing_factor = additional_spacing_factor;

        for (List<Person> list : listOfGenerations) {
            double oldestPerson = getOldestPersonFromList(list);

            for (Person person : list) {
                double topMargin = computeTranslationY(oldestPerson, person);
                Node node = mapOfNodes.get(person);
                // right margin was set by MainModelController#translatePersonNodesHorizontally()
                double rightMargin = HBox.getMargin(node).getRight();
                HBox.setMargin(node, new Insets(topMargin, rightMargin, 0.0, 0.0));
            }
        }
    }

    private double computeTranslationY(double oldestPerson, Person person) {
        if (person.getYearOfBirth().isEmpty()) {
            return 0;
        }
        return (person.getYearOfBirth().get().getValue()-oldestPerson) * additional_spacing_factor;
    }

    private double getOldestPersonFromList(List<Person> persons) {
        return persons.stream()
                .filter(person -> person.getYearOfBirth().isPresent())
                .mapToInt(p -> p.getYearOfBirth().get().getValue())
                .min()
                .orElse(0);
    }
}
