package com.sqlite.parser.parse;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.sqlite.parser.utils.StringHelper;

/**
 * Query the provided connection to find a list of all tables, on each table
 * query it with the PRAGMA table_info() method. The results for each table in 
 * the query are used to populate a reference of SQLTable, which is then added to the 
 * SQLSchemaObjectRepresentation for use in a generator class
 * @author samkirton
 */
public class SQLParser {
	private SQLSchemaObjectRepresentation sqlSchemaObjectRepresentation;
	
	public SQLSchemaObjectRepresentation getSQLSchemaObjectRepresentation() {
		return sqlSchemaObjectRepresentation;
	}
	
	public SQLParser(Connection connection) throws SQLException {
		sqlSchemaObjectRepresentation = populateSQLSchemaObjectRepresentation(connection);
		
		System.out.print(StringHelper.CONSOLE_START_SYMBOL);
		System.out.println("Object representation created from SQLite schema");
	}
	
	public SQLSchemaObjectRepresentation populateSQLSchemaObjectRepresentation(Connection connection) throws SQLException {
		SQLSchemaObjectRepresentation sqlSchemaObjectRepresentation = new SQLSchemaObjectRepresentation();
		
		Statement tableStatement = connection.createStatement();
		tableStatement.setQueryTimeout(30);
		ResultSet tableResultSet = tableStatement.executeQuery("SELECT name FROM sqlite_master WHERE type='table';");
		
		ArrayList<SQLTable> sqlTableCollection = new ArrayList<SQLTable>();
		
		while(tableResultSet.next()) {
			String tableName = tableResultSet.getString("name");
			SQLTable sqlTable = new SQLTable();
			sqlTable.setTableName(tableName);
			
			Statement columnStatement = connection.createStatement();
			columnStatement.setQueryTimeout(30);
			ResultSet columnResultSet = columnStatement.executeQuery("PRAGMA table_info(" + tableName + ");");
			while (columnResultSet.next()) {
				SQLColumn sqlColumn = new SQLColumn();
				String columnName = columnResultSet.getString("name");
				String columnType = columnResultSet.getString("type");
				int pk = columnResultSet.getInt("pk");
				
				sqlColumn.setName(columnName);
				sqlColumn.setType(columnType);
				sqlColumn.setIsPrimaryKey(pk == 1);
				sqlTable.getColumns().add(sqlColumn);
			}
			
			sqlTableCollection.add(sqlTable);
		}
		
		sqlSchemaObjectRepresentation.setSQLiteTableCollection(sqlTableCollection);
		
		return sqlSchemaObjectRepresentation;
	}
}
