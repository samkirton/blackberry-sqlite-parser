package com.sqlite.parser.parse;

import java.util.ArrayList;

/**
 * An object representation of a SQLite schema
 * @author samkirton
 */
public class SQLSchemaObjectRepresentation {
	private ArrayList<SQLTable> sqliteTableCollection;
	
	public ArrayList<SQLTable> getSQLiteTableCollection() {
		return sqliteTableCollection;
	}
	
	public void setSQLiteTableCollection(ArrayList<SQLTable> newVal) {
		sqliteTableCollection = newVal;
	}
}
