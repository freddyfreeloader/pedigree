package de.pedigreeProject.controller.pedigreeController;

import de.pedigreeProject.model.Model;
import de.pedigreeProject.model.Pedigree;

/**
 * Implementation of abstract class <code>PedigreeController</code> to update title and description the current pedigree of the model.
 *
 * @see PedigreeController
 */
public class UpdatePedigreeController extends PedigreeController {

    private final Pedigree pedigree;

    public UpdatePedigreeController(Model model) {
        super(model);
        this.pedigree = model.getCurrentPedigree();
    }

    @Override
    void init() {
        initTextFields(pedigree);
    }

    private void initTextFields(Pedigree pedigree) {
        titleTextField.setText(pedigree.getTitle());
        descriptionTextField.setText(pedigree.getDescription());
    }

    @Override
    boolean save(String title, String description) {
        return model.updatePedigreeTitleAndDescription(title, description);
    }
}
