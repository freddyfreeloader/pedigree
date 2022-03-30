module pedigreeModule {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires com.google.errorprone.annotations;
    requires com.google.common;
    requires org.jetbrains.annotations;
    requires org.apache.logging.log4j;

    exports de.pedigreeProject;
    opens de.pedigreeProject to javafx.fxml, javafx.graphics;
    opens de.pedigreeProject.model to javafx.fxml;
    opens de.pedigreeProject.controller.personDataController to javafx.fxml;
    opens de.pedigreeProject.controller to javafx.fxml;
    opens de.pedigreeProject.controller.inputRelatives to javafx.fxml;
    opens de.pedigreeProject.controller.pedigreeController to javafx.fxml;
}