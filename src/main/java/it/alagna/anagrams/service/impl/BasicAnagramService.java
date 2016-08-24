package it.alagna.anagrams.service.impl;

import it.alagna.anagrams.model.AnagramClassModel;
import it.alagna.anagrams.service.AnagramService;
import it.alagna.anagrams.strategy.AnagramStrategy;
import it.alagna.anagrams.strategy.impl.BasicAnagramStrategy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class BasicAnagramService implements AnagramService
{
	/**
	 * An {@link AnagramStrategy} used to evaluate anagrams
	 */
	private AnagramStrategy anagramStrategy;
	
	public BasicAnagramService()
	{
		anagramStrategy = new BasicAnagramStrategy();
	}
	
	@Override
	public Collection<AnagramClassModel> processAnagramsFile(final File file) throws IOException
	{
		if(!file.exists())
		{
			throw new FileNotFoundException(String.format("File %s not found", file.getCanonicalPath()));
		}
		
		// The target collection, which will contain all the found anagram classes.
		final List<AnagramClassModel> anagramClasses = new LinkedList<>();
		
		try(BufferedReader reader = new BufferedReader(new FileReader(file)))
		{
			while(true)
			{
				final String line = reader.readLine();
				
				if(line == null)
				{
					// EOF reached
					break;
				}
				
				boolean anagramClassFound = false;
				
				// Loop through anagram classes to find a possible match with any already processed line
				for(final AnagramClassModel anagramClass : anagramClasses)
				{
					if(anagramStrategy.isAnagram(line, anagramClass.getKey()))
					{
						anagramClass.getWords().add(line);
						anagramClassFound = true;
						break;
					}
				}
				
				// In case no match is found, add a new anagram class, using the current line as a key,
				// and containing the current line as only element in the list
				if(!anagramClassFound)
				{
					anagramClasses.add(new AnagramClassModel(line));
				}
			}
		}
		
		return anagramClasses;
	}
	
	public AnagramStrategy getAnagramStrategy() {
		return anagramStrategy;
	}
	
	public void setAnagramStrategy(final AnagramStrategy anagramStrategy) {
		this.anagramStrategy = anagramStrategy;
	}
}
