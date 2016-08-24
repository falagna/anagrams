package it.alagna.anagrams.service.impl;

public class HsqlDatabaseWritingAnagramService extends DatabaseWritingAnagramService
{
	public static final String DATABASE_URL = "jdbc:hsqldb:file:";
	public static final String DATABASE_NAME = "anagramsdb";
	public static final String DATABASE_USERNAME = "sa";
	public static final String DATABASE_PASSWORD = "";

	public static final String DATABASE_TABLE = "anagrams";
	public static final String DATABASE_KEY_COLUMN = "basestring";
	public static final String DATABASE_VALUE_COLUMN = "value";

	public static final String DATABASE_INIT_SCRIPT[] =
	{
		"CREATE TABLE " + DATABASE_TABLE + " (" + DATABASE_KEY_COLUMN + " VARCHAR(30) NOT NULL, " + DATABASE_VALUE_COLUMN + " VARCHAR(30) NOT NULL);",
		"CREATE INDEX " + DATABASE_KEY_COLUMN + "_index ON " + DATABASE_TABLE + " (" + DATABASE_KEY_COLUMN + ");"
	};

	public static final String DATABASE_DROP_SCRIPT[] =
	{
		"DROP TABLE " + DATABASE_TABLE + ";"
	};

	@Override
	public void init() {
		final String localPath = getClass().getClassLoader().getResource("").getFile();
		setDatabaseUrl(DATABASE_URL + localPath + DATABASE_NAME);
		setDatabaseUsername(DATABASE_USERNAME);
		setDatabasePassword(DATABASE_PASSWORD);
		setDatabaseRootUrl(DATABASE_URL + localPath + DATABASE_NAME);
		setDatabaseRootUsername(DATABASE_USERNAME);
		setDatabaseRootPassword(DATABASE_PASSWORD);
		setDatabaseTable(DATABASE_TABLE);
		setDatabaseKeyColumn(DATABASE_KEY_COLUMN);
		setDatabaseValueColumn(DATABASE_VALUE_COLUMN);
		setDatabaseCreateScripts(DATABASE_INIT_SCRIPT);
		setDatabaseDropScripts(DATABASE_DROP_SCRIPT);
	}
}
