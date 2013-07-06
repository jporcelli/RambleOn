/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rambleonevents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import rambleon.RambleOn;
import rambleon.RambleOnDataModel;
import rambleon.RambleOnModeType;
import static rambleon.RambleOnSettings.*;
import regiondata.Region;

/**
 *
 * @author James Porcelli, SBU ID 108900819
 */
public class NameModeButtonHandler implements ActionListener {

    private RambleOn game;

    /**
     *
     * @param game
     */
    public NameModeButtonHandler(RambleOn game) {
        this.game = game;
    }

    /**
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //MAKE SURE THE BUTTON IS IN AN ENABLED STATE, ELSE DO NOTHIN
        if (game.getGUIButtons().get(NAME_TYPE).getState().equals(ENABLED_STATE)
                || game.getGUIButtons().get(NAME_TYPE).getState().equals(MOUSE_OVER_STATE_ON)) {
            ((RambleOnDataModel) game.getData()).initNameModeStack();
            //SET THE FLAG INDICATING WE ARE DONE WITH REGION SELECTION AND
            //THEREFORE ARE IN GAME MODE, NAME MODE TO BE EXACT
            ((RambleOnDataModel) game.getData()).setCurMode(RambleOnModeType.NAME_MODE);
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
