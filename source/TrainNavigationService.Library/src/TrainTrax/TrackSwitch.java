package TrainTrax;


/**
 * Class represents a single track switch on the
 * positive train control test bed.
 * @author Corey Sanders
 *
 */
public class TrackSwitch implements TrackSwitchInterface {
	
	/**
	 * Retrieves the unique identifier for the represented switch
	 * @return Unique identifier for the represented switch
	 */
	public String getSwitchId(){
		
		//TODO: Implement
		
		return "";
	}
	
	/**
	 * Retrieves the current state of the represented switch
	 * @return Current state of the represented switch
	 */
	public SwitchState getCurrentSwitchState(){
		
		//TODO: Implement
		
		return SwitchState.Pass;
	}
	
	/**
	 * Changes the state of the represented switch
	 * @param switchState State to change the switch
	 */
	public void setSwitchState(SwitchState switchState){
		//TODO: Implement
	}
	
	/**
	 * Subscribes a client to receive track switch events
	 * @param subscriber Client to receive track switch events
	 */
	public void Subscribe(TrackSwitchEventSubscriber subscriber){
		//TODO: Implement
	}
	
	/**
	 * Unsubscribes a client from receiving track switch events
	 * @param subscriber Client receiving track switch events
	 */
	public void Unsubscribe(TrackSwitchEventSubscriber subscriber){
		//TODO: Implement
	}

}
