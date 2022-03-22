package de.frederik.unitTests.jUnitTests.kinship.validator;

import de.pedigreeProject.model.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParentKinshipValidatorTest extends ValidatorBase {

    @ParameterizedTest(name = "{2}")
    @MethodSource("aspirantCouldBeNotAParent")
    @DisplayName("PARENT: test if aspirant could be a parent of person")
    void test_if_aspirant_could_be_a_PARENT_of_person(Person aspirant, Person person, String message) {
        assertTrue(kinshipValidator.aspirantCouldBeParentOfPerson(aspirant, person).isPresent(), message);
    }

    private static Stream<Arguments> aspirantCouldBeNotAParent() {

        return Stream.of(
                aspirantEqualsPerson(),
                aspirantIsParentOfPersonsSpouse(),
                aspirantIsSiblingOfPersonsParent(),
                aspirantIsChildOfPerson(),
                aspirantIsInChildrenLineOfPerson(),
                personIsParentOfAspirant(),
                aspirantIsParentOfPerson(),
                personHasTwoParents(),
                aspirantIsSiblingOfPerson(),
                aspirantIsSpouseOfPerson(),
                aspirantIsYoungerThanPerson()
        );
    }

    @ParameterizedTest(name = "{2}")
    @MethodSource("aspirantAllowedToBecomeAParent")
    @DisplayName("PARENT: Aspirant is ALLOWED to become a parent of person")
    void test_if_aspirant_is_allowed_to_become_PARENT_of_person(Person aspirant, Person person, String message) {
        assertFalse(kinshipValidator.aspirantCouldBeParentOfPerson(aspirant, person).isPresent(), message);
    }

    private static Stream<Arguments> aspirantAllowedToBecomeAParent() {

        return Stream.of(
                aspirantAndPersonHaveNoKinship(),
                personIsNotInParentLineOfAspirant()
        );
    }
}