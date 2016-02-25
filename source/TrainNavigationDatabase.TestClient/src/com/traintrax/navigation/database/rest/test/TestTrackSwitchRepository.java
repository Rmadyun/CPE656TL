package com.traintrax.navigation.database.rest.test;

import com.traintrax.navigation.database.rest.client.*;
import com.traintrax.navigation.database.rest.data.TrackSwitchSearchResults;

/**
 * Test implementation of the TrackSwitch repository
 * 
 * @author Corey Sanders
 * This is used to simplify the work necessary to test how well a
 *         client can integrate with the repository interfaces
 */
public class TestTrackSwitchRepository extends RemoteTrackSwitchRepository {
	
	private static final String testTrackSwitchDataJsonString = "{\"matches\":[{\"passBlockId\":\"2\",\"name\":\"SW43\",\"pointId\":\"43\",\"bypassBlockId\":\"3\",\"switchId\":\"1\"},{\"passBlockId\":\"8\",\"name\":\"SW42\",\"pointId\":\"44\",\"bypassBlockId\":\"9\",\"switchId\":\"2\"},{\"passBlockId\":\"11\",\"name\":\"SW13\",\"pointId\":\"45\",\"bypassBlockId\":\"12\",\"switchId\":\"3\"},{\"passBlockId\":\"10\",\"name\":\"SW12\",\"pointId\":\"46\",\"bypassBlockId\":\"13\",\"switchId\":\"4\"},{\"passBlockId\":\"15\",\"name\":\"SW11\",\"pointId\":\"47\",\"bypassBlockId\":\"16\",\"switchId\":\"5\"},{\"passBlockId\":\"18\",\"name\":\"SW21\",\"pointId\":\"48\",\"bypassBlockId\":\"19\",\"switchId\":\"6\"},{\"passBlockId\":\"21\",\"name\":\"SW22\",\"pointId\":\"49\",\"bypassBlockId\":\"22\",\"switchId\":\"7\"},{\"passBlockId\":\"8\",\"name\":\"SW23\",\"pointId\":\"50\",\"bypassBlockId\":\"20\",\"switchId\":\"8\"},{\"passBlockId\":\"24\",\"name\":\"SW24\",\"pointId\":\"51\",\"bypassBlockId\":\"25\",\"switchId\":\"9\"},{\"passBlockId\":\"26\",\"name\":\"SW34\",\"pointId\":\"52\",\"bypassBlockId\":\"27\",\"switchId\":\"10\"},{\"passBlockId\":\"28\",\"name\":\"SW33\",\"pointId\":\"53\",\"bypassBlockId\":\"29\",\"switchId\":\"11\"},{\"passBlockId\":\"30\",\"name\":\"SW32\",\"pointId\":\"54\",\"bypassBlockId\":\"31\",\"switchId\":\"12\"},{\"passBlockId\":\"32\",\"name\":\"SW31\",\"pointId\":\"55\",\"bypassBlockId\":\"33\",\"switchId\":\"13\"},{\"passBlockId\":\"35\",\"name\":\"SW64\",\"pointId\":\"56\",\"bypassBlockId\":\"36\",\"switchId\":\"14\"},{\"passBlockId\":\"34\",\"name\":\"SW74\",\"pointId\":\"57\",\"bypassBlockId\":\"38\",\"switchId\":\"15\"},{\"passBlockId\":\"40\",\"name\":\"SW73\",\"pointId\":\"58\",\"bypassBlockId\":\"41\",\"switchId\":\"16\"},{\"passBlockId\":\"39\",\"name\":\"SW72\",\"pointId\":\"59\",\"bypassBlockId\":\"42\",\"switchId\":\"17\"},{\"passBlockId\":\"44\",\"name\":\"SW71\",\"pointId\":\"60\",\"bypassBlockId\":\"45\",\"switchId\":\"18\"},{\"passBlockId\":\"47\",\"name\":\"SW81\",\"pointId\":\"61\",\"bypassBlockId\":\"48\",\"switchId\":\"19\"},{\"passBlockId\":\"50\",\"name\":\"SW82\",\"pointId\":\"62\",\"bypassBlockId\":\"51\",\"switchId\":\"20\"},{\"passBlockId\":\"52\",\"name\":\"SW83\",\"pointId\":\"63\",\"bypassBlockId\":\"49\",\"switchId\":\"21\"},{\"passBlockId\":\"54\",\"name\":\"SW52\",\"pointId\":\"64\",\"bypassBlockId\":\"55\",\"switchId\":\"22\"},{\"passBlockId\":\"37\",\"name\":\"SW51\",\"pointId\":\"65\",\"bypassBlockId\":\"41\",\"switchId\":\"23\"},{\"passBlockId\":\"58\",\"name\":\"SW84\",\"pointId\":\"66\",\"bypassBlockId\":\"59\",\"switchId\":\"24\"},{\"passBlockId\":\"61\",\"name\":\"SW61\",\"pointId\":\"67\",\"bypassBlockId\":\"62\",\"switchId\":\"25\"},{\"passBlockId\":\"63\",\"name\":\"SW62\",\"pointId\":\"68\",\"bypassBlockId\":\"64\",\"switchId\":\"26\"},{\"passBlockId\":\"65\",\"name\":\"SW63\",\"pointId\":\"69\",\"bypassBlockId\":\"66\",\"switchId\":\"27\"},{\"passBlockId\":\"67\",\"name\":\"SW14\",\"pointId\":\"70\",\"bypassBlockId\":\"68\",\"switchId\":\"28\"},{\"passBlockId\":\"70\",\"name\":\"SW41\",\"pointId\":\"71\",\"bypassBlockId\":\"71\",\"switchId\":\"29\"}]}";
	/**
	 * Constructor
	 */
	public TestTrackSwitchRepository() {
		super(new FakeWebServiceClientWithTestData(testTrackSwitchDataJsonString),
				new JsonRepositoryMessageDeserializer<TrackSwitchSearchResults>(TrackSwitchSearchResults.class));
	}

}
