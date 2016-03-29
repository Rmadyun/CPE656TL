package com.traintrax.navigation.database.library;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * A collection of frequently used methods used to
 * creating SQL statements.
 * @author Corey Sanders
 *
 */
public class SqlUtilities {

	/**
	 * Creates a string in a format where SQL recognizes
	 * a given value as a string
	 * @param originalString Original string value to include
	 * @return SQL representation of the assigned string.
	 */
	public static String createSqlString(String originalString){
		return "'"+originalString+"'";
	}
	
	/**
	 * Creates a string in a format where SQL recognizes
	 * a given value as an integer
	 * @param originalValue Original integer value to include
	 * @return SQL representation of the assigned integer.
	 */
	public static String createSqlInt(String originalValue){
		String finalValue = originalValue.trim();
		finalValue = finalValue.isEmpty()? "NULL" : finalValue;

		return finalValue;
	}
	
	/**
	 * Creates a string in a format where SQL recognizes
	 * a given value as an integer
	 * @param originalValue Original integer value to include
	 * @return SQL representation of the assigned integer.
	 */
	public static String createSqlInt(int originalValue){
		return createSqlInt(Integer.toString(originalValue));
	}
	
	/**
	 * Creates a string in a format where SQL recognizes
	 * a given value as a double.
	 * @param originalValue Original double value to include
	 * @return SQL representation of the assigned double.
	 */
	public static String createSqlDouble(String originalValue){
		String finalValue = originalValue.trim();
		finalValue = finalValue.isEmpty()? "NULL" : finalValue;

		return finalValue;
	}
	
	/**
	 * Creates a string in a format where SQL recognizes
	 * a given value as an double
	 * @param originalValue Original double value to include
	 * @return SQL representation of the assigned double.
	 */
	public static String createSqlDouble(double originalValue){
		return createSqlDouble(Double.toString(originalValue));
	}


	/**
	 * Creates a string in a format where SQL recognizes
	 * a given value as an timestamp
	 * @param originalValue Original time value to include
	 * @return SQL representation of the assigned timestamp.
	 */
	public static String createSqlTimestamp(Calendar timeDetected) {
		
		//Conversion courtesy of: http://alvinalexander.com/java/java-timestamp-example-current-time-now
		Timestamp currentTimestamp = new java.sql.Timestamp(timeDetected.getTime().getTime());
		
		return currentTimestamp.toString();
	}
	
	/**
	 * Extracts a timestamp class from a SQL compatible timestamp string
	 * @param sqlTimestamp SQL representation of the assigned timestamp.
	 * @return Java class representation of the timestamp.
	 */
	public static Calendar parseSqlTimestamp(String sqlTimestamp) {
		
		Calendar timeDetected = Calendar.getInstance();
		
		//Using timestamp parsing though deprecated to expedite parsing
		
		//Use a DateFormat class instead. Recommend using SimpleDateFormat for parsing
		//http://stackoverflow.com/questions/18915075/java-convert-string-to-timestamp
		long millisecondsInUnixEpoch = Timestamp.parse(sqlTimestamp);
		
		timeDetected.setTimeInMillis(millisecondsInUnixEpoch);
		
		return timeDetected;
	}
}
