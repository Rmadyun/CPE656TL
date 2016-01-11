package TrainTrax;

/**
 * Class facilitates communication with switch controllers
 * on the Positive Train Control Test Bed
 * @author death
 *
 */
public class TrainController implements TrainControllerInterface {
	
	/**
	 * Requests that a switches' state be changed.
	 * @param switchIdentifier Unique identifier for the desired switch to change
	 * @param switchState State to change the targeted switch
	 */
	public void ChangeSwitchState(String switchIdentifier, SwitchState switchState){
		//TODO: Implement
	}

}
