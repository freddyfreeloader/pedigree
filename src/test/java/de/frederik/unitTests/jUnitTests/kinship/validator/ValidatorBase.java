package de.frederik.unitTests.jUnitTests.kinship.validator;

import de.pedigreeProject.kinship.KinshipValidator;
import de.pedigreeProject.kinship.StrongKinshipValidator;
import de.pedigreeProject.model.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.provider.Arguments;

import java.time.Year;

/**
 * Provides reusable methods for validating kinship.
 */
public abstract class ValidatorBase {
    static KinshipValidator kinshipValidator;

    @BeforeAll
    static void setUp() {
        kinshipValidator = new StrongKinshipValidator();
    }

    // Arguments that should provoke an error message:

    static Arguments aspirantEqualsPerson() {
        Person person = createPerson(1);
        String message = "Generates an error message because aspirant equals person.";

        return Arguments.of(person, person, message);
    }

    static Arguments aspirantIsParentOfPerson() {
        Person person = createPerson(1);
        Person aspirant = createPerson(2);
        person.addParent(aspirant);
        String message = "Generates an error message because aspirant is already a parent of person.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments aspirantIsSpouseOfPerson() {
        Person person = createPerson(1);
        Person aspirant = createPerson(2);
        person.addSpouse(aspirant);
        String message = "Generates an error message because aspirant is already a spouse of person.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments aspirantIsSiblingOfPerson() {
        Person person = createPerson(1);
        Person aspirant = createPerson(2);
        person.addSibling(aspirant);
        String message = "Generates an error message because aspirant is already a sibling of person.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments aspirantIsChildOfPerson() {
        Person person = createPerson(1);
        Person aspirant = createPerson(2);
        person.addChild(aspirant);
        String message = "Generates an error message because aspirant is already a child of person.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments aspirantIsOlderThanPerson() {
        Person person = createPerson(1);
        Person aspirant = createPerson(2);
        person.setYearOfBirth(Year.of(2000));
        aspirant.setYearOfBirth(Year.of(1000));
        String message = "Generates an error message because aspirant is older than person.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments aspirantIsYoungerThanPerson() {
        Person person = createPerson(1);
        person.setYearOfBirth(Year.of(2000));
        Person aspirant = createPerson(2);
        aspirant.setYearOfBirth(Year.of(2001));
        String message = "Generates an error message because aspirant is younger than person.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments aspirantHasTwoOtherParents() {
        Person person = createPerson(1);
        Person aspirant = createPerson(2);
        aspirant.addParent(createPerson(3));
        aspirant.addParent(createPerson(4));
        String message = "Generates an error message because aspirant has two other parents.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments aspirantIsInChildrenLineOfPerson() {
        Person person = createPerson(1);
        Person aspirant = createPerson(2);
        Person childOfPerson = createPerson(3);
        person.addChild(childOfPerson);
        childOfPerson.addChild(aspirant);
        String message = "Generates an error message because aspirant is in children's line of person.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments personIsInChildrenLineOfAspirant() {
        Person person = createPerson(1);
        Person aspirant = createPerson(2);
        Person childOfAspirant = createPerson(3);
        aspirant.addChild(childOfAspirant);
        childOfAspirant.addChild(person);
        String message = "Generates an error message because person is in children's line of aspirant.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments personIsChildOfAspirant() {
        Person person = createPerson(1);
        Person aspirant = createPerson(2);
        aspirant.addChild(person);
        String message = "Generates an error message because person is already child of aspirant.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments personIsParentOfAspirant() {
        Person person = createPerson(1);
        Person aspirant = createPerson(2);
        aspirant.addParent(person);
        String message = "Generates an error message because person is a parent of aspirant.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments personIsInParentLineOfAspirant() {
        Person person = createPerson(1);
        Person aspirant = createPerson(2);
        Person parentOfAspirant = createPerson(3);
        aspirant.addParent(parentOfAspirant);
        parentOfAspirant.addParent(person);
        String message = "Generates an error message because person is in the parent line of aspirant.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments aspirantIsInParentLineOfPerson() {
        Person person = createPerson(2);
        Person aspirant = createPerson(1);
        Person parentOfPerson = createPerson(3);
        person.addParent(parentOfPerson);
        parentOfPerson.addParent(aspirant);
        String message = "Generates an error message because aspirant is in the parent line of person.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments aspirantAndPersonHaveDifferentParents() {
        Person person = createPerson(1);
        Person aspirant = createPerson(2);
        Person parentOfPerson1 = createPerson(3);
        Person parentOfPerson2 = createPerson(4);
        Person parentOfAspirant1 = createPerson(5);
        Person parentOfAspirant2 = createPerson(6);

        person.addParent(parentOfPerson1);
        person.addParent(parentOfPerson2);

        aspirant.addParent(parentOfAspirant1);
        aspirant.addParent(parentOfAspirant2);
        String message = "Generates an error message because aspirant and person have different parents.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments aspirantIsParentOfPersonsSpouse() {
        Person person = createPerson(1);
        Person aspirant = createPerson(2);
        Person spouseOfPerson = createPerson(3);
        person.addSpouse(spouseOfPerson);
        spouseOfPerson.addParent(aspirant);
        String message = "Generates an error message because aspirant is already a parent of persons spouse.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments aspirantIsSiblingOfPersonsParent() {
        Person person = createPerson(1);
        Person aspirant = createPerson(2);
        Person parentOfPerson = createPerson(3);
        person.addParent(parentOfPerson);
        parentOfPerson.addSibling(aspirant);
        String message = "Generates an error message because aspirant is already an uncle/an aunt of person.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments personHasTwoParents() {
        Person person = createPerson(1);
        Person aspirant = createPerson(2);
        Person parentOfPerson = createPerson(3);
        Person parentOfPerson2 = createPerson(4);
        person.addParent(parentOfPerson);
        person.addParent(parentOfPerson2);
        String message = "Generates an error message because aspirant is already an uncle/an aunt of person.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments personHasAlreadyASpouse() {
        Person person = createPerson(1);
        Person aspirant = createPerson(2);
        Person spouseOfPerson = createPerson(3);
        person.addSpouse(spouseOfPerson);
        String message = "Generates an error message because person has already a spouse.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments aspirantHasAlreadyAnotherSpouse() {
        Person person = createPerson(1);
        Person aspirant = createPerson(2);
        Person spouseOfAspirant = createPerson(3);
        aspirant.addSpouse(spouseOfAspirant);
        String message = "Generates an error message because aspirant has already another spouse.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments aspirantAndPersonAreSiblings() {
        Person person = createPerson(1);
        Person aspirant = createPerson(2);
        person.addSibling(aspirant);
        aspirant.addSibling(person);
        String message = "Generates an error message because aspirant and person are siblings.";

        return Arguments.of(aspirant, person, message);
    }

    // Arguments that causes NO error messages:

    static Arguments aspirantIsNotInChildrenLineOfPerson() {
        Person person = createPerson(1);
        Person aspirant = createPerson(2);
        Person childOfPerson = createPerson(3);
        person.addChild(childOfPerson);
        childOfPerson.addChild(createPerson(4));
        String message = "Generates NO error message because aspirant is NOT in the children's line of person.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments aspirantIsNotInParentLineOfPerson() {
        Person person = createPerson(1);
        Person aspirant = createPerson(2);
        Person parentOfPerson = createPerson(3);
        person.addParent(parentOfPerson);
        parentOfPerson.addParent(createPerson(4));
        String message = "Generates NO error message because aspirant is NOT in the parent line of person.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments personIsNotInParentLineOfAspirant() {
        Person person = createPerson(1);
        Person aspirant = createPerson(2);
        Person parentOfAspirant = createPerson(3);
        Person parentOfParentOfAspirant = createPerson(4);
        aspirant.addParent(parentOfAspirant);
        parentOfAspirant.addParent(parentOfParentOfAspirant);
        parentOfParentOfAspirant.addParent(createPerson(5));
        String message = "Generates NO error message because person is NOT in the parent line of aspirant.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments aspirantIsNotInChildrenLine() {
        Person person = createPerson(1);
        Person aspirant = createPerson(2);
        Person childOfPerson = createPerson(3);
        person.addChild(childOfPerson);
        childOfPerson.addChild(createPerson(4));
        String message = "Generates NO error message because aspirant is NOT in the parent line of person.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments aspirantAndPersonHaveNoKinship() {
        Person person = createPerson(1);
        Person aspirant = createPerson(2);
        String message = "Generates NO error message because aspirant and person have no relatives.";

        return Arguments.of(aspirant, person, message);
    }


    static Person createPerson(int id) {
        return new Person(id, 1, "person" + id, "", null);
    }
}
