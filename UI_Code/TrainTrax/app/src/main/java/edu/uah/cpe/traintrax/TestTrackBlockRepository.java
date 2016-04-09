package edu.uah.cpe.traintrax;

/* import com.traintrax.navigation.database.rest.client.*;
import com.traintrax.navigation.database.rest.data.TrackBlockSearchResults; */

import com.traintrax.navigation.database.rest.client.*;
import com.traintrax.navigation.database.rest.data.*;

/**
 * Test implementation of the TrackBlock repository
 * 
 * @author Rashad Madyun
 * This is used to simplify the work necessary to test how well a
 *         client can integrate with the repository interfaces
 */
public class TestTrackBlockRepository extends RemoteTrackBlockRepository {

    private static final String testTrackBlockDataJsonString = "{\"matches\":[{\"blockId\":\"1\",\"blockName\":\"1-1\"},{\"blockId\":\"2\",\"blockName\":\"2-1\"},{\"blockId\":\"3\",\"blockName\":\"2-2\"},{\"blockId\":\"4\",\"blockName\":\"2-5\"},{\"blockId\":\"5\",\"blockName\":\"2-3\"},{\"blockId\":\"6\",\"blockName\":\"2-4\"},{\"blockId\":\"7\",\"blockName\":\"2-7\"},{\"blockId\":\"8\",\"blockName\":\"2-9\"},{\"blockId\":\"9\",\"blockName\":\"2-11\"},{\"blockId\":\"10\",\"blockName\":\"2-12\"},{\"blockId\":\"11\",\"blockName\":\"2-13\"},{\"blockId\":\"12\",\"blockName\":\"U-6;5-4\"},{\"blockId\":\"13\",\"blockName\":\"2-14\"},{\"blockId\":\"14\",\"blockName\":\"2-16\"},{\"blockId\":\"15\",\"blockName\":\"1-2\"},{\"blockId\":\"16\",\"blockName\":\"2-15\"},{\"blockId\":\"17\",\"blockName\":\"U-48;4-13\"},{\"blockId\":\"18\",\"blockName\":\"U-53;1-11\"},{\"blockId\":\"19\",\"blockName\":\"U-57;3-13\"},{\"blockId\":\"20\",\"blockName\":\"3-8\"},{\"blockId\":\"21\",\"blockName\":\"3-4\"},{\"blockId\":\"22\",\"blockName\":\"5-15\"},{\"blockId\":\"23\",\"blockName\":\"5-14\"},{\"blockId\":\"24\",\"blockName\":\"5-3\"},{\"blockId\":\"25\",\"blockName\":\"U-9;8-1\"},{\"blockId\":\"26\",\"blockName\":\"U-12;8-6\"},{\"blockId\":\"27\",\"blockName\":\"U-15;7-6\"},{\"blockId\":\"28\",\"blockName\":\"U-18;8-12\"},{\"blockId\":\"29\",\"blockName\":\"U-20;8-10\"},{\"blockId\":\"30\",\"blockName\":\"U-21;8-9\"},{\"blockId\":\"31\",\"blockName\":\"U-22;8-14\"},{\"blockId\":\"32\",\"blockName\":\"U-31;6-5\"},{\"blockId\":\"33\",\"blockName\":\"U-33;4-3\"},{\"blockId\":\"34\",\"blockName\":\"U-45;1-8\"},{\"blockId\":\"35\",\"blockName\":\"U-47;1-15\"},{\"blockId\":\"36\",\"blockName\":\"3-16\"},{\"blockId\":\"37\",\"blockName\":\"1-3\"},{\"blockId\":\"38\",\"blockName\":\"U-60\"},{\"blockId\":\"39\",\"blockName\":\"3-15;U-59\"},{\"blockId\":\"40\",\"blockName\":\"3-13;U-57\"},{\"blockId\":\"41\",\"blockName\":\"3-14;U-58\"},{\"blockId\":\"42\",\"blockName\":\"4-15;U-51\"},{\"blockId\":\"43\",\"blockName\":\"U-54;1-12\"},{\"blockId\":\"44\",\"blockName\":\"U-52;4-16\"},{\"blockId\":\"45\",\"blockName\":\"U-46;1-14\"},{\"blockId\":\"46\",\"blockName\":\"U-44;1-7\"},{\"blockId\":\"47\",\"blockName\":\"U-34;4-4\"},{\"blockId\":\"48\",\"blockName\":\"U-35;1-6\"},{\"blockId\":\"49\",\"blockName\":\"U-36;1-5\"},{\"blockId\":\"50\",\"blockName\":\"3-3\"},{\"blockId\":\"51\",\"blockName\":\"U-37;4-2\"},{\"blockId\":\"52\",\"blockName\":\"3-1;6-8\"},{\"blockId\":\"53\",\"blockName\":\"U-38;5-8\"},{\"blockId\":\"54\",\"blockName\":\"5-5\"},{\"blockId\":\"55\",\"blockName\":\"U-39;5-7\"},{\"blockId\":\"56\",\"blockName\":\"U-41;5-6\"},{\"blockId\":\"57\",\"blockName\":\"U-40;7-1\"},{\"blockId\":\"58\",\"blockName\":\"U-27;6-1\"},{\"blockId\":\"59\",\"blockName\":\"6-4\"},{\"blockId\":\"60\",\"blockName\":\"6-3\"},{\"blockId\":\"61\",\"blockName\":\"U-26;8-15\"},{\"blockId\":\"62\",\"blockName\":\"6-2\"},{\"blockId\":\"63\",\"blockName\":\"U-25;8-16\"},{\"blockId\":\"64\",\"blockName\":\"1-4\"},{\"blockId\":\"65\",\"blockName\":\"U-19;8-11\"},{\"blockId\":\"66\",\"blockName\":\"U-16;7-5\"},{\"blockId\":\"67\",\"blockName\":\"U-13;7-8\"},{\"blockId\":\"68\",\"blockName\":\"U-14;8-5\"},{\"blockId\":\"69\",\"blockName\":\"U-8;8-3\"},{\"blockId\":\"70\",\"blockName\":\"U-10;8-2\"},{\"blockId\":\"71\",\"blockName\":\"U-61;8-4\"},{\"blockId\":\"72\",\"blockName\":\"2-10\"},{\"blockId\":\"73\",\"blockName\":\"U-5;5-13\"},{\"blockId\":\"74\",\"blockName\":\"U-4;5-2\"},{\"blockId\":\"75\",\"blockName\":\"4-10;5-1\"},{\"blockId\":\"76\",\"blockName\":\"4-12\"},{\"blockId\":\"77\",\"blockName\":\"4-9\"},{\"blockId\":\"78\",\"blockName\":\"2-6\"},{\"blockId\":\"79\",\"blockName\":\"U-1;3-4\"},{\"blockId\":\"80\",\"blockName\":\"4-11\"},{\"blockId\":\"81\",\"blockName\":\"5-16\"}]}";

    /**
     * Constructor
     */
    public TestTrackBlockRepository() {
          super(new FakeWebServiceClientWithTestData(testTrackBlockDataJsonString),
              new JsonRepositoryMessageDeserializer<TrackBlockSearchResults>(TrackBlockSearchResults.class));
    }

}