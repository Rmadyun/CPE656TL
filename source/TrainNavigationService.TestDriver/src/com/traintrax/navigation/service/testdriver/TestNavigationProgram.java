package com.traintrax.navigation.service.testdriver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;

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
import com.traintrax.navigation.service.TestTrackSwitchController;
import com.traintrax.navigation.service.TrackSwitchController;
import com.traintrax.navigation.service.TrackSwitchControllerInterface;
import com.traintrax.navigation.service.TrainMonitor;
import com.traintrax.navigation.service.TrainMonitorInterface;
import com.traintrax.navigation.service.TrainNavigationDatabase;
import com.traintrax.navigation.service.TrainNavigationDatabaseInterface;
import com.traintrax.navigation.service.TrainNavigationService;
import com.traintrax.navigation.service.TrainNavigationServiceEvent;
import com.traintrax.navigation.service.TrainNavigationServiceEventNotifier;
import com.traintrax.navigation.service.TrainNavigationServiceEventSubscriber;
import com.traintrax.navigation.service.TrainNavigationServiceInterface;
import com.traintrax.navigation.service.events.GenericPublisher;
import com.traintrax.navigation.service.events.NotifierInterface;
import com.traintrax.navigation.service.events.PublisherInterface;
import com.traintrax.navigation.service.math.*;
import com.traintrax.navigation.service.mdu.*;
import com.traintrax.navigation.service.position.Coordinate;
import com.traintrax.navigation.service.position.ValueUpdate;
import com.traintrax.navigation.service.rotation.*;

import gnu.io.*;
import jmri.jmrix.SerialPortAdapter;
import jmri.jmrix.loconet.LnPacketizer;
import jmri.jmrix.loconet.LnPortController;
import jmri.jmrix.loconet.LnTurnout;
import jmri.jmrix.loconet.locobuffer.LocoBufferAdapter;
import jmri.jmrix.loconet.ms100.MS100Adapter;
import jmri.jmrix.nce.usbdriver.UsbDriverAdapter;

public class TestNavigationProgram {
	
	   /**
     * @return    A HashSet containing the CommPortIdentifier for all serial ports that are not currently being used.
     */
    public static HashSet<CommPortIdentifier> getAvailableSerialPorts() {
        HashSet<CommPortIdentifier> h = new HashSet<CommPortIdentifier>();
        Enumeration thePorts = CommPortIdentifier.getPortIdentifiers();
        while (thePorts.hasMoreElements()) {
            CommPortIdentifier com = (CommPortIdentifier) thePorts.nextElement();
            switch (com.getPortType()) {
            case CommPortIdentifier.PORT_SERIAL:
                try {
                    CommPort thePort = com.open("CommUtil", 50);
                    thePort.close();
                    h.add(com);
                } catch (PortInUseException e) {
                    System.out.println("Port, "  + com.getName() + ", is in use.");
                } catch (Exception e) {
                    System.err.println("Failed to open port " +  com.getName());
                    e.printStackTrace();
                }
            }
        }
        return h;
    }
    
    private static void TestJmri(){
    	String serialPort = "/dev/ttyUSB0";
    	String prefix = "";
    	int switchNumber = 43;
    	
    	//TODO: Figure out if LocoBuffer or MS100: (We are using a MS100 compatible device)
    	
    	LnPortController serialPortAdapter = new MS100Adapter();
    	
    	serialPortAdapter.openPort(serialPort, "Train Navigation Service");
    	
    	try {
			serialPortAdapter.connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	LnPacketizer lnPacketizer = new LnPacketizer();
    	
    	lnPacketizer.connectPort(serialPortAdapter);
    	
    	LnTurnout lnTurnout = new LnTurnout(prefix, switchNumber, lnPacketizer);
    	
    	
    }
    
    private static void TestMduMeasurementRead(){
    	
		String trainId = "1";
		Coordinate currentPosition = new Coordinate(0,0,0);
		EulerAngleRotation currentOrientation = new EulerAngleRotation(0,0,0);

		MduCommunicationChannelInterface mduCommunicationChannel = new TestMduCommunicationChannel();
		MduProtocolParserInterface mduProtocolParser = new MduProtocolParser();
		MotionDetectionUnitInterface motionDetectionUnit = new MotionDetectionUnit(mduCommunicationChannel, mduProtocolParser);
		
		TrainNavigationDatabaseInterface trainNavigationDatabase;
		GenericDatabaseInterface gdi = new MySqlDatabaseAdapter();
		FilteredSearchRepositoryInterface<TrackPoint, TrackPointSearchCriteria> trackPointRepository = new TrackPointRepository(gdi);
		FilteredSearchRepositoryInterface<com.traintrax.navigation.database.library.AccelerometerMeasurement, AccelerometerMeasurementSearchCriteria> accelerometerMeasurementRepository = new AccelerometerMeasurementRepository();
		FilteredSearchRepositoryInterface<com.traintrax.navigation.database.library.RfidTagDetectedNotification, RfidTagDetectedNotificationSearchCriteria> rfidTagNotificationRepository = new RfidTagDetectedNotificationRepository();
		FilteredSearchRepositoryInterface<com.traintrax.navigation.database.library.GyroscopeMeasurement, GyroscopeMeasurementSearchCriteria> gyroscopeMeasurementRepository = new GyroscopeMeasurementRepository();
		FilteredSearchRepositoryInterface<TrainPosition, TrainPositionSearchCriteria> trainPositionRepository = new TrainPositionRepository();
		
		trainNavigationDatabase = new TrainNavigationDatabase(trackPointRepository, accelerometerMeasurementRepository,
				gyroscopeMeasurementRepository, rfidTagNotificationRepository, trainPositionRepository);
		
		InertialMotionPositionAlgorithmInterface positionAlgorithm = new TrainPositionAlgorithm(currentPosition, currentOrientation);
		
		TrainMonitorInterface trainMonitor = new TrainMonitor(trainId, positionAlgorithm, motionDetectionUnit, trainNavigationDatabase);
		TrackSwitchControllerInterface trainController = null;
/*		try {
			trainController = new TrainController();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} */
		
		trainController = new TestTrackSwitchController();
		
		NotifierInterface<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent> eventNotifier = new TrainNavigationServiceEventNotifier();
		
		PublisherInterface<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent> eventPublisher = new GenericPublisher<TrainNavigationServiceEventSubscriber, TrainNavigationServiceEvent> (eventNotifier);

    	TrainNavigationServiceInterface trainNavigationService = new TrainNavigationService(trainMonitor, trainController, eventPublisher);

    	ReadTrainPositionsFromTrainNavigationService(trainNavigationService);
    	
    }
    

	private static void TestSampleRotation(){
		List<GyroscopeMeasurement> measurements = new ArrayList<GyroscopeMeasurement>();
		EulerAngleRotation initialBodyFrameOrientation = new EulerAngleRotation(0,0,0);	
		Calendar timeMeasured = Calendar.getInstance();
		
		int numberOfSamples = 100;
	    double degreeChange = Math.PI/2;
	    double degreeChangePerSample = degreeChange/numberOfSamples;
	    
	    for(int i = 0; i < numberOfSamples; i++){
	    	GyroscopeMeasurement measurement = new GyroscopeMeasurement(degreeChangePerSample, 0, 0, 1, timeMeasured);

	    	measurements.add(measurement);
	    }

		TestGyroscope testGyroscope = new TestGyroscope(measurements);
		RotationMonitor rotationMonitor = new RotationMonitor(testGyroscope, initialBodyFrameOrientation);
		rotationMonitor.AddSubscriber(new GenerationRotationChangeSubscriber(){

			@Override
			public void OrientationChanged(
					EulerAngleRotation newBodyFrameOrientation) {
				
				super.OrientationChanged(newBodyFrameOrientation);
				
				System.out.println(String.format("%f radians around x axis", newBodyFrameOrientation.getRadiansRotationAlongXAxis()));
				System.out.println(String.format("%f radians around y axis", newBodyFrameOrientation.getRadiansRotationAlongYAxis()));
				System.out.println(String.format("%f radians around z axis", newBodyFrameOrientation.getRadiansRotationAlongZAxis()));
			}
			
		});
		
		EulerAngleRotation lastReportedTotalBodyFrameRotation = null;
		for(int i = 0; i < numberOfSamples; i++){
			lastReportedTotalBodyFrameRotation = rotationMonitor.waitForNextRotationUpdate();
		}
		
		boolean testPassed = true;
		double tolerance = 0.1;
		
		testPassed = testPassed && (lastReportedTotalBodyFrameRotation != null)&&(Math.abs(lastReportedTotalBodyFrameRotation.getRadiansRotationAlongXAxis() - degreeChange) < tolerance);
		
		if(testPassed)
		{
			System.out.println("Test Passed!");
		}
		else{
			System.out.println("Test Failed!");
		}

	}
	
	private static void PrintRotation(EulerAngleRotation rotation){

		System.out.println(String.format("%f radians around x axis", rotation.getRadiansRotationAlongXAxis()));
		System.out.println(String.format("%f radians around y axis", rotation.getRadiansRotationAlongYAxis()));
		System.out.println(String.format("%f radians around z axis", rotation.getRadiansRotationAlongZAxis()));
		
	}
	
	private static void TestMeasurementCsv(){

		final String filename = "/home/death/Documents/CPE656/fullRotation_Nexus_7_09_18_15.csv";
		GyroscopeReader gyroscopeReader = new GyroscopeReader(filename);
		List<GyroscopeMeasurement> measurements = gyroscopeReader.getGyroscopeMeasurements();
		//EulerAngleRotation initialBodyFrameOrientation = new EulerAngleRotation(Math.PI/2,0,0);		
		EulerAngleRotation initialBodyFrameOrientation = new EulerAngleRotation(0,Math.PI/3, 0);

		final int numberOfSamples = measurements.size();
		TestGyroscope testGyroscope = new TestGyroscope(measurements);
		RotationMonitor rotationMonitor = new RotationMonitor(testGyroscope, initialBodyFrameOrientation);
		rotationMonitor.AddSubscriber(new GenerationRotationChangeSubscriber(){

			@Override
			public void OrientationChanged(
					EulerAngleRotation newBodyFrameOrientation) {
				
				super.OrientationChanged(newBodyFrameOrientation);
				
				PrintRotation(newBodyFrameOrientation);
			}
			
		});
		
		EulerAngleRotation lastReportedTotalBodyFrameRotation = null;
		for(int i = 0; i < numberOfSamples; i++){
			lastReportedTotalBodyFrameRotation = rotationMonitor.waitForNextRotationUpdate();
			
			//Adjust rotation to inertial frame
			EulerAngleRotation inertialFrameRotation = RotationUtilities.convertRotationFromBodyFrameToNedFrame(lastReportedTotalBodyFrameRotation, initialBodyFrameOrientation);
			
			System.out.println(String.format("%f radians around Earth x axis", inertialFrameRotation.getRadiansRotationAlongXAxis()));
			System.out.println(String.format("%f radians around Earth y axis", inertialFrameRotation.getRadiansRotationAlongYAxis()));
			System.out.println(String.format("%f radians around Earth z axis", inertialFrameRotation.getRadiansRotationAlongZAxis()));
			
			Quat4d initialRotationQuaternion = RotationUtilities.convertFromEulerAngleToQuaternion(initialBodyFrameOrientation.getRadiansRotationAlongXAxis(),
					initialBodyFrameOrientation.getRadiansRotationAlongYAxis(),
					initialBodyFrameOrientation.getRadiansRotationAlongZAxis());
			
			Quat4d bodyFrameRotationQuaternion = RotationUtilities.convertFromEulerAngleToQuaternion(inertialFrameRotation.getRadiansRotationAlongXAxis(),
					inertialFrameRotation.getRadiansRotationAlongYAxis(),
					inertialFrameRotation.getRadiansRotationAlongZAxis());
			
			Quat4d compositeRotationQuaternion = initialRotationQuaternion.multiply(bodyFrameRotationQuaternion);
			
			System.out.println("Estimate Earth orientation by attempting to use only quaternions");
			
			EulerAngleRotation compositeEulerAngleRotation = RotationUtilities.convertFromQuaternionToEulerAngle(compositeRotationQuaternion);
			
			PrintRotation(compositeEulerAngleRotation);
		}
		
		boolean testPassed = true;
		double tolerance = 0.1;
		
		
		//testPassed = testPassed && (lastReportedTotalBodyFrameRotation != null)&&(Math.abs(lastReportedTotalBodyFrameRotation.getRadiansRotationAlongXAxis() - degreeChange) < tolerance);
		
		if(testPassed)
		{
			System.out.println("Test Passed!");
		}
		else{
			System.out.println("Test Failed!");
		}

	}
	
	public static void TestQuaternionMultiplication(){
		Quat4d quat4d = new Quat4d(1,0,1,0);
		Quat4d quat4d2 = new Quat4d(1,0.5,0.5, 0.75);
		Quat4d expectedProduct = new Quat4d(0.5, 1.25, 1.5, 0.25);
		Quat4d prod = quat4d.multiply(quat4d2);
		
		boolean areEqual = prod.equals(expectedProduct);
		
		if(areEqual)
			System.out.println("Are Equal");
		else
			System.out.println("Are NOT Equal");		
	}
	
	
	private static void TestRotationOfVectorUsingEulerAngleDerivedRotationMatrix(){
		Matrix testVector = new Matrix(3,1);
		testVector.setValue(0, 0, 0);
		testVector.setValue(1, 0, 1);
		testVector.setValue(2, 0, 0);
		
		Matrix rotationMatrix = RotationUtilities.createRotationMatrix(new EulerAngleRotation(Math.PI/2, 0, 0));
		
		Matrix expectedRotatedVector = new Matrix(3, 1);
		expectedRotatedVector.setValue(0, 0, 0); //x
		expectedRotatedVector.setValue(1, 0, 0); //y
		expectedRotatedVector.setValue(2, 0, 1); //z
		
		//Assumptions:
		//Counter clockwise rotations are positive
		//Check using the NED Local Earth Reference Frame
		//Heading North is Positive
		//Heading East is Positive
		//Heading into the Earth is Positive (So going in the air is negative)
		
		Matrix rotatedVector =  rotationMatrix.multiply(testVector).round();
		
		Matrix.PrintMatrix(rotatedVector);
		
		boolean coordinateTransformationIsCorrect = rotatedVector.round().equals(expectedRotatedVector.round());
		
		if(coordinateTransformationIsCorrect){
			System.out.println("Transformation of coordinate axises are correct!");
		}
		else{
			System.out.println("ERROR: Transformation of coordinate axises are WRONG!");
		}
	}
	
	public static void Assert(boolean condition) throws Exception{
		if(!condition){
			
			throw new Exception("Test Failed");
		}
	}
	
	public static void TestLinearInterpolation(){
		
		boolean testPassed = true;
		double x[] = {0, 1};
		double y[] = {0, 1};
		
		try{
			double interpolatedY;
			
			//Test Beyond Upper Bound
			interpolatedY = MathUtilities.LinearInterpolate(x, y,2);
			Assert(interpolatedY == 2);
			
			//Test At Upper Bound
			interpolatedY = MathUtilities.LinearInterpolate(x, y,1);
			Assert(interpolatedY == 1);
			
			//Test At Lower Bound
			interpolatedY = MathUtilities.LinearInterpolate(x, y,0);
			Assert(interpolatedY == 0);
			
			//Test Below Lower Bound
			interpolatedY = MathUtilities.LinearInterpolate(x, y,-1);
			Assert(interpolatedY == -1);
			
			//Test Between Bounds
			interpolatedY = MathUtilities.LinearInterpolate(x, y,0.5);
			Assert(interpolatedY == 0.5);
			
			//Test Single Point Interpolate
			double x1[] = {0};
			double y1[] = {0};
			interpolatedY = MathUtilities.LinearInterpolate(x1, y1, 1);
			Assert(interpolatedY == 0);
		}
		catch (Exception e){
			testPassed = false;
		}
		
		
		if(testPassed){
			System.out.println("Test Passed");
		}
		else{
			System.out.println("Test Failed");
		}
	}
	
	private static void ReadTrainPositionsFromTrainNavigationService(TrainNavigationServiceInterface trainNavigationService){
		
		List<String> trains = null;
		try {
			trains = trainNavigationService.GetKnownTrainIdentifiers();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("List of known Train Ids:");
		
		for(String trainId : trains){
			System.out.println(trainId);
		}
		System.out.println("");
		
		String selectedTrain = trains.get(0);
		
		//Wait 2 seconds
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ValueUpdate<Coordinate> trainPosition = null;
		try {
			trainPosition = trainNavigationService.GetLastKnownPosition(selectedTrain);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(String.format("Current position of train %s: (%f, %f) at %s", selectedTrain, trainPosition.getValue().getX(),
				trainPosition.getValue().getY(), trainPosition.getTimeObserved().getTime()));
	
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//ReadTrainPositionsFromTrainNavigationService();
		
		/*HashSet<CommPortIdentifier> ports = getAvailableSerialPorts();
		
	    for(CommPortIdentifier p : ports){
	    	System.out.println(p.getName());
	    }*/
		
		TestMduMeasurementRead();
	}

}
