package edu.uah.cpe.traintrax;

/* import com.traintrax.navigation.database.rest.client.*;
import com.traintrax.navigation.database.rest.data.TrackSwitchSearchResults; */

import com.traintrax.navigation.database.rest.client.*;
import com.traintrax.navigation.database.rest.data.*;

/**
 * Test implementation of the TrackSwitch repository
 * 
 * @author Corey Sanders
 * This is used to simplify the work necessary to test how well a
 *         client can integrate with the repository interfaces
 */
public class TestTrackSwitchRepository extends RemoteTrackSwitchRepository {
	
	private static final String testTrackSwitchDataJsonString = "{\"matches\":[{\"passBlockId\":\"2\",\"name\":\"SW43\",\"pointId\":\"126\",\"bypassBlockId\":\"3\",\"switchId\":\"1\"},{\"passBlockId\":\"37\",\"name\":\"SW42\",\"pointId\":\"127\",\"bypassBlockId\":\"38\",\"switchId\":\"2\"},{\"passBlockId\":\"40\",\"name\":\"SW13\",\"pointId\":\"128\",\"bypassBlockId\":\"41\",\"switchId\":\"3\"},{\"passBlockId\":\"39\",\"name\":\"SW12\",\"pointId\":\"129\",\"bypassBlockId\":\"20\",\"switchId\":\"4\"},{\"passBlockId\":\"43\",\"name\":\"SW11\",\"pointId\":\"130\",\"bypassBlockId\":\"44\",\"switchId\":\"5\"},{\"passBlockId\":\"45\",\"name\":\"SW21\",\"pointId\":\"131\",\"bypassBlockId\":\"35\",\"switchId\":\"6\"},{\"passBlockId\":\"33\",\"name\":\"SW22\",\"pointId\":\"132\",\"bypassBlockId\":\"47\",\"switchId\":\"7\"},{\"passBlockId\":\"37\",\"name\":\"SW23\",\"pointId\":\"133\",\"bypassBlockId\":\"46\",\"switchId\":\"8\"},{\"passBlockId\":\"21\",\"name\":\"SW24\",\"pointId\":\"134\",\"bypassBlockId\":\"49\",\"switchId\":\"9\"},{\"passBlockId\":\"50\",\"name\":\"SW34\",\"pointId\":\"135\",\"bypassBlockId\":\"51\",\"switchId\":\"10\"},{\"passBlockId\":\"52\",\"name\":\"SW33\",\"pointId\":\"136\",\"bypassBlockId\":\"53\",\"switchId\":\"11\"},{\"passBlockId\":\"54\",\"name\":\"SW32\",\"pointId\":\"137\",\"bypassBlockId\":\"55\",\"switchId\":\"12\"},{\"passBlockId\":\"56\",\"name\":\"SW31\",\"pointId\":\"138\",\"bypassBlockId\":\"57\",\"switchId\":\"13\"},{\"passBlockId\":\"59\",\"name\":\"SW64\",\"pointId\":\"139\",\"bypassBlockId\":\"60\",\"switchId\":\"14\"},{\"passBlockId\":\"58\",\"name\":\"SW74\",\"pointId\":\"140\",\"bypassBlockId\":\"62\",\"switchId\":\"15\"},{\"passBlockId\":\"30\",\"name\":\"SW73\",\"pointId\":\"141\",\"bypassBlockId\":\"64\",\"switchId\":\"16\"},{\"passBlockId\":\"63\",\"name\":\"SW72\",\"pointId\":\"142\",\"bypassBlockId\":\"31\",\"switchId\":\"17\"},{\"passBlockId\":\"28\",\"name\":\"SW71\",\"pointId\":\"143\",\"bypassBlockId\":\"65\",\"switchId\":\"18\"},{\"passBlockId\":\"67\",\"name\":\"SW81\",\"pointId\":\"144\",\"bypassBlockId\":\"68\",\"switchId\":\"19\"},{\"passBlockId\":\"12\",\"name\":\"SW82\",\"pointId\":\"145\",\"bypassBlockId\":\"69\",\"switchId\":\"20\"},{\"passBlockId\":\"70\",\"name\":\"SW83\",\"pointId\":\"146\",\"bypassBlockId\":\"25\",\"switchId\":\"21\"},{\"passBlockId\":\"16\",\"name\":\"SW52\",\"pointId\":\"147\",\"bypassBlockId\":\"14\",\"switchId\":\"22\"},{\"passBlockId\":\"61\",\"name\":\"SW51\",\"pointId\":\"148\",\"bypassBlockId\":\"64\",\"switchId\":\"23\"},{\"passBlockId\":\"72\",\"name\":\"SW84\",\"pointId\":\"149\",\"bypassBlockId\":\"9\",\"switchId\":\"24\"},{\"passBlockId\":\"73\",\"name\":\"SW61\",\"pointId\":\"150\",\"bypassBlockId\":\"23\",\"switchId\":\"25\"},{\"passBlockId\":\"74\",\"name\":\"SW62\",\"pointId\":\"151\",\"bypassBlockId\":\"75\",\"switchId\":\"26\"},{\"passBlockId\":\"76\",\"name\":\"SW63\",\"pointId\":\"152\",\"bypassBlockId\":\"77\",\"switchId\":\"27\"},{\"passBlockId\":\"78\",\"name\":\"SW14\",\"pointId\":\"153\",\"bypassBlockId\":\"7\",\"switchId\":\"28\"},{\"passBlockId\":\"80\",\"name\":\"SW41\",\"pointId\":\"154\",\"bypassBlockId\":\"81\",\"switchId\":\"29\"}]}";
	/**
	 * Constructor
	 */
	public TestTrackSwitchRepository() {
		super(new FakeWebServiceClientWithTestData(testTrackSwitchDataJsonString),
				new JsonRepositoryMessageDeserializer<TrackSwitchSearchResults>(TrackSwitchSearchResults.class));
	}

}
