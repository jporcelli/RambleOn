package rambleonevents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import rambleon.RambleOn;
import rambleon.RambleOnDataModel;
import rambleon.RambleOnScreenManager;
import static rambleon.RambleOnSettings.*;

/**
 * Handles events for all buttons on the current account screen.
 *
 * @author James C. Porcelli, SBU ID 108900819
 */
public class CurrentAccountButtonsHandler implements ActionListener {

    //THE SCREEN MANAGER TO HANDLES EVENTS FOR
    private RambleOnScreenManager screenManager;

    /**
     * Create a new handler for the buttons on the current account screen.
     *
     * @param screenManager
     */
    public CurrentAccountButtonsHandler(RambleOnScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    /**
     * Handles all events fired by buttons on the current account screen.
     *
     * @param e The event fired by a button click on the current account screen.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //THE STRING ASSOCIATED WITH THE TOOLTIP OF THE BUTTON THAT GENERATED THE EVENT
        String associatedToolTip = e.getActionCommand();

        //FIND OUT WHICH BUTTON THE EVENT IS ASSOCIATED WITH
        switch (associatedToolTip) {
            //BACK TO ACCOUNTS PAGE
            case BACK_BUTTON_TOOLTIP: {
                //RESET THE ACCOUNTS TABLE SO NO ACCOUNT IS SELECTED
                screenManager.getAccountsTable().clearSelection();
                //REMOVE THE CURRENT ACCOUNTS SCREEN FROM THE WINDOW
                screenManager.getWindow().remove(screenManager.getCurrentAccountScreen());
                //ADD THE ACCOUNTS SCREEEN
                screenManager.getWindow().add(screenManager.getAccountsScreen());
                //AND REFRESH THE WINDOW
                screenManager.refreshWindow();
                //RESETS THE JTREE FOR THE NEXT TIME WE COME BACK TO THE CURRENT ACCOUNTS SCREN
                screenManager.selectRootNode();

                break;
            }
            //THIS TAKES US TO GAME MODE BUT WELL JUST END THE PROGRAM HERE AT THIS STAGE IN DEVELOPTMENT
            case PLAY_BUTTON_TOOLTIP: {
                //SINCE WE ARE NOT READY TO DEPLOY THE ENTIRE APP YET THIS IS WHERE OUR PROGRAM WILL END
                screenManager.getWindow().setVisible(false);
                //SAVE THE DATABASE TO THE DATASTORE FILE
                screenManager.writeData();

                //NOW WE START THE ACTUAL GAME
                RambleOn game = new RambleOn(APP_TITLE, FRAMES_PER_SEC);

                //RESET THE DATA MANAGER TO AN INITIAL STATE
                ((RambleOnDataModel) game.getData()).reset(game);
                //NOW PUT THE GAME INTO A STARTED GAME STATE
                game.startGame();
                break;
            }
        }
    }
}
