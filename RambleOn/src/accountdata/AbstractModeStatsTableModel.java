/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package accountdata;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import static rambleon.RambleOnSettings.*;

/**
 * This table model represents the table that will hold the statistical data for
 * the results of game play for a user for a single region. There are 4 game
 * modes and we are tracking 3 statistical categories so we have a 4 x 3 table
 * for each region for every user.
 *
 * @author James C. Porcelli, SBU ID 108900819
 */
public class AbstractModeStatsTableModel extends AbstractTableModel {

    //THE STATISTICAL VALUES WERE TRACKING FOR EACH MODE FOR EACH REGION FOR EACH USER.
    private GameStats statsSubRegionMode, statsFlagMode, statsCapitalMode, statsLeaderMode;
    //A LIST OF GAMESTATS OBJECTS , ONE FOR EACH MODE
    private ArrayList<GameStats> modes;
    private String rowNames[] = {NAME_MODE, CAPITAL_MODE, LEADER_MODE,FLAG_MODE};
    private String colNames[] = {FASTEST_TIME, GAMES_PLAYED, HIGH_SCORE};
    public static final int FASTEST_TIME_INDEX = 0, GAMES_PLAYED_INDEX = 1, HIGHEST_SCORE_INDEX = 2;

    /**
     * The table model for the table we will use to hold the data for game play
     * results for a given user and region.
     *
     * @param data The list containing the statistical values for the region and
     * user this table will hold data for. We will consider the data in the list
     * sequentially to fill the table, that is we will fill each column in a
     * given row with sequential values in the list until we fill the last cell
     * in that row and then well move to the next row if it exists and continue
     * sequentially through the list.
     */
    public AbstractModeStatsTableModel() {
        //INIT THE LIST OF MODES
        modes = new ArrayList<>();

        //INIT THE GAME STATS FOR EACH MODE
        statsSubRegionMode = new GameStats(1000000, INIT_STAT_VALUE, INIT_STAT_VALUE);
        statsFlagMode = new GameStats(1000000, INIT_STAT_VALUE, INIT_STAT_VALUE);
        statsCapitalMode = new GameStats(1000000, INIT_STAT_VALUE, INIT_STAT_VALUE);
        statsLeaderMode = new GameStats(1000000, INIT_STAT_VALUE, INIT_STAT_VALUE);

        //NOW ADD THOSE GAME STATS TO EACH MODE
        modes.add(statsSubRegionMode);
        modes.add(statsFlagMode);
        modes.add(statsCapitalMode);
        modes.add(statsLeaderMode);

    }

    /**
     *
     * @return
     */
    public GameStats getStatsSubRegionMode() {
        return statsSubRegionMode;
    }

    /**
     *
     * @param statsSubRegionMode
     */
    public void setStatsSubRegionMode(GameStats statsSubRegionMode) {
        this.statsSubRegionMode = statsSubRegionMode;
    }

    /**
     *
     * @return
     */
    public GameStats getStatsFlagMode() {
        return statsFlagMode;
    }

    /**
     *
     * @param statsFlagMode
     */
    public void setStatsFlagMode(GameStats statsFlagMode) {
        this.statsFlagMode = statsFlagMode;
    }

    /**
     *
     * @return
     */
    public GameStats getStatsCapitalMode() {
        return statsCapitalMode;
    }

    /**
     *
     * @param statsCapitalMode
     */
    public void setStatsCapitalMode(GameStats statsCapitalMode) {
        this.statsCapitalMode = statsCapitalMode;
    }

    /**
     *
     * @return
     */
    public GameStats getStatsLeaderMode() {
        return statsLeaderMode;
    }

    /**
     *
     * @param statsLeaderMode
     */
    public void setStatsLeaderMode(GameStats statsLeaderMode) {
        this.statsLeaderMode = statsLeaderMode;
    }

    /**
     *
     * @return
     */
    public ArrayList<GameStats> getModes() {
        return modes;
    }

    /**
     *
     * @param modes
     */
    public void setModes(ArrayList<GameStats> modes) {
        this.modes = modes;
    }

    /**
     *
     * @return
     */
    @Override
    public int getRowCount() {
        return modes.size();
    }

    /**
     *
     * @return
     */
    @Override
    public int getColumnCount() {
        return colNames.length;
    }

    /**
     *
     * @param rowIndex The row to look in for the desired statistic which
     * corresponds to different game modes
     * @param columnIndex The column to look in for the desired statistic which
     * corresponds to a different statistical category.
     * @return The value in the cell specified by the rowIndex and colIndex
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        GameStats mode = modes.get(rowIndex);
        switch (columnIndex) {
            case FASTEST_TIME_INDEX:
                return mode.getFastestTime();
            case HIGHEST_SCORE_INDEX:
                return mode.getHighestScore();
            case GAMES_PLAYED_INDEX:
                return mode.getTimesPlayed();
        }
        //IN CASE OF A MISTAKE RETURN NULL SO WE CAN EASILY IDENTIFY THE ERROR
        return null;
    }

    /**
     *
     * @param value
     * @param row
     * @param col
     */
    @Override
    public void setValueAt(Object value, int row, int col) {
        //GET THE GAMEMODE WHICH CORRESPONDS TO THE ROW
        GameStats statsForMode = (GameStats) modes.get(row);
        //SET THE STAT AT THE CORRESPONDING COLUMN
        switch (col) {
            case (FASTEST_TIME_INDEX):
                statsForMode.setFastestTime((int) value);
                break;
            case (GAMES_PLAYED_INDEX):
                statsForMode.setTimesPlayed((int) value);
                break;
            case (HIGHEST_SCORE_INDEX):
                statsForMode.setHighestScore((int) value);
                break;
        }
    }
}
