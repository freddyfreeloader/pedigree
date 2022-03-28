package de.frederik.manuellTests;

import de.pedigreeProject.Start;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
//@Disabled("test can't run automatically.")
class StartTest {
    final static org.apache.logging.log4j.Logger logger = LogManager.getLogger(StartTest.class.getName());

    // Wrapper thread updates this if
    // the JavaFX application runs without a problem.
    // Declared volatile to ensure that writes are visible to every thread.
    private volatile boolean success = false;

    /**
     * Test that starter class of application launches.<br>
     * <p><b>NOTE: Application doesn't shutdown correctly, so run this test separately! </b></p>
     */
    @Test
    public void testMain() {
        Thread thread = new Thread() { // Wrapper thread.
            @Override
            public void run() {
                try {
                    Start.main(null);
//                    Application.launch(MainApp.class, args); // use if MainApp is starter class.
                    success = true;
                } catch (Throwable t) {
                    if (t.getCause() != null && t.getCause().getClass().equals(InterruptedException.class)) {
                        // We expect to get this exception since we interrupted
                        // the JavaFX application.
                        success = true;
                        Platform.exit();
                        return;
                    }
                    logger.error("Can't start MainApp.", t);
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        try {
            Thread.sleep(3000);  // Wait for 3 seconds before interrupting JavaFX application
        } catch (InterruptedException ignored) {
        }
        thread.interrupt();
        try {
            thread.join(1); // Wait 1 second for our wrapper thread to finish.
        } catch (InterruptedException ignored) {
        }
        assertTrue(success);
        Platform.exit();
    }
}