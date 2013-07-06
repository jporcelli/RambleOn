package rambleonevents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import rambleon.RambleOnScreenManager;

/**
 * Event Handler for the welcome screen of the RambleOn application
 *
 * @author James C. Porcelli, SBU ID 108900819
 */
public class WelcomePageHandler implements ActionListener, Serializable {

    private RambleOnScreenManager screenManager;

    /**
     *
     * @param game The RambleOn game that we will respond to events for.
     */
    public WelcomePageHandler(RambleOnScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    /**
     *
     * @param e The event fired by the WelcomeScreen in response to a mouse
     * click
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //REMOVE THE CURRENT PANEL
        screenManager.getWindow().remove(screenManager.getWelcomeScreen());
        //AND LOAD THE NEXT SCREEN, THE ACCOUNT SCREEN
        screenManager.getWindow().add(screenManager.getAccountsScreen());
        //REFRSESH THE WINDOW TO PROPERLY LOAD THE NEXT PANEL
        screenManager.refreshWindow();
    }
}
