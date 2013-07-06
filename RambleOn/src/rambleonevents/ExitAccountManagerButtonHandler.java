package rambleonevents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import rambleon.RambleOnScreenManager;

/**
 * Handler for exit button clicks.
 *
 * @author James C. Porcelli, SBU ID 108900819
 */
public class ExitAccountManagerButtonHandler implements ActionListener, Serializable {

    private RambleOnScreenManager screenManager;

    /**
     * Create a new handler for listening for exit button clicks.
     *
     * @param screenManager
     */
    public ExitAccountManagerButtonHandler(RambleOnScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    /**
     * Handles events for any button registered as an exit button and registered
     * with this listener
     *
     * @param e The event thrown by the exit button.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //SAVE THE APP
        screenManager.writeData();
        //THEN SHUT IT DOWN
        screenManager.killApp();
    }
}
