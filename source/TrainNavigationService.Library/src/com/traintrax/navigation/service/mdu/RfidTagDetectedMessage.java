package com.traintrax.navigation.service.mdu;

import java.nio.ByteBuffer;
import java.util.Calendar;

import javax.xml.bind.DatatypeConverter;

import com.traintrax.navigation.service.position.RfidTagDetectedNotification;

/**
 * MDU Protocol message for reporting a received rfid tag notification
 * @author Corey Sanders
 *
 */
public class RfidTagDetectedMessage extends BaseMduMessage {
	
	/**
	 * MDU Protocol Message Type
	 */
	public static final byte RfidReading = 0x04;

	/**
	 * Fixed size of the packet
	 */
	public static final int RfidReadingPacketSize = 13;
	
	private final byte destination;
	private final byte source;
	private final byte messageType;
	
	private final RfidTagDetectedNotification rfidTagDetectedNotification;
	
	
	/**
	 * Constructor
	 * @param destination ID of the destination of the message
	 * @param source Originator of the message
	 * @param rfidTagDetectedNotification RFID Tag detected notification
	 */
	public RfidTagDetectedMessage(byte destination, byte source, RfidTagDetectedNotification rfidTagDetectedNotification) {
		super();
		this.destination = destination;
		this.source = source;
		this.messageType = RfidReading;
		this.rfidTagDetectedNotification = rfidTagDetectedNotification;
	}

	/**
	 * Retrieves the destination ID for the message.
	 * Indicates the unique ID for the machine this message is intended for.
	 * @return the destination ID for the message
	 */
	public byte getDestination() {
		return destination;
	}

	/**
	 * Retrieves the unique ID associated with the machine that
	 * sent the message
	 * @return the unique ID associated with the machine that sent the message
	 */
	public byte getSource() {
		return source;
	}

	/**
	 * Information that characterizes the type of message sent
	 * @return ID describing the type of message sent
	 */
	public byte getMessageType() {
		return messageType;
	}
	
	/**
	 * Retrieves the RFID Tag detection notification message
	 * @return RFID Tag detection notification received.
	 */
	public RfidTagDetectedNotification getRfidTagDetectedNotification() {
		return rfidTagDetectedNotification;
	}

	/**
	 * Attempts to read an accelerometer measurement from the MDU packet
	 * 
	 * @param mduPacket
	 *            MDU Packet
	 * @return Decoded Accelerometer measurement. Returns null if one cannot be
	 *         decoded.
	 */
	public static RfidTagDetectedMessage TryDecodeRfidTagDetectedNotification(byte[] mduPacket) {
		RfidTagDetectedNotification decodedMeasurement = null;
		byte messageType = readMessageType(mduPacket);
		byte sourceId = readSourceId(mduPacket);
		byte destId = readDestinationId(mduPacket);
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

			Calendar timeMeasured = decodeTimestamp(timestamp);
			String trainId = getTrainId(mduPacket);

			decodedMeasurement = new RfidTagDetectedNotification(trainId, rfidTagValue, timeMeasured);
		}

		return new RfidTagDetectedMessage(destId, sourceId, decodedMeasurement);
	}
	
	/**
	 * Encodes the RFID Detected message into a MDU protocol packet
	 * 
	 * @param message
	 *            Message to encode
	 * @return Encoded MDU Packet
	 */
	public static byte[] Encode(RfidTagDetectedMessage message) {
		byte[] mduPacket = new byte[RfidReadingPacketSize];
		int TagOffset = 7;
		
		//Figure out RFID Tag values
		byte[] rfidTag = new byte[5];
		String[] segments;
		segments = message.rfidTagDetectedNotification.getRfidTagValue().split(":");
		
		if(segments.length >= 5)
		{
			for(int i = 0; i < 5; i++)
			{
				byte[] hexBytes = DatatypeConverter.parseHexBinary(segments[i]);
				
				rfidTag[i] = hexBytes[0];
			}
		}
		else if(segments.length < 5){
			
			int initBytes = (5 - segments.length);
			for(int i = 0; i < initBytes; i++)
			{
				rfidTag[i] = 0x00;
			}
			
			for(int i = 0; i < segments.length; i++)
			{
				byte[] hexBytes = DatatypeConverter.parseHexBinary(segments[i]);
				
				rfidTag[i+initBytes] = hexBytes[0]; 
			}
		}
		
		writeDestinationId(mduPacket, message.destination);
		writeSourceId(mduPacket, message.source);
		writeMessageType(mduPacket, RfidReading);
		writeTimestamp(mduPacket, message.rfidTagDetectedNotification.getTimeDetected());
		
		//Write RFID Tag values
		mduPacket[TagOffset] = rfidTag[0];
		mduPacket[TagOffset + 1] = rfidTag[1];
		mduPacket[TagOffset + 2] = rfidTag[2];
		mduPacket[TagOffset + 3] = rfidTag[3];
		mduPacket[TagOffset + 4] = rfidTag[4];
		
		writeTail(mduPacket);
				
		return mduPacket;
	}

}

