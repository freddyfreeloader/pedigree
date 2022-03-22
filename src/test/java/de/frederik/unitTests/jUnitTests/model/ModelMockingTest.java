package de.frederik.unitTests.jUnitTests.model;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModelMockingTest {
    @Mock
    GatewayFactory gatewayFactory;
    @Mock
    PedigreeGateway pedigreeGatewayMock;
    @Mock
    PersonGateway personGatewayMock;

    @Mock
    KinshipCalculator kinshipCalculator;
    @Mock
    KinshipSorter kinshipSorter;
    @Mock
    KinshipUpdater kinshipUpdater;
    @Mock
    IndexChanger indexChanger;
    @Mock
    List<Person> personListMock;

    private Person testPerson;
    private static final Pedigree testPedigree = new Pedigree(1, "TestPedigree", "", LocalDateTime.now());
    private Model model;

    @BeforeEach
    void setUp() {
        when(gatewayFactory.getPedigreeGateway()).thenReturn(pedigreeGatewayMock);
        when(gatewayFactory.getPersonGateway()).thenReturn(personGatewayMock);
        testPerson = new Person(1, 1, "testPerson", "", null);
    }

    @Test
    @DisplayName("created person should be in models persons list")
    void createPerson() {
        model = new Model(gatewayFactory, kinshipSorter, kinshipUpdater, kinshipCalculator, indexChanger);
        model.setCurrentPedigree(testPedigree);
        when(personGatewayMock.createPerson(any(), any(), any(), any())).thenReturn(Optional.of(testPerson));

        Person person = model.createPerson("anyName", "anyFamilyName", null).orElse(null);
        assertEquals(person, testPerson);
        assertTrue(model.getPersons().contains(testPerson));
    }

    @Test
    @DisplayName("createPerson() should return Optional.empty()")
    void createPerson_returns_empty_optional() {
        model = new Model(gatewayFactory, kinshipSorter, kinshipUpdater, kinshipCalculator, indexChanger);
        model.setCurrentPedigree(testPedigree);
        when(personGatewayMock.createPerson(any(), any(), any(), any())).thenReturn(Optional.empty());
        Optional<Person> personOptional = model.createPerson("anyName", "anyFamilyName", null);
        assertTrue(personOptional.isEmpty());
    }

    @Test
    @DisplayName("createPerson() should rethrow RuntimeException")
    void createPerson_RuntimeException() {
        model = new Model(gatewayFactory, kinshipSorter, kinshipUpdater, kinshipCalculator, indexChanger);
        model.setCurrentPedigree(testPedigree);
        when(personGatewayMock.createPerson(eq(testPedigree), Mockito.anyString(), Mockito.anyString(), any())).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> model.createPerson("anyName", "anyFamilyName", null));
    }

    @Test
    @DisplayName("create person with empty given name and empty family name should throw IllegalArgumentException")
    void createPerson_IllegalArgumentException() {
        model = new Model(gatewayFactory, kinshipSorter, kinshipUpdater, kinshipCalculator, indexChanger);
        model.setCurrentPedigree(testPedigree);
        assertThrows(IllegalArgumentException.class, () -> model.createPerson("", "", null));
    }

    @Test
    @DisplayName("test changeIndexOfPerson() returns true")
    void changeIndex_true() {
        model = new Model(gatewayFactory, kinshipSorter, kinshipUpdater, kinshipCalculator, indexChanger);
        List<List<Person>> list = new ArrayList<>();
        List<Person> listInList = new ArrayList<>();
        listInList.add(testPerson);
        list.add(listInList);

        when(kinshipSorter.getSortedLists(any())).thenReturn(list);
        when(indexChanger.changeIndex(testPerson, listInList, 1)).thenReturn(true);
        // to call model.updateGenerationsLists() and fill generations.ListsSorted
        model.setCurrentPerson(testPerson);

        assertTrue(model.changeIndexOfPerson(testPerson, 1), "should return true if indexChanger.changeIndex() return true");
    }

    @Test
    @DisplayName("test changeIndexOfPerson() returns false")
    void changeIndex_false() {
        model = new Model(gatewayFactory, kinshipSorter, kinshipUpdater, kinshipCalculator, indexChanger);
        List<List<Person>> list = new ArrayList<>();
        List<Person> listInList = new ArrayList<>();
        listInList.add(testPerson);
        list.add(listInList);
        when(kinshipSorter.getSortedLists(any())).thenReturn(list);
        when(indexChanger.changeIndex(testPerson, listInList, 1)).thenReturn(false);
        // to call model.updateGenerationsLists() and fill generations.ListsSorted
        model.setCurrentPerson(testPerson);
        assertFalse(model.changeIndexOfPerson(testPerson, 1), "should return false if indexChanger.changeIndex() return false");
    }

    @Test
    @DisplayName("test changeIndexOfPerson() returns false if generation lists are empty")
    void changeIndex_false_emptyGenerationsLists() {
        model = new Model(gatewayFactory, kinshipSorter, kinshipUpdater, kinshipCalculator, indexChanger);
        assertFalse(model.changeIndexOfPerson(testPerson, 1), "should return false if indexChanger.changeIndex() return false");
    }

    @Test
    @DisplayName("test updateRelatives()")
    void updateRelatives() {
        model = new Model(gatewayFactory, kinshipSorter, kinshipUpdater, kinshipCalculator, indexChanger);
        model.getPersons().add(testPerson);
        model.setCurrentPerson(testPerson);
        model.updateRelatives(testPerson, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        verify(personGatewayMock).updateRelatives(testPerson);
    }

    @Test
    @DisplayName("test deletePerson()")
    void deletePerson() {
        model = new Model(gatewayFactory, kinshipSorter, kinshipUpdater, kinshipCalculator, indexChanger);
        Person person = createTestPerson(1);
        Person relative = createTestPerson(2);

        person.addParent(relative);
        person.addSpouse(relative);
        person.addChild(relative);
        person.addSibling(relative);

        relative.addParent(person);
        relative.addSpouse(person);
        relative.addChild(person);
        relative.addSibling(person);

        model.getPersons().add(person);
        model.getPersons().add(relative);
        model.setCurrentPerson(relative);


        assertTrue(person.getParents().contains(relative));
        assertTrue(person.getChildren().contains(relative));
        assertTrue(person.getSpouses().contains(relative));
        assertTrue(person.getSiblings().contains(relative));

        assertTrue(model.getPersons().contains(relative));
        assertTrue(model.getPersons().contains(person));

        model.deletePerson(relative);

        assertFalse(person.getParents().contains(relative));
        assertFalse(person.getChildren().contains(relative));
        assertFalse(person.getSpouses().contains(relative));
        assertFalse(person.getSiblings().contains(relative));

        assertFalse(model.getPersons().contains(relative));
        assertTrue(model.getPersons().contains(person));

        verify(personGatewayMock).deletePerson(relative);
    }


    @Test
    @DisplayName("test updatePersonData(): successful")
    void updatePersonData_successful() {
        model = new Model(gatewayFactory, kinshipSorter, kinshipUpdater, kinshipCalculator, indexChanger);
        when(personGatewayMock.updatePersonsData(any(), eq("Test"), any(), any())).thenReturn(true);
        assertTrue(model.updatePersonData(testPerson, "Test", "", null));
    }

    @Test
    @DisplayName("test updatePersonData(): person already exists in database")
    void updatePersonData_person_already_exists() {
        model = new Model(gatewayFactory, kinshipSorter, kinshipUpdater, kinshipCalculator, indexChanger);
        when(personGatewayMock.updatePersonsData(any(), eq("Test"), any(), any())).thenReturn(false);
        assertFalse(model.updatePersonData(testPerson, "Test", "", null));
    }

    @Test
    @DisplayName("test updatePersonData(): rethrow RuntimeException")
    void updatePersonData_rethrow_RuntimeException() {
        model = new Model(gatewayFactory, kinshipSorter, kinshipUpdater, kinshipCalculator, indexChanger);
        when(personGatewayMock.updatePersonsData(any(), eq("Test"), any(), any())).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> model.updatePersonData(testPerson, "Test", "", null));
    }

    @Test
    @DisplayName("test updatePersonData(): rethrow IllegalArgumentException")
    void updatePersonData_rethrow_IllegalArgumentException() {
        model = new Model(gatewayFactory, kinshipSorter, kinshipUpdater, kinshipCalculator, indexChanger);
        when(personGatewayMock.updatePersonsData(any(), eq("Test"), any(), any())).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> model.updatePersonData(testPerson, "Test", "", null));
    }

    @Test
    @DisplayName("test updatePersonData(): if full name is blank then throw IllegalArgumentException")
    void updatePersonData_throw_IllegalArgumentException() {
        model = new Model(gatewayFactory, kinshipSorter, kinshipUpdater, kinshipCalculator, indexChanger);
        assertThrows(IllegalArgumentException.class, () -> model.updatePersonData(testPerson, "", "", null));
    }

    @Test
    @DisplayName("init model with existing pedigree")
    void initModel() {
        List<Pedigree> list = new ArrayList<>();
        Pedigree pedigree = createPedigree();
        list.add(pedigree);
        when(pedigreeGatewayMock.readPedigrees()).thenReturn(list);
        model = new Model(gatewayFactory, kinshipSorter, kinshipUpdater, kinshipCalculator, indexChanger);

        assertEquals(pedigree, model.getCurrentPedigree());
    }

    @Test
    @DisplayName("test createNewPedigree() : valid input")
    void createNewPedigree_validInput() {
        Pedigree pedigree = createPedigree();

        when(pedigreeGatewayMock.createPedigree(any(), any())).thenReturn(Optional.of(pedigree));
        model = new Model(gatewayFactory, kinshipSorter, kinshipUpdater, kinshipCalculator, indexChanger);

        assertEquals(Optional.of(pedigree), model.createNewPedigree("Test", ""));
        assertSame(model.getCurrentPedigree(), pedigree);
    }

    @Test
    @DisplayName("test createNewPedigree() : empty title")
    void createNewPedigree_empty_title() {
        model = new Model(gatewayFactory, kinshipSorter, kinshipUpdater, kinshipCalculator, indexChanger);
        assertThrows(IllegalArgumentException.class, () -> model.createNewPedigree("", ""));
    }

    @Test
    @DisplayName("test createNewPedigree() : rethrows RuntimeException")
    void createNewPedigree_runtimeException() {
        Pedigree pedigree = createPedigree();
        when(pedigreeGatewayMock.createPedigree(any(), any())).thenReturn(Optional.of(pedigree)).thenThrow(RuntimeException.class);

        model = new Model(gatewayFactory, kinshipSorter, kinshipUpdater, kinshipCalculator, indexChanger);
        assertThrows(RuntimeException.class, () -> model.createNewPedigree("Test", ""));
    }

    @Test
    @DisplayName("test createNewPedigree() : pedigree already exists in database")
    void createNewPedigree_pedigree_already_exists() {
        Pedigree pedigree = createPedigree();
        when(pedigreeGatewayMock.createPedigree(any(), any())).thenReturn(Optional.of(pedigree)).thenReturn(Optional.empty());

        model = new Model(gatewayFactory, kinshipSorter, kinshipUpdater, kinshipCalculator, indexChanger);
        assertFalse(model.createNewPedigree("Test", "").isPresent());
    }

    @Test
    @DisplayName("deletePedigree(): valid")
    void deletePedigree_valid() {
        Pedigree pedigree = createPedigree();
        model = new Model(gatewayFactory, kinshipSorter, kinshipUpdater, kinshipCalculator, indexChanger);
        model.getPedigrees().add(pedigree);
        model.setCurrentPedigree(pedigree);
        assertTrue(model.getPedigrees().contains(pedigree));
        model.deletePedigree();
        assertFalse(model.getPedigrees().contains(pedigree));
    }

    @Test
    @DisplayName("deletePedigree(): rethrow RuntimeException")
    void deletePedigree_runtimeException() {
        doThrow(RuntimeException.class).when(pedigreeGatewayMock).deletePedigree(any());
        model = new Model(gatewayFactory, kinshipSorter, kinshipUpdater, kinshipCalculator, indexChanger);
        assertThrows(RuntimeException.class, () -> model.deletePedigree());
    }

    @Test
    @DisplayName("deletePedigree(): default pedigree should recreated if deleted")
    void deletePedigree_default_pedigree() {
        String title = ResourceBundle.getBundle("pedigree").getString("default.pedigree.title");
        String description = ResourceBundle.getBundle("pedigree").getString("default.pedigree.description");
        when(pedigreeGatewayMock.createPedigree(title, description))
                .thenReturn(Optional.of(createDefaultPedigree(1)))
                .thenReturn(Optional.of(createDefaultPedigree(2)));
        model = new Model(gatewayFactory, kinshipSorter, kinshipUpdater, kinshipCalculator, indexChanger);

        Pedigree pedigreeBefore = model.getPedigrees().get(0);
        assertEquals(1, model.getPedigrees().size());
        assertEquals(title, pedigreeBefore.getTitle());
        assertEquals(1, pedigreeBefore.getId());

        verify(pedigreeGatewayMock, times(1)).createPedigree(title, description);
        model.deletePedigree();
        Pedigree pedigreeAfter = model.getPedigrees().get(0);

        verify(pedigreeGatewayMock, times(2)).createPedigree(title, description);
        assertEquals(1, model.getPedigrees().size());
        assertEquals(title, pedigreeAfter.getTitle());
        assertEquals(2, pedigreeAfter.getId());
    }

    @Test
    @DisplayName("replacePedigree()")
    void replacePedigree() {
        Pedigree firstPedigree = new Pedigree(2, "TestPedigree2", "", LocalDateTime.now());

        model = new Model(gatewayFactory, kinshipSorter, kinshipUpdater, kinshipCalculator, indexChanger);

        model.setCurrentPedigree(firstPedigree);
        model.getPersons().add(testPerson);
        model.getGenerationsListsSorted().add(personListMock);

        assertSame(firstPedigree, model.getCurrentPedigree());
        assertFalse(model.getGenerationsListsSorted().isEmpty());
        assertFalse(model.getPersons().isEmpty());

        model.replacePedigree(testPedigree);

        assertSame(testPedigree, model.getCurrentPedigree());
        assertTrue(model.getGenerationsListsSorted().isEmpty());
        assertTrue(model.getPersons().isEmpty());
    }

    @Test
    @DisplayName("update pedigree title and description - valid input")
    void updatePedigreeTitleAndDescription_ValidInput() {
        when(pedigreeGatewayMock.updatePedigree(any(), any(), any())).thenReturn(true);
        model = new Model(gatewayFactory, kinshipSorter, kinshipUpdater, kinshipCalculator, indexChanger);
        Pedigree pedigree = createPedigree();
        model.setCurrentPedigree(pedigree);
        String changedTitle = "changedTitle";
        String changedDescription = "changedDescription";

        assertTrue(model.updatePedigreeTitleAndDescription(changedTitle, changedDescription));

        Pedigree actualPedigree = model.getCurrentPedigree();
        assertSame(pedigree, actualPedigree);
        assertEquals(changedTitle, pedigree.getTitle());
        assertEquals(changedDescription, pedigree.getDescription());
    }

    @Test
    @DisplayName("update pedigree title and description - invalid input")
    void updatePedigreeTitleAndDescription_invalidInput() {
        when(pedigreeGatewayMock.updatePedigree(any(), any(), any())).thenReturn(false);
        model = new Model(gatewayFactory, kinshipSorter, kinshipUpdater, kinshipCalculator, indexChanger);
        Pedigree pedigree = createPedigree();
        model.setCurrentPedigree(pedigree);
        String oldTitle = pedigree.getTitle();
        String oldDescription = pedigree.getDescription();
        String changedTitle = "changedTitle";
        String changedDescription = "changedDescription";

        assertFalse(model.updatePedigreeTitleAndDescription(changedTitle, changedDescription));

        Pedigree actualPedigree = model.getCurrentPedigree();
        assertSame(pedigree, actualPedigree);
        assertEquals(oldTitle, pedigree.getTitle());
        assertEquals(oldDescription, pedigree.getDescription());
    }

    private Person createTestPerson(int id) {


        return new Person(id, 1, "TestPerson" + id, "", null);
    }

    private Pedigree createPedigree() {
        return new Pedigree(1, "testPedigree", "", LocalDateTime.now());
    }

    private Pedigree createDefaultPedigree(int id) {
        String title = ResourceBundle.getBundle("pedigree").getString("default.pedigree.title");
        String description = ResourceBundle.getBundle("pedigree").getString("default.pedigree.description");
        return new Pedigree(id, title, description, LocalDateTime.now());
    }
}