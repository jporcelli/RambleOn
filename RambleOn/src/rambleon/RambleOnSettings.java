package rambleon;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * RambleOnSettings defines all necessary game settings for RambleOn including
 * String constant values, data constant values, and file paths for externally
 * stored game data which we will be utilizing in this application.
 *
 * @author James C. Porcelli, SBU ID 108900819
 */
public class RambleOnSettings {

    public static final String APP_TITLE = "Ramble On";
    public static final int FRAMES_PER_SEC = 30;
    // THESE CONSTANTS SETUP THE GAME DIMENSIONS. THE GAME WIDTH
    // AND HEIGHT SHOULD MIRROR THE BACKGROUND IMAGE DIMENSIONS. WE
    // WILL NOT RENDER ANYTHING OUTSIDE THOSE BOUNDS.
    public static final int GAME_WIDTH = 1200;
    public static final int GAME_HEIGHT = 700;
    //REGION MAP DIMENSIONS
    public static final int IMAGE_WIDTH = 900;
    public static final int IMAGE_HEIGHT = 700;
    //THESE ARE THE FOUR DIFFERENT GAME MODES AVAILABLE TO PLAYERS OF THIS GAME
    public static final String NAME_MODE = "NAME_MODE";
    public static final String FLAG_MODE = "FLAG_MODE";
    public static final String CAPITAL_MODE = "CAPITAL_MODE";
    public static final String LEADER_MODE = "LEADER_MODE";
    //THE NUMBER OF DIFFERENT STATS AND MODES
    public static final int NUM_GAME_MODES = 4;
    public static final int NUM_GAME_STATS = 3;
    //AN INITITIAL STATS VALUE
    public static final int INIT_STAT_VALUE = 0;
    //THESE ARE THE GAME STATS WE WILL BE KEEPING TRACK OF
    public static final String GAMES_PLAYED = "GAMES_PLAYED";
    public static final String HIGH_SCORE = "HIGH_SCORE";
    public static final String FASTEST_TIME = "FASTEST_TIME";
    //THE PATH TO ALL THE ARTWORK FOR THIS APP
    public static final String ARTWORK_PATH = "./data/artwork/";
    //THE PATH TO THE ACCOUNTS DATA FILE
    public static final String DATA_PATH = "./data/accounts/";
    //THE PATH TO THE AUDIO FILES
    public static final String AUDIO_DIR = "./data/audio/";
    //AND THE SUFFIX TO ALL THE REGION NATIONAL ANTHEMS
    public static final String NATIONAL_ANTHEM = " National Anthem.mid";
    //THE DIFFERENT STATES POSSIBLE FOR A SPRITE
    public static final String VISIBLE_STATE = "VISIBLE";
    public static final String INVISIBLE_STATE = "INVISIBLE_STATE";
    public static final String ENABLED_STATE = "ENABLED_STATE";
    public static final String DISABLED_STATE = "DISABLED_STATE";
    public static final String MOUSE_OVER_STATE_ON = "MOUSE_OVER_STATE_ON";
    public static final String MOUSE_OVER_STATE_OFF = "MOUSE_OVER_STATE_OFF";
    //SPRITE TYPES
    public static final String LEADER_TYPE = "LEADER_TYPE";
    public static final String CAPITAL_TYPE = "CAPITAL_TYPE";
    public static final String FLAG_TYPE = "FLAG_TYPE";
    public static final String NAME_TYPE = "NAME_TYPE";
    public static final String HORIZONTAL_SEPERATOR_TYPE1 = "HORIZONTAL_SEPERATOR_TYPE1";
    public static final String HORIZONTAL_SEPERATOR_TYPE2 = "HORIZONTAL_SEPERATOR_TYPE2";
    public static final String HORIZONTAL_SEPERATOR_TYPE3 = "HORIZONTAL_SEPERATOR_TYPE3";
    public static final String VERTICAL_SEPERATOR_TYPE1 = "VERTICAL_SEPERATOR_TYPE1";
    public static final String VERTICAL_SEPERATOR_TYPE2 = "VERTICAL_SEPERATOR_TYPE2";
    public static final String VERTICAL_SEPERATOR_TYPE3 = "VERTICAL_SEPERATOR_TYPE3";
    public static final String VERTICAL_SEPERATOR_TYPE4 = "VERTICAL_SEPERATOR_TYPE4";
    public static final String PLAY_TYPE = "PLAY_TYPE";
    public static final String EXIT_TYPE = "EXIT_TYPE";
    public static final String STACK_BACKDROP_TYPE = "STACK_BACKDROP_TYPE";
    public static final String WIN_DISPLAY_TYPE = "WIN_TYPE";
    public static final String WIN_DISPLAY_TOOLBAR_TYPE = "WIN_CLOSE_TOOLBAR_TYPE";
    public static final String WIN_DISPLAY_CLOSE_BUTTON_TYPE = "WIN_CLOSE_BUTTON_TYPE";
    public static final String BACK_TO_ACCOUNTS_BUTTON_TYPE = "BACK_TO_ACCOUNTS_TYPE";
    //IMAGE NAMES
    public static final String WELCOME_SCREEN = "WELCOME_SCREEN";
    public static final String NEW_ACCOUNT_BUTTON_ICON = "ADD_USER";
    public static final String ACCOUNT_MENU_BANNER = "ACCOUNT_MENU_BANNER";
    public static final String CURRENT_ACCOUNT_BACK_BUTTON = "CURRENT_ACCOUNT_BACK_BUTTON";
    public static final String CURRENT_ACCOUNT_START_BUTTON = "CURRENT_ACCOUNT_START_BUTTON";
    public static final String CURRENT_ACCOUNT_STATS_TITLE = "CURRENT_ACCOUNT_STATS_TITLE";
    public static final String EXIT_BUTTON = "EXIT_BUTTON";
    public static final String CREATE_ACCOUNT_SCREEN_BANNER = "CREATE_ACCOUNT_SCREEN_BANNER";
    //AUDIO FILE NAME
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILURE = "FAILURE";
    public static final String TRACKED_SONG = "TRACKED_SONG";
    //MOUSE OVER TOOL TIPS
    public static final String WELCOME_SCREEN_TOOLTIP = "Click to Play";
    public static final String NEW_USER_TOOLTIP = "Create New User";
    public static final String BACK_BUTTON_TOOLTIP = "Back to Previous Page";
    public static final String PLAY_BUTTON_TOOLTIP = "Play Game";
    public static final String EXIT_BUTTON_TOOLTIP = "Exit";
    public static final String CREATE_ACCOUNT_OK_TOOLTIP = "Create New Account";
    public static final String CREATE_ACCOUNT_CANCEL_TOOLTIP = "Cancel New Account Creation";
    //INSETS, TOOLBARS, AND OTHER SETTINGS
    public static final int BUTTON_INSET = 1;
    public static final Border TOOLBAR_BORDER = BorderFactory.createRaisedBevelBorder();
    //FONTS
    public static final Font HEADER_FONT = new Font("Serif", Font.BOLD, 35);
    public static final Font LABEL_FONT = new Font("Serif", Font.BOLD, 20);
    public static final Font STATS_TABLE_LABEL_FONT = new Font("Serif", Font.BOLD, 30);
    public static final Font WIN_STATS_FONT = new Font("Sans Serif", Font.BOLD, 24);
    public static final Font WIN_EQUATION_FONT = new Font("Monospaced", Font.BOLD, 16);
    //STRING LABELS
    public static final String RAMBLE_ON_LABEL = "Ramble On";
    public static final String ACCOUNT_CREATION_PAGE_TITLE_HEADER = "New Account Creation";
    public static final String ACCOUNTS_PAGE_TITLE_HEADER = "RambleOn Player Accounts";
    public static final String CREATE_NEWUSER_LABEL = "Create New User";
    public static final String REGIONS_PLAYED_ROOT = "World";
    public static final String ASIA_SUBTREE_ROOT = "Asia";
    public static final String EUROPE_SUBTREE_ROOT = "Europe";
    public static final String SOUTH_AMERICA_SUBTREE_ROOT = "SouthAmerica";
    public static final String NORTH_AMERICA_SUBTREE_ROOT = "NorthAmerica";
    public static final String AFRICA_SUBTREE_ROOT = "Africa";
    public static final String ANTARTICA_SUBTREE_ROOT = "Antartica";
    public static final String AUSTRALIA_SUBTREE_ROOT = "Australia";
    public static final String BACK_TO_ACCOUNTS_PAGE_LABEL = "Back To Player Accounts";
    public static final String FOWARD_TO_GAME_MODE_LABEL = "Foward To Play RambleOn";
    public static final String CURRENT_ACCOUNT_PAGE_TITLE_HEADER = "                              Account Overview";
    public static final String SUB_REGION_MODE_LABEL = "Sub Region Mode:";
    public static final String FLAG_MODE_LABEL = "Flag Mode:";
    public static final String CAPITAL_MODE_LABEL = "Capital Mode:";
    public static final String LEADER_MODE_LABEL = "Leader Mode:";
    public static final String FASTEST_TIME_STAT_LABEL = "Fastest Time";
    public static final String TIMES_PLAYES_STAT_LABEL = "Times Played";
    public static final String HIGH_SCORE_STAT_LABEL = "High Score";
    public static final String PLAYER_ACCOUNTS_LABEL = "                                   Player Accounts";
    public static final String PLAYER_STATS_LABEL = "Player Stats";
    public static final String CREATE_NEW_USER_ACCOUNT_LABEL = "Create A New User Account";
    public static final String SPECIFY_USER_NAME_LABEL = "Choose A Non-Existing UserName";
    //INSETS
    public static final Insets INSETS = new Insets(5, 15, 5, 15);
    //RAMBLE ON COLOR SCHEME COLORS
    public static final Color RAMBLE_ON_BLUE = new Color(3, 44, 152);
    public static final Color RAMBLE_ON_GREEN = new Color(116, 220, 89);
    public static final Color REGION_NAME_COLOR = new Color(220, 220, 80);
    public static final Color SUBREGION_NAME_COLOR = new Color(10, 10, 100);
    public static final Color REGION_MAP_ORANGE = new Color(220, 100, 0);
    public static final Color REGION_MAP_WATER = new Color(0, 162, 232);
    public static final Color WIN_STATS_COLOR = new Color(0, 255, 00);
    //X PAD, Y PAD VALUES
    public static final int IPADX = 5;
    public static final int IPADY = 10;
    //DATA VALUES
    public static final int SPLIT_PANE_LEFT_LOCATION = 400;
    public static final int FRAMES_PER_SECOND = 30;
    public static final int STATS_TABLE_ROW_HEIGHT = 60;
    public static final int ACCOUNTS_TABLE_ROW_HEIGHT = 75;
    public static final int NUM_TEXT_SPRITES_ON_STACK = 8;
    public static final int SUB_STACK_VELOCITY = 5;
    public static final int FIRST_REGION_Y_IN_STACK = GAME_HEIGHT - 50;
    public static final int FIRST_LEVEL_OF_TREE = 0;
    public static final int WIN_STATS_X = IMAGE_WIDTH / 2 - 245;
    public static final int WIN_STATS_START_Y = IMAGE_HEIGHT / 2 - 75;
    public static final int WIN_STATS_Y_INC = 40;
    public static final int MAX_SCORE = 10000;
    //IMAGES AND FILES
    public static final String DATA_FILE = "data.txt";
    public static final String WELCOME_SCREEN_FILE = "welcome.png";
    public static final String NEW_ACCOUNT_BUTTON_ICON_FILE = "add_button.png";
    public static final String ACCOUNT_MENU_BANNER_FILE = "accounts_screen_txt.png";
    public static final String CURRENT_ACCOUNT_BACK_BUTTON_FILE = "prev_page.png";
    public static final String CURRENT_ACCOUNT_START_BUTTON_FILE = "next_page.png";
    public static final String CURRENT_ACCOUNT_STATS_TITLE_FILE = "player_stats.png";
    public static final String EXIT_BUTTON_FILE = "exit_accounts.png";
    public static final String CREATE_ACCOUNT_SCREEN_BANNER_FILE = "create_account_screen_txt.png";
    public static final String FLAG_MODE_GO_BUTTON_FILE = "flag_mode_go.png";
    public static final String FLAG_MODE_GO_MOUSE_OVER_FILE = "flag_mode_go_mouse_over.png";
    public static final String FLAG_MODE_STOP_MOUSE_OVER_FILE = "flag_mode_stop_mouse_over.png";
    public static final String FLAG_MODE_STOP_BUTTON_FILE = "flag_mode_stop.png";
    public static final String LEADER_MODE_GO_BUTTON_FILE = "leader_mode_go.png";
    public static final String LEADER_MODE_GO_MOUSE_OVER_FILE = "leader_mode_on_mouse_over.png";
    public static final String LEADER_MODE_STOP_MOUSE_OVER_FILE = "leader_mode_stop_mouse_over.png";
    public static final String LEADER_MODE_STOP_BUTTON_FILE = "leader_mode_stop.png";
    public static final String CAPITAL_MODE_GO_BUTTON_FILE = "capital_mode_go.png";
    public static final String CAPITAL_MODE_GO_MOUSE_OVER_FILE = "capital_mode_on_mouse_over.png";
    public static final String CAPITAL_MODE_STOP_MOUSE_OVER_FILE = "capital_mode_off_mouse_over.png";
    public static final String CAPITAL_MODE_STOP_BUTTON_FILE = "capital_mode_stop.png";
    public static final String NAME_MODE_GO_BUTTON_FILE = "name_mode_go.png";
    public static final String NAME_MODE_GO_MOUSE_OVER_FILE = "name_mode_go_mouse_over.png";
    public static final String NAME_MODE_STOP_MOUSE_OVER_FILE = "name_mode_stop_mouse_over.png";
    public static final String NAME_MODE_STOP_BUTTON_FILE = "name_mode_stop.png";
    public static final String DISABLED_BUTTON_FILE = "disabled.png";
    public static final String HORIZONTAL_SEPERATOR_FILE = "toolbar_horizontal_seperator.png";
    public static final String VERTICAL_SEPERATOR_FILE = "toolbar_vertical_seperator.png";
    public static final String VERTICAL_SEPERATOR_SMALL_FILE = "toolbar_vertical_seperator_small.png";
    public static final String VERTICAL_SEPERATOR_SMALLEST_FILE = "toolbar_vertical_seperator_smallest.png";
    public static final String PLAY_GAME_GO_BUTTON_FILE = "play_go.png";
    public static final String PLAY_GAME_STOP_BUTTON_FILE = "play_stop.png";
    //public static final String EXIT_GAME_BUTTON_FILE = "exit.png";
    public static final String EXIT_GAME_BUTTON_FILE = "exit_small.png";
    //public static final String EXIT_GAME_MOUSE_OVER_FILE = "exit_mouse_over.png";
    public static final String EXIT_GAME_MOUSE_OVER_FILE = "exit_small_mouse_over.png";
    public static final String BACK_TO_ACCOUNTS_BUTTON_FILE = "back_to_accounts.png";
    public static final String BACK_TO_ACCOUNTS_MOUSE_OVER_FILE = "back_to_accounts_mouse_over.png";
    public static final String STACK_BACKDROP_FILE = "stack_backdrop.png";
    public static final String SUCCESS_FILE_NAME = AUDIO_DIR + "button_click_good.wav";
    public static final String FAILURE_FILE_NAME = AUDIO_DIR + "button_click_bad.wav";
    public static final String TRACKED_FILE_NAME = AUDIO_DIR + "RambleOn.mid";
    public static final String WIN_DISPLAY_FILE = "win_display.png";
    public static final String WIN_DISPLAY_TOOLBAR_FILE = "win_close_toolbar.png";
    public static final String WIN_DISPLAY_CLOSE_BUTTON_FILE = "win_close_button.png";
    //ERROR MESSAGES
    public static final String XML_LOAD_FAILURE = "Fatal Error Loading XML Data File, Program Terminating";
}
