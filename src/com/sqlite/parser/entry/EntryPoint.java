package com.sqlite.parser.entry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.sqlite.parser.generate.AndroidModelFile;
import com.sqlite.parser.generate.BlackBerryModelFile;
import com.sqlite.parser.parse.SQLParser;
import com.sqlite.parser.parse.SQLSchemaObjectRepresentation;
import com.sqlite.parser.utils.OSHelper;
import com.sqlite.parser.utils.StringHelper;

/**
 * EntryPoint
 * @author samkirton
 */
public class EntryPoint {
	private static final String SQLITE_WINDOWS_FILE_PATH = "C:\\tmp\\database.db";
	private static final String SQLITE_WINDOWS_OUTPUT_PATH = "C:\\tmp\\";
	private static final String ROOT_WINDOWS_SEPERATOR = "\\";
	private static final String SQLITE_MAC_FILE_PATH = "/tmp/database.db";
	private static final String SQLITE_MAC_OUTPUT_PATH = "/tmp/";
	private static final String ROOT_MAC_SEPERATOR = "/";
	private static final String PACKAGE_NAME = "com.memtrip.app.sql.model";
	
	public static void main(String[] args) throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		
		String databaseFilePath = null;
		String rootDir = null;
		String pathSeparator = null;
		
		if (OSHelper.isWindows()) {
			databaseFilePath = SQLITE_WINDOWS_FILE_PATH;
			rootDir = SQLITE_WINDOWS_OUTPUT_PATH;
			pathSeparator = ROOT_WINDOWS_SEPERATOR;
		} else if (OSHelper.isMac()) {
			databaseFilePath = SQLITE_MAC_FILE_PATH;
			rootDir = SQLITE_MAC_OUTPUT_PATH;
			pathSeparator = ROOT_MAC_SEPERATOR;
		}
		
		Connection connection = null;
		SQLSchemaObjectRepresentation sqlSchemaObjectRepresentation = null;
		
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFilePath);
			sqlSchemaObjectRepresentation = new SQLParser(connection).getSQLSchemaObjectRepresentation();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection);
		}
		
		if (sqlSchemaObjectRepresentation != null) {
			generateSource(rootDir,pathSeparator,PACKAGE_NAME,sqlSchemaObjectRepresentation);
		} else {
			System.out.println("Error: No data written");
		}
	}
	
	/**
	 * Generate source code based on the SQLite database provided.
	 * The source code files are created under the rootDir provided.
	 * @param 	rootDir	The root dir of where the source code is being stored
	 * @param	pathSeparator	The path separator of the host OS
	 * @param	packageName	The package that the source code will be generated under
	 * @param	sqlSchemaObjectRepresentation	Object representation of the solution SQLite database
	 */
	private static void generateSource(String rootDir, 
			String pathSeparator, 
			String packageName,
			SQLSchemaObjectRepresentation sqlSchemaObjectRepresentation) {
		
		String blackBerryDir = rootDir + "generated" + pathSeparator + "blackberry" + pathSeparator + "model" + pathSeparator;
		BlackBerryModelFile blackBerryModelFile = new BlackBerryModelFile(blackBerryDir, PACKAGE_NAME, sqlSchemaObjectRepresentation);
		blackBerryModelFile.buildFileOutput();
		
		String androidDir = rootDir + "generated" + pathSeparator + "android" + pathSeparator + "model" + pathSeparator;
		AndroidModelFile androidModelFile = new AndroidModelFile(androidDir, PACKAGE_NAME, sqlSchemaObjectRepresentation);
		androidModelFile.buildFileOutput();
		
		System.out.println(StringHelper.CONSOLE_START_SYMBOL + "SQLite code generated");
	}

	/**
	 * Attempt to close the connection of the provide object reference
	 * @param 	connection	The connection object reference
	 */
	private static void closeConnection(Connection connection) {
		try {
			// close database
			if (connection != null)
				connection.close();
		} catch (SQLException e) { }
	}
}
