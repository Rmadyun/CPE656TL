/**
 * 
 */
package TestNavigation;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * @author death
 * Test implementation of the gyroscope sensor
 */
public class TestGyroscope implements IGyroscope {

	private final Queue<GyroscopeMeasurement> measurementQueue;
	
	///Constructor
	public TestGyroscope(List<GyroscopeMeasurement> measurements){

		Queue<GyroscopeMeasurement> measurementQueue = new LinkedBlockingQueue<GyroscopeMeasurement>();
		for(GyroscopeMeasurement measurement : measurements){
			measurementQueue.add(measurement);
		}
		
		this.measurementQueue = measurementQueue;
	}

	/* (non-Javadoc)
	 * @see TestNavigation.IGyroscope#getNextMeasurement()
	 */
	@Override
	public GyroscopeMeasurement getNextMeasurement() {
		GyroscopeMeasurement measurement = null;
		
		if(!measurementQueue.isEmpty()){
			
			measurement = measurementQueue.remove();
		}
		
		return measurement;
	}

}
