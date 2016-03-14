package it.alagna.anagrams.service;

import it.alagna.anagrams.model.AnagramClassModel;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Basic interface for AnagramService implementation.
 * @author falagna
 */
public interface AnagramService
{
	/**
	 * Processes an input text file, and returns a collection of
	 * {@link AnagramClassModel} objects contained in it.
	 * @param file The input file to process.
	 * @return A collection of {@link AnagramClassModel} found inside the input file.
	 * @throws IOException if the file does not exist or is not readable.
	 */
	Collection<AnagramClassModel> processAnagramsFile(File file) throws IOException;
}
