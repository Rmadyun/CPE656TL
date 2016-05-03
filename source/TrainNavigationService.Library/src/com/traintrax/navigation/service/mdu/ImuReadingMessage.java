package com.traintrax.navigation.service.mdu;

import java.nio.ByteBuffer;
import java.util.Calendar;

import com.traintrax.navigation.service.position.Acceleration;
import com.traintrax.navigation.service.position.AccelerometerMeasurement;
import com.traintrax.navigation.service.position.GyroscopeMeasurement;

/**
 * MDU Protocol message for reporting a received rfid tag notification
 * 
 * @author Corey Sanders
 *
 */
public class ImuReadingMessage extends BaseMduMessage {

	/**
	 * MDU Protocol Message Type
	 */
	public static final byte ImuReading = 0x03;

	/**
	 * Fixed size of the packet
	 */
	public static final int ImuReadingPacketSize = 20;

	private final byte destination;
	private final byte source;
	private final byte messageType;

	private final AccelerometerMeasurement accelerometerMeasurement;
	private final GyroscopeMeasurement gyroscopeMeasurement;

	/**
	 * Constructor
	 * 
	 * @param destination
	 *            ID of the destination of the message
	 * @param source
	 *            Originator of the message
	 * @param accelerometerMeasurement
	 *            Accelerometer measurement
	 * @param gyroscopeMeasurement
	 *            Gyroscope measurement
	 */
	public ImuReadingMessage(byte destination, byte source, AccelerometerMeasurement accelerometerMeasurement,
			GyroscopeMeasurement gyroscopeMeasurement) {
		super();
		this.destination = destination;
		this.source = source;
		this.messageType = ImuReading;
		this.accelerometerMeasurement = accelerometerMeasurement;
		this.gyroscopeMeasurement = gyroscopeMeasurement;
	}

	/**
	 * Retrieves the destination ID for the message. Indicates the unique ID for
	 * the machine this message is intended for.
	 * 
	 * @return the destination ID for the message
	 */
	public byte getDestination() {
		return destination;
	}

	/**
	 * Retrieves the unique ID associated with the machine that sent the message
	 * 
	 * @return the unique ID associated with the machine that sent the message
	 */
	public byte getSource() {
		return source;
	}

	/**
	 * Information that characterizes the type of message sent
	 * 
	 * @return ID describing the type of message sent
	 */
	public byte getMessageType() {
		return messageType;
	}

	/**
	 * @return the accelerometerMeasurement
	 */
	public AccelerometerMeasurement getAccelerometerMeasurement() {
		return accelerometerMeasurement;
	}

	/**
	 * @return the gyroscopeMeasurement
	 */
	public GyroscopeMeasurement getGyroscopeMeasurement() {
		return gyroscopeMeasurement;
	}

	/**
	 * Attempts to read a gyroscope measurement from the MDU packet
	 * 
	 * @param mduPacket
	 *            MDU Packet
	 * @return Decoded Gyroscope measurement. Returns null if one cannot be
	 *         decoded.
	 */
	private static GyroscopeMeasurement TryDecodeGyroscopeMeasurement(byte[] mduPacket,
			Calendar timeOfLastMeasurement) {
		GyroscopeMeasurement decodedMeasurement = null;
		byte messageType = readMessageType(mduPacket);
		int TimeStampOffset = 3;
		int GxOffset = 13;
		int GyOffset = 15;
		int GzOffset = 17;

		// Assuming that a MPU-6050 is the gyroscope that is being used by the
		// MDU. Also assuming that it is in 250 Degrees per second resolution
		double GScale = (250.0 / 32768.0) * (Math.PI / 180.0);

		if (messageType == ImuReading && mduPacket.length == ImuReadingPacketSize) {
			// Decode values
			ByteBuffer bb = ByteBuffer.wrap(mduPacket);

			int timestamp = bb.getInt(TimeStampOffset);
			short gx = bb.getShort(GxOffset);
			short gy = bb.getShort(GyOffset);
			short gz = bb.getShort(GzOffset);

			Calendar timeMeasured = Calendar.getInstance();
			int year = timeMeasured.get(Calendar.YEAR);
			int month = timeMeasured.get(Calendar.MONTH);
			int date = timeMeasured.get(Calendar.DAY_OF_MONTH);

			timeMeasured.set(year, month, date, 0, 0, 0);
			timeMeasured.add(Calendar.MILLISECOND, timestamp);

			double timeBetweenMeasurements = (timeOfLastMeasurement == null) ? 0
					: (timeOfLastMeasurement.getTimeInMillis() - timeMeasured.getTimeInMillis()) / 1000.0;

			String trainId = getTrainId(mduPacket);

			decodedMeasurement = new GyroscopeMeasurement(trainId, gx * GScale, gy * GScale, gz * GScale,
					timeBetweenMeasurements, timeMeasured);
		}

		return decodedMeasurement;
	}

	/**
	 * Attempts to read an accelerometer measurement from the MDU packet
	 * 
	 * @param mduPacket
	 *            MDU Packet
	 * @return Decoded Accelerometer measurement. Returns null if one cannot be
	 *         decoded.
	 */
	private static AccelerometerMeasurement TryDecodeAccelerometerMeasurement(byte[] mduPacket,
			Calendar timeOfLastMeasurement) {
		AccelerometerMeasurement decodedMeasurement = null;
		byte messageType = readMessageType(mduPacket);
		int TimeStampOffset = 3;
		int AxOffset = 7;
		int AyOffset = 9;
		int AzOffset = 11;
		// Assuming that a MPU-6050 is the accelerometer that is being used by
		// the
		// MDU. Also assuming that it is in 2g resolution
		double AScale = (2.0 / 32768.0) * 9.81;

		if (messageType == ImuReading && mduPacket.length == ImuReadingPacketSize) {
			// Decode values
			ByteBuffer bb = ByteBuffer.wrap(mduPacket);

			int timestamp = bb.getInt(TimeStampOffset);
			short ax = bb.getShort(AxOffset);
			short ay = bb.getShort(AyOffset);
			short az = bb.getShort(AzOffset);

			Calendar timeMeasured = Calendar.getInstance();
			int year = timeMeasured.get(Calendar.YEAR);
			int month = timeMeasured.get(Calendar.MONTH);
			int date = timeMeasured.get(Calendar.DAY_OF_MONTH);

			timeMeasured.set(year, month, date, 0, 0, 0);
			timeMeasured.add(Calendar.MILLISECOND, timestamp);

			double timeBetweenMeasurements = (timeOfLastMeasurement == null) ? 0
					: (timeOfLastMeasurement.getTimeInMillis() - timeMeasured.getTimeInMillis()) / 1000.0;

			String trainId = getTrainId(mduPacket);

			decodedMeasurement = new AccelerometerMeasurement(trainId,
					new Acceleration(ax * AScale, ay * AScale, az * AScale), timeBetweenMeasurements, timeMeasured);
		}

		return decodedMeasurement;
	}

	/**
	 * Attempts to read an accelerometer measurement from the MDU packet
	 * 
	 * @param mduPacket
	 *            MDU Packet
	 * @return Decoded Accelerometer measurement. Returns null if one cannot be
	 *         decoded.
	 */
	public static ImuReadingMessage TryDecodeRfidTagDetectedNotification(byte[] mduPacket,
			Calendar timeOfLastMeasurement) {
		ImuReadingMessage decodedMeasurement = null;
		byte sourceId = readSourceId(mduPacket);
		byte destId = readDestinationId(mduPacket);
		GyroscopeMeasurement gyroscopeMeasurement;
		AccelerometerMeasurement accelerometerMeasurement;

		//NOTE: Assuming that the verification of the message type is handled from the
		//try decode measurement private functions.
		accelerometerMeasurement = TryDecodeAccelerometerMeasurement(mduPacket, timeOfLastMeasurement);
		gyroscopeMeasurement = TryDecodeGyroscopeMeasurement(mduPacket, timeOfLastMeasurement);

		if ((accelerometerMeasurement != null) && (gyroscopeMeasurement != null)) {
			decodedMeasurement = new ImuReadingMessage(destId, sourceId, accelerometerMeasurement,
					gyroscopeMeasurement);
		}

		return decodedMeasurement;
	}

	/**
	 * Encodes the RFID Detected message into a MDU protocol packet
	 * 
	 * @param message
	 *            Message to encode
	 * @return Encoded MDU Packet
	 */
	public static byte[] Encode(ImuReadingMessage message) {
		byte[] mduPacket = new byte[ImuReadingPacketSize];
		int AxOffset = 7;
		int AyOffset = 9;
		int AzOffset = 11;
		int GxOffset = 13;
		int GyOffset = 15;
		int GzOffset = 17;

		// Assuming that a MPU-6050 is the accelerometer that is being used by
		// the
		// MDU. Also assuming that it is in 2g resolution
		double AScale = (2.0 / 32768.0) * 9.81;
		
		// Assuming that a MPU-6050 is the gyroscope that is being used by the
		// MDU. Also assuming that it is in 250 Degrees per second resolution
		double GScale = (250.0 / 32768.0) * (Math.PI / 180.0);

		writeDestinationId(mduPacket, message.destination);
		writeSourceId(mduPacket, message.source);
		writeMessageType(mduPacket, ImuReading);
		writeTimestamp(mduPacket, message.accelerometerMeasurement.getTimeMeasured());
		
		//Create IMU values
		short ax = (short) (message.accelerometerMeasurement.getAcceleration().getMetersPerSecondSquaredAlongXAxis()/AScale);
		short ay = (short) (message.accelerometerMeasurement.getAcceleration().getMetersPerSecondSquaredAlongYAxis()/AScale);
		short az = (short) (message.accelerometerMeasurement.getAcceleration().getMetersPerSecondSquaredAlongZAxis()/AScale);
		short gx = (short) (message.gyroscopeMeasurement.getRadiansRotationPerSecondAlongXAxis()/GScale);
		short gy = (short) (message.gyroscopeMeasurement.getRadiansRotationPerSecondAlongYAxis()/GScale);
		short gz = (short) (message.gyroscopeMeasurement.getRadiansRotationPerSecondAlongZAxis()/GScale);
		
		//Write remaining values
		byte[] tempBuffer;
		
		tempBuffer = encodeShort(ax);
		mduPacket[AxOffset] = tempBuffer[0];
		mduPacket[AxOffset + 1] = tempBuffer[1];
		
		tempBuffer = encodeShort(ay);
		mduPacket[AyOffset] = tempBuffer[0];
		mduPacket[AyOffset + 1] = tempBuffer[1];
		
		tempBuffer = encodeShort(az);
		mduPacket[AzOffset] = tempBuffer[0];
		mduPacket[AzOffset + 1] = tempBuffer[1];
		
		tempBuffer = encodeShort(gx);
		mduPacket[GxOffset] = tempBuffer[0];
		mduPacket[GxOffset + 1] = tempBuffer[1];
		
		tempBuffer = encodeShort(gy);
		mduPacket[GyOffset] = tempBuffer[0];
		mduPacket[GyOffset + 1] = tempBuffer[1];
		
		tempBuffer = encodeShort(gz);
		mduPacket[GzOffset] = tempBuffer[0];
		mduPacket[GzOffset + 1] = tempBuffer[1];

		writeTail(mduPacket);

		return mduPacket;
	}

}
