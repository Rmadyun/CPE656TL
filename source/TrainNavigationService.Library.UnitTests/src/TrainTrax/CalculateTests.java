package TrainTrax;

import static org.junit.Assert.*;

import org.junit.Test;

public class CalculateTests {

	@Test
	public void testRound() {		
		double testValue = 8.3123456789;
		double expectedValueFor1Digit = 8.3;
		double expectedValueFor2Digit = 8.31;
		double expectedValueFor0Digit = 8;
		double expectedValueForMatchingDigits = 8.3123456789;
		double expectedValueForGreaterThanMatchingDigits = 8.3123456789;
		double expectedValueForMaxDigits = 8.3123456789;
	    final int MatchingDigits = 10;
	    final int MaxDigits = 18;
		
		double roundedValue = Calculate.round(testValue, 1);
		
		org.junit.Assert.assertEquals(expectedValueFor1Digit, roundedValue, 0);
		
        roundedValue = Calculate.round(testValue, 2);
		
		org.junit.Assert.assertEquals(expectedValueFor2Digit, roundedValue, 0);

        roundedValue = Calculate.round(testValue, 0);
		
		org.junit.Assert.assertEquals(expectedValueFor0Digit, roundedValue, 0);
		
        roundedValue = Calculate.round(testValue, MatchingDigits);
		
		org.junit.Assert.assertEquals(expectedValueForMatchingDigits, roundedValue, 0);
		
        roundedValue = Calculate.round(testValue, MatchingDigits + 1);
		
		org.junit.Assert.assertEquals(expectedValueForGreaterThanMatchingDigits, roundedValue, 0);
		
        roundedValue = Calculate.round(testValue, MaxDigits);
		
		org.junit.Assert.assertEquals(expectedValueForMaxDigits, roundedValue, 0);
		
		try{
			roundedValue = Calculate.round(testValue, MaxDigits + 1);
			fail("Exception not thrown when exceeding max digits");
		}
		catch(IllegalArgumentException exception){
			
		}
		
		try{
			roundedValue = Calculate.round(testValue, -1);
			fail("Exception not thrown when receiving negative digits");
		}
		catch(IllegalArgumentException exception){
			
		}		
		
	}

}
