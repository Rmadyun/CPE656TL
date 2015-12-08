package TestNavigation;

public class Calculate{
	
public static double round(double initialValue, int numberOfFloatingPointDigits){
		
		int divisor = 10 *numberOfFloatingPointDigits;
		
		int dividend = (int) (initialValue * divisor);
		
		return ((double)dividend)/((double) divisor);
		
	}
	
}