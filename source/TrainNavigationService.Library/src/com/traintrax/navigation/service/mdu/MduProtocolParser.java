package com.traintrax.navigation.service.mdu;

import java.io.FileWriter;
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
	 * Size of the header that is a part of every MDU Protocol message
	 */
	private static final int MduProtocolHeaderSize = 3;
	
	/**
	 * Unique ID used by the protocol to refer to the PC end of MDU Protocol communication.
	 */
	private static final byte BaseStationId = 0x63;

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
	private static final int RoundtripTimeRequestPacketSize = 4;
	private static final int RoundtripTimeReplyPacketSize = 4;
	private static final int TimeSyncRequestPacketSize = 4;
	private static final int TimeSyncReplyPacketSize = 8;

	private static final int MessageTargetId = 0;
	private static final int MessageTypeOffset = 2;

	//DEBUG:
	private static FileWriter fileWriter;
	

	/**
	 * Assigns the contents of the packet buffer header
	 * 
	 * @param numberOfPacketBytesStored
	 *            Number of Packet Bytes that are stored in the packet buffer
	 * @param packetBuffer
	 *            Reference to the packet buffer byte array
	 */
	private void setPacketBufferHeader(int numberOfPacketBytesStored, byte[] packetBuffer) {
		// Assuming that the packet buffer header size is 1 for now

		packetBuffer[0] = (byte) numberOfPacketBytesStored;
	}

	/**
	 * Determines what size the packet should be
	 * 
	 * @return Returns the size in bytes that are expected. Returns -1 if it
	 *         cannot be determined.
	 */
	private int getExpectedMessageLength(byte[] packetBuffer, int packetSize) {

		int expectedMessageLength = -1;

		if (packetSize >= MduProtocolHeaderSize) {
			// Assuming that every MDU Protocol Message Type has a fixed length
			byte messageType = packetBuffer[PacketBufferHeaderSize + MessageTypeOffset];

			if (messageType == ImuReading) {
				expectedMessageLength = ImuReadingPacketSize;
			} else if (messageType == RfidReading) {
				expectedMessageLength = RfidReadingPacketSize;
			} else if (messageType == Identification) {
				expectedMessageLength = IdentificationPacketSize;

			} else if (messageType == RoundTripTimeTestRequest) {
				expectedMessageLength = RoundtripTimeRequestPacketSize;

			} else if (messageType == RoundTripTimeTestResponse) {
				expectedMessageLength = RoundtripTimeReplyPacketSize;

			} else if (messageType == TimeRequest) {
				expectedMessageLength = TimeSyncRequestPacketSize;

			} else if (messageType == TimeResponse) {
				expectedMessageLength = TimeSyncReplyPacketSize;
			} else {
				// TODO: Add more messages
			}

		}

		return expectedMessageLength;
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
		// Initially report the buffer as empty
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
					
					
					if(fileWriter == null)
					{
						fileWriter = new FileWriter("C:\\TrainTrax\\rawBytes.txt");
					}
					

					fileWriter.write(String.format("%02X ", readByte));
					fileWriter.flush();
					

					// Store byte
					if ((packetBufferSize + PacketBufferHeaderSize) == packetBuffer.length) {
						System.out.println(
								"WARNING (MDU Protocol Parsing): MTU exceeded while searching for end of packet. Dropping earliest bytes.");

						// Shift everything left by 1 byte
						for (int i = 0; i < (packetBuffer.length - 1); i++) {
							packetBuffer[i] = packetBuffer[i + 1];
						}
					} else {
						packetBufferSize++;
					}

					packetBuffer[PacketBufferHeaderSize + packetBufferSize - 1] = readByte;

					if (readByte == NewLineCharacter) {
						int expectedLen = getExpectedMessageLength(packetBuffer, packetBufferSize);

						if (expectedLen < 0) {
							// Do Nothing
						} else if ((packetBufferSize > expectedLen)||(packetBuffer[PacketBufferHeaderSize + MessageTargetId] != BaseStationId)) { //Ignore non-sensical msgs / those not intended for base station
							// Look for the first occurrence of the new line and
							// start the buffer there.
							int firstNewLineIndex = -1;
							for (int i = 0; i < (packetBufferSize - 1); i++) {
								if (packetBuffer[PacketBufferHeaderSize + i] == NewLineCharacter) {
									firstNewLineIndex = PacketBufferHeaderSize + i;
									break;
								}
							}
							int numberBytesToShift = firstNewLineIndex + 1;

							// Shift past the first New Line Character found and
							// retry
							for (int j = 0; j < numberBytesToShift; j++) {

								// Shift everything left by 1 byte
								for (int i = 0; i < (packetBuffer.length - 1); i++) {
									packetBuffer[i] = packetBuffer[i + 1];
								}
							}

						} else if (packetBufferSize == expectedLen) {

							// Write packet buffer header
							setPacketBufferHeader(packetBufferSize, packetBuffer);

							// Mark to Save Buffer
							appendingMduMessage = false;
							packetBufferSize = 0;
                            fileWriter.write('\n');
                            fileWriter.flush();
						} else { // packetBufferSize < expectedLen

							// Do Nothing
						}
					}
				}
			}
		}

		return packetBuffer;
	}

	@Override
	public byte[] getPacketBytesStored(byte[] packetBuffer) {

		// return Arrays.copyOfRange(packetBuffer, PacketBufferHeaderSize,
		// packetBuffer.length - PacketBufferHeaderSize + 1);
		int len = getNumberOfPacketBytesStored(packetBuffer);
		byte[] bufferCopy = new byte[len];
		int offset = PacketBufferHeaderSize;
		for (int i = 0; i < len; i++) {
			bufferCopy[i] = packetBuffer[offset + i];
		}

		return bufferCopy;
	}
}
