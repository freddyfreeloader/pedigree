package de.pedigreeProject.database;

import de.pedigreeProject.model.Pedigree;
import de.pedigreeProject.model.Person;
import org.jetbrains.annotations.NotNull;

import java.time.Year;
import java.util.List;
import java.util.Optional;
/**
 * <p>Offers CRUD-operations for {@link Person}.</p>
 * <ul>
 *    <li><b>createPerson</b>
 *       - inserts a new Person into database</li>
 *    <li><b>readPersons</b>
 *        - returns all persons of a pedigree of database</li>
 *    <li><b>updatePersonsData</b>
 *        - updates given name, family name, year of birth of person</li>
 *    <li><b>updateRelatives</b>
 *        - updates the Lists of relations of the person</li>
 *    <li><b>deletePerson</b>
 *        - remove person from database</li>
 * </ul>
 */
public interface PersonGateway {
    /**
     * <p>Inserts a new <code>Person</code> into the database.</p>
     * <p>Returns an {@code Optional<Person>} if insertion was successfully or an {@code Optional.empty()} if a person with same given name, same family name and same year of birth already exists in the database.</p>
     * <p>It throws an <code>IllegalArgumentException</code> if both given name and family name are null or blank.</p>
     * <p>It throws an <code>NullPointerException</code> if pedigree is null.</p>
     * <p> The year of birth may be null</p>
     * <p>Updates the time stamp of the pedigree the person belongs to.</p>
     *
     * @param pedigree the new person belongs to
     * @param givenName persons given name
     * @param familyName persons family name
     * @param yearOfBirth persons year of birth, may be null
     * @return {@code Optional<Person>} with new Person or empty Optional
     * @throws RuntimeException if database connection failed or sql statement fails
     * @throws IllegalArgumentException if both given name and family name are null/blank
     */
    Optional<Person> createPerson(Pedigree pedigree, String givenName, String familyName, Year yearOfBirth) ;
    /**
     * <p>Returns a {@code List<Person>} with all persons of given <code>Pedigree</code> or an empty List.</p>
     * <p>Each person is fully initialized with persons data and lists of relatives.</p>
     *
     * @param pedigree the persons belong to, not null
     * @return {@code List<Person>} or <code>Collections.emptyList</code>
     * @throws RuntimeException if database connection failed or sql statement fails
     * @throws NullPointerException if pedigree is null
     */
    List<Person> readPersons(Pedigree pedigree);

    /**
     * <p>Updates persons data in database.</p>
     * <p>Returns false if an entry with same given name, same family name and same year of birth already exists in database.</p>
     * <p>Throws <code>IllegalArgumentException</code> if given name and family name both are null or blank.</p>
     * <p>Updates the time stamp of the pedigree the person belongs to.</p>
     * @param person Person to update, must be not null
     * @param givenName given name to update, may be null
     * @param familyName family name to update, may be null
     * @param yearOfBirth year of Birth to update, may be null
     * @return <code>true</code> if update is successful or <code>false</code> if update failed
     * @throws RuntimeException if database connection failed or sql statement fails
     * @throws IllegalArgumentException if given name and family name both are null/blank
     * @throws NullPointerException if person is null
     */
    boolean updatePersonsData(@NotNull Person person, String givenName, String familyName, Year yearOfBirth);
    /**
     * <p>Updates the relatives of person into the database.</p>
     * @param person to update, may be null
     * @return {@code true} if updated successfully , {@code false} if person is null
     * @throws RuntimeException if database connection failed or sql statement fails
     */
    boolean updateRelatives(Person person);
    /**
     * <p>Deletes the person from database.</p>
     * <p>Returns <code>false</code> if person is null or person could not be deleted, otherwise <code>true</code>.</p>
     * <p>Updates the time stamp of persons pedigree.</p>
     * @param person to delete, may be null
     * @return <code>true</code> if successful, <code>false</code> if deleting fails or person is null
     * @throws RuntimeException if database connection failed or sql statement fails
     */
    boolean deletePerson(Person person);

}
