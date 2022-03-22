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
        Person person = createPerson();
        String message = "Generates an error message because aspirant equals person.";

        return Arguments.of(person, person, message);
    }

    static Arguments aspirantIsParentOfPerson() {
        Person person = createPerson();
        Person aspirant = createAspirant();
        person.addParent(aspirant);
        String message = "Generates an error message because aspirant is already a parent of person.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments aspirantIsSpouseOfPerson() {
        Person person = createPerson();
        Person aspirant = createAspirant();
        person.addSpouse(aspirant);
        String message = "Generates an error message because aspirant is already a spouse of person.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments aspirantIsSiblingOfPerson() {
        Person person = createPerson();
        Person aspirant = createAspirant();
        person.addSibling(aspirant);
        String message = "Generates an error message because aspirant is already a sibling of person.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments aspirantIsChildOfPerson() {
        Person person = createPerson();
        Person aspirant = createAspirant();
        person.addChild(aspirant);
        String message = "Generates an error message because aspirant is already a child of person.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments aspirantIsOlderThanPerson() {
        Person person = createPerson();
        Person aspirant = createAspirant();
        person.setYearOfBirth(Year.of(2000));
        aspirant.setYearOfBirth(Year.of(1000));
        String message = "Generates an error message because aspirant is older than person.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments aspirantIsYoungerThanPerson() {
        Person person = createPerson();
        person.setYearOfBirth(Year.of(2000));
        Person aspirant = createAspirant();
        aspirant.setYearOfBirth(Year.of(2001));
        String message = "Generates an error message because aspirant is younger than person.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments aspirantHasTwoOtherParents() {
        Person person = createPerson();
        Person aspirant = createAspirant();
        aspirant.addParent(createRelative1());
        aspirant.addParent(createRelative2());
        String message = "Generates an error message because aspirant has two other parents.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments aspirantIsInChildrenLineOfPerson() {
        Person person = createPerson();
        Person aspirant = createAspirant();
        Person childOfPerson = createRelative1();
        person.addChild(childOfPerson);
        childOfPerson.addChild(aspirant);
        String message = "Generates an error message because aspirant is in children's line of person.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments personIsInChildrenLineOfAspirant() {
        Person person = createPerson();
        Person aspirant = createAspirant();
        Person childOfAspirant = createRelative1();
        aspirant.addChild(childOfAspirant);
        childOfAspirant.addChild(person);
        String message = "Generates an error message because person is in children's line of aspirant.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments personIsChildOfAspirant() {
        Person person = createPerson();
        Person aspirant = createAspirant();
        aspirant.addChild(person);
        String message = "Generates an error message because person is already child of aspirant.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments personIsParentOfAspirant() {
        Person person = createPerson();
        Person aspirant = createAspirant();
        aspirant.addParent(person);
        String message = "Generates an error message because person is a parent of aspirant.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments personIsInParentLineOfAspirant() {
        Person person = createAspirant();
        Person aspirant = createPerson();
        Person parentOfAspirant = createRelative1();
        aspirant.addParent(parentOfAspirant);
        parentOfAspirant.addParent(person);
        String message = "Generates an error message because person is in the parent line of aspirant.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments aspirantIsInParentLineOfPerson() {
        Person person = createAspirant();
        Person aspirant = createPerson();
        Person parentOfPerson = createRelative1();
        person.addParent(parentOfPerson);
        parentOfPerson.addParent(aspirant);
        String message = "Generates an error message because aspirant is in the parent line of person.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments aspirantAndPersonHaveDifferentParents() {
        Person person = createPerson();
        Person aspirant = createAspirant();
        Person parentOfPerson1 = createRelative1();
        Person parentOfPerson2 = createRelative2();
        Person parentOfAspirant1 = createRelative3();
        Person parentOfAspirant2 = createRelative4();

        person.addParent(parentOfPerson1);
        person.addParent(parentOfPerson2);

        aspirant.addParent(parentOfAspirant1);
        aspirant.addParent(parentOfAspirant2);
        String message = "Generates an error message because aspirant and person have different parents.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments aspirantIsParentOfPersonsSpouse() {
        Person person = createPerson();
        Person aspirant = createAspirant();
        Person spouseOfPerson = createRelative1();
        person.addSpouse(spouseOfPerson);
        spouseOfPerson.addParent(aspirant);
        String message = "Generates an error message because aspirant is already a parent of persons spouse.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments aspirantIsSiblingOfPersonsParent() {
        Person person = createPerson();
        Person aspirant = createAspirant();
        Person parentOfPerson = createRelative1();
        person.addParent(parentOfPerson);
        parentOfPerson.addSibling(aspirant);
        String message = "Generates an error message because aspirant is already an uncle/an aunt of person.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments personHasTwoParents() {
        Person person = createPerson();
        Person aspirant = createAspirant();
        Person parentOfPerson = createRelative1();
        Person parentOfPerson2 = createRelative2();
        person.addParent(parentOfPerson);
        person.addParent(parentOfPerson2);
        String message = "Generates an error message because aspirant is already an uncle/an aunt of person.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments personHasAlreadyASpouse() {
        Person person = createPerson();
        Person aspirant = createAspirant();
        Person spouseOfPerson = createRelative1();
        person.addSpouse(spouseOfPerson);
        String message = "Generates an error message because person has already a spouse.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments aspirantHasAlreadyAnotherSpouse() {
        Person person = createPerson();
        Person aspirant = createAspirant();
        Person spouseOfAspirant = createRelative1();
        aspirant.addSpouse(spouseOfAspirant);
        String message = "Generates an error message because aspirant has already another spouse.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments aspirantAndPersonAreSiblings() {
        Person person = createPerson();
        Person aspirant = createAspirant();
        person.addSibling(aspirant);
        aspirant.addSibling(person);
        String message = "Generates an error message because aspirant and person are siblings.";

        return Arguments.of(aspirant, person, message);
    }

    // Arguments that causes NO error messages:

    static Arguments aspirantIsNotInChildrenLineOfPerson() {
        Person person = createPerson();
        Person aspirant = createAspirant();
        Person childOfPerson = createRelative1();
        person.addChild(childOfPerson);
        childOfPerson.addChild(createRelative2());
        String message = "Generates NO error message because aspirant is NOT in the children's line of person.";

        return Arguments.of(aspirant, person, message);
    }
    static Arguments aspirantIsNotInParentLineOfPerson() {
        Person person = createAspirant();
        Person aspirant = createPerson();
        Person parentOfPerson = createRelative1();
        person.addParent(parentOfPerson);
        parentOfPerson.addParent(createRelative2());
        String message = "Generates NO error message because aspirant is NOT in the parent line of person.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments personIsNotInParentLineOfAspirant() {
        Person person = createAspirant();
        Person aspirant = createPerson();
        Person parentOfAspirant = createRelative1();
        Person parentOfParentOfAspirant = createRelative2();
        aspirant.addParent(parentOfAspirant);
        parentOfAspirant.addParent(parentOfParentOfAspirant);
        parentOfParentOfAspirant.addParent(createRelative3());
        String message = "Generates NO error message because person is NOT in the parent line of aspirant.";

        return Arguments.of(aspirant, person, message);
    }

    static Arguments aspirantIsNotInChildrenLine() {
        Person person = createPerson();
        Person aspirant = createAspirant();
        Person childOfPerson = createRelative1();
        person.addChild(childOfPerson);
        childOfPerson.addChild(createRelative2());
        String message = "Generates NO error message because aspirant is NOT in the parent line of person.";

        return Arguments.of(aspirant, person, message);
    }
    static Arguments aspirantAndPersonHaveNoKinship() {
        Person person = createPerson();
        Person aspirant = createAspirant();
        String message = "Generates NO error message because aspirant and person have no relatives.";

        return Arguments.of(aspirant, person, message);
    }


    static Person createPerson() {
        return new Person(1, 1, "person", "", null);
    }

    static Person createAspirant() {
        return new Person(2, 2, "aspirant", "", null);
    }

    static Person createRelative1() {
        return new Person(3, 3, "relative1", "", null);
    }

    static Person createRelative2() {
        return new Person(4, 4, "relative2", "", null);
    }

    static Person createRelative3() {
        return new Person(5, 5, "relative3", "", null);
    }

    static Person createRelative4() {
        return new Person(6, 6, "relative4", "", null);
    }
}
