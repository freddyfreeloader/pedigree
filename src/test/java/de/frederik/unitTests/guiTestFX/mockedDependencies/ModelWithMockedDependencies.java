package de.frederik.unitTests.guiTestFX.mockedDependencies;

import de.frederik.integrationTests.guiTestFX.utils.TestFxHelperMethods;
import de.pedigreeProject.controller.MainModelController;
import de.pedigreeProject.database.GatewayFactory;
import de.pedigreeProject.database.PedigreeGateway;
import de.pedigreeProject.database.PersonGateway;
import de.pedigreeProject.kinship.KinshipCalculator;
import de.pedigreeProject.kinship.KinshipSorter;
import de.pedigreeProject.kinship.KinshipUpdater;
import de.pedigreeProject.model.Model;
import de.pedigreeProject.model.Pedigree;
import de.pedigreeProject.model.Person;
import de.pedigreeProject.utils.IndexChanger;
import de.pedigreeProject.utils.gui_utils.StageInjector;
import de.pedigreeProject.utils.gui_utils.StageInjectorService;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;

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
    KinshipSorter kinshipSorter;
    @Mock
    KinshipUpdater kinshipUpdater;
    @Mock
    KinshipCalculator kinshipCalculator;
    @Mock
    IndexChanger indexChanger;
    @Mock
    Pedigree pedigreeMock;
    @Mock
    Person personMock;

    TestFxHelperMethods helper = new TestFxHelperMethods();

    @Start
    public void start(Stage stage) {
        when(gatewayFactory.getPedigreeGateway()).thenReturn(pedigreeGatewayMock);
        when(gatewayFactory.getPersonGateway()).thenReturn(personGatewayMock);
        when(pedigreeMock.titleProperty()).thenReturn(new SimpleStringProperty("testpedigree"));
        when(pedigreeMock.descriptionProperty()).thenReturn(new SimpleStringProperty(""));
        Model model = new Model(gatewayFactory, kinshipSorter, kinshipUpdater, kinshipCalculator, indexChanger);

        model.setCurrentPedigree(pedigreeMock);
        model.setCurrentPerson(personMock);

        StageInjector stageInjector = new StageInjectorService(new MainModelController(model));

        stage = stageInjector.getStage();
        stage.show();
        stage.toFront();
    }
}