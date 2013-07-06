package regiondata;

//LIBRARIES TO USE
import java.io.File;
import java.io.IOException;
import xml_utilities.InvalidXMLFileFormatException;

/**
 * This interface provides the requirements for an importer plug-in to be used
 * for loading region data from an XML file. Note that this interface, does not
 * have a particular XML file structure/schema. So various formats could be
 * used, which would be up to the plug-in developer.
 *
 * @author James C. Porcelli, SBU ID # 108900819
 */
public interface RegionImporter {

    /**
     * Attempts to load all the sub region data for the current region specified
     * by the prefix of the XML data file passed as the regionDataFile.
     *
     * @param regionDataFile The XML file from which the data will be extracted.
     * This XML file is simply the desired region to load which corresponds
     * exactly to a directory in the game files with a " Data.XML" suffix
     * appended to it. The decision has been made to pass the responsibility off
     * to the caller to append the required suffix to the filename before
     * calling this method.
     *
     * @param dataManager The data read from the file will be loaded into this
     * region data manager which will store and make available to the programmer
     * all the information contained therein.
     * 
     * @throws IOException An IOException is thrown if an error occurs during 
     * the loading of the region data from the XML file or during the process of
     * loading the region map image.
     * 
     * @throws InvalidXMLFileFormatException An InvalidXMLFileFormatException is 
     * thrown if the XML file for the specified region is poorly formatted, doesn't
     * exist altogether or causes any other sort of IOException during the initial
     * validation and loading of the XML file from the specified DIR.
     *
     * @return Returns true if the world loaded successfully, false otherwise.
     */
    public boolean loadRegion(File regionDataFile, RegionDataManager dataManager)
            throws InvalidXMLFileFormatException, IOException;

    /**
     * Attempts to load all the sub region leader images for the current region
     * specified by the current regions name which corresponds exactly to the
     * DIR which should contain if available all the sub region leader images
     * for the current region. Note - The current region will have had to have
     * been selected in order for any sub region leader images to be requested
     * so we can just obtain the current region information from the manager and
     * derive the DIR name from there.
     *
     * @param currentRegionDIRpath The path to the current region directory in
     * the application file structure.
     *
     * @param dataManager The data read from the file will be loaded into this
     * region data manager which will store and make available to the programmer
     * all the information contained therein.
     *
     * @throws IOException If the region for which leader images were requested
     * does not contain any such images/information.
     */
    public void loadRegionLeaderImages(String currentRegionDIRpath, RegionDataManager dataManager)
            throws IOException;

    /**
     * Attempts to load all the sub region flag images for the current region
     * specified by the current regions name which corresponds exactly to the
     * DIR which should contain if available all the sub region flag images for
     * the current region. Note - The current region will have had to have been
     * selected in order for any sub region flag images to be requested so we
     * can just obtain the current region information from the manager and
     * derive the DIR name from there.
     *
     * @param currentRegionDIRpath The path to the current region directory in
     * the application file structure.
     *
     * @param dataManager The data read from the file will be loaded into this
     * region data manager which will store and make available to the programmer
     * all the information contained therein.
     *
     * @throws IOException If the region for which flag images were requested
     * does not contain any such images/information.
     */
    public void loadRegionFlagImages(String currentRegionDIRpath, RegionDataManager dataManager)
            throws IOException;
}
