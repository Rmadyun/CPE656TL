package com.traintrax.navigation.service.mdu;

import com.traintrax.navigation.service.rotation.EulerAngleRotation;

public interface IRotationChangeSubscriber {
	
	void OrientationChanged(EulerAngleRotation newBodyFrameOrientation);
	
}
