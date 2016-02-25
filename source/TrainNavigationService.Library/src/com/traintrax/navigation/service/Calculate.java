package com.traintrax.navigation.service;

public class Calculate{
	
public static double round(double initialValue, int numberOfFloatingPointDigits) throws IllegalArgumentException {
		
		long divisor = 1;
		final int MaxRequestedDigits = 18;
		
		if((numberOfFloatingPointDigits > MaxRequestedDigits)||(numberOfFloatingPointDigits < 0)){
			String message = String.format("Number of requested digits is out of bounds. Max Supported Digits is %d. Requestd Value is %d.", MaxRequestedDigits, numberOfFloatingPointDigits);
			throw new IllegalArgumentException(message);
		}
		
		for(int i = 0; i < numberOfFloatingPointDigits; i++)
		{
		    divisor = 10 * divisor;
		}
		
		long dividend = (long) (initialValue * divisor);
		
		return ((double)dividend)/((double) divisor);
		
	}
	
}