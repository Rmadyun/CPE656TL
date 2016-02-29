package com.traintrax.navigation.service.mdu;

///Interface for Gyroscope sensors
public interface IGyroscope {
	
	///Grab the latest measurement from the gyroscope.
	GyroscopeMeasurement getNextMeasurement();

}
