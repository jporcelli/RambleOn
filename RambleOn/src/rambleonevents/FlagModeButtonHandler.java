/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rambleonevents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import rambleon.RambleOn;
import rambleon.RambleOnDataModel;
import rambleon.RambleOnModeType;
import static rambleon.RambleOnSettings.*;

/**
 *
 * @author James Porcelli, SBU ID 108900819
 */
public class FlagModeButtonHandler implements ActionListener {

    private RambleOn game;

    /**
     *
     * @param game
     */
    public FlagModeButtonHandler(RambleOn game) {
        this.game = game;
    }

    /**
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //MAKE SURE THE BUTTON IS IN AN ENABLED STATE, ELSE DO NOTHIN
        if (game.getGUIButtons().get(FLAG_TYPE).getState().equals(ENABLED_STATE)
                || game.getGUIButtons().get(FLAG_TYPE).getState().equals(MOUSE_OVER_STATE_ON)) {
            ((RambleOnDataModel) game.getData()).initFlagModeStack();
            //SET THE FLAG INDICATING WE ARE DONE WITH REGION SELECTION AND
            //THEREFORE ARE IN GAME MODE, FLAG MODE TO BE EXACT
            ((RambleOnDataModel) game.getData()).setCurMode(RambleOnModeType.FLAG_MODE);
            //BEFORE WE START A GAME IN THIS MODE RETURN ANY SUB REGIONS OF THIS
            //REGION THAT WERE SET TO WHITE IN ORDER TO SHOW THEY HAVE NO PLAYABLE
            //SUB REGIONS BACK TO THEIR DEFAULT GREY SCALE COLOR
            ((RambleOnDataModel) game.getData()).setNonPlayableBackForGame();

            //PLAY THE SUCCESS SOUND WHEN AN ENABLED BUTTON IS PRESSED
            game.getAudio().play(SUCCESS, false);
        } else {
            //AND THE FAILURE SOUND WHEN A DISABLED BUTTON IS PRESSED
            game.getAudio().play(FAILURE, false);
        }
    }
}
