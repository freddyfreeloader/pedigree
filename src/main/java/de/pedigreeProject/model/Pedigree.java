package de.pedigreeProject.model;

import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.util.Callback;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>Represents a group of persons with relationship to each other.</p>
 * <p>Every person could only belong to one pedigree, so you can't create different pedigrees with same person.</p>
 * <p>The Pedigree has a time stamp field that will be updated if something in this pedigree
 * or with the belonging persons have changed. So the controller/model could choose the last updated pedigree
 * as start pedigree.</p>
 * <p>The title of the pedigree is unique, the description is optional.</p>
 */
public class Pedigree {

    private final ReadOnlyIntegerWrapper id = new ReadOnlyIntegerWrapper();
    private final StringProperty title = new SimpleStringProperty("");
    private final StringProperty description = new SimpleStringProperty("");
    private final ObjectProperty<LocalDateTime> timeStamp = new SimpleObjectProperty<>();


    public static final Callback<Pedigree, Observable[]> extractorPedigree = e -> new Observable[]{
            e.titleProperty(),
            e.descriptionProperty(),
            e.timeStampProperty(),
    };

    public Pedigree(int id, String title, String description, LocalDateTime timeStamp) {
        setId(id);
        setTitle(title);
        setDescription(description);
        setTimeStamp(timeStamp);
    }

    /**
     * @return the title of the pedigree
     */
   /* @Override
    public String toString() {
        return title.get();
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedigree pedigree = (Pedigree) o;
        return Objects.equals(getId(), pedigree.getId()) && getTitle().equals(pedigree.getTitle()) && Objects.equals(getDescription(), pedigree.getDescription());
    }

    /*@Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getDescription());
    }

    @SuppressWarnings("unused")
    public ReadOnlyIntegerProperty idProperty() {
        return id.getReadOnlyProperty();
    }
*/
    private void setId(int id) {
        this.id.set(id);
    }
    public int getId() {
        return id.get();
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String name) {
        this.title.set(name);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp.get();
    }

    public ObjectProperty<LocalDateTime> timeStampProperty() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp.set(timeStamp);
    }
}
