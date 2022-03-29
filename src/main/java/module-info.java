module pedigree.main {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires com.google.errorprone.annotations;
    requires com.google.common;
    requires org.jetbrains.annotations;
//    requires transitive org.apache.logging.log4j;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    exports de.pedigreeProject to  javafx.graphics, javafx.fxml;
    opens de.pedigreeProject to javafx.fxml;
    opens de.pedigreeProject.model to javafx.fxml;
    exports de.pedigreeProject.model;
    exports de.pedigreeProject.controller.pedigreeController;
    opens de.pedigreeProject.controller.pedigreeController to javafx.fxml;
    exports de.pedigreeProject.controller.personDataController;
    opens de.pedigreeProject.controller.personDataController to javafx.fxml;
    exports de.pedigreeProject.controller.inputRelatives;
    opens de.pedigreeProject.controller.inputRelatives to javafx.fxml;
    exports de.pedigreeProject.utils.gui_utils;
    opens de.pedigreeProject.utils.gui_utils to javafx.fxml;
    exports de.pedigreeProject.controller;
    opens de.pedigreeProject.controller to javafx.fxml;
}