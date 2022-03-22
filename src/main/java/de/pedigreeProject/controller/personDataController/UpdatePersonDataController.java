package de.pedigreeProject.controller.personDataController;

import de.pedigreeProject.model.Model;
import de.pedigreeProject.model.Person;

import java.time.Year;

/**
 * Implements {@code PersonDataController} to update person data.
 */
public class UpdatePersonDataController extends PersonDataController {

    private final Person person;

    public UpdatePersonDataController(Model model, Person person) {
        super(model);
        this.person = person;

    }

    /**
     * @see PersonDataController#init()
     */
    @Override
    void init() {
        initTextFields(person);
    }

    private void initTextFields(Person person) {
        givenNameTF.setText(person.getGivenName());
        familyNameTF.setText(person.getFamilyName());
        if (person.getYearOfBirth().isPresent()) {
            yearOfBirthTF.setText(person.getYearOfBirth().get().toString());
        }
    }

    /**
     * @see Model#updatePersonData
     */
    @Override
    boolean save(String givenName, String familyName, Year yearOfBirth) {
        return model.updatePersonData(person, givenName, familyName, yearOfBirth);
    }
}