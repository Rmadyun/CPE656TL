package com.traintrax.navigation.service.mdu;

import com.traintrax.navigation.service.position.GyroscopeMeasurement;

///Interface for Gyroscope sensors
public interface IGyroscope {
	
	///Grab the latest measurement from the gyroscope.
	GyroscopeMeasurement getNextMeasurement();

}
