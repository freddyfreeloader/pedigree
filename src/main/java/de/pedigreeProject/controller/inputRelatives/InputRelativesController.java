package de.pedigreeProject.controller.inputRelatives;

import de.pedigreeProject.kinship.CloseKinshipUpdater;
import de.pedigreeProject.kinship.KinshipValidator;
import de.pedigreeProject.kinship.StrongKinshipValidator;
import de.pedigreeProject.model.Model;
import de.pedigreeProject.model.Person;
import de.pedigreeProject.utils.tableCells.DeleteEntryButtonCell;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.util.Callback;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>Controller class for the view for editing and adding kinship to a person.</p>
 * It provides four tables:
 * <ul>
 *     <li><b>personsTable</b>
 *     <ul>
 *         <li> list of all possible persons who could be a relative to this person.<br></li>
 *         <Li> the persons table will be updated dynamically, if the members of the other tables change</Li>
 *         <li> the person could be dragged to the other tables</li>
 *     </ul>
 *     <li><b>parentsTable</b> - list of persons parents</li>
 *     <ul>
 *         <li>only two parents are allowed</li>
 *     </ul>
 *     <li><b>spousesTable</b> - list of persons spouses </li>
 *     <ul>
 *         <li>actually only one spouse is allowed</li>
 *     </ul>
 *     <li><b>siblingsTable</b> - list of persons siblings </li>
 *     <li><b>childrenTable</b> - list of persons children </li>
 *     </ul>
 *     <p>Every relatives table (parents, spouses, siblings, children) have a validator,
 *     if a relationship is possible. If not, dropping the person will fail and
 *     an error message appear to inform the user.</p>
 */
public class InputRelativesController {

    @FXML
    private TableView<Person> personsTable;
    @FXML
    private TableColumn<Person, String> personsGivenNameColumn;
    @FXML
    private TableColumn<Person, String> personsFamilyNameColumn;
    @FXML
    private TableColumn<Person, Year> personsYearOfBirthColumn;
    @FXML
    private TableView<Person> spousesTable;
    @FXML
    private TableColumn<Person, String> spousesGivenNameColumn;
    @FXML
    private TableColumn<Person, String> spousesFamilyNameColumn;
    @FXML
    private TableColumn<Person, Integer> deleteSpousesColumn;
    @FXML
    private TableView<Person> siblingsTable;
    @FXML
    private TableColumn<Person, String> siblingsGivenNameColumn;
    @FXML
    private TableColumn<Person, String> siblingsFamilyNameColumn;
    @FXML
    private TableColumn<Person, Integer> deleteSiblingsColumn;
    @FXML
    private TableView<Person> parentsTable;
    @FXML
    private TableColumn<Person, String> parentsGivenNameColumn;
    @FXML
    private TableColumn<Person, String> parentsFamilyNameColumn;
    @FXML
    private TableColumn<Person, Integer> deleteParentColumn;
    @FXML
    private TableView<Person> childrenTable;
    @FXML
    private TableColumn<Person, String> childrenGivenNameColumn;
    @FXML
    private TableColumn<Person, String> childrenFamilyNameColumn;
    @FXML
    private TableColumn<Person, Integer> deleteChildrenColumn;

    /**
     * StackPane to display the {@link #errorLabel} in the childrenTable.
     */
    @FXML
    private StackPane childrenStackPane;
    /**
     * StackPane to display the {@link #errorLabel} in the siblingsTable.
     */
    @FXML
    private StackPane siblingsStackPane;
    /**
     * StackPane to display the {@link #errorLabel} in the parentsTable.
     */
    @FXML
    private StackPane parentsStackPane;
    /**
     * StackPane to display the {@link #errorLabel} in the spousesTable.
     */
    @FXML
    private StackPane spousesStackPane;
    /**
     * Label to display the full name of the {@link #person}.
     */
    @FXML
    private Label nameLabel;

    /**
     * Label that appears while dragging persons from personsTable to relatives tables.
     */
    @FXML
    private Label draggingLabel;

    /**
     * Label with the error message if drop to table fails
     */
    private final Label errorLabel = new Label();
    /**
     * Flags if drop to a table is possible.
     */
    final BooleanProperty dropIsPossible = new SimpleBooleanProperty();

    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

    private final PseudoClass firstRow = PseudoClass.getPseudoClass("first-row");
    private final PseudoClass lastRow = PseudoClass.getPseudoClass("last-row");
    private final PseudoClass singleRow = PseudoClass.getPseudoClass("single-row");

    private final ObservableList<Person> parents = FXCollections.observableArrayList();
    private final ObservableList<Person> spouses = FXCollections.observableArrayList();
    private final ObservableList<Person> children = FXCollections.observableArrayList();
    private final ObservableList<Person> siblings = FXCollections.observableArrayList();

    private final Model model;
    /**
     * The Person to update its kinship
     */
    private final Person person;

    /**
     * <p>Constructs an {@code InputRelativesController} with {@link Model} and {@link Person}.</p>
     *
     * @param model  the {@code Model} of the application
     * @param person the {@code Person} to update its kinship
     */
    public InputRelativesController(Model model, Person person) {
        Objects.requireNonNull(model);
        Objects.requireNonNull(person);
        this.model = model;
        this.person = person;

        this.parents.addAll(person.getParents());
        this.children.addAll(person.getChildren());
        this.spouses.addAll(person.getSpouses());
        this.siblings.addAll(person.getSiblings());
    }

    /**
     * Called automatically by the Application after constructing,
     * initializes the labels, listeners and tables.
     */
    public void initialize() {
        nameLabel.textProperty().bind(person.givenNameProperty().concat(" ").concat(person.familyNameProperty()));
        initErrorLabel();
        initTables();
        initRelativesListsListeners();
    }

    //TODO init could done in fxml
    private void initErrorLabel() {
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setMouseTransparent(true);
        errorLabel.setWrapText(true);
    }

    /**
     * Adds a ListChangeListener to the Lists of relatives to
     * <ul>
     *     <li>{@link #updateRelatives()} of each person</li>
     *     <li>updates the {@link #personsTable}</li>
     * </ul>
     */
    private void initRelativesListsListeners() {
        ListChangeListener<Person> listener = (change) -> {
            updateRelatives();
            getItemsForPersonsTable();
        };
        spouses.addListener(listener);
        parents.addListener(listener);
        children.addListener(listener);
        siblings.addListener(listener);
    }

    private void initTables() {
        initPersonTable();
        initParentsTable();
        initChildrenTable();
        initSpousesTable();
        initSiblingsTable();
    }

    /**
     * Sets TableColumns and Items, configure the {@code setOnDragDetected} method.
     */
    private void initPersonTable() {

        personsGivenNameColumn.setCellValueFactory(cellData -> cellData.getValue().givenNameProperty());
        personsFamilyNameColumn.setCellValueFactory(cellData -> cellData.getValue().familyNameProperty());
        personsYearOfBirthColumn.setCellValueFactory(cellData -> cellData.getValue().yearOfBirthProperty());

        getItemsForPersonsTable();

        personsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        configureDragBehaviour();
    }

    private void configureDragBehaviour() {
        personsTable.setRowFactory(tv -> {
            TableRow<Person> row = new TableRow<>() {
                @Override
                public void updateIndex(int index) {
                    super.updateIndex(index);
                    pseudoClassStateChanged(lastRow, index >= 0 && index == personsTable.getItems().size() - 1);
                    pseudoClassStateChanged(firstRow, index == 0);
                    pseudoClassStateChanged(singleRow, index == 0 && index == personsTable.getItems().size() - 1);
                }
            };
            row.setOnDragDetected(event -> {

                if (!row.isEmpty()) {
                    Dragboard dragboard = row.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent clipboardContent = new ClipboardContent();

                    Person selectedPerson = personsTable.getSelectionModel().getSelectedItem();

                    configureDragLabel(dragboard, selectedPerson);

                    clipboardContent.put(SERIALIZED_MIME_TYPE, selectedPerson.getId());

                    dragboard.setContent(clipboardContent);
                    event.consume();
                }
            });
            return row;
        });
    }

    private void configureDragLabel(Dragboard dragboard, Person selectedPerson) {
        draggingLabel.setText(selectedPerson.toString());
        SnapshotParameters snap = new SnapshotParameters();
        snap.setFill(Paint.valueOf("transparent"));
        dragboard.setDragView(draggingLabel.snapshot(snap, null));
    }

    private void initChildrenTable() {
        initRelativesTableColumns(childrenGivenNameColumn, childrenFamilyNameColumn, deleteChildrenColumn);
        childrenTable.setItems(children);
        configDragAndDropBehaviour(childrenTable, children, childrenStackPane);
        addStyleClassToTableRow(childrenTable);
    }

    private void initParentsTable() {
        initRelativesTableColumns(parentsGivenNameColumn, parentsFamilyNameColumn, deleteParentColumn);
        parentsTable.setItems(parents);
        configDragAndDropBehaviour(parentsTable, parents, parentsStackPane);
        addStyleClassToTableRow(parentsTable);
    }

    private void initSiblingsTable() {
        initRelativesTableColumns(siblingsGivenNameColumn, siblingsFamilyNameColumn, deleteSiblingsColumn);
        siblingsTable.setItems(siblings);
        configDragAndDropBehaviour(siblingsTable, siblings, siblingsStackPane);
        addStyleClassToTableRow(siblingsTable);

    }

    private void initSpousesTable() {
        initRelativesTableColumns(spousesGivenNameColumn, spousesFamilyNameColumn, deleteSpousesColumn);
        spousesTable.setItems(spouses);
        configDragAndDropBehaviour(spousesTable, spouses, spousesStackPane);
        addStyleClassToTableRow(spousesTable);
    }

    private void addStyleClassToTableRow(TableView<Person> table) {

        table.setRowFactory(tv -> new TableRow<>() {
            @Override
            public void updateIndex(int index) {
                super.updateIndex(index);
                pseudoClassStateChanged(firstRow, index == 0);
                pseudoClassStateChanged(lastRow, index >= 0 && index == table.getItems().size() - 1);
                pseudoClassStateChanged(singleRow, index == 0 && index == table.getItems().size() - 1);
            }
        });
    }

    /**
     * Common method to initialize the TableColumns of each table of relatives (parents, children, spouses, siblings)
     *
     * @param givenNameColumn  the TableColumn for the given Name
     * @param familyNameColumn the TableColumn for the family Name
     * @param deleteColumn     the TableColumn for removing items
     */
    private void initRelativesTableColumns(TableColumn<Person, String> givenNameColumn, TableColumn<Person, String> familyNameColumn, TableColumn<Person, Integer> deleteColumn) {
        givenNameColumn.setCellValueFactory(cellData -> cellData.getValue().givenNameProperty());
        familyNameColumn.setCellValueFactory(cellData -> cellData.getValue().familyNameProperty());
        deleteColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

        Callback<TableColumn<Person, Integer>, TableCell<Person, Integer>> deleteEntry = p -> new DeleteEntryButtonCell();
        deleteColumn.setCellFactory(deleteEntry);
    }

    /**
     * Common method to initialize drag&drop behaviour for parents, children, siblings, and spouses table
     * <ul>
     *     <li>config TableView.setOnDragEnter</li>
     *     <li>config TableView.setOnDragOver</li>
     *     <li>config TableView.setOnDragDropped</li>
     *     <li>config TableView.setOnDragExited</li>
     * </ul>
     *
     * @param table      the TableView to config
     * @param personList the List of persons associated with the table
     * @param stackPane  the StackPane associated with the table to display error messages
     */
    private void configDragAndDropBehaviour(TableView<Person> table, List<Person> personList, StackPane stackPane) {
        configDragEntered(table, stackPane);
        configDragOver(table);
        configDragExited(table, stackPane);
        configDragDropped(table, personList);
    }

    /**
     * Sets the flag {@link #dropIsPossible} and display optional error message
     *
     * @param table     the table the drag gesture enters
     * @param stackPane the StackPane of the table to display the error message
     */
    private void configDragEntered(TableView<Person> table, StackPane stackPane) {

        table.setOnDragEntered(event -> {
            Dragboard dragboard = event.getDragboard();
            if (dragboard.hasContent(SERIALIZED_MIME_TYPE)) {

                int personId = (int) dragboard.getContent(SERIALIZED_MIME_TYPE);

                Optional<String> errorMessage = getErrorCode(table, personId);
                if (errorMessage.isEmpty()) {
                    dropIsPossible.set(true);
                } else {
                    showErrorMessage(stackPane, errorMessage.get());
                    dropIsPossible.set(false);
                }
            }
        });
    }

    /**
     * Validates if drop is possible by calling the corresponding method of {@link KinshipValidator}.<br>
     * Returns an empty Optional if dropping is possible, or an {@code Optional<String>} with the error message.
     *
     * @param table the table to drop person
     * @param id    the id of the person who wants to drop
     * @return an {@code Optional<String>} with error message if drop is not permitted, otherwise an empty Optional
     * @see StrongKinshipValidator
     */
    private Optional<String> getErrorCode(TableView<Person> table, int id) {
        Person aspirant = model.getPersons().stream().filter(p -> p.getId() == id).findFirst().orElse(null);

        KinshipValidator kinshipValidator = new StrongKinshipValidator();
        if (table == spousesTable) {
            return kinshipValidator.aspirantCouldBeSpouseOfPerson(aspirant, person);
        }
        if (table == parentsTable) {
            return kinshipValidator.aspirantCouldBeParentOfPerson(aspirant, person);
        }
        if (table == siblingsTable) {
            return kinshipValidator.aspirantCouldBeSiblingOfPerson(aspirant, person);
        }
//      table == childrenTable
        return kinshipValidator.aspirantCouldBeChildOfPerson(aspirant, person);
    }

    /**
     * Adds the {@link #errorLabel} with message to the StackPane
     *
     * @param stackPane the StackPane of the table where drop gesture fails
     * @param message   the specific error message generated by {@link KinshipValidator}
     */
    private void showErrorMessage(StackPane stackPane, String message) {
        if (!stackPane.getChildren().contains(errorLabel)) {
            errorLabel.setText(message);
            stackPane.getChildren().add(errorLabel);
        }
    }

    /**
     * Sets the accepted transfer mode to MOVE if drop is allowed
     *
     * @param table the TableView the drag gesture is over
     */
    private void configDragOver(TableView<Person> table) {
        table.setOnDragOver(event -> {
            if (dropIsPossible.get()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
        });
    }

    /**
     * Sets the flag {@link #dropIsPossible} to false and remove the error message from given StackPane.
     *
     * @param table     the TableView the drag gesture exits
     * @param stackPane the StackPane with the error message
     */
    private void configDragExited(TableView<Person> table, StackPane stackPane) {
        table.setOnDragExited(e -> {
            dropIsPossible.set(false);
            stackPane.getChildren().remove(errorLabel);
        });
    }

    /**
     * Adds the dragged Person to given table.
     *
     * @param table   the TableView to drop the Person
     * @param persons the List of Persons associated with the TableView
     */
    private void configDragDropped(TableView<Person> table, List<Person> persons) {

        table.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();

            if (db.hasContent(SERIALIZED_MIME_TYPE)) {

                int id = (int) db.getContent(SERIALIZED_MIME_TYPE);

                Person person = model.getPersons().stream().filter(p -> p.getId() == id).findFirst().orElse(null);
                persons.add(person);

                event.setDropCompleted(true);
                event.consume();
            }
        });
    }

    /**
     * Populates the personsTable with a copy of {@link Model} persons list,<br>
     * filters the persons {@link FilterForPersonsTable#filterPersons} and<br>
     * order the table (family name -> year of birth -> given name).
     */
    private void getItemsForPersonsTable() {
        personsTable.setItems(FXCollections.observableArrayList(model.getPersons()));

        FilterForPersonsTable.filterPersons(personsTable, person, parents, spouses, siblings, children);

        personsTable.getSortOrder().add(personsFamilyNameColumn);
        personsTable.getSortOrder().add(personsYearOfBirthColumn);
        personsTable.getSortOrder().add(personsGivenNameColumn);
        personsTable.sort();
    }

    /**
     * Calls {@link CloseKinshipUpdater#updateKinship}.
     */
    private void updateRelatives() {
        if (person != null) {
            model.updateRelatives(person, new ArrayList<>(parents), new ArrayList<>(children), new ArrayList<>(spouses), new ArrayList<>(siblings));
        }
    }

    /**
     * Closes the stage, called by clicking cancel button.
     *
     * @param event {@code ActionEvent} from the button
     */
    @FXML
    private void closeStage(ActionEvent event) {
        ((Node) event.getSource()).getScene().getWindow().hide();
    }
}
