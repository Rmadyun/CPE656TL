package com.traintrax.navigation.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.traintrax.navigation.service.mdu.SerialPortMduCommunicationChannel;
import com.traintrax.navigation.trackswitch.SwitchState;

import jmri.jmrix.loconet.LnPacketizer;
import jmri.jmrix.loconet.LnPortController;
import jmri.jmrix.loconet.LnTurnout;
import jmri.jmrix.loconet.LocoNetInterface;
import jmri.jmrix.loconet.pr3.PR3Adapter;

/**
 * Class facilitates communication with switch controllers
 * on the Positive Train Control Test Bed
 * @author Corey Sanders
 *
 */
public class TrackSwitchController implements TrackSwitchControllerInterface {
	private Map<String,SwitchState> switchStateLut = new HashMap<String,SwitchState>();

	private final String serialPort;

	private final String prefix;

	private final LocoNetInterface locoNetInterface;

	private final LnPortController serialPortAdapter;
	
	//NOTE: Verified that Test Bed is configured to use the Default Prefix.
	//Also verified that JMRI works with Windows.
	//Serial port configured to 9600 8N1 works 
	
	private static final String DefaultPrefix = "L";
	private static final String ApplicationName = "Train Navigation Service";
	
	/**
	 * Default Constructor
	 * @throws Exception Reports any type of failure involved with connecting to the
	 * controller
	 */
	public TrackSwitchController() throws Exception{
		//TODO: Figure out actual default values and assign
		//elsewhere
		this("/dev/ttyACM0", DefaultPrefix);
	}
	
	/**
	 * Constructor
	 * @param serialPort Contact to the LocoNet
	 * @param prefix 'Connection Prefix' used to help JMRI communicate separately with 
	 * multiple 'connections' to layout hardware. It determines which LocoNet subnet
	 * that should be associated with the Port controller
	 * @throws Exception Reports any type of failure involved with connecting to the
	 * controller
	 */
	public TrackSwitchController(String serialPort, String prefix) throws Exception{

		this.serialPort = serialPort;
		this.prefix = prefix;
		
    	//Assumes we are using a MS100 compatible device, not a 
		//LocoBuffer
    	
    	LnPortController serialPortAdapter = new PR3Adapter();
    	
    	SerialPortMduCommunicationChannel sc = new SerialPortMduCommunicationChannel(serialPort);
    	
    	InputStream is = sc.getInputStream();
    	
    	serialPortAdapter.openPort(serialPort, ApplicationName);
	    serialPortAdapter.connect();

    	LnPacketizer lnPacketizer = new LnPacketizer();
    	
    	lnPacketizer.connectPort(serialPortAdapter);
    	
    	this.serialPortAdapter = serialPortAdapter;
    	this.locoNetInterface = lnPacketizer;
	}
	
	/**
	 * Method retrieves the LocoNet Switch number for a switch
	 * The current implementation assumes that switch identifiers
	 * are composed of a 2 character header ("SW") and a number.
	 * @param switchIdentifier Unique Identifier for a switch
	 * @return LocoNet Switch number
	 */
	private int getSwitchNumber(String switchIdentifier){
		
		String switchNumberString = switchIdentifier.substring(2);
		int switchNumber = Integer.parseInt(switchNumberString);
		
		return switchNumber;
	}

	/**
	 * Requests that a switches' state be changed.
	 * @param switchIdentifier Unique identifier for the desired switch to change
	 * @param switchState State to change the targeted switch
	 */
	public void ChangeSwitchState(String switchIdentifier, SwitchState switchState){
		
		int switchNumber = getSwitchNumber(switchIdentifier);
    	LnTurnout lnTurnout = new LnTurnout(prefix, switchNumber, locoNetInterface);
    	
    	int commandedState = (switchState == SwitchState.Pass) ? LnTurnout.CLOSED : LnTurnout.THROWN;
    	
    	lnTurnout.setCommandedState(commandedState);
		
		switchStateLut.put(switchIdentifier, switchState);
	}

	/**
	 * Retrieves the last known state of a given switch
	 * @param switchIdentifier Unique ID for the switch of interest
	 * @return Current state of the desired switch
	 */
	public SwitchState getSwitchState(String switchIdentifier) {
		
		return switchStateLut.get(switchIdentifier);
	}

}
