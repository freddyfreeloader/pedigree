package de.pedigreeProject.kinship;

import de.pedigreeProject.model.Person;

import java.util.List;

public interface KinshipUpdater {
    /**
     * <p>Updates the lists of relatives of this person and every person, who is affected by the update.</p>
     * <p>If person is null, method returns immediately without changes. </p>
     * <p> Updates:</p>
     * <ul>
     *     <li>the lists of this person
     *     <ul>
     *         <li>list of parents</li>
     *         <li>list of spouses</li>
     *         <li>list of siblings</li>
     *         <li>list of children</li>
     *     </ul>
     *     </li>
     *     <li>the lists of the members of the person lists</li>
     *     <li>the lists of persons whose are transitive affected by the updates
     *     <ul>
     *         <li>all children of persons parent are siblings of person</li>
     *         <li>all children of person are siblings</li>
     *         <li>each child of parent should have that parent</li>
     *     </ul>
     *     </li>
     * </ul>
     *
     * @param person   person to update, may be null
     * @param parents  replaces persons parents list
     * @param children replaces persons children list
     * @param spouses  replaces persons spouses list
     * @param siblings replaces persons siblings list
     */
    void updateKinship(Person person, List<Person> parents, List<Person> children, List<Person> spouses, List<Person> siblings);
}
