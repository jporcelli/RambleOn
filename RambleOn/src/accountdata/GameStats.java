package accountdata;

import java.io.Serializable;

/**
 * This class encapsulates all the game statistics that we are tracking for a given
 * game mode.
 * 
 * @author James C. Porcelli
 */
public class GameStats implements Serializable{
    
    //THE FASTEST TIME TO COMPLETITION STAT
    private int fastestTime;
    //THE HIGHEST SCORE STAT
    private int highestScore;
    //THE NUMBER OF TIMES PLAYED STAT
    private int timesPlayed;
    
    /**
     * 
     * 
     * @param fastestTime
     * @param highestScore
     * @param timesPlayed 
     */
    public GameStats(int fastestTime, int highestScore, int timesPlayed){
        this.fastestTime = fastestTime;
        this.highestScore = highestScore;
        this.timesPlayed = timesPlayed;
    }
    
    /**
     * 
     * @return 
     */
    public int getFastestTime() {
        return fastestTime;
    }
    
    /**
     * 
     * @param fastestTime 
     */
    public void setFastestTime(int fastestTime) {
        this.fastestTime = fastestTime;
    }
    
    /**
     * 
     * @return 
     */
    public int getHighestScore() {
        return highestScore;
    }
    
    /**
     * 
     * @param highestScore 
     */
    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }
    
    /**
     * 
     * @return 
     */
    public int getTimesPlayed() {
        return timesPlayed;
    }
    
    /**
     * 
     * @param timesPlayed 
     */
    public void setTimesPlayed(int timesPlayed) {
        this.timesPlayed = timesPlayed;
    }
    
    
    
}
