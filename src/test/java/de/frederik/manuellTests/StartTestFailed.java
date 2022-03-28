package de.frederik.manuellTests;

import de.pedigreeProject.Start;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
//@Disabled("test can't run automatically. You have to close alert dialogs manually.")
class StartTestFailed {
    final static org.apache.logging.log4j.Logger logger = LogManager.getLogger(StartTestFailed.class.getName());

    // Wrapper thread updates this if
    // the JavaFX application runs without a problem.
    // Declared volatile to ensure that writes are visible to every thread.
    private volatile boolean success = false;
    /**
     * Test that starter class of application launches with invalid database name.<br>
     * <p><b>NOTE: Application doesn't shutdown correctly, so run this test separately! </b></p>
     */
    @Test
    public void testMain_ShouldFail() {
        Thread thread = new Thread(() -> {
            try {
                String[] args = new String[1];
                args[0] = ":::"; // invalid database name to provoke exceptions
                Start.main(args);
//                    Application.launch(MainApp.class, args); // use if MainApp is starter class.
                success = true;
            } catch (Throwable t) {
                if (t.getCause() != null && t.getCause().getClass().equals(InterruptedException.class)) {
                    // We expect to get this exception since we interrupted
                    // the JavaFX application.
                    success = true;
                    return;
                }
                logger.error("Can't start MainApp.", t);
            }
        });
        thread.setDaemon(true);
        thread.start();
        try {
            Thread.sleep(10000);  // Wait for 10 seconds before interrupting JavaFX application to close alert dialogs manuell
        } catch (InterruptedException ignored) {
        }
        thread.interrupt();
        try {
            thread.join(1); // Wait 1 second for our wrapper thread to finish.
        } catch (InterruptedException ignored) {
        }
        assertTrue(success);
    }
}