package com.traintrax.navigation.service.mdu;

import java.util.Calendar;

/**
 * Base class for any message exchanged with the MDU Protocol
 * @author Corey Sanders
 *
 */
public abstract class BaseMduMessage {
	
	/**
	 * Reports the offset in the MDU Protocol message header to find the byte
	 * that is the destination ID of the target machine of the message.
	 */
	public static final int MduProtocolDestIdOffset = 0;
	
	
	/**
	 * Reports the offset in the MDU Protocol message header to find the byte
	 * that is the source ID of the originator of the message.
	 */
	public static final int MduProtocolSrcIdOffset = 1;
	
	/**
	 * Reports the offset in the MDU Protocol message header to find the byte
	 * that is the ID that describes the type of message that is being sent.
	 */
	public static final int MduProtocolMessageTypeOffset = 2;
	
	public static final int MduProtocolTimestampOffset = 3;
	
	/**
	 * Reads the message type from the MDU packet
	 * 
	 * @param mduPacket
	 *            MDU packet
	 * @return value of the message type read from the packet
	 */
	protected static byte readMessageType(byte[] mduPacket) {

		return mduPacket[MduProtocolMessageTypeOffset];
	}
	
	protected static void writeMessageType(byte[] mduPacket, byte messageType) {

		mduPacket[MduProtocolDestIdOffset] = messageType;
	}
	
	/**
	 * Reads the source ID from the MDU packet
	 * 
	 * @param mduPacket
	 *            MDU packet
	 * @return value of the source ID read from the packet
	 */
	protected static byte readSourceId(byte[] mduPacket) {

		return mduPacket[MduProtocolSrcIdOffset];
	}
	
	protected static void writeSourceId(byte[] mduPacket, byte sourceId) {

		mduPacket[MduProtocolSrcIdOffset] = sourceId;
	}

	
	/**
	 * Reads destination ID from the MDU packet
	 * 
	 * @param mduPacket
	 *            MDU packet
	 * @return value of the destination ID read from the packet
	 */
	protected static byte readDestinationId(byte[] mduPacket) {

		return mduPacket[MduProtocolDestIdOffset];
	}
	
	protected static void writeDestinationId(byte[] mduPacket, byte destinationId) {

		mduPacket[MduProtocolDestIdOffset] = destinationId;
	}

	
	protected static String getTrainId(byte[] mduPacket){
		String trainId;
		
		trainId = getTrainId(mduPacket[MduProtocolSrcIdOffset]);
		
		return trainId;
	}
	
	protected static String getTrainId(byte trainIdByte){
		String trainId;
		
		trainId = String.format("%2X", trainIdByte);
		
		return trainId;
	}
	
	
	protected static Calendar decodeTimestamp(int numberOfMillisecondsSinceStartOfDay){
		Calendar timeMeasured = Calendar.getInstance();
		int year = timeMeasured.get(Calendar.YEAR);
		int month = timeMeasured.get(Calendar.MONTH);
		int date = timeMeasured.get(Calendar.DAY_OF_MONTH);

		timeMeasured.set(year, month, date, 0, 0, 0);
		timeMeasured.add(Calendar.MILLISECOND, numberOfMillisecondsSinceStartOfDay);
		
		return timeMeasured;
	}
	
	protected static int getNumberOfMillisecondsSinceStartOfDay(Calendar timestamp){
		Calendar timeMeasured = (Calendar) timestamp.clone();
		int year = timeMeasured.get(Calendar.YEAR);
		int month = timeMeasured.get(Calendar.MONTH);
		int date = timeMeasured.get(Calendar.DAY_OF_MONTH);

		timeMeasured.set(year, month, date, 0, 0, 0);
		
		long now = timestamp.getTimeInMillis();
		long startOfDay = timeMeasured.getTimeInMillis();
		long diff = (now - startOfDay);
		
		return (int) (diff&0xFFFFFFFF);
	}
	
	protected static byte[] encodeTimestamp(int timestamp){
		byte[] binaryTimestamp = new byte[4];
		int offset = 0;
		binaryTimestamp[offset] = (byte) ((timestamp >> 24)&0xFF);
		binaryTimestamp[offset+1] = (byte) ((timestamp >> 16)&0xFF);
		binaryTimestamp[offset+2] = (byte) ((timestamp >> 8)&0xFF);
		binaryTimestamp[offset+3] = (byte) ((timestamp)&0xFF);
		
		return binaryTimestamp;
	}

	/**
	 * Writes to the packet a timestamp (based on the number of milliseconds since start of the day
	 * @param mduPacket Buffer storing the packet to be made
	 * @param timestamp timestamp to add (based on # of milliseconds since start of the day)
	 */
	protected static void writeTimestamp(byte[] mduPacket, Calendar timestamp) {

		int timestampValue = getNumberOfMillisecondsSinceStartOfDay(timestamp);
		byte[] encodedTimestamp = encodeTimestamp(timestampValue);
		writeTimestamp(mduPacket, encodedTimestamp);
	}

	/**
	 * Writes to the packet a raw timestamp value
	 * @param mduPacket Buffer storing the packet being created
	 * @param timestamp raw timestamp value to use in the packet.
	 */
	protected static void writeTimestamp(byte[] mduPacket, byte[] timestamp) {

		mduPacket[MduProtocolTimestampOffset] = timestamp[0];
		mduPacket[MduProtocolTimestampOffset + 1] = timestamp[1];
		mduPacket[MduProtocolTimestampOffset + 2] = timestamp[2];
		mduPacket[MduProtocolTimestampOffset + 3] = timestamp[3];
	}

	/**
	 * Write the packets that mark the end of the packet
	 * @param mduPacket Buffer storing the packet being created
	 */
	protected static void writeTail(byte[] mduPacket){
		int lastByte = mduPacket.length - 1;
		
		//Write the final byte of the packet
		mduPacket[lastByte] = 0xA;
	}

}
