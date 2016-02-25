package com.traintrax.navigation.service;

///Class is responsible for monitoring changes
///in the orientation of an object.
public class RotationMonitor {
	
	private final static double ANGULAR_RATE_THRESHOLD = 0.01;
	
	private IGyroscope gyroscope;
	private EulerAngleRotation initialBodyFrameOrientation;
	private EulerAngleRotation currentBodyFrameOrientation;
	private IRotationChangeSubscriber subscriber = null;
	private GyroscopeMeasurement measurementBias;
	
	///Constructor
	public RotationMonitor(IGyroscope gyroscope, 
			EulerAngleRotation initialBodyFrameOrientation){
		this.gyroscope = gyroscope;
		this.initialBodyFrameOrientation = initialBodyFrameOrientation;
		this.currentBodyFrameOrientation = initialBodyFrameOrientation;
	}
	
	public void AddSubscriber(IRotationChangeSubscriber subscriber){
		this.subscriber = subscriber;
	}	
			 
	public EulerAngleRotation waitForNextRotationUpdate() {

		GyroscopeMeasurement gyroscopeMeasurement = gyroscope.getNextMeasurement();

		if(gyroscopeMeasurement != null){
			//Calculate the rotation
			
			//Always leave the measurement bias as null.
			/*if(measurementBias == null)
			{
				measurementBias = gyroscopeMeasurement;
			}*/

			currentBodyFrameOrientation = RotationUtilities.calculateOrientation(gyroscopeMeasurement, currentBodyFrameOrientation, measurementBias);

			if(subscriber != null){

				subscriber.OrientationChanged(currentBodyFrameOrientation);
			}
		}

		return currentBodyFrameOrientation;
	}
	
	EulerAngleRotation getCurrentBodyFrameOrientation() {
		
		return currentBodyFrameOrientation;
	}

}
