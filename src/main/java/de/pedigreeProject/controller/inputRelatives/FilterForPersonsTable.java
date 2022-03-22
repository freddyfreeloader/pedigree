package de.pedigreeProject.controller.inputRelatives;

import de.pedigreeProject.model.Person;
import javafx.scene.control.TableView;

import java.util.List;

/**
 * Provides a Filter for the persons table in {@link InputRelativesController} to minimize the items.
 */
public class FilterForPersonsTable {


    /**
     * <p>Filters the persons list from persons who are already are relative or
     * can not be presumably a direct relative to this person.</p>
     * <p>The aim is to shorten the persons list to help user to find the relative.</p>
     * <p>The filter is fragil because it depends on local rights and could be made optional in further implementations.</p>
     * <p>Actually it filters:</p>
     * <ul>
     *     <li>every person who is already in persons relative lists</li>
     *     <li>every person in the children's line of persons siblings</li>
     *     <li>every person in the parent line of persons parents</li>
     *     <li>every person in the children's line of persons children</li>
     *     <li>every person who is sibling of persons spouse</li>
     * </ul>
     */
    public static void filterPersons(TableView<Person> personsTable, Person person, List<Person> parents, List<Person> spouses, List<Person> siblings, List<Person> children   ) {
        personsTable.getItems().remove(person);
        personsTable.getItems().removeAll(parents);
        personsTable.getItems().removeAll(spouses);
        personsTable.getItems().removeAll(children);
        personsTable.getItems().removeAll(siblings);

        siblings.forEach(sib -> removeChildren(sib, personsTable.getItems()));
        siblings.forEach(sibling -> sibling.getSpouses().forEach(personsTable.getItems()::remove));

        parents.forEach(parent -> removeParents(parent, personsTable.getItems()));
        parents.forEach(parent -> parent.getSiblings().forEach(personsTable.getItems()::remove));

        children.forEach(child -> removeChildren(child, personsTable.getItems()));
        children.forEach(child -> child.getSpouses().forEach(personsTable.getItems()::remove));

        spouses.forEach(spouse -> spouse.getSiblings().forEach(personsTable.getItems()::remove));
        spouses.forEach(spouse -> spouse.getParents().forEach(personsTable.getItems()::remove));
    }

    /**
     * Removes recursively every person of the parents line of the person
     *
     * @param person  the person whose parents line have to be removed
     * @param persons the list from where the person should be removed
     */
    private static void removeParents(Person person, List<Person> persons) {
        if (!person.getParents().isEmpty()) {
            person.getParents().forEach(parent -> {
                removeParents(parent, persons);
                persons.remove(parent);
            });
        }
    }

    /**
     * Removes recursively every person of the children's line of the person
     *
     * @param person  the person whose children's line have to be removed
     * @param persons the list from where the person should be removed
     */
    private static void removeChildren(Person person, List<Person> persons) {
        if (!person.getChildren().isEmpty()) {
            person.getChildren().forEach(child -> {
                removeChildren(child, persons);
                persons.remove(child);
            });
        }
    }
}
