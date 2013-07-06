package regiondata;

//LIBRARIES TO USE
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.imageio.ImageIO;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import static regiondata.RegionIOSettings.*;
import xml_utilities.InvalidXMLFileFormatException;
import xml_utilities.XMLUtilities;

/**
 * This class serves as a plug-in for reading region data from XML files using
 * the RegionData.XSD schema. As currently provided this class does not cover
 * output to XML files because the target application of this plug in does not
 * require that behavior.
 *
 * @author James C. Porcelli, SBU ID # 108900819
 */
public class RegionIO implements RegionImporter {

    // THIS WILL HELP US PARSE THE XML FILES
    private XMLUtilities xmlUtil;
    // THIS IS THE SCHEMA WE'LL USE
    private File regionDataSchema;

    /**
     * Constructor for making our data importer. Note that it initializes the
     * XML utility for processing XML files and it sets up the schema for use.
     */
    public RegionIO(File regionDataSchema) {
        xmlUtil = new XMLUtilities();

        // WE'LL USE THE SCHEMA FILE TO VALIDATE THE XML FILES
        this.regionDataSchema = regionDataSchema;
    }

    public XMLUtilities getXmlUtil() {
        return xmlUtil;
    }
    

    /**
     * Loads the region data from the specified XML region data file into the
     * regions manager.
     *
     * @param regionsFile The XML file to load the data from.
     *
     * @param regionsManager The manager which we will load the requested region
     * information from the XML file into.
     *
     * @return Returns true if the region data loads successfully, false
     * otherwise.
     */
    @Override
    public boolean loadRegion(File regionDataFile, RegionDataManager regionsManager)
            throws InvalidXMLFileFormatException, IOException {

        //FIRST VERIFY THE XML FILE TO LOAD AGAISNT THE SCHEMA OUR APPLICATION IS USING
        //NOTE - THIS VALIDATION PROCESS INCLUDES INSTANCES WHERE THE FILES DON'T EVEN EXIST
        //ETC. WHICH WE JUST ORGRANIZE UNDER THE UMBRELLA OF AN INVALID XML DOC. SEE XML UTILITIES
        //CLASS FOR THE SPECIFICS.
        if (!(xmlUtil.validateXMLDoc(regionDataFile.getAbsolutePath(), regionDataSchema.getAbsolutePath()))) {
            //THROW AN INVALID XML FILE FORMAT EXCEPTION FOR THE XML FILE FOR THE REQUESTED REGION
            throw new InvalidXMLFileFormatException(regionDataFile.getAbsolutePath());
        }
        //IF WE MAKE IT HEAR THE DATA FILE EXISTS AND VALIDATES AGAISNT THE SCHEMA WERE USING

        // FIRST LOAD ALL THE XML DATA INTO A DOM
        Document doc = xmlUtil.loadXMLDocument(regionDataFile.getAbsolutePath(),
                regionDataSchema.getAbsolutePath());

        //THIS LOAD REGION METHOD COMPRISES LOADING OF ALL REQUIRED DATA, THAT IS
        //THE SUB REGION RGB VALUES, NAMES, AND THE REGION MAP
        //
        //LOAD ALL OF THE DATA FROM THE XML DATA FILE INTO THE REGION DATA MANAGER
        loadRegionData(doc, regionsManager);
        //LOAD THE REGION MAP FOR THE CURRENT REGION
        loadRegionMap(regionDataFile.getParent(), regionsManager);

        // XML FILE LOADED PROPERLY
        return true;
    }

    /**
     * Private helper method for loading region data. This method loads the
     * complete list of regions for the specified file which would be the parent
     * region, and then makes the appropriate call to the other helper methods
     * to load the other data associated with the current region, i.e flag
     * images, leader images, etc.
     *
     * @param doc The hierarchical region data loaded from an XML file into a
     * DOM, we'll extract the data from this doc and load it into the regions
     * manager.
     *
     * @param manager The data manager for all region and sub region data. We'll
     * load the data in the doc into this manager.
     */
    private void loadRegionData(Document doc, RegionDataManager manager) {
       // Region parentOfNewParent = manager.getCurrentRegion();
        
        // EMPTY THE SUB REGION LISTS
        manager.clearRegions();
        //
        //UPON EMPTYING OF THE REGIONS LIST ALL REFERENCES TO THE REGIONS 
        //CONTAINED THEREIN WILL BE DESTROYED AND SO THE JVM WILL FREE THE
        //MEMORY ASSOCIATED WITH THOSE REGIONS THAT WERE CONTAINED INSIDE
        //EFFECTIVLY CLEARING THE ACTIVE REGION OBJECTS NO LONGER IN USE

        // FIRST GET THE PARENT REGION
        Node parentRegion = doc.getElementsByTagName(REGION_NODE).item(0);
        // AND THEN THE PARENT REGIONS ATTRIBUTES
        NamedNodeMap parentRegionAttributes = parentRegion.getAttributes();

        //GET THE PARENT REGION CAPITAL, IF IT EXISTS
        Node capitalNode = parentRegionAttributes.getNamedItem(CAPITAL_ATTRIBUTE);
        //THEN THE PARENT REGION LEADER, IF IT EXISTS
        Node leaderNode = parentRegionAttributes.getNamedItem(LEADER_ATTRIBUTE);
        //AND THEN THE NAME, ALL PARENT REGIOSN HAVE NAMES
        Node nameNode = parentRegionAttributes.getNamedItem(NAME_ATTRIBUTE);

        //PARENT REGION REGION OBJECT
        Region currentRegion;
        //IF PARENT REGION HAS NO CAPITAL AND NO LEADER THEN WE ARE LOADING THE WORLD OR A CONTINENT
        //
        //WORLD DOES NOT HAVE A GREY SCALE VALUE IN ANY CONTEXT, AND CONTINENT DOESNT IN THIS CONTEXT
        if (capitalNode == null && leaderNode == null) {
            //REGION IS THE WORLD OR CONTINENT
            //
            //CHECK WHICH BEFORE SETTING THE TYPE
            if (nameNode.getNodeValue().equals(THE_WORLD)) {
                //THIS MEANS WE ARE PLAYING THE CONTINENETS GAME
                currentRegion = new Region(nameNode.getNodeValue().toUpperCase(), nameNode.getNodeValue(), RegionType.WORLD);
            } else {
                //MUST BE A CONTINENT
                //
                //THIS MEANS WE ARE PLAYING A CONTINENET
                currentRegion = new Region(nameNode.getNodeValue().toUpperCase(), nameNode.getNodeValue(), RegionType.CONTINENT);
            }
        } else {
            //REGION IS A NATION
            //
            //PARENT REGIONS DONT HAVE A GREY SCALE VALUE IN THIS CONTEXT
            //THIS MEANS WE ARE PLAYING THE COUNTRY GAME
            currentRegion = new Region(nameNode.getNodeValue().toUpperCase(), nameNode.getNodeValue(),
                    RegionType.NATION, capitalNode.getNodeValue(), leaderNode.getNodeValue(), null);
        }

       // currentRegion.setParent(parentOfNewParent);
        
        //SET THIS REGION AS THE CURRENT REGION IN THE MANAGER
        manager.setCurrentRegion(currentRegion);

        // AND THEN GO THROUGH AND ADD ALL THE LISTED SUB REGIONS OF THE PARENT
        ArrayList<Node> regionsList = xmlUtil.getChildNodesWithName(parentRegion, SUB_REGION_NODE);
        for (int i = 0; i < regionsList.size(); i++) {
            // GET THEIR DATA FROM THE DOM
            //
            //WE WILL ITERATIVLY PROCESS EACH SUB REGION ELEMENT NODE
            Node regionNode = regionsList.get(i);

            //GET THE CURRENT NODES ATTRIBUTES
            NamedNodeMap regionAttributes = regionNode.getAttributes();

            //REQUIRED ATTRIBUTES
            //
            //SUB REGION NAME
            String name = regionAttributes.getNamedItem(NAME_ATTRIBUTE).getNodeValue();
            //SUB REGION BLUE VALUE
            String blue = regionAttributes.getNamedItem(BLUE_ATTRIBUTE).getNodeValue();
            //SUB REGION GREEN VALUE
            String green = regionAttributes.getNamedItem(GREEN_ATTRIBUTE).getNodeValue();
            //SUB REGION RED VALUE
            String red = regionAttributes.getNamedItem(RED_ATTRIBUTE).getNodeValue();
            //THE TYPE OF THIS SUB REGION
            RegionType regionType;
            //REGION OBJECT FOR THIS SUB REGION NODE
            Region regionToAdd;

            //THIS REGIONS GREY SCALE COLOR ON THE MAP
            Color greyScale = new Color(new Integer(blue), new Integer(green), new Integer(red));

            //OPTIONAL ATTRIBUTES (DEPENDS ON THE REGION TYPE)
            //
            //SUB REGION CAPITAL
            capitalNode = regionAttributes.getNamedItem(CAPITAL_ATTRIBUTE);

            //SUB REGION LEADER
            if (currentRegion.getType() != RegionType.NATION) {
                leaderNode = regionAttributes.getNamedItem(LEADER_ATTRIBUTE);
            }

            //PROCESS THE SUB REGION NODE ACCORDING TO WHAT DATA WAS FOUND 
            if (capitalNode != null && leaderNode != null) {
                // ALL NATIONS AND STATES HAVE CAPITALS
                regionType = RegionType.STATE;
                //
                //NOTE THAT THE LEADER ATTRIBUTE IS ALWAYS SET FOR THE PARENT REGION
                //BUT ONLY SOMETIMES SET FOR THE SUB REGIONS. EVEN THOUGH LEADER
                //MODE MAY NOT BE A VALID GAME MODE FOR ALL REGIONS WE CAN STILL SET
                //EACH SUB REGIONS LEADER ATTRIBUTE USING EIGHTER THE LOCAL VALUE OR
                //THE GLOBAL VALUE. THIS ATTRIBUTE IS THEN AVAILABLE IN THE CONTEXT 
                //OF REGION OBJECTS BUT MAY OR MAY NOT BE AVAILABLE IN TERMS OF THE
                //LEADER GAME PLAY MODE FOR RAMBLEON
                regionToAdd = new Region(name.toUpperCase(), name, regionType,
                        capitalNode.getNodeValue(), leaderNode.getNodeValue(), greyScale);

            } else if (capitalNode != null && leaderNode == null) {
                //
                regionType = RegionType.NATION;
                regionToAdd = new Region(name.toUpperCase(), name, regionType,
                        capitalNode.getNodeValue(), null, greyScale);

            } else if (capitalNode == null && leaderNode != null) {
                //
                regionType = RegionType.NATION;
                regionToAdd = new Region(name.toUpperCase(), name, regionType,
                        null, leaderNode.getNodeValue(), greyScale);
            } else {
                // CONTINENTS DO NOT
                regionType = RegionType.CONTINENT;
                regionToAdd = new Region(name.toUpperCase(), name, regionType, null, null, greyScale);
            }

            //SET THE PARENT REGION OF EACH SUB REGION WE ADD 
            //regionToAdd.setParent(currentRegion);
            //PUT THE REGION IN THE MANAGER
            manager.addRegion(regionToAdd);
            //AND ADD IT TO THE CURRENT REGIONS SUB REGIONS LIST
            currentRegion.addSubRegion(regionToAdd);

        }
    }

    /**
     * Load the map image for the current region. This map lays out all the sub
     * regions contained within the current region.
     *
     * @param parentRegion The current region for which we want the map.
     * @param manager The region data manager where we will store the map image.
     */
    private void loadRegionMap(String parentFilePath, RegionDataManager manager)
            throws IOException {
        //THE MAP IMAGE
        BufferedImage currentRegionMap;
        String currentRegion = manager.getCurrentRegion().getName();

        //LOAD THE CURRENT PARENT REGIONS MAP
        //
        //FILE:../The World/The World/parentFilePath/currentRegionName Map.png
        currentRegionMap = ImageIO.read(new File(parentFilePath + "/" + currentRegion + MAP_IMAGE));

        //IF WERE HERE NO ERRORS OCCURED
        //
        //SO ADD THE MAP IMAGE TO THE DATA MANAGER
        manager.setCurrentMap(currentRegionMap);
    }

    /**
     * This method is called in an attempt to load leader images/information for
     * a particular region. If leader information is available for all the sub
     * regions of the current region then those leader images are loaded into
     * the data structure in the region data manager for storing leader images.
     *
     * @param currentRegionDIRpath The path to the current region directory in
     * the application file structure .
     *
     * @param dataManager The manager for all the region/sub region information.
     * Well load all the leader images if available into a corresponding data
     * structure in this manager.
     *
     * @throws IOException When leader information is not available for the
     * current region. Note - That IOException is the base class of
     * FileNotFoundException which is more likely to be thrown. So using
     * IOException is a safe choice in anticipating all possible errors
     */
    @Override
    public void loadRegionLeaderImages(String currentRegionDIRpath, RegionDataManager dataManager)
            throws IOException {
        //THE CURRENT REGION WHICH WELL HAVE TO HAVE BEEN SELECTED BEFORE ANY SUB REGION
        //LEADER IMAGES CAN BE REQUESTED
        //
        //SO WELL USE THE PARENT REGION TO GET A LIST OF ALL THE SUBREGION NAMES FOR
        //WHICH WE NEED LEADER IMAGES
        Region currentRegion = dataManager.getCurrentRegion();

        //GET AN ITERATOR TO ALL THE SUB REGIONS OF THE CURRENT REGION
        Iterator<Region> subRegionIterator = currentRegion.getSubRegions();

        //ITERATE THROUGH ALL THE SUB REGIONS
        while (subRegionIterator.hasNext()) {
            //THE NAME OF THE CURRENT SUB REGION 
            String subRegionName = subRegionIterator.next().getName();
            //SUB REGION LEADER IMAGE
            BufferedImage leaderImage;

            //LOAD THE CURRENT SUB REGIONS LEADER IMAGE
            //
            //FILE : pathToCurrentRegionDIR/subRegionName Leader.jpg
            leaderImage = ImageIO.read(new File(currentRegionDIRpath + "/" + subRegionName + LEADER_IMAGE));

            //ADD THE LEADER IMAGE FOR THE CURRENT SUB REGION TO THE MANAGER
            dataManager.addLeaderImage(subRegionName, leaderImage);
        }
    }

    /**
     * This method is called in an attempt to load flag images/information for a
     * particular region. If flag information is available for all the sub
     * regions of the current region then those flag images are loaded into the
     * data structure in the region data manager for storing flag images.
     *
     * @param currentRegionDIRpath The path to the current region directory in
     * the application file structure.
     *
     * @param dataManager The manager for all the region/sub region information.
     * Well load all the flag images if available into a corresponding data
     * structure in this manager.
     *
     * @throws IOException When flag information is not available for the
     * current region. Note - That IOException is the base class of
     * FileNotFoundException which is more likely to be thrown. So using
     * IOException is a safe choice in anticipating all possible errors.
     */
    @Override
    public void loadRegionFlagImages(String currentRegionDIRpath, RegionDataManager dataManager)
            throws IOException {

        //THE CURRENT REGION WHICH WELL HAVE TO HAVE BEEN SELECTED BEFORE ANY SUB REGION
        //FLAG IMAGES CAN BE REQUESTED
        //
        //SO WELL USE THE PARENT REGION TO GET A LIST OF ALL THE SUBREGION NAMES FOR
        //WHICH WE NEED FLAG IMAGES
        Region currentRegion = dataManager.getCurrentRegion();

        //GET AN ITERATOR TO ALL THE SUB REGIONS OF THE CURRENT REGION
        Iterator<Region> subRegionIterator = currentRegion.getSubRegions();

        //ITERATE THROUGH ALL THE SUB REGIONS
        while (subRegionIterator.hasNext()) {
            //THE NAME OF THE CURRENT SUB REGION 
            String subRegionName = subRegionIterator.next().getName();
            //SUB REGION flag IMAGE
            BufferedImage flagImage;

            //LOAD THE CURRENT SUB REGIONS FLAG IMAGE
            //
            //FILE : pathToCurrentRegionDIR/subRegionName Flag.jpg
            flagImage = ImageIO.read(new File(currentRegionDIRpath + "/" + subRegionName + FLAG_IMAGE));

            //ADD THE FLAG IMAGE FOR THE CURRENT SUB REGION TO THE MANAGER
            dataManager.addFlagImage(subRegionName, flagImage);
        }
    }
}
