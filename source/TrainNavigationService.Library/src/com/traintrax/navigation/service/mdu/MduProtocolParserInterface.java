package com.traintrax.navigation.service.mdu;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface parses input streams for 
 * MDU Messages
 * @author Corey Sanders
 *
 */
public interface MduProtocolParserInterface {
	
	/**
	 * Retrieves the number of packet bytes stored in a
	 * packet buffer
	 * @param packetBuffer Reference to the packet buffer byte array storing
	 * all of the information about a given packet.
	 * @return number of packet bytes stored in the targeted
	 * packet buffer
	 */
	public int getNumberOfPacketBytesStored(byte[] packetBuffer);
	
	/**
	 * Retrieves the packet bytes stored in the
	 * packet buffer
	 * @param packetBuffer Reference to the packet buffer byte array storing
	 * all of the information about a given packet.
	 * @return Packet bytes stored in the targeted
	 * packet buffer
	 */
	public byte[] getPacketBytesStored(byte[] packetBuffer);
	
	/**
	 * Reads the next MDU Packet from the MDU
	 * @param inputStream Contact to the MDU
	 * @return Byte Sequence that contains the size and content
	 * of a MDU packet
	 * @throws IOException Reports failures to read MDU packets
	 * Blocks until it can receive the MDU packet.
	 */
	byte[] getNextMduPacket(InputStream inputStream) throws IOException;

}
