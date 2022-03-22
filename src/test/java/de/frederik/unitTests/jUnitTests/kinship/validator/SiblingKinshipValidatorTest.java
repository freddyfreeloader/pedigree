package de.frederik.unitTests.jUnitTests.kinship.validator;

import de.pedigreeProject.model.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SiblingKinshipValidatorTest extends ValidatorBase {

    @ParameterizedTest(name = "{index} {2}")
    @MethodSource("aspirantCouldBeNotASibling")
    @DisplayName("SIBLING: Aspirant should NOT become a sibling of person")

    void test_if_aspirant_could_be_a_SIBLING_of_person(Person aspirant, Person person, String message) {
        assertTrue(kinshipValidator.aspirantCouldBeSiblingOfPerson(aspirant, person).isPresent(), message);
    }

    public static Stream<Arguments> aspirantCouldBeNotASibling() {

        return Stream.of(
                aspirantEqualsPerson(),
                aspirantIsSpouseOfPerson(),
                aspirantIsSiblingOfPerson(),
                aspirantIsInChildrenLineOfPerson(),
                personIsInChildrenLineOfAspirant(),
                personIsChildOfAspirant(),
                personIsParentOfAspirant(),
                personIsInParentLineOfAspirant(),
                aspirantIsInParentLineOfPerson(),
                aspirantAndPersonHaveDifferentParents()
        );
    }

    @ParameterizedTest(name = "{index} {2}")
    @MethodSource("aspirantAllowedToBecomeASibling")
    @DisplayName("SIBLING: Aspirant is ALLOWED to become a sibling of person")

    void test_if_aspirant_is_allowed_to_become_SIBLING_of_person(Person aspirant, Person person, String message) {
        assertFalse(kinshipValidator.aspirantCouldBeSiblingOfPerson(aspirant, person).isPresent(), message);
    }

    public static Stream<Arguments> aspirantAllowedToBecomeASibling() {

        return Stream.of(
                aspirantAndPersonHaveNoKinship(),
                aspirantIsNotInChildrenLineOfPerson(),
                aspirantIsNotInParentLineOfPerson()
        );
    }
}