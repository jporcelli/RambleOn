package regiondata;

//LIBRARIES TO USE 
import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

/**
 * This class represents a named region for a geographic application where all
 * regions are hierarchically related. A region should have an id, name, and
 * type, and may optionally have a capital, and/or leader. All regions have a
 * unique grey scale color corresponding to a map of the parent region in which
 * a region is contained.
 *
 * @author James C. Porcelli, SBU ID 108900819
 * @author Richard McKenna, Prof Computer Science SBU
 * @version 1.1
 */
public class Region<T extends Comparable<T>> implements Comparable<Region<T>> {

    //UNIQUE IDENTIFIER FOR THIS REGION
    private String id;
    // NAME OF REGION
    private String name;
    // CAPTIAL OF THIS REGION. NOTE THAT SOME REGIONS DO NOT HAVE A CAPITAL
    private String capital;
    //LEADER OF THIS REGIONS
    private String leader;
    //TYPE OF REGION
    private RegionType type;
    //THE PARENT REGION, WITHIN WHICH THIS REGION IS CONTAINED
    private String parentRegion;
    //GREY SCALE VALUE OF THIS REGION
    private Color greyScale;
    //LIST OF CHILD REGIONS FOR THIS REGION. FOR EXAMPLE, A NATION
    //WOULD LIST STATES HERE
    private ArrayList<Region> subRegions;
    //FLAG INDICATING IF THIS REGION IS PLAYABLE IN THE CURRENT CONTEXT
    private boolean playable;

    /**
     * Constructor that initializes the three required fields for any region:
     * its id, name, and type
     *
     * @param initId The unique identifier for this regions. No two regions may
     * have the same id.
     *
     * @param initName The name of this region, which would typically be used
     * for display purposes.
     *
     * @param initType The type of this region
     */
    public Region(String initId, String initName, RegionType initType) {
        // INIT THE PROVIDED FIELDS
        id = initId;
        name = initName;
        type = initType;

        // NULL THE MISSING FIELDS
        parentRegion = null;
        capital = null;
        leader = null;

        // AND SETUP THE LIST SO WE CAN ADD CHILD REGIONS
        subRegions = new ArrayList();        
    }

    /**
     * Constructor that initializes the three required fields for any region:
     * its id, name, and type
     *
     * @param initId The unique identifier for this regions. No two regions may
     * have the same id.
     *
     * @param initName The name of this region, which would typically be used
     * for display purposes.
     *
     * @param initType The type of this region.
     *
     * @param initCapital The capital of this region.
     */
    public Region(String initId, String initName, RegionType initType,
            String initCapital, String initLeader, Color initGreyScale) {
        // LET THE OTHER CONSTRUCTOR DO MOST OF THE SETUP WORK
        this(initId, initName, initType);

        // WE'LL KEEP THE CAPITAL
        capital = initCapital;
        // AND THE LEADER
        leader = initLeader;
        // AND SET THE GREYSCALE VALUE FOR THIS REGION
        greyScale = initGreyScale;
    }

    //ACCESSOR METHODS
    //----------------
    /**
     * Access method for getting this region's leader.
     *
     * @return The leader of this region.
     */
    public String getLeader() {
        return leader;
    }
    
    /**
     * 
     * @return 
     */
    public boolean isPlayable() {
        return playable;
    }
    
    /**
     * Access method for getting this region's grey scale value.
     *
     * @return The grey scale value of this region.
     */
    public Color getGreyScale() {
        return greyScale;
    }

    /**
     * Access method for getting this region's id.
     *
     * @return The id of this region.
     */
    public String getId() {
        return id;
    }

    /**
     * Access method for getting this region's name.
     *
     * @return The name of this region.
     */
    public String getName() {
        return name;
    }

    /**
     * Access method for getting this region's type.
     *
     * @return The type of this region.
     */
    public RegionType getType() {
        return type;
    }

    /**
     * Access method for getting this region's parent region.
     *
     * @return The parent region of this region.
     */
    public String getParentRegion() {
        return parentRegion;
    }

    /**
     * Access method for getting this region's capital.
     *
     * @return The name of the capital of this region.
     */
    public String getCapital() {
        return capital;
    }

    /**
     * Access method for getting all of this regions subregions in the form of
     * an Iterator.
     *
     * @return An Iterator that can traverse sequentially through all of the
     * child regions of this region.
     */
    public Iterator<Region> getSubRegions() {
        return subRegions.iterator();
    }

    /**
     * Access method for getting the child Region of this region that has an id
     * the same as the subRegionId argument.
     *
     * @param subRegionId The region id of the subregion we're looking for.
     *
     * @return The region with the same id as the subRegionId argument.
     */
    public Region getSubRegion(String subRegionId) {
        //GO THROUGH ALL THE CHILD REGIONS OF THIS REGION
        Iterator it = subRegions.iterator();
        while (it.hasNext()) {
            Region subRegion = (Region) it.next();

            //IF A CHILD REGION HAS THE ID GIVEN BY THE SUB REGION ID ARGUMENT
            if (subRegion.id.equals(subRegionId)) {
                //THEN RETURN THAT REGION
                return subRegion;
            }
        }
        //OTHERWISE RETURN NULL
        return null;
    }

    /**
     * This method tests to see if this region has any sub regions.
     *
     * @return Returns true if this region has children, false otherwise.
     */
    public boolean hasSubRegions() {
        return !subRegions.isEmpty();
    }

    /**
     * This method tests to see if this region has a capital.
     *
     * @return Returns true if this region has a capital, false otherwise.
     */
    public boolean hasCapital() {
        return capital != null;
    }

    // MUTATOR METHODS
    //----------------
    /**
     * Mutator method for changing this region's unique identifier.
     *
     * @param initId Unique id to be used for this region.
     */
    public void setId(String initId) {
        id = initId;
    }
    
    /**
     * 
     * @param playable 
     */
    public void setPlayable(boolean playable) {
        this.playable = playable;
    }
    
    /**
     * Mutator method for changing or setting this regions grey scale value.
     *
     * @param greyScale The color to change this regions grey scale value to.
     */
    public void setGreyScale(Color greyScale) {
        this.greyScale = greyScale;
    }

    /**
     * Mutator method for changing or setting this regions leader.
     *
     * @param leader The name of the leader to set as this regions leader.
     */
    public void setLeader(String leader) {
        this.leader = leader;
    }

    /**
     * Mutator method for changing this regions name.
     *
     * @param initName The name to set as the name of this region.
     */
    public void setName(String initName) {
        name = initName;
    }

    /**
     * Mutator method for setting or changing this regions capital.
     *
     * @param initCapital The capital to set as this regions capital.
     */
    public void setCapital(String initCapital) {
        capital = initCapital;
    }

    /**
     * Mutator method for setting the type of this region.
     *
     * @param initType The region type to be used for this region.
     */
    public void setType(RegionType initType) {
        type = initType;
    }

    /**
     * Mutator method for setting the parent region of this region.
     *
     * @param parentRegion The region to set as this regions parent region.
     */
    public void setParent(String parentRegion) {
        //SET THIS REGIONS PARENT REGION
        this.parentRegion = parentRegion;
    }

    // ADDITIONAL SERVICE METHODS
    //---------------------------
    /**
     * Adds another region to the list of subregions of this region.
     *
     * @param subRegionToAdd Region to be added as a sub region.
     */
    public void addSubRegion(Region subRegionToAdd) {
        //ADD THE REGION TO THIS REGIONS LIST OF SUB REGIONS
        subRegions.add(subRegionToAdd);

        //KEEP THE LIST OF SUBREGIONS SORTED BY NAME
        Collections.sort(subRegions);
    }

    /*
     * Removes the region passed as an argument from this regions list
     * of subregions.
     */
    public void removeSubRegion(Region subRegionToRemove) {
        //REMOVE THE REGION FROM THE LIST OF SUB REGIONS FOR THIS REGION
        subRegions.remove(subRegionToRemove);
    }

    /**
     * Used for comparing Regions for the purpose of sorting them.
     *
     * @param region The Region to be compared to this one.
     *
     * @return 0 if they have the same name, -1 if this Region's name
     * alphabetically precedes it, and 1 if it follows it.
     */
    @Override
    public int compareTo(Region<T> region) {
        return name.compareTo(region.name);
    }

    /**
     * Method for testing equivalence of this region with the regionAsObject
     * argument.
     *
     * @param regionAsObject The region to test for equivalence with this one.
     *
     * @return true if they have the same id, false otherwise.
     */
    @Override
    public boolean equals(Object regionAsObject) {
        //TEST - CHANGED COMPARATOR TO NAME FROM ID
        if (regionAsObject instanceof Region) {
            Region region = (Region) regionAsObject;
            return id.equals(region.id);
        }
        return false;
    }

    /**
     * The hash code for this region. Hash codes are used when storing Region
     * objects in a hash table.
     *
     * @return A unique integer corresponding to the hash code generated for
     * this region.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.name);
        return hash;
    }

    /**
     * Generates a textual representation of this region.
     *
     * @return The textual representation of this region, which is simply the
     * name.
     */
    @Override
    public String toString() {
        return name;
    }
}
