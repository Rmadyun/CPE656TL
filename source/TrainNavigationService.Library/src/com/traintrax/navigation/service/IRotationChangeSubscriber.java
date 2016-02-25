package com.traintrax.navigation.service;

public interface IRotationChangeSubscriber {
	
	void OrientationChanged(EulerAngleRotation newBodyFrameOrientation);
	
}
