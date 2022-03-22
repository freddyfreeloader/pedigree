package de.frederik.unitTests.guiTestFX.mockedDependencies;

import de.pedigreeProject.database.GatewayFactory;
import de.pedigreeProject.database.PedigreeGateway;
import de.pedigreeProject.database.PersonGateway;
import de.frederik.integrationTests.guiTestFX.utils.TestFxHelperMethods;
import de.pedigreeProject.controller.MainModelController;
import de.pedigreeProject.utils.gui_utils.StageInjector;
import de.pedigreeProject.utils.gui_utils.StageInjectorService;
import de.pedigreeProject.kinship.CloseKinshipUpdater;
import de.pedigreeProject.kinship.GeneticKinshipCalculator;
import de.pedigreeProject.kinship.KinshipSorterImpl;
import de.pedigreeProject.model.Model;
import de.pedigreeProject.model.Pedigree;
import de.pedigreeProject.model.Person;
import de.frederik.testUtils.TestDatabaseCleaner;
import de.pedigreeProject.utils.IndexChanger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public abstract class ModelWithMockedDependencies extends ApplicationTest {

    @Mock
    GatewayFactory gatewayFactory;

    @Mock
    PedigreeGateway pedigreeGatewayMock;
    @Mock
    PersonGateway personGatewayMock;
    @Mock
    Pedigree pedigreeMock;
    @Mock
    Person personMock;

    Model model;

    TestFxHelperMethods helper = new TestFxHelperMethods();
    static TestDatabaseCleaner cleaner = new TestDatabaseCleaner();


    @Start
    public void start(Stage stage) {
        cleaner.deleteRecords();
        when(gatewayFactory.getPedigreeGateway()).thenReturn(pedigreeGatewayMock);
        when(gatewayFactory.getPersonGateway()).thenReturn(personGatewayMock);
        when(personMock.getParents()).thenReturn(FXCollections.emptyObservableSet());
        when(personMock.getChildren()).thenReturn(FXCollections.emptyObservableSet());
        when(personMock.getSpouses()).thenReturn(FXCollections.emptyObservableSet());
        when(personMock.getSiblings()).thenReturn(FXCollections.emptyObservableSet());
        when(pedigreeMock.titleProperty()).thenReturn(new SimpleStringProperty("testpedigree"));
        when(pedigreeMock.descriptionProperty()).thenReturn(new SimpleStringProperty(""));
        model = new Model(gatewayFactory, new KinshipSorterImpl(), new CloseKinshipUpdater(), new GeneticKinshipCalculator(), new IndexChanger());

        model.setCurrentPedigree(pedigreeMock);
        model.setCurrentPerson(personMock);
        assumeTrue(model.getPersons().size() == 0);

        StageInjector stageInjector = new StageInjectorService(new MainModelController(model));

        stage = stageInjector.getStage();
        stage.show();
        stage.toFront();
    }
}