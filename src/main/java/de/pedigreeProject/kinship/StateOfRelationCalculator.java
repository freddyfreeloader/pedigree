package de.pedigreeProject.kinship;

import de.pedigreeProject.model.Person;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiPredicate;

/**
 * Computes the state of kinship of a person in a List to its neighbours.
 */
public class StateOfRelationCalculator {

    private final BiPredicate<Person, Person> nextPersonIsSpouse = (current, next) -> current.getSpouses().stream().mapToInt(Person::getId).anyMatch(id -> id == next.getId());
    private final BiPredicate<Person, Person> nextPersonIsSibling = (current, next) -> current.getSiblings().stream().mapToInt(Person::getId).anyMatch(id -> id == next.getId());
    private final BiPredicate<Person, Person> PersonBeforeIsSiblingOfNextPerson = (before, next) -> before != null && before.getSiblings().stream().mapToInt(Person::getId).anyMatch(id -> id == next.getId());

    /**
     * Returns the state of kinship of a person in a List to his neighbours as a {@link StateOfRelation}.
     *
     * @param personCurrent the person who wants to know its state of relationship
     * @param personsList the List of persons the person belongs to
     * @return a {@code StateOfRelation} enum
     */
    public StateOfRelation getState(final Person personCurrent, @NotNull final List<Person> personsList) {

        StateOfRelation state = StateOfRelation.LAST_INDEX;
        Person personBefore = null;
        int currentIndex = personsList.indexOf(personCurrent);
        int beforeIndex = currentIndex -1;
        int nextIndex = currentIndex + 1;

        if (currentIndex > 0) {
            personBefore = personsList.get(beforeIndex);
        }
        if (nextIndex < personsList.size()) {

            Person personAfter = personsList.get(nextIndex);

            if (nextPersonIsSpouse.test(personCurrent, personAfter)) {
                state = StateOfRelation.NEXT_IS_SPOUSE;

            } else if (nextPersonIsSibling.test(personCurrent, personAfter)) {
                state = StateOfRelation.NEXT_IS_SIBLING;

            } else if (PersonBeforeIsSiblingOfNextPerson.test(personBefore, personAfter)) {
                state = StateOfRelation.BEFORE_IS_SIBLING_OF_NEXT;

            } else {
                state = StateOfRelation.NEXT_IS_NO_RELATIVE;
            }
        }
        return state;
    }
}
