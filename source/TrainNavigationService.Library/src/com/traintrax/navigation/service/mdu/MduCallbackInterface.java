package com.traintrax.navigation.service.mdu;

import com.traintrax.navigation.service.position.Train;

/***
 * Interface used to notify the nav service about changes in the MDU
 * @author Corey
 *
 */
public interface MduCallbackInterface {
	
	void TrainAdded(Train train);

}
