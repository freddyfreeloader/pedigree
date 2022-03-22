package de.frederik.testUtils.testData.csvTestData;

import de.pedigreeProject.model.Person;

import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

abstract class TestCSV_Factory {

    BiPredicate<Person, Person> isEqual = Person::equals;
    BiPredicate<Person, Person> aspirantIsParentOfPerson = (p1, p2) -> p2.getParents().contains(p1);
    BiPredicate<Person, Person> aspirantIsSpouseOfPerson = (p1, p2) -> p2.getSpouses().contains(p1);
    BiPredicate<Person, Person> aspirantIsChildOfPerson = (p1, p2) -> p2.getChildren().contains(p1);
    BiPredicate<Person, Person> hasOneCommonParent = (p1, p2) -> p1.getParents().stream().anyMatch(parent -> p2.getParents().contains(parent));
    BiPredicate<Person, Person> isSibling = (p1, p2) -> p2.getSiblings().contains(p1) || hasOneCommonParent.test(p1, p2);
    BiPredicate<Person, Person> aspirantIsOlderThanPerson = (p1, p2) -> p1.getYearOfBirth().isPresent() && p2.getYearOfBirth().isPresent() && p1.getYearOfBirth().get().isBefore(p2.getYearOfBirth().get());
    BiPredicate<Person, Person> aspirantIsYoungerThanPerson = (p1, p2) -> p1.getYearOfBirth().isPresent() && p2.getYearOfBirth().isPresent() && p1.getYearOfBirth().get().isAfter(p2.getYearOfBirth().get());
    BiPredicate<Person, Person> isSiblingOfParent = (p1, p2) -> p2.getParents().stream().map(Person::getSiblings).anyMatch(sib -> sib.contains(p1));
    BiPredicate<Person, Person> personsSpouseHasAspirantAsParent = (p1, p2) -> p2.getSpouses().stream().map(Person::getParents).anyMatch(par -> par.contains(p1));
    Predicate<Person> hasTwoParents = (p1) -> p1.getParents().size() == 2;
    Predicate<Person> isMarried = (p1) -> p1.getSpouses().size() > 0;
    BiPredicate<Person, Person> aspirantIsInParentalLineOfPerson = this::aspirantIsInParentalLineOfPerson;
    BiPredicate<Person, Person> aspirantIsInChildrenLineOfPerson = this::aspirantIsInChildrenLineOfPerson;


    private boolean aspirantIsInParentalLineOfPerson(Person aspirant, Person person) {
        Set<Person> set = person.getParents();
        if (set.contains(aspirant)) {
            return true;
        }
        if (!set.isEmpty()) {

            set.forEach(par -> aspirantIsInParentalLineOfPerson(aspirant, par));
        }
        return false;
    }

    private boolean aspirantIsInChildrenLineOfPerson(Person aspirant, Person person) {
        Set<Person> set = person.getChildren();
        if (set.contains(aspirant)) {
            return true;
        }
        if (!set.isEmpty()) {

            set.forEach(child -> aspirantIsInChildrenLineOfPerson(aspirant, child));
        }
        return false;
    }

    abstract List<String[]>  getStringArray();
}
