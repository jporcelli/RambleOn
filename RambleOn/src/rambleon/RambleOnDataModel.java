package rambleon;

import accountdata.Account;
import accountdata.AccountDataManager;
import accountdata.AccountStatistics;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import mini_game.MiniGame;
import mini_game.MiniGameDataModel;
import mini_game.Sprite;
import mini_game.SpriteType;
import static rambleon.RambleOnSettings.*;
import rambleonsprites.TextualSprite;
import regiondata.Region;
import regiondata.RegionDataManager;
import regiondata.RegionIO;
import static regiondata.RegionIOSettings.*;
import xml_utilities.InvalidXMLFileFormatException;

/**
 * This class represents the data model for our game application. This class is
 * concerned with the data for the application, and only with the data. We
 * manage game play data such as account data, as well as all region data i.e
 * regions maps, and pixel values. This class is also responsible for keeping
 * data pertaining to the current game i.e the score and statistics until the
 * current game finishes and the data is passed off to the accounts manager for
 * storage.
 *
 * @author James C. Porcelli, SBU ID 108900819
 */
public class RambleOnDataModel extends MiniGameDataModel {

    //REGION DATA/MAPS IMPORTER
    private RegionIO regionImporter;
    //THE SCHEMA FOR REGION DATA XML FILES
    private final File xmlSchema = new File(REGION_DATA_SCHEMA);
    //THE WORLD MAP
    private final File theWorldFile = new File(WORLD_DIR + THE_WORLD + XML_DATA_FILE);
    //THE FILE TO THE CURRENT REGIONS DATA
    private File currentRegionFile;
    //THE FILE TO THE PARENT OF THE CURRENT REGIONS DATA
    private File parentOfCurrentFile;
    //A STACK CONTAINING THE PARENTS USED TO TRAVERSE TO THE CURRENT LEVEL IN THE REGION HEIRARCHY
    private Stack<File> parentFiles;
    //GAME DATA MANAGERS
    private AccountDataManager accountsManager;
    private RegionDataManager dataManager;
    //MAPPINGS TO EACH REGIONS GREY SCALE VALUE
    private HashMap<Color, String> colorToSubRegionMappings;
    private HashMap<String, Color> subRegionToColorMappings;
    //EACH REGIONS PIXEL LIST
    private HashMap<String, ArrayList<Point2D.Double>> pixels;
    //LIST OF REGIONS CURRENTLY IN RED STATE
    private LinkedList<String> redSubRegions;
    //REGION NAMES GAME STACK
    private LinkedList<TextualSprite> nameModeStack;
    //REGION LEADERS GAME STACK
    private LinkedList<Sprite> leaderModeStack;
    //REGION FLAG STACK
    private LinkedList<Sprite> flagModeStack;
    //REGION CAPITALS STACK
    private LinkedList<TextualSprite> capitalModeStack;
    //START TIME FOR THIS GAME 
    private GregorianCalendar startTime;
    //END TIME OF GAME WIN
    private GregorianCalendar winEndingTime;
    //NUMBER OF INCORRECT GUESSES
    private int incorrectGuesses;
    //CURRENT GAME MODE ENUM VALUE
    private RambleOnModeType curMode;

    /**
     * Initialize the games data model and all the corresponding data structures
     * that make up that model. As well since we only construct this model once
     * per application run we initialize the manager by loading the world map.
     * Once we have the world map in the manager we can move forward with the
     * application.
     */
    public RambleOnDataModel(AccountDataManager accManager) {
        //INIT THE DATA MANAGERS
        accountsManager = accManager;
        dataManager = new RegionDataManager();
        //INIT THE IMPORTER
        regionImporter = new RegionIO(xmlSchema);
        //INIT THE LIST DATA STRUCTURES
        colorToSubRegionMappings = new HashMap<>();
        subRegionToColorMappings = new HashMap<>();
        pixels = new HashMap<>();
        redSubRegions = new LinkedList<>();
        nameModeStack = new LinkedList<>();
        leaderModeStack = new LinkedList<>();
        capitalModeStack = new LinkedList<>();
        flagModeStack = new LinkedList<>();
        startTime = new GregorianCalendar();
        winEndingTime = new GregorianCalendar();
        //INIT THE INCORRECT GUESSES VALUE
        incorrectGuesses = 0;
        //INIT THE PARENT FILES STACK
        parentFiles = new Stack<>();
        //WE ALWAYS START AT THE WORLD
        currentRegionFile = theWorldFile;
        //WE START IN REGION SELECTION MODE
        curMode = RambleOnModeType.REGION_SELECTION;

    }

    //WE DONT NEED JAVADOC DOCUMENTATION FOR GETTER/SETTERS 
    //OR PRIVATE METHODS
    /*
     * 
     * @return 
     */
    public File getCurrentRegionFile() {
        return currentRegionFile;
    }

    /**
     *
     * @return
     */
    public RambleOnModeType getCurMode() {
        return curMode;
    }

    /**
     *
     * @param curMode
     */
    public void setCurMode(RambleOnModeType curMode) {
        this.curMode = curMode;
    }

    /*
     * 
     * @param currentRegionFile 
     */
    public void setCurrentRegionFile(File currentRegionFile) {
        this.currentRegionFile = currentRegionFile;
    }

    /*
     * 
     * @return 
     */
    public File getParentOfCurrentFile() {
        return parentOfCurrentFile;
    }

    /*
     * 
     * @param parentOfCurrentFile 
     */
    public void setParentOfCurrentFile(File parentOfCurrentFile) {
        this.parentOfCurrentFile = parentOfCurrentFile;
    }

    /*
     *
     * @return
     */
    public AccountDataManager getAccountsManager() {
        return accountsManager;
    }

    /*
     *
     * @return
     */
    public RegionDataManager getDataManager() {
        return dataManager;
    }

    /*
     *
     * @return
     */
    public HashMap<Color, String> getColorToSubRegionMappings() {
        return colorToSubRegionMappings;
    }

    /*
     *
     * @return
     */
    public HashMap<String, Color> getSubRegionToColorMappings() {
        return subRegionToColorMappings;
    }

    /*
     *
     * @return
     */
    public HashMap<String, ArrayList<Double>> getPixels() {
        return pixels;
    }

    /*
     *
     * @return
     */
    public LinkedList<String> getRedSubRegions() {
        return redSubRegions;
    }

    /*
     *
     * @return
     */
    public LinkedList<TextualSprite> getNameModeStack() {
        return nameModeStack;
    }

    /**
     *
     * @return
     */
    public LinkedList<Sprite> getLeaderModeStack() {
        return leaderModeStack;
    }

    /**
     *
     * @return
     */
    public LinkedList<Sprite> getFlagModeStack() {
        return flagModeStack;
    }

    public LinkedList<TextualSprite> getCapitalModeStack() {
        return capitalModeStack;
    }

    /*
     *
     * @return
     */
    public GregorianCalendar getStartTime() {
        return startTime;
    }

    /*
     *
     * @param startTime
     */
    public void setStartTime(GregorianCalendar startTime) {
        this.startTime = startTime;
    }

    /*
     *
     * @return
     */
    public GregorianCalendar getWinEndingTime() {
        return winEndingTime;
    }

    /*
     *
     * @param winEndingTime
     */
    public void setWinEndingTime(GregorianCalendar winEndingTime) {
        this.winEndingTime = winEndingTime;
    }

    /*
     *
     * @return
     */
    public int getIncorrectGuesses() {
        return incorrectGuesses;
    }

    /*
     *
     * @param incorrectGuesses
     */
    public void setIncorrectGuesses(int incorrectGuesses) {
        this.incorrectGuesses = incorrectGuesses;
    }

    /*
     * 
     * @return 
     */
    public int calculateScore() {
        int points = MAX_SCORE;
        long numMilliseconds = winEndingTime.getTimeInMillis() - startTime.getTimeInMillis();
        long numSeconds = numMilliseconds / 1000L;
        points -= numSeconds;
        points -= (100 * incorrectGuesses);
        return points;
    }

    /*
     * 
     * @return 
     */
    public String getGameWinDurationText() {
        long numMilliseconds = winEndingTime.getTimeInMillis() - startTime.getTimeInMillis();
        long numSeconds = numMilliseconds / 1000L;
        return getSecondsAsTimeText(numSeconds);
    }

    /*
     * 
     * @param numSeconds
     * @return 
     */
    public String getSecondsAsTimeText(long numSeconds) {
        long numHours = numSeconds / 3600;
        numSeconds = numSeconds - (numHours * 3600);
        long numMinutes = numSeconds / 60;
        numSeconds = numSeconds - (numMinutes * 60);

        String timeText = "";
        if (numHours > 0) {
            timeText += numHours + ":";
        }
        timeText += numMinutes + ":";
        if (numSeconds < 10) {
            timeText += "0" + numSeconds;
        } else {
            timeText += numSeconds;
        }
        return timeText;
    }

    /*
     * 
     * @return 
     */
    public String getGameTimeText() {
        if (startTime == null) {
            return "";
        }
        GregorianCalendar now = new GregorianCalendar();
        long diff = now.getTimeInMillis() - startTime.getTimeInMillis();
        long numSeconds = diff / 1000L;

        return getSecondsAsTimeText(numSeconds);
    }

    /*
     * 
     * @return 
     */
    public int getRegionsFound() {
        return colorToSubRegionMappings.keySet().size() - curModeStack().size();
    }

    /*
     * 
     * @return 
     */
    public int getRegionsNotFound() {
        return curModeStack().size();
    }

    /**
     *
     * @param game
     */
    @Override
    public void updateDebugText(MiniGame game) {
    }

    /**
     * This method is a hook into the data model from the game/canvas that
     * enables an involuntary check of certain data that may change often. We
     * can then use that data which is updated in real time to update any GUI
     * buttons or decor that may have undergone a state change.
     *
     * @param game The game we need to make changes to, and for which we are
     * storing data.
     */
    @Override
    public void updateAll(MiniGame game) {
        if (getCurMode() == RambleOnModeType.REGION_SELECTION) {
            //CHECK IS CAPITALS ARE AVAILABLE FOR THE CURRENT REGION
            if (dataManager.capitalsAvailable()) {
                //IF THEY ARE THEN REFLECT THIS IN THE CAPITAL MODE BUTTONS STATE
                game.getGUIButtons().get(CAPITAL_TYPE).setState(ENABLED_STATE);
            } else {
                //IF NOT THEN SIMILARILY WE NEED TO REFLECT THE UNAVAILABILITY IN
                //THE BUTTONS STATE
                game.getGUIButtons().get(CAPITAL_TYPE).setState(DISABLED_STATE);
            }

            //IF THE DATA STRUCTURE HOLDING THE FLAG IMAGES IS NOT EMPTY
            if (!(dataManager.getSubRegionFlags().isEmpty())) {
                //THEN FLAG MODE SHOULD BE AVAILABLE FOR PLAY AND THEREFORE REFLECT
                //THAT IN THE FLAG MODE BUTTON
                game.getGUIButtons().get(FLAG_TYPE).setState(ENABLED_STATE);
            } else {
                //FLAG MODE NOT AVAILABLE
                game.getGUIButtons().get(FLAG_TYPE).setState(DISABLED_STATE);
            }

            //CHECK IS LEADER MODE IS AVAILABLE
            if (!(dataManager.getSubRegionLeaders().isEmpty())) {
                //IF IT IS REFLECT THAT AVAILABILITY IN THE LEADER MODE BUTTON
                game.getGUIButtons().get(LEADER_TYPE).setState(ENABLED_STATE);
            } else {
                //LEADER MODE NOT AVAILABLE
                game.getGUIButtons().get(LEADER_TYPE).setState(DISABLED_STATE);
            }

            //THE ONLY TIME NAME MODE IS NOT AVAILABLE ON A VALID REGION IS FOR
            //THE WORLD MAP I.E THE BASE CLASS REGION FOR ALL REGIONS
            //
            //WE CAN NOT START A GAME FROM THE WORLD MAP SO ALL MODES ARE DISABLED
            if (true) {
                //BUT FOR ANY OTHER VALID REGION BESIDES THE WORLD WE AT LEAST
                //HAVE NAME MODE AVAILABLE OR THE REGION IS INVALID
                game.getGUIButtons().get(NAME_TYPE).setState(ENABLED_STATE);
            } else {
                //SO DISABLE NAME MODE FOR THE WORLD AND ALL INVALID REGIONS
                //CONSEQUENTLY ALL OTHER MODES ARE DISABLED AS WELL 
                game.getGUIButtons().get(NAME_TYPE).setState(DISABLED_STATE);
            }
        }

        //IF WE ARE NOT IN REGION SELECTION WE ARE IN GAME PLAY MODE
        if (getCurMode() != RambleOnModeType.REGION_SELECTION) {
            //SO UPDATE ALL THE STACK SPRITES AT ALL TIMES. WHAT ACTUALLY
            //HAPPENS DURING THIS UPDATE IS DETERMINED BY THE SPRITES CURRENT
            //ATRRIBUTES, I.E VELOCITY, POSITION, ETC
            for (Sprite s : (LinkedList<Sprite>) curModeStack()) {
                s.update(game);
            }
            //IF THE STACK IS NOT EMPTY
            if (!curModeStack().isEmpty()) {
                //GET THE BOTTOM MOST STACK ELEMENT
                Sprite bottomOfStack = (Sprite) curModeStack().get(0);

                //THE BOTTOM MOST ELEMENTS Y POSITION IS DIFFERENT DEPENDING
                //ON THE GAME MODE, SO DETERMINE THE CURRENT MODE
                if (this.getCurMode() == RambleOnModeType.CAPITAL_MODE
                        || this.getCurMode() == RambleOnModeType.NAME_MODE) {
                    //SEE IF THE BOTTOM MOST ELEMENT HAS REACHED THE BOTTOM OF THE STACK
                    if (bottomOfStack.getY() == FIRST_REGION_Y_IN_STACK) {
                        //IF IT HAS WE STOP ALL STACK ELEMENTS MOVEMENT
                        for (Sprite s : (LinkedList<Sprite>) curModeStack()) {
                            //SET ALL STACK SPRITES VELOCITY TO ZERO = STOP
                            s.setVy(0);
                        }
                    }
                    //ELSE THE CURRENT GAME MODE MUST BE LEADER, OR FLAG
                } else {
                    //SEE IF THE BOTTOM MOST STACK ELEMENT HAS REACHED THE BOTTOM
                    //OF THE STACK
                    if (bottomOfStack.getY() == GAME_HEIGHT - 275) {
                        //AGAIN IF IT HAS WE STOP ALL STACK ELEMENTS MOVEMENT
                        for (Sprite s : (LinkedList<Sprite>) curModeStack()) {
                            s.setVy(0);
                        }
                    }
                }
            }
        }
    }

    /**
     * Resets all the game data and initializes the background to the world map
     * which is where game play starts.
     *
     * @param game The game that we are reseting the data for which uses the
     * mini game frame work.
     */
    @Override
    public void reset(MiniGame game) {
        try {
            //LOAD THE WORLD MAP AND DATA INTO THE MANAGER
            regionImporter.loadRegion(theWorldFile, dataManager);

            //IF THIS HAPPENES WE CANNOT GO FURTHER SO THIS SHOULD NEVER HAPPEN
        } catch (InvalidXMLFileFormatException | IOException ex) {
            //FATAL ERROR
            Logger.getLogger(RambleOnDataModel.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }

        //THE PROTOCALL FOR EACH TIME A NEW CURRENT REGION IS LOADED INCLUDING AT STARTUP OR RESET
        newCurrentRegionProtocall();
        //SET THE GAME MODE TO REGION SELECTION, GAME NOT STARTED, NO MODE SELECTED
        setCurMode(RambleOnModeType.REGION_SELECTION);
        //START THE GAME
        beginGame();

        game.getGUIButtons().get(NAME_TYPE).setState(ENABLED_STATE);
    }

    /**
     * Initializes the linked list data structure that will hold all the textual
     * sprites used to comprise the stack for name mode.
     */
    public void initNameModeStack() {
        //START WITH CLEAN STACKS
        clearStacks();

        //THE Y POS WHERE WE WILL START PLACING THE SPRITES ON THE STACK
        int yPos = GAME_HEIGHT - 50;
        //AND THE X POS FOR THAT PLACEMENT
        int xPos = GAME_WIDTH - 300;
        //THE INCREMENT I.E EACH SPRITE WILL HAVE A HEIGHT OF 50
        int spriteHeight = 50;

        //GET AN ITERATOR TO THIS SHUFFLED LIST 
        Iterator<String> shuffledNames = getShuffledSubRegionsIterator();
        //FILL THE DATA STRUCTURE FOR THE NAME MODE STACK WITH A TEXTUAL SPRITE
        //HAVING A TEXT STRING VALUE EQUAL TO THE SUB REGION NAME THAT SRITE REPRESENTS
        while (shuffledNames.hasNext()) {
            //THE NEXT SUB REGIONS NAME
            String regionName = shuffledNames.next();

            //CREATE A SPRITE TYPE WITH A VALUE EQUAL TO THE REGIONS NAME
            SpriteType sType = new SpriteType(regionName);
            //ADD THE SPRITE TO NAME MODE STACK
            nameModeStack.add(new TextualSprite(regionName, sType, xPos, yPos, 0, 0, ENABLED_STATE));

            //INCREMENT THE Y POS FOR THE NEXT SPRITE SINCE WE WILL ROTATE NAME MODE
            //SPRITES IN VERTICALLY
            yPos -= spriteHeight;
        }
    }

    /**
     * Initializes the linked list data structure that will hold all the sprites
     * used to comprise the stack for leader mode.
     */
    public void initLeaderModeStack() {
        //START WITH CLEAN STACKS
        clearStacks();

        //THE Y POS WHERE WE WILL START PLACING THE SPRITES ON THE STACK
        int yPos = GAME_HEIGHT - 275;
        //AND THE X POS FOR THAT PLACEMENT
        int xPos = GAME_WIDTH - 200;
        //THE INCREMENT 
        int distanceBetween = 275;

        //GET AN ITERATOR TO THIS SHUFFLED LIST 
        Iterator<String> shuffledNames = getShuffledSubRegionsIterator();
        //FILL THE DATA STRUCTURE FOR THE NAME MODE STACK WITH A TEXTUAL SPRITE
        //HAVING A TEXT STRING VALUE EQUAL TO THE SUB REGION NAME THAT SRITE REPRESENTS
        while (shuffledNames.hasNext()) {
            //THE NAME OF THE NEXT SUB REGION
            String regionName = shuffledNames.next();

            //SPRITE TYPE FOR THIS REGION
            SpriteType sType = new SpriteType(regionName);
            //ADD THE LEADER IMAGE FOR THIS SUB REGION TO THE SPRITE TYPE STATE IMAGE
            sType.addState(ENABLED_STATE, dataManager.getSubRegionLeaders().get(regionName));
            //ADD THE SPRITE TO THE LEADER MODE STACK
            leaderModeStack.add(new Sprite(sType, xPos, yPos, 0, 0, ENABLED_STATE));

            yPos -= distanceBetween;
        }
    }

    /**
     * Initializes the linked list data structure that will hold all the textual
     * sprites used to comprise the stack for capital mode.
     */
    public void initCapitalModeStack() {
        //START WITH CLEAN STACKS
        clearStacks();

        //THE Y POS WHERE WE WILL START PLACING THE SPRITES ON THE STACK
        int yPos = GAME_HEIGHT - 50;
        //AND THE X POS FOR THAT PLACEMENT
        int xPos = GAME_WIDTH - 300;
        //THE INCREMENT I.E EACH SPRITE WILL HAVE A HEIGHT OF 50
        int spriteHeight = 50;

        //GET AN ITERATOR TO THIS SHUFFLED LIST 
        Iterator<String> shuffledNames = getShuffledSubRegionsIterator();
        //FILL THE DATA STRUCTURE FOR THE NAME MODE STACK WITH A TEXTUAL SPRITE
        //HAVING A TEXT STRING VALUE EQUAL TO THE SUB REGION NAME THAT SRITE REPRESENTS
        while (shuffledNames.hasNext()) {
            //THE NEXT SUB REGIONS NAME
            String regionName = shuffledNames.next();
            String regionCapital = dataManager.getSubRegionCapitals().get(regionName);

            //CREATE A SPRITE TYPE WITH A VALUE EQUAL TO THE REGIONS NAME
            SpriteType sType = new SpriteType(regionName);
            //ADD THE SPRITE TO CAPITAL MODE STACK
            capitalModeStack.add(new TextualSprite(regionCapital, sType, xPos, yPos, 0, 0, ENABLED_STATE));
            //INCREMENT THE Y POS FOR THE NEXT SPRITE SINCE WE WILL ROTATE CAPITAL MODE
            //SPRITES IN VERTICALLY
            yPos -= spriteHeight;
        }
    }

    public void initFlagModeStack() {
        //START WITH CLEAN STACKS
        clearStacks();

        //THE Y POS WHERE WE WILL START PLACING THE SPRITES ON THE STACK
        int yPos = GAME_HEIGHT - 275;
        //AND THE X POS FOR THAT PLACEMENT
        int xPos = GAME_WIDTH - 250;
        //THE INCREMENT I.E EACH SPRITE WILL HAVE A HEIGHT OF 50
        int distanceBetween = 275;

        //GET AN ITERATOR TO THIS SHUFFLED LIST 
        Iterator<String> shuffledNames = getShuffledSubRegionsIterator();
        //FILL THE DATA STRUCTURE FOR THE NAME MODE STACK WITH A TEXTUAL SPRITE
        //HAVING A TEXT STRING VALUE EQUAL TO THE SUB REGION NAME THAT SRITE REPRESENTS
        while (shuffledNames.hasNext()) {
            //THE NAME OF THE NEXT SUB REGION
            String regionName = shuffledNames.next();

            //SPRITE TYPE FOR THIS REGION
            SpriteType sType = new SpriteType(regionName);
            //ADD THE FLAG IMAGE FOR THIS SUB REGION TO THE SPRITE TYPE STATE IMAGE
            sType.addState(ENABLED_STATE, dataManager.getSubRegionFlags().get(regionName));
            //ADD THE SPRITE TO THE FLAG MODE STACK
            flagModeStack.add(new Sprite(sType, xPos, yPos, 0, 0, ENABLED_STATE));

            yPos -= distanceBetween;
        }
    }

    /*
     * Gets and returns an iterator to a shuffled list of the sub regions
     * of the current region.
     */
    private Iterator<String> getShuffledSubRegionsIterator() {
        //GET AN ITERATOR TO THE LIST OF THE CURRENT REGIONS SUB REGIONS
        Iterator<String> subRegionNames = dataManager.getAllRegionsNamesIterator();
        //TEMPORTARY DATA STRUCTURE
        ArrayList<String> tempStruct = new ArrayList<>();

        //FILL THE TEMPORARY DATA STRUCTURE WITH ALL THE NAMES OF THE SUB REGIONS
        //FOR THE CURRENT REGION
        while (subRegionNames.hasNext()) {
            //ADD THE NEXT SUB REGION TO THE TEMPORARY DATA STRCUTRE
            tempStruct.add(subRegionNames.next());
        }

        //SHUFFLE THE LIST OF REGION NAMES
        Collections.shuffle(tempStruct);

        //RETURN AN ITERATOR TO THE SHUFFLED LIST 
        return tempStruct.iterator();
    }

    /*
     * Clears the game stacks so we can be sure there are no lingering sprites
     * on any of the stacks after a mode switch i.e start of a new game.
     */
    private void clearStacks() {
        //CLEAR THE NAME MODE STACK
        nameModeStack.clear();
        //AND THE CAPITAL MODE STACK
        capitalModeStack.clear();
        //LEADER MODE STACK
        leaderModeStack.clear();
        //AND FLAG MODE STACK
        flagModeStack.clear();
    }

    /*
     * Initialize the list of pixel values associated with every sub region for
     * the corresponding current region map.
     *
     * @param img The map image to whose pixels we are looking at.
     */
    private void initSubRegionPixels(BufferedImage img) {
        //RGB INDIVIDUAL COLOR VALUES FOR CURRENT PIXEL 
        int r, g, b;
        //SCAN THE ENTIRE IMAGE AND ANALYZE PIXEL DATA
        for (int i = 0; i < IMAGE_WIDTH; i++) {
            for (int j = 0; j < IMAGE_HEIGHT; j++) {

                //GET THE PIXEL AT CORDINATE (i, j)'S COLOR
                int pixel_int_color = img.getRGB(i, j);
                Color pixel_color = new Color(pixel_int_color);

                //SINCE WE HAVE TO PERFORM THIS OPERATION WITH EVERY NEW REGION
                //AND THIS SUB ROUTINE IS LIKELY THE MOST EXPENSIVE ROUTINE WE
                //PERFORM AS PART OF OUR NEW CURRENT REGION PROTOCALL ANYTHING WE
                //CAN DO TO PRUNE THE SEARCH SPACE, AND SPEED UP THE EXECUTION WILL
                //HELP OUR APPLICATIONS PERFORMANCE
                r = pixel_color.getRed();
                g = pixel_color.getGreen();
                b = pixel_color.getBlue();

                //SO LETS BOMB OUT IF R!=G!=B SINCE THIS MUST BE TRUE FOR ANY GREY
                //SCALE VALUE, THIS WAY WE AVOID THE ITERATION BELOW. 
                if ((r == g) && (r == b) && (g == b)) {

                    //TO ITERATE THROUGH THE LIST OF SUB REGIONS
                    Iterator<String> subRegionsIterator = dataManager.getAllRegions().keySet().iterator();

                    //TEST PIXEL COLOR AGAISNT EACH SUB REGIONS GREY SCALE VALUE
                    while (subRegionsIterator.hasNext()) {
                        Region cur_region = dataManager.getAllRegions().get(subRegionsIterator.next());

                        //IF THE PIXEL COLOR MATCHES THE SUB REGIONS GREY SCALE COLOR
                        if (pixel_color.equals(cur_region.getGreyScale())) {
                            //ADD THE PIXEL CORDINATES TO THIS SUBREGIONS LIST OF PIXELS
                            ArrayList<Point2D.Double> subRegionPixels = pixels.get(cur_region.getName());
                            subRegionPixels.add(new Point2D.Double(i, j));
                            //IF WE FIND A MATCHING GREY SCALE VALUE MOVE ON TO NEXT PIXEL
                            break;
                        }
                    }
                }
            }
        }
    }

    /*
     * Initialize the sub region to color mappings for the current regions sub
     * regions. This should be done every time a new region is loaded.
     */
    private void initSubRegionMappings() {
        //GET AN ITERATOR TO ALL THE SUB REGIONS OF THE CURRENT REGION
        Iterator<String> regionIterator = dataManager.getAllRegionsNamesIterator();
        //AND TRAVERSE THROUGH ALL THOSE SUB REGIONS
        while (regionIterator.hasNext()) {
            //GET THE CURRENT SUB REGIONS NAME
            String subRegionName = regionIterator.next();
            //AND THE ACTUAL REGION OBJECT BY THAT NAME SO WE CAN ACCESS ALL
            //OF THAT REGIONS ATTRIBUTES
            Region subRegion = dataManager.getAllRegions().get(subRegionName);

            //AND ADD A MAPPING OF THAT REGIONS NAME TO THAT REGIONS GREY SCALE COLOR
            subRegionToColorMappings.put(subRegionName, subRegion.getGreyScale());
            //AND VICE VERSA
            colorToSubRegionMappings.put(subRegion.getGreyScale(), subRegionName);
        }
    }

    /*
     * Proto-call used each time a new region is loaded. This method uses a facade
     * design pattern to encapsulates all the behavior we need to execute each time a
     * new region is loaded.
     */
    private void newCurrentRegionProtocall() {
        //TO ITERATE THROUGH THE LIST OF SUB REGIONS 
        Iterator<String> subRegionsIterator = dataManager.getAllRegionsNamesIterator();

        //CREATE THE PIXEL LISTS FOR THE CURRENT REGION
        while (subRegionsIterator.hasNext()) {
            //GET THE REGION OBJECT FOR THE NEXT SUB REGION IN THE LIST
            Region cur_region = dataManager.getAllRegions().get(subRegionsIterator.next());
            //AND CREATE A PIXEL LIST FOR THAT REGION
            pixels.put(cur_region.getName(), new ArrayList());
        }

        //INITIALIZE THE GIVEN PIXEL LISTS WITH THE PIXELS FROM THE CURRENT REGION MAP
        initSubRegionPixels(dataManager.getCurrentMap());
        //INITIALIZE THE COLOR TO REGION NAME AND VICE VERSA MAPPINGS
        initSubRegionMappings();

        //INITIALIZE THE SUB REGION CAPITALS MAP IF AVAILABLE
        dataManager.initCapitalsMap();

        //CHANGE ALL THE REGIONS WITH OUT AT LEAST ONE PLAYABLE REGION TO WHITE ON THE CURRENT MAP
        setNonPlayableRegions();
    }

    /*
     * Gets the region if possible that the mouse press occured in relative
     * to the current region map.
     *
     * @param mouseX The x cordinate of the mouse press on the sprite.
     * 
     * @param mouseY The y cordinate of the mouse press on the sprite.
     * 
     * @return The region that the mouse press occured in if at all, else null.
     */
    private Region getSelectedRegion(int mouseX, int mouseY) {
        //THE MAP OF THE CURRENT REGION
        BufferedImage img = dataManager.getCurrentMap();
        //CHECK THAT THE MOUSE CLICK WAS IN BOUNDS
        if (mouseX > 0 && mouseX < IMAGE_WIDTH && mouseY > 0 && mouseY < IMAGE_HEIGHT) {
            //GET THE RGB VALUE AS AN INTEGER OF THE PIXEL WHERE THE MOUSE PRESS OCCURED
            int rgb = img.getRGB(mouseX, mouseY);
            //CONVERT THE INTEGER RGB VALUE TO AN ACTUAL COLOR OBJECT
            Color pixelColor = new Color(rgb);

            //SEE IF THERE EXISTS A MAPPING FOR THAT COLOR TO AN ACTUAL REGION
            String clickedSubRegion = colorToSubRegionMappings.get(pixelColor);

            //IF THERE DID NOT EXIST A MAPPING THEN THE MOUSE PRESS CAN BE IGNORED
            if (clickedSubRegion == null) {
                //NO MAPPING FOUND
                return null;

            }//IF THE SUB REGION STACK IS EMPTY AND THE CLICK WAS ON A REGION THEN
            //WE ARE STILL IN REGION SELECTION AND THE ACTUAL GAME HAS NOT YET STARTED
            else if (clickedSubRegion != null) {
                //THE REGION SELECTED IS FOR REGION SELECTION PURPOSES NOT GAMEPLAY
                return dataManager.getAllRegions().get(clickedSubRegion);

            }
            return null;
        }
        return null;
    }

    /*
     * Realizes a full path to the selectedRegions XML data file.
     * 
     * @return A string containing the full path to the selected regions DIR.
     */
    private String realizePathName(String selectedRegionName) {
        //BY KEEPING THE CURRENT REGION DIR ATTRIBUTE AT ALL TIMES WE CAN ALWAYS REACH
        //THE NEXT SELECTED REGION WITH ONE DIR FILE STRUCTURE TREE TRAVERSAL EIGHTER UP OR DOWN
        return currentRegionFile.getParent() + "/" + selectedRegionName + "/" + selectedRegionName + XML_DATA_FILE;
    }

    /**
     * Handles the procedure for a left click on the canvas during region
     * selection, i.e prior to the start of actual game play.
     *
     * @param mouseX The x cordinate of the mouse click .
     * @param mouseY The y cordinate of the mouse click.
     */
    public void handleLeftClick(int mouseX, int mouseY) {
        //GET THE REGION THAT THE MOUSE CLICK OCCURED IN, IF AT ALL
        Region regionSelected = getSelectedRegion(mouseX, mouseY);

        //IF THE MOUSE CLICK WAS INSIDE A REGION
        if (regionSelected != null) {
            //THE PATH TO THE DIR OF THE SELECTED REGION
            String selectedRegionData = realizePathName(regionSelected.getName());

            try {
                //ATTEMPT TO LOAD THE MAP DATA AND IMAGE 
                //
                //AN EXCEPTION SHOULD NEVER BE THROWN
                //HERE BUT WE MUST ANTICIPATE ONE ALL THE SAME

                //SET THE PARENT OF CURRENT REGION FILE TO THE REGION THAT
                //IS THE CURRENT REGION
                parentOfCurrentFile = currentRegionFile;

                //ADD THE CURRENT FILE TO THE PARENT FILES STACK
                parentFiles.add(parentOfCurrentFile);

                //SET THE CURRENT REGION FILE TO THE REGION ABOUT TO BECOME
                //THE NEW CURRENT REGION
                currentRegionFile = new File(selectedRegionData);

                //LOAD THE DATA, AND MAP FOR THE NEW CURRENT REGION
                regionImporter.loadRegion(currentRegionFile, dataManager);
                //ATTEMPT TO LOAD THE FLAG IMAGES AS WELL
                loadFlags(currentRegionFile.getParent());
                //AND THE LEADER IMAGES
                loadLeaders(currentRegionFile.getParent());
            } catch (InvalidXMLFileFormatException | IOException ex) {
                //AN EXCEPTION HERE IS FATAL
                JOptionPane.showMessageDialog(null, "Sorry, the region you selected is currently unavailable", "Load Region Error", JOptionPane.ERROR_MESSAGE);

                //REVERSE THE PROCESS CARRIED OUT BEFORE THE CRASH
                currentRegionFile = parentFiles.pop();
                parentOfCurrentFile = parentFiles.peek();
                //AND EXIT AHEAD OF THE NEW CURRENT REGION PROTOCALL THAT IS NO LONGER NECESSARY
                return;
            }
            //GO THROUGH THE NEW CURRENT REGION PROTOCALL
            newCurrentRegionProtocall();
        }
    }

    /*
     * Attempt to load the sub region flag images for flag mode for the current
     * region. Whether this load is successful or not will be reflected in the 
     * state of the flag mode button, red implies not available, green is available.
     * 
     * @param currRegionDIR The DIR of the current region.
     */
    private void loadFlags(String currRegionDIR) {
        try {
            //ATTEMPT TO LOAD THE FLAG IMAGES USING THE REGION DATA LIBRARY
            regionImporter.loadRegionFlagImages(currRegionDIR, dataManager);
        } catch (IOException ex) {
            //IF THAT ATTEMPT FAILS CLEAR ANY IMAGES THAT MAY HAVE BEEN LOADED
            //INTO THE MANAGER BEFORE IT FAILED. ALL GAME MODES MUST HAVE ALL
            //THE NECESSARY DATA FOR THEM TO BE PLAYABLE.
            dataManager.getSubRegionFlags().clear();
        }
    }

    /*
     * Attempt to load the sub region leader images for leader mode for the current
     * region. Whether this load is successful or not will be reflected in the 
     * state of the leader mode button, red implies not available, green is available.
     * 
     * @param currRegionDIR The DIR of the current region.
     */
    private void loadLeaders(String currRegionDIR) {
        try {
            //ATTEMPT TO LOAD THE LEADER IMAGES USING THE REGION DATA LIBRARY
            regionImporter.loadRegionLeaderImages(currRegionDIR, dataManager);
        } catch (IOException ex) {
            //IF THAT ATTEMPT FAILS CLEAR ANY IMAGES THAT MAY HAVE BEEN LOADED
            //INTO THE MANAGER BEFORE IT FAILED. ALL GAME MODES MUST HAVE ALL
            //THE NECESSARY DATA FOR THEM TO BE PLAYABLE.
            dataManager.getSubRegionLeaders().clear();
        }
    }

    /**
     * Handles the procedure for a right click on the canvas during region
     * selection, i.e prior to the start of actual game play.
     *
     * @param mouseX The x cordinate of the mouse click.
     * @param mouseY The y cordinate of the mouse click.
     */
    public void handleRightClick(int mouseX, int mouseY) {
        //WE ONLY REQUIRE THIS BEHAVIOR IN REGION SELECTION
        if (getCurMode() == RambleOnModeType.REGION_SELECTION) {
            //ATTEMPT TO LOAD THE MAP DATA AND IMAGE
            try {
                //AN EXCEPTION SHOULD NEVER BE THROWN
                //HERE BUT WE MUST ANTICIPATE ONE ALL THE SAME

                //SET THE CURRENT REGION FILE TO THE REGION ABOUT TO BECOME 
                //THE CURRENT REGION

                //NOTE - WE ONLY ARE HEAR WHEN THE CURRENT REGION IS NOT THE ROOT
                //OF THE REGION HEIRARCHY I.E THE WORLD. IN WHICH CASE THERE WILL
                //STILL BE PARENT REGIONS ON THE STACK AND SO WE DONT NEED TO CHECK
                //FOR AN EMPTY STACK
                currentRegionFile = parentFiles.pop();

                //LOAD THE MANAGER WITH THE SELECTED REGION DATA AND LOAD 
                //THE SELECTED REGION MAP 
                regionImporter.loadRegion(currentRegionFile, dataManager);
                //ATTEMPT TO LOAD THE FLAG IMAGES AS WELL
                loadFlags(currentRegionFile.getParent());
                //AND THE LEADER IMAGES
                loadLeaders(currentRegionFile.getParent());
            } catch (InvalidXMLFileFormatException | IOException ex) {
                //AN EXCEPTION HERE IS FATAL 
                Logger.getLogger(RambleOnDataModel.class.getName()).log(Level.SEVERE, null, ex);
            }
            //GO THROUGH THE NEW CURRENT REGION PROTOCALL
            newCurrentRegionProtocall();
        }
    }

    /**
     * The custom game response to a mouse click on a sprite. This method
     * overrides the method by the same name in the base class data model. We
     * implement the correct response to a mouse click on a sprite that is not a
     * button in this method. The mouse clicks that this method responds to are
     * the ones that occur on and inside a region map image.
     *
     * @param game The game which we are providing response for.
     *
     * @param mouseX The x cordinate where the mouse click occurred.
     * @param mouseY The y cordinate where the mouse click occurred.
     */
    @Override
    public void checkMousePressOnSprites(MiniGame game, int mouseX, int mouseY, MouseEvent me) {
        //STILL IN COUNTRY SELECTION, GAME PLAY HAS NOT YET STARTED
        if (getCurMode() == RambleOnModeType.REGION_SELECTION) {

            //IF WE ARE AT THE TOP MOST REGION IN THE REGION HEIRARCHY 
            if (dataManager.getCurrentRegion().getName().equals(THE_WORLD)) {
                //WE ONLY RESPOND TO LEFT CLICKS AND WE TRAVERSE DOWNWARD IN THE REGION HEIRARCHY
                if (SwingUtilities.isLeftMouseButton(me)) {
                    //HANDLE A LEFT CLICK ON THE MAP PRE-GAME PLAY, AT THE ROOT
                    handleLeftClick(mouseX, mouseY);
                }
            } else //WE ARE NOT AT THE TOP MOST REGION IN THE REGION HEIRARCHY SO WE CAN GO UP AND DOWN
            //CHECK FOR RIGHT CLICKS, I.E TO GO BACK TO THE PREVIOUS REGION
            if (SwingUtilities.isRightMouseButton(me)) {
                //HANDLE A RIGHT CLICK ON THE MAP PRE-CAME PLAY, NOT AT THE ROOT
                handleRightClick(mouseX, mouseY);
            }//MUST BE A LEFT CLICK SO WE ARE GOING DEEPER INTO THE REGION HEIRARCHY 
            else {
                //HANDLE A LEFT CLICK ON THE MAP PRE-GAME PLAY, NOT AT THE ROOT
                handleLeftClick(mouseX, mouseY);
            }
        } else //WE ARE IN THE MIDDLE OF GAME PLAY, I.E THE REGION HAS ALREADY BEEN
        //SELECTED AND NOW WE ARE ACTUALLY PLAYING THE GAME
        {
            //CHECK THAT THE MOUSE CLICK WAS IN BOUNDS
            if (mouseX > 0 && mouseX < IMAGE_WIDTH && mouseY > 0 && mouseY < IMAGE_HEIGHT) {
                //RESPOND TO A MOUSE CLICK ON THE REGION MAP
                respondToMapSelection((RambleOn) game, mouseX, mouseY);
            }
        }
    }

    /*
     * Equivalent to getCurMode which returns the current
     * game mode except that instead of returning a RambleOnModeType
     * object this method returns the current game stack associated 
     * with the current mode.
     * 
     * @return The current modes game stack list.
     */
    private LinkedList curModeStack() {
        //IF THE CURRENT MODE IS CAPITAL, THEN RETURN THE CAPITALS STACK
        if (getCurMode() == RambleOnModeType.CAPITAL_MODE) {
            return getCapitalModeStack();
        } //FLAG MODE STACK
        else if (getCurMode() == RambleOnModeType.FLAG_MODE) {
            return getFlagModeStack();
        }//LEADER MODE STACK
        else if (getCurMode() == RambleOnModeType.LEADER_MODE) {
            return getLeaderModeStack();
        } //NAME MODE STACK
        else if (getCurMode() == RambleOnModeType.NAME_MODE) {
            return getNameModeStack();
        }
        return null;
    }

    /*
     * This is where we respond to a user mouse click on the map
     * during game play.
     * 
     * @param game The game we are making changes to.
     * @param mouseX The x cordinate of the mouse click on the map.
     * @param mouseY The y cordinate of the mouse click on the map.
     */
    private void respondToMapSelection(RambleOn game, int mouseX, int mouseY) {
        //GET THE MAP FOR THE CURRENT REGION
        BufferedImage img = dataManager.getCurrentMap();
        //EXTRACT THE COLOR OF THE PIXEL AT THE x,y CORDINATES OF THE MOUSE CLICK
        int rgb = img.getRGB(mouseX, mouseY);
        Color pixelColor = new Color(rgb);
        //SEE IF THE COLOR MAPS TO ANY OF THE CURRENT REGIONS SUB REGIONS
        String clickedSubRegion = colorToSubRegionMappings.get(pixelColor);
        //IF IT DOESNT WE DO NOTHING
        if ((clickedSubRegion == null) || (curModeStack().isEmpty())) {
            return;
        }
        //ELSE WE NEED TO SEE IF THE REGION SELECTED CORRESPONDS TO THE ELEMENT
        //ON THE BOTTOM OF THE STACK
        String spriteID;
        //GET THE SPRITE ID FOR THE BOTTOMS MOST STACK ELEMENT DEPENDING ON GAME MODE
        if (getCurMode() == RambleOnModeType.FLAG_MODE || getCurMode() == RambleOnModeType.LEADER_MODE) {
            spriteID = ((Sprite) curModeStack().get(0)).getSpriteType().getSpriteTypeID();
        } else {
            spriteID = ((TextualSprite) curModeStack().get(0)).getSpriteType().getSpriteTypeID();
        }
        //SEE IF THE SELECTED REGION CORRESPONDS TO THE BOTTOM MOST STACK ELEMENT
        if (clickedSubRegion.equals(spriteID)) {
            //PLAY SUCCESS AUDIO FILE SNIPPET
            game.getAudio().play(SUCCESS, false);

            //COLOR A CORRECTLY SELECTED REGIONS PIXELS GREEN
            changeSubRegionColorOnMap(clickedSubRegion, Color.GREEN);

            //AND LET'S CHANGE THE RED ONES BACK TO THEIR PROPER COLORS
            for (String s : redSubRegions) {
                Color subRegionColor = subRegionToColorMappings.get(s);
                changeSubRegionColorOnMap(s, subRegionColor);
            }
            //AND CLEAR THE LIST OF RED SUB REGIONS
            redSubRegions.clear();

            //AND SINCE WE CORRECTLY PICKED A REGION WE MOVE IT OFF THE STACK
            //BY SHIFTING THE STACK DOWN
            startStackSpritesMovingDown();

            //REMOVE THE BOTTOM ELEMENT FROM THE STACK
            curModeStack().removeFirst();

            //IF THAT WAS THE LAST ELEMENT ON THE STACK THE GAME IS OVER
            if (curModeStack().isEmpty()) {
                //FINAL TIME
                winEndingTime = new GregorianCalendar();
                //SET THE WIN STATE
                endGameAsWin();

                //PLAY THE WINNING AUDIO WHICH IS THE NATIONAL ANTHEM
                //OF THE CURRENT REGION, IF ITS AVAILABLE
                try {
                    //STOP THE TRACKED SONG
                    game.getAudio().stop(TRACKED_SONG);
                    //ATTEMPT TO LOAD THE NATION ANTHEM FOR THE CURRENT REGION
                    game.getAudio().loadAudio(dataManager.getCurrentRegion().getName(), getNationalAnthemFile());
                    //AND THEN PLAY IT AS THE VICTORY SONG
                    game.getAudio().play(dataManager.getCurrentRegion().getName(), false);
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InvalidMidiDataException | MidiUnavailableException ex) {
                    //IF WE COUDLNT FIND A NATIONAL ANTHEM FOR THE CURENT REGION JUST PLAY
                    //THE DEFAULT TRACKED SONG
                    game.getAudio().play(TRACKED_SONG, false);
                }
                //SAVE THE GAME PLAY INFORMATION TO THE CURRENT USERS ACCOUNT
                updateAccountInformation();
            }
        }//REGION SELECTED DID NOT CORRESPOND TO THE BOTTOM MOST STACK ELEMENT
        else {
            if (!redSubRegions.contains(clickedSubRegion)) {
                //WRONG ANSWER AUDIO
                game.getAudio().play(FAILURE, false);

                //INCREMENT WRONG GUESSES
                incorrectGuesses++;

                //TURN THE TERRITORY TEMPORARILY RED
                changeSubRegionColorOnMap(clickedSubRegion, Color.RED);
                //AND IT TO THE LIST OF CURRENTLY RED SUB REGIONS
                redSubRegions.add(clickedSubRegion);
            }
        }
    }

    /*
     * Updates the current users account statistics after the completition
     * of a game.
     */
    private void updateAccountInformation() {
        //GET THE CURRENT ACCOUNT 
        Account curAccount = accountsManager.getCurrentAccount();
        //GET THE CURRENT REGION NAME
        Region curRegion = dataManager.getCurrentRegion();

        String parent;
        //GET THE SUBSTRING OF THE PARENT FILE PATH NAME 
        //EQUAL TO JUST THE PARENT REGION NAME WITH NO PATH INFORMATION INCLUDED
        if (!curRegion.getName().equals(THE_WORLD)) {
            parent = parentFiles.peek().getName();
            parent = parent.substring(0, parent.indexOf("."));
            parent = parent.substring(0, parent.length() - 4);
        } else {
            curRegion.setName("World");
            parent = THE_WORLD;
        }

        //NOW WE CAN SET THE PARENT FOR THE CURREN REGION
        curRegion.setParent(parent);

        //ATTEMPT TO GET THE CURRENT STATS FOR THE CURRENT REGION IF THE
        //CURRENT USER HAS PLAYED THIS REGION BEFORE
        AccountStatistics accStats = curAccount.getRegionStats().get(curRegion.getName());

        //IF THERE IS NO RECORD FOR THIS REGION IN THE USERS STATS
        if (accStats == null) {
            //THEN CREATE A NEW STATISTICAL RECORD FOR THIS REGION
            accStats = new AccountStatistics();
            //AND IT TO THE CURRENT USERS REGION STATS
            curAccount.getRegionStats().put(curRegion.getName(), accStats);
            //AND ADD THIS REGION TO THE LIST OF REGIONS PLAYED BY THIS USER         
            curAccount.getRegionsPlayed().add(curRegion);
        }

        //CALCULATE THE GAME DURATION
        long numMilliseconds = winEndingTime.getTimeInMillis() - startTime.getTimeInMillis();
        long numSeconds = numMilliseconds / 1000L;
        int gameDur = (int) numSeconds;

        //CALCULATE THE GAME SCORE
        int score = calculateScore();

        //ADD THE RESULTS OF THE CURRENT GAME TO THIS USERS STATS 
        accStats.addResults(getCurMode().name(), gameDur, score);

    }

    /*
     * Assembles and returns a string value representing the path to the current
     * regions national anthem .mid file, or at least where the file should be.
     * 
     * @return Returns the path to the national anthem .mid file for the current
     * region, or at least where the file should be.
     */
    private String getNationalAnthemFile() {
        //THE NAME OF THE CURRENT REGION
        String currentRegionName = dataManager.getCurrentRegion().getName();
        //RETURN THE PATH TO THE NATIONAL ANTHEM .mid FILE , IF IT EXISTS
        return currentRegionFile.getParent() + "/" + currentRegionName + NATIONAL_ANTHEM;
    }

    /*
     * Set every element on the stack in a downward motion.
     */
    private void startStackSpritesMovingDown() {
        //GET AN ITERATOR TO THE CURRENT GAME MODE STACK
        Iterator stackIterator = curModeStack().iterator();

        //AND START EACH ELEMENT MOVING DOWN 
        while (stackIterator.hasNext()) {
            ((Sprite) stackIterator.next()).setVy(SUB_STACK_VELOCITY);
        }
    }

    /*
     * Sets the pixels of the selected region to eighter red if the 
     * region was incorrectly guessed , or green if the region was
     * the correct choice.
     * 
     * @param game The game we are making changes for.
     * @param subRegion The selected sub region on the map.
     * @param color The color we are changing the pixels of that region to.
     */
    private void changeSubRegionColorOnMap(String subRegion, Color color) {
        //GET THE CURRENT MAP
        BufferedImage img = dataManager.getCurrentMap();
        //AND THE SELECTED REGIONS PIXEL LIST
        ArrayList<Point2D.Double> subRegionPixels = pixels.get(subRegion);
        //GO THROUGH EVERY PIXEL IN THE LIST AND SET IT TO THE SPECIFIED COLOR
        for (Point2D.Double p : subRegionPixels) {
            int rgb = color.getRGB();
            img.setRGB((int) (p.x), (int) (p.y), rgb);
        }
    }

    /*
     * Check for sub regions of the current region that do not have at least
     * one playable region.
     */
    private void setNonPlayableRegions() {
        //GET AN ITERATOR TO THE LIST OF ALL THE SUB REGIONS OF THE CURRENT REGION
        Iterator<String> regionNameIt = dataManager.getAllRegionsNamesIterator();
        //NOW ITERATE THROUGH ALL THOSE SUB REGIONS
        while (regionNameIt.hasNext()) {
            //GET THE CURRENT SUB REGIONS NAME
            String regionName = regionNameIt.next();
            //AND REALIZE THE PATH TO THE XML DATA FILE FOR THAT SUB REGION
            String path = realizePathName(regionName);
            //GET THE XML DATA FILE 
            File xmlData = new File(path);
            //AND THE DIR WHERE IT SHOULD BE CONTAINED
            File regDir = new File(xmlData.getParent());
            //AND CHECK IF IT EXISTS                  
            if ((regDir.exists() && (!(xmlData.exists()))) || (!(regDir.exists()))
                    || (!(regionImporter.getXmlUtil().validateXMLDoc(path, xmlSchema.getAbsolutePath())))) {
                //IF IT DOES NOT THEN CHANGE THAT SUB REGIONS COLOR TO WHITE
                changeSubRegionColorOnMap(regionName, Color.WHITE);
                //AND SET THE PLAYABLE FLAG FOR THAT REGION TO FALSE
                dataManager.getAllRegions().get(regionName).setPlayable(false);
            }
        }
    }

    /**
     * Sets all the sub regions of the current region that were set to white in
     * order to indicate they have no playable regions back to their default
     * grey scale value upon the starting of a game. A white region just implies
     * that no further traversal can be accomplished through that region.
     */
    public void setNonPlayableBackForGame() {
        //GET AN ITERATOR TO THE LIST OF ALL THE CURREN REGIONS SUB REGIONS
        Iterator<String> regionNameIt = dataManager.getAllRegionsNamesIterator();
        //NOW ITERATE THROUGH ALL THOSE SUB REGIONS
        while (regionNameIt.hasNext()) {
            //THE NEXT REGION NAME
            String regionName = regionNameIt.next();
            //GET THE REGION OBJECT BY THAT NAME
            Region curRegion = dataManager.getAllRegions().get(regionName);
            //AND IF THE REGION IS CURRENTLY SET TO NOT PLAYABLE            
            if (!(curRegion.isPlayable())) {
                //THEN RESET IT TO PLAYABLE FOR THE GAME WITH THE CURRENT REGION
                changeSubRegionColorOnMap(regionName, subRegionToColorMappings.get(regionName));
            }
        }
    }

    /**
     * Used with the cheat code control-c to skip to the next to last sub region
     * for a given region.
     *
     * @param game The game we are making changes to.
     */
    public void removeAllButOneFromeStack() {
        //KEEP MOVING THE REGIONS OFF THE STACK UNTIL THE LAST ONE
        while (curModeStack().size() > 1) {
            //REMOVE THE NEXT REGION
            Sprite s = (Sprite) curModeStack().removeFirst();
            String subRegionName = s.getSpriteType().getSpriteTypeID();

            //COLOR THE REGION GREEN ON THE MAP
            changeSubRegionColorOnMap(subRegionName, Color.GREEN);
        }
        //MOVE THE REGIONS DOWN THE STACK
        startStackSpritesMovingDown();
    }
}