package rambleon;

//IMPORT ALL THE LIBRARIES AND CLASSES WE WILL NEED
import accountdata.AbstractActiveAccountsTableModel;
import accountdata.AbstractModeStatsTableModel;
import accountdata.Account;
import accountdata.AccountDataManager;
import accountdata.AccountStatistics;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import static rambleon.RambleOnSettings.*;
import rambleonevents.AccountCreationScreenButtonsHandler;
import rambleonevents.AccountSelectionHandler;
import rambleonevents.CreateAccountButtonHandler;
import rambleonevents.CurrentAccountButtonsHandler;
import rambleonevents.ExitAccountManagerButtonHandler;
import rambleonevents.RegionsPlayedTreeHandler;
import rambleonevents.WelcomePageHandler;
import regiondata.Region;
import regiondata.RegionDataManager;
import regiondata.RegionType;

/**
 * The RambleOnScreenManager is used to manager any non-game play screens such
 * as any menu based screens. This screen manager will take us from the start up
 * screen through the current account screen where control will be passed to the
 * mini-game framework to run the actual game.
 *
 * @author James C. Porcelli, SBU ID 108900819
 */
public class RambleOnScreenManager {

    //COMPONENTS USED FOR THE GAME SCREENS
    //FRAMES
    private JFrame window; //FRAME FOR ALL MENU SCREENS
    //PANELS
    private JPanel //PANELS FOR THE WELCOME SCREEN
            welcomeScreen,
            //PANELS FOR THE ACCOUNTS SCREEN
            accountsScreen, accountsScreenTablePanel, accountsScreenButtonPanel,
            accountsScreenSubButtonPanel, accountsScreenCommentsPanel, accountsScreenTitlePanel,
            //PANELS FOR THE CURRENT ACCOUNT SCREEN
            regionsPlayedTreePanel, currentAccountStatsPanel, currentAccountButtonPanel, currentAccountScreen,
            currentAccountTitlePanel, currentAccountSubButtonPanel, currentAccountStatsPanelSubPanel,
            //PANELS FOR THE ACCOUNT CREATION SCREEN
            accountCreationScreen, accountCreationScreenTextFieldPanel, accountCreationScreenCommentsPanel,
            accountCreationScreenTitlePanel, accountCreationScreenButtonPanel, accountCreationScreenAppTitlePanel,
            accountCreationScreenAppTitleSubPanel;
    //BUTTONS
    private JButton //BUTTONS USED FOR THE WELCOME SCREEN
            welcomeScreenButton,
            //BUTTONS USED FOR THE ACCOUNTS SCREEN
            newUserAccountButton,
            //BUTTONS FOR THE ACCOUNT CREATION SCREEN
            accountCreationOkButton, accountCreationCancelButton,
            //BUTTONS FOR THE CURRENT ACCOUNT SCREEN
            backToAccountsButton, toGameModeButton,
            //BUTTONS USED IN ALL SCREENS
            exitButton;
    //SPLIT PANES
    private JSplitPane splitPane;
    //SCROLL PANES
    private JScrollPane //SCROLL PANES FOR THE ACCOUTNS SCREEN
            accountsTableScrollPane,
            //SCROLL PANES FOR THE CURRENT ACCOUNT SCREEN
            regionsPlayedTreeScrollPane;
    //LABELS
    private JLabel //LABELS FOR THE ACCOUNTS SCREEN
            accountsPageHeaderLabel, createNewUserLabel, accountMenuBanner,
            //LABELS FOR THE CURRENT ACCOUNT SCREEN
            subRegionModeLabel, capitalModeLabel, leaderModeLabel, flagModeLabel,
            fastestTimeLabel, timesPlayedLabel, highScoreLabel, backToAccountsLabel, accountsScreenName,
            playRambleOnLabel, currentAccountPageHeaderLabel, currentPlayerStatsLabel, currentAccountScreenTitle,
            //LABELS FOR THE CREATE ACCOUNT SCREEN
            createAccountHeaderLabel, newAccountUserNameLabel, appTitleLabel, accountCreationScreenBanner;
    //TREES
    private JTree //TREE FOR THE CURRENT ACCOUNT SCREEN
            regionsPlayedTree;
    //TREE MODELS
    private DefaultTreeModel //TREE MODEL FOR THE TREE ON THE CURRENT ACCOUNT SCREEN
            regionsPlayedTreeModel;
    //TABLES
    private JTable//TABLES FOR THE ACCOUNTS SCREEN
            accountsTable,
            //TABLE FOR THE CURRENT ACCOUNT SCREEN
            statsTable;
    //TEXT FIELDS
    private JTextField //TEXT FIELDS FOR THE ACCOUTN CREATION SCREEN
            newUserNameTextField;
    //ACCOUNTS DATA MANAGER
    AccountDataManager accountsManager;
    //REGIONS MANAGER
    RegionDataManager dataManager;
    //THE BARE REGIONS PLAYED TREE STRUCTURE
    String[] bareRegionsPlayedTree = {REGIONS_PLAYED_ROOT, ASIA_SUBTREE_ROOT, EUROPE_SUBTREE_ROOT, SOUTH_AMERICA_SUBTREE_ROOT,
        NORTH_AMERICA_SUBTREE_ROOT, AFRICA_SUBTREE_ROOT, ANTARTICA_SUBTREE_ROOT, AUSTRALIA_SUBTREE_ROOT};

    /**
     * Creates an instance of a RambleOn screen manager. All necessary data
     * needed to start the application is initialized in this method.
     */
    public RambleOnScreenManager(AccountDataManager accManager) {
        //CREATE THE MAIN WINDOW
        window = new JFrame(RAMBLE_ON_LABEL);
        //SET ITS SIZE
        window.setSize(GAME_WIDTH, GAME_HEIGHT);
        //CANT RESIZE IT THAT WOULD MESS EVERYTHING UP
        window.setResizable(false);
        //WINDOW HAS A LAYOUT
        window.setLayout(new BorderLayout());
        //DEFAULT CLOSE OPERATION
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //CENTER THE WINDOW IN THE USERS SCREEN
        centerWindow();

        //CREATE THE ACCOUNTS DATA MANAGER
        accountsManager = accManager;

        //LOAD THE DATA FROM THE DATA STORE
        loadData();
        //WE CAN INIT THE WELCOME SCREEN DURING THE INIT OF THE SCREEN MANAGER
        initWelcomeScreen();
        //AS WELL AS THE NEXT SCREEN THE ACCOUNTS SCREEN
        initAccountsScreen();

        //TEMPORARY DURING BETA
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     *
     * @return
     */
    public AccountDataManager getAccountsManager() {
        return accountsManager;
    }

    /**
     * This method helps us load a bunch of images and ensure they are fully
     * loaded when we want to use them.
     *
     * @param imageFile The path and name of the image file to load.
     *
     * @param tracker This will help ensure all the images are loaded.
     *
     * @param id A unique identifier for each image in the tracker. It will only
     * wait for ID's it knows about.
     *
     * @return A constructed image that has been registered with the tracker.
     * Note that the images data has not necessarily been fully loaded when this
     * method ends.
     */
    private Image batchLoadImage(String imageFile, MediaTracker tracker, int id) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image img = tk.getImage(imageFile);
        tracker.addImage(img, id);
        return img;
    }

    /**
     * Waits for all of the media given to the media tracker to fully load so
     * that they can be used by the program. Note that if we don't do this we
     * may end up with missing images for our GUI icons.
     *
     * @param tracker The tracker that already has images it is keeping an eye
     * on.
     */
    public void waitForMediaTracker(MediaTracker tracker) {
        // NOW THE MEDIA TRACKER WILL MAKE SURE ALL THE IMAGES
        // ARE DONE LOADING BEFORE WE GO ON
        try {
            tracker.waitForAll();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Sets up everything a button needs except how it is laid out, since we'll
     * have buttons put in tool bars that use FlowLayout
     *
     * @param imageFile The image to use for the button.
     *
     * @param parent The container inside which to put the button. Note that for
     * this GUI, we'll always assume FlowLayout.
     *
     * @param tracker This makes sure our button fully loads.
     *
     * @param id A unique id for the button so the tracker knows it's there.
     *
     * @param tooltip The mouse-over text for the button.
     *
     * @param eventHandler The listener that will respond to presses on this
     * button.
     *
     * @return A fully constructed and initialized button with all the data
     * provided to it as arguments. Note that it has not been laid out inside a
     * container yet, as that will be a custom operation.
     */
    private JButton initGenericButton(String imageFile,
            MediaTracker tracker,
            int id,
            String tooltip,
            ActionListener eventHandler) {
        // LOAD THE IMAGE AND MAKE AN ICON
        Image img = batchLoadImage(imageFile, tracker, id);
        ImageIcon ii = new ImageIcon(img);

        // MAKE THE BUTTON THAT WE'LL END UP RETURNING
        JButton createdButton = new JButton();

        // NOW SETUP OUR BUTTON FOR USE

        // GIVE IT THE ICON
        createdButton.setIcon(ii);

        // GIVE IT THE MOUSE-OVER TEXT
        createdButton.setToolTipText(tooltip);

        // INSETS ARE SPACING INSIDE THE BUTTON,
        // TOP LEFT RIGHT BOTTOM
        Insets buttonMargin = new Insets(
                BUTTON_INSET, BUTTON_INSET, BUTTON_INSET, BUTTON_INSET);
        createdButton.setMargin(buttonMargin);

        // INIT THE EVENT HANDLERS - NOTE WE'RE USING THE TOOLTIP
        // AS THE ACTION COMMAND SINCE THEY ARE UNIQUE FOR EACH BUTTON
        createdButton.setActionCommand(tooltip);
        createdButton.addActionListener(eventHandler);

        // AND RETURN THE CREATED BUTTON
        return createdButton;
    }

    /**
     * Create and initialize the RambleOn active player accounts screen. We can
     * initialize this screen during the creation of the screen manager since
     * all the data for this screen is known at startup
     */
    private void initAccountsScreen() {
        //CREATE ALL THE COMPONENTS THAT MAKE UP THE ACCOUNTS SCREEN
        //ALL THE PANELS
        accountsScreen = new JPanel();
        accountsScreenTablePanel = new JPanel();
        accountsScreenButtonPanel = new JPanel();
        accountsScreenCommentsPanel = new JPanel();
        accountsScreenTitlePanel = new JPanel();
        accountsScreenSubButtonPanel = new JPanel();
        //ALL THE LABELS
        createNewUserLabel = new JLabel(CREATE_NEWUSER_LABEL);
        accountsPageHeaderLabel = new JLabel(RAMBLE_ON_LABEL);
        accountsScreenName = new JLabel(PLAYER_ACCOUNTS_LABEL);
        accountsScreenName = new JLabel(PLAYER_ACCOUNTS_LABEL);
        //PLUS THE OTHER VARIOUS COMPONENTS
        accountsTable = new JTable(new AbstractActiveAccountsTableModel(accountsManager.getUserNames()));
        accountsTableScrollPane = new JScrollPane(accountsTable);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, accountsScreenCommentsPanel, accountsScreenTablePanel);
        //AND  A MEDIA TRACKER FOR LOADING IMAGES
        MediaTracker tracker = new MediaTracker(window);

        //SET THE FONT OF THE LABELS
        createNewUserLabel.setFont(LABEL_FONT);
        accountsPageHeaderLabel.setFont(HEADER_FONT);
        accountsScreenName.setFont(HEADER_FONT);

        //SET THE BORDER OF THE COMPONENTS
        accountsScreen.setBorder(TOOLBAR_BORDER);
        accountsScreenButtonPanel.setBorder(TOOLBAR_BORDER);
        accountsScreenTablePanel.setBorder(TOOLBAR_BORDER);
        accountsScreenCommentsPanel.setBorder(TOOLBAR_BORDER);
        accountsScreenTitlePanel.setBorder(TOOLBAR_BORDER);
        accountsScreenSubButtonPanel.setBorder(TOOLBAR_BORDER);
        accountsTable.setBorder(TOOLBAR_BORDER);
        accountsTableScrollPane.setBorder(TOOLBAR_BORDER);

        //SET THE LAYOUT OF ANY COMPONENTS THAT REQUIRE ONE
        accountsScreen.setLayout(new BorderLayout());
        accountsScreenTablePanel.setLayout(new BorderLayout());
        accountsScreenButtonPanel.setLayout(new BorderLayout());
        accountsScreenCommentsPanel.setLayout(new BorderLayout());

        //CREATE THE LISTENERS FOR THE BUTTON AND TABLE
        CreateAccountButtonHandler createAccountButtonHandler = new CreateAccountButtonHandler(this);
        AccountSelectionHandler accountSelectionHandler = new AccountSelectionHandler(this);
        ExitAccountManagerButtonHandler exitButtonHandler = new ExitAccountManagerButtonHandler(this);

        //CREATE THE BUTTONS
        newUserAccountButton = initGenericButton(ARTWORK_PATH + NEW_ACCOUNT_BUTTON_ICON_FILE, tracker, 0, NEW_USER_TOOLTIP, createAccountButtonHandler);
        exitButton = initGenericButton(ARTWORK_PATH + EXIT_BUTTON_FILE, tracker, 1, EXIT_BUTTON_TOOLTIP, exitButtonHandler);

        //ADD COMPONENTS TO THE BUTTON PANEL SUB PANEL
        accountsScreenSubButtonPanel.add(newUserAccountButton);
        accountsScreenSubButtonPanel.add(exitButton);

        //ADD THE BUTTON PANEL SUB PANEL TO THE MAIN BUTTON PANEL
        accountsScreenButtonPanel.add(accountsScreenSubButtonPanel, BorderLayout.LINE_START);
        //AND ADD A TITLE PAGE LABEL TO THIS PANEL
        accountsScreenButtonPanel.add(accountsScreenName);

        //ADD THE TABLE LISTENER
        accountsTable.getSelectionModel().addListSelectionListener(accountSelectionHandler);
        //SET SOME CUSTOM PREFERENCES FOR THE TABLE
        accountsTable.setPreferredScrollableViewportSize(new Dimension(500, 400));
        accountsTable.setFillsViewportHeight(true);
        accountsTable.setAutoCreateRowSorter(true);
        accountsTable.setRowHeight(ACCOUNTS_TABLE_ROW_HEIGHT);
        accountsTable.setFont(LABEL_FONT);

        //ADD THE APP NAME TO THE TITLE PANEL
        accountsScreenTitlePanel.add(accountsPageHeaderLabel);
        //AND ADD THE TITLE PANEL TO THE PANEL IN THE NORTH REGION OF THE SCREEN
        accountsScreenButtonPanel.add(accountsScreenTitlePanel, BorderLayout.LINE_END);

        //ADD SOME CUSTOM ART WHICH WILL SERVE AS AN OPTIONS DISPLAY FOR THE SCREEN
        accountMenuBanner = loadImageToLabel(ARTWORK_PATH + ACCOUNT_MENU_BANNER_FILE);
        //AND ADD THE ART TO THE COMMENTS PANEL 
        accountsScreenCommentsPanel.add(accountMenuBanner);

        //ADD THE TABLE TO ITS PANEL
        accountsScreenTablePanel.add(accountsTableScrollPane, BorderLayout.CENTER);
        //SET THE SPLIT PANE DIVIDER LOCATION
        splitPane.setDividerLocation(SPLIT_PANE_LEFT_LOCATION);

        //NOW ADD ALL THE SUB-COMPONENTS TO THE MAIN ACCOUNTS SCREEN PANEL
        accountsScreen.add(splitPane, BorderLayout.CENTER);
        accountsScreen.add(accountsScreenButtonPanel, BorderLayout.NORTH);

        //WAIT FOR ALL IMAGES TO LOAD BEFORE EXITING 
        waitForMediaTracker(tracker);
    }

    /**
     *
     * @return
     */
    public JTable getAccountsTable() {
        return accountsTable;
    }

    /**
     *
     * @param accountsTable
     */
    public void setAccountsTable(JTable accountsTable) {
        this.accountsTable = accountsTable;
    }

    /**
     * Creates the current account screen. We cannot initialize this screen at
     * start up because we do not know what the current account will be so this
     * screen is created during run time.
     */
    public void initCurrentAccountScreen() {
        //CREATE ALL THE OBJECTS NEEDED FOR THE CURRENT ACCOUNT SCREEN
        //ALL THE PANELS
        currentAccountScreen = new JPanel();
        regionsPlayedTreePanel = new JPanel();
        currentAccountStatsPanel = new JPanel();
        currentAccountButtonPanel = new JPanel();
        currentAccountTitlePanel = new JPanel();
        currentAccountSubButtonPanel = new JPanel();
        currentAccountStatsPanelSubPanel = new JPanel();

        JPanel d = new JPanel();

        //ALL THE LABELS
        currentAccountPageHeaderLabel = new JLabel(RAMBLE_ON_LABEL);
        currentAccountScreenTitle = new JLabel(CURRENT_ACCOUNT_PAGE_TITLE_HEADER);
        currentPlayerStatsLabel = new JLabel(PLAYER_STATS_LABEL);
        backToAccountsLabel = new JLabel(BACK_TO_ACCOUNTS_PAGE_LABEL);
        playRambleOnLabel = new JLabel(FOWARD_TO_GAME_MODE_LABEL);
        subRegionModeLabel = new JLabel(SUB_REGION_MODE_LABEL);
        capitalModeLabel = new JLabel(CAPITAL_MODE_LABEL);
        leaderModeLabel = new JLabel(LEADER_MODE_LABEL);
        timesPlayedLabel = new JLabel(TIMES_PLAYES_STAT_LABEL);
        fastestTimeLabel = new JLabel(FASTEST_TIME_STAT_LABEL);
        highScoreLabel = new JLabel(HIGH_SCORE_STAT_LABEL);
        flagModeLabel = new JLabel(FLAG_MODE_LABEL);

        JLabel dx = new JLabel("Fastest time in seconds / High score out of 10000");

        //AND THE OTHER VARIOUS COMPONENTS WELL USE
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, regionsPlayedTreePanel, currentAccountStatsPanel);
        statsTable = new JTable(new AbstractModeStatsTableModel());
        regionsPlayedTree = new JTree();
        //PLUS A MEDIA TRACKER FOR LOADING IMAGES
        MediaTracker tracker = new MediaTracker(window);

        //SET THE LABELS FONT
        currentAccountPageHeaderLabel.setFont(HEADER_FONT);
        currentAccountScreenTitle.setFont(HEADER_FONT);
        currentPlayerStatsLabel.setFont(HEADER_FONT);
        playRambleOnLabel.setFont(LABEL_FONT);
        backToAccountsLabel.setFont(LABEL_FONT);
        subRegionModeLabel.setFont(STATS_TABLE_LABEL_FONT);
        capitalModeLabel.setFont(STATS_TABLE_LABEL_FONT);
        leaderModeLabel.setFont(STATS_TABLE_LABEL_FONT);
        flagModeLabel.setFont(STATS_TABLE_LABEL_FONT);
        fastestTimeLabel.setFont(STATS_TABLE_LABEL_FONT);
        timesPlayedLabel.setFont(STATS_TABLE_LABEL_FONT);
        highScoreLabel.setFont(STATS_TABLE_LABEL_FONT);

        dx.setFont(LABEL_FONT);

        //SET THE BORDER FOR THE COMPONENTS
        statsTable.setBorder(TOOLBAR_BORDER);
        splitPane.setBorder(TOOLBAR_BORDER);
        regionsPlayedTree.setBorder(TOOLBAR_BORDER);
        currentAccountScreen.setBorder(TOOLBAR_BORDER);
        regionsPlayedTreePanel.setBorder(TOOLBAR_BORDER);
        currentAccountStatsPanel.setBorder(TOOLBAR_BORDER);
        currentAccountButtonPanel.setBorder(TOOLBAR_BORDER);
        currentAccountTitlePanel.setBorder(TOOLBAR_BORDER);
        currentAccountSubButtonPanel.setBorder(TOOLBAR_BORDER);
        currentAccountStatsPanelSubPanel.setBorder(TOOLBAR_BORDER);

        d.setBorder(TOOLBAR_BORDER);

        d.add(dx);


        //SET THE LAYOUT MANAGERS FOR THE COMPONENTS THAT NEED IT
        currentAccountScreen.setLayout(new BorderLayout());
        currentAccountStatsPanel.setLayout(new BorderLayout());
        regionsPlayedTreePanel.setLayout(new BorderLayout());
        currentAccountButtonPanel.setLayout(new BorderLayout());
        currentAccountStatsPanelSubPanel.setLayout(new BorderLayout());
        //WELL USE GRIDBAGLAYOUT TO ARRANGE THE TABLE AND TABLE LABELS
        currentAccountStatsPanel.setLayout(new GridBagLayout());

        //CREATE THE REGIONS PLAYED TREE
        initTree();
        //NOW FILL THE TREE WITH THE PERTENT DATA FOR THIS ACCOUNT
        initRegionsPlayedTree();
        //AND ADD IT TO THE SCROLL PANE
        regionsPlayedTreePanel.add(regionsPlayedTreeScrollPane, BorderLayout.CENTER);

        //EVENT HANDLERS FOR ALL BUTTONS IN THE CURRENT ACCOUNT SCREEN
        CurrentAccountButtonsHandler currentAccountButtonsHandler = new CurrentAccountButtonsHandler(this);
        ExitAccountManagerButtonHandler exitButtonHandler = new ExitAccountManagerButtonHandler(this);

        //CREATE THE BUTTONS FOR THIS SCREEN
        backToAccountsButton = initGenericButton(ARTWORK_PATH + CURRENT_ACCOUNT_BACK_BUTTON_FILE, tracker, 0, BACK_BUTTON_TOOLTIP, currentAccountButtonsHandler);
        toGameModeButton = initGenericButton(ARTWORK_PATH + CURRENT_ACCOUNT_START_BUTTON_FILE, tracker, 1, PLAY_BUTTON_TOOLTIP, currentAccountButtonsHandler);
        exitButton = initGenericButton(ARTWORK_PATH + EXIT_BUTTON_FILE, tracker, 1, EXIT_BUTTON_TOOLTIP, exitButtonHandler);

        //ADD COMPONENTS TO THE SUB PANEL OF THE MAIN BUTTON PANEL
        currentAccountSubButtonPanel.add(backToAccountsButton);
        currentAccountSubButtonPanel.add(toGameModeButton);
        currentAccountSubButtonPanel.add(exitButton);

        //ADD THE SCREEN HEADER TO ITS PANEL
        currentAccountTitlePanel.add(currentAccountPageHeaderLabel);
        //AND IT TO THE PANEL IN THE NORTH REGION OF THE SCREEN
        currentAccountButtonPanel.add(currentAccountTitlePanel, BorderLayout.LINE_START);


        //ADD THE SCREEN TITLE LABEL TO THE PANEL IN THE NORTH
        currentAccountButtonPanel.add(currentAccountScreenTitle);




        //AND ADD THE SUB PANEL CONTAINING THE BUTTONS TO THE SAME PANEL 
        currentAccountButtonPanel.add(currentAccountSubButtonPanel, BorderLayout.LINE_END);

        //ADD ALL THE SUB COMPONENTS TO THE MAIN CURRENT ACCOUNT SCREEN PANEL
        currentAccountScreen.add(currentAccountButtonPanel, BorderLayout.NORTH);
        currentAccountScreen.add(splitPane, BorderLayout.CENTER);

        //SET THE SPLIT PANE DIVIDER LOCATION
        splitPane.setDividerLocation(SPLIT_PANE_LEFT_LOCATION - 215);

        //ADD SOME TABLE SPECIFIC MODIFIERS
        statsTable.setRowHeight(STATS_TABLE_ROW_HEIGHT);

        //ADD THE CURRENT PLAYER STATS TABLE TO THE STATS PANEL SUB PANEL
        currentAccountStatsPanelSubPanel.add(statsTable, BorderLayout.CENTER);

        //ADD COMPONENTS TO THE CURRENT ACCOUNT STATS PANEL USING GRID BAG LAYOUT
        addComponentToAccountStatsPanel(currentAccountStatsPanelSubPanel, 4, 6, 3, 5);
        addComponentToAccountStatsPanel(currentPlayerStatsLabel, 2, 0, 1, 1);
        addComponentToAccountStatsPanel(subRegionModeLabel, 2, 6, 1, 1);
        addComponentToAccountStatsPanel(capitalModeLabel, 2, 7, 1, 1);
        addComponentToAccountStatsPanel(leaderModeLabel, 2, 8, 1, 1);
        addComponentToAccountStatsPanel(flagModeLabel, 2, 9, 1, 1);
        addComponentToAccountStatsPanel(dx, 4, 11, 2, 1);
        addComponentToAccountStatsPanel(fastestTimeLabel, 4, 4, 1, 1);
        addComponentToAccountStatsPanel(timesPlayedLabel, 5, 4, 1, 1);
        addComponentToAccountStatsPanel(highScoreLabel, 6, 4, 1, 1);

        //WAIT FOR ALL THE IMAGES TO FINISH LOADING BEFORE RETURNING
        waitForMediaTracker(tracker);
    }

    /**
     *
     * @param statsForRegion
     */
    public void updateTable(AccountStatistics statsForRegion) {
        //THE TABLE OF STATS FOR THE SELECTED REGION FOR THE CURRENT PLAYER
        JTable tbl = statsForRegion.getModeStats();

        //TRANSFER THE VALUES IN THE CURRENT PLAYERS TABLE FOR THE SELECTED REGION
        //INTO THE CURRENT ACCOUNT STATS TABLE VISIBLE ON THE CURRENT ACCOUNT SCREEN
        for (int i = 0; i < NUM_GAME_MODES; i++) {
            for (int j = 0; j < NUM_GAME_STATS; j++) {
                //TRANSFER THE STATS FROM ONE TABLE TO THE OTHER
                statsTable.setValueAt(tbl.getValueAt(i, j), i, j);
            }
        }
    }

    /**
     * Gets the text in the newUserNameTextField.
     *
     * @return
     */
    public String getUserNameFromTextField() {
        return newUserNameTextField.getText();
    }

    /**
     *
     * @return
     */
    public JTextField getNewUserNameTextField() {
        return newUserNameTextField;
    }

    /**
     *
     * @return
     */
    public JTree getRegionsPlayedTree() {
        return regionsPlayedTree;
    }

    /**
     *
     * @param regionsPlayedTree
     */
    public void setRegionsPlayedTree(JTree regionsPlayedTree) {
        this.regionsPlayedTree = regionsPlayedTree;
    }

    /**
     *
     * @return
     */
    public JTable getStatsTable() {
        return statsTable;
    }

    /**
     *
     * @param statsTable
     */
    public void setStatsTable(JTable statsTable) {
        this.statsTable = statsTable;
    }

    /**
     *
     *
     * @param componentToAdd
     * @param col
     * @param row
     * @param colSpan
     * @param rowSpan
     */
    private void addComponentToAccountStatsPanel(Component componentToAdd, int col, int row, int colSpan, int rowSpan) {
        // EMPLOY GRID BAG LAYOUT TO PUT IT IN THE PANEL
        GridBagConstraints gbc = new GridBagConstraints();
        //SET THE X AND Y POSITIONS
        gbc.gridx = col;
        gbc.gridy = row;
        //SET THE WIDTH AND HEIGHT 
        gbc.gridwidth = colSpan;
        gbc.gridheight = rowSpan;
        //SET THE X AND Y PADS
        gbc.ipadx = IPADX;
        gbc.ipady = IPADY;
        //SET THE INSETS
        gbc.insets = INSETS;
        gbc.fill = GridBagConstraints.BOTH;
        //ADD THE COMPONENT TO THE CURRENT ACCOUNT STATS PANEL
        currentAccountStatsPanel.add(componentToAdd, gbc);
    }

    /**
     * Creates the new account creation screen.
     */
    public void initAccountCreationScreen() {
        //ALL THE COMPONENTS THAT MAKE UP THE ACCOUNT CREATIONS SCREEN
        //THE PANELS
        accountCreationScreen = new JPanel();
        accountCreationScreenTextFieldPanel = new JPanel();
        accountCreationScreenCommentsPanel = new JPanel();
        accountCreationScreenTitlePanel = new JPanel();
        accountCreationScreenButtonPanel = new JPanel();
        accountCreationScreenAppTitlePanel = new JPanel();
        accountCreationScreenAppTitleSubPanel = new JPanel();
        //TEXT FIELD
        newUserNameTextField = new JTextField();
        //LABELS
        createAccountHeaderLabel = new JLabel(CREATE_NEW_USER_ACCOUNT_LABEL);
        newAccountUserNameLabel = new JLabel(SPECIFY_USER_NAME_LABEL);
        appTitleLabel = new JLabel(ACCOUNT_CREATION_PAGE_TITLE_HEADER);
        //SPLIT PANE
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, accountCreationScreenCommentsPanel, accountCreationScreenTextFieldPanel);
        //AND THE MEDIA TRACKER FOR LOADING IMAGES
        MediaTracker tracker = new MediaTracker(window);

        //SET THE LAYOUTS
        accountCreationScreen.setLayout(new BorderLayout());
        accountCreationScreenTextFieldPanel.setLayout(new GridBagLayout());
        accountCreationScreenCommentsPanel.setLayout(new BorderLayout());
        accountCreationScreenTitlePanel.setLayout(new BorderLayout());

        //SET THE BORDERS
        newUserNameTextField.setBorder(TOOLBAR_BORDER);
        accountCreationScreenTextFieldPanel.setBorder(TOOLBAR_BORDER);
        accountCreationScreenCommentsPanel.setBorder(TOOLBAR_BORDER);
        //accountCreationScreenAppTitlePanel.setBorder(TOOLBAR_BORDER);
        accountCreationScreenButtonPanel.setBorder(TOOLBAR_BORDER);
        //accountCreationScreenAppTitleSubPanel.setBorder(TOOLBAR_BORDER);

        //SET THE FONTS ON THE LABELS
        createAccountHeaderLabel.setFont(HEADER_FONT);
        newAccountUserNameLabel.setFont(LABEL_FONT);
        appTitleLabel.setFont(HEADER_FONT);

        //THE HANDLER FOR ALL BUTTONS ON THIS PAGE
        AccountCreationScreenButtonsHandler accountCreationScreenButtonsHandler = new AccountCreationScreenButtonsHandler(this);

        //CREATE THE BUTTONS FOR THIS PAGE
        //THE OK BUTTON
        accountCreationOkButton = initGenericButton(ARTWORK_PATH + NEW_ACCOUNT_BUTTON_ICON_FILE, tracker, 0, CREATE_ACCOUNT_OK_TOOLTIP, accountCreationScreenButtonsHandler);
        //AND THE CANCEL BUTTON
        accountCreationCancelButton = initGenericButton(ARTWORK_PATH + EXIT_BUTTON_FILE, tracker, 1, CREATE_ACCOUNT_CANCEL_TOOLTIP, accountCreationScreenButtonsHandler);

        //LOAD THE BANNER IMAGE ONTO A LABEL
        accountCreationScreenBanner = loadImageToLabel(ARTWORK_PATH + CREATE_ACCOUNT_SCREEN_BANNER_FILE);
        //AND ADD THE BANNER TO THE COMMENTS PANEL
        accountCreationScreenCommentsPanel.add(accountCreationScreenBanner, BorderLayout.CENTER);

        //ADD THE BUTTONS TO THE BUTTON PANEL
        accountCreationScreenButtonPanel.add(accountCreationOkButton);
        accountCreationScreenButtonPanel.add(accountCreationCancelButton);

        //ADD THE APP TITLE TO THE APP TITLE PANEL
        accountCreationScreenAppTitleSubPanel.add(appTitleLabel);
        accountCreationScreenAppTitlePanel.add(accountCreationScreenAppTitleSubPanel);

        //SET THE SPLIT PANE DIVIDER LOCATION
        splitPane.setDividerLocation(SPLIT_PANE_LEFT_LOCATION);

        //ADD THE SCREEN HEADER TO THE PAGE TITLE PANEL
        accountCreationScreenTitlePanel.add(createAccountHeaderLabel);

        //INSERT THE COMPONENTS ONTO THE ACCOUNT CREATION SCREEN TEXT FIELD PANEL USING A GRID BAG LAYOUT
        //ADD THE TEXT FIELD 
        addComponentToAccountCreationScreenTextFieldPanel(newUserNameTextField, 5, 7, 3, 1);
        //ADD THE BUTTON PANEL
        addComponentToAccountCreationScreenTextFieldPanel(accountCreationScreenButtonPanel, 5, 8, 3, 1);
        //ADD THE SCREEN TITLE PANEL
        addComponentToAccountCreationScreenTextFieldPanel(accountCreationScreenTitlePanel, 5, 0, 3, 1);
        //ADD THE SPECIFY USER NAME LABEL
        addComponentToAccountCreationScreenTextFieldPanel(newAccountUserNameLabel, 3, 7, 1, 1);

        //ADD THE SUB PANELS TO THE MAIN ACCOUNT CREATION SCREEN PANEL
        //THE TEXT FIELD PANEL
        accountCreationScreen.add(splitPane, BorderLayout.CENTER);
        //THE APP TITLE PANEL
        accountCreationScreen.add(accountCreationScreenAppTitlePanel, BorderLayout.NORTH);
        //accountCreationScreen.add(appTitleLabel, BorderLayout.NORTH);
    }

    /**
     *
     *
     * @param componentToAdd
     * @param col
     * @param row
     * @param colSpan
     * @param rowSpan
     */
    private void addComponentToAccountCreationScreenTextFieldPanel(Component componentToAdd, int col, int row, int colSpan, int rowSpan) {
        // EMPLOY GRID BAG LAYOUT TO PUT IT IN THE PANEL
        GridBagConstraints gbc = new GridBagConstraints();
        //SET THE X AND Y POSITIONS
        gbc.gridx = col;
        gbc.gridy = row;
        //SET THE WIDTH AND HEIGHT 
        gbc.gridwidth = colSpan;
        gbc.gridheight = rowSpan;
        //SET THE X AND Y PADS
        gbc.ipadx = IPADX;
        gbc.ipady = IPADY;
        //SET THE INSETS
        gbc.insets = INSETS;
        gbc.fill = GridBagConstraints.BOTH;
        //ADD THE COMPONENT TO THE CURRENT ACCOUNT STATS PANEL
        accountCreationScreenTextFieldPanel.add(componentToAdd, gbc);
    }

    /**
     * Initializes the tree with the proper data, including setting up the
     * tree's data model.
     */
    public void initRegionsPlayedTree() {
        //SET THE ROOT
        Region world = new Region(REGIONS_PLAYED_ROOT, REGIONS_PLAYED_ROOT, RegionType.WORLD);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(world), worldSubTree = new DefaultMutableTreeNode(world);


        // IF FIRST TIME, MAKE MODEL
        if (regionsPlayedTreeModel == null) {
            //WELL USE THE DEFAULT TREE MODEL
            regionsPlayedTreeModel = new DefaultTreeModel(root);
            //SET THE TREES DATA MODEL
            regionsPlayedTree.setModel(regionsPlayedTreeModel);
        } else {
            //SET THE ROOT OF THE TREE
            regionsPlayedTreeModel.setRoot(root);
        }
        //CREATE A REGION FOR EACH CONTINENT AND THE WORLD
        Region asia = new Region(ASIA_SUBTREE_ROOT, ASIA_SUBTREE_ROOT, RegionType.CONTINENT);
        Region southAmerica = new Region(SOUTH_AMERICA_SUBTREE_ROOT, SOUTH_AMERICA_SUBTREE_ROOT, RegionType.CONTINENT);
        Region europe = new Region(EUROPE_SUBTREE_ROOT, EUROPE_SUBTREE_ROOT, RegionType.CONTINENT);
        Region northAmerica = new Region(NORTH_AMERICA_SUBTREE_ROOT, NORTH_AMERICA_SUBTREE_ROOT, RegionType.CONTINENT);
        Region africa = new Region(AFRICA_SUBTREE_ROOT, AFRICA_SUBTREE_ROOT, RegionType.CONTINENT);
        Region antartica = new Region(ANTARTICA_SUBTREE_ROOT, ANTARTICA_SUBTREE_ROOT, RegionType.CONTINENT);
        Region australia = new Region(AUSTRALIA_SUBTREE_ROOT, AUSTRALIA_SUBTREE_ROOT, RegionType.CONTINENT);

        //AND DECLARE A TREE NODE FOR EACH
        DefaultMutableTreeNode asiaSubTree, southAmericaSubTree, europeSubTree, northAmericaSubTree, africaSubTree,
                antarticaSubTree, australiaSubTree;

        //AND INITIALIZE THE TREE NODE FOR EACH
        asiaSubTree = new DefaultMutableTreeNode(asia);
        southAmericaSubTree = new DefaultMutableTreeNode(southAmerica);
        europeSubTree = new DefaultMutableTreeNode(europe);
        northAmericaSubTree = new DefaultMutableTreeNode(northAmerica);
        africaSubTree = new DefaultMutableTreeNode(africa);
        antarticaSubTree = new DefaultMutableTreeNode(antartica);
        australiaSubTree = new DefaultMutableTreeNode(australia);

        //ITERATOR TO ALL REGIONS PLAYED BY THE CURRENT USER ACCOUNT 
        Iterator<Region> currentAccountRegionsPlayed = accountsManager.getCurrentAccount().getRegionsPlayed().iterator();

        //FOR EVERY REGION THE CURRENT USER HAS PLAYED
        while (currentAccountRegionsPlayed.hasNext()) {
            //GET THE NEXT REGION IN THE LIST OF REGIONS PLAYED BY THE CURRENT ACCOUNT
            Region regionToAdd = currentAccountRegionsPlayed.next();
            //GET THE PARENT REGION OF THE CURRENT REGION
            //Region regionsParent = regionToAdd.getParentRegion();

            //CREATE A TREENODE FOR THE REGION TO ADD
            DefaultMutableTreeNode regionNode = new DefaultMutableTreeNode(regionToAdd);

            //FLAGS
            boolean asiaFlag, southAmericaFlag, europeFlag, northAmericaFlag, africaFlag, antarticaFlag, australiaFlag, worldFlag;
            asiaFlag = southAmericaFlag = europeFlag = northAmericaFlag = africaFlag = antarticaFlag = australiaFlag = worldFlag = false;

            //SWITCH FOR THE PARENT REGION AND ADD THE REGION TO THAT SUB TREE
            switch (regionToAdd.getParentRegion().trim()) {

                case "The World": {
                    if (!worldFlag) {
                        root.add(worldSubTree);
                        worldFlag = true;
                    }
                    worldSubTree.add(regionNode);
                    break;
                }
                case "Asia": {
                    if (!asiaFlag) {
                        root.add(asiaSubTree);
                        asiaFlag = true;
                    }
                    asiaSubTree.add(regionNode);
                    break;
                }
                case "South America": {
                    if (!southAmericaFlag) {
                        root.add(southAmericaSubTree);
                        southAmericaFlag = true;
                    }
                    southAmericaSubTree.add(regionNode);
                    break;
                }
                case "North America": {
                    if (!northAmericaFlag) {
                        root.add(northAmericaSubTree);
                        northAmericaFlag = true;
                    }
                    northAmericaSubTree.add(regionNode);
                    break;
                }
                case "Africa": {
                    if (!africaFlag) {
                        root.add(africaSubTree);
                        africaFlag = true;
                    }
                    africaSubTree.add(regionNode);
                    break;
                }
                case "Antartica": {
                    if (!antarticaFlag) {
                        root.add(antarticaSubTree);
                        antarticaFlag = true;
                    }
                    antarticaSubTree.add(regionNode);
                    break;
                }
                case "Australia": {
                    if (!australiaFlag) {
                        root.add(australiaSubTree);
                        australiaFlag = true;
                    }
                    australiaSubTree.add(regionNode);
                    break;
                }
                case "Europe": {
                    if (!europeFlag) {
                        root.add(europeSubTree);
                        europeFlag = true;
                    }
                    europeSubTree.add(regionNode);
                    break;
                }
            }
        }

        //REMOVE ALL CONTINENTS THAT DID NOT RECIEVE ANY SUB REGIONS
        Enumeration<DefaultMutableTreeNode> e = root.children();

        while (e.hasMoreElements()) {
            DefaultMutableTreeNode continentNode = e.nextElement();
            if (continentNode.getChildCount() <= 0) {
                root.remove(continentNode);
            }
        }

        //MAKE THE ROOT NODE THE INITIAL SELECTED NODE
        selectRootNode();
    }

    /**
     * Selects the root node in the tree and opens the tree to display its
     * children.
     */
    public void selectRootNode() {
        regionsPlayedTree.setSelectionRow(FIRST_LEVEL_OF_TREE);
        openFirstRowOfWorldTree();
    }

    /**
     * This method opens all the child nodes of the root. We use this when the
     * application first starts.
     */
    public void openFirstRowOfWorldTree() {
        //EXPAND THE FIRST LEVEL OF THE TREE 
        regionsPlayedTree.expandRow(FIRST_LEVEL_OF_TREE);
    }

    /**
     *
     *
     * @param regionToAdd
     * @param parentNode
     */
    private void addSubRegionsToTree(Region regionToAdd, DefaultMutableTreeNode parentNode) {
        //CREATE A TREE NODE OF THE REGION TO ADD TO THE REGIONS PLAYED TREE
        // DefaultMutableTreeNode regionPlayedNode = new DefaultMutableTreeNode(regionToAdd);
        //THEN INSERT THAT NODE INTO THE TREE
        //regionsPlayedTreeModel.insertNodeInto(regionPlayedNode, parentNode, parentNode.getChildCount());

        // WE'LL NEED TO ARRANGE THEM IN SORTED ORDER BY NAME
        TreeMap<String, Region> subRegions = new TreeMap();
        Iterator<Region> subRegionsIterator = regionToAdd.getSubRegions();
        while (subRegionsIterator.hasNext()) {
            Region subRegion = subRegionsIterator.next();
            subRegions.put(subRegion.getName(), subRegion);
        }

        // ADD ALL THE CHILDREN
        Iterator<String> keysIterator = subRegions.keySet().iterator();
        while (keysIterator.hasNext()) {
            String subRegionName = keysIterator.next();
            Region subRegion = subRegions.get(subRegionName);
            DefaultMutableTreeNode subNode = new DefaultMutableTreeNode(subRegion);
            regionsPlayedTreeModel.insertNodeInto(subNode, parentNode, parentNode.getChildCount());

            // HERE IS OUR RECURSIVE CALL TO CASCADE IT ON DOWN
            addSubRegionsToTree(subRegion, subNode);
        }

    }

    /**
     * Forces the world tree to open all nodes necessary to display the node
     * that is currently selected.
     */
    public void openSelectedRegion() {
        // FIND THE NODE BY WALKING THE TREE
        DefaultMutableTreeNode walker = (DefaultMutableTreeNode) regionsPlayedTreeModel.getRoot();

        // GET THE PATH TO THE NODE WE WANT OPENED
        TreeNode[] path = walker.getPath();
        int count = 0;
        for (int i = 0; i < path.length; i++) {
            DefaultMutableTreeNode testNode = (DefaultMutableTreeNode) path[i];
            count = count + walker.getIndex(testNode) + 1;

            // EXPAND EACH NODE'S ROW AS WE GO
            regionsPlayedTree.expandRow(count);
            walker = testNode;
        }
    }

    /**
     * Centers the window in the users console.
     */
    private void centerWindow() {
        // GET THE SCREEN SIZE FROM THE TOOLKIT AND USE THOSE DIMENSIONS
        // TO CALCULATE HOW TO CENTER IT ON THE SCREEN
        Toolkit singletonToolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = singletonToolkit.getScreenSize();
        int halfWayX = screenSize.width / 2;
        int halfWayY = screenSize.height / 2;
        int halfWindowWidth = window.getWidth() / 2;
        int halfWindowHeight = window.getHeight() / 2;
        int windowX = halfWayX - halfWindowWidth;
        int windowY = halfWayY - halfWindowHeight;
        window.setLocation(windowX, windowY - 25);
    }

    /**
     * Initializes the regions played tree.
     */
    private void initTree() {
        // INIT THE TREE
        regionsPlayedTree = new JTree();

        // MAKE IT SCROLLABLE
        regionsPlayedTreeScrollPane = new JScrollPane(regionsPlayedTree);

        //AND HOOK UP THE TREE LISTENER
        RegionsPlayedTreeHandler treeHandler = new RegionsPlayedTreeHandler(this);
        regionsPlayedTree.addTreeSelectionListener(treeHandler);
    }

    /**
     * Creates and returns a JLabel with the image given by the file name.
     *
     * @param filePath The file path to the image we are adding to a label.
     * @return The label with the image added to it.
     */
    private JLabel loadImageToLabel(String filePath) {
        //THE LABEL WELL ADD OUR IMAGE TO
        JLabel label = null;

        try {
            //READ IN THE IMAGE FROM THE SPECIFIED FILE
            BufferedImage myPicture = ImageIO.read(new File(filePath));

            //AND ADD IT TO THE LABEL
            label = new JLabel(new ImageIcon(myPicture));
        } catch (IOException a) {
            a.printStackTrace();
        }
        //RETURN THE LABEL 
        return label;
    }

    /**
     * Creates the applications welcome screen.
     */
    private void initWelcomeScreen() {
        //CREATE ALL THE COMPONENTS NEEDED FOR THE WELCOME SCREEN
        MediaTracker tracker = new MediaTracker(window);
        welcomeScreen = new JPanel();

        //SET THE LAYOUTS FOR ANY COMPONENTS THAT NEED ONE
        welcomeScreen.setLayout(new BorderLayout());

        //CREATE THE HANDLER TO HANDLE EVENTS FOR THIS SCREEN
        WelcomePageHandler welcomeHandler = new WelcomePageHandler(this);

        //THE WELCOME SCREEN WILL CONSIST OF A WELCOME PAGE IMAGE THAT IS ONE LARGE BUTTON
        welcomeScreenButton = initGenericButton(ARTWORK_PATH + WELCOME_SCREEN_FILE, tracker, 0, WELCOME_SCREEN_TOOLTIP, welcomeHandler);

        //ADD ALL THE COMPONENTS TO THE MAIN PANEL
        welcomeScreen.add(welcomeScreenButton, BorderLayout.CENTER);

        //WAIT FOR ANY IMAGES TO LOAD BEFORE WE RETURN
        waitForMediaTracker(tracker);
    }

    /**
     * To start the application we load the WelcomeScreen. The event handling
     * takes care of going from one screen to the next.
     */
    public void startApp() {
        //THE APP STARTS FROM THE WELCOME SCREEN AND PROGRESSES 
        window.add(welcomeScreen, BorderLayout.CENTER);
        //SET THE MENU FRAME TO VISIBLE 
        window.setVisible(true);
    }

    /**
     *
     */
    public void gotoAccountsManager() {
        window.setVisible(true);
        getWindow().add(getCurrentAccountScreen());
        refreshWindow();
    }

    /**
     * Kills the application by closing the window and stopping execution.
     */
    public void killApp() {
        window.setVisible(false);
        System.exit(0);
    }

    /**
     * Refreshes the window after removing one panel and adding another when
     * going to and from the different game screens.
     */
    public void refreshWindow() {
        //REPAINT THE WINDOW AFTER AN UPDATE OF COMPONENTS IN THE CURRENT WINDOW
        window.repaint();
        //AND REVALIDATE IT 
        window.revalidate();
    }

    /**
     * Resets the cells in the game statistics table on the current account page
     * between changes of the current user i.e different account selections.
     */
    public void resetStatTableCells() {
        AbstractModeStatsTableModel statsTbl = (AbstractModeStatsTableModel) this.getStatsTable().getModel();

        //RESET EACH CELL IN THE STATS TABLE ON THE CURRENT ACCOUNT PAGE
        //TO ITS INITIAL STATE WHICH IS JUST ZERO VALUES IN EACH CELL. THIS
        //IS DONE FOR EACH ACCOUNT SELECTION AND CURRENT ACCOUNT CHANGE
        for (int i = 0; i < NUM_GAME_MODES; i++) {
            for (int j = 0; j < NUM_GAME_STATS; j++) {
                //SET EACH CELL TO ITS INITIAL STATE
                statsTable.setValueAt(0, i, j);
            }
        }
    }

    /*
     * Loads the accounts data from file into the accounts manager and
     * necessary data structures.
     */
    public void loadData() {
        //DATA FILE
        String fileName = DATA_PATH + DATA_FILE;
        File ipf = new File(fileName);
        //OPEN A STREAM TO THE DATASTORE
        try {
            //READ THE DATA FROM THE STORE 
            Scanner in = new Scanner(ipf);
            while (in.hasNext()) {
                //FIRST LINE IS ACCOUNT USER NAME

                //NOTE - THIS IS ONLY READING A SINGLE STRING FOR A USER NAME
                //IDEALLY WE WANT TO BE ABLE TO HANDLE SPACES?? 
                //IF NOT JUST LET THE USER KNOW THE SPECS FOR USERNAMES AND
                //PREVENT ANYBODY FROM TRYING TO SAVE A BAD ONE
                String accountName = in.next();
                Account newAccount;

                //CREATE A NEW ACCOUNT AND ADD IT TO THE ACCOUTNS MANAGER DATABASE
                if (accountsManager.isNewUserNameUnique(accountName)) {
                    accountsManager.createNewAccount(accountName);
                    //GET A REFERENCE TO THE ACCOUNT CREATED
                    newAccount = accountsManager.getAccounts().get(accountName);
                } else {
                    newAccount = accountsManager.getAccounts().get(accountName);
                }
                //NEXT LINE IS THE NUMBER OF REGIONS PLAYED
                int regionsPlayed = in.nextInt();

                //GO THROUGH EACH REGION AND ADD IT TO THE REGIONS PLAYED FOR THIS 
                //ACCOUNT AND ADD THE CORRESPONDING STATS
                for (int i = 0; i < regionsPlayed; i++) {

                    //System.out.println(regionsPlayed);

                    //NAME OF THE REGION PLAYED
                    String regionPlayed = in.next();

                    //System.out.println(regionPlayed);

                    //NAME OF THE REGION PLAYEDS PARENT REGION
                    String parentRegion = in.nextLine();


                    //CREATE A REGION OBJECT OF THE REGION PLAYED
                    Region regionToAdd = new Region(regionPlayed, regionPlayed, RegionType.NATION);
                    regionToAdd.setParent(parentRegion);

                    //SET THE PARENT LINKAGE
                    // regionToAdd.setParent(parentOfRegionToAdd);
                    //AND ADD THE REGION TO THE LIST OF REGIONS PLAYED BY THE CURRENT ACCOUNT

                    if (!(newAccount.getRegionsPlayed().contains(regionToAdd))) {
                        newAccount.getRegionsPlayed().add(regionToAdd);
                    }

                    //CREATE AN ACCOUNT STATISTICS OBJECT FOR THE REGION PLAYED                  
                    AccountStatistics stats = new AccountStatistics();

                    //MAP THIS ACCOUNT STATISTIC TO THE REGION IT WAS THE RESULT OF PLAYING
                    newAccount.getRegionStats().put(regionPlayed, stats);

                    //MODE 1 STATS
                    int stat11 = in.nextInt();
                    int stat12 = in.nextInt();
                    int stat13 = in.nextInt();
                    //MODE 2 STATS
                    int stat21 = in.nextInt();
                    int stat22 = in.nextInt();
                    int stat23 = in.nextInt();
                    //MODE 3 STATS
                    int stat31 = in.nextInt();
                    int stat32 = in.nextInt();
                    int stat33 = in.nextInt();
                    //MODE 4 STATS
                    int stat41 = in.nextInt();
                    int stat42 = in.nextInt();
                    int stat43 = in.nextInt();

                    //FILL THE TABLE WITH THE STATS
                    stats.getModeStats().getModel().setValueAt(stat11, 0, 0);
                    stats.getModeStats().getModel().setValueAt(stat12, 0, 1);
                    stats.getModeStats().getModel().setValueAt(stat13, 0, 2);
                    stats.getModeStats().getModel().setValueAt(stat21, 1, 0);
                    stats.getModeStats().getModel().setValueAt(stat22, 1, 1);
                    stats.getModeStats().getModel().setValueAt(stat23, 1, 2);
                    stats.getModeStats().getModel().setValueAt(stat31, 2, 0);
                    stats.getModeStats().getModel().setValueAt(stat32, 2, 1);
                    stats.getModeStats().getModel().setValueAt(stat33, 2, 2);
                    stats.getModeStats().getModel().setValueAt(stat41, 3, 0);
                    stats.getModeStats().getModel().setValueAt(stat42, 3, 1);
                    stats.getModeStats().getModel().setValueAt(stat43, 3, 2);
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(RambleOnScreenManager.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Write the data in the accounts database to the account data file before
     * the application terminates so to enable data persistence for the life of
     * the application.
     */
    public void writeData() {
        //ATTEMPT TO OPEN A WRITER TO THE DATASTORE
        PrintWriter out = null;
        try {
            //CONNECT WITH THE STORE
            out = new PrintWriter(new FileOutputStream(DATA_PATH + DATA_FILE));


        } catch (FileNotFoundException ex) {
            Logger.getLogger(RambleOnScreenManager.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        //GET A MAPPING OF ALL THE USERNAMES TO THEIR ACCOUNTS
        TreeMap<String, Account> currentAccounts = accountsManager.getAccounts();
        //AND A LIST OF ALL THE USERNAMES OF ACTIVE ACCOUNTS
        Iterator<String> userNames = accountsManager.getUserNames().iterator();

        //ITERATE THROUGH EVERY ACCOUNT
        while (userNames.hasNext()) {
            //GET CURRENT ACCOUNT USER NAME
            String currentAccountUserName = userNames.next();
            //AND THE ACCOUNT FOR THAT USER NAME
            Account currentAccount = currentAccounts.get(currentAccountUserName);
            //AND A LIST OF ALL THE REGIONS PLAYED FOR THE ACCOUNT
            Iterator<Region> regionsPlayed = currentAccount.getRegionsPlayed().iterator();
            //AND THE NUMBER OF REGIONS PLAYED
            int numRegionsPlayed = currentAccount.getRegionsPlayed().size();
            //AND A MAP TO THE STATS FOR THOSE REGIONS THAT WERE PLAYED
            TreeMap<String, AccountStatistics> statsForRegionsPlayed = currentAccount.getRegionStats();

            //DUMP THE USER NAME
            out.println(currentAccountUserName);
            //AND THE NUMBER OF REGIONS PLAYED
            out.println(numRegionsPlayed);

            //NOW PRINT THE STATS FOR EVERY ONE OF THE REGIONS PLAYED
            while (regionsPlayed.hasNext()) {
                //THE REGION PLAYED
                Region region = regionsPlayed.next();
                //THE STATS FOR THIS REGION
                AccountStatistics regionStats = statsForRegionsPlayed.get(region.getName());
                //PRINT THE REGION NAME FOLLOWED BY SPACE AND THE PARENT REGION NAME
                out.println(region.getName().trim() + " " + region.getParentRegion().trim());

                TreeMap<Integer, String> modeMap = regionStats.getRowToModeNameMap();
                TreeMap<Integer, String> statsMap = regionStats.getColToStateNameMap();

                //NOW DUMP THE STATS
                for (int i = 0; i < NUM_GAME_MODES; i++) {
                    for (int j = 0; j < NUM_GAME_STATS; j++) {
                        //MAP THE INTEGER VALUS TO STRING VALUES
                        String mode = modeMap.get(i);
                        String stat = statsMap.get(j);
                        //PRINT THE STAT FROM EACH CELL SEPERATED BY  A SPACE
                        out.print(regionStats.getStats(mode, stat) + " ");
                    }
                    //GO TO THE NEXT LINE
                    out.println();
                }
            }
        }
        //CLOSE THE OUT STREAM
        out.close();
    }

    //ACCESSOR AND MUTATOR METHODS FOR THE SCREENS
    /**
     *
     * @return
     */
    public JFrame getWindow() {
        return window;
    }

    /**
     *
     * @return
     */
    public RegionDataManager getdataManager() {
        return dataManager;
    }

    /**
     *
     * @return
     */
    public JPanel getWelcomeScreen() {
        return welcomeScreen;
    }

    /**
     *
     * @return
     */
    public JPanel getAccountsScreen() {
        return accountsScreen;
    }

    /**
     *
     * @return
     */
    public JPanel getAccountCreationScreen() {
        return accountCreationScreen;
    }

    /**
     *
     * @return
     */
    public JPanel getCurrentAccountScreen() {
        return currentAccountScreen;
    }

    /**
     *
     * @return
     */
    public JButton getWelcomeScreenButton() {
        return welcomeScreenButton;
    }
}