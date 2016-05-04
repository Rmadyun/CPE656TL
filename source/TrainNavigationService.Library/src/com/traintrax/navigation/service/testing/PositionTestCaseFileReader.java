package com.traintrax.navigation.service.testing;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.traintrax.navigation.service.ValueUpdate;
import com.traintrax.navigation.service.math.Matrix;
import com.traintrax.navigation.service.math.ThreeDimensionalSpaceVector;
import com.traintrax.navigation.service.mdu.RotationUtilities;
import com.traintrax.navigation.service.position.Acceleration;
import com.traintrax.navigation.service.position.AccelerometerMeasurement;
import com.traintrax.navigation.service.position.Coordinate;
import com.traintrax.navigation.service.position.GyroscopeMeasurement;
import com.traintrax.navigation.service.position.RfidTagDetectedNotification;
import com.traintrax.navigation.service.position.UnitConversionUtilities;
import com.traintrax.navigation.service.position.Velocity;
import com.traintrax.navigation.service.rotation.EulerAngleRotation;

/**
 * Class is responsible for importing test cases from
 * file
 * @author Corey Sanders
 *
 */
public class PositionTestCaseFileReader {
	
	//Test Case Header Columns (describes in general what to expect from the test case)
	private static final int TestCaseHeaderNumberOfColumns = 8;
	private static final int DescriptionColumnIndex = 0;
	private static final int InitialPositionXColumnIndex = 1;
	private static final int InitialPositionYColumnIndex = 2;
	private static final int InitialPositionZColumnIndex = 3;
	private static final int InitialOrientationXColumnIndex = 4;
	private static final int InitialOrientationYColumnIndex = 5;
	private static final int InitialOrientationZColumnIndex = 6;
	private static final int InitialSpeedColumnIndex = 7;
	
	//Test Sample Header Columns (describes a single measurement obtained)
	private static final int TestSampleNumberOfColumns = 12;
	private static final int AccXColumnIndex = 0;
	private static final int AccYColumnIndex = 1;
	private static final int AccZColumnIndex = 2;
	private static final int GyrXColumnIndex = 3;
	private static final int GyrYColumnIndex = 4;
	private static final int GyrZColumnIndex = 5;
	private static final int RfidTagColumnIndex = 6;
	private static final int TimestampColumnIndex = 7;
	private static final int DeltaTimeColumnIndex = 8;
	private static final int ExpectedPositionXColumnIndex = 9;
	private static final int ExpectedPositionYColumnIndex = 10;
	private static final int ExpectedPositionZColumnIndex = 11;

	
	/**
	 * Method reads a file and interprets its contents as a train position test case
	 * @param filename File to parse
	 * @return Test Case created from file content. Returns null if file cannot be parsed.
	 */
	public static PositionTestCase Read(String filename){
		PositionTestCase testCase = null;
		
		boolean setImu = true;
		boolean setRfid = true;

		//Describes how many rows at the beginning of the file
		//consisst of the test case header
		final int TestCaseHeaderNumberOfRows = 3;
		final int TestCaseHeaderContentRow = 1;
		
		//Describes the location of the first row that contains
		//Test sample data
		final int TestSampleRowOffset = TestCaseHeaderNumberOfRows;
		
		Reader in;
		try {
			in = new FileReader(filename);
			CSVParser parser = CSVFormat.EXCEL.parse(in);
			List<CSVRecord> records = parser.getRecords();
			
			//Parse header content
			CSVRecord headerContent = records.get(TestCaseHeaderContentRow); 
			
			String description = headerContent.get(DescriptionColumnIndex);
			Coordinate initialPosition;
			EulerAngleRotation initialOrientation;
			
			double initialPositionX = Double.parseDouble(headerContent.get(InitialPositionXColumnIndex));
			double initialPositionY = Double.parseDouble(headerContent.get(InitialPositionYColumnIndex));
			double initialPositionZ = Double.parseDouble(headerContent.get(InitialPositionZColumnIndex));
			
			initialPosition = new Coordinate(initialPositionX, initialPositionY, initialPositionZ);
			
			initialPosition = UnitConversionUtilities.convertFromInchesToMeters(initialPosition);
			
			double initialOrientationX = Double.parseDouble(headerContent.get(InitialOrientationXColumnIndex));
			double initialOrientationY = Double.parseDouble(headerContent.get(InitialOrientationYColumnIndex));
			double initialOrientationZ = Double.parseDouble(headerContent.get(InitialOrientationZColumnIndex));
			initialOrientation = new EulerAngleRotation(initialOrientationX, initialOrientationY, initialOrientationZ);
			
			double speed = Double.parseDouble(headerContent.get(InitialSpeedColumnIndex));
			
			
			//Parse Samples
			List<PositionTestSample> samples = new LinkedList<PositionTestSample>();
			for(int i = TestSampleRowOffset; i < records.size(); i++){
				CSVRecord record = records.get(i);
				
				PositionTestSample testSample = ReadSample(record, setImu, setRfid);
				if(testSample != null)
				{
					samples.add(testSample);
				}
			}
			
			//NOTE: This should work for 3-D Transforms. If this fails, default to 2D logic: x=speed*cos(initialOrientation.getRadiansRotationAlongZAxis()); y=speed*sin(initialOrientation.getRadiansRotationAlongZAxis());
			Matrix rotationMatrix = RotationUtilities.createRotationMatrix(initialOrientation);
			Velocity vectorRepresentationOfSpeed = new Velocity(speed,0,0);
			ThreeDimensionalSpaceVector velocity3DVector = RotationUtilities.changeToInertialFrame(Velocity.ToThreeDimensionalSpaceVector(vectorRepresentationOfSpeed), rotationMatrix);
			Velocity initialVelocity = new Velocity(velocity3DVector);
			
			testCase = new PositionTestCase(description, initialPosition, initialOrientation, initialVelocity, samples);
			
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return testCase;
	
	}
	
	/**
	 * Method does the work of decoding a single test sample
	 * @param record Record to decode
	 * @setImu Assign true for IMU measurements to be included as samples; otherwise they will not be used
	 * @setRfid Assign true for RFID position updates to be included as samples; otherwise they will not be used 
	 * @return Decoded test sample.
	 */
	private static PositionTestSample ReadSample(CSVRecord record, boolean setImu, boolean setRfid){
		
		final String DefaultTrainId = "1";
		PositionTestSample sample = new PositionTestSample();
		
		double timestampInSeconds = Double.parseDouble(record.get(TimestampColumnIndex));
		double deltaTimeInSeconds = Double.parseDouble(record.get(DeltaTimeColumnIndex));
		
		//Assigning to epoch since we don't really are about the actual date.
		Calendar measurementTime = Calendar.getInstance();
		int year = measurementTime.get(Calendar.YEAR);
		int month = measurementTime.get(Calendar.MONTH);
		int date = measurementTime.get(Calendar.DAY_OF_MONTH);

		measurementTime.set(year, month, date, 0, 0, 0);
		measurementTime.add(Calendar.MILLISECOND, (int) (timestampInSeconds*1000));
		//measurementTime.setTimeInMillis((long) (timestampInSeconds*1000));
		
		String accXString = record.get(AccXColumnIndex).trim();
		String accYString = record.get(AccYColumnIndex).trim();
		String accZString = record.get(AccZColumnIndex).trim();
		
		if(!IsNullOrEmpty(accXString)&&!IsNullOrEmpty(accYString)&&!IsNullOrEmpty(accZString)){
			
			double accX = Double.parseDouble(accXString);
			double accY = Double.parseDouble(accYString);
			double accZ = Double.parseDouble(accZString);
			
			if(setImu)
			{
		        sample.setAccelerometerMeasurement(new AccelerometerMeasurement(DefaultTrainId, new Acceleration(accX, accY, accZ), deltaTimeInSeconds, measurementTime));
			}
		}
		
		String gyrXString = record.get(GyrXColumnIndex);
		String gyrYString = record.get(GyrYColumnIndex);
		String gyrZString = record.get(GyrZColumnIndex);
		
		if(!IsNullOrEmpty(gyrXString)&&!IsNullOrEmpty(gyrYString)&&!IsNullOrEmpty(gyrZString)){
			
			double gyrX = Double.parseDouble(gyrXString);
			double gyrY = Double.parseDouble(gyrYString);
			double gyrZ = Double.parseDouble(gyrZString);
			
			if(setImu)
			{
			    sample.setGyroscopeMeasurement(new GyroscopeMeasurement(DefaultTrainId, gyrX, gyrY, gyrZ, deltaTimeInSeconds, measurementTime));
			}
		}
		
		String rfIdTagValue = record.get(RfidTagColumnIndex).trim();
	
		if(IsNullOrEmpty(record.get(ExpectedPositionXColumnIndex)) || IsNullOrEmpty(record.get(ExpectedPositionYColumnIndex)) || IsNullOrEmpty(record.get(ExpectedPositionZColumnIndex)))
		{
			sample.setExpectedPosition(null);
		}
		else
		{
			String expectedPositionXString = record.get(ExpectedPositionXColumnIndex);
			double expectedPositionX = Double.parseDouble(expectedPositionXString);
			
			String expectedPositionYString = record.get(ExpectedPositionYColumnIndex);
			double expectedPositionY = Double.parseDouble(expectedPositionYString);
					
		    String expectedPositionZString = record.get(ExpectedPositionZColumnIndex);
			double expectedPositionZ = Double.parseDouble(expectedPositionZString);
			sample.setExpectedPosition(new ValueUpdate<Coordinate>(UnitConversionUtilities.convertFromInchesToMeters(new Coordinate(expectedPositionX, expectedPositionY, expectedPositionZ)), measurementTime));
			
			if(!IsNullOrEmpty(rfIdTagValue)&&setRfid)
			{
			    sample.setRfidTagPosition(new ValueUpdate<Coordinate>(UnitConversionUtilities.convertFromInchesToMeters(new Coordinate(expectedPositionX, expectedPositionY, expectedPositionZ)), measurementTime));
			}
		}

		if(IsNullOrEmpty(rfIdTagValue)||!setRfid)
		{
			sample.setRfidTagDetectedNotification(null);
		}
		else{
			sample.setRfidTagDetectedNotification(new RfidTagDetectedNotification(DefaultTrainId, rfIdTagValue, measurementTime));
		}
		
		return sample;
	}
	
	/**
	 * Reports if a string has a null value or an empty value
	 * @param string String to evaluate
	 * @return Returns true if string has a null or empty value; otherwise returns false
	 */
	private static boolean IsNullOrEmpty(String string)
	{
		boolean isNull = (string == null);
		boolean isEmpty = (string.isEmpty());
		
		return (isNull || isEmpty);
	}

}
