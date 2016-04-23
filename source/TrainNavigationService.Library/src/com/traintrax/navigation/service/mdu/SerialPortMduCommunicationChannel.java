package com.traintrax.navigation.service.mdu;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

/**
 * Serial port implementation of the MDU Communication Channel
 * 
 * @author Corey Sanders
 *
 */
public class SerialPortMduCommunicationChannel implements MduCommunicationChannelInterface {

	private static final String SerialPortOwner = "Train Navigation Service";
	private static final int OpenWaitTimeInMilliseconds = 50;

	private CommPort serialPort;

	/**
	 * Constructor
	 * 
	 * @param serialPort
	 *            FilePath of the serial port to use to contact the MDU
	 * @throws Exception
	 *             Reports failure to use serial port
	 */
	public SerialPortMduCommunicationChannel(String portName) throws Exception {
		CommPortIdentifier commPortIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		serialPort = commPortIdentifier.open(SerialPortOwner, OpenWaitTimeInMilliseconds);
		
	    SerialPort sp = (SerialPort) serialPort;
	    
	    int baudRate = 9600; // 9600bps
        sp.setSerialPortParams(
                baudRate,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return serialPort.getInputStream();
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
	
		return serialPort.getOutputStream();
	}

}
