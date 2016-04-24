package com.traintrax.navigation.service.mdu;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.traintrax.navigation.service.position.Acceleration;
import com.traintrax.navigation.service.position.AccelerometerMeasurement;
import com.traintrax.navigation.service.position.GyroscopeMeasurement;
import com.traintrax.navigation.service.position.RfidTagDetectedNotification;
import com.traintrax.navigation.service.position.Train;

/**
 * Class facilitates communication with Motion Detection Unit hardware
 * 
 * @author Corey Sanders
 *
 */
public class MotionDetectionUnit implements MotionDetectionUnitInterface {
	private final Queue<byte[]> mduPacketBufferQueue;
	private final ConcurrentHashMap<String, Train> trainIdToTrainLut;
	private final MduCommunicationChannelInterface mduCommunicationChannel;
	private final Thread mduReadThread;
	private final Thread mduDecodeThread;
	private final Lock lock = new ReentrantLock();
	private final Condition notEmpty = lock.newCondition();
	private final MduProtocolParserInterface mduProtocolParser;
	private final MduCallbackInterface mduCallback;

	// MDU Protocol fields
	private static final byte ImuReading = 0x03;
	private static final byte RfidReading = 0x04;
	private static final byte RoundTripTimeTestRequest = 0x05;
	private static final byte RoundTripTimeTestResponse = 0x06;
	private static final byte Identification = 0x07;
	private static final byte TimeRequest = 0x08;
	private static final byte TimeResponse = 0x09;

	private static final int ImuReadingPacketSize = 20;
	private static final int RfidReadingPacketSize = 13;
	private static final int IdentificationPacketSize = 4;

	/**
	 * Reports the offset in the MDU Protocol message header to find the byte
	 * that is the source ID of the originator of the message.
	 */
	private static final int MduProtocolSrcIdOffset = 1;
	
	/**
	 * Constructor
	 * 
	 * @param mduCommunicationChannel Contact to the MDU
	 * @param mduProcotolParser Does the logic for separating MDU Packets from the
	 * byte stream.
	 */
	public MotionDetectionUnit(MduCommunicationChannelInterface mduCommunicationChannel,
			MduProtocolParserInterface mduProtocolParser) {
		this(mduCommunicationChannel, mduProtocolParser, null);
	}

	/**
	 * Constructor
	 * 
	 * @param mduCommunicationChannel Contact to the MDU
	 * @param mduProcotolParser Does the logic for separating MDU Packets from the
	 * byte stream.
	 * @param mduCallback Notifies an agent about internal changes in the MDU.
	 */
	public MotionDetectionUnit(MduCommunicationChannelInterface mduCommunicationChannel,
			MduProtocolParserInterface mduProtocolParser, MduCallbackInterface mduCallback) {
		mduPacketBufferQueue = new ConcurrentLinkedQueue<byte[]>();
		trainIdToTrainLut = new ConcurrentHashMap<String, Train>();

		this.mduCommunicationChannel = mduCommunicationChannel;
		this.mduProtocolParser = mduProtocolParser;
		this.mduCallback = mduCallback;

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
	}

	/**
	 * Method reads data from the MDU and organizes them into messages for
	 * processing
	 */
	private void collectMduMessages() {
		do {
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
				notEmpty.signalAll();
				lock.unlock();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} while (true);
	}

	/**
	 * Method is responsible for retrieving the Train object associated with the
	 * provided train ID. It checks to see if a train is already being tracked.
	 * If it is not, then the train is added to the map tracking trains.
	 * 
	 * @param trainId
	 *            Unique ID for train from MDU Protocol message. This is
	 *            typically the source field of the message header.
	 */
	private Train FetchTrain(byte rawTrainId) {
		String trainId = String.format("%02X", rawTrainId);

		Train train = (Train) this.trainIdToTrainLut.get(trainId);

		// Check if train already exists.
		if (train == null) {

			// Adds trains train if it doesn't already exist.
			Train newTrain = new Train(trainId);
			Train previousTrain = trainIdToTrainLut.putIfAbsent(trainId, newTrain);

			if (previousTrain == null) {
				train = newTrain;
				
				if(mduCallback != null){
					//Notify that a new train has been added.
					mduCallback.TrainAdded(newTrain);
				}
			} else {
				train = previousTrain;
			}
		}

		return train;
	}

	private void decodeMduPackets() {

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

			// Read packet
			byte[] mduPacketBuffer = mduPacketBufferQueue.remove();

			// Decode packet
			byte[] mduPacket = this.mduProtocolParser.getPacketBytesStored(mduPacketBuffer);

			System.out.println("Decoding MDU Packet");
			Train train = FetchTrain(mduPacket[MduProtocolSrcIdOffset]);

			GyroscopeMeasurement gyroscopeMeasurement = TryDecodeGyroscopeMeasurement(mduPacket,
					train.getLastGyroscopeMeasurement());
			if (gyroscopeMeasurement != null) {
				System.out.println("GYR Measurement Received");
				train.add(gyroscopeMeasurement);
			}

			AccelerometerMeasurement accelerometerMeasurement = TryDecodeAccelerometerMeasurement(mduPacket,
					train.getLastAccelerometerMeasurement());
			if (accelerometerMeasurement != null) {
				System.out.println("ACC Measurement Received");
				train.add(accelerometerMeasurement);
			}

			RfidTagDetectedNotification rfidTagDetectedNotification = TryDecodeRfidTagDetectedNotification(mduPacket);
			if (rfidTagDetectedNotification != null) {
				System.out.println("RFID Tag Detection Notification Received");
				train.add(rfidTagDetectedNotification);
			}

			TrainIdentificationMessage trainIdentificationMessage = TryDecodeTrainIdentification(mduPacket);

			if (trainIdentificationMessage != null) {
				System.out.println("Train Identification Request Received");
				// Send train identification reply

				try {
					OutputStream outputStream = mduCommunicationChannel.getOutputStream();

					TrainIdentificationReplyMessage responseIdentificationMessage = new TrainIdentificationReplyMessage(
							trainIdentificationMessage);
					byte[] responsePacket = this.EncodeTrainIdentificationReply(responseIdentificationMessage);

					outputStream.write(responsePacket);
					outputStream.flush();
					System.out.println("Train Identification Reply Sent");

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			RoundTripTimeRequestMessage roundTripTimeRequestMessage = TryDecodeRoundTripTimeRequest(mduPacket);

			if (roundTripTimeRequestMessage != null) {
				System.out.println("RTT Request Received");

				// Send train identification reply

				try {
					OutputStream outputStream = mduCommunicationChannel.getOutputStream();

					RoundTripTimeResponseMessage responseIdentificationMessage = new RoundTripTimeResponseMessage(
							roundTripTimeRequestMessage);
					byte[] responsePacket = RoundTripTimeResponseMessage
							.EncodeRoundTripTimeReply(responseIdentificationMessage);

					outputStream.write(responsePacket);
					outputStream.flush();
					System.out.println("Round Trip Time Reply Sent");

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			TimeSyncRequestMessage timeSyncRequestMessage = TimeSyncRequestMessage.TryDecodeTimeSyncRequest(mduPacket);

			if (timeSyncRequestMessage != null) {
				System.out.println("Time Sync Request Received");

				// Send train identification reply

				try {
					OutputStream outputStream = mduCommunicationChannel.getOutputStream();

					TimeSyncResponseMessage responseIdentificationMessage = new TimeSyncResponseMessage(
							timeSyncRequestMessage, Calendar.getInstance());
					byte[] responsePacket = TimeSyncResponseMessage.EncodeTimeSyncReply(responseIdentificationMessage);

					outputStream.write(responsePacket);
					outputStream.flush();
					System.out.println("Time Sync Reply Sent");

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} while (true);
	}

	/**
	 * Reads the message type from the MDU packet
	 * 
	 * @param mduPacket
	 *            MDU packet
	 * @return value of the message type read from the packet
	 */
	private byte readMessageType(byte[] mduPacket) {
		int MessageTypeOffset = 2;

		return mduPacket[MessageTypeOffset];
	}

	/**
	 * Attempts to read a gyroscope measurement from the MDU packet
	 * 
	 * @param mduPacket
	 *            MDU Packet
	 * @return Decoded Gyroscope measurement. Returns null if one cannot be
	 *         decoded.
	 */
	private GyroscopeMeasurement TryDecodeGyroscopeMeasurement(byte[] mduPacket, Calendar timeOfLastMeasurement) {
		GyroscopeMeasurement decodedMeasurement = null;
		byte messageType = readMessageType(mduPacket);
		int TimeStampOffset = 3;
		int GxOffset = 13;
		int GyOffset = 15;
		int GzOffset = 17;
		int SrcOffset = 1;

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

			String trainId = String.format("%02X", mduPacket[SrcOffset]);

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
	private AccelerometerMeasurement TryDecodeAccelerometerMeasurement(byte[] mduPacket,
			Calendar timeOfLastMeasurement) {
		AccelerometerMeasurement decodedMeasurement = null;
		byte messageType = readMessageType(mduPacket);
		int SrcOffset = 1;
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

			String trainId = String.format("%02X", mduPacket[SrcOffset]);

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
	private RfidTagDetectedNotification TryDecodeRfidTagDetectedNotification(byte[] mduPacket) {
		RfidTagDetectedNotification decodedMeasurement = null;
		byte messageType = readMessageType(mduPacket);
		int SrcOffset = 1;
		int TimeStampOffset = 3;
		int TagOffset = 7;
		int RfidTagSize = 5;

		if (messageType == RfidReading && mduPacket.length == RfidReadingPacketSize) {
			// Decode values
			ByteBuffer bb = ByteBuffer.wrap(mduPacket);

			int timestamp = bb.getInt(TimeStampOffset);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < RfidTagSize; i++) {
				byte b = bb.get(i + TagOffset);
				sb.append(String.format("%02X", b));

				if (i != (RfidTagSize - 1)) {
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

			String trainId = String.format("%02X", mduPacket[SrcOffset]);

			decodedMeasurement = new RfidTagDetectedNotification(trainId, rfidTagValue, timeMeasured);
		}

		return decodedMeasurement;
	}

	/**
	 * Attempts to read a train identification message from the MDU packet
	 * 
	 * @param mduPacket
	 *            MDU Packet
	 * @return Decoded Train Identification message. Returns null if one cannot
	 *         be decoded.
	 */
	private TrainIdentificationMessage TryDecodeTrainIdentification(byte[] mduPacket) {
		TrainIdentificationMessage trainIdentificationMessage = null;
		byte messageType = readMessageType(mduPacket);
		int DestinationOffset = 0;
		int SourceOffset = 1;

		if (messageType == Identification && mduPacket.length == IdentificationPacketSize) {
			// Decode values

			byte destination = mduPacket[DestinationOffset];
			byte source = mduPacket[SourceOffset];

			trainIdentificationMessage = new TrainIdentificationMessage(destination, source);
		}

		return trainIdentificationMessage;
	}

	/**
	 * Encodes the train identification message into a MDU protocol packet
	 * 
	 * @param trainIdentificationReplyMessage
	 *            Message to encode
	 * @return MDU Packet
	 */
	private byte[] EncodeTrainIdentificationReply(TrainIdentificationReplyMessage trainIdentificationReplyMessage) {
		byte[] encodedMessage = new byte[TrainIdentificationReplyMessage.IdentificationReplyPacketSize];

		int DestinationOffset = 0;
		int SourceOffset = 1;
		int MessageTypeOffset = 2;
		int MessageTailOffset = 3;

		encodedMessage[DestinationOffset] = trainIdentificationReplyMessage.getDestination();
		encodedMessage[SourceOffset] = trainIdentificationReplyMessage.getSource();
		encodedMessage[MessageTypeOffset] = trainIdentificationReplyMessage.getMessageType();
		encodedMessage[MessageTailOffset] = 0x0A;

		return encodedMessage;
	}

	/**
	 * Attempts to read a round trip time request message from the MDU packet
	 * 
	 * @param mduPacket
	 *            MDU Packet
	 * @return Decoded Round trip time request message. Returns null if one
	 *         cannot be decoded.
	 */
	private RoundTripTimeRequestMessage TryDecodeRoundTripTimeRequest(byte[] mduPacket) {
		RoundTripTimeRequestMessage roundTripTimeRequestMessage = null;
		byte messageType = readMessageType(mduPacket);
		int DestinationOffset = 0;
		int SourceOffset = 1;

		if (messageType == RoundTripTimeRequestMessage.RoundtripTimeRequest
				&& mduPacket.length == RoundTripTimeRequestMessage.RoundtripTimeRequestPacketSize) {
			// Decode values

			byte destination = mduPacket[DestinationOffset];
			byte source = mduPacket[SourceOffset];

			roundTripTimeRequestMessage = new RoundTripTimeRequestMessage(destination, source);
		}

		return roundTripTimeRequestMessage;
	}

	/**
	 * Read collected accelerometer measurements
	 * 
	 * @return Collected accelerometer measurements
	 */
	public List<AccelerometerMeasurement> readCollectedAccelerometerMeasurements() {

		List<AccelerometerMeasurement> collected = new LinkedList<AccelerometerMeasurement>();

		for (Train train : this.trainIdToTrainLut.values()) {
			collected.addAll(train.readCollectedAccelerometerMeasurements());
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

		for (Train train : this.trainIdToTrainLut.values()) {
			collected.addAll(train.readCollectedGyroscopeMeasurements());
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

		for (Train train : this.trainIdToTrainLut.values()) {
			collected.addAll(train.readCollectedRfidTagDetectionNotifications());
		}

		return collected;
	}

	/**
	 * Retrieves a list of all of the train that have
	 * been reported on the MDU channel being used.
	 * @return a list of all of the train that have
	 * been reported on the MDU channel being used.
	 */
	@Override
	public Collection<Train> getAssociatedTrains() {
		return this.trainIdToTrainLut.values();
	}

}
