package it.alagna.anagrams.service.impl;

import it.alagna.anagrams.model.AnagramClassModel;
import it.alagna.anagrams.service.AnagramService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

public abstract class DatabaseWritingAnagramService implements AnagramService
{
	private static final String DATABASE_TABLE_TOKEN = "%dbTable";
	private static final String DATABASE_KEY_TOKEN = "%dbKey";
	private static final String DATABASE_VALUE_TOKEN = "%dbValue";

	private static final String DATABASE_INSERT_QUERY_TEMPLATE = "INSERT INTO " + DATABASE_TABLE_TOKEN + " (" + DATABASE_KEY_TOKEN + ", " + DATABASE_VALUE_TOKEN + ") VALUES (?, ?);";
	private static final String DATABASE_SELECT_KEY_QUERY_TEMPLATE = "SELECT DISTINCT " + DATABASE_KEY_TOKEN + " FROM " + DATABASE_TABLE_TOKEN + " GROUP BY " + DATABASE_KEY_TOKEN + " HAVING (COUNT(*) > 1);";
	private static final String DATABASE_SELECT_VALUES_QUERY_TEMPLATE = "SELECT DISTINCT " + DATABASE_VALUE_TOKEN + " FROM " + DATABASE_TABLE_TOKEN + " WHERE " + DATABASE_KEY_TOKEN + " = ?;";

	private String databaseUrl;
	private String databaseUsername;
	private String databasePassword;
	private String databaseRootUrl;
	private String databaseRootUsername;
	private String databaseRootPassword;

	private String databaseTable;
	private String databaseKeyColumn;
	private String databaseValueColumn;

	private String[] databaseCreateScripts;
	private String[] databaseDropScripts;

	public abstract void init();

	@Override
	public Collection<AnagramClassModel> processAnagramsFile(final File file) throws IOException
	{
		init();

		if (!file.exists()) {
			throw new FileNotFoundException(String.format("File %s not found", file.getCanonicalPath()));
		}

		dropDatabase(true);
		createDatabase();

		final StringBuilder databaseUrl = new StringBuilder();
		databaseUrl.append(this.databaseUrl);

		int count = 0;

		final String insertQuery = DATABASE_INSERT_QUERY_TEMPLATE
				.replaceAll(DATABASE_TABLE_TOKEN, databaseTable)
				.replaceAll(DATABASE_KEY_TOKEN, databaseKeyColumn)
				.replaceAll(DATABASE_VALUE_TOKEN, databaseValueColumn);

		try(Connection connection = DriverManager.getConnection(databaseUrl.toString(), databaseUsername, databasePassword);
			PreparedStatement statement = connection.prepareStatement(insertQuery);
			BufferedReader reader = new BufferedReader(new FileReader(file)))
		{
			while (true)
			{
				count++;
				if (count % 10000 == 0) System.out.println("************ " + count + " words read ************");

				final String line = reader.readLine();

				if (line == null) {
					// EOF reached¡
					break;
				}

				final char[] sortedString = line.toCharArray();
				Arrays.sort(sortedString);
				final String sortedLine = new String(sortedString);

				statement.setString(1, sortedLine);
				statement.setString(2, line);
				statement.executeUpdate();
			}
		} catch (final SQLException e) {
			e.printStackTrace();
		}


		// Collect data

		final Collection<AnagramClassModel> anagramClasses = new LinkedHashSet<>();

		final String selectKeysQuery = DATABASE_SELECT_KEY_QUERY_TEMPLATE
				.replaceAll(DATABASE_TABLE_TOKEN, databaseTable)
				.replaceAll(DATABASE_KEY_TOKEN, databaseKeyColumn)
				.replaceAll(DATABASE_VALUE_TOKEN, databaseValueColumn);
		final String selectValuesQuery = DATABASE_SELECT_VALUES_QUERY_TEMPLATE
				.replaceAll(DATABASE_TABLE_TOKEN, databaseTable)
				.replaceAll(DATABASE_KEY_TOKEN, databaseKeyColumn)
				.replaceAll(DATABASE_VALUE_TOKEN, databaseValueColumn);

		try(Connection connection = DriverManager.getConnection(databaseUrl.toString(), databaseUsername, databasePassword);
			Statement statement = connection.createStatement();
			ResultSet keys = statement.executeQuery(selectKeysQuery);
			PreparedStatement preparedStatement = connection.prepareStatement(selectValuesQuery))
		{
			count = 0;
			while (keys.next())
			{
				count++;
				if (count % 1000 == 0) System.out.println("************ " + count + " keys written ************");

				final String key = keys.getString(databaseKeyColumn);
				preparedStatement.setString(1, key);
				final ResultSet values = preparedStatement.executeQuery();

				AnagramClassModel anagramClassModel = null;
				while (values.next())
				{
					final String value = values.getString(databaseValueColumn);
					if(anagramClassModel == null)
						anagramClassModel = new AnagramClassModel(value);
					else
						anagramClassModel.getWords().add(value);
				}

				if(anagramClassModel != null)
					anagramClasses.add(anagramClassModel);

				values.close();
			}
		} catch (final SQLException e) {
			e.printStackTrace();
		}

		return anagramClasses;
	}

	protected void createDatabase()
	{
		try(Connection connection = DriverManager.getConnection(databaseRootUrl, databaseRootUsername, databaseRootPassword);
			Statement statement = connection.createStatement())
		{
			for(final String line: databaseCreateScripts)
			{
				statement.executeUpdate(line);
			}
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}

	protected void dropDatabase(final boolean silenceErrors)
	{
		try(Connection connection = DriverManager.getConnection(databaseRootUrl, databaseRootUsername, databaseRootPassword);
			Statement statement = connection.createStatement())
		{
			for(final String line: databaseDropScripts)
			{
				statement.executeUpdate(line);
			}
		} catch (final SQLException e) {
			if(!silenceErrors)
				e.printStackTrace();
		}
	}

	public void setDatabaseUrl(final String databaseUrl) {
		this.databaseUrl = databaseUrl;
	}

	public void setDatabaseUsername(final String databaseUsername) {
		this.databaseUsername = databaseUsername;
	}

	public void setDatabasePassword(final String databasePassword) {
		this.databasePassword = databasePassword;
	}

	public void setDatabaseRootUrl(final String databaseRootUrl) {
		this.databaseRootUrl = databaseRootUrl;
	}

	public void setDatabaseRootUsername(final String databaseRootUsername) {
		this.databaseRootUsername = databaseRootUsername;
	}

	public void setDatabaseRootPassword(final String databaseRootPassword) {
		this.databaseRootPassword = databaseRootPassword;
	}

	public void setDatabaseCreateScripts(final String[] databaseCreateScripts) {
		this.databaseCreateScripts = databaseCreateScripts;
	}

	public void setDatabaseDropScripts(final String[] databaseDropScripts) {
		this.databaseDropScripts = databaseDropScripts;
	}

	public void setDatabaseTable(final String databaseTable) {
		this.databaseTable = databaseTable;
	}

	public void setDatabaseKeyColumn(final String databaseKeyColumn) {
		this.databaseKeyColumn = databaseKeyColumn;
	}

	public void setDatabaseValueColumn(final String databaseValueColumn) {
		this.databaseValueColumn = databaseValueColumn;
	}
}
