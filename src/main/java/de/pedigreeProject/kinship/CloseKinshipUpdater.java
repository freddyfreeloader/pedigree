package de.pedigreeProject.kinship;

import de.pedigreeProject.model.Person;

import java.util.List;

public class CloseKinshipUpdater implements KinshipUpdater {

    public CloseKinshipUpdater() {
    }

    /**
     * <p>Updates the lists of relatives of this person and every person, who is affected by the update.</p>
     * <p>If person is null, method returns immediately without changes. </p>
     * <p> Updates:</p>
     * <ul>
     *     <li>the lists of this person
     *     <ul>
     *         <li>list of parents</li>
     *         <li>list of spouses</li>
     *         <li>list of siblings</li>
     *         <li>list of children</li>
     *     </ul>
     *     </li>
     *     <li>the lists of the members of the person lists</li>
     *     <li>the lists of persons whose are transitive affected by the updates
     *     <ul>
     *         <li>all children of persons parent are siblings of person</li>
     *         <li>all children of person are siblings</li>
     *         <li>each child of parent should have that parent</li>
     *     </ul>
     *     </li>
     * </ul>
     *
     * @param person person to update, may be null
     * @param parents replaces persons parents list
     * @param children replaces persons children list
     * @param spouses replaces persons spouses list
     * @param siblings replaces persons siblings list
     */
    @Override
    public void updateKinship(Person person, List<Person> parents, List<Person> children, List<Person> spouses, List<Person> siblings) {
        if (person == null) {
            return;
        }
        // 1. remove every direct relationship to person from oldData
        removeOldKins(person);

        // 2. update person with new Data
        addNewKins(person, parents, children, spouses, siblings);

        // 3. update every direct relationship from updated person
        updateCloseKinship(person);

        // 4. update transitive kinship
        // 4.1 all children of persons parent are siblings of person
        updateSiblingsLists(person);
        // 4.2. all children of person are siblings
        addSiblingsToChildren(person, children);
        // 4.2. each child of parent should have that parent
        addParentsToSiblings(person);
    }

    private void addParentsToSiblings(Person person) {
        person.getParents().forEach(parent ->
                parent.getChildren().forEach(child -> child.addParent(parent)));
    }

    private void addSiblingsToChildren(Person person, List<Person> children) {
        person.getChildren().forEach(child ->
                children.stream()
                        .filter(c -> c.getId() != child.getId())
                        .forEach(child::addSibling));
    }

    private void updateSiblingsLists(Person person) {
        person.getParents()
                .forEach(parent ->
                        parent.getChildren().stream()
                                .filter(person1 -> person1 != person)
                                .forEach(childOfParent -> {
                                    childOfParent.addSibling(person);
                                    person.addSibling(childOfParent);
                                }));
    }

    private void updateCloseKinship(Person person) {
        person.getParents().forEach(parent -> parent.addChild(person));
        person.getChildren().forEach(child -> child.addParent(person));
        person.getSpouses().forEach(spouse -> spouse.addSpouse(person));
        person.getSiblings().forEach(sibling -> sibling.addSibling(person));
    }

    private void addNewKins(Person person, List<Person> parents, List<Person> children, List<Person> spouses, List<Person> siblings) {
        parents.forEach(person::addParent);
        children.forEach(person::addChild);
        spouses.forEach(person::addSpouse);
        siblings.forEach(person::addSibling);
    }

    private void removeOldKins(Person person) {
        person.getParents().stream().filter(parent -> parent != person).forEach(parent -> parent.removeChild(person));
        person.getParents().clear();

        person.getChildren().stream().filter(child -> child != person).forEach(child -> child.removeParent(person));
        person.getChildren().clear();

        person.getSiblings().stream().filter(sibling -> sibling != person).forEach(sibling -> sibling.removeSibling(person));
        person.getSiblings().clear();

        person.getSpouses().stream().filter(spouse -> spouse != person).forEach(spouse -> spouse.removeSpouse(person));
        person.getSpouses().clear();
    }
}
