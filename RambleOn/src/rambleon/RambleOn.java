package rambleon;

import accountdata.AccountDataManager;
import audio_manager.AudioManager;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import mini_game.MiniGame;
import mini_game.MiniGameDataModel;
import mini_game.Sprite;
import mini_game.SpriteType;
import static rambleon.RambleOnSettings.*;
import rambleonevents.BackToAccountsButtonHandler;
import rambleonevents.CapitalModeButtonHandler;
import rambleonevents.CloseWinDisplayHandler;
import rambleonevents.ExitGameButtonHandler;
import rambleonevents.FlagModeButtonHandler;
import rambleonevents.HotkeyHandler;
import rambleonevents.LeaderModeButtonHandler;
import rambleonevents.NameModeButtonHandler;

/**
 * RambleOn is a Mini-Game based loosely on the popular "Stack The States" game.
 * RambleOn uses the MiniGameFramework as the framework for this mini-game.
 *
 * @author James C. Porcelli, SBU ID 108900819
 */
public class RambleOn extends MiniGame {

    //THIS APPLICATIONS AUDIO MANAGER
    private AudioManager audio;
    //THIS APPLICATIONS ACCOUNT MANAGER
    private static AccountDataManager accManager = new AccountDataManager();
    //THE APPLICATIONS SCREEN MANAGER
    private static RambleOnScreenManager screenManager;

    /**
     * Create an instance of a mini game application.
     *
     * @param appTitle The name of the game.
     * @param framesPerSecond The frame rate for the game.
     */
    public RambleOn(String appTitle, int framesPerSecond) {
        //INIT THE MINI-GAME FRAMEWORK
        super(appTitle, framesPerSecond);

        //INITIALIZE THE AUDIO MANAGER
        initAudio();
    }

    /**
     *
     * @return
     */
    public AccountDataManager getAccManager() {
        return accManager;
    }

    /**
     *
     * @param accManager
     */
    public void setAccManager(AccountDataManager accManager) {
        RambleOn.accManager = accManager;
    }

    /**
     * Initialize the audio manager for the game.
     */
    private void initAudio() {
        audio = new AudioManager();
        try {
            audio.loadAudio(TRACKED_SONG, TRACKED_FILE_NAME);
            audio.play(TRACKED_SONG, true);
            //audio.loadAudio(,);
            audio.loadAudio(SUCCESS, SUCCESS_FILE_NAME);
            audio.loadAudio(FAILURE, FAILURE_FILE_NAME);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InvalidMidiDataException | MidiUnavailableException e) {
            JOptionPane.showMessageDialog(window, e.getStackTrace());
        }
    }

    /**
     * Accessor for the mini game frameworks window which is the container for
     * the game canvas.
     *
     * @return
     */
    public JFrame getWindow() {
        return window;
    }

    /**
     * Accessor for this games audio manager.
     *
     * @return Returns this games audio manager.
     */
    public AudioManager getAudio() {
        return audio;
    }

    /**
     * Initialize the data model for the game.
     */
    @Override
    public void initData() {
        //INIT THE DATA MODEL FOR THIS GAME
        data = new RambleOnDataModel(getAccManager());

        //SET THE GAME DINMENSIONS
        data.setGameDimensions(GAME_WIDTH, GAME_HEIGHT);

        //INIT THE GAME BOUNDRYS 
        boundaryLeft = 0;
        boundaryRight = GAME_WIDTH;
        boundaryTop = 0;
        boundaryBottom = GAME_HEIGHT;
    }

    public static RambleOnScreenManager getScreenManager() {
        return screenManager;
    }

    /**
     * Accessor for the games data model.
     *
     * @return The data model for this game
     */
    public MiniGameDataModel getData() {
        return data;
    }

    /**
     * Update the GUI, which is done X FRAMES PER SECOND
     */
    @Override
    public void updateGUI() {
    }

    /**
     * Resets the game and associated data for the start of a new game.
     */
    @Override
    public void reset() {
        data.reset(this);
    }

    /**
     * Initialize all the GUI handlers for the game.
     */
    @Override
    public void initGUIHandlers() {
        //SET THE LISTENER FOR THE EXIT BUTTON
        ExitGameButtonHandler exitGameHandler = new ExitGameButtonHandler(this);
        guiButtons.get(EXIT_TYPE).setActionListener(exitGameHandler);

        //NOW THE LISTENERS FOR THE BUTTONS TO START AN ACTUAL GAME IN THE DIFFERENT
        //AVAILABLE MODES
        //
        //NAME MODE
        NameModeButtonHandler nameModeHandler = new NameModeButtonHandler(this);
        guiButtons.get(NAME_TYPE).setActionListener(nameModeHandler);
        //LEADER MODE
        LeaderModeButtonHandler leaderModeHandler = new LeaderModeButtonHandler(this);
        guiButtons.get(LEADER_TYPE).setActionListener(leaderModeHandler);
        //CAPITAL MODE
        CapitalModeButtonHandler capitalModeHandler = new CapitalModeButtonHandler(this);
        guiButtons.get(CAPITAL_TYPE).setActionListener(capitalModeHandler);
        //AND FLAG MODE
        FlagModeButtonHandler flagModeHandler = new FlagModeButtonHandler(this);
        guiButtons.get(FLAG_TYPE).setActionListener(flagModeHandler);

        //HANDLER FOR THE CLOSE [X] BUTTON ON THE END OF GAME WIN(STATS) DISPLAY 
        CloseWinDisplayHandler closeWinDisplayHandler = new CloseWinDisplayHandler(this);
        guiButtons.get(WIN_DISPLAY_CLOSE_BUTTON_TYPE).setActionListener(closeWinDisplayHandler);

        //HANDLER FOR THE BACK TO ACCOUNTS SCREENS BUTTON
        BackToAccountsButtonHandler backToAccountsHandler = new BackToAccountsButtonHandler(this);
        guiButtons.get(BACK_TO_ACCOUNTS_BUTTON_TYPE).setActionListener(backToAccountsHandler);

        //HOT KEY HANDLER FOR USE PRIMARILY WITH CHEAT CODES USED DURING TESTING
        HotkeyHandler hh = new HotkeyHandler(this);
        canvas.addKeyListener(hh);
        window.addKeyListener(hh);
    }

    /**
     * Initialize all the GUI controls for the game.
     */
    @Override
    public void initGUIControls() {
        //THE GAME CANVAS EVERYTHING WELL BE RENDERED IN AND DISPLAYED TO
        canvas = new RambleOnPanel(this, (RambleOnDataModel) data);


        // WE'LL REUSE THESE TO INIT OUR GUICONTROLS
        SpriteType sT;
        BufferedImage img;
        int x, y, vX, vY;
        Sprite s;

        //BUTTON FOR NAME MODE GAME PLAY
        sT = new SpriteType(NAME_TYPE);
        //ENABLED
        img = loadImage(ARTWORK_PATH + NAME_MODE_GO_BUTTON_FILE);
        sT.addState(ENABLED_STATE, img);
        //(DISABLED)
        img = loadImage(ARTWORK_PATH + NAME_MODE_STOP_BUTTON_FILE);
        sT.addState(DISABLED_STATE, img);
        //MOUSE OVER FOR ENABLED STATE
        img = loadImage(ARTWORK_PATH + NAME_MODE_GO_MOUSE_OVER_FILE);
        sT.addState(MOUSE_OVER_STATE_ON, img);
        //MOUSE OVER FOR DISABLED STATE
        img = loadImage(ARTWORK_PATH + NAME_MODE_STOP_MOUSE_OVER_FILE);
        sT.addState(MOUSE_OVER_STATE_OFF, img);
        //TOP LEFT CORNER BUTTON TOOLBAR
        x = GAME_WIDTH - img.getWidth();
        y = 0;
        vX = vY = 0;
        s = new Sprite(sT, x, y, 0, 0, ENABLED_STATE);
        guiButtons.put(NAME_TYPE, s);

        //BUTTON FOR CAPITAL MODE GAME PLAY
        sT = new SpriteType(CAPITAL_TYPE);
        //ENABLED
        img = loadImage(ARTWORK_PATH + CAPITAL_MODE_GO_BUTTON_FILE);
        sT.addState(ENABLED_STATE, img);
        //(DISABLED)
        img = loadImage(ARTWORK_PATH + CAPITAL_MODE_STOP_BUTTON_FILE);
        sT.addState(DISABLED_STATE, img);
        //MOUSE OVER FOR ENABLED STATE
        img = loadImage(ARTWORK_PATH + CAPITAL_MODE_GO_MOUSE_OVER_FILE);
        sT.addState(MOUSE_OVER_STATE_ON, img);
        //MOUSE OVER FOR DISABLED STATE
        img = loadImage(ARTWORK_PATH + CAPITAL_MODE_STOP_MOUSE_OVER_FILE);
        sT.addState(MOUSE_OVER_STATE_OFF, img);
        //TOP RIGHT CORNER BUTTON TOOLBAR
        x = GAME_WIDTH - (img.getWidth() * 2);
        y = 0;
        vX = vY = 0;
        s = new Sprite(sT, x, y, 0, 0, DISABLED_STATE);
        guiButtons.put(CAPITAL_TYPE, s);

        //BUTTON FOR LEADER MODE GAME PLAY
        sT = new SpriteType(LEADER_TYPE);
        //ENABLED
        img = loadImage(ARTWORK_PATH + LEADER_MODE_GO_BUTTON_FILE);
        sT.addState(ENABLED_STATE, img);
        //(DISABLED)
        img = loadImage(ARTWORK_PATH + LEADER_MODE_STOP_BUTTON_FILE);
        sT.addState(DISABLED_STATE, img);
        //MOUSE OVER FOR ENABLED STATE
        img = loadImage(ARTWORK_PATH + LEADER_MODE_GO_MOUSE_OVER_FILE);
        sT.addState(MOUSE_OVER_STATE_ON, img);
        //MOUSE OVER FOR DISABLED STATE
        img = loadImage(ARTWORK_PATH + LEADER_MODE_STOP_MOUSE_OVER_FILE);
        sT.addState(MOUSE_OVER_STATE_OFF, img);
        //BOTTOM LEFT CORNER BUTTON TOOLBAR
        x = GAME_WIDTH - img.getWidth();
        y = 100;
        vX = vY = 0;
        s = new Sprite(sT, x, y, 0, 0, DISABLED_STATE);
        guiButtons.put(LEADER_TYPE, s);

        //BUTTON FOR FLAG MODE GAME PLAY
        sT = new SpriteType(FLAG_TYPE);
        //ENABLED
        img = loadImage(ARTWORK_PATH + FLAG_MODE_GO_BUTTON_FILE);
        sT.addState(ENABLED_STATE, img);
        //(DISABLED)
        img = loadImage(ARTWORK_PATH + FLAG_MODE_STOP_BUTTON_FILE);
        sT.addState(DISABLED_STATE, img);
        //MOUSE OVER FOR ENABLED STATE
        img = loadImage(ARTWORK_PATH + FLAG_MODE_GO_MOUSE_OVER_FILE);
        sT.addState(MOUSE_OVER_STATE_ON, img);
        //MOUSE OVER FOR DISABLED STATE
        img = loadImage(ARTWORK_PATH + FLAG_MODE_STOP_MOUSE_OVER_FILE);
        sT.addState(MOUSE_OVER_STATE_OFF, img);
        //BOTTOM RIGHT CORNER BUTTON TOOLBAR
        x = GAME_WIDTH - (img.getWidth() * 2);
        y = 100;
        vX = vY = 0;
        s = new Sprite(sT, x, y, 0, 0, DISABLED_STATE);
        guiButtons.put(FLAG_TYPE, s);

        //TOOLBAR VERTICAL SEPERATOR1
        sT = new SpriteType(VERTICAL_SEPERATOR_TYPE1);
        img = loadImage(ARTWORK_PATH + VERTICAL_SEPERATOR_FILE);
        sT.addState(ENABLED_STATE, img);
        x = data.getGameWidth() - (301);
        y = 0;
        s = new Sprite(sT, x, y, 0, 0, ENABLED_STATE);
        guiDecor.put(VERTICAL_SEPERATOR_TYPE1, s);

        //TOOLBAR VERTICAL SEPERATOR2
        sT = new SpriteType(VERTICAL_SEPERATOR_TYPE2);
        img = loadImage(ARTWORK_PATH + VERTICAL_SEPERATOR_FILE);
        sT.addState(ENABLED_STATE, img);
        x = data.getGameWidth() - (1);
        y = 0;
        s = new Sprite(sT, x, y, 0, 0, ENABLED_STATE);
        guiDecor.put(VERTICAL_SEPERATOR_TYPE2, s);

        //TOOLBAR VERTICAL SEPERATOR4
        sT = new SpriteType(VERTICAL_SEPERATOR_TYPE3);
        img = loadImage(ARTWORK_PATH + VERTICAL_SEPERATOR_FILE);
        sT.addState(ENABLED_STATE, img);
        x = data.getGameWidth() - 151;
        y = 0;
        s = new Sprite(sT, x, y, 0, 0, ENABLED_STATE);
        guiDecor.put(VERTICAL_SEPERATOR_TYPE3, s);

        //TOOLBAR HORIZONTAL SEPERATOR1
        sT = new SpriteType(HORIZONTAL_SEPERATOR_TYPE1);
        img = loadImage(ARTWORK_PATH + HORIZONTAL_SEPERATOR_FILE);
        sT.addState(ENABLED_STATE, img);
        x = data.getGameWidth() - (300);
        y = 101;
        s = new Sprite(sT, x, y, 0, 0, ENABLED_STATE);
        guiDecor.put(HORIZONTAL_SEPERATOR_TYPE1, s);

        //TOOLBAR HORIZONTAL SEPERATOR2
        sT = new SpriteType(HORIZONTAL_SEPERATOR_TYPE2);
        img = loadImage(ARTWORK_PATH + HORIZONTAL_SEPERATOR_FILE);
        sT.addState(ENABLED_STATE, img);
        x = data.getGameWidth() - (300);
        y = 201;
        s = new Sprite(sT, x, y, 0, 0, ENABLED_STATE);
        guiDecor.put(HORIZONTAL_SEPERATOR_TYPE2, s);

        //TOOLBAR HORIZONTAL SEPERATOR3
        sT = new SpriteType(HORIZONTAL_SEPERATOR_TYPE3);
        img = loadImage(ARTWORK_PATH + HORIZONTAL_SEPERATOR_FILE);
        sT.addState(ENABLED_STATE, img);
        x = data.getGameWidth() - (300);
        y = 301;
        s = new Sprite(sT, x, y, 0, 0, ENABLED_STATE);
        guiDecor.put(HORIZONTAL_SEPERATOR_TYPE3, s);

        //EXIT GAME BUTTON
        sT = new SpriteType(EXIT_TYPE);
        //(ENABLED)
        img = loadImage(ARTWORK_PATH + EXIT_GAME_BUTTON_FILE);
        sT.addState(ENABLED_STATE, img);
        //MOUSE OVER FOR ENABLED STATE
        img = loadImage(ARTWORK_PATH + EXIT_GAME_MOUSE_OVER_FILE);
        sT.addState(MOUSE_OVER_STATE_ON, img);
        //EXIT GAME IS ALWAYS ENABLED
        x = data.getGameWidth() - 300;
        y = 200;
        s = new Sprite(sT, x, y, 0, 0, ENABLED_STATE);
        guiButtons.put(EXIT_TYPE, s);

        //BACK TO ACCOUNTS BUTTON
        sT = new SpriteType(BACK_TO_ACCOUNTS_BUTTON_TYPE);
        //(ENABLED)
        img = loadImage(ARTWORK_PATH + BACK_TO_ACCOUNTS_BUTTON_FILE);
        sT.addState(ENABLED_STATE, img);
        //MOUSE OVER FOR ENABLED STATE
        img = loadImage(ARTWORK_PATH + BACK_TO_ACCOUNTS_MOUSE_OVER_FILE);
        sT.addState(MOUSE_OVER_STATE_ON, img);
        //BACK TO ACCOUTNS ALWAYS ENABLED
        x = data.getGameWidth() - img.getWidth();
        y = 200;
        s = new Sprite(sT, x, y, 0, 0, ENABLED_STATE);
        guiButtons.put(BACK_TO_ACCOUNTS_BUTTON_TYPE, s);

        //STACK BACKDROP
        sT = new SpriteType(STACK_BACKDROP_TYPE);
        //(ENABLED)
        img = loadImage(ARTWORK_PATH + STACK_BACKDROP_FILE);
        sT.addState(ENABLED_STATE, img);
        //EXIT GAME IS ALWAYS ENABLED
        x = data.getGameWidth() - (img.getWidth());
        y = data.getGameHeight() - (img.getHeight() + 2);
        s = new Sprite(sT, x, y, 0, 0, ENABLED_STATE);
        guiDecor.put(STACK_BACKDROP_TYPE, s);

        //WINNING CONDITION DISPLAY
        sT = new SpriteType(WIN_DISPLAY_TYPE);
        //THE WIN DISPLAY 
        img = loadImage(ARTWORK_PATH + WIN_DISPLAY_FILE);
        sT.addState(ENABLED_STATE, img);
        x = IMAGE_WIDTH / 2 - img.getWidth() / 2;
        y = IMAGE_HEIGHT / 2 - img.getHeight() / 2;
        s = new Sprite(sT, x, y, 0, 0, ENABLED_STATE);
        guiDecor.put(WIN_DISPLAY_TYPE, s);

        //WINNING CONDITION CLOSE TOOLBAR
        sT = new SpriteType(WIN_DISPLAY_TOOLBAR_TYPE);
        //THE WIN DISPLAY CLOSE BUTTON
        img = loadImage(ARTWORK_PATH + WIN_DISPLAY_TOOLBAR_FILE);
        sT.addState(ENABLED_STATE, img);
        x = IMAGE_WIDTH / 2 + 500 / 2 - 500;
        y = IMAGE_HEIGHT / 2 - 300 / 2 - 50;
        s = new Sprite(sT, x, y, 0, 0, ENABLED_STATE);
        guiDecor.put(WIN_DISPLAY_TOOLBAR_TYPE, s);

        //WINNING CONDITION CLOSE BUTTON
        sT = new SpriteType(WIN_DISPLAY_CLOSE_BUTTON_TYPE);
        //THE WIN DISPLAY CLOSE BUTTON
        img = loadImage(ARTWORK_PATH + WIN_DISPLAY_CLOSE_BUTTON_FILE);
        sT.addState(ENABLED_STATE, img);
        sT.addState(DISABLED_STATE, null);
        x = IMAGE_WIDTH / 2 + 500 / 2 - 70;
        y = IMAGE_HEIGHT / 2 - 300 / 2 - 50;
        s = new Sprite(sT, x, y, 0, 0, DISABLED_STATE);
        guiButtons.put(WIN_DISPLAY_CLOSE_BUTTON_TYPE, s);
    }

    /**
     * Driver for this mini-game game application.
     *
     * @param args the command line arguments, which we will not use for this
     * application.
     */
    public static void main(String[] args) {
        //INITIALIZE THE SCREEN MANAGER
        screenManager = new RambleOnScreenManager(accManager);
        //THIS WILL TAKE US THROUGH ALL ACCOUNT MANAGEMENT SCREENS AS NEEDED
        //AND THEN ON TO THE ACTUAL GAME
        screenManager.startApp();
    }
}
