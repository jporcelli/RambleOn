package rambleonevents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import rambleon.RambleOn;
import rambleon.RambleOnDataModel;
import rambleon.RambleOnModeType;
import static rambleon.RambleOnSettings.*;

/**
 *
 *
 * @author James Porcelli
 */
public class CloseWinDisplayHandler implements ActionListener {

    //THE GAME WE ARE MAKING CHANGES FOR
    private RambleOn game;

    /**
     *
     * @param game
     */
    public CloseWinDisplayHandler(RambleOn game) {
        this.game = game;
    }

    /**
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //WE ONLY RESPOND TO A MOUSE CLICK ON THE BUTTON WHEN THE WIN DISPLAY IS BEING RENDERED
        if (game.getGUIButtons().get(WIN_DISPLAY_CLOSE_BUTTON_TYPE).getState().equals(ENABLED_STATE)) {

            //SUCCES SOUND ON CLOSE
            game.getAudio().play(SUCCESS, false);

            //AND ONLY PERFORM THIS FIRST SET OF POST WIN SUB ROUTINES IF WE DIDNT JUST PLAY THE WORLD REGION
            if (!(((RambleOnDataModel) game.getData()).getDataManager().getCurrentRegion().getName().equals("World"))) {

                //STOP THE CURRENT REGION NATIONAL ANTHEM IF IT IS PLAYING 
                if ((!(((RambleOnDataModel) game.getData()).getCurMode() == RambleOnModeType.FLAG_MODE))
                        && (!(((RambleOnDataModel) game.getData()).getCurMode() == RambleOnModeType.LEADER_MODE))
                        && (!(((RambleOnDataModel) game.getData())).getDataManager().getCurrentRegion().getName().equals("Africa"))
                        && (!(((RambleOnDataModel) game.getData())).getDataManager().getCurrentRegion().getName().equals("North America"))
                        && (!(((RambleOnDataModel) game.getData())).getDataManager().getCurrentRegion().getName().equals("South America"))
                        && (!(((RambleOnDataModel) game.getData())).getDataManager().getCurrentRegion().getName().equals("Europe"))
                        && (!(((RambleOnDataModel) game.getData())).getDataManager().getCurrentRegion().getName().equals("South Africa"))
                        && (!(((RambleOnDataModel) game.getData())).getDataManager().getCurrentRegion().getName().equals("Asia"))
                        && (!(((RambleOnDataModel) game.getData())).getDataManager().getCurrentRegion().getName().equals("Antartica"))
                        && (!(((RambleOnDataModel) game.getData())).getDataManager().getCurrentRegion().getName().equals("Oceania"))) {

                    //NATIONAL ANTHEMS ARE NOT PLAYED FOR CONTNENT OR WORLD GAMES
                    game.getAudio().stop(((RambleOnDataModel) game.getData()).getDataManager().getCurrentRegion().getName());
                    //AND SET THE CURRENT AUDIO BACK TO THE TRACKED SONG ON LOOP
                    game.getAudio().play(TRACKED_SONG, true);
                }
                //RESET THE CURRENT GAME MODE TO 'REGION SELECTION'
                ((RambleOnDataModel) game.getData()).setCurMode(RambleOnModeType.REGION_SELECTION);
                //SIMULATE A RIGHT CLICK I.E GO BACK TO THE PARENT REGION
                ((RambleOnDataModel) game.getData()).handleRightClick(0, 0);
            } else {
                //IF WE JUST PLAYED THE WORLD AKA THE PARENT REGION IN THE REGION HEIRARCHY
                //THEN WELL JUST RESET THE GAME 
                game.reset();
            }
            //RESET THE GAME STATE TO IN PROGRESS INSTEAD OF 'WIN' I.E OVER
            game.getData().beginGame();

            //AND RESET THE WIN DISPLAY CLOSE BUTTON TO DISABLED AFTER WE HANDLE AN EVENT FOR IT
            game.getGUIButtons().get(WIN_DISPLAY_CLOSE_BUTTON_TYPE).setState(DISABLED_STATE);
        }
    }
}
