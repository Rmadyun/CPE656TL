package com.traintrax.navigation.service.mdu;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.traintrax.navigation.service.position.Acceleration;

/**
 * Class facilitates communication with Motion Detection Unit hardware
 * 
 * @author Corey Sanders
 *
 */
public class MotionDetectionUnit implements MotionDetectionUnitInterface {
	private final Queue<byte[]> mduPacketBufferQueue;
	private final Queue<AccelerometerMeasurement> collectedAccelerometerMeasurements;
	private final Queue<GyroscopeMeasurement> collectedGyroscopeMeasurements;
	private final Queue<RfidTagDetectedNotification> collectedRfidTagDetectionNotifications;
	private final MduCommunicationChannelInterface mduCommunicationChannel;
	private final Thread mduReadThread;
	private final Thread mduDecodeThread;
	private final Lock lock = new ReentrantLock();
	private final Condition notEmpty = lock.newCondition();
	private final MduProtocolParserInterface mduProtocolParser;
	
	//MDU Protocol fields
	private static final byte ImuReading = 0x03;
	private static final byte RfidReading = 0x04;
	/*private static final byte RoundTripTimeTestRequest =0x05;
	private static final byte RoundTripTimeTestResponse = 0x06;
	private static final byte Identification = 0x07;
	private static final byte TimeRequest = 0x08;
	private static final byte TimeResponse = 0x09; */
	
	private static final int ImuReadingPacketSize = 20;
	private static final int RfidReadingPacketSize = 13;
	
	/**
	 * Constructor
	 * 
	 * @param mduCommunicationChannel
	 *            Contact to the MDU
	 */
	public MotionDetectionUnit(MduCommunicationChannelInterface mduCommunicationChannel,
			MduProtocolParserInterface mduProtocolParser) {
		mduPacketBufferQueue = new ConcurrentLinkedQueue<byte[]>();
		collectedAccelerometerMeasurements = new ConcurrentLinkedQueue<AccelerometerMeasurement>();
		collectedGyroscopeMeasurements = new ConcurrentLinkedQueue<GyroscopeMeasurement>();
		collectedRfidTagDetectionNotifications = new ConcurrentLinkedQueue<RfidTagDetectedNotification>();

		this.mduCommunicationChannel = mduCommunicationChannel;
		this.mduProtocolParser = mduProtocolParser;

		mduReadThread = new Thread() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.lang.Thread#run()
			 */
			@Override
			public void run() {

				collectMduMessages();
				super.run();
			}

		};

		mduDecodeThread = new Thread() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.lang.Thread#run()
			 */
			@Override
			public void run() {

				decodeMduPackets();
				super.run();
			}

		};

		mduReadThread.start();
		mduDecodeThread.start();
		// TODO: Setup support to contact MDU
		// TODO: Setup support to collect measurements
	}

	/**
	 * Method reads data from the MDU and organizes them into messages for
	 * processing
	 */
	private void collectMduMessages() {
		InputStream mduInputStream = null;

		try {
			mduInputStream = mduCommunicationChannel.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			byte[] mduPacketBuffer = mduProtocolParser.getNextMduPacket(mduInputStream);

			// Enqueue MDU packet
			mduPacketBufferQueue.add(mduPacketBuffer);

			lock.lock();
			// Signal that packets are available
			notEmpty.signal();
			lock.unlock();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void decodeMduPackets() {
		Calendar lastGyroscopeMeasurement = null;
		Calendar lastAccelerometerMeasurement = null;
		
		do {
			
			lock.lock();
			// wait for packets to become available
			while (mduPacketBufferQueue.isEmpty()) {
				try {
					notEmpty.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			lock.unlock();
			
			//Read packet
			byte[] mduPacketBuffer = mduPacketBufferQueue.remove();
			
			//Decode packet
			byte[] mduPacket = this.mduProtocolParser.getPacketBytesStored(mduPacketBuffer);
			
			GyroscopeMeasurement gyroscopeMeasurement = TryDecodeGyroscopeMeasurement(mduPacket, lastGyroscopeMeasurement);
			if(gyroscopeMeasurement != null)
			{
				collectedGyroscopeMeasurements.add(gyroscopeMeasurement);
				lastGyroscopeMeasurement = gyroscopeMeasurement.getTimeMeasured();
			}
			
			AccelerometerMeasurement accelerometerMeasurement = TryDecodeAccelerometerMeasurement(mduPacket, lastAccelerometerMeasurement);
			if(accelerometerMeasurement != null)
			{
				collectedAccelerometerMeasurements.add(accelerometerMeasurement);
				lastAccelerometerMeasurement = accelerometerMeasurement.getTimeMeasured();
			}
			
			RfidTagDetectedNotification rfidTagDetectedNotifiation = TryDecodeRfidTagDetectedNotification(mduPacket);
			if(rfidTagDetectedNotifiation != null){
				collectedRfidTagDetectionNotifications.add(rfidTagDetectedNotifiation);
			}
		} while (true);
	}
	
	/**
	 * Reads the message type from the MDU packet
	 * @param mduPacket MDU packet
	 * @return value of the message type read from the packet
	 */
	private byte readMessageType(byte[] mduPacket){
		int MessageTypeOffset = 0;
		
		return mduPacket[MessageTypeOffset]; 
	}
	
	/**
	 * Attempts to read a gyroscope measurement from the MDU packet
	 * @param mduPacket MDU Packet
	 * @return Decoded Gyroscope measurement. Returns null if one cannot be decoded.
	 */
	private GyroscopeMeasurement TryDecodeGyroscopeMeasurement(byte[] mduPacket, Calendar timeOfLastMeasurement){
		GyroscopeMeasurement decodedMeasurement = null;
		byte messageType = readMessageType(mduPacket);
		int TimeStampOffset = 3;
		int GxOffset = 13;
		int GyOffset = 15;
		int GzOffset = 17;
		
		
		//Assuming that a MPU-6050 is the gyroscope that is being used by the
		//MDU. Also assuming that it is in 250 Degrees per second resolution
		double GScale = (250.0/32768.0)*(Math.PI/180.0);
		
		
		if(messageType == ImuReading && mduPacket.length == ImuReadingPacketSize){
            //Decode values	
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
		    
		    double timeBetweenMeasurements = (timeOfLastMeasurement == null) ? 0 : (timeOfLastMeasurement.getTimeInMillis() - timeMeasured.getTimeInMillis())/1000.0;    
			
			decodedMeasurement = new GyroscopeMeasurement(gx*GScale, gy*GScale, gz*GScale,timeBetweenMeasurements, timeMeasured); 
		}
		
		return decodedMeasurement;
	}
	
	/**
	 * Attempts to read an accelerometer measurement from the MDU packet
	 * @param mduPacket MDU Packet
	 * @return Decoded Accelerometer measurement. Returns null if one cannot be decoded.
	 */
	private AccelerometerMeasurement TryDecodeAccelerometerMeasurement(byte[] mduPacket, Calendar timeOfLastMeasurement){
		AccelerometerMeasurement decodedMeasurement = null;
		byte messageType = readMessageType(mduPacket);
		int TimeStampOffset = 3;
		int AxOffset = 7;
		int AyOffset = 9;
		int AzOffset = 11;
		//Assuming that a MPU-6050 is the accelerometer that is being used by the
		//MDU. Also assuming that it is in 2g resolution
		double AScale = (2.0/32768.0)*9.81;
		
		if(messageType == ImuReading && mduPacket.length == ImuReadingPacketSize){
            //Decode values	
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
		    
		    double timeBetweenMeasurements = (timeOfLastMeasurement == null) ? 0 : (timeOfLastMeasurement.getTimeInMillis() - timeMeasured.getTimeInMillis())/1000.0;    
			
			decodedMeasurement = new AccelerometerMeasurement(new Acceleration(ax*AScale, ay*AScale, az*AScale),timeBetweenMeasurements, timeMeasured); 
		}
		
		return decodedMeasurement;
	}
	
	/**
	 * Attempts to read an accelerometer measurement from the MDU packet
	 * @param mduPacket MDU Packet
	 * @return Decoded Accelerometer measurement. Returns null if one cannot be decoded.
	 */
	private RfidTagDetectedNotification TryDecodeRfidTagDetectedNotification(byte[] mduPacket){
		RfidTagDetectedNotification decodedMeasurement = null;
		byte messageType = readMessageType(mduPacket);
		int TimeStampOffset = 3;
		int TagOffset = 7;
		int RfidTagSize = 6;
		
		if(messageType == RfidReading && mduPacket.length == RfidReadingPacketSize){
            //Decode values	
			ByteBuffer bb = ByteBuffer.wrap(mduPacket);
			
			int timestamp = bb.getInt(TimeStampOffset);
		    StringBuilder sb = new StringBuilder();
		    for (int i= 0; i < RfidTagSize; i++) {
		    	byte b = bb.get(i+TagOffset);
		        sb.append(String.format("%02X", b));
		        
		        if(i != (RfidTagSize -1) )
		        {
		        	sb.append(":");
		        }
		    }
		    String rfidTagValue = sb.toString();
			
			Calendar timeMeasured = Calendar.getInstance();
		    int year = timeMeasured.get(Calendar.YEAR);
		    int month = timeMeasured.get(Calendar.MONTH);
		    int date = timeMeasured.get(Calendar.DAY_OF_MONTH);
		    
		    timeMeasured.set(year, month, date, 0, 0, 0);
		    timeMeasured.add(Calendar.MILLISECOND, timestamp);
			
			decodedMeasurement = new RfidTagDetectedNotification(rfidTagValue, timeMeasured); 
		}
		
		return decodedMeasurement;
	}

	/**
	 * Read collected accelerometer measurements
	 * 
	 * @return Collected accelerometer measurements
	 */
	public List<AccelerometerMeasurement> readCollectedAccelerometerMeasurements() {

		List<AccelerometerMeasurement> collected = new LinkedList<AccelerometerMeasurement>();
		AccelerometerMeasurement measurement = null;

		if (!collectedAccelerometerMeasurements.isEmpty()) {
			do {
				measurement = collectedAccelerometerMeasurements.poll();
				if (measurement != null) {
					collected.add(measurement);
				}
			} while (measurement != null);
		}

		return collected;
	}

	/**
	 * Read collected gyroscope measurements
	 * 
	 * @return Collected gyroscope measurements
	 */
	public List<GyroscopeMeasurement> readCollectedGyroscopeMeasurements() {
		List<GyroscopeMeasurement> collected = new LinkedList<GyroscopeMeasurement>();
		GyroscopeMeasurement measurement = null;

		if (!collectedGyroscopeMeasurements.isEmpty()) {
			do {
				measurement = collectedGyroscopeMeasurements.poll();
				if (measurement != null) {
					collected.add(measurement);
				}
			} while (measurement != null);
		}

		return collected;
	}

	/**
	 * Read collected RFID tag detection notifications
	 * 
	 * @return Collected RFID tag detection notifications
	 */
	public List<RfidTagDetectedNotification> readCollectedRfidTagDetectionNotifications() {
		List<RfidTagDetectedNotification> collected = new LinkedList<RfidTagDetectedNotification>();
		RfidTagDetectedNotification measurement = null;

		if (!collectedRfidTagDetectionNotifications.isEmpty()) {
			do {
				measurement = collectedRfidTagDetectionNotifications.poll();
				if (measurement != null) {
					collected.add(measurement);
				}
			} while (measurement != null);
		}

		return collected;
	}

}
