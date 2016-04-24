package com.traintrax.navigation.service.testing;

import com.traintrax.navigation.database.library.AccelerometerMeasurementRepository;
import com.traintrax.navigation.database.library.AccelerometerMeasurementSearchCriteria;
import com.traintrax.navigation.database.library.FilteredSearchRepositoryInterface;
import com.traintrax.navigation.database.library.GenericDatabaseInterface;
import com.traintrax.navigation.database.library.GyroscopeMeasurementRepository;
import com.traintrax.navigation.database.library.GyroscopeMeasurementSearchCriteria;
import com.traintrax.navigation.database.library.MySqlDatabaseAdapter;
import com.traintrax.navigation.database.library.RfidTagDetectedNotificationRepository;
import com.traintrax.navigation.database.library.RfidTagDetectedNotificationSearchCriteria;
import com.traintrax.navigation.database.library.TrackPoint;
import com.traintrax.navigation.database.library.TrackPointRepository;
import com.traintrax.navigation.database.library.TrackPointSearchCriteria;
import com.traintrax.navigation.database.library.TrackSwitch;
import com.traintrax.navigation.database.library.TrackSwitchRepository;
import com.traintrax.navigation.database.library.TrackSwitchSearchCriteria;
import com.traintrax.navigation.database.library.TrainPosition;
import com.traintrax.navigation.database.library.TrainPositionRepository;
import com.traintrax.navigation.database.library.TrainPositionSearchCriteria;
import com.traintrax.navigation.service.TestTrackSwitchController;
import com.traintrax.navigation.service.TrackSwitchControllerInterface;
import com.traintrax.navigation.service.TrainMonitor;
import com.traintrax.navigation.service.TrainMonitorInterface;
import com.traintrax.navigation.service.TrainNavigationDatabase;
import com.traintrax.navigation.service.TrainNavigationDatabaseInterface;
import com.traintrax.navigation.service.TrainNavigationService;
import com.traintrax.navigation.service.TrainNavigationServiceEvent;
import com.traintrax.navigation.service.TrainNavigationServiceEventSubscriber;
import com.traintrax.navigation.service.TrainNavigationServiceInterface;
import com.traintrax.navigation.service.events.GenericPublisher;
import com.traintrax.navigation.service.events.NotifierInterface;
import com.traintrax.navigation.service.events.PublisherInterface;
import com.traintrax.navigation.service.events.TrainNavigationServiceEventNotifier;
import com.traintrax.navigation.service.mdu.MotionDetectionUnitInterface;
import com.traintrax.navigation.service.mdu.SimulatedMotionDetectionUnit;
import com.traintrax.navigation.service.position.Coordinate;
import com.traintrax.navigation.service.position.InertialMotionPositionAlgorithmInterface;
import com.traintrax.navigation.service.position.Train;
import com.traintrax.navigation.service.position.TrainPosition2DAlgorithm;
import com.traintrax.navigation.service.rotation.EulerAngleRotation;

/**
 * Class creates new TrainNavigation Service instances.
 * This class is intended to simplify the creation of train navigation service
 * instances since it requires complex setup.
 * @author Corey Sanders
 * 
 * TODO: Refactor builder since the organization of the Train Navigation Service has changed.
 */
public class TrainNavigationServiceBuilder {
	
	private static final TrainNavigationServiceBuilder instance = new TrainNavigationServiceBuilder();
	
	/**
	 * Method retrieves the singleton used for creating Train Navigation
	 * Service instances.
	 * @return Retrieves the singleton instance of the factory
	 */
	public static TrainNavigationServiceBuilder getBuilder(){
		return instance;
	}


	private PublisherInterface<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent> eventPublisher;
	private TrackSwitchControllerInterface trackSwitchController;
	private TrainMonitorInterface trainMonitor;
	private MotionDetectionUnitInterface motionDetectionUnitInterface;
	private String trainId;
	private InertialMotionPositionAlgorithmInterface positionAlgorithm;
	private TrainNavigationDatabaseInterface trainNavigationDatabase;

	/**
	 * Default constructor
	 */
	public TrainNavigationServiceBuilder(){
		String trainId = "1";
		Coordinate currentPosition = new Coordinate(0, 0, 0);
		EulerAngleRotation currentOrientation = new EulerAngleRotation(0, 0, Math.PI / 4);
		
		SimulatedMotionDetectionUnit motionDetectionUnit = new SimulatedMotionDetectionUnit();

		TrainNavigationDatabaseInterface trainNavigationDatabase;
		GenericDatabaseInterface gdi = new MySqlDatabaseAdapter();
		FilteredSearchRepositoryInterface<TrackPoint, TrackPointSearchCriteria> trackPointRepository = new TrackPointRepository(
				gdi);
		FilteredSearchRepositoryInterface<com.traintrax.navigation.database.library.AccelerometerMeasurement, AccelerometerMeasurementSearchCriteria> accelerometerMeasurementRepository = new AccelerometerMeasurementRepository(
				gdi);
		FilteredSearchRepositoryInterface<com.traintrax.navigation.database.library.RfidTagDetectedNotification, RfidTagDetectedNotificationSearchCriteria> rfidTagNotificationRepository = new RfidTagDetectedNotificationRepository(
				gdi);
		FilteredSearchRepositoryInterface<com.traintrax.navigation.database.library.GyroscopeMeasurement, GyroscopeMeasurementSearchCriteria> gyroscopeMeasurementRepository = new GyroscopeMeasurementRepository(
				gdi);
		FilteredSearchRepositoryInterface<TrainPosition, TrainPositionSearchCriteria> trainPositionRepository = new TrainPositionRepository(
				gdi);
		FilteredSearchRepositoryInterface<TrackSwitch, TrackSwitchSearchCriteria> trackSwitchRepository = new TrackSwitchRepository(
				gdi);

		trainNavigationDatabase = new TrainNavigationDatabase(trackPointRepository, accelerometerMeasurementRepository,
				gyroscopeMeasurementRepository, rfidTagNotificationRepository, trainPositionRepository, trackSwitchRepository);

		InertialMotionPositionAlgorithmInterface positionAlgorithm = new TrainPosition2DAlgorithm(currentPosition,
				currentOrientation);

		Train train = null;
		
		for(Train t : motionDetectionUnit.getAssociatedTrains()){
			train = t;
			break;
		}
		TrainMonitorInterface trainMonitor = new TrainMonitor(train, positionAlgorithm, trainNavigationDatabase);
		TrackSwitchControllerInterface trackSwitchController = null;

		trackSwitchController = new TestTrackSwitchController();

		NotifierInterface<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent> eventNotifier = new TrainNavigationServiceEventNotifier();

		PublisherInterface<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent> eventPublisher = new GenericPublisher<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent>(
				eventNotifier);

		this.trainId = trainId;
		this.positionAlgorithm = positionAlgorithm;
		this.trainNavigationDatabase = trainNavigationDatabase;
		this.motionDetectionUnitInterface = motionDetectionUnit;
		this.eventPublisher = eventPublisher;
		this.trackSwitchController = trackSwitchController;
		this.trainMonitor = trainMonitor;
	}
	
	/**
	 * Assigns the event publisher to use for creating the service
	 * @param eventPublisher Event publisher to use for creating the service
	 */
	public void setEventPublisher(
			PublisherInterface<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent> eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	/**
	 * Assigns the track switch controller to use for creating the service
	 * @param trackSwitchController Controls track switches on the test bed 
	 */
	public void setTrainController(TrackSwitchControllerInterface trackSwitchController) {
		this.trackSwitchController = trackSwitchController;
	}

	/**
	 * Assigns the train monitor to use for creating the service
	 * @param trainMonitor Tracks trains on the test bed
	 */
	public void setTrainMonitor(TrainMonitorInterface trainMonitor) {
		this.trainMonitor = trainMonitor;
	}

	/**
	 * Assigns the motion detection unit to use for configuring the service
	 * @param motionDetectionUnitInterface Contact to remote equipment mounted on trains in the test bed.
	 */
	public void setMotionDetectionUnitInterface(MotionDetectionUnitInterface motionDetectionUnitInterface) {
		this.motionDetectionUnitInterface = motionDetectionUnitInterface;
		Train train = null;
		
		for(Train t : motionDetectionUnitInterface.getAssociatedTrains()){
			train = t;
			break;
		}
		
		if(train != null){
		    trainMonitor = new TrainMonitor(train, positionAlgorithm,
			    	trainNavigationDatabase);
		}
	}
	
	/**
	 * Assigns the position algorithm to use for configuring the service
	 * @param positionAlgorithm Performs the logic to calculate the position of a train based on measurements
	 */
	public void setPositionAlgorithm(InertialMotionPositionAlgorithmInterface positionAlgorithm) {
		this.positionAlgorithm = positionAlgorithm;
		Train train = null;
				
		for(Train t : motionDetectionUnitInterface.getAssociatedTrains()){
			train = t;
			break;
		}
		
		if(train != null){
		    trainMonitor = new TrainMonitor(train, positionAlgorithm,
			    	trainNavigationDatabase);
		}
	}
	
	/**
	 * Creates a new Train Navigation Service instance
	 * @return a new Train Navigation Service instance
	 */
	public TrainNavigationServiceInterface build(){
		TrainNavigationServiceInterface trainNavigationService = new TrainNavigationService(trainMonitor,
				trackSwitchController, eventPublisher);
		
		return trainNavigationService;
	}
	
}
