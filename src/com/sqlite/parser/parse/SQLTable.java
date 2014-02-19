package com.sqlite.parser.parse;

import java.util.ArrayList;

/**
 * An object representation of a SQLite table
 * @author samkirton
 */
public class SQLTable {
	private String tableName;
	private ArrayList<SQLColumn> columns;
	
	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String newVal) {
		tableName = newVal;
	}
	
	public ArrayList<SQLColumn> getColumns() {
		return columns;
	}
	
	public SQLTable() {
		columns = new ArrayList<SQLColumn>();
	}
}
