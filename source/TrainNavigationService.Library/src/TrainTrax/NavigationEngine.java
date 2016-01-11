package TrainTrax;



public class NavigationEngine {
	
	private Matrix rotationMatrix;
	
	
	public NavigationEngine(){
		
	}
	
	public static void PrintMatrix(Matrix matrix){
		
		for(int i = 0; i < matrix.getNumberOfRows(); i++){
			String rowString = "";
			for(int j = 0; j < matrix.getNumberOfColumns(); j++){
				rowString += String.format("%f  ", matrix.getValue(i,  j));
			}
			
			System.out.println(rowString);
		}
	}

	
};