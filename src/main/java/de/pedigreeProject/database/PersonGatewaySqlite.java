package de.pedigreeProject.database;

import de.pedigreeProject.model.Pedigree;
import de.pedigreeProject.model.Person;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.*;

/**
 * @see PersonGateway
 */
public class PersonGatewaySqlite extends Gateway implements PersonGateway {

    /**
     * Constructs a new PersonGatewaySqlite.
     *
     * @param connection the Connection object to database
     * @throws RuntimeException if some sql statements or database connection fails
     * @see PersonGateway
     */
    public PersonGatewaySqlite(Connection connection) {
        super(connection);
    }

    /**
     * @see PersonGateway#createPerson
     */
    @Override
    public Optional<Person> createPerson(@NotNull final Pedigree pedigree, final String givenName, final String familyName, final Year yearOfBirth) {
        Objects.requireNonNull(pedigree);
        if (StringUtils.isAllBlank(givenName, familyName)) {
            throw new IllegalArgumentException("givenName and familyName are null or blank");
        }
        Person person = null;

        String strippedGivenName = givenName == null ? "" : givenName.strip();
        String strippedFamilyName = familyName == null ? "" : familyName.strip();

        String sql = "INSERT INTO " + PERSONS + " (pedigreeId, givenName, familyName, yearOfBirth) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, pedigree.getId());
            stmt.setString(2, strippedGivenName);
            stmt.setString(3, strippedFamilyName);
            stmt.setString(4, yearOfBirth == null ? "" : yearOfBirth.toString());

            stmt.executeUpdate();

            ResultSet resultSet = stmt.getGeneratedKeys();

            if (resultSet.next()) {

                int id = resultSet.getInt(1);

                person = new Person(id, pedigree.getId(), strippedGivenName, strippedFamilyName, yearOfBirth);

                updateTimeStampOfPedigree(person);
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 2067 || e.getErrorCode() == 19) { // Unique constrains failed
                return Optional.empty();
            }else {
                throw new RuntimeException(e);
            }
        }
        return Optional.ofNullable(person);
    }

    /**
     * @see PersonGateway#readPersons
     */
    @Override
    public List<Person> readPersons(@NotNull final Pedigree pedigree) {
        Objects.requireNonNull(pedigree);
        try {
            List<Person> persons = readPersonsData(pedigree);

            if (persons.isEmpty()) {
                return Collections.emptyList();
            }
            addRelativesToEachPerson(persons);
            return persons;
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Person> readPersonsData(final Pedigree pedigree) {

        List<Person> persons = new ArrayList<>();

        String sql = """
                SELECT personId, givenName, familyName, yearOfBirth
                FROM""" + " " + PERSONS + " " + """
                WHERE pedigreeId=""" + pedigree.getId();

        try (Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {

                int id = rs.getInt(1);
                String givenName = rs.getString(2);
                String familyName = rs.getString(3);
                String year = rs.getString(4);
                Year yearOfBirth = Objects.equals(year, "") ? null : Year.parse(year);

                persons.add(new Person(id, pedigree.getId(), givenName, familyName, yearOfBirth));
            }
            return persons;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void addRelativesToEachPerson(final List<Person> persons) throws SQLException {
        Objects.requireNonNull(persons);
        for (Person person : persons) {
            String sqlCompact = """
                    SELECT DISTINCT
                    spouses.spouseId,
                    siblings.siblingId,
                    parents.parentId,
                    children.childId
                    FROM persons
                    LEFT JOIN spouses ON spouses.personId=persons.personId
                    LEFT JOIN siblings ON siblings.personId=persons.personId
                    LEFT JOIN parents ON Parents.personId=persons.personId
                    LEFT JOIN children ON children.personId=persons.personId
                    WHERE
                    persons.personId=""" + person.getId();

            try (Statement stmt = conn.createStatement()) {

                ResultSet rs = stmt.executeQuery(sqlCompact);

                while (rs.next()) {

                    getPersonById(rs.getInt(1), persons).ifPresent(person.getSpouses()::add);
                    getPersonById(rs.getInt(2), persons).ifPresent(person.getSiblings()::add);
                    getPersonById(rs.getInt(3), persons).ifPresent(person.getParents()::add);
                    getPersonById(rs.getInt(4), persons).ifPresent(person.getChildren()::add);
                }
            }
        }
    }

    /**
     * @see PersonGateway#updatePersonsData
     */
    @Override
    public boolean updatePersonsData(@NotNull Person person, String givenName, String familyName, Year yearOfBirth) {
        Objects.requireNonNull(person);

        if (StringUtils.isAllBlank(givenName, familyName)) {
            throw new IllegalArgumentException("given name and family name both are null or blank!");
        }

        String strippedGivenName = givenName.strip();
        String strippedFamilyName = familyName.strip();

        String sql = "UPDATE " + PERSONS + " SET givenName=?, familyName=?, yearOfBirth=? WHERE personId=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, strippedGivenName);
            stmt.setString(2, strippedFamilyName);
            stmt.setString(3, yearOfBirth == null ? "" : yearOfBirth.toString());
            stmt.setInt(4, person.getId());

            stmt.executeUpdate();

            person.updateNameAndYear(strippedGivenName, strippedFamilyName, yearOfBirth);
            updateTimeStampOfPedigree(person);

            return true;
        } catch (SQLException e) {
            if (e.getErrorCode() == 2067 || e.getErrorCode() == 19) {// Unique constrains failed
                return false;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * @see PersonGateway#updateRelatives
     */
    @Override
    public boolean updateRelatives(final Person person) {
        if (person == null) {
            return false;
        }
        int id = person.getId();

        try (Statement stmt = conn.createStatement()) {

            stmt.addBatch("PRAGMA foreign_keys = ON;");
            stmt.addBatch("BEGIN;");

            stmt.addBatch("DELETE FROM " + CHILDREN + " WHERE  personId=" + id);
            stmt.addBatch("DELETE FROM " + PARENTS + " WHERE  personId=" + id);
            stmt.addBatch("DELETE FROM " + SPOUSES + " WHERE  personId=" + id);
            stmt.addBatch("DELETE FROM " + SIBLINGS + " WHERE  personId=" + id);


            for (Person sibling : person.getSiblings()) {
                stmt.addBatch("INSERT INTO " + SIBLINGS + " (personId, siblingId) VALUES (" + id + ", " + sibling.getId() + ")");
            }
            for (Person parent : person.getParents()) {
                stmt.addBatch("INSERT INTO " + PARENTS + " (personId, parentId) VALUES (" + id + ", " + parent.getId() + ")");
            }
            for (Person spouse : person.getSpouses()) {
                stmt.addBatch("INSERT INTO " + SPOUSES + " (personId, spouseId) VALUES (" + id + ", " + spouse.getId() + ")");
            }
            for (Person child : person.getChildren()) {
                stmt.addBatch("INSERT INTO " + CHILDREN + " (personId, childId) VALUES (" + id + ", " + child.getId() + ")");
            }

            stmt.addBatch("COMMIT;");
            stmt.executeBatch();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see PersonGateway#deletePerson
     */
    @Override
    public boolean deletePerson(final Person person) {
        if (person == null) {
            return false;
        }
        String sql = "DELETE FROM " + PERSONS + " WHERE personId=?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, person.getId());

            int rowCount = stmt.executeUpdate();

            updateTimeStampOfPedigree(person);

            if (rowCount > 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private void updateTimeStampOfPedigree(final Person person) throws SQLException {
        Objects.requireNonNull(person);

        String sql = "UPDATE " + PEDIGREES + " SET timeStamp=? WHERE pedigreeId=?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, LocalDateTime.now().toString());
            stmt.setInt(2, person.getPedigreeId());

            stmt.executeUpdate();
        }
    }
}
