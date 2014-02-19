package com.sqlite.parser.parse;

/**
 * An object representation of a SQLite column
 * @author samkirton
 */
public class SQLColumn {
	private String name;
	private String type;
	private boolean isPrimaryKey;
	
	public String getName() {
		return name;
	}
	
	public void setName(String newVal) {
		name = newVal;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String newVal) {
		type = newVal;
	}
	
	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}
	
	public void setIsPrimaryKey(boolean newVal) {
		isPrimaryKey = newVal;
	}
}
