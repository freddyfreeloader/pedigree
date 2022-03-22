package de.pedigreeProject.model;

import de.pedigreeProject.database.GatewayFactory;
import de.pedigreeProject.database.PedigreeGateway;
import de.pedigreeProject.database.PersonGateway;
import de.pedigreeProject.kinship.CloseKinshipUpdater;
import de.pedigreeProject.kinship.KinshipCalculator;
import de.pedigreeProject.kinship.KinshipSorter;
import de.pedigreeProject.kinship.KinshipUpdater;
import de.pedigreeProject.utils.IndexChanger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.time.Year;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>The main model of the application.</p>
 * <p>Its unite the {@link Person} and {@link Pedigree} model to provide database operations, validations and computing of kinship.</p>
 */
public class Model {

//    static final Logger logger = LogManager.getLogger(Model.class.getName());

    /**
     * List of all Persons who belongs to the current pedigree
     */
    private final ObservableList<Person> persons = FXCollections.observableArrayList(Person.extractorPerson);
    /**
     * List of every pedigree persists in current database.
     */
    private final ObservableList<Pedigree> pedigrees = FXCollections.observableArrayList(Pedigree.extractorPedigree);
    /**
     * The actually active Pedigree
     */
    private final ObjectProperty<Pedigree> currentPedigree = new SimpleObjectProperty<>();
    public final StringProperty title = new SimpleStringProperty();
    public final StringProperty description = new SimpleStringProperty();

    private final PersonGateway personGateway;
    private final PedigreeGateway pedigreeGateway;
    private final KinshipCalculator kinshipCalculator;
    private final KinshipSorter kinshipSorter;
    private final KinshipUpdater kinshipUpdater;
    private final IndexChanger indexChanger;

    /**
     * A List of Lists of persons who belongs to the same generation.<br>
     * Computed in {@link #updateGenerationsLists()} and updated if {@link #currentPerson} changed.
     */
    private final ObservableList<List<Person>> generationsListsSorted = FXCollections.observableArrayList();
    /**
     * The Person who is actually the starting point for computing the kinship.
     */
    private final ObjectProperty<Person> currentPerson = new SimpleObjectProperty<>();

    /**
     * Constructs the Model with given {@code GateWayFactory},<br>
     * <ul>
     * <li>initializes the gateways to database,
     * <li>loads all pedigrees from database,
     * <li>sets the initial pedigree to {@link #currentPedigree},
     * <li>adds listener to {@link #currentPerson}
     * </ul>
     *
     * @param gatewayFactory the GateWayFactory with initialized gateways to database
     */
    public Model(GatewayFactory gatewayFactory, KinshipSorter kinshipSorter, KinshipUpdater kinshipUpdater, KinshipCalculator kinshipCalculator, IndexChanger indexChanger) {
        this.pedigreeGateway = gatewayFactory.getPedigreeGateway();
        this.personGateway = gatewayFactory.getPersonGateway();
        this.kinshipSorter = kinshipSorter;
        this.kinshipUpdater = kinshipUpdater;
        this.kinshipCalculator = kinshipCalculator;
        this.indexChanger = indexChanger;

        initListeners();
        initPedigrees();
    }

    private void initListeners() {
        currentPedigree.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                title.bind(newValue.titleProperty());
                description.bind(newValue.descriptionProperty());
            }
        });
        currentPerson.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateGenerationsLists();
            }
        });
    }

    /**
     * Loads all pedigrees from current database and sets the last updated pedigree as {@link #currentPedigree}.
     * If database is empty, a first default pedigree will be created.
     */
    private void initPedigrees() {

        pedigrees.clear();
        persons.clear();
        generationsListsSorted.clear();

        pedigrees.addAll(pedigreeGateway.readPedigrees());
        if (pedigrees.isEmpty()) {
            createDefaultPedigree();

        } else {
            // choose the last updated pedigree
            Pedigree pedigree = pedigrees.stream().max(Comparator.comparing(Pedigree::getTimeStamp)).get();
            setCurrentPedigree(pedigree);

            clearListsAndGetPersonsFromDatabase();
        }
    }

    /**
     * Computes the relatives of {@link #currentPerson} and put the sorted persons in {@link #generationsListsSorted}.
     *
     * @see KinshipCalculator
     * @see KinshipSorter
     */
    private void updateGenerationsLists() {

        generationsListsSorted.clear();
        if (getCurrentPerson() == null) {
            return;
        }

        Map<Person, Integer> generationsMap = kinshipCalculator.getPersonGenerationMap(getCurrentPerson());
        List<List<Person>> sortedList = kinshipSorter.getSortedLists(generationsMap);
        generationsListsSorted.addAll(sortedList);
    }

    /**
     * Clears {@link #persons} and {@link #generationsListsSorted}, add persons from database of current pedigree to PERSONS.
     */
    private void clearListsAndGetPersonsFromDatabase() {

        persons.clear();
        generationsListsSorted.clear();

        List<Person> personsList = personGateway.readPersons(getCurrentPedigree());
        persons.addAll(personsList);
    }

    /**
     * Shifts the position of the person in the List the person belongs to.
     *
     * @param person the Person to shift
     * @param change the number of positions the person should shift (change<0 -> left, change>0 -> right<br>
     * @return true if shifting was possible and done
     * @see IndexChanger
     */
    public boolean changeIndexOfPerson(final Person person, int change) {

        AtomicBoolean indexHasChanged = new AtomicBoolean(false);

        generationsListsSorted.stream()
                .filter(list -> list.contains(person))
                .findFirst()
                .ifPresent(generation -> {
                    boolean hasChanged = indexChanger.changeIndex(person, generation, change);
                    indexHasChanged.set(hasChanged);
                });

        return indexHasChanged.get();
    }

    // person data management

    /**
     * <p>Creates a new Person in database and add this person to models persons list.</p>
     * <p>Returns an {@code Optional.empty()} if a person with same given name, same family name and same year of birth already exists in the database.</p>
     * <p>It throws an <code>IllegalArgumentException</code> if both given name and family name are null or blank.</p>
     * <p>It throws an <code>NullPointerException</code> if current pedigree is null.</p>
     * <p> The year of birth may be null</p>
     *
     * @param givenName   persons given name
     * @param familyName  persons family name
     * @param yearOfBirth persons year of birth, may be null
     * @return an empty {@code Optional} if person with same given name, family name and year of birth already exists
     * @throws RuntimeException         if connection to database failed or sql is false
     * @throws IllegalArgumentException if both given name and family name are null/blank
     * @see PersonGateway#createPerson
     */
    public Optional<Person> createPerson(String givenName, String familyName, Year yearOfBirth) {
        Objects.requireNonNull(getCurrentPedigree());
        validateFullNameIsNotBlank(givenName, familyName);
        Optional<Person> optionalPerson = personGateway.createPerson(getCurrentPedigree(), givenName, familyName, yearOfBirth);

        optionalPerson.ifPresent(persons::add);
        return optionalPerson;
    }

    /**
     * <p>Updates the lists of relatives of this person and updates also every person who is related to this person.</p>
     * <p>For example adding a parent to a person would also add this person to the children list of the parent.</p>
     * <p>Changes are persisted in database, model generationListSorted will be updated.</p>
     *
     * @param person   to update, may be null
     * @param parents  replace persons parent list, not null
     * @param children replace persons children list, not null
     * @param spouses  replace persons spouses list, not null
     * @param siblings replace persons siblings list, not null
     * @throws RuntimeException if connection to database failed or sql is false
     * @see PersonGateway#updateRelatives(Person)
     * @see CloseKinshipUpdater#updateKinship
     */
    public void updateRelatives(Person person, List<Person> parents, List<Person> children, List<Person> spouses, List<Person> siblings) {

        kinshipUpdater.updateKinship(person, parents, children, spouses, siblings);
        for (Person p : persons) {
            personGateway.updateRelatives(p);
        }
        if (getCurrentPerson() != null) {
            updateGenerationsLists();
        }
    }

    /**
     * Deletes person from each relative list of PERSONS and from database,<br>
     * then calls {@link #updateGenerationsLists()}.<br>
     * If person equals {@link #currentPerson} set currentPerson to null.
     *
     * @param person the Person to delete
     */
    public void deletePerson(Person person) {

        persons.forEach(p -> {
            p.getParents().remove(person);
            p.getChildren().remove(person);
            p.getSpouses().remove(person);
            p.getSiblings().remove(person);
        });
        persons.remove(person);
        if (getCurrentPerson() == person) {
            setCurrentPerson(null);
        }
        personGateway.deletePerson(person);
        updateGenerationsLists();
    }

    /**
     * <p>Updates persons data in database.</p>
     * <p>Update fails if name already exists in database and returns <code>false</code>.</p>
     * <p>Throws an <code>IllegalArgumentException</code> if given name and family name both are null or blank</p>
     * <p>Throws a <code>NullPointerException</code> if person is null.</p>
     * <p>Updates the time stamp of the pedigree the person belongs to.</p>
     *
     * @param person      Person to update, must be not null
     * @param givenName   given name to update, may be null
     * @param familyName  family name to update, may be null
     * @param yearOfBirth year of Birth to update, may, be null
     * @return <code>true</code> if update is successful or <code>false</code> if update failed
     * @throws RuntimeException         if connection to database failed or sql is false
     * @throws IllegalArgumentException if given name and family name both are null/blank
     * @throws NullPointerException     if person is null
     * @see PersonGateway#updatePersonsData
     */
    public boolean updatePersonData(@NotNull Person person, String givenName, String familyName, Year yearOfBirth) {
        Objects.requireNonNull(person);
        validateFullNameIsNotBlank(givenName, familyName);
        return personGateway.updatePersonsData(person, givenName, familyName, yearOfBirth);
    }

    /**
     * Throws an {@code IllegalArgumentException} if arguments both are blank or null.
     *
     * @param givenName  the first string to test
     * @param familyName the second string to test
     */
    private void validateFullNameIsNotBlank(String givenName, String familyName) {
        if (StringUtils.isAllBlank(givenName, familyName)) {
            throw new IllegalArgumentException("Full name must not be blank or null!");
        }
    }

    // pedigree management

    /**
     * Calls {@link #createNewPedigree} with default parameter.
     */
    private void createDefaultPedigree() {
        String title = ResourceBundle.getBundle("pedigree", Locale.getDefault()).getString("default.pedigree.title");
        String description = ResourceBundle.getBundle("pedigree", Locale.getDefault()).getString("default.pedigree.description");
        createNewPedigree(title, description);
    }

    /**
     * <p>Creates a new <code>Pedigree</code> in database, add the pedigree to models pedigree list, set this pedigree as current pedigree and updates the persons list of model.</p>
     * <p>The title of the pedigree must not be blank or null, otherwise an <code>IllegalArgumentException</code> will be thrown.</p>
     * <p>If the tile of the pedigree already exists in the database, it returns an <code>Optional.empty()</code>.</p>
     *
     * @param title       name of the pedigree, not null or blank
     * @param description additional description of the pedigree, optional, may be null
     * @return {@code Optional<Pedigree>} , is empty if title already exists
     * @throws RuntimeException         if connection to database failed or sql is false
     * @throws IllegalArgumentException if title is blank/null
     * @see PedigreeGateway#createPedigree
     */
    public Optional<Pedigree> createNewPedigree(@NotNull String title, String description) {

        validateNotNullOrBlank(title);

        Optional<Pedigree> optionalPedigree = pedigreeGateway.createPedigree(title, description);
        optionalPedigree.ifPresent(this::integratePedigreeToModel);
        return optionalPedigree;
    }

    private void integratePedigreeToModel(Pedigree pedigree) {

        pedigrees.add(pedigree);
        setCurrentPedigree(pedigree);
        clearListsAndGetPersonsFromDatabase();
    }

    /**
     * Validate if parameter is blank or null, throws an {@code IllegalArgumentException}.
     *
     * @param s the string to validate
     * @see StringUtils#isBlank
     */
    private void validateNotNullOrBlank(String s) {
        if (StringUtils.isBlank(s)) {
            throw new IllegalArgumentException("String is null or blank!");
        }
    }

    /**
     * <p>Deletes the pedigree.</p>
     * <ul>
     * <li>Deletes the current pedigree from database.</li>
     * <li>Removes the current pedigree from model pedigree list.</li>
     * <li>Searches for the last updated pedigree in database and set the result as current pedigree.</li>
     * <li>If there is no pedigree in the database any more, then creates the default pedigree.</li>
     * </ul>
     *
     * @throws RuntimeException if connection to database failed or sql is false
     * @see PedigreeGateway#deletePedigree
     */
    public void deletePedigree() {

        pedigreeGateway.deletePedigree(getCurrentPedigree());

        pedigrees.remove(getCurrentPedigree());

        pedigrees.stream()
                .max(Comparator.comparing(Pedigree::getTimeStamp))
                .ifPresentOrElse(this::setCurrentPedigree, this::createDefaultPedigree);

        clearListsAndGetPersonsFromDatabase();
    }

    /**
     * <p>Exchanges the pedigree.</p>
     * <ul>
     * <li>Changes the actual pedigree with the given pedigree</li>
     * <li>Sets the current pedigree to the new pedigree.</li>
     * <li>Updates the persons list of model.</li>
     * </ul>
     *
     * @param pedigree the pedigree to switch
     * @throws RuntimeException if connection to database failed or sql is false
     */
    public void replacePedigree(Pedigree pedigree) {
        setCurrentPedigree(pedigree);
        clearListsAndGetPersonsFromDatabase();
    }

    /**
     * <ul>
     * <li>Updates given Pedigree and database with new title and description.</li>
     * <li>Updates time stamp of pedigree to <code>now()</code></li>
     * <liUpdates current pedigree with new parameters if the update was successful.</li>
     * <li>Returns <code>false</code> if a pedigree with same title already exists in database.</li>
     * </ul>
     *
     * @param title       name of the pedigree, must not be null or empty
     * @param description additional description of the pedigree, may be null
     * @return <code>true</code>, if update was successful, <code>false</code>, if title already exists
     * @throws RuntimeException         if connection to database failed or sql is false
     * @throws IllegalArgumentException if title is null or empty
     * @see PedigreeGateway#updatePedigree
     */
    public boolean updatePedigreeTitleAndDescription(String title, String description) {

        boolean updateWasSuccessfully = pedigreeGateway.updatePedigree(getCurrentPedigree(), title, description);
        if (updateWasSuccessfully) {
            getCurrentPedigree().setTitle(title);
            getCurrentPedigree().setDescription(description);
            return true;
        } else {
            return false;
        }
    }

    //getter and setter
    public ObservableList<Pedigree> getPedigrees() {
        return pedigrees;
    }

    public Pedigree getCurrentPedigree() {
        return currentPedigree.get();
    }

    public void setCurrentPedigree(Pedigree pedigree) {
        this.currentPedigree.set(pedigree);
    }

    public ObservableList<Person> getPersons() {
        return persons;
    }

    public ObservableList<List<Person>> getGenerationsListsSorted() {
        return generationsListsSorted;
    }

    public Person getCurrentPerson() {
        return currentPerson.get();
    }

    public void setCurrentPerson(Person person) {
        this.currentPerson.set(person);
    }

    public ObjectProperty<Person> currentPersonProperty () {
        return currentPerson;
    }
}

