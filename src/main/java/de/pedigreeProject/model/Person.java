package de.pedigreeProject.model;

import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.util.Callback;

import java.time.Year;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents an entity of a person. <br>
 * Important Note:<br>
 * The Person object holds in his lists of relatives not the ID but the references to every other related person.<br>
 * So it is possible to build a genetic pedigree from each single person by simply searching recursively throughout its relatives.<br>
 * Consequently, it is NOT possible to read a single Person WITH relatives from database, before every related person is already constructed.<br>
 * <p>Nearly every class or method to compute or handle kinship in this application uses this decision of design,
 * so change this behaviour would cause a huge effort.</p>
 * This decision of design could perhaps cause troubles in future extensions of the application, for example synchronisation with shared pedigrees.
 * <p>Spouses:</p>
 * Spouses are not normally genetic related to a person, nevertheless there was the decision to list spouses:
 * <ul>
 *     <li>The user expect to see spouses in a pedigree to be graphically nearly related to each other</li>
 *     <li>Future implementations could extend the app to switch between a 'genetic modus' and 'civil right modus',<br>
 *     where spouses are seen as relatives.</li>
 *     <li>Future implementations could extend to a 'greek gods modus', where every relationship are allowed</li>
 * </ul>
 */
@SuppressWarnings("unused")
public class Person {
    private final ReadOnlyIntegerWrapper id = new ReadOnlyIntegerWrapper();
    private final ReadOnlyIntegerWrapper pedigreeId = new ReadOnlyIntegerWrapper();
    private final StringProperty givenName = new SimpleStringProperty("");
    private final StringProperty familyName = new SimpleStringProperty("");
    private final ObjectProperty<Year> yearOfBirth = new SimpleObjectProperty<>();

    private final ObservableSet<Person> parents = FXCollections.observableSet();
    private final ObservableSet<Person> children = FXCollections.observableSet();
    private final ObservableSet<Person> siblings = FXCollections.observableSet();
    private final ObservableSet<Person> spouses = FXCollections.observableSet();

    public static Callback<Person, Observable[]> extractorPerson = e -> new Observable[]{
            e.givenNameProperty(),
            e.familyNameProperty(),
            e.yearOfBirthProperty(),
            e.getParents(),
            e.getChildren(),
            e.getSpouses(),
    };

    public Person(int id, int pedigreeId, String givenName, String familyName, Year yearOfBirth) {
        setId(id);
        setPedigreeId(pedigreeId);
        setGivenName(givenName);
        setFamilyName(familyName);
        setYearOfBirth(yearOfBirth);

    }

    /**
     * @return the full name
     */
    @Override
    public String toString() {
        return (givenName.get() + " " + familyName.get()).strip();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(getId(), person.getId())
                && Objects.equals(getGivenName(), person.getGivenName())
                && Objects.equals(getFamilyName(), person.getFamilyName())
                && Objects.equals(yearOfBirth.get(), person.yearOfBirthProperty().get())
                && Objects.equals(getParents().stream().map(Person::getId).sorted().collect(Collectors.toList()).toString(), person.getParents().stream().map(Person::getId).sorted().collect(Collectors.toList()).toString())
                && Objects.equals(getChildren().stream().map(Person::getId).sorted().collect(Collectors.toList()).toString(), person.getChildren().stream().map(Person::getId).sorted().collect(Collectors.toList()).toString())
                && Objects.equals(getSpouses().stream().map(Person::getId).sorted().collect(Collectors.toList()).toString(), person.getSpouses().stream().map(Person::getId).sorted().collect(Collectors.toList()).toString())
                && Objects.equals(getSiblings().stream().map(Person::getId).sorted().collect(Collectors.toList()).toString(), person.getSiblings().stream().map(Person::getId).sorted().collect(Collectors.toList()).toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGivenName(), getFamilyName(), getYearOfBirth());
    }


    public int getId() {
        return id.get();
    }

    public ReadOnlyIntegerProperty idProperty() {
        return id.getReadOnlyProperty();
    }

    private void setId(int id) {
        this.id.set(id);
    }

    public int getPedigreeId() {
        return pedigreeId.get();
    }

//    public ReadOnlyIntegerProperty pedigreeIdProperty() {
//        return pedigreeId.getReadOnlyProperty();
//    }

    public void setPedigreeId(int pedigreeId) {
        this.pedigreeId.set(pedigreeId);
    }

    public String getGivenName() {
        return givenName.get();
    }

    public StringProperty givenNameProperty() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName.set(givenName);
    }


    public String getFamilyName() {
        return familyName.get();
    }

    public StringProperty familyNameProperty() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName.set(familyName);
    }


    public void updateNameAndYear(String givenName, String familyName, Year yearOfBirth) {
        this.givenName.set(givenName);
        this.familyName.set(familyName);
        this.yearOfBirth.set(yearOfBirth);
    }

    public Optional<Year> getYearOfBirth() {
        return Optional.ofNullable(yearOfBirth.get());
    }

    public ObjectProperty<Year> yearOfBirthProperty() {
        return yearOfBirth;
    }

    public void setYearOfBirth(Year yearOfBirth) {
        this.yearOfBirth.set(yearOfBirth);
    }


    public ObservableSet<Person> getParents() {
        return parents;
    }

    public ObservableSet<Person> getChildren() {
        return children;
    }

    public Set<Person> getSiblings() {
        return siblings;
    }

    public ObservableSet<Person> getSpouses() {
        return spouses;
    }


    public void addChild(Person... persons) {
        this.children.addAll(Arrays.asList(persons));
    }

    public void removeChild(Person person) {
        if (person != null) {
            this.children.remove(person);
        }
    }

    public void removeParent(Person person) {
        if (person != null) {
            this.parents.remove(person);
        }
    }

    public void addParent(Person... persons) {
        this.parents.addAll(Arrays.asList(persons));
    }

    public void removeSibling(Person person) {
        if (person != null) {
            this.siblings.remove(person);
        }
    }

    public void addSibling(Person... persons) {
        this.siblings.addAll(Arrays.asList(persons));
    }

    public void removeSpouse(Person person) {
        if (person != null) {
            this.spouses.remove(person);
        }
    }

    public void addSpouse(Person... persons) {
        this.spouses.addAll(Arrays.asList(persons));
    }
}
