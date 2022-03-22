package de.pedigreeProject.controller.personDataController;

import de.pedigreeProject.model.Model;
import de.pedigreeProject.model.Person;

import java.time.Year;
import java.util.Optional;

/**
 * Implements PersonDataController to create a new Person.
 */
public class NewPersonDataController extends PersonDataController {

    public NewPersonDataController(Model model) {
        super(model);
    }

    /**
     * @see PersonDataController#init()
     */
    @Override
    void init() {
    }

    /**
     * @see Model#createPerson
     */
    @Override
    boolean save(String givenName, String familyName, Year yearOfBirth) {

        Optional<Person> opt = model.createPerson(givenName, familyName, yearOfBirth);
        return opt.isPresent();
    }
}