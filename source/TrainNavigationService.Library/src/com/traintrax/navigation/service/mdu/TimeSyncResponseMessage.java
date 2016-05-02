package com.traintrax.navigation.service.mdu;

import java.util.Calendar;

/**
 * Message reported in response to a Time Sync Request
 * from the MDU
 * @author Corey Sanders
 *
 */
public class TimeSyncResponseMessage extends BaseMduMessage {
	
	/**
	 * MDU Protocol Message Type
	 */
	public static final byte TimeSyncResponse = 0x09;
	
	/**
	 * Fixed size of the packet
	 */
	public static final int TimeSyncResponsePacketSize = 8;
	
	private final byte destination;
	private final byte source;
	private final byte messageType;

	private final Calendar timestamp;
	
	/**
	 * Constructor
	 * @param timeSyncResponseMessage Message to reply to.
	 * @param calendar Time to use in the response
	 */
	public TimeSyncResponseMessage(TimeSyncRequestMessage timeSyncResponseMessage, Calendar calendar) {
		super();
		this.destination = timeSyncResponseMessage.getSource();
		this.source = timeSyncResponseMessage.getDestination();
		this.messageType = TimeSyncResponse;
		this.timestamp = calendar;
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
	
	public Calendar getTimestamp(){
		return timestamp;
	}
	
	/**
	 * Encodes the train identification message into a MDU protocol packet
	 * 
	 * @param timeSyncReplyMessage
	 *            Message to encode
	 * @return MDU Packet
	 */
	public static byte[] EncodeTimeSyncReply(TimeSyncResponseMessage timeSyncReplyMessage) {
		byte[] encodedMessage = new byte[TimeSyncResponseMessage.TimeSyncResponsePacketSize];

		int DestinationOffset = 0;
		int SourceOffset = 1;
		int MessageTypeOffset = 2;
		int TimeOffset = 3;
		int MessageTailOffset = 7;
		
		// Decode values
		encodedMessage[DestinationOffset] = timeSyncReplyMessage.getDestination();
		encodedMessage[SourceOffset] = timeSyncReplyMessage.getSource();
		encodedMessage[MessageTypeOffset] = timeSyncReplyMessage.getMessageType();
				
		int millisecondsSinceDayStart = getNumberOfMillisecondsSinceStartOfDay(timeSyncReplyMessage.getTimestamp());
		long diff = (millisecondsSinceDayStart/1000);
				
		encodedMessage[TimeOffset] = (byte) ((diff >> 24)&0xFF);
		encodedMessage[TimeOffset+1] = (byte) ((diff >> 16)&0xFF);
		encodedMessage[TimeOffset+2] = (byte) ((diff >> 8)&0xFF);
		encodedMessage[TimeOffset+3] = (byte) ((diff)&0xFF);
		
		encodedMessage[MessageTailOffset] = 0x0A;

		return encodedMessage;
	}

}
