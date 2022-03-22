package de.pedigreeProject.controller.pedigreeController;

import de.pedigreeProject.model.Model;

/**
 * Extends the abstract class PedigreeController to support creation of a new Pedigree.
 *
 * @see PedigreeController
 */
public class NewPedigreeController extends PedigreeController {

    /**
     * Constructor of the implementation of the abstract PedigreeController
     *
     * @param model the Model object
     * @see PedigreeController
     */
    public NewPedigreeController(Model model) {
        super(model);
    }

    @Override
    void init() {
    }

    @Override
    boolean save(String title, String description) {
        return model.createNewPedigree(title, description).isPresent();
    }
}
