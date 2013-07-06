package mini_game;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.Serializable;
import javax.swing.JPanel;

/**
 * Some basic event handling is provided by this class for this framework.
 * Specifically, mouse button presses on Sprites and the playing surface. Note
 * that this class makes sure to get a lock on game data using the Game class'
 * beginUsingData method, and releases the lock using the endUsingData. Game
 * developers using this framework must do the same when adding additional event
 * handlers.
 *
 * @author Richard McKenna
 * @version 1.0
 */
public class MiniGameEventRelayer implements MouseListener, MouseMotionListener, KeyListener, Serializable {
    // THE GAME FROM WHICH THE EVENTS WERE GENERATED

    private MiniGame game;

    /**
     * The constructor just sets up access to the game for which to respond to.
     *
     * @param initGame the game being played.
     */
    public MiniGameEventRelayer(MiniGame initGame) {
        game = initGame;
    }

    /**
     * This method will test to see if the mouse press was on one of the game
     * developer's registered buttons, and if it is, the appropriate event
     * handler will be executed. If not, the game program's custom response will
     * be executed in the appropriate data model class. Note that this response
     * makes sure to get a lock on the data before use.
     *
     * @param me the event object, it contains information about the user
     * interaction, like the x,y coordinates of the mouse button press.
     */
    public void mousePressed(MouseEvent me) {
        try {
            //RAMBLE ON MODIFICATIONS
            //CURRENTPAGE == GAMEPLAYPAGE && GAME NOT STARTED
            //PASS A MOUSE EVENT TO GAMEMAPNAVIGATIONHANDLER


            // LOCK THE DATA
            game.beginUsingData();

            // GET THE COORDINATES
            int x = me.getX();
            int y = me.getY();

            // FIRST CHECK THE GUI BUTTONS
            boolean buttonClicked = game.processButtonPress(x, y);

            // IF IT WAS NOT A GUI BUTTON, THEN WE SHOULD
            // EXECUTE THE CUSTOM GAME RESPONSE
            if (!buttonClicked && game.getDataModel().inProgress()) {
                MiniGameDataModel data = game.getDataModel();
                data.checkMousePressOnSprites(game, x, y, me);
            }
        } finally {
            // RELEASE THE DATA SO THAT THE TIMER THREAD MAY
            // APPROPRIATELY UPDATE AND RENDER THE GAME
            // WITHOUT INTERFERENCE
            game.endUsingData();
        }
    }

    // WE WILL NOT USE THESE METHODS
    public void mouseEntered(MouseEvent me) {
    }

    public void mouseExited(MouseEvent me) {
    }

    public void mouseReleased(MouseEvent me) {
    }

    public void mouseClicked(MouseEvent me) {
    }

    /**
     * This guy demonstrates how to get pixel information about about a
     * particular location of what's being drawing inside a panel.
     *
     * @param me Location of mouse click in canvas.0
     */
    public void mouseMoved(MouseEvent me) {
        game.getDataModel().setLastMouseX(me.getX());
        game.getDataModel().setLastMouseY(me.getY());
        game.getDataModel().updateDebugText(game);
    }

    // WE'RE NOT USING THIS GUY AT THE MOMENT, THOUGH YOU MAY CHOOSE TO
    public void mouseDragged(MouseEvent me) {
    }

    /**
     * This key handler simply activates our debug text display, which may then
     * be rendered by the canvas.
     *
     * @param ke the event object, it contains information about the user
     * interaction, like which key was pressed.
     */
    public void keyPressed(KeyEvent ke) {
        // THE 'D' KEY TOGGLES DEBUG TEXT DISPLAY
        if (ke.getKeyCode() == KeyEvent.VK_D) {
            // TOGGLE IT OFF
            if (game.getDataModel().isDebugTextRenderingActive()) {
                game.getDataModel().deactivateDebugTextRendering();
            } // TOGGLE IT ON
            else {
                game.getDataModel().activateDebugTextRendering();
            }
        } // THE 'P' KEY PAUSES THE GAME, WHICH MEANS 
        // ALL UPDATE LOGIC GETS SKIPPED
        else if (ke.getKeyCode() == KeyEvent.VK_P) {
            // TOGGLE THE OFF
            if (game.getDataModel().isPaused()) {
                game.getDataModel().unpause();
            } // TOGGLE IT ON
            else {
                game.getDataModel().pause();
            }
        }
    }

    // WE WILL NOT USE THESE METHODS
    public void keyReleased(KeyEvent ke) {
    }

    public void keyTyped(KeyEvent ke) {
    }
}