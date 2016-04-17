package com.traintrax.navigation.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
import com.traintrax.navigation.database.library.TrainPosition;
import com.traintrax.navigation.database.library.TrainPositionRepository;
import com.traintrax.navigation.database.library.TrainPositionSearchCriteria;
import com.traintrax.navigation.service.events.GenericPublisher;
import com.traintrax.navigation.service.events.NotifierInterface;
import com.traintrax.navigation.service.events.PublisherInterface;
import com.traintrax.navigation.service.events.TrainNavigationServiceEventNotifier;
import com.traintrax.navigation.service.mdu.InertialMotionPositionAlgorithmInterface;
import com.traintrax.navigation.service.mdu.MduCommunicationChannelInterface;
import com.traintrax.navigation.service.mdu.MduProtocolParser;
import com.traintrax.navigation.service.mdu.MduProtocolParserInterface;
import com.traintrax.navigation.service.mdu.MotionDetectionUnit;
import com.traintrax.navigation.service.mdu.MotionDetectionUnitInterface;
import com.traintrax.navigation.service.mdu.SerialPortMduCommunicationChannel;
import com.traintrax.navigation.service.mdu.SimulatedMotionDetectionUnit;
import com.traintrax.navigation.service.mdu.TrainPosition2DAlgorithm;
import com.traintrax.navigation.service.position.Coordinate;
import com.traintrax.navigation.service.rotation.EulerAngleRotation;
import com.traintrax.navigation.trackswitch.SwitchState;

/**
 * Representation of the Train Navigation Service.
 * @author Corey Sanders
 *
 */
public class TrainNavigationService implements TrainNavigationServiceInterface {
	
	/**
	 * Default assumption of the position of the train relative to the origin
	 * in meters
	 */
	private static final Coordinate DefaultTrainPosition = new Coordinate(0,0,0);
	private final List<String> trainIds = new LinkedList<String>();
	private Timer timer = new Timer();
	private final TrainMonitorInterface trainMonitor;
	private final TrackSwitchControllerInterface trainController;
	private final PublisherInterface<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent> eventPublisher;
	private static final int POLL_RATE_IN_MS = 5000;
	private final Map<String, ValueUpdate<Coordinate>> trainPositionLut = new HashMap<>();
	
	/**
	 * Default train identifier to use if none are specified.
	 */
	private static final String DefaultTrainId = "1";
	
	/**
	 * Default serial port to contact Motion Detection Units
	 */
	private static final String DEFAULT_MDU_SERIAL_PORT="COM5";
	
	/**
	 * Default serial port to use to contact DigiTrax PR3 LocoNet Computer Interface
	 */
	private static final String DEFAULT_PR3_SERIAL_PORT="COM4";
	
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
	 * @throws Exception Reports failure to configure the service to run.
	 */
	public TrainNavigationService() throws Exception{
	    this(DEFAULT_MDU_SERIAL_PORT, DEFAULT_PR3_SERIAL_PORT);
	}
	
	/**
	 * Constructor
	 * @param mduSerialPort the serial port to use to contact Motion Detection Units (MDUs)
	 * @param pr3SerialPort the serial port to use to contact the PR3 LocoNet Computer Interface 
	 * @throws Exception Reports failure to configure the service to run.
	 */
	public TrainNavigationService(String mduSerialPort, String pr3SerialPort) throws Exception{

		this(mduSerialPort, pr3SerialPort, DefaultHost, DefaultDbPort, DefaultDbName, DefaultDbUsername, DefaultDbPassword);
	}
	
	/**
	 * Constructor
	 * @param trainMonitor Provides train position information
	 * @param trainController Controls the train and track
	 */
	public TrainNavigationService(TrainMonitorInterface trainMonitor, TrackSwitchControllerInterface trainController, PublisherInterface<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent> eventPublisher){
		this.trainMonitor = trainMonitor;
		this.trainController = trainController;
		this.eventPublisher = eventPublisher;
	
		initialize("1", DefaultTrainPosition, trainMonitor, trainController, eventPublisher);
	}
	
	/**
	 * Constructor 
	 * @param mduSerialPort the serial port to use to contact Motion Detection Units (MDUs)
	 * @param pr3SerialPort the serial port to use to contact the PR3 LocoNet Computer Interface
	 * @param dbUsername Username to use to contact the database
	 * @param dbPassword Password to use to access the database
	 * @param dbName Name of the database to access
	 * @param dbHost Network hostname or address to use to access the database
	 * @param dbPort Network port to use to access the database
	 * @throws Exception Fires exception if external dependencies cannot be configured
	 */
	public TrainNavigationService(String mduSerialPort, String pr3SerialPort, String dbHost, int dbPort, String dbName,
			String dbUsername, String dbPassword) throws Exception {
		String trainId = DefaultTrainId;
		Coordinate currentPosition = DefaultTrainPosition;
		EulerAngleRotation currentOrientation = new EulerAngleRotation(0,0,0);

		MduCommunicationChannelInterface mduCommChannel = new SerialPortMduCommunicationChannel(mduSerialPort);
		MduProtocolParserInterface mduProtocolParser = new MduProtocolParser();
		MotionDetectionUnitInterface motionDetectionUnit = new MotionDetectionUnit(mduCommChannel, mduProtocolParser);
		
		TrainNavigationDatabaseInterface trainNavigationDatabase;
		GenericDatabaseInterface gdi = new MySqlDatabaseAdapter(dbUsername, dbPassword, dbName, dbHost, dbPort);
		FilteredSearchRepositoryInterface<TrackPoint, TrackPointSearchCriteria> trackPointRepository = new TrackPointRepository(gdi);
		FilteredSearchRepositoryInterface<com.traintrax.navigation.database.library.AccelerometerMeasurement, AccelerometerMeasurementSearchCriteria> accelerometerMeasurementRepository = new AccelerometerMeasurementRepository(gdi);
		FilteredSearchRepositoryInterface<com.traintrax.navigation.database.library.RfidTagDetectedNotification, RfidTagDetectedNotificationSearchCriteria> rfidTagNotificationRepository = new RfidTagDetectedNotificationRepository(gdi);
		FilteredSearchRepositoryInterface<com.traintrax.navigation.database.library.GyroscopeMeasurement, GyroscopeMeasurementSearchCriteria> gyroscopeMeasurementRepository = new GyroscopeMeasurementRepository(gdi);
		FilteredSearchRepositoryInterface<TrainPosition, TrainPositionSearchCriteria> trainPositionRepository = new TrainPositionRepository(gdi);
		
		trainNavigationDatabase = new TrainNavigationDatabase(trackPointRepository, accelerometerMeasurementRepository,
				gyroscopeMeasurementRepository, rfidTagNotificationRepository, trainPositionRepository);
		
		InertialMotionPositionAlgorithmInterface positionAlgorithm = new TrainPosition2DAlgorithm(currentPosition, currentOrientation);
		
		TrainMonitorInterface trainMonitor = new TrainMonitor(trainId, positionAlgorithm, motionDetectionUnit, trainNavigationDatabase);
		TrackSwitchControllerInterface trainController = null;
		try {
			trainController = new TrackSwitchController(pr3SerialPort, TrackSwitchController.DefaultPrefix);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		NotifierInterface<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent> eventNotifier = new TrainNavigationServiceEventNotifier();
		
		PublisherInterface<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent> eventPublisher = new GenericPublisher<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent> (eventNotifier);
		
		this.trainMonitor = trainMonitor;
		this.trainController = trainController;
		this.eventPublisher = eventPublisher;
	
		initialize(trainId, currentPosition, trainMonitor, trainController, eventPublisher);
	}

	/**
	 * Completes initialization of the class.
	 * @param trainMonitor Determines train position
	 * @param trainController Controls switches
	 * @param eventPublisher Notifiers subscribers of changes within the service or monitored trains
	 */
	private void initialize(String trainId, Coordinate currentPosition, TrainMonitorInterface trainMonitor, TrackSwitchControllerInterface trainController, PublisherInterface<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent> eventPublisher){

		trainPositionLut.put(trainId, new ValueUpdate<Coordinate>(currentPosition, Calendar.getInstance()));
		
		setupTimer();
	}
	
	/**
	 * Configures the timer to run for listening to train events
	 */
	private void setupTimer(){
		timer.scheduleAtFixedRate(new TimerTask() {
			  @Override
			  public void run() {
			    
				  ValueUpdate<Coordinate> positionUpdate = trainMonitor.waitForNextPositionUpdate();
				  TrainPositionUpdatedEvent updatedEvent = new TrainPositionUpdatedEvent(trainMonitor.getTrainId(), positionUpdate);
				  
				  trainPositionLut.put(updatedEvent.getTrainIdentifier(), updatedEvent.getPosition());
				  eventPublisher.PublishEvent(updatedEvent);
			  }
			}, 0, POLL_RATE_IN_MS);
	}
	
    /**
     * Gets the current state of a given switch
     * @param switchIdentifier Unique identifier for the switch of interest
     * @return Current state of the switch requested
     */
	public SwitchState GetSwitchState(String switchIdentifier){
		
		return trainController.getSwitchState(switchIdentifier);
	}
	
	/**
	 * Changes the state of a given switch
     * @param switchIdentifier Unique identifier for the switch of interest 
	 * @param switchState State requested for the switch to change
	 */
	public void SetSwitchState(String switchIdentifier, SwitchState switchState){
		
		trainController.ChangeSwitchState(switchIdentifier, switchState); 
	}
	
	/**
	 * Subscribe a client to listen to events from the Train Navigation service.
	 * @param subscriber Client requested to listen to service events.
	 */
	public void Subscribe(TrainNavigationServiceEventSubscriber subscriber){
		eventPublisher.Subscribe(subscriber);
	}
	
	/**
	 * Unsubscribe a client from listening to events
	 * @param subscriber Client requesting to stop receiving events from the service.
	 */
	public void Unsubscribe(TrainNavigationServiceEventSubscriber subscriber){
		eventPublisher.Unsubscribe(subscriber);
	}

	@Override
	public ValueUpdate<Coordinate> GetLastKnownPosition(String trainIdentifier) {
		return trainPositionLut.get(trainIdentifier);
	}

	@Override
	public List<String> GetKnownTrainIdentifiers() {
		List<String> knownTrainIdentifers = new LinkedList<String>(trainPositionLut.keySet());
		
		return knownTrainIdentifers;
	}


}
