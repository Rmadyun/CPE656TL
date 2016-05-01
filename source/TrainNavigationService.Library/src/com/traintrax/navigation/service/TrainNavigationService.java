package com.traintrax.navigation.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

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
import com.traintrax.navigation.service.events.GenericPublisher;
import com.traintrax.navigation.service.events.NotifierInterface;
import com.traintrax.navigation.service.events.PublisherInterface;
import com.traintrax.navigation.service.events.TrainNavigationServiceEventNotifier;
import com.traintrax.navigation.service.math.ThreeDimensionalSpaceVector;
import com.traintrax.navigation.service.mdu.MduCallbackInterface;
import com.traintrax.navigation.service.mdu.MduCommunicationChannelInterface;
import com.traintrax.navigation.service.mdu.MduProtocolParser;
import com.traintrax.navigation.service.mdu.MduProtocolParserInterface;
import com.traintrax.navigation.service.mdu.MotionDetectionUnit;
import com.traintrax.navigation.service.mdu.MotionDetectionUnitInterface;
import com.traintrax.navigation.service.mdu.SerialPortMduCommunicationChannel;
import com.traintrax.navigation.service.position.Coordinate;
import com.traintrax.navigation.service.position.InertialMotionPositionAlgorithmInterface;
import com.traintrax.navigation.service.position.Train;
import com.traintrax.navigation.service.position.TrainPosition2DAlgorithm;
import com.traintrax.navigation.service.position.TrainPositionEstimate;
import com.traintrax.navigation.service.position.Velocity;
import com.traintrax.navigation.service.rotation.EulerAngleRotation;
import com.traintrax.navigation.trackswitch.SwitchState;

/**
 * Representation of the Train Navigation Service.
 * 
 * @author Corey Sanders
 *
 */
public class TrainNavigationService implements TrainNavigationServiceInterface, MduCallbackInterface {

	/**
	 * Default assumption of the position of the train relative to the origin in
	 * meters
	 */
	private static final Coordinate DefaultTrainPosition = new Coordinate(0, 0, 0);
	private Timer timer;
	private TrackSwitchControllerInterface trackSwitchController;
	private PublisherInterface<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent> eventPublisher;
	private static final int POLL_RATE_IN_MS = 50;
	private final Map<String, TrainPositionEstimate> trainPositionLut = new ConcurrentHashMap<>();
	private final Map<String, TrainMonitorInterface> trainMonitorLut = new ConcurrentHashMap<>();
	private TrainNavigationDatabaseInterface trainNavigationDatabase;
	private MotionDetectionUnitInterface motionDetectionUnit;
	private InertialMotionPositionAlgorithmInterface positionAlgorithm;

	/**
	 * Default train identifier to use if none are specified.
	 */
	private static final String DefaultTrainId = "1";

	/**
	 * Default serial port to contact Motion Detection Units
	 */
	private static final String DEFAULT_MDU_SERIAL_PORT = "COM5";

	/**
	 * Default serial port to use to contact DigiTrax PR3 LocoNet Computer
	 * Interface
	 */
	private static final String DEFAULT_PR3_SERIAL_PORT = "COM4";

	/**
	 * Default user name to use to access database information
	 */
	private static final String DefaultDbUsername = "root";

	/**
	 * Default password to use to access database information
	 */
	private static final String DefaultDbPassword = "root";

	/**
	 * Default name of the database to access
	 */
	private static final String DefaultDbName = "TrainTrax";

	/**
	 * Default network hostname or address to use to access the database
	 */
	private static final String DefaultHost = "localhost";

	/**
	 * Default network port to use to access the database
	 */
	private static final int DefaultDbPort = 3306;

	/**
	 * Default Constructor
	 * 
	 * @throws Exception
	 *             Reports failure to configure the service to run.
	 */
	public TrainNavigationService() throws Exception {
		this(DEFAULT_MDU_SERIAL_PORT, DEFAULT_PR3_SERIAL_PORT);
	}

	/**
	 * Constructor
	 * 
	 * @param mduSerialPort
	 *            the serial port to use to contact Motion Detection Units
	 *            (MDUs)
	 * @param pr3SerialPort
	 *            the serial port to use to contact the PR3 LocoNet Computer
	 *            Interface
	 * @throws Exception
	 *             Reports failure to configure the service to run.
	 */
	public TrainNavigationService(String mduSerialPort, String pr3SerialPort) throws Exception {

		this(mduSerialPort, pr3SerialPort, DefaultHost, DefaultDbPort, DefaultDbName, DefaultDbUsername,
				DefaultDbPassword);
	}

	/**
	 * Constructor
	 * 
	 * @param mduSerialPort
	 *            the serial port to use to contact Motion Detection Units
	 *            (MDUs)
	 * @param pr3SerialPort
	 *            the serial port to use to contact the PR3 LocoNet Computer
	 *            Interface
	 * @param dbUsername
	 *            Username to use to contact the database
	 * @param dbPassword
	 *            Password to use to access the database
	 * @param dbName
	 *            Name of the database to access
	 * @param dbHost
	 *            Network hostname or address to use to access the database
	 * @param dbPort
	 *            Network port to use to access the database
	 * @throws Exception
	 *             Fires exception if external dependencies cannot be configured
	 */
	public TrainNavigationService(String mduSerialPort, String pr3SerialPort, String dbHost, int dbPort, String dbName,
			String dbUsername, String dbPassword) throws Exception {
		MduCommunicationChannelInterface mduCommChannel = new SerialPortMduCommunicationChannel(mduSerialPort);
		MduProtocolParserInterface mduProtocolParser = new MduProtocolParser();
		MotionDetectionUnitInterface motionDetectionUnit = new MotionDetectionUnit(mduCommChannel, mduProtocolParser, this);

		TrainNavigationDatabaseInterface trainNavigationDatabase;
		GenericDatabaseInterface gdi = new MySqlDatabaseAdapter(dbUsername, dbPassword, dbName, dbHost, dbPort);
		
		gdi.connect();

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
				gyroscopeMeasurementRepository, rfidTagNotificationRepository, trainPositionRepository,
				trackSwitchRepository);

		TrackSwitchControllerInterface trackSwitchController = null;
		try {
			trackSwitchController = new TrackSwitchController(pr3SerialPort, TrackSwitchController.DefaultPrefix,
					trainNavigationDatabase);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		NotifierInterface<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent> eventNotifier = new TrainNavigationServiceEventNotifier();

		PublisherInterface<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent> eventPublisher = new GenericPublisher<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent>(
				eventNotifier);
		
		
		//Default position estimation algorithm
		Coordinate currentPosition = DefaultTrainPosition;
		EulerAngleRotation currentOrientation = new EulerAngleRotation(0, 0, 0);
		Velocity currentVelocity = new Velocity(0,0,0);

		InertialMotionPositionAlgorithmInterface positionAlgorithm = new TrainPosition2DAlgorithm(currentPosition,
				currentOrientation, currentVelocity);


		Initialize(motionDetectionUnit, trackSwitchController, trainNavigationDatabase, eventPublisher, positionAlgorithm );
	}
	
	/**
	 * Constructor
	 * 
	 * @param motionDetectionUnit Contacts MDU. Provides train position information
	 * @param trackSwitchController
	 *            Controls the train and track
	 * @param trainNavigationDatabase Contacts the Train Navigation Database. Provides Track Geometry information. Saves measurements and estimates.
	 * @param eventPublisher Notifies clients about Train Navigation Service changes
	 * @param trainPositionAlgorithm Estimates train movement
	 */
	public TrainNavigationService(MotionDetectionUnitInterface motionDetectionUnit, TrackSwitchControllerInterface trackSwitchController, TrainNavigationDatabaseInterface trainNavigationDatabase,
			PublisherInterface<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent> eventPublisher, InertialMotionPositionAlgorithmInterface trainPositionAlgorithm) {

		Initialize(motionDetectionUnit, trackSwitchController, trainNavigationDatabase, eventPublisher, trainPositionAlgorithm );
	}
	
	/**
	 * Constructor
	 * 
	 * @param motionDetectionUnit Contacts MDU. Provides train position information
	 * @param trackSwitchController
	 *            Controls the train and track
	 * @param trainNavigationDatabase Contacts the Train Navigation Database. Provides Track Geometry information. Saves measurements and estimates.
	 * @param eventPublisher Notifies clients about Train Navigation Service changes
     * @param trainPositionAlgorithm Estimates train movement 
	 */
	public void Initialize(MotionDetectionUnitInterface motionDetectionUnit, TrackSwitchControllerInterface trackSwitchController, TrainNavigationDatabaseInterface trainNavigationDatabase,
			PublisherInterface<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent> eventPublisher, InertialMotionPositionAlgorithmInterface trainPositionAlgorithm) {

		
		this.motionDetectionUnit = motionDetectionUnit;
		this.trackSwitchController = trackSwitchController;
		this.eventPublisher = eventPublisher;
		this.trainNavigationDatabase = trainNavigationDatabase;
		this.positionAlgorithm = trainPositionAlgorithm;
		this.timer = new Timer();
		
		//Assign the callback to here to know about detected trains
		this.motionDetectionUnit.setMduCallback(this);
		setupTimer();
	}
	
	/**
	 * Method is responsible for creating a new train monitor to track
	 * position updates for a train.
	 * @param Object keeping track of train state
	 * @param 
	 * @return
	 */
	private TrainMonitorInterface CreateTrainMonitor(Train train, TrainNavigationDatabaseInterface trainNavigationDatabase){
		
		TrainMonitorInterface trainMonitor = new TrainMonitor(train, positionAlgorithm, trainNavigationDatabase);
		
		return trainMonitor;
	}

	/**
	 * Configures the timer to run for listening to train events
	 */
	private void setupTimer() {
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {

				for (TrainMonitorInterface trainMonitor : trainMonitorLut.values()) {
					
					//Try to read new position updates for each train being monitored
					TrainPositionEstimate positionUpdate = trainMonitor.tryFetchNextPositionUpdate();
					if (positionUpdate != null) {
						TrainPositionUpdatedEvent updatedEvent = new TrainPositionUpdatedEvent(
								trainMonitor.getTrainId(), positionUpdate);

						trainPositionLut.put(updatedEvent.getTrainIdentifier(), updatedEvent.getPosition());
						eventPublisher.PublishEvent(updatedEvent);
					}
				}
			}
		}, 0, POLL_RATE_IN_MS);
	}

	/**
	 * Gets the current state of a given switch
	 * 
	 * @param switchIdentifier
	 *            Unique identifier for the switch of interest
	 * @return Current state of the switch requested
	 */
	public SwitchState GetSwitchState(String switchIdentifier) {

		return trackSwitchController.getSwitchState(switchIdentifier);
	}

	/**
	 * Changes the state of a given switch
	 * 
	 * @param switchIdentifier
	 *            Unique identifier for the switch of interest
	 * @param switchState
	 *            State requested for the switch to change
	 */
	public void SetSwitchState(String switchIdentifier, SwitchState switchState) {

		trackSwitchController.ChangeSwitchState(switchIdentifier, switchState);
	}

	/**
	 * Subscribe a client to listen to events from the Train Navigation service.
	 * 
	 * @param subscriber
	 *            Client requested to listen to service events.
	 */
	public void Subscribe(TrainNavigationServiceEventSubscriber subscriber) {
		eventPublisher.Subscribe(subscriber);
	}

	/**
	 * Unsubscribe a client from listening to events
	 * 
	 * @param subscriber
	 *            Client requesting to stop receiving events from the service.
	 */
	public void Unsubscribe(TrainNavigationServiceEventSubscriber subscriber) {
		eventPublisher.Unsubscribe(subscriber);
	}

	@Override
	public TrainPositionEstimate GetLastKnownPosition(String trainIdentifier) {
		return trainPositionLut.get(trainIdentifier);
	}

	@Override
	public List<String> GetKnownTrainIdentifiers() {
		List<String> knownTrainIdentifiers = new LinkedList<String>();

		for(String key : trainPositionLut.keySet()){
			
			TrainPositionEstimate trainPositionEstimate = trainPositionLut.get(key);
			
			if(trainPositionEstimate != null)
			{
				knownTrainIdentifiers.add(key);
			}
		}

		return knownTrainIdentifiers;
	}

	@Override
	public void TrainAdded(Train train) {
		//Handles a new train being detected from a MDU channel
		
		//Create a new train monitor for track position changes
		TrainMonitorInterface trainMonitor = CreateTrainMonitor(train, trainNavigationDatabase);
		
		//Add train monitor to LUT
		trainMonitorLut.put(trainMonitor.getTrainId(), trainMonitor);
	}

}
