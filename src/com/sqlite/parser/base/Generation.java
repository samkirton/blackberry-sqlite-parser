package com.sqlite.parser.base;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.sqlite.parser.utils.StringHelper;

/**
 * Base class that handles persisting the file output to a physical file,
 * it must be inherited by all generate classes
 * @author memtrip
 */
public abstract class Generation {
	protected static final String EMPTY = "";
	protected static final String JAVA_EXT = ".java";
	
	/**
	 * Persist the file output string to a file
	 * @param	fileOutput	The file output string that is being saved to a file
	 * @param	filePath	The path where the final file will reside
	 * @param	fileName	Name of the file
	 * @param	fileExtension	File extension 
	 */
	public void persistFileOuput(String fileOutput, String filePath, String fileName, String fileExtension) {		
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filePath + fileName + fileExtension));
			out.write(fileOutput);
			out.close();
		} catch (IOException e) {
			System.out.print(StringHelper.CONSOLE_START_SYMBOL);
			System.out.println("FAILED to write file " + fileName + fileExtension);		
			System.exit(0);
		}
		
		System.out.print(StringHelper.CONSOLE_START_SYMBOL);
		System.out.println(fileName + fileExtension + " has been written to: " + filePath);
	}
	
	/**
	 * This method should be implemented to build the file ouput,
	 * the final output should be saved to a file using PersistFileOutput()
	 */
	public abstract void buildFileOutput();
}
