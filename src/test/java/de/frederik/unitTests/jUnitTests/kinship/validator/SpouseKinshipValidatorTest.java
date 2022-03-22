package de.frederik.unitTests.jUnitTests.kinship.validator;

import de.pedigreeProject.model.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpouseKinshipValidatorTest extends ValidatorBase {

    @ParameterizedTest(name = "{2}")
    @MethodSource("aspirantCouldBeNotASpouse")
    @DisplayName("SPOUSE: test if aspirant could be a spouse of person")
    void test_if_aspirant_could_be_a_SPOUSE_of_person(Person aspirant, Person person, String message) {
        assertTrue(kinshipValidator.aspirantCouldBeSpouseOfPerson(aspirant, person).isPresent(), message);
    }

    public static Stream<Arguments> aspirantCouldBeNotASpouse() {

        return Stream.of(
                aspirantEqualsPerson(),
                aspirantIsSpouseOfPerson(),
                aspirantIsChildOfPerson(),
                aspirantIsInChildrenLineOfPerson(),
                personIsChildOfAspirant(),
                personIsInChildrenLineOfAspirant(),
                personIsParentOfAspirant(),
                personIsInParentLineOfAspirant(),
                aspirantIsInParentLineOfPerson(),
                personHasAlreadyASpouse(),
                aspirantHasAlreadyAnotherSpouse(),
                aspirantAndPersonAreSiblings()
        );
    }

    @ParameterizedTest(name = "{2}")
    @MethodSource("aspirantAllowedToBecomeASpouse")
    @DisplayName("SPOUSE: Aspirant is ALLOWED to become a spouse of person")
    void test_if_aspirant_is_allowed_to_become_SPOUSE_of_person(Person aspirant, Person person, String message) {
        assertFalse(kinshipValidator.aspirantCouldBeSpouseOfPerson(aspirant, person).isPresent(), message);
    }

    public static Stream<Arguments> aspirantAllowedToBecomeASpouse() {

        return Stream.of(
                aspirantAndPersonHaveNoKinship(),
                aspirantIsNotInParentLineOfPerson(),
                aspirantIsNotInChildrenLine()
        );
    }
}