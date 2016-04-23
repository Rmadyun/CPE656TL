package com.traintrax.navigation.service.mdu;

/**
 * Message reported by the MDU to identify itself to the
 * Train Navigation Service
 * @author Corey Sanders
 *
 */
public class TimeSyncRequestMessage {
	
	/**
	 * MDU Protocol Message Type
	 */
	public static final byte TimeSyncRequest = 0x08;
	
	/**
	 * Fixed size of the packet
	 */
	public static final int TimeSyncRequestPacketSize = 4;
	
	private final byte destination;
	private final byte source;
	private final byte messageType;
	
	/**
	 * Constructor
	 * @param destination ID of the destination of the message
	 * @param source Originator of the message
	 */
	public TimeSyncRequestMessage(byte destination, byte source) {
		super();
		this.destination = destination;
		this.source = source;
		this.messageType = TimeSyncRequest;
	}

	/**
	 * Constructor
	 * @param trainIdentificationMessage Message to reply to.
	 */
	public TimeSyncRequestMessage(TrainIdentificationMessage trainIdentificationMessage) {
		this(trainIdentificationMessage.getSource(), trainIdentificationMessage.getDestination());
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
	 * Reads the message type from the MDU packet
	 * 
	 * @param mduPacket
	 *            MDU packet
	 * @return value of the message type read from the packet
	 */
	private static byte readMessageType(byte[] mduPacket) {
		int MessageTypeOffset = 2;

		return mduPacket[MessageTypeOffset];
	}

	
	/**
	 * Attempts to read a train identification message from the MDU packet
	 * 
	 * @param mduPacket
	 *            MDU Packet
	 * @return Decoded Train Identification message. Returns null if one cannot
	 *         be decoded.
	 */
	public static TimeSyncRequestMessage TryDecodeTimeSyncRequest(byte[] mduPacket) {
		TimeSyncRequestMessage timeSyncRequestMessage = null;
		byte messageType = readMessageType(mduPacket);
		int DestinationOffset = 0;
		int SourceOffset = 1;

		if (messageType == TimeSyncRequest && mduPacket.length == TimeSyncRequestPacketSize) {
			// Decode values

			byte destination = mduPacket[DestinationOffset];
			byte source = mduPacket[SourceOffset];

			timeSyncRequestMessage = new TimeSyncRequestMessage(destination, source);
		}

		return timeSyncRequestMessage;
	}

}
