package com.traintrax.navigation.service.mdu;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Test implementation of the MduCommunicationChannelInterface
 * @author Corey Sanders
 * Class emulates traffic from a MDU.
 */
public class TestMduCommunicationChannel implements MduCommunicationChannelInterface {
	
	private final ByteArrayInputStream inputStream;
	private final ByteArrayOutputStream outputStream;
	
	/**
	 * Constructor
	 */
	public TestMduCommunicationChannel(){
		byte[] testMessage = new byte[20];
		int time = 64800000;
		short ax = 944;
		short ay = -668;
		short az = 12620;
		short gx = 16;
		short gy = 170;
		short gz = 90;
		ByteBuffer b = ByteBuffer.allocate(4);
		//b.order(ByteOrder.BIG_ENDIAN); // optional, the initial order of a byte buffer is always BIG_ENDIAN.
		b.putInt(time);

		//Assign Example IMU Measurement
		
		testMessage[0] = 0x03;
		testMessage[1] = 0x7;
		testMessage[2] = 0x00;
		
		//time
		byte[] timeInBytes = b.array();
		testMessage[3] = timeInBytes[0];
		testMessage[4] = timeInBytes[1];
		testMessage[5] = timeInBytes[2];
		testMessage[6] = timeInBytes[3];
		
		b.clear();
		b.putShort(ax);
		timeInBytes = b.array(); 
		testMessage[7] = timeInBytes[0];
		testMessage[8] = timeInBytes[1];
		
		b.clear();
		b.putShort(ay);
		timeInBytes = b.array(); 
		testMessage[9] = timeInBytes[0];
		testMessage[10] = timeInBytes[1];
		
		b.clear();
		b.putShort(az);
		timeInBytes = b.array(); 
		testMessage[11] = timeInBytes[0];
		testMessage[12] = timeInBytes[1];
		
		b.clear();
		b.putShort(gx);
		timeInBytes = b.array(); 
		testMessage[13] = timeInBytes[0];
		testMessage[14] = timeInBytes[1];
		
		b.clear();
		b.putShort(gy);
		timeInBytes = b.array(); 
		testMessage[15] = timeInBytes[0];
		testMessage[16] = timeInBytes[1];
		
		b.clear();
		b.putShort(gz);
		timeInBytes = b.array(); 
		testMessage[17] = timeInBytes[0];
		testMessage[18] = timeInBytes[1];
		
		testMessage[19] = 0x0a;
		
		inputStream = new ByteArrayInputStream(testMessage);
		outputStream = new ByteArrayOutputStream();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return inputStream;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		
		outputStream.flush();
		outputStream.reset();

		return outputStream;
	}

}
