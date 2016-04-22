package com.traintrax.navigation.service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.traintrax.navigation.database.library.AccelerometerMeasurementSearchCriteria;
import com.traintrax.navigation.database.library.FilteredSearchRepositoryInterface;
import com.traintrax.navigation.database.library.GyroscopeMeasurementSearchCriteria;
import com.traintrax.navigation.database.library.RepositoryEntry;
import com.traintrax.navigation.database.library.RfidTagDetectedNotificationSearchCriteria;
import com.traintrax.navigation.database.library.TrackPoint;
import com.traintrax.navigation.database.library.TrackPointSearchCriteria;
import com.traintrax.navigation.database.library.TrackSwitch;
import com.traintrax.navigation.database.library.TrackSwitchSearchCriteria;
import com.traintrax.navigation.database.library.TrainPosition;
import com.traintrax.navigation.database.library.TrainPositionSearchCriteria;
import com.traintrax.navigation.service.position.AccelerometerMeasurement;
import com.traintrax.navigation.service.position.Coordinate;
import com.traintrax.navigation.service.position.GyroscopeMeasurement;
import com.traintrax.navigation.service.position.RfidTagDetectedNotification;
import com.traintrax.navigation.service.position.TrainPositionEstimate;
import com.traintrax.navigation.service.position.UnitConversionUtilities;

/**
 * Class communicates with the Train Navigation Database
 * @author Corey Sanders
 *
 */
public class TrainNavigationDatabase implements TrainNavigationDatabaseInterface {


	
	private FilteredSearchRepositoryInterface<TrackPoint, TrackPointSearchCriteria> trackPointRepository;
	private FilteredSearchRepositoryInterface<com.traintrax.navigation.database.library.AccelerometerMeasurement, AccelerometerMeasurementSearchCriteria> accelerometerMeasurementRepository;
	private FilteredSearchRepositoryInterface<com.traintrax.navigation.database.library.RfidTagDetectedNotification, RfidTagDetectedNotificationSearchCriteria> rfidTagNotificationRepository;
	private FilteredSearchRepositoryInterface<com.traintrax.navigation.database.library.GyroscopeMeasurement, GyroscopeMeasurementSearchCriteria> gyroscopeMeasurementRepository;
	private FilteredSearchRepositoryInterface<TrainPosition, TrainPositionSearchCriteria> trainPositionRepository;
	private FilteredSearchRepositoryInterface<TrackSwitch, TrackSwitchSearchCriteria> trackSwitchRepository;

	/**
	 * Constructor
	 * @param trackPointRepository Provides information about RFID tag positions
	 * @param accelerometerMeasurementRepository Saves accelerometer measurements
	 * @param gyroscopeMeasurementRepository Saves gyroscope measurements
	 * @param rfidTagNotificationRepository Saves RFID Tag Detected notifications
	 * @param trainPositionRepository Saves train position estimates
	 * @param trackSwitchRepository Provides switch information
	 */
	public TrainNavigationDatabase(
			FilteredSearchRepositoryInterface<TrackPoint, TrackPointSearchCriteria> trackPointRepository,
			FilteredSearchRepositoryInterface<com.traintrax.navigation.database.library.AccelerometerMeasurement, AccelerometerMeasurementSearchCriteria> accelerometerMeasurementRepository,
			FilteredSearchRepositoryInterface<com.traintrax.navigation.database.library.GyroscopeMeasurement, GyroscopeMeasurementSearchCriteria> gyroscopeMeasurementRepository,
		    FilteredSearchRepositoryInterface<com.traintrax.navigation.database.library.RfidTagDetectedNotification, RfidTagDetectedNotificationSearchCriteria> rfidTagNotificationRepository,
		    FilteredSearchRepositoryInterface<TrainPosition, TrainPositionSearchCriteria> trainPositionRepository, FilteredSearchRepositoryInterface<TrackSwitch,TrackSwitchSearchCriteria> trackSwitchRepository) {
		    
		    this.trackPointRepository = trackPointRepository;
		    this.accelerometerMeasurementRepository = accelerometerMeasurementRepository;
		    this.gyroscopeMeasurementRepository = gyroscopeMeasurementRepository;
		    this.rfidTagNotificationRepository = rfidTagNotificationRepository;
		    this.trackSwitchRepository = trackSwitchRepository;
		    this.trainPositionRepository = trainPositionRepository;
	}
	
	/**
	 * Lookup the position of an RFID Tag placed on the Positive Train
	 * Control Test Bed track.
	 * @param rfidTagIdentifier Unique identifier for the target RFID tag.
	 * @return Position on the track of the RFID tag. null if the position cannot be found
	 */
	public Coordinate findTrackMarkerPosition(String rfidTagIdentifier){
		Coordinate tagPosition = null;
		TrackPointSearchCriteria searchCriteria = new TrackPointSearchCriteria();
		
		List<RepositoryEntry<TrackPoint>> matches = trackPointRepository.find(searchCriteria);
		
		if(matches.size() > 0){
			RepositoryEntry<TrackPoint> selectedMatch = matches.get(0);
						
			tagPosition = new Coordinate(selectedMatch.getValue().getX(), selectedMatch.getValue().getY());
			
			//Convert measurements from inches into meters
			tagPosition = UnitConversionUtilities.convertFromInchesToMeters(tagPosition);
		}

		return tagPosition;
	}
	
	/**
	 * Lists all of the known track switches on the track
	 * @return All of the known track switches on the track
	 */
	public List<TrackSwitch> getTrackSwitches(){
		
		List<RepositoryEntry<TrackSwitch>> trackSwitches;
		
		trackSwitches = trackSwitchRepository.findAll();
		
		List<TrackSwitch> switchList = new LinkedList<TrackSwitch>();
		
		for(RepositoryEntry<TrackSwitch> trackSwitch : trackSwitches){
			switchList.add(trackSwitch.getValue());
		}
		
		return switchList;
	}
	
	/**
	 * Persists an estimate of a given train's position.
	 * @param trainPosition Train position estimate to save
	 */
	public void save(TrainPositionEstimate trainPosition){
		TrainPosition entry = new TrainPosition(trainPosition.getTrainId(), 
				trainPosition.getPosition().getX(),
				trainPosition.getPosition().getY(),
				trainPosition.getPosition().getZ(),
				trainPosition.getTimeAtPosition());
		
		trainPositionRepository.add(entry);
	}
	
	/**
	 * Persists an accelerometer measurement
	 * @param measurement Measurement to save
	 */
	public void save(AccelerometerMeasurement measurement){
		
		com.traintrax.navigation.database.library.AccelerometerMeasurement entry = new
				com.traintrax.navigation.database.library.AccelerometerMeasurement(
						measurement.getAccelerationMeasurement().getMetersPerSecondSquaredAlongXAxis(),
						measurement.getAccelerationMeasurement().getMetersPerSecondSquaredAlongYAxis(),
						measurement.getAccelerationMeasurement().getMetersPerSecondSquaredAlongZAxis(),
						measurement.getTimeMeasured());
		
		accelerometerMeasurementRepository.add(entry);
	}
	
	/**
	 * Persists an gyroscope measurement
	 * @param measurement Measurement to save
	 */
	public void save(GyroscopeMeasurement measurement){

		com.traintrax.navigation.database.library.GyroscopeMeasurement entry = new
				com.traintrax.navigation.database.library.GyroscopeMeasurement(
						measurement.getRadiansRotationPerSecondAlongXAxis(),
						measurement.getRadiansRotationPerSecondAlongYAxis(),
						measurement.getRadiansRotationPerSecondAlongZAxis(),
						measurement.getTimeMeasured());
		
		gyroscopeMeasurementRepository.add(entry);
		
	}
	
	/**
	 * Persists a RFID Tag Detected notification
	 * @param notification Notification to save
	 */
	public void save(RfidTagDetectedNotification notification){
		com.traintrax.navigation.database.library.RfidTagDetectedNotification entry = 
				new com.traintrax.navigation.database.library.RfidTagDetectedNotification(notification.getRfidTagValue(), notification.getTimeDetected());
		
		rfidTagNotificationRepository.add(entry);
	}
	
}
