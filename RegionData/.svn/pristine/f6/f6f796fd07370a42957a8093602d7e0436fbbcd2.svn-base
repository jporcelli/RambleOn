package regiondata;

import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Region data management class for accessing and storing the regions, and
 * associated data currently in use by the application.
 *
 * @author James C. Porcelli, SBU ID # 108900819
 */
public class RegionDataManager {

    //THE CURRENTLY SELECTED REGION
    //
    //THIS CAN BE CONSIDERED AS THE ROOT NODE
    //OF A TREE REPRESENTATION OF THE CURRENTLY
    //SELECTED REGION AND ALL ITS SUB REGIONS
    private Region currentRegion;
    //THE MAP CORRESPONDING TO THE PARENT REGION
    //
    //THE MAP WOULD LAYOUT ALL THE PARENT REGIONS SUB REGIONS FOR USE IN THE GAME
    private BufferedImage currentMap;
    //THIS IS WHERE WE'LL STORE ONE OF EACH REGION
    //
    //THIS CAN BE CONSIDERED AS ALL CHILDREN OF THE ROOT
    //NODE
    private TreeMap<String, Region> subRegions;
    //THE FLAG IMAGES FOR ALL OF THE SUB REGIONS OF THE CURRENT REGION
    private TreeMap<String, BufferedImage> subRegionFlags;
    //THE LEADER IMAGES FOR ALL OF THE SUB REGIONS OF THE CURRENT REGION
    private TreeMap<String, BufferedImage> subRegionLeaders;
    //THE CAPITALS OF THE SUB REGIONS OF THE CURRENT REGION
    private TreeMap<String, String> subRegionCapitals;

    /**
     * Create the regions manager and allocate the data structures needed to
     * store all the data for the current region.
     */
    public RegionDataManager() {
        //INIT THE TREE FOR SUB REGIONS
        this.subRegions = new TreeMap<>();
        //INIT THE TREE FOR SUB REGION FLAGS
        this.subRegionFlags = new TreeMap<>();
        //INIT THE TREE FOR SUB REGION LEADERS
        this.subRegionLeaders = new TreeMap<>();
        //INIT THE TREE FOR SUB REGION CAPITALS
        this.subRegionCapitals = new TreeMap<>();

    }

    //MANAGER UTILITIE METHODS
    //------------------------
    /**
     * Removes all of the regions currently in the manager.
     */
    public void clearRegions() {
        subRegions.clear();
        subRegionFlags.clear();
        subRegionLeaders.clear();
        subRegionCapitals.clear();
    }

    /**
     *
     * @return
     */
    public boolean capitalsAvailable() {
        Iterator<String> currentRegions = this.getAllRegionsNamesIterator();
        while (currentRegions.hasNext()) {
            Region curSub = subRegions.get(currentRegions.next());
            if (curSub.getCapital() == null) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 
     */
    public void initCapitalsMap() {
        if (capitalsAvailable()) {
            Iterator<String> currentRegions = this.getAllRegionsNamesIterator();
            while (currentRegions.hasNext()) {
                Region curSub = subRegions.get(currentRegions.next());
                subRegionCapitals.put(curSub.getName(), curSub.getCapital());
            }
        }
    }

    /**
     * Adds a region to the tree of regions under the current region.
     *
     * @param regionToAdd The region to add to the tree containing all
     * subregions of the current region.
     */
    public void addRegion(Region regionToAdd) {
        subRegions.put(regionToAdd.getName(), regionToAdd);
    }

    /**
     * Adds a sub region leader image to the tree of sub region leader images.
     *
     * @param subRegionName The name of the sub region for which we are adding a
     * leader image
     *
     * @param subRegionLeader The sub region leader image we are adding to the
     * leader images data structure
     */
    public void addLeaderImage(String subRegionName, BufferedImage subRegionLeader) {
        subRegionLeaders.put(subRegionName, subRegionLeader);
    }

    /**
     * Adds a sub region flag image to the tree of sub region flag images.
     *
     * @param subRegionName The name of the sub region for which we are adding a
     * flag image
     *
     * @param subRegionFlag The sub region flag image we are adding to the flag
     * images data structure.
     */
    public void addFlagImage(String subRegionName, BufferedImage subRegionFlag) {
        subRegionFlags.put(subRegionName, subRegionFlag);
    }

    //MUTATORS
    //--------
    /**
     * Sets the current region attribute of the manager.
     *
     * @param currentRegion The region to set as the current region.
     */
    public void setCurrentRegion(Region currentRegion) {
        this.currentRegion = currentRegion;
    }

    /**
     * Sets the map for the current region.
     *
     * @param currentMap The map image to set as the map for the current region.
     */
    public void setCurrentMap(BufferedImage currentMap) {
        this.currentMap = currentMap;
    }

    //ACCESSORS
    //---------
    /**
     * Access method for obtaining an iterator to the list of the names of all
     * sub regions of the current region.
     *
     * @return A String iterator to the names of all of the sub regions of the
     * current region.
     */
    public Iterator<String> getAllRegionsNamesIterator() {
        return subRegions.keySet().iterator();
    }

    /**
     * Access method for getting the current region.
     *
     * @return Returns the current region.
     */
    public Region getCurrentRegion() {
        return currentRegion;
    }

    /**
     * Access method for getting the tree containing all sub regions of the
     * current region.
     *
     * @return The tree map containing all the sub regions of the current
     * region.
     */
    public TreeMap<String, Region> getAllRegions() {
        return subRegions;
    }

    /**
     * Access method for getting the current map being used by the application.
     *
     * @return The image of the map currently being used.
     */
    public BufferedImage getCurrentMap() {
        return currentMap;
    }

    /**
     * Access method for getting the tree containing all sub region flags for
     * the current region.
     *
     * @return The tree map containing all the sub region flags for the current
     * region.
     */
    public TreeMap<String, BufferedImage> getSubRegionFlags() {
        return subRegionFlags;
    }

    /**
     * Access method for getting the tree containing all sub region leaders for
     * the current region.
     *
     * @return The tree containing all the sub region leaders for the current
     * region.
     */
    public TreeMap<String, BufferedImage> getSubRegionLeaders() {
        return subRegionLeaders;
    }
    
    /**
     * 
     * @return 
     */
    public TreeMap<String, String> getSubRegionCapitals() {
        return subRegionCapitals;
    }   
    
}
