package de.pedigreeProject.database;

import de.pedigreeProject.model.Pedigree;
import de.pedigreeProject.model.Person;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * <p>Offers CRUD-operations for {@link Pedigree}.</p>
 * <ul>
 *    <li><b>createPedigree</b>
 *       - inserts a new Pedigree into database</li>
 *    <li><b>readPedigrees</b>
 *        - returns all pedigrees of database</li>
 *    <li><b>updatePedigree</b>
 *        - updates the pedigree in database</li>
 *    <li><b>deletePedigree</b>
 *        - deletes the pedigree and associated persons from database</li>
 * </ul>
 */
public interface PedigreeGateway {
    /**
     * <p>Inserts a new {@link Pedigree} in database with stripped title and stripped description and actual time stamp. </p>
     * <p>Returns an {@code Optional<Pedigree>} if insertion is successfully,<br>
     * returns an {@code Optional.empty()} if the title already exists in database.</p>
     * <p>The title of Pedigree must not be null or blank, otherwise an {@code IllegalArgumentException} will be thrown.</p>
     *
     * @param title       name of the pedigree, must not be null or empty
     * @param description additional description of the pedigree (optional), may be null
     * @return an  {@code Optional<Pedigree>}, or an {@code Optional.empty()} if title already exists
     * @throws RuntimeException if Connection is invalid or SQL statements fail
     * @throws IllegalArgumentException if title is null or blank
     *
     */
    Optional<Pedigree> createPedigree(@NotNull String title, String description);

    /**
     * Returns each pedigree of database.
     * @return a {@code List<Pedigree>} or a {@code Collections.emptyList}
     * @throws RuntimeException  if Connection is invalid or SQL statements fail
     */
    List<Pedigree> readPedigrees();

    /**
     * <p>Updates given Pedigree and database with new title and description.</p>
     * <p>Updates time stamp of pedigree to <code>now()</code></p>
     * <p>Returns <code>true</code> if the update was successful or <code>false</code> if the title of the pedigree already exists in database.</p>
     *
     * @param pedigree    Pedigree to update, must not be null
     * @param title       name of the pedigree, must not be null or empty
     * @param description additional description of the pedigree, may be null
     * @return <code>true</code>, if update was successful, <code>false</code>, if title already exists
     * @throws RuntimeException  if Connection is invalid or SQL statements fail
     * @throws IllegalArgumentException if title is null or empty
     * @throws NullPointerException if pedigree is null
     */
    boolean updatePedigree(@NotNull Pedigree pedigree, @NotNull String title, String description);

    /**
     * <p>Deletes given Pedigree and every associated {@link Person} from database</p>
     * @param pedigree the pedigree to delete, may be null
     * @throws RuntimeException  if Connection is invalid or SQL statements fail
     */
    void deletePedigree(Pedigree pedigree);
}
