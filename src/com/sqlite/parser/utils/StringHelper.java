package com.sqlite.parser.utils;

/**
 * Utility class for generating strings
 * @author memtrip
 */
public class StringHelper {
	public final static String CONSOLE_START_SYMBOL = " > ";
	
	private final static String JAVA_TYPE_STRING = "String";
	private final static String JAVA_TYPE_INTEGER = "int";
	private final static String JAVA_TYPE_LONG = "long";
	private final static String JAVA_TYPE_DOUBLE = "double";
	private final static String JAVA_TYPE_OBJECT_INTEGER = "Integer";
	private final static String JAVA_TYPE_OBJECT_LONG = "Long";
	private final static String JAVA_TYPE_OBJECT_DOUBLE = "Double";
	private final static String JAVA_TYPE_BYTE_ARRAY = "byte[]";
	public final static String SQL_TYPE_VARCHAR = "varchar";
	public final static String SQL_TYPE_INTEGER = "integer";
	public final static String SQL_TYPE_LONG = "long";
	public final static String SQL_TYPE_REAL = "real";
	public final static String SQL_TYPE_BLOB = "blob";
	private final static String EMPTY = "";
	
	/**
	 * Return a string concatenation of "\t" based on the numberOfTabs
	 * @param	numberOfTabs	The amount of "\t" to return
	 * @return	Concatenation of "\t"
	 */
	public static String tab(int numberOfTabs) {
		String tabOutput = EMPTY;
		
		for (int i = 0; i < numberOfTabs; i++) {
			tabOutput += "\t";
		}
		
		return tabOutput;
	}
	
	/**
	 * Return a string concatenation of "\n" based on the numberOfNewLines
	 * @param	numberOfNewLines	The amount of "\n" to return
	 * @return	Concatenation of "\n"
	 */
	public static String newLine(int numberOfNewLines) {
		String newLineOutput = EMPTY;
		
		for (int i = 0; i < numberOfNewLines; i++) {
			newLineOutput += "\n";
		}
		
		return newLineOutput;
	}
	
	/**
	 * Returns the provided text with its first letter in upper or lower case,
	 * depending on what is provided for the "upper" parameter.
	 * @param	text	A string value
	 * @param	upper	Whether the first letter should be upper or lower case
	 * @return	The text with its first letter in upper or lower case
	 */
	public static String firstLetterToCase(String text, boolean upper) {
	    char[] stringArray = text.toCharArray();
	    
	    if (text.length() >= 1) {
	    	if (upper) {
	    		stringArray[0] = Character.toUpperCase(stringArray[0]);
	    	} else {
	    		stringArray[0] = Character.toLowerCase(stringArray[0]);	
	    	}
	    }
	    
	    return new String(stringArray);
	}
	
	/**
	 * Convert the provided sqlite primitive data type into a java primitive type
	 * @param 	sqliteType	The sqlite primitive data type to convert into a java primitive type
	 * @return	The java primitive data type that relates to the provided sqliteType
	 */	
	public static String convertSQLiteTypeToJavaType(String sqliteType) {
		String javaType = "";
		
		if (sqliteType.equals(SQL_TYPE_VARCHAR)) {
			javaType = JAVA_TYPE_STRING;
		} else if (sqliteType.equals(SQL_TYPE_INTEGER)) {
			javaType = JAVA_TYPE_INTEGER;
		} else if (sqliteType.equals(SQL_TYPE_LONG)) {
			javaType = JAVA_TYPE_LONG;
		} else if (sqliteType.equals(SQL_TYPE_REAL)) {
			javaType = JAVA_TYPE_DOUBLE;
		} else if (sqliteType.equals(SQL_TYPE_BLOB)) {
			javaType = JAVA_TYPE_BYTE_ARRAY;
		} else {
			javaType = JAVA_TYPE_STRING;
		}
		
		return javaType;
	}
	
	/**
	 * Convert the provided sqlite primitive data type into a java object type
	 * @param 	sqliteType	The sqlite primitive data type to convert into a java object type
	 * @return	The java object data type that relates to the provided sqliteType
	 */	
	public static String convertSQLiteTypeToJavaObjectType(String sqliteType) {
		String javaType = "";
		
		if (sqliteType.equals(SQL_TYPE_VARCHAR)) {
			javaType = JAVA_TYPE_STRING;
		} else if (sqliteType.equals(SQL_TYPE_INTEGER)) {
			javaType = JAVA_TYPE_OBJECT_INTEGER;
		} else if (sqliteType.equals(SQL_TYPE_LONG)) {
			javaType = JAVA_TYPE_OBJECT_LONG;
		} else if (sqliteType.equals(SQL_TYPE_REAL)) {
			javaType = JAVA_TYPE_OBJECT_DOUBLE;
		} else {
			javaType = JAVA_TYPE_STRING;
		}
		
		return javaType;
	}
}
