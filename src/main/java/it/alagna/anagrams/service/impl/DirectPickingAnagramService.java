package it.alagna.anagrams.service.impl;

import it.alagna.anagrams.model.AnagramClassModel;
import it.alagna.anagrams.service.AnagramService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class DirectPickingAnagramService implements AnagramService
{
	@Override
	public Collection<AnagramClassModel> processAnagramsFile(File file) throws IOException
	{
		if(!file.exists())
		{
			throw new FileNotFoundException(String.format("File %s not found", file.getCanonicalPath()));
		}
		
		// In this implementation, the found anagram classes will be stored in an HashMap
		Map<String, AnagramClassModel> anagramClasses = new LinkedHashMap<String, AnagramClassModel>();
		
		try(BufferedReader reader = new BufferedReader(new FileReader(file)))
		{
			while(true)
			{
				String line = reader.readLine();
				
				if(line == null)
				{
					// EOF reached
					break;
				}
				
				/**
				 *  Instead of looping through all the AnagramClassModel objects, we trigger
				 *  a direct picking using the Map.get(key) call. We ensure to uniquely identify
				 *  the correct anagram class by using the (alphabetically) sorted version of
				 *  the string as key both to store and to retrieve.
				 */
				char[] sortedString = line.toCharArray();				
				Arrays.sort(sortedString);
				String sortedLine = new String(sortedString);
				

				AnagramClassModel targetAnagramClass = anagramClasses.get(sortedLine);
				
				if(targetAnagramClass != null)
				{
					targetAnagramClass.getWords().add(line);
				}
				else
				{
					/**
					 *  In case no match is found, add a new anagram class, using the sorted current line
					 *  as a key, and containing the current line as only element in the list.
					 */
					anagramClasses.put(sortedLine, new AnagramClassModel(line));
				}
			}
		}
		
		return anagramClasses.values();
	}
}
