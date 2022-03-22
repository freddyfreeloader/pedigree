package de.pedigreeProject.kinship;

import de.pedigreeProject.model.Person;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class provides methods to validate if a relationship of two persons are allowed.<br>
 * Note:<br>
 * <p>The rules are actually muddled up for different reasons:<br>
 * <ul>
 *     <li>logic (a child could not be older than its parent)</li>
 *     <li>genetic (siblings must have a common parent)</li>
 *     <li>local civil right (only one spouse are allowed)</li>
 *
 * </ul></p>
 * Civil right could differ from countries or could be changed,<br>
 * illogical or genetic impossible kinship could be valid for a pedigree of greek gods for example.<br>
 * So future implementations should let choose the rules by the user.
 */
public class StrongKinshipValidator implements KinshipValidator {
    public StrongKinshipValidator() {
    }
    final ResourceBundle bundle = ResourceBundle.getBundle("kinshipValidator", Locale.getDefault());
    /**
     * Validates if the given aspirant could be a child of the given person.<br>
     * It generates a message why the relationship is not possible within an {@code Optional<String>}.<br>
     * The Optional is empty if no rules are violated and the relationship is allowed.
     *
     * @param aspirant the person who wants to be a child of the person
     * @param person   the person  the relationship is asked for
     * @return an {@code Optional<String} with a message or an {@code Optional.empty()}
     */
    @Override
    public Optional<String> aspirantCouldBeChildOfPerson(Person aspirant, Person person) {
        String message = null;
        initBooleans(aspirant, person);

        if (isSame) {
            message = (MessageFormat.format(bundle.getString("child.of.himself"), aspirant));
        } else if (aspirantIsParentOfPerson) {
            message = MessageFormat.format(bundle.getString("simultan.child.and.parent"), aspirant);
        } else if (aspirantIsSpouseOfPerson) {
            message = MessageFormat.format(bundle.getString("simultan.child.and.spouse"), aspirant);
        } else if (aspirantIsSiblingOfPerson) {
            message = MessageFormat.format(bundle.getString("simultan.child.and.sibling"), aspirant);
        } else if (aspirantIsChildOfPerson) {
            message = MessageFormat.format(bundle.getString("is.already.child"), aspirant);
        } else if (aspirantIsOlderThanPerson) {
            message = MessageFormat.format(bundle.getString("is.older.than"), aspirant,person);
        }
        if (aspirantHasTwoParents && !personIsParentOfAspirant) {
            message = MessageFormat.format(bundle.getString("has.two.other.parents"), aspirant);
        }
        return Optional.ofNullable(message);
    }

    /**
     * Validates if the given aspirant could be a parent of the given person.<br>
     * It generates a message why the relationship is not possible within an {@code Optional<String>}.<br>
     * The Optional is empty if no rules are violated and the relationship is allowed.
     *
     * @param aspirant the person who wants to be a parent of the person
     * @param person   the person  the relationship is asked for
     * @return an {@code Optional<String} with a message or an {@code Optional.empty()}
     */
    @Override
    public Optional<String> aspirantCouldBeParentOfPerson(Person aspirant, Person person) {
        String message = null;
        initBooleans(aspirant, person);

        if (isSame) {
            message = (MessageFormat.format(bundle.getString("is.parent.of.himself"), aspirant));
        } else if (aspirantIsParentOfPersonsSpouse) {
            message = MessageFormat.format(bundle.getString("is.already.parent.of.spouse"), aspirant);
        } else if (aspirantIsSiblingOfPersonsSpouse) {
            message = MessageFormat.format(bundle.getString("is.already.uncle"), aspirant);
        } else if (aspirantIsInChildrenLineOfPerson) {
            message = bundle.getString("is.in.children.line");
        } else if (aspirantIsParentOfPerson) {
            message = MessageFormat.format(bundle.getString("is.already.parent"), aspirant);
        } else if (personIsInParentLineOfAspirant) {
            message = MessageFormat.format(bundle.getString("is.in.parental.line"), person);
        } else if (personHasTwoParents) {
            message = bundle.getString("two.parents.already");
        } else if (aspirantIsSiblingOfPerson) {
            message = bundle.getString("is.already.sibling");
        } else if (aspirantIsSpouseOfPerson) {
            message = bundle.getString("is.already.spouse");
        } else if (aspirantIsYoungerThanPerson) {
                message = MessageFormat.format(bundle.getString("is.younger.than"), aspirant,person);
        }
        return Optional.ofNullable(message);
    }

    /**
     * Validates if the given aspirant could be a spouse of the given person.<br>
     * It generates a message why the relationship is not possible within an {@code Optional<String>}.<br>
     * The Optional is empty if no rules are violated and the relationship is allowed.
     *
     * @param aspirant the person who wants to be a spouse of the person
     * @param person   the person  the relationship is asked for
     * @return an {@code Optional<String} with a message or an {@code Optional.empty()}
     */
    @Override
    public Optional<String> aspirantCouldBeSpouseOfPerson(Person aspirant, Person person) {
        String message = null;
        initBooleans(aspirant, person);

        if (aspirantIsParentOfPerson || personIsParentOfAspirant) {
            message = MessageFormat.format(bundle.getString("is.already.parent"), aspirant);
        } else if (aspirantIsInChildrenLineOfPerson || personIsInChildrenLineOfAspirant) {
            message = MessageFormat.format(bundle.getString("is.in.children.line"), aspirant);
        } else if (aspirantIsInParentLineOfPerson || personIsInParentLineOfAspirant) {
            message = MessageFormat.format(bundle.getString("is.in.parental.line"), aspirant);
        } else if (isSame) {
            message = bundle.getString("is.already.listed");
        } else if (personHasSpouse) {
            message = bundle.getString("only.one.spouse.allowed");
        } else if (aspirantHasSpouse && !personIsSpouseOfAspirant) {
            message = MessageFormat.format(bundle.getString("0.is.already.married.with.1"), aspirant,aspirant.getSpouses());
        } else if (personIsSiblingOfAspirant || aspirantIsSiblingOfPerson) {
            message = MessageFormat.format(bundle.getString("is.already.sibling"), aspirant);
        }
        return Optional.ofNullable(message);
    }

    /**
     * Validates if the given aspirant could be a sibling of the given person.<br>
     * It generates a message why the relationship is not possible within an {@code Optional<String>}.<br>
     * The Optional is empty if no rules are violated and the relationship is allowed.
     *
     * @param aspirant the person who wants to be a sibling of the person
     * @param person   the person  the relationship is asked for
     * @return an {@code Optional<String} with a message or an {@code Optional.empty()}
     */
    @Override
    public Optional<String> aspirantCouldBeSiblingOfPerson(Person aspirant, Person person) {
        String message = null;
        initBooleans(aspirant, person);

        if (isSame) {
            message = MessageFormat.format(bundle.getString("cant.be.sibling.of.himself"), aspirant);
        } else if (personIsSpouseOfAspirant || aspirantIsSpouseOfPerson) {
            message = MessageFormat.format(bundle.getString("is.already.spouse"), aspirant);
        } else if (aspirantIsSiblingOfPerson) {
            message = MessageFormat.format(bundle.getString("is.already.sibling"), aspirant);
        } else if (aspirantIsInChildrenLineOfPerson || personIsInChildrenLineOfAspirant) {
            message = MessageFormat.format(bundle.getString("is.in.children.line"), aspirant);
        } else if (aspirantIsInParentLineOfPerson || personIsInParentLineOfAspirant) {
            message = MessageFormat.format(bundle.getString("is.in.parental.line"), aspirant);
        } else if (aspirantHasTwoParents && personHasTwoParents && haveDifferentParents) {
            message = bundle.getString("have.both.another.parents");
        }
        return Optional.ofNullable(message);
    }
    
    private boolean isSame;
    
    private boolean aspirantIsParentOfPerson;
    private boolean personIsParentOfAspirant;
    private boolean aspirantIsSpouseOfPerson;
    private boolean personIsSpouseOfAspirant;
    private boolean aspirantIsSiblingOfPerson;
    private boolean personIsSiblingOfAspirant;
    private boolean aspirantIsChildOfPerson;
    
    private boolean aspirantIsOlderThanPerson;
    private boolean aspirantIsYoungerThanPerson;
    
    private boolean aspirantHasTwoParents;
    private boolean personHasTwoParents;
    private boolean haveDifferentParents;

    private boolean aspirantIsParentOfPersonsSpouse;
    private boolean aspirantIsSiblingOfPersonsSpouse;
    private boolean personHasSpouse;
    private boolean aspirantHasSpouse;

    private boolean aspirantIsInChildrenLineOfPerson;
    private boolean personIsInChildrenLineOfAspirant;
    private boolean aspirantIsInParentLineOfPerson;
    private boolean personIsInParentLineOfAspirant;

    // it would be cheaper to compute conditions of relations on demand (per Predicate or method),
    // but this way it is more readable.
    private void initBooleans(Person aspirant, Person person) {
        isSame = aspirant == person || aspirant.getId() == person.getId();

        aspirantIsParentOfPerson = person.getParents().contains(aspirant);
        personIsParentOfAspirant = aspirant.getParents().contains(person);
        aspirantIsSpouseOfPerson = person.getSpouses().contains(aspirant);
        personIsSpouseOfAspirant = aspirant.getSpouses().contains(person);
        aspirantIsSiblingOfPerson = person.getSiblings().contains(aspirant);
        personIsSiblingOfAspirant = aspirant.getSiblings().contains(person);
        aspirantIsChildOfPerson = person.getChildren().contains(aspirant);

        aspirantIsOlderThanPerson =
                aspirant.getYearOfBirth().isPresent() &&
                        person.getYearOfBirth().isPresent() &&
                        aspirant.getYearOfBirth().get().isBefore(person.getYearOfBirth().get());
        aspirantIsYoungerThanPerson =
                aspirant.getYearOfBirth().isPresent() &&
                        person.getYearOfBirth().isPresent() &&
                        aspirant.getYearOfBirth().get().isAfter(person.getYearOfBirth().get());

        aspirantHasTwoParents = aspirant.getParents().size() == 2;
        personHasTwoParents = person.getParents().size() == 2;
        haveDifferentParents = aspirant.getParents().stream().noneMatch(aspirantsParent -> person.getParents().contains(aspirantsParent));

        aspirantIsParentOfPersonsSpouse = person.getSpouses().stream().map(Person::getParents).anyMatch(par -> par.contains(aspirant));
        aspirantIsSiblingOfPersonsSpouse = person.getParents().stream().map(Person::getSiblings).anyMatch(sib -> sib.contains(aspirant));
        personHasSpouse = person.getSpouses().size() > 0;
        aspirantHasSpouse = aspirant.getSpouses().size() > 0;

        aspirantIsInChildrenLineOfPerson = isInChildrenLine(aspirant, person);
        personIsInChildrenLineOfAspirant = isInChildrenLine(person, aspirant);
        aspirantIsInParentLineOfPerson = isInParentLine(aspirant, person);
        personIsInParentLineOfAspirant = isInParentLine(person, aspirant);
    }

    private boolean isInParentLine(Person personToSearchFor, Person personWithParentLine) {
        if (personWithParentLine.getParents().contains(personToSearchFor)) {
            return true;
        }
        if (!personWithParentLine.getParents().isEmpty()) {
            for (Person parent : personWithParentLine.getParents()) {
                if (isInParentLine(personToSearchFor, parent)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isInChildrenLine(Person personToSearchFor, Person personWithChildrenLine) {
        if (personWithChildrenLine.getChildren().contains(personToSearchFor)) {
            return true;
        }
        if (!personWithChildrenLine.getChildren().isEmpty()) {
            for (Person child : personWithChildrenLine.getChildren()) {
                if (isInChildrenLine(personToSearchFor, child)) {
                    return true;
                }
            }
        }
        return false;
    }
}
