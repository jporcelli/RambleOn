package rambleonevents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import rambleon.RambleOn;
import static rambleon.RambleOnSettings.*;

/**
 *
 * @author James Porcelli, SBU ID 108900819
 */
public class ExitGameButtonHandler implements ActionListener {

    private RambleOn game;

    /**
     *
     * @param game
     */
    public ExitGameButtonHandler(RambleOn game) {
        this.game = game;
    }

    /**
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        RambleOn.getScreenManager().writeData();
        game.getAudio().play(SUCCESS, false);
        game.killApplication();

    }
}
