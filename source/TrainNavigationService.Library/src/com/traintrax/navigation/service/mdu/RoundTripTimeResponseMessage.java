package com.traintrax.navigation.service.mdu;

/**
 * Message reported by the in response to a
 * RoundTripRequest from the MDU
 * @author Corey Sanders
 *
 */
public class RoundTripTimeResponseMessage {
	
	/**
	 * MDU Protocol Message Type
	 */
	public static final byte RoundtripTimeReply = 0x06;
	
	/**
	 * Fixed size of the packet
	 */
	public static final int RoundtripTimeReplyPacketSize = 4;
	
	private final byte destination;
	private final byte source;
	private final byte messageType;
	
	/**
	 * Constructor
	 * @param destination ID of the destination of the message
	 * @param source Originator of the message
	 */
	public RoundTripTimeResponseMessage(byte destination, byte source) {
		super();
		this.destination = destination;
		this.source = source;
		this.messageType = RoundtripTimeReply;
	}

	/**
	 * Constructor
	 * @param roundTripRequestMessage Message to reply to.
	 */
	public RoundTripTimeResponseMessage(RoundTripTimeRequestMessage roundTripRequestMessage) {
		this(roundTripRequestMessage.getSource(), roundTripRequestMessage.getDestination());
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
	 * Encodes the train identification message into a MDU protocol packet
	 * 
	 * @param roundtripReplyMessage
	 *            Message to encode
	 * @return MDU Packet
	 */
	public static byte[] EncodeRoundTripTimeReply(RoundTripTimeResponseMessage roundtripReplyMessage) {
		byte[] encodedMessage = new byte[RoundTripTimeResponseMessage.RoundtripTimeReplyPacketSize];

		int DestinationOffset = 0;
		int SourceOffset = 1;
		int MessageTypeOffset = 2;
		int MessageTailOffset = 3;

		encodedMessage[DestinationOffset] = roundtripReplyMessage.getDestination();
		encodedMessage[SourceOffset] = roundtripReplyMessage.getSource();
		encodedMessage[MessageTypeOffset] = roundtripReplyMessage.getMessageType();
		encodedMessage[MessageTailOffset] = 0x0A;

		return encodedMessage;
	}

}
