package it.alagna.anagrams.service;

import it.alagna.anagrams.model.AnagramClassModel;
import it.alagna.anagrams.service.impl.BasicAnagramService;
import it.alagna.anagrams.service.impl.DirectPickingAnagramService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import org.junit.Test;

public class AnagramServiceIntegrationTest
{
	private static final String SAMPLE_FILENAME = "sample.txt";
	private static final String DICTIONARY_FILENAME = "dictionary.txt";
	private static final String DICTIONARY2_FILENAME = "dictionary2.txt";
	
	@Test
	public void test_sampleFile_basic() throws IOException
	{
		test(SAMPLE_FILENAME, new BasicAnagramService());
	}
	
	@Test
	public void test_sampleFile_dp() throws IOException
	{
		test(SAMPLE_FILENAME, new DirectPickingAnagramService());
	}
	
	@Test
	public void test_shortDictionary_basic() throws IOException
	{
		test(DICTIONARY2_FILENAME, new BasicAnagramService());
	}
	
	@Test
	public void test_shortDictionary_dp() throws IOException
	{
		test(DICTIONARY2_FILENAME, new DirectPickingAnagramService());
	}
	
	@Test
	public void test_fullDictionary_basic() throws IOException
	{
		test(DICTIONARY_FILENAME, new BasicAnagramService());
	}
	
	@Test
	public void test_fullDictionary_dp() throws IOException
	{
		test(DICTIONARY_FILENAME, new DirectPickingAnagramService());
	}
	
	protected void test(String filename, AnagramService anagramService) throws IOException
	{
		long currentTime = System.currentTimeMillis();
		System.out.println(String.format("******** Processing file '%s' - rows: %d *********", filename, getNumberOfWords(filename)));
		System.out.println(String.format("************** Results from file '%s' ***************", filename));
		System.out.println();
		
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(filename).getFile());
		
		Collection<AnagramClassModel> anagramClasses = anagramService.processAnagramsFile(file);
		
		for(AnagramClassModel anagramClass : anagramClasses)
		{
			// Skip classes with less than two matching words
			if(anagramClass.getWords().size() < 2)
			{
				continue;
			}
			
			StringBuilder line = new StringBuilder();
			for(String value : anagramClass.getWords())
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
	
	protected long getNumberOfWords(String filename) throws FileNotFoundException, IOException
	{
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(filename).getFile());
		try(BufferedReader reader = new BufferedReader(new FileReader(file)))
		{
			return reader.lines().count();
		}
	}
}
