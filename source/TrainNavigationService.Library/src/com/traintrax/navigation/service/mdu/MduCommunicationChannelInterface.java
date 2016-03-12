package com.traintrax.navigation.service.mdu;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Generic interface communicating with a MDU device
 * @author Corey Sanders
 *
 */
public interface MduCommunicationChannelInterface {

	/**
	 * Retrieve the input stream to receive data from the MDU
	 * @return Contact to receive input from the MDU
	 * @throws IOException Reports failure to access an input stream
	 */
	InputStream getInputStream() throws IOException;
	
	/**
	 * Return the output stream to send data to the MDU.
	 * @return Contact to send output to the MDU
	 * @throws IOException Reports failure to access an output stream
	 */
	OutputStream getOutputStream() throws IOException;
}
