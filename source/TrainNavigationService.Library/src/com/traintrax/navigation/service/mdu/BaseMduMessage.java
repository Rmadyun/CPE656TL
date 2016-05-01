package com.traintrax.navigation.service.mdu;

public abstract class BaseMduMessage {
	
	/**
	 * Reads the message type from the MDU packet
	 * 
	 * @param mduPacket
	 *            MDU packet
	 * @return value of the message type read from the packet
	 */
	protected static byte readMessageType(byte[] mduPacket) {
		int MessageTypeOffset = 2;

		return mduPacket[MessageTypeOffset];
	}

}
