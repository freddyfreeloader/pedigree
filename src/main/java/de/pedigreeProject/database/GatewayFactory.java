package de.pedigreeProject.database;

import java.sql.Connection;
import java.util.Objects;

/**
 * <p>A factory to produce a new PersonGateway for CRUD operations with Person objects<br>
 * and a new PedigreeGateway for CRUD operations with Pedigree objects.</p>
 * <p>Both PersonGateway and PedigreeGateway should operate with the same Connection object,<br>
 * so this GatewayFactory serves to initialize both with the same database connection.</p>
 */
public class GatewayFactory {

    private final PersonGateway personGateway;
    private final PedigreeGateway pedigreeGateway;


    /**
     * Constructs a new PedigreeGateway and a new PersonGateway with given Connection object.
     *
     * @param connection the Connection object for the database, must not be null
     * @throws RuntimeException if Connection object is invalid or sql statements fails
     */
    public GatewayFactory(Connection connection) {
        Objects.requireNonNull(connection);
        this.pedigreeGateway= new PedigreeGatewaySqlite(connection);
        this.personGateway= new PersonGatewaySqlite(connection);
    }

    public PersonGateway getPersonGateway() {
        return personGateway;
    }

    public PedigreeGateway getPedigreeGateway() {
        return pedigreeGateway;
    }
}
