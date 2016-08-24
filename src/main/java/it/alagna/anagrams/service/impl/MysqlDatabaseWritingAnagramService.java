package it.alagna.anagrams.service.impl;

public class MysqlDatabaseWritingAnagramService extends DatabaseWritingAnagramService
{
	public static final String DATABASE_URL = "jdbc:mysql://localhost/";
	public static final String DATABASE_NAME = "anagramsdb";
	public static final String DATABASE_USERNAME = "anagramsuser";
	public static final String DATABASE_PASSWORD = "anagramspassword";
	public static final String DATABASE_ROOT_USERNAME = "root";
	public static final String DATABASE_ROOT_PASSWORD = "";
	public static final String DATABASE_OPTIONS = "useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

	public static final String DATABASE_TABLE = "anagrams";
	public static final String DATABASE_KEY_COLUMN = "basestring";
	public static final String DATABASE_VALUE_COLUMN = "value";

	public static final String DATABASE_INIT_SCRIPT[] =
	{
		"CREATE DATABASE " + DATABASE_NAME + ";",
		"USE " + DATABASE_NAME + ";",
		"CREATE USER '" + DATABASE_USERNAME + "'@'localhost' IDENTIFIED BY '" + DATABASE_PASSWORD + "';",
		"GRANT ALL PRIVILEGES ON " + DATABASE_NAME + ".* TO '" + DATABASE_USERNAME + "'@'localhost';",
		"CREATE TABLE " + DATABASE_TABLE + " (" + DATABASE_KEY_COLUMN + " VARCHAR(30) NOT NULL, " + DATABASE_VALUE_COLUMN + " VARCHAR(30) NOT NULL);",
		"CREATE INDEX " + DATABASE_KEY_COLUMN + "_index ON " + DATABASE_TABLE + " (" + DATABASE_KEY_COLUMN + ");"
	};

	public static final String DATABASE_DROP_SCRIPT[] =
	{
		"DROP DATABASE " + DATABASE_NAME + ";",
		"DROP USER '" + DATABASE_USERNAME + "'@'localhost';",
	};

	@Override
	public void init() {
		setDatabaseUrl(DATABASE_URL + DATABASE_NAME + "?" + DATABASE_OPTIONS);
		setDatabaseUsername(DATABASE_USERNAME);
		setDatabasePassword(DATABASE_PASSWORD);
		setDatabaseRootUrl(DATABASE_URL + "?" + DATABASE_OPTIONS);
		setDatabaseRootUsername(DATABASE_ROOT_USERNAME);
		setDatabaseRootPassword(DATABASE_ROOT_PASSWORD);
		setDatabaseTable(DATABASE_TABLE);
		setDatabaseKeyColumn(DATABASE_KEY_COLUMN);
		setDatabaseValueColumn(DATABASE_VALUE_COLUMN);
		setDatabaseCreateScripts(DATABASE_INIT_SCRIPT);
		setDatabaseDropScripts(DATABASE_DROP_SCRIPT);
	}
}
