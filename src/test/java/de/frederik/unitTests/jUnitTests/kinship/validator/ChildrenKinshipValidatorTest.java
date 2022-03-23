package de.frederik.unitTests.jUnitTests.kinship.validator;

import de.pedigreeProject.model.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChildrenKinshipValidatorTest extends ValidatorBase {

    @ParameterizedTest(name = "{2}")
    @MethodSource("aspirantCouldBeNotAChild")
    @DisplayName("CHILD: test if aspirant could be a child of person")
    void test_if_aspirant_could_be_a_CHILD_of_person(Person aspirant, Person person, String message) {
        assertTrue(kinshipValidator.aspirantCouldBeChildOfPerson(aspirant, person).isPresent(), message);
    }

    private static Stream<Arguments> aspirantCouldBeNotAChild() {

        return Stream.of(
                aspirantIsParentOfPerson(),
                aspirantIsInParentLineOfPerson(),
                aspirantIsSpouseOfPerson(),
                aspirantIsSiblingOfPerson(),
                aspirantIsChildOfPerson(),
                aspirantIsInChildrenLineOfPerson(),
                aspirantIsOlderThanPerson(),
                aspirantHasTwoOtherParents(),
                aspirantEqualsPerson()
        );
    }

    @ParameterizedTest(name = "{2}")
    @MethodSource("aspirantAllowedToBecomeAChild")
    @DisplayName("CHILD: Aspirant is ALLOWED to become a child of person")
    void test_if_aspirant_is_allowed_to_become_Child_of_person(Person aspirant, Person person, String message) {
        assertFalse(kinshipValidator.aspirantCouldBeChildOfPerson(aspirant, person).isPresent(), message);
    }

    public static Stream<Arguments> aspirantAllowedToBecomeAChild() {

        return Stream.of(
                aspirantAndPersonHaveNoKinship()
        );
    }
}