package edu.uah.cpe.traintrax;

/* import com.traintrax.navigation.database.rest.client.*;
import com.traintrax.navigation.database.rest.data.TrackBlockSearchResults; */

/**
 * Test implementation of the TrackBlock repository
 * 
 * @author Rashad Madyun
 * This is used to simplify the work necessary to test how well a
 *         client can integrate with the repository interfaces
 */
public class TestTrackBlockRepository extends RemoteTrackBlockRepository {

    private static final String testTrackBlockDataJsonString = "{\"matches\":[{\"blockId\":\"1\",\"blockName\":\"1-1\"},{\"blockId\":\"2\",\"blockName\":\"2-1\"},{\"blockId\":\"3\",\"blockName\":\"2-2\"},{\"blockId\":\"4\",\"blockName\":\"2-5\"},{\"blockId\":\"5\",\"blockName\":\"2-3\"},{\"blockId\":\"6\",\"blockName\":\"2-4\"},{\"blockId\":\"7\",\"blockName\":\"3-16\"},{\"blockId\":\"8\",\"blockName\":\"1-3\"},{\"blockId\":\"9\",\"blockName\":\"U-60\"},{\"blockId\":\"10\",\"blockName\":\"3-15;U-59\"},{\"blockId\":\"11\",\"blockName\":\"3-13;U-57\"},{\"blockId\":\"12\",\"blockName\":\"3-14;U-58\"},{\"blockId\":\"13\",\"blockName\":\"3-8\"},{\"blockId\":\"14\",\"blockName\":\"4-15;U-51\"},{\"blockId\":\"15\",\"blockName\":\"U-54;1-12\"},{\"blockId\":\"16\",\"blockName\":\"U-52;4-16\"},{\"blockId\":\"17\",\"blockName\":\"U-45;1-8\"},{\"blockId\":\"18\",\"blockName\":\"U-46;1-14\"},{\"blockId\":\"19\",\"blockName\":\"U-47;1-15\"},{\"blockId\":\"20\",\"blockName\":\"U-44;1-7\"},{\"blockId\":\"21\",\"blockName\":\"U-33;4-3\"},{\"blockId\":\"22\",\"blockName\":\"U-34;4-4\"},{\"blockId\":\"23\",\"blockName\":\"U-35;1-6\"},{\"blockId\":\"24\",\"blockName\":\"3-4\"},{\"blockId\":\"25\",\"blockName\":\"U-36;1-5\"},{\"blockId\":\"26\",\"blockName\":\"3-3\"},{\"blockId\":\"27\",\"blockName\":\"U-37;4-2\"},{\"blockId\":\"28\",\"blockName\":\"3-1;6-8\"},{\"blockId\":\"29\",\"blockName\":\"U-38;5-8\"},{\"blockId\":\"30\",\"blockName\":\"5-5\"},{\"blockId\":\"31\",\"blockName\":\"U-39;5-7\"},{\"blockId\":\"32\",\"blockName\":\"U-41;5-6\"},{\"blockId\":\"33\",\"blockName\":\"U-40;7-1\"},{\"blockId\":\"34\",\"blockName\":\"U-27;6-1\"},{\"blockId\":\"35\",\"blockName\":\"6-4\"},{\"blockId\":\"36\",\"blockName\":\"6-3\"},{\"blockId\":\"37\",\"blockName\":\"U-26;8-15\"},{\"blockId\":\"38\",\"blockName\":\"6-2\"},{\"blockId\":\"39\",\"blockName\":\"U-25;8-16\"},{\"blockId\":\"40\",\"blockName\":\"U-21;8-9\"},{\"blockId\":\"41\",\"blockName\":\"1-4\"},{\"blockId\":\"42\",\"blockName\":\"U-22;8-14\"},{\"blockId\":\"43\",\"blockName\":\"U-20;8-10\"},{\"blockId\":\"44\",\"blockName\":\"U-18;8-12\"},{\"blockId\":\"45\",\"blockName\":\"U-19;8-11\"},{\"blockId\":\"46\",\"blockName\":\"U-16;7-5\"},{\"blockId\":\"47\",\"blockName\":\"U-13;7-8\"},{\"blockId\":\"48\",\"blockName\":\"U-14;8-5\"},{\"blockId\":\"49\",\"blockName\":\"U-9;8-1\"},{\"blockId\":\"50\",\"blockName\":\"U-6;5-4\"},{\"blockId\":\"51\",\"blockName\":\"U-8;8-3\"},{\"blockId\":\"52\",\"blockName\":\"U-10;8-2\"},{\"blockId\":\"53\",\"blockName\":\"2-14\"},{\"blockId\":\"54\",\"blockName\":\"2-15\"},{\"blockId\":\"55\",\"blockName\":\"2-16\"},{\"blockId\":\"56\",\"blockName\":\"U-61;8-4\"},{\"blockId\":\"57\",\"blockName\":\"2-12\"},{\"blockId\":\"58\",\"blockName\":\"2-10\"},{\"blockId\":\"59\",\"blockName\":\"2-11\"},{\"blockId\":\"60\",\"blockName\":\"5-3\"},{\"blockId\":\"61\",\"blockName\":\"U-5;5-13\"},{\"blockId\":\"62\",\"blockName\":\"5-14\"},{\"blockId\":\"63\",\"blockName\":\"U-4;5-2\"},{\"blockId\":\"64\",\"blockName\":\"4-10;5-1\"},{\"blockId\":\"65\",\"blockName\":\"4-12\"},{\"blockId\":\"66\",\"blockName\":\"4-9\"},{\"blockId\":\"67\",\"blockName\":\"2-6\"},{\"blockId\":\"68\",\"blockName\":\"2-7\"},{\"blockId\":\"69\",\"blockName\":\"U-1;3-4\"},{\"blockId\":\"70\",\"blockName\":\"4-11\"},{\"blockId\":\"71\",\"blockName\":\"5-16\"}]}";

    /**
     * Constructor
     */
    public TestTrackBlockRepository() {
          super(new FakeWebServiceClientWithTestData(testTrackBlockDataJsonString),
              new JsonRepositoryMessageDeserializer<TrackBlockSearchResults>(TrackBlockSearchResults.class));
    }

}