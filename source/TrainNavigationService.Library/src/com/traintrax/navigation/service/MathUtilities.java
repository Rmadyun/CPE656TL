/* Corey Sanders
 * CPE 653
 * Math Utilities class is responsible for providing access to Math operations frequently
 * used by the Navigation Service
 */
 
package com.traintrax.navigation.service;

import java.util.Arrays;

///Class stores frequently used Math operations for the Navigation Service
public class MathUtilities{
	
	//Interpolate what the value would be for a given X value.
	//This method assumes linear interpolation.
	//This also assumes that x and y are parallel arrays that have already
	//been sorted in increasing order by x.
	public static double LinearInterpolate(double x[], double y[], double requestedX) throws Exception{
		double requestedY = 0;
		double m = 0;
		double b = 0;
		
		if(x.length < 1 || y.length < 1 || x.length != y.length){
			throw new Exception("Invalid arguments");
		}
		
		if(x.length == 1)
		{
			m = 0;
			b = y[0] - m * x[0];
		}
		else if(requestedX <= x[0]) //lower bound
		{
			m = ((y[1] - y[0])/(x[1] - x[0]));	
			b = y[0] - m * x[0];
		}
		else if(requestedX >= x[x.length - 1]){ //upper bound
		    m = ((y[y.length-1] - y[y.length-2])/(x[x.length-1] - x[x.length-2]));
			
			b = y[y.length-1] - m * x[x.length-1];
		}
		else{ //between bounds
			int i = 0;
			//Find index of upper bound.
			while(x[i] < requestedX){
				i++;
			}

		    m = ((y[i] - y[i-1])/(x[i] - x[i-1]));	
			b = y[i] - m * x[i];
		}
		
		requestedY = m*requestedX + b;
		
		return requestedY;
		
	}
}

