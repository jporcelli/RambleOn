package rambleonevents;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import rambleon.RambleOn;
import rambleon.RambleOnDataModel;

/**
 *
 * @author McKillaGorilla, James Porcelli, SBU ID 108900819
 */
public class HotkeyHandler implements KeyListener {

    private RambleOn game;

    public HotkeyHandler(RambleOn initGame) {
        game = initGame;
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

    /**
     * Cheat code for skipping to end of game play except one region. Code is
     * control-C.
     *
     * @param ke
     */
    @Override
    public void keyPressed(KeyEvent ke) {
        int keyCode = ke.getKeyCode();
        if (keyCode == KeyEvent.VK_C) {
            try {
                game.beginUsingData();
                RambleOnDataModel dataModel = (RambleOnDataModel) (game.getDataModel());
                dataModel.removeAllButOneFromeStack();
            } finally {
                game.endUsingData();
            }
        }
    }
}
