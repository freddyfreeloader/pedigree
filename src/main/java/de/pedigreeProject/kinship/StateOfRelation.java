package de.pedigreeProject.kinship;

/**
 * Represents the state of relationship of a person in an ordered List to its neighbours in the List.<br>
 * The {@link #spacing} determinate the distance to the next person.
 */
public enum StateOfRelation {
    NEXT_IS_SIBLING(5.0),
    NEXT_IS_SPOUSE(0.0),
    BEFORE_IS_SIBLING_OF_NEXT(10.0),
    NEXT_IS_NO_RELATIVE(30.0),
    LAST_INDEX(0.0);

    /**
     * The distance to the next person
     */
    public final double spacing;

    StateOfRelation(double spacing) {
        this.spacing = spacing;
    }
}
