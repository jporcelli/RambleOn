package accountdata;

import java.util.TreeMap;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import static rambleon.RambleOnSettings.*;

/**
 * An AccountStatistics object is a data structure containing the game
 * statistics for every game play mode for a particular region.
 *
 * @author James C. Porcelli, SBU ID 108900819
 */
public class AccountStatistics {

    //A TABLE CONTAINING 12 DATA FIELDS, 3 STATS FOR EACH OF THE 4 MODES
    private JTable modeStats;
    private AbstractTableModel modeStatsTableModel;
    //MAPPINGS SO WE CAN EASILY ACCESS THE ACCOUNT STATISTICS TABLES
    //TO INSERT AND READ DATA
    private TreeMap<String, Integer> modeNameToRowMap;
    private TreeMap<String, Integer> statNameToColMap;
    private TreeMap<Integer, String> rowToModeNameMap;
    private TreeMap<Integer, String> colToStatNameMap;

    /**
     * Create new AccountStatistics for a new region played for an RambleOn user
     * account.
     *
     */
    public AccountStatistics() {
        //CREATE THE NEEDED DATA STRUCTURES
        //THE TABLE MODEL
        modeStatsTableModel = new AbstractModeStatsTableModel();
        //AND THE TABLE
        modeStats = new JTable(modeStatsTableModel);
        //AND THE MAPS
        modeNameToRowMap = new TreeMap<>();
        statNameToColMap = new TreeMap<>();
        rowToModeNameMap = new TreeMap<>();
        colToStatNameMap = new TreeMap<>();

        initMappings();
    }

    /**
     * Initialize the mappings for mode and statistic names to the column and
     * row they reside in the account statistics table, and vice versa for
     * column and row value to mode, and statistic name
     */
    private void initMappings() {
        //GAME MODES
        String rowNames[] = {NAME_MODE, CAPITAL_MODE, LEADER_MODE, FLAG_MODE};
        //GAME STATS
        String colNames[] = {FASTEST_TIME, GAMES_PLAYED, HIGH_SCORE};

        //MAP FOR MODES
        for (int i = 0; i < rowNames.length; i++) {
            rowToModeNameMap.put(i, rowNames[i]);
            modeNameToRowMap.put(rowNames[i], i);
        }
        //AND FOR STATS
        for (int i = 0; i < colNames.length; i++) {
            colToStatNameMap.put(i, colNames[i]);
            statNameToColMap.put(colNames[i], i);
        }
    }

    /**
     * We can use this access method to get a copy of the JTable containing all
     * the game statistics for this AccountStatistics object which would belong
     * to a given user.
     *
     * @return The JTable containing all the game statistics for the user to
     * whom this AccountStatistics object belongs
     */
    public JTable getModeStats() {
        return modeStats;
    }

    /**
     *
     * @return
     */
    public AbstractTableModel getModeStatsTableModel() {
        return modeStatsTableModel;
    }

    /**
     *
     * @return
     */
    public TreeMap<String, Integer> getModeNameToRowMap() {
        return modeNameToRowMap;
    }

    /**
     *
     * @return
     */
    public TreeMap<String, Integer> getStatNameToColMap() {
        return statNameToColMap;
    }

    /**
     *
     * @return
     */
    public TreeMap<Integer, String> getRowToModeNameMap() {
        return rowToModeNameMap;
    }

    /**
     *
     * @return
     */
    public TreeMap<Integer, String> getColToStateNameMap() {
        return colToStatNameMap;
    }

    /**
     *
     * @param gameMode
     * @param gameDuration
     * @param score
     */
    public void addResults(String gameMode, int gameDuration, int score) {
        //THE STATISTICAL CATERGORIED WE ARE TRACKING FOR EACH
        //MODE, FOR A GIVEN REGION
        int fastestTime = getStats(gameMode, FASTEST_TIME);
        int highScore = getStats(gameMode, HIGH_SCORE);
        int gamesPlayed = getStats(gameMode, GAMES_PLAYED);

        //SEE IF WE HAVE A NEW FASTEST TIME
        if (fastestTime > gameDuration) {
            //IF SO WELL UPDATE IT
            fastestTime = gameDuration;
        }

        //SEE IF WE HAVE A NEW HIGH SCORE
        if (highScore < score) {
            //IF SO UPDATE IT
            highScore = score;
        }

        //WERE HERE BECAUSE THE CURRENT USER COMPLETED THE REGION FOR WHICH WE 
        //ARE NOW UPDATING THE STATS SO TIMES PLAYED GETS INCREMENTED BY 1
        gamesPlayed++;

        //UPDATE THE FASTEST TIME STAT FOR THIS MODE
        setStat(gameMode, FASTEST_TIME, fastestTime);
        //UPDATE THE HIGH SCORE STAT FOR THIS MODE
        setStat(gameMode, HIGH_SCORE, highScore);
        //UPDATE THE GAMES PLAYED STAT FOR THIS MODE
        setStat(gameMode, GAMES_PLAYED, gamesPlayed);

    }

    /**
     * Get the specified statistic for the specified mode and statistical value.
     *
     * @param mode
     * @param stat
     * @return
     */
    public int getStats(String mode, String stat) {
        //MAP THE STRING CONSTANT SPECIFIER FOR THE DESIRED MODE AND STAT TYPE
        //TO THE ROW, AND COLUMN LOCATION IN THE TABLE 
        int row = modeNameToRowMap.get(mode),
                col = statNameToColMap.get(stat);

        //ALL STATS ARE INTEGER VALUES
        return (int) modeStatsTableModel.getValueAt(row, col);
    }

    /**
     *
     * @param mode
     * @param stat
     * @param value
     */
    public void setStat(String mode, String stat, int value) {
        //SET THE SPECIFIED STAT FOR THE SPECIFIED MODE WITH THE SPECIFIED VALUE
        modeStatsTableModel.setValueAt(value, modeNameToRowMap.get(mode), statNameToColMap.get(stat));
    }
}
