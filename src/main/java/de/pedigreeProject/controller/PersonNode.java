package de.pedigreeProject.controller;

import de.pedigreeProject.model.Model;
import de.pedigreeProject.model.Person;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonNode {
    private final Model model;

    public PersonNode(Model model){
        this.model=model;
    }

    public Map<Person, Node> getMapOfNodes(List<Person> persons) {
        Map<Person, Node> map = new HashMap<>();
        for (Person person : persons) {
            map.put(person, generateLabel(person));
        }
        return map;
    }

    /**
     * Generates a {@link Label} with persons full name and year of birth.<br>
     * If the person is selected then the label have special CSS-class 'selected-person'.
     * <p>Note: It returns a {@code Node} object, so further implementations could change or wrap Label into another Node, <br>
     * for example a subclass of {@link javafx.scene.layout.Pane} to extend possibilities or build a hit box around. </p>
     *
     * @param person the Person the Label belongs to
     * @return a Node with persons data
     */
    private Node generateLabel(Person person) {
        Label label = new Label();
        label.textProperty().bind(person.givenNameProperty().concat(" ")
                .concat(person.familyNameProperty())
                .concat("\n")
                .concat(person.getYearOfBirth().isEmpty() ? "" : "*" + person.yearOfBirthProperty().getValue()));
        label.getStyleClass().add("person-label");
        label.setOnMouseClicked(event -> model.setCurrentPerson(person));

        if (person.equals(model.getCurrentPerson())) {
            label.getStyleClass().add("selected-person");
        } else {
            label.getStyleClass().remove("selected-person");
        }
        return label;
    }
}
