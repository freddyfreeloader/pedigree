package de.frederik.testUtils.testData;

import de.frederik.testUtils.DatabaseName;
import de.frederik.testUtils.TestDatabaseCleaner;
import de.pedigreeProject.database.DatabaseConnectionSqlite;
import de.pedigreeProject.database.GatewayFactory;
import de.pedigreeProject.database.PedigreeGateway;
import de.pedigreeProject.database.PersonGateway;
import de.pedigreeProject.model.Pedigree;
import de.pedigreeProject.model.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Provides static methods to get test data.
 */
public class TestDatabase {
    final static Logger logger = LogManager.getLogger("test");

    static GatewayFactory gatewayFactory;
    static PedigreeGateway pedigreeGateway;
    static PersonGateway personGateway;

    static final String BUDDENBROOKS = "Familie Buddenbrook";
    static final String RANDOM_PERSONS = "random persons";
    static final String BASE_FAMILY = "Base Family";
    static final String TRANSLATION_Y = "translation Y";
    static final String THREE_SIBLINGS = "three siblings";

    static TestDatabaseCleaner cleaner = new TestDatabaseCleaner();

    static {
        Connection connection = new DatabaseConnectionSqlite(DatabaseName.TEST.toString()).getConnection();
        gatewayFactory = new GatewayFactory(connection);
        pedigreeGateway = gatewayFactory.getPedigreeGateway();
        personGateway = gatewayFactory.getPersonGateway();
    }

    private TestDatabase() {
    }

    /**
     * @see RandomPersons
     */
    public static List<Person> getRandomPersons() {
        TestPersons randomPersons = new RandomPersons();
        return randomPersons.getPersons();
    }

    /**
     * @see Buddenbrooks
     */
    public static List<Person> getBuddenbrooks() {
        TestPersons buddenbrooks = new Buddenbrooks();
        return buddenbrooks.getPersons();
    }

    /**
     * @see BaseFamily
     */
    public static List<Person> getBaseFamily() {
        TestPersons baseFamily = new BaseFamily();
        return baseFamily.getPersons();
    }


    @SuppressWarnings("unused")
    public static Pedigree getPedigreeOfRandomPersons() {
        return createPedigree(RANDOM_PERSONS, new RandomPersons());
    }

    /**
     * @see BaseFamily
     */
    public static Pedigree getPedigreeOfBaseFamily() {
        return createPedigree(BASE_FAMILY, new BaseFamily());
    }

    /**
     * @see Buddenbrooks
     */
    public static Pedigree getPedigreeOfBuddenbrook() {
        return createPedigree(BUDDENBROOKS, new Buddenbrooks());
    }

    /**
     * @see TranslationY
     */
    public static Pedigree getTranslationTestPedigree() {
        return createPedigree(TRANSLATION_Y, new TranslationY());
    }

    /**
     * @see ThreeSiblings
     */
    public static Pedigree getThreeSiblings() {
        return createPedigree(THREE_SIBLINGS, new ThreeSiblings());
    }

    private static Pedigree createPedigree(String pedigreeTitle, TestPersons testPersons) {
        cleaner.deleteRecords();

        Pedigree pedigree = pedigreeGateway.createPedigree(pedigreeTitle, "").orElseThrow();

        List<Person> persons = addPersonsToDataBase(testPersons, pedigree);
        copyRelatives(testPersons.getPersons(), persons);
        persons.forEach(personGateway::updateRelatives);
        return pedigree;
    }

    private static List<Person> addPersonsToDataBase(TestPersons testPersons, Pedigree pedigree) {
        List<Person> createdPersons = new ArrayList<>();

        for (Person person : testPersons.getPersons()) {

            Person created = personGateway.createPerson(pedigree, person.getGivenName(), person.getFamilyName(), person.getYearOfBirth().orElse(null)).orElseThrow();

            createdPersons.add(created);
        }
        return createdPersons;
    }

    private static void copyRelatives(List<Person> sourceList, List<Person> targetList) {
        for (Person source : sourceList) {

            Person target = getPersonWithSameName(source, targetList);

            if (target != null && source != null) {

                source.getParents().forEach(person -> target.addParent(getPersonWithSameName(person, targetList)));
                source.getChildren().forEach(person -> target.addChild(getPersonWithSameName(person, targetList)));
                source.getSpouses().forEach(person -> target.addSpouse(getPersonWithSameName(person, targetList)));
                source.getSiblings().forEach(person -> target.addSibling(getPersonWithSameName(person, targetList)));
            }
        }
    }

    private static Person getPersonWithSameName(Person personToFind, List<Person> createdPersons) {

        Optional<Person> opt = createdPersons.stream()
                .filter(person -> person.getGivenName().equals(personToFind.getGivenName()) && person.getFamilyName().equals(personToFind.getFamilyName()))
                .findFirst();
        return opt.orElse(null);
    }
}
