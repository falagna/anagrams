package it.alagna.anagrams.service;

import it.alagna.anagrams.model.AnagramClassModel;
import it.alagna.anagrams.service.impl.HsqlDatabaseWritingAnagramService;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

public class AnagramServiceIntegrationTest
{
	private static final String SAMPLE_FILENAME = "sample.txt";
	private static final String DICTIONARY_FILENAME = "dictionary.txt";
	private static final String DICTIONARY2_FILENAME = "dictionary2.txt";
	
	private AnagramService anagramService;
	
	@Before
	public void setUp() throws Exception
	{
		anagramService = new HsqlDatabaseWritingAnagramService();
	}

	@Test
	public void test_sampleFile() throws IOException
	{
		test(SAMPLE_FILENAME);
	}
	
	@Test
	public void test_shortDictionary() throws IOException
	{
		test(DICTIONARY2_FILENAME);
	}
	
	@Test
	public void test_fullDictionary() throws IOException
	{
		test(DICTIONARY_FILENAME);
	}
	
	protected void test(final String filename) throws IOException
	{
		final long currentTime = System.currentTimeMillis();
		System.out.println(String.format("******** Processing file '%s' - rows: %d *********", filename, getNumberOfWords(filename)));
		System.out.println(String.format("************** Results from file '%s' ***************", filename));
		System.out.println();
		
		final ClassLoader classLoader = getClass().getClassLoader();
		final File file = new File(classLoader.getResource(filename).getFile());
		
		final Collection<AnagramClassModel> anagramClasses = anagramService.processAnagramsFile(file);
		
		for(final AnagramClassModel anagramClass : anagramClasses)
		{
			// Skip classes with less than two matching words
			if(anagramClass.getWords().size() < 2)
			{
				continue;
			}
			
			final StringBuilder line = new StringBuilder();
			for(final String value : anagramClass.getWords())
			{
				line.append(' ').append(value);
			}

			// Remove the first space
			System.out.println(line.substring(1));
		}
		
		System.out.println();
		System.out.println(String.format("************ End results from file '%s' *************", filename));
		System.out.println(String.format("************ File '%s' processed in %d ms *************", filename, System.currentTimeMillis() - currentTime));
		System.out.println();
	}
	
	protected long getNumberOfWords(final String filename) throws FileNotFoundException, IOException
	{
		final ClassLoader classLoader = getClass().getClassLoader();
		final File file = new File(classLoader.getResource(filename).getFile());
		try(BufferedReader reader = new BufferedReader(new FileReader(file)))
		{
			return reader.lines().count();
		}
	}
}
