package edu.uah.cpe.traintrax;

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
}
