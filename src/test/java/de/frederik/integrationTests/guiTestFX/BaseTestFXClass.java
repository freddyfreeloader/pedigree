package de.frederik.integrationTests.guiTestFX;

import de.frederik.integrationTests.guiTestFX.utils.TestFxHelperMethods;
import de.frederik.testUtils.DatabaseName;
import de.frederik.testUtils.TestDatabaseCleaner;
import de.frederik.testUtils.testData.TestDatabase;
import de.pedigreeProject.controller.MainModelController;
import de.pedigreeProject.database.DatabaseConnectionSqlite;
import de.pedigreeProject.database.GatewayFactory;
import de.pedigreeProject.database.PedigreeGateway;
import de.pedigreeProject.database.PersonGateway;
import de.pedigreeProject.kinship.*;
import de.pedigreeProject.model.Model;
import de.pedigreeProject.model.Pedigree;
import de.pedigreeProject.model.Person;
import de.pedigreeProject.utils.IndexChanger;
import de.pedigreeProject.utils.gui_utils.StageInjector;
import de.pedigreeProject.utils.gui_utils.StageInjectorService;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.TableViewMatchers;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Set;

import static de.frederik.integrationTests.guiTestFX.utils.NodesOfFxmls.*;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.testfx.api.FxAssert.verifyThat;

@ExtendWith(MockitoExtension.class)
abstract class BaseTestFXClass extends ApplicationTest {

    Model model;
    static GatewayFactory gatewayFactory;

    MainModelController controller;
    PedigreeGateway pedigreeGateway;
    PersonGateway personGateway;

    static TestFxHelperMethods helper = new TestFxHelperMethods();
    static TestDatabaseCleaner cleaner = new TestDatabaseCleaner();


    @Start
    public void start(Stage stage) {
        cleaner.deleteRecords();

        Connection connection = new DatabaseConnectionSqlite(DatabaseName.TEST.toString()).getConnection();
        gatewayFactory = new GatewayFactory(connection);
        pedigreeGateway = gatewayFactory.getPedigreeGateway();
        personGateway = gatewayFactory.getPersonGateway();

        KinshipUpdater kinshipUpdater = new CloseKinshipUpdater();
        KinshipSorter kinshipSorter = new KinshipSorterImpl();
        KinshipCalculator kinshipCalculator = new GeneticKinshipCalculator();
        IndexChanger indexChanger = new IndexChanger();

        model = new Model(gatewayFactory, kinshipSorter, kinshipUpdater, kinshipCalculator, indexChanger);

        controller = Mockito.spy(new MainModelController(model));
        StageInjector stageInjector = new StageInjectorService(controller);
        Stage stage1 = stageInjector.getStage();
        stage1.show();
        stage1.toFront();
    }

    // helper methods for testFX:

    @Nullable
    Person getPersonByGivenName(String givenName) {
        return model.getPersons().stream().filter(person -> person.getGivenName().equals(givenName)).findFirst().orElse(null);
    }

    void createBaseFamilyPedigree() {
        Platform.runLater(() -> {
            Pedigree pedigree = TestDatabase.getPedigreeOfBaseFamily();
            model.replacePedigree(pedigree);
        });
        interrupt();
    }

    void createBuddenbrookPedigree() {
        Platform.runLater(() -> {
            Pedigree pedigree = TestDatabase.getPedigreeOfBuddenbrook();
            model.replacePedigree(pedigree);
        });
        interrupt();
    }

    void createTranslationTestPedigree() {
        Platform.runLater(() -> {
            Pedigree pedigree = TestDatabase.getTranslationTestPedigree();
            model.replacePedigree(pedigree);
        });
        interrupt();
    }

    void addNewPerson(String givenName, String familyName, String yearOfBirth) {
        helper.fireButton(ADD_PERSON_BUTTON);

        fillAllTextFieldsAndSave(givenName, familyName, yearOfBirth);
    }

    void fillAllTextFieldsAndSave(String givenName, String familyName, String yearOfBirth) {
        helper.fillTextField(GIVEN_NAME_TF, givenName);
        helper.fillTextField(FAMILY_NAME_TF, familyName);
        helper.fillTextField(YEAR_OF_BIRTH_TF, yearOfBirth);
        helper.fireButton(SAVE_BUTTON_PERSON_DATA);
    }

    void fireEditPersonButton(String givenName, String familyName) {
        Button editButton = lookup(helper.isButtonInTableRow(EDIT_SELECTOR, givenName, familyName)).queryButton();
        helper.fireButton(editButton);
    }

    Set<Label> getLabelsFromScrollPane() {
        Node scrollPane = lookup(SCROLL_PANE).query();
        return from(scrollPane).lookup(instanceOf(Label.class)).queryAll();
    }

    Label getLabelFromScrollPane(String identifier) {
        Node scrollPane = lookup(SCROLL_PANE).query();
        Set<Label> labels = from(scrollPane).lookup(instanceOf(Label.class)).queryAll();
        return labels.stream().filter(label -> label.getText().contains(identifier)).findFirst().orElse(null);

    }

    boolean mostlyOneLabelIsTranslated() {
        Set<Label> labels = getLabelsFromScrollPane();
        return labels.stream().anyMatch(label -> HBox.getMargin(label).getTop() > 0);
    }

    void fireEditRelativesButton(String givenName, String yearOfBirth) {
        Button editButton = lookup(helper.isButtonInTableRow(ADD_RELATIVES_CSS, givenName, yearOfBirth)).queryButton();
        helper.fireButton(editButton);
        interrupt();
    }

    void fireDeleteButton(String givenName, String tableRowElement) {
        Button deleteButton = lookup(helper.isButtonInTableRow(CLOSE_BUTTON_RELATIVES, givenName, tableRowElement)).queryButton();
        Platform.runLater(deleteButton::fire);
        interrupt();
    }

    void personsTableHaveOnlyThisMembers(Person... persons) {
        verifyThat(PERSONS_TABLE, TableViewMatchers.hasNumRows(persons.length));
        Arrays.stream(persons).forEach(person -> verifyThat(PERSONS_TABLE, TableViewMatchers.hasTableCell(person)));
    }

    void parentsTableHaveOnlyThisMembers(Person... persons) {
        verifyThat(PARENTS_TABLE, TableViewMatchers.hasNumRows(persons.length));
        Arrays.stream(persons).forEach(person -> verifyThat(PARENTS_TABLE, TableViewMatchers.hasTableCell(person)));
    }

    void spousesTableHaveOnlyThisMembers(Person... persons) {
        verifyThat(SPOUSES_TABLE, TableViewMatchers.hasNumRows(persons.length));
        Arrays.stream(persons).forEach(person -> verifyThat(SPOUSES_TABLE, TableViewMatchers.hasTableCell(person)));
    }

    void siblingsTableHaveOnlyThisMembers(Person... persons) {
        verifyThat(SIBLINGS_TABLE, TableViewMatchers.hasNumRows(persons.length));
        Arrays.stream(persons).forEach(person -> verifyThat(SIBLINGS_TABLE, TableViewMatchers.hasTableCell(person)));
    }

    void childrenTableHaveOnlyThisMembers(Person... persons) {
        verifyThat(CHILDREN_TABLE, TableViewMatchers.hasNumRows(persons.length));
        Arrays.stream(persons).forEach(person -> verifyThat(CHILDREN_TABLE, TableViewMatchers.hasTableCell(person)));
    }
}