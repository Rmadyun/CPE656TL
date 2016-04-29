package edu.uah.cpe.traintrax;

import com.traintrax.navigation.trackswitch.SwitchState;

/**
 * Class encapsulates the outcome of an attempt to
 * change the state of a switch on the track
 */
public class ChangeSwitchStateTaskResult {
    private String failureMessage;
    private SwitchState assignedSwitchState;
    private String switchNumber;

    /**
     * Constructor
     * @param failureMessage Detailed information about why the switch request failed. Assign to an empty string if successful.
     * @param assignedSwitchState The switch state requested for the switch to be changed to
     * @param switchNumber Unique identifier of the switch whose state has been requested to be changed
     */
    public ChangeSwitchStateTaskResult(String failureMessage, SwitchState assignedSwitchState, String switchNumber) {
        this.failureMessage = failureMessage;
        this.assignedSwitchState = assignedSwitchState;
        this.switchNumber = switchNumber;
    }

    /**
     * Retrieves detailed information about why the switch request failed. Assign to an empty string if successful.
     * @return detailed information about why the switch request failed. Assign to an empty string if successful.
     */
    public String getFailureMessage() {
        return failureMessage;
    }

    /**
     * Retrieves the switch state requested for the switch to be changed to
     * @return the switch state requested for the switch to be changed to
     */
    public SwitchState getAssignedSwitchState() {
        return assignedSwitchState;
    }

    /**
     * Retrieves the unique identifier of the switch whose state has been requested to be changed
     * @return unique identifier of the switch whose state has been requested to be changed
     */
    public String getSwitchNumber() {
        return switchNumber;
    }

    /**
     * Indicates whether the switch was successfully changed
     * @return Returns true if the request switch state was changed.
     */
    public boolean isSwitchStateChanged(){
        boolean switchStateChanged = false;

        switchStateChanged = ((failureMessage != null)&&(failureMessage != ""));

        return switchStateChanged;
    }
}
