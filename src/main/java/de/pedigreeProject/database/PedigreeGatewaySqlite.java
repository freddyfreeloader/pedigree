package de.pedigreeProject.database;

import de.pedigreeProject.model.Pedigree;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Implementation of {@link PedigreeGateway }.
 */
public class PedigreeGatewaySqlite extends Gateway implements PedigreeGateway {
    /**
     * Constructs a new PedigreeGatewaySqlite.
     *
     * @param connection the Connection object to database
     * @throws RuntimeException if some sql statements or database connection fails
     * @see PedigreeGateway
     */
    public PedigreeGatewaySqlite(Connection connection)  {
        super(connection);
    }


    private boolean titleAlreadyExists(String title) throws SQLException {
        String sql = "SELECT * from " + PEDIGREES + " WHERE title=?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return true;
            }
        } return false;
    }


    /**
     * @see PedigreeGateway#createPedigree
     */
    @Override
    public Optional<Pedigree> createPedigree(final @NotNull String title, final String description) {
        if (StringUtils.isBlank(title)) {
            throw new IllegalArgumentException("title is null or blank!");
        }

        Pedigree pedigree = null;
        String strippedTitle = title.strip();
        String strippedDescription = description.strip();

        String insertNewPedigree = "INSERT INTO " + PEDIGREES + " (title, description, timeStamp) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(insertNewPedigree, Statement.RETURN_GENERATED_KEYS)) {
            if (titleAlreadyExists(title)) {
                return Optional.empty();
            }
            Thread.sleep(1); // important! ensure different time stamps
            LocalDateTime timeStamp = LocalDateTime.now();

            stmt.setString(1, strippedTitle);
            stmt.setString(2, strippedDescription);
            stmt.setString(3, timeStamp.toString());

            stmt.executeUpdate();

            ResultSet resultSet = stmt.getGeneratedKeys();
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                pedigree = new Pedigree(id, strippedTitle, strippedDescription, timeStamp);

            }

        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(pedigree);
    }

    /**
     * @see PedigreeGateway#readPedigrees()
     */
    @Override
    public List<Pedigree> readPedigrees() {

        String sql = "SELECT pedigreeId, title, description, timeStamp FROM " + PEDIGREES;

        try (Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);

            List<Pedigree> pedigrees = new ArrayList<>();
            while (rs.next()) {
                pedigrees.add(new Pedigree(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        LocalDateTime.parse(rs.getString(4))));
            }
            if (pedigrees.isEmpty()) {
                return Collections.emptyList();
            }
            return pedigrees;
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see PedigreeGateway#updatePedigree
     */
    @Override
    public boolean updatePedigree(@NotNull Pedigree pedigree, @NotNull String title, String description) {
        Objects.requireNonNull(pedigree);
        if (StringUtils.isBlank(title)) {
            throw new IllegalArgumentException("title must not be null or blank");
        }
        String strippedTitle = title.strip();
        String strippedDescription = description.strip();
        String sql = "UPDATE " + PEDIGREES + " SET title=?, description=?, timeStamp=? WHERE pedigreeId=?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (!pedigree.getTitle().equals(strippedTitle) && titleAlreadyExists(title)) {
                return false;
            }
            Thread.sleep(1); // important for repeated test to ensure different time stamps
            LocalDateTime timeStamp = LocalDateTime.now();
            stmt.setString(1, strippedTitle);
            stmt.setString(2, strippedDescription);
            stmt.setString(3, timeStamp.toString());
            stmt.setInt(4, pedigree.getId());

            stmt.executeUpdate();
            pedigree.setTitle(strippedTitle);
            pedigree.setDescription(strippedDescription);
            pedigree.setTimeStamp(timeStamp);
            return true;

        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see PedigreeGateway#deletePedigree
     */
    @Override
    public void deletePedigree(Pedigree pedigree) {
        if (pedigree == null) {
            return;
        }
        String deletePersons = "DELETE FROM " + PERSONS + " WHERE pedigreeId=" + pedigree.getId();
        String deletePedigree = "DELETE FROM " + PEDIGREES + " WHERE pedigreeId=" + pedigree.getId();

        try (Statement stmt = conn.createStatement()) {

            stmt.addBatch(deletePersons);
            stmt.addBatch(deletePedigree);

            stmt.executeBatch();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
