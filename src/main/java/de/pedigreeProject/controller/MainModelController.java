package de.pedigreeProject.controller;

import de.pedigreeProject.controller.pedigreeController.NewPedigreeController;
import de.pedigreeProject.controller.pedigreeController.UpdatePedigreeController;
import de.pedigreeProject.controller.personDataController.NewPersonDataController;
import de.pedigreeProject.kinship.StateOfRelation;
import de.pedigreeProject.kinship.StateOfRelationCalculator;
import de.pedigreeProject.model.Model;
import de.pedigreeProject.model.Pedigree;
import de.pedigreeProject.model.Person;
import de.pedigreeProject.utils.VerticalTranslator;
import de.pedigreeProject.utils.gui_utils.LineGenerator;
import de.pedigreeProject.utils.gui_utils.StageInjectorService;
import de.pedigreeProject.utils.tableCells.DeletePersonButtonCell;
import de.pedigreeProject.utils.tableCells.EditDataButtonCell;
import de.pedigreeProject.utils.tableCells.EditRelativesButtonCell;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableMap;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.util.Callback;
import org.jetbrains.annotations.NotNull;

import java.time.Year;
import java.util.*;

/**
 * <p>The controller class of the main stage.</p>
 */
public class MainModelController {
    /**
     * Contains Labels (title and description) of current pedigree.
     */
    @FXML
    public VBox pedigreeTitelVBox;
    @FXML
    private Label descriptionOfPedigreeLabel;
    @FXML
    private Label titleOfPedigreeLabel;

    /**
     * The Parent of the fxml
     */
    @FXML
    private AnchorPane mainPane;
    /**
     * The {@code VBox} Node with children of all generated {@code HBox}es from {@link #generateHBoxesWithNodes()}.
     */
    @FXML
    private VBox mainVBox;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private AnchorPane pedigreeView;
    @FXML
    private BorderPane borderPane;
    @FXML
    private AnchorPane anchorCenter;

    @FXML
    private TableView<Person> table;
    @FXML
    private TableColumn<Person, String> givenNameColumn;
    @FXML
    private TableColumn<Person, String> familyNameColumn;
    @FXML
    private TableColumn<Person, Year> yearOfBirthColumn;
    @FXML
    private TableColumn<Person, Integer> deleteColumn;
    @FXML
    private TableColumn<Person, Integer> editColumn;
    @FXML
    private TableColumn<Person, Integer> editRelativesColumn;

    @FXML
    private CheckMenuItem ageCheckBox;
    @FXML
    private Menu menuRecent;
    /**
     * Calls {@link #openDataInputStage()}.
     */
    @FXML
    public Button addNewPersonButton;
    /**
     * Button that appears in persons Label if hovered. Moves the Label one position to right.
     */
    @FXML
    private Button movePersonToRight;
    /**
     * Button that appears in persons Label if hovered. Moves the Label one position to left.
     */
    @FXML
    private Button movePersonToLeft;
    /**
     * A List of all lines between the Labels of parent/child related persons.
     * The lines were generated in {@link #drawLinesFromParentToChildren} and drawn in the {@link #mainPane}.
     */
    private final List<Line> linesFromParentToChild = new ArrayList<>();

    private final Model model;

    /**
     * An {@code ObservableMap<Person, Node>} of every visible person with their belonging Node/Label shown in the {@link #mainVBox}.
     */
    private final ObservableMap<Person, Node> personNodes = FXCollections.observableMap(new HashMap<>());

    /**
     * Constructor for the MainModelController.
     *
     * @param model the model object
     */
    public MainModelController(Model model) {
        this.model = model;
    }

    ResourceBundle resourceBundle;

    /**
     * Initialize the nodes of the controller, called automatically after constructing.
     */
    public void initialize() {

        this.resourceBundle = ResourceBundle.getBundle("alerts", Locale.getDefault());

        initAgeCheckBox();

        initRecentMenu();

        initPedigreeTitleLabels();

        initPersonTable();

        initListeners();

    }

    private void initListeners() {

        model.getGenerationsListsSorted().addListener((ListChangeListener<List<Person>>) c -> buildNodesForPedigreeView());
    }

    /**
     * Generates Nodes for the pedigree view, called after {@link #changeIndexOfPerson} or if models generations lists are changed.
     */
    private void buildNodesForPedigreeView() {

        clearDataOfLastSearch();

        fillMapOfNodes();

        initMoveButtonsToNode();

        generateHBoxesWithNodes();

        translatePersonNodesHorizontally();

        translatePersonNodesVertically();

        drawLinesFromParentToChildren();
    }

    private void fillMapOfNodes() {
        PersonNode personNode = new PersonNode(model);
        model.getGenerationsListsSorted().forEach(generation -> {
            Map<Person, Node> map = personNode.getMapOfNodes(generation);
            personNodes.putAll(map
            );
        });
    }

    /**
     * Binds the title and description of the current Pedigree to {@link #titleOfPedigreeLabel} and {@link #descriptionOfPedigreeLabel}.
     */
    private void initPedigreeTitleLabels() {
        titleOfPedigreeLabel.textProperty().bind(model.title);
        descriptionOfPedigreeLabel.textProperty().bind(model.description);
    }

    /**
     * Initializes the 'Recent' submenu in the menu 'File' to show all known pedigrees.
     */
    private void initRecentMenu() {
        menuRecent.getItems().clear();

        model.getPedigrees().forEach(pedigree -> {
            MenuItem menuItem = new MenuItem();
            menuItem.textProperty().bind(pedigree.titleProperty());
            menuItem.setOnAction(event -> {
                model.replacePedigree(pedigree);
                clearDataOfLastSearch();
            });
            menuRecent.getItems().add(menuItem);
        });
    }

    /**
     * Adds a listener to the {@link #ageCheckBox} to toggle views with or without vertical shifted person Labels.
     */
    private void initAgeCheckBox() {
        ageCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                translatePersonNodesVertically();
            } else {
                undoTranslationNodesVertically();
            }
        });
    }

    /**
     * Removes the vertical shifting of the person Labels.
     */
    private void undoTranslationNodesVertically() {
        personNodes.forEach((person, node) -> {
            double marginRight = HBox.getMargin(node).getRight();
            HBox.setMargin(node, new Insets(0.0, marginRight, 0.0, 0.0));
        });
    }

    /**
     * Shifts the person Labels vertically down or up, depending on the year of birth of the persons.
     *
     * @see VerticalTranslator
     */
    private void translatePersonNodesVertically() {
        if (ageCheckBox.isSelected()) {
            VerticalTranslator verticalTranslator = new VerticalTranslator(personNodes, model.getGenerationsListsSorted());
            verticalTranslator.translateNodes(5.0);
        }
    }

    private void initPersonTable() {

        givenNameColumn.setCellValueFactory(cellData -> cellData.getValue().givenNameProperty());
        familyNameColumn.setCellValueFactory(cellData -> cellData.getValue().familyNameProperty());
        yearOfBirthColumn.setCellValueFactory(cellData -> cellData.getValue().yearOfBirthProperty());

        deleteColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        Callback<TableColumn<Person, Integer>, TableCell<Person, Integer>> deleteEntry = p -> new DeletePersonButtonCell(model);
        deleteColumn.setCellFactory(deleteEntry);

        editColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        Callback<TableColumn<Person, Integer>, TableCell<Person, Integer>> editData = p -> new EditDataButtonCell(model);
        editColumn.setCellFactory(editData);

        editRelativesColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        Callback<TableColumn<Person, Integer>, TableCell<Person, Integer>> editRelatives = p -> new EditRelativesButtonCell(model);
        editRelativesColumn.setCellFactory(editRelatives);

        PseudoClass lastRow = PseudoClass.getPseudoClass("last-row");
        PseudoClass firstRow = PseudoClass.getPseudoClass("first-row");
        PseudoClass singleRow = PseudoClass.getPseudoClass("single-row");

        table.setRowFactory(tv -> {
            TableRow<Person> row = new TableRow<>() {
                @Override
                public void updateIndex(int index) {
                    super.updateIndex(index);
                    boolean isLastIndex = index == table.getItems().size() - 1;
                    pseudoClassStateChanged(lastRow, index > 0 && isLastIndex);
                    pseudoClassStateChanged(firstRow, index == 0);
                    pseudoClassStateChanged(singleRow, index == 0 && isLastIndex);
                }
            };
            row.setOnMouseClicked(event -> model.setCurrentPerson(row.getItem()));
            return row;
        });

        model.getPersons().addListener((ListChangeListener<Person>) c -> setItemsTable(table));
        model.currentPersonProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                table.getSelectionModel().select(newValue);
            }
        });
        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> model.setCurrentPerson(newValue));
        setItemsTable(table);
    }

    private void setItemsTable(TableView<Person> table) {
        // copy of persons is necessary to provide an independent sort policy
        table.setItems(FXCollections.observableArrayList(model.getPersons()));
        table.getSortOrder().add(familyNameColumn);
        table.getSortOrder().add(yearOfBirthColumn);
        table.getSortOrder().add(givenNameColumn);
        table.sort();
    }

    /**
     * Adds spacing between the person nodes depending on their state of relationship to left and right 'neighbour' in the list/on the screen.
     *
     * @see StateOfRelation
     * @see StateOfRelationCalculator#getState
     */
    private void translatePersonNodesHorizontally() {
        StateOfRelationCalculator calculator = new StateOfRelationCalculator();
        for (List<Person> persons : model.getGenerationsListsSorted()) {
            persons.forEach(person -> {
                Node node = personNodes.get(person);
                double spacing = calculator.getState(person, persons).spacing;
                HBox.setMargin(node, new Insets(0, spacing, 0, 0));
            });
        }
    }

    /**
     * Removes all the Nodes from the {@link #mainVBox}, <br>
     * removes lines from {@link #mainPane}, <br>
     * clears the {@link #personNodes} and {@link #linesFromParentToChild} list.
     */
    private void clearDataOfLastSearch() {
        personNodes.clear();
        linesFromParentToChild.forEach(line -> pedigreeView.getChildren().remove(line));
        linesFromParentToChild.clear();
        mainVBox.getChildren().clear();
    }

    /**
     * Generates for each generation an HBox.<br>
     * Each HBox is filled with the persons Nodes/Labels.<br>
     * Sets listener to each HBox and the table for hiding the move buttons.
     */
    private void generateHBoxesWithNodes() {

        model.getGenerationsListsSorted().forEach(generation -> {

            HBox hbox = generateHBox();

            hbox.setOnMouseEntered(e -> hideMoveButtons());

            generation.forEach((person) -> hbox.getChildren().add(personNodes.get(person)));
            mainVBox.getChildren().add(hbox);
        });
    }

    @NotNull
    private HBox generateHBox() {
        HBox hbox = new HBox();
        hbox.getStyleClass().add("hBox-generation");
        return hbox;
    }

    /**
     * Sets the visibility of the move buttons to false.
     */
    private void hideMoveButtons() {
        movePersonToRight.setVisible(false);
        movePersonToLeft.setVisible(false);
    }

    /**
     * Defines the behaviour if mouse entered a person Label:
     * <ul>
     *     <li>positions the move buttons onto the edges of the labels</li>
     *     <li>sets the visibility policy for the move buttons:</li>
     *     <ul>
     *         <li>Labels on the left end have only the move right button</li>
     *         <li>Labels on the right end have only the move left button</li>
     *         <li>Labels with right and left neighbour have both move buttons</li>
     *         <li>Labels with neither right nor left neighbour have no move buttons</li>
     *     </ul>
     *     <li>Defines the behaviour by clicking on the move buttons</li>
     * </ul>
     */
    private void initMoveButtonsToNode() {
        personNodes.forEach((person1, node) -> node.setOnMouseEntered(e -> {
            hideMoveButtons();

            initMoveRightButton(node, person1);

            initMoveLeftButton(node, person1);

            initVisibilityMoveButtons(node);
        }));
    }

    private void initVisibilityMoveButtons(Node node) {
        List<Node> nodes = node.getParent().getChildrenUnmodifiable();
        int index = nodes.indexOf(node);
        if (index > 0) {
            movePersonToLeft.setVisible(true);
        }
        if (index < nodes.size() - 1) {
            movePersonToRight.setVisible(true);
        }
        table.setOnMouseEntered(e -> hideMoveButtons());
        pedigreeTitelVBox.setOnMouseEntered(e -> hideMoveButtons());
    }

    private void initMoveLeftButton(Node node, Person person) {
        Bounds bounds = node.localToScene(node.getBoundsInLocal());
        movePersonToLeft.setLayoutX(bounds.getMinX() - movePersonToLeft.getWidth() + 10);
        movePersonToLeft.setLayoutY(bounds.getCenterY() - movePersonToLeft.getHeight() / 2);
        movePersonToLeft.setOnAction(event -> {
            changeIndexOfPerson(person, -1);
            movePersonToLeft.setVisible(false);
        });

    }

    private void initMoveRightButton(Node node, Person person) {
        Bounds bounds = node.localToScene(node.getBoundsInLocal());
        movePersonToRight.setLayoutX(bounds.getMaxX() - 10);
        movePersonToRight.setLayoutY(bounds.getCenterY() - movePersonToRight.getHeight() / 2);
        movePersonToRight.setOnAction(event -> {
            changeIndexOfPerson(person, 1);
            movePersonToRight.setVisible(false);
        });
    }

    /**
     * @param person to shift right or left
     * @param change number of shifting
     * @see Model#changeIndexOfPerson
     */
    private void changeIndexOfPerson(Person person, int change) {

        if (model.changeIndexOfPerson(person, change)) {
            buildNodesForPedigreeView();
        }
    }

    /**
     * Draws lines between each parent/child.
     * Adds the generated lines to {@link #linesFromParentToChild} and {@link #mainPane}.
     *
     * @see LineGenerator
     */
    private void drawLinesFromParentToChildren() {
        linesFromParentToChild.forEach(line -> pedigreeView.getChildren().remove(line));

        linesFromParentToChild.clear();

        LineGenerator lineGenerator = new LineGenerator(pedigreeView);
        personNodes.forEach((person, labelParent) -> person.getChildren().forEach(child -> {
            Node labelChild = personNodes.get(child);

            Line line = lineGenerator.getLine(labelParent, labelChild);

            pedigreeView.getChildren().add(line);
            linesFromParentToChild.add(line);
        }));
    }

    /**
     * Opens a new window to add a new Person entry.
     * Called by {@link #addNewPersonButton}.
     */
    @FXML
    private void openDataInputStage() {
        new StageInjectorService(new NewPersonDataController(model)).showAndWait();
    }

    /**
     * Opens a new window to create a new Pedigree.<br>
     * If creating was successfully the new Pedigree replaces the former. <br>
     * Called by Menu -> File -> "neuer Stammbaum"
     */
    @FXML
    private void createNewPedigree() {
        Pedigree actualPedigree = model.getCurrentPedigree();
        new StageInjectorService(new NewPedigreeController(model)).showAndWait();
        if (model.getCurrentPedigree() != actualPedigree) {
            clearDataOfLastSearch();
            initRecentMenu();
            initPersonTable();
        }
    }

    /**
     * Shows an AlertDialog and deletes the pedigree with all belonging Persons.
     */
    @FXML
    private void deletePedigree() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, resourceBundle.getString("delete.pedigree"));
        Optional<ButtonType> opt = alert.showAndWait();
        opt.ifPresent(buttonType -> {
            if (buttonType.equals(ButtonType.OK)) {

                model.deletePedigree();
                initRecentMenu();
                initPersonTable();
            }
        });
    }

    /**
     * Opens a new window to change title and description of current pedigree.<br>
     * Called by clicking on {@link #pedigreeTitelVBox}.
     */
    @FXML
    public void updatePedigreeName() {
        new StageInjectorService(new UpdatePedigreeController(model)).showAndWait();
    }

    /**
     * Exits the application.
     * Called by Menu -> File -> Exit
     */
    @FXML
    public void exit() {
        Platform.exit();
        System.exit(0);
    }
}