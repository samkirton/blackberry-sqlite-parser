package com.sqlite.parser.generate;

import java.io.File;
import java.util.ArrayList;

import com.sqlite.parser.base.Generation;
import com.sqlite.parser.parse.SQLColumn;
import com.sqlite.parser.parse.SQLSchemaObjectRepresentation;
import com.sqlite.parser.parse.SQLTable;
import com.sqlite.parser.utils.StringHelper;

/**
 * Generate the Android model file
 * @author samkirton
 */
public class AndroidModelFile extends Generation {
	private String filePath;
	private String packageName;
	private SQLSchemaObjectRepresentation sqlSchemaObjectRepresentation;
	
	public AndroidModelFile(String filePath, String packageName, SQLSchemaObjectRepresentation sqlSchemaObjectRepresentation) {
		this.filePath = filePath;
		this.packageName = packageName;
		this.sqlSchemaObjectRepresentation = sqlSchemaObjectRepresentation;
	}
	
	@Override
	public void buildFileOutput() {
		ArrayList<SQLTable> sqlTableCollection = sqlSchemaObjectRepresentation.getSQLiteTableCollection();
		
		// ^ SQLTable[] - Generate a Model for each table
		for (int i = 0; i < sqlTableCollection.size(); i++) {
			String fileOutput = EMPTY;
			
			SQLTable sqlTable = sqlTableCollection.get(i);
			String tableName = sqlTable.getTableName();
			String fileName = tableName + "Model";
			
			ArrayList<SQLColumn> sqlColumnCollection = sqlTable.getColumns();
			
			fileOutput += "package " + packageName + ";";
			fileOutput += StringHelper.newLine(2);
			fileOutput += "import com.app.sqlite.base.BaseModel;";
			fileOutput += StringHelper.newLine(2);
			fileOutput += "import java.util.HashMap;";
			fileOutput += StringHelper.newLine(2);
			fileOutput += "import android.content.ContentValues;";
			fileOutput += StringHelper.newLine(2);
			fileOutput += "public class " + fileName + " extends BaseModel {";
			fileOutput += StringHelper.newLine(1);
			fileOutput += buildClassVariables(sqlColumnCollection);
			fileOutput += StringHelper.newLine(1);
			fileOutput += buildClassConstants(sqlColumnCollection);
			fileOutput += StringHelper.newLine(1);
			fileOutput += StringHelper.tab(1) + "// The SQL provider uses reflection to retrieve the table name from this variable";
			fileOutput += StringHelper.newLine(1);
			fileOutput += StringHelper.tab(1) + "public static final String TABLE_NAME = \"" + tableName + "\";";
			fileOutput += StringHelper.newLine(2);
			fileOutput += buildClassProperties(sqlColumnCollection);
			fileOutput += StringHelper.newLine(1);
			fileOutput += buildHashmapMethod(sqlColumnCollection);
			fileOutput += StringHelper.newLine(1);
			fileOutput += buildContentValueMethod(sqlColumnCollection);			
			fileOutput += "}";
			
			// Create namespace folder if it does not already exist
			String createFilePath = filePath;
			boolean success = (new File(createFilePath)).mkdirs();
			if (success) {
				System.out.print(StringHelper.CONSOLE_START_SYMBOL);
				System.out.println("Created: " + createFilePath + " BlackBerry model directory");
			}
			
			persistFileOuput(fileOutput, createFilePath, fileName, JAVA_EXT);
		}
	}
	
	/**
	 * Build a string that contains a class variable for each column in provided collection
	 * @param	sqlColumnCollection	Build the variables based on this collection
	 * @return	A string containing a series of properties that represent the provided sqlColumnCollection column names
	 */
	private String buildClassVariables(ArrayList<SQLColumn> sqlColumnCollection) {
		String classVariables = "";
		
		for (int i = 0; i < sqlColumnCollection.size(); i++) {
			SQLColumn sqlColumn = sqlColumnCollection.get(i);
			String name = sqlColumn.getName();
			String type = sqlColumn.getType();
			
			classVariables += StringHelper.tab(1) + "private " + StringHelper.convertSQLiteTypeToJavaType(type) + " " + name + ";";
			classVariables += StringHelper.newLine(1);
		}
		
		return classVariables;
	}
	
	/**
	 * Build a string that contains a class column constant for each column in provided collection
	 * @param	sqlColumnCollection	Build the constants based on this collection
	 * @return	A string containing a series of constants that represent the provided sqlColumnCollection column names
	 */
	private String buildClassConstants(ArrayList<SQLColumn> sqlColumnCollection) {
		String classConstants = "";
		
		for (int i = 0; i < sqlColumnCollection.size(); i++) {
			SQLColumn sqlColumn = sqlColumnCollection.get(i);
			String name = sqlColumn.getName();
			boolean isPrimaryKey = sqlColumn.isPrimaryKey();
			
			if (!isPrimaryKey) {
				classConstants += StringHelper.tab(1) + "private static final String COLUMN_" + name.toUpperCase() + " = \"" + name + "\";";
				classConstants += StringHelper.newLine(1);
			}
		}
		
		return classConstants;
	}
	
	
	/**
	 * Build a series of class properties that are used to retrieve and set model values
	 * @param 	sqlColumnCollection	Build the class properties based on this collection
	 * @return	A string containing a series of class properties that represent the provided sqlColumnCollection
	 */
	public String buildClassProperties(ArrayList<SQLColumn> sqlColumnCollection) {
		String classProperties = "";
		
		for (int i = 0; i < sqlColumnCollection.size(); i++) {
			SQLColumn sqlColumn = sqlColumnCollection.get(i);
			String name = sqlColumn.getName();
			String type = sqlColumn.getType();
			
			classProperties += StringHelper.tab(1) + "public " + StringHelper.convertSQLiteTypeToJavaType(type) + " get" + StringHelper.firstLetterToCase(name, true) + "() {";
			classProperties += StringHelper.newLine(1);
			classProperties += StringHelper.tab(2) + "return " + name + ";";
			classProperties += StringHelper.newLine(1);
			classProperties += StringHelper.tab(1) + "}";
			classProperties += StringHelper.newLine(2);
			
			classProperties += StringHelper.tab(1) + "public void set" + StringHelper.firstLetterToCase(name, true) + "(" + StringHelper.convertSQLiteTypeToJavaType(type) + " newVal) {";
			classProperties += StringHelper.newLine(1);
			classProperties += StringHelper.tab(2) + name + " = newVal;";
			classProperties += StringHelper.newLine(1);
			classProperties += StringHelper.tab(1) + "}";
			classProperties += StringHelper.newLine(1);
		}
		
		return classProperties;
	}
	
	/**
	 * Build a string that contains a method that builds the modelColumns hashmap
	 * @param	sqlColumnCollection	Build the constants based on this collection
	 * @return	A string containing a series of constants that represent the provided sqlColumnCollection column names
	 */
	private String buildHashmapMethod(ArrayList<SQLColumn> sqlColumnCollection) {
		String hashmapMethod = "";
		
		hashmapMethod += StringHelper.tab(1) + "@Override";
		hashmapMethod += StringHelper.newLine(1);
		hashmapMethod += StringHelper.tab(1) + "public HashMap<String,Integer> getModelColumnTypeMap() {";
		hashmapMethod += StringHelper.newLine(1);
		hashmapMethod += StringHelper.tab(2) + "HashMap<String,Integer> modelColumns = new HashMap<String,Integer>();";
		hashmapMethod += StringHelper.newLine(1);
		
		for (int i = 0; i < sqlColumnCollection.size(); i++) {
			SQLColumn sqlColumn = sqlColumnCollection.get(i);
			String name = sqlColumn.getName();
			String type = sqlColumn.getType();
			boolean isPrimaryKey = sqlColumn.isPrimaryKey();
			
			if (!isPrimaryKey) {
				hashmapMethod += StringHelper.tab(2) + "modelColumns.put(COLUMN_" + name.toUpperCase() + ", " + getBaseModelDataTypeConstant(type) + ");";
				hashmapMethod += StringHelper.newLine(1);
			}
		}
		
		hashmapMethod += StringHelper.tab(2) + "return modelColumns;";
		hashmapMethod += StringHelper.newLine(1);
		hashmapMethod += StringHelper.tab(1) + "}";
		hashmapMethod += StringHelper.newLine(1);
		
		return hashmapMethod;
	}
	
	/**
	 * Build a string that contains a method that builds the modelColumns hashmap
	 * @param	sqlColumnCollection	Build the constants based on this collection
	 * @return	A string containing a series of constants that represent the provided sqlColumnCollection column names
	 */
	private String buildContentValueMethod(ArrayList<SQLColumn> sqlColumnCollection) {
		String hashmapMethod = "";
		
		hashmapMethod += StringHelper.tab(1) + "@Override";
		hashmapMethod += StringHelper.newLine(1);
		hashmapMethod += StringHelper.tab(1) + "public ContentValues toContentValues() {";
		hashmapMethod += StringHelper.newLine(1);
		hashmapMethod += StringHelper.tab(2) + "ContentValues contentValues = new ContentValues();";
		hashmapMethod += StringHelper.newLine(1);
		
		for (int i = 0; i < sqlColumnCollection.size(); i++) {
			SQLColumn sqlColumn = sqlColumnCollection.get(i);
			String name = sqlColumn.getName();
			boolean isPrimaryKey = sqlColumn.isPrimaryKey();
			
			if (!isPrimaryKey) {
				hashmapMethod += StringHelper.tab(2) + "contentValues.put(COLUMN_" + name.toUpperCase() + ", get" + StringHelper.firstLetterToCase(name, true) + "());";
				hashmapMethod += StringHelper.newLine(1);
			}
		}
		
		hashmapMethod += StringHelper.tab(2) + "return contentValues;";
		hashmapMethod += StringHelper.newLine(1);
		hashmapMethod += StringHelper.tab(1) + "}";
		hashmapMethod += StringHelper.newLine(1);
		
		return hashmapMethod;
	}
	
	/**
	 * Return the BaseModel datatype associated with the provided sqlite type
	 * @param	type	The sqlite type
	 * @param	isPrimaryKey	Is the type a primary key?
	 * @return	The BaseModel constant type
	 */
	private String getBaseModelDataTypeConstant(String type) {
		String constantType = null;
		
		if (type.equals(StringHelper.SQL_TYPE_VARCHAR)) {
			constantType = "BaseModel.FIELD_STRING";
		} else if (type.equals(StringHelper.SQL_TYPE_LONG)) {
			constantType = "BaseModel.FIELD_LONG";
		} else if (type.equals(StringHelper.SQL_TYPE_INTEGER)) {
			constantType = "BaseModel.FIELD_INTEGER";
		} else if (type.equals(StringHelper.SQL_TYPE_FLOAT)) {
			constantType = "BaseModel.FIELD_FLOAT";
		} else if (type.equals(StringHelper.SQL_TYPE_BLOB)) {
			constantType = "BaseModel.FIELD_BLOB";
		} else {
			constantType = "BaseModel.FIELD_STRING";
		}
		
		return constantType;
	}
}
