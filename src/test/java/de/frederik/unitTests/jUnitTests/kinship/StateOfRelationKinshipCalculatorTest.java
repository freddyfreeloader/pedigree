package de.frederik.unitTests.jUnitTests.kinship;

import de.pedigreeProject.kinship.StateOfRelation;
import de.pedigreeProject.kinship.StateOfRelationCalculator;
import de.pedigreeProject.model.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StateOfRelationKinshipCalculatorTest {
    static List<Person> testPersons;

    @BeforeAll
    static void createData() {
        testPersons = createTestPersons();
    }

    private static List<Person> createTestPersons() {
        int i = 0;
        Person me = new Person(++i, 1, "me", "", null);
        Person spouse = new Person(++i, 1, "spouse", "", null);
        Person sibling1 = new Person(++i, 1, "sibling1", "", null);
        Person sibling2 = new Person(++i, 1, "sibling2", "", null);
        Person noRelation1 = new Person(++i, 1, "noRelation1", "", null);

        me.addSibling(sibling1);
        me.addSpouse(spouse);
        sibling1.addSibling(me, sibling2);
        sibling2.addSibling(me, sibling1);
        spouse.addSpouse(me);

        return new ArrayList<>(List.of(me, spouse, sibling1, sibling2, noRelation1));
    }

    @ParameterizedTest(name = "[{index}] {0} should have state: {1}")
    @MethodSource("providePersonPositionCombination")
    @DisplayName("test state of relation")
    void test(Person person, StateOfRelation expectedPosition) {

        StateOfRelation actualPosition = new StateOfRelationCalculator().getState(person, testPersons);
        assertEquals(expectedPosition, actualPosition);
    }

    private static Stream<Arguments> providePersonPositionCombination() {

        return Stream.of(
                Arguments.of(testPersons.get(0), StateOfRelation.NEXT_IS_SPOUSE),
                Arguments.of(testPersons.get(1), StateOfRelation.BEFORE_IS_SIBLING_OF_NEXT),
                Arguments.of(testPersons.get(2), StateOfRelation.NEXT_IS_SIBLING),
                Arguments.of(testPersons.get(3), StateOfRelation.NEXT_IS_NO_RELATIVE),
                Arguments.of(testPersons.get(4), StateOfRelation.LAST_INDEX)
        );
    }
}