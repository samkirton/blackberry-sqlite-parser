package com.sqlite.parser.generate;

import java.io.File;
import java.util.ArrayList;

import com.sqlite.parser.base.Generation;
import com.sqlite.parser.parse.SQLColumn;
import com.sqlite.parser.parse.SQLSchemaObjectRepresentation;
import com.sqlite.parser.parse.SQLTable;
import com.sqlite.parser.utils.StringHelper;

/**
 * Generate the BlackBerry model file
 * @author samkirton
 */
public class BlackBerryModelFile extends Generation {
	private String filePath;
	private String packageName;
	private SQLSchemaObjectRepresentation sqlSchemaObjectRepresentation;
	
	public BlackBerryModelFile(String filePath, String packageName, SQLSchemaObjectRepresentation sqlSchemaObjectRepresentation) {
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
			
			// ^SQLColumn[]
			ArrayList<SQLColumn> sqlColumnCollection = sqlTable.getColumns();
			for (int x = 0; x < sqlColumnCollection.size(); x++) {
				
			}
			
			fileOutput += "package " + packageName + ";";
			fileOutput += StringHelper.newLine(2);
			fileOutput += "import com.app.sqlite.base.BaseModel;";
			fileOutput += StringHelper.newLine(1);
			fileOutput += "import com.app.sqlite.base.DatabaseField;";
			fileOutput += StringHelper.newLine(1);
			fileOutput += "import java.util.Hashtable;";
			fileOutput += StringHelper.newLine(2);
			fileOutput += "public class " + fileName + " extends BaseModel {";
			fileOutput += StringHelper.newLine(1);
			fileOutput += buildClassConstants(sqlColumnCollection);
			fileOutput += StringHelper.newLine(1);
			fileOutput += buildClassProperties(sqlColumnCollection);
			fileOutput += StringHelper.tab(1) + "public " + fileName + "() {";
			fileOutput += StringHelper.newLine(1);
			fileOutput += StringHelper.tab(2) + "super();";
			fileOutput += StringHelper.newLine(1);
			fileOutput += buildConstructor(sqlColumnCollection);
			fileOutput += StringHelper.tab(1) + "}";
			fileOutput += StringHelper.newLine(2);
			fileOutput += StringHelper.tab(1) + "public String getTableName() {";
			fileOutput += StringHelper.newLine(1);
			fileOutput += StringHelper.tab(2) + "return \"" + tableName + "\";";
			fileOutput += StringHelper.newLine(1);
			fileOutput += StringHelper.tab(1) + "}";
			fileOutput += StringHelper.newLine(2);
			fileOutput += StringHelper.tab(1) + "public Hashtable getFields() {";
			fileOutput += StringHelper.newLine(1);
			fileOutput += StringHelper.tab(2) + "return fields;";
			fileOutput += StringHelper.newLine(1);
			fileOutput += StringHelper.tab(1) + "}";
			fileOutput += StringHelper.newLine(1);
			
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
	 * Build the model constructor that provides the BaseModel with the model field data
	 * @param	sqlColumnCollection	The collection to build the constructor with
	 * @return	A string containing the constructor built
	 */
	private String buildConstructor(ArrayList<SQLColumn> sqlColumnCollection) {
		String constructor = "";
		
		for (int i = 0; i < sqlColumnCollection.size(); i++) {
			SQLColumn sqlColumn = sqlColumnCollection.get(i);
			String name = sqlColumn.getName();
			String type = sqlColumn.getType();
			boolean isPrimaryKey = sqlColumn.isPrimaryKey();
			
			if (!isPrimaryKey) {
				constructor += StringHelper.tab(2) + "fields.put(FIELD_" + name.toUpperCase() + ",  new DatabaseField(" + getBaseModelDataTypeConstant(type) + "));";
				constructor += StringHelper.newLine(1);
			}
		}
		
		return constructor;
	}
	
	/**
	 * Build a series of field constants that store the table field names
	 * @param	sqlColumnCollection	Build the field constants based on this collection
	 * @return	A string containing a series of field constants that represent the provided sqlColumnCollection column names
	 */
	private String buildClassConstants(ArrayList<SQLColumn> sqlColumnCollection) {
		String classVariables = "";
		
		for (int i = 0; i < sqlColumnCollection.size(); i++) {
			SQLColumn sqlColumn = sqlColumnCollection.get(i);
			String name = sqlColumn.getName();
			boolean isPrimaryKey = sqlColumn.isPrimaryKey();
			
			if (!isPrimaryKey) {
				classVariables += StringHelper.tab(1) + "public static final String FIELD_" + name.toUpperCase() + " = \"" + name + "\";";
				classVariables += StringHelper.newLine(1);
			}
		}
		
		return classVariables;
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
			boolean isPrimaryKey = sqlColumn.isPrimaryKey();
			
			if (!isPrimaryKey) {
				classProperties += StringHelper.tab(1) + "public " + StringHelper.convertSQLiteTypeToJavaType(type) + " get" + StringHelper.firstLetterToCase(name, true) + "() {";
				classProperties += StringHelper.newLine(1);
				classProperties += StringHelper.tab(2) + "return (" + StringHelper.convertSQLiteTypeToJavaObjectType(type) + ")";
				classProperties += "((DatabaseField)fields.get(FIELD_" + name.toUpperCase() + ")).getValue();";
				classProperties += StringHelper.newLine(1);
				classProperties += StringHelper.tab(1) + "}";
				classProperties += StringHelper.newLine(2);
				
				classProperties += StringHelper.tab(1) + "public void set" + StringHelper.firstLetterToCase(name, true) + "(" + StringHelper.convertSQLiteTypeToJavaType(type) + " newVal) {";
				classProperties += StringHelper.newLine(1);
				classProperties += StringHelper.tab(2) + "((DatabaseField)fields.get(FIELD_" + name.toUpperCase() + "))";
				classProperties += ".setValue(new " + StringHelper.convertSQLiteTypeToJavaObjectType(type) + "(newVal));";
				classProperties += StringHelper.newLine(1);
				classProperties += StringHelper.tab(1) + "}";
				classProperties += StringHelper.newLine(2);
			}
		}
		
		return classProperties;
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
			constantType = "TYPE_STRING";
		} else if (type.equals(StringHelper.SQL_TYPE_LONG)) {
			constantType = "TYPE_LONG";
		} else if (type.equals(StringHelper.SQL_TYPE_INTEGER)) {
			constantType = "TYPE_INTEGER";
		} else if (type.equals(StringHelper.SQL_TYPE_FLOAT)) {
			constantType = "TYPE_FLOAT";
		} else {
			constantType = "TYPE_STRING";
		}
		
		return constantType;
	}
}
