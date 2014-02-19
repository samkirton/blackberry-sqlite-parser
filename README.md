java-sqlite-parser
==================
A tool that takes a SQLite database as an input and outputs a series of model files that represent the database. The model files can be used with the blackberry-sqlite-provider and android-sqlite-provider libraries found at http://github/samkirton/

To run the parser you must change the constants in com.sqlite.parser.entry EntryPoint.java file

SQLITE_WINDOWS_FILE_PATH
- Enter the full path of where your SQLite database resides  

SQLITE_WINDOWS_OUTPUT_PATH
- Enter the full path of where you would like the model classes to be generated  

PACKAGE_NAME
- The package where the generated model files will reside in your mobile project  
