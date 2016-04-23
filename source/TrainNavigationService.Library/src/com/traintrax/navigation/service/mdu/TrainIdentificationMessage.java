package com.traintrax.navigation.service.mdu;

/**
 * Message reported by the MDU to identify itself to the
 * Train Navigation Service
 * @author Corey Sanders
 *
 */
public class TrainIdentificationMessage {
	
	/**
	 * MDU Protocol Message Type
	 */
	public static final byte Identification = 0x07;
	
	/**
	 * Fixed size of the packet
	 */
	public static final int IdentificationPacketSize = 4;
	
	private final byte destination;
	private final byte source;
	private final byte messageType;
	
	/**
	 * Constructor
	 * @param destination ID of the destination of the message
	 * @param source Originator of the message
	 */
	public TrainIdentificationMessage(byte destination, byte source) {
		super();
		this.destination = destination;
		this.source = source;
		this.messageType = Identification;
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
	
	

}
