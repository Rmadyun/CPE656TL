package TrainTrax;

public interface IRotationChangeSubscriber {
	
	void OrientationChanged(EulerAngleRotation newBodyFrameOrientation);
	
}
