package com.traintrax.navigation.service.mdu;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Class responsible for detecting MDU Protocol messages in an input stream
 * 
 * @author Corey Sanders
 *
 */
public class MduProtocolParser implements MduProtocolParserInterface {

	private static final byte NewLineCharacter = 0x0a;
	/**
	 * Each byte array that contains what is believed to be a packet has a
	 * header in front of it that describes the following: Byte 0: Number of
	 * Packet Bytes in the Array (Assuming that the Packet Size will never
	 * exceed 256 bytes)
	 */
	private static final int PacketBufferHeaderSize = 1;

	/**
	 * Maximum transmission unit size for the MDU Protocol.
	 */
	private static final int MduProtocolMtuSize = 20;

	/**
	 * Assigns the contents of the packet buffer header
	 * 
	 * @param numberOfPacketBytesStored
	 *            Number of Packet Bytes that are stored in the packet buffer
	 * @param packetBuffer
	 *            Reference to the packet buffer byte array
	 */
	private void setPacketBufferHeader(int numberOfPacketBytesStored, byte[] packetBuffer) {
		//Assuming that the packet buffer header size is 1 for now
		
		packetBuffer[0] = (byte) numberOfPacketBytesStored;
	}

	/**
	 * Retrieves the number of packet bytes stored in a packet buffer
	 * 
	 * @param packetBuffer
	 *            Reference to the packet buffer byte array
	 * @return number of packet bytes stored in the targeted packet buffer
	 */
	public int getNumberOfPacketBytesStored(byte[] packetBuffer) {
		int numberOfpacketBytesStored = (int) packetBuffer[0];

		return numberOfpacketBytesStored;
	}

	@Override
	public byte[] getNextMduPacket(InputStream mduInputStream) throws IOException {
		
		byte[] packetBuffer = new byte[PacketBufferHeaderSize + MduProtocolMtuSize];
		int packetBufferSize = 0;
		boolean appendingMduMessage = true;
		//Initially report the buffer as empty
		setPacketBufferHeader(packetBufferSize, packetBuffer);

		if (mduInputStream == null) {
			String message = "WARNING: No input stream obtained for MDU. Aborting Reading Messages";
			System.out.println(message);
		} else {

			while (appendingMduMessage) {
				int readValue = -1;
				
				readValue = mduInputStream.read();

				if (readValue >= 0) {
					byte readByte = (byte) readValue;

					// Store byte
					if ((packetBufferSize + PacketBufferHeaderSize) == packetBuffer.length) {
						System.out.println(
								"WARNING (MDU Protocol Parsing): MTU exceeded while searching for end of packet. Dropping earliest bytes.");

						// Shift everything left
						for (int i = 0; i < (packetBuffer.length - 1); i++) {
							packetBuffer[i] = packetBuffer[i + 1];
						}
					} else {
						packetBufferSize++;
					}

					packetBuffer[PacketBufferHeaderSize + packetBufferSize - 1] = readByte;

					if (readByte == NewLineCharacter) {
						// Write packet buffer header
						setPacketBufferHeader(packetBufferSize, packetBuffer);

						// Mark to Save Buffer
						appendingMduMessage = false;
					}
				}
			}
		}

		return packetBuffer;
	}

	@Override
	public byte[] getPacketBytesStored(byte[] packetBuffer) {

		return Arrays.copyOfRange(packetBuffer, PacketBufferHeaderSize, packetBuffer.length - PacketBufferHeaderSize + 1);
	}
}
