package de.pedigreeProject.kinship;

import de.pedigreeProject.model.Person;

import java.util.Optional;

/**
 * Validator that checks if a relationship between two persons is allowed.
 * @see StrongKinshipValidator
 */
public interface KinshipValidator {
    /**
     * Validates if the given aspirant could be a child of the given person.<br>
     * It generates a message why the relationship is not possible within an {@code Optional<String>}.<br>
     * The Optional is empty if no rules are violated and the relationship is allowed.
     *
     * @param aspirant the person who wants to be a child of the person
     * @param person   the person  the relationship is asked for
     * @return an {@code Optional<String} with a message or an {@code Optional.empty()}
     */
    Optional<String> aspirantCouldBeChildOfPerson(Person aspirant, Person person);

    /**
     * Validates if the given aspirant could be a parent of the given person.<br>
     * It generates a message why the relationship is not possible within an {@code Optional<String>}.<br>
     * The Optional is empty if no rules are violated and the relationship is allowed.
     *
     * @param aspirant the person who wants to be a parent of the person
     * @param person   the person  the relationship is asked for
     * @return an {@code Optional<String} with a message or an {@code Optional.empty()}
     */
    Optional<String> aspirantCouldBeParentOfPerson(Person aspirant, Person person);

    /**
     * Validates if the given aspirant could be a spouse of the given person.<br>
     * It generates a message why the relationship is not possible within an {@code Optional<String>}.<br>
     * The Optional is empty if no rules are violated and the relationship is allowed.
     *
     * @param aspirant the person who wants to be a spouse of the person
     * @param person   the person  the relationship is asked for
     * @return an {@code Optional<String} with a message or an {@code Optional.empty()}
     */
    Optional<String> aspirantCouldBeSpouseOfPerson(Person aspirant, Person person);

    /**
     * Validates if the given aspirant could be a sibling of the given person.<br>
     * It generates a message why the relationship is not possible within an {@code Optional<String>}.<br>
     * The Optional is empty if no rules are violated and the relationship is allowed.
     *
     * @param aspirant the person who wants to be a sibling of the person
     * @param person   the person  the relationship is asked for
     * @return an {@code Optional<String} with a message or an {@code Optional.empty()}
     */
    Optional<String> aspirantCouldBeSiblingOfPerson(Person aspirant, Person person);
}
