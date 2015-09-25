package TestNavigation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GyroscopeReader {
	
	private String filename;
	
	private List<GyroscopeMeasurement> gyroscopeMeasurements;
	
	public GyroscopeReader(String filename){
		this.filename = filename;
		gyroscopeMeasurements = new ArrayList<GyroscopeMeasurement>();
		
		try {
			final int numberOfColumns = 4;
			final int rotationAroundXColumnIndex = 0;
			final int rotationAroundYColumnIndex = 1;
			final int rotationAroundZColumnIndex = 2;
			final int timeColumnIndex = 3;
			
			FileReader fr = new FileReader(filename);
			BufferedReader reader = new BufferedReader(fr);
			String currentRow = "";
				
			do
			{
				try {
					currentRow = reader.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					currentRow = "";
				}

				if(currentRow != null && !currentRow.isEmpty()){
					String[] segments;
					segments = currentRow.split(",");

					if(segments.length >= numberOfColumns){

						double rotationAroundX = Double.parseDouble(segments[rotationAroundXColumnIndex]);
						double rotationAroundY = Double.parseDouble(segments[rotationAroundYColumnIndex]);
						double rotationAroundZ = Double.parseDouble(segments[rotationAroundZColumnIndex]);
						double deltaTimeInSeconds = Double.parseDouble(segments[timeColumnIndex]);

						GyroscopeMeasurement gyroscopeMeasurement = new GyroscopeMeasurement(rotationAroundX, rotationAroundY, rotationAroundZ, deltaTimeInSeconds);

						gyroscopeMeasurements.add(gyroscopeMeasurement);
					}
				}
			}
			while(currentRow != null && !currentRow.isEmpty());
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		/*try ( BufferedReader reader =
			      new BufferedReader(new FileReader(filename))) {
			    String line = null;
			    while ((line = reader.readLine()) != null) {
			        System.out.println(line);
			    }
			} catch (IOException x) {
			    System.err.println(x);
			} */
	}
	
    List<GyroscopeMeasurement> getGyroscopeMeasurements(){
    	return gyroscopeMeasurements;
    }
	
}
