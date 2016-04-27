package com.traintrax.navigation.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.traintrax.navigation.database.library.TrackSwitch;
import com.traintrax.navigation.service.mdu.SerialPortMduCommunicationChannel;
import com.traintrax.navigation.trackswitch.SwitchState;

import gnu.io.CommPortIdentifier;
import jmri.jmrix.loconet.LnConstants;
import jmri.jmrix.loconet.LnPacketizer;
import jmri.jmrix.loconet.LnPortController;
import jmri.jmrix.loconet.LnTurnout;
import jmri.jmrix.loconet.LocoNetInterface;
import jmri.jmrix.loconet.LocoNetMessage;
import jmri.jmrix.loconet.pr3.PR3Adapter;
import jmri.jmrix.loconet.pr3.PR3SystemConnectionMemo;

/**
 * Class facilitates communication with switch controllers on the Positive Train
 * Control Test Bed
 * 
 * @author Corey Sanders
 *
 */
public class TrackSwitchController implements TrackSwitchControllerInterface {
	private Map<String, SwitchState> switchStateLut = new HashMap<String, SwitchState>();

	private final String serialPort;

	private final String prefix;

	private TrainNavigationDatabaseInterface trainNavigationDatabase;

	// NOTE: Verified that Test Bed is configured to use the Default Prefix.
	// Also verified that JMRI works with Windows.
	// Serial port configured to 9600 8N1 works

	public static final String DefaultSerialPort = "COM4";

	public static final String DefaultPrefix = "L";
	private static final String ApplicationName = "Train Navigation Service";

	/**
	 * Default Constructor
	 * 
	 * @throws Exception
	 *             Reports any type of failure involved with connecting to the
	 *             controller
	 */
	public TrackSwitchController() throws Exception {
		this(DefaultSerialPort, DefaultPrefix, null);
	}

	/**
	 * Constructor
	 * 
	 * @param serialPort
	 *            Contact to the LocoNet
	 * @param prefix
	 *            'Connection Prefix' used to help JMRI communicate separately
	 *            with multiple 'connections' to layout hardware. It determines
	 *            which LocoNet subnet that should be associated with the Port
	 *            controller
	 * @param trainNavigationDatabase
	 *            Contract to the train Navigation Database. Specify null if not used.
	 * @throws Exception
	 *             Reports any type of failure involved with connecting to the
	 *             controller
	 */
	public TrackSwitchController(String serialPort, String prefix,
			TrainNavigationDatabaseInterface trainNavigationDatabaseInterface) {

		this.serialPort = serialPort;
		this.prefix = prefix;
		this.trainNavigationDatabase = trainNavigationDatabaseInterface;

		// Initialize switches
		// Note a better check for null may be necessary if the DB is used
		// outside
		// of only initialization. In fact, requiring a non-null value may be
		// necessary.
		if (trainNavigationDatabase != null) {
			initializeAllSwitchesToKnownState(trainNavigationDatabase);
		}
	}

	/**
	 * Method initializes all of the known switches on the rail system to be in
	 * a known state.
	 * 
	 * @param trainNavigationDatabase
	 *            Contact for information about the train track and to save
	 *            measurements
	 */
	private void initializeAllSwitchesToKnownState(TrainNavigationDatabaseInterface trainNavigationDatabase) {

		List<TrackSwitch> switches = trainNavigationDatabase.getTrackSwitches();

		// Set all switches into pass.
		for (TrackSwitch trackSwitch : switches) {

			if(trackSwitch != null)
			{
			    ChangeSwitchState(trackSwitch.getSwitchName(), SwitchState.Pass);
			}
		}
	}

	/**
	 * Method creates a LnPort Controller to use to communicate with the PR3.
	 * NOTE: Be sure to dispose of this interface when done. The reason this
	 * method is used instead of a field is so that the program can share the
	 * serial port for controlling a PR3 with other programs such as JMRI.
	 * 
	 * @return Created LnPort Controller instance
	 * @throws Exception
	 *             Reports an error when the port controller fails to be created
	 */
	private LnPortController CreateLnPortController() throws Exception {
		// Assumes we are using a MS100 compatible device, not a
		// LocoBuffer

		LnPortController serialPortAdapter = new PR3Adapter();

		// JMRI 4.3/3.8 Code
		serialPortAdapter.setCommandStationType("Stand-alone LocoNet");

		serialPortAdapter.openPort(serialPort, ApplicationName);
		serialPortAdapter.connect();
		serialPortAdapter.configure();

		return serialPortAdapter;
	}

	/**
	 * Method creates a LocoNet interface to use for switch control. NOTE: The
	 * reason this method is used instead of a field is so that the program can
	 * share the serial port for controlling a PR3 with other programs such as
	 * JMRI.
	 * 
	 * @param serialPortAdapter
	 *            Contact to the PR3 Adapter
	 * @return Created LocoNet interface instance
	 * @throws Exception
	 *             Reports an error when the interface fails to be created
	 */
	private LocoNetInterface CreateLocoNetInterface(LnPortController serialPortAdapter) throws Exception {
		LocoNetInterface locoNetInterface;
		// Assumes we are using a MS100 compatible device, not a
		// LocoBuffer

		// JMRI 3.8 Code
		PR3SystemConnectionMemo memo = (PR3SystemConnectionMemo) serialPortAdapter.getSystemConnectionMemo();
		locoNetInterface = memo.getLnTrafficController();

		selectMS100mode(locoNetInterface);

		return locoNetInterface;
	}

	/**
	 * Configures the PR3 to be in 'Programmer' Mode.
	 * 
	 * NOTE: This code was taken from the JMRI source branch:
	 * https://github.com/JMRI/JMRI
	 * 
	 * @param locoNetInterface
	 *            Contact to the PR3 Programming Interface
	 */
	void selectPR2mode(LocoNetInterface locoNetInterface) {
		// set to PR2 mode
		LocoNetMessage msg = new LocoNetMessage(6);
		msg.setOpCode(0xD3);
		msg.setElement(1, 0x10);
		msg.setElement(2, 1); // set PR2
		msg.setElement(3, 0);
		msg.setElement(4, 0);
		locoNetInterface.sendLocoNetMessage(msg);
	}

	/**
	 * Configures the PR3 to be in 'Interface' Mode.
	 * 
	 * NOTE: This code was taken from the JMRI source branch:
	 * https://github.com/JMRI/JMRI
	 * 
	 * @param locoNetInterface
	 *            Contact to the PR3 Programming Interface
	 */
	void selectMS100mode(LocoNetInterface locoNetInterface) {
		// set to MS100 mode

		LocoNetMessage msg = new LocoNetMessage(6);
		msg.setOpCode(0xD3);
		msg.setElement(1, 0x10);
		msg.setElement(2, 0); // set MS100
		msg.setElement(3, 0);
		msg.setElement(4, 0);
		locoNetInterface.sendLocoNetMessage(msg);
	}

	/**
	 * Method decodes an incoming LOCONET message to determine if it is a status
	 * message that reports the current mode of the PR3
	 * 
	 * @param msg
	 *            LOCONET message to inspect for PR3 mode information.
	 * @return Returns a value that describes the mode that the PR3 is in if the
	 *         LOCONET message is a PR3 status message. Otherwise, it returns
	 *         null.
	 * 
	 *         NOTE: This code was mostly taken and refactored from the JMRI
	 *         source branch: https://github.com/JMRI/JMRI
	 */
	public String readPr3Mode(LocoNetMessage msg) {
		String pr3Mode = null;

		if ((msg.getOpCode() == LnConstants.OPC_PEER_XFER) && (msg.getElement(1) == 0x10) && (msg.getElement(2) == 0x22)
				&& (msg.getElement(3) == 0x22) && (msg.getElement(4) == 0x01)) { // Digitrax
																					// form,
																					// check
																					// PR2/PR3
																					// or
																					// MS100/PR3
																					// mode
			int mode = msg.getElement(8) & 0x0C;
			if (mode == 0x00) {
				// PR2 format
				pr3Mode = "StatusPr2";
			} else {
				// MS100 format
				pr3Mode = "StatusMs100";
			}
		}

		return pr3Mode;
	}

	/**
	 * Method retrieves the LocoNet Switch number for a switch The current
	 * implementation assumes that switch identifiers are composed of a 2
	 * character header ("SW") and a number.
	 * 
	 * @param switchIdentifier
	 *            Unique Identifier for a switch
	 * @return LocoNet Switch number
	 */
	private int getSwitchNumber(String switchIdentifier) {

		String switchNumberString = switchIdentifier.substring(2);
		int switchNumber = Integer.parseInt(switchNumberString);

		return switchNumber;
	}

	/**
	 * Requests that a switches' state be changed.
	 * 
	 * @param switchIdentifier
	 *            Unique identifier for the desired switch to change
	 * @param switchState
	 *            State to change the targeted switch
	 */
	public void ChangeSwitchState(String switchIdentifier, SwitchState switchState) {

		int switchNumber = getSwitchNumber(switchIdentifier);

		// CS - Disabled as a part of troubleshooting difficulties controlling
		// switches with
		// JMRI
		/*
		 * LnTurnout lnTurnout = new LnTurnout(prefix, switchNumber,
		 * locoNetInterface);
		 * 
		 * int commandedState = (switchState == SwitchState.Pass) ?
		 * LnTurnout.CLOSED : LnTurnout.THROWN;
		 * 
		 * lnTurnout.setCommandedState(commandedState);
		 */

		LocoNetMessage msg = new LocoNetMessage(4);
		msg.setOpCode(0XB0);
		msg.setElement(1, (byte) ((switchNumber - 1) & 0xFF));
		msg.setElement(2, (switchState == SwitchState.Pass) ? 0x30 : 0x10);

		// Calculate the checksum and assign it to the message.
		msg.setParity();

		// Created LocoNet Interface to use for the switch control
		// NOTE: Be sure to dispose.
		// instance is created instead of shared so that the port may
		// be shared with other programs, such as JMRI.
		LocoNetInterface locoNetInterface = null;
		LnPortController lnPortController = null;
		try {
			lnPortController = CreateLnPortController();
			locoNetInterface = CreateLocoNetInterface(lnPortController);
			locoNetInterface.sendLocoNetMessage(msg);

			switchStateLut.put(switchIdentifier, switchState);
		} catch (Exception e) {
			System.out.println(
					"Cannot change switch to: " + switchState.toString() + ". Error: " + e.getMessage() + e.toString());
			e.printStackTrace();
		}

		// Cleanup the LocoNetInterface
		if (lnPortController != null) {
			lnPortController.dispose();
		}
	}

	/**
	 * Retrieves the last known state of a given switch
	 * 
	 * @param switchIdentifier
	 *            Unique ID for the switch of interest
	 * @return Current state of the desired switch
	 */
	public SwitchState getSwitchState(String switchIdentifier) {

		return switchStateLut.get(switchIdentifier);
	}

}
