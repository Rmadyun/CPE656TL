package edu.uah.cpe.traintrax;


import com.traintrax.navigation.service.rest.client.RemoteTrainNavigationService;

/**
 * Restful Web client implementation of the Train Navigation Service
 * 
 * @author Corey Sanders
 *
 */
public class TrainPositionMonitor  {

	//NOTE: Unfinished. May remove to use the Train Navigation interface directly

	String hostName = "10.0.2.2"; //"192.168.1.104";//"10.0.2.2";
	Integer port = 8182;

	private RemoteTrainNavigationService trainNavigationService = new RemoteTrainNavigationService(hostName, port);

	/**
	 * Constructor
	 */
	public TrainPositionMonitor() {
		hostName = "localhost";
		port = 8182;
	}


}
