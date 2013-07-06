package rambleonevents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import rambleon.RambleOnScreenManager;

/**
 * This Handler is for taking the user from the accounts screen to the account
 * creation screen upon indication by button press of a desire to create a new
 * user account.
 *
 * @author James C. Porcelli, SBU ID 108900819
 */
public class CreateAccountButtonHandler implements ActionListener, Serializable {
    
    //THE SCREEN MANAGER THIS HANDLER MANAGES EVENTS FOR
    private RambleOnScreenManager screenManager;

    /**
     *
     * @param screenManager
     */
    public CreateAccountButtonHandler(RambleOnScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    /**
     * Handles the event generated by a button click for the desire to
     * create a new user account.
     * 
     * @param e The event fired by the goto account creation screen button.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        screenManager.initAccountCreationScreen();
        //REMOVE THE ACCOUNTS SCREEN FROM THE MAIN WINDOW
        screenManager.getWindow().remove(screenManager.getAccountsScreen());
        //AND ADD THE ACCOUNT CREATION SCREEN
        screenManager.getWindow().add(screenManager.getAccountCreationScreen());
        //AND REFRESH THE WINDOW
        screenManager.refreshWindow();
    }
}
