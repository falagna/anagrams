package it.alagna.anagrams.service.impl;

import it.alagna.anagrams.model.AnagramClassModel;
import it.alagna.anagrams.service.AnagramService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;

public class FileWritingAnagramService implements AnagramService
{
	public static final String TEMP_DIR = "temp";

	@Override
	public Collection<AnagramClassModel> processAnagramsFile(final File file) throws IOException
	{
		if(!file.exists())
		{
			throw new FileNotFoundException(String.format("File %s not found", file.getCanonicalPath()));
		}

		final File tempDir = new File(TEMP_DIR);
		tempDir.mkdir();

		System.out.println("************ Reading words from file ************");
		int count = 0;

		// Java 8 version of the file reading
		//		try (Stream<String> stream = Files.lines(Paths.get(file.getAbsolutePath()))) {
		//
		//			stream.parallel().forEach(this::putStringOnFile);
		//
		//		} catch (final IOException e) {
		//			e.printStackTrace();
		//		}

		/**
		 *  To achieve parallelism and small memory usage, we use temporary files to
		 *  store anagram classes.
		 *  We create a file for each anagram class. The file name is the sorted
		 *  version of the anagram string, and it contains a list of words, one per line.
		 */
		try(BufferedReader reader = new BufferedReader(new FileReader(file)))
		{
			while(true)
			{
				count++;
				if(count % 10000 == 0)
					System.out.println("************ " + count + " words read ************");

				final String line = reader.readLine();

				if(line == null)
				{
					// EOF reached
					break;
				}

				putStringOnFile(line);
			}
		}

		if(tempDir.listFiles() == null || tempDir.listFiles().length == 0)
			return Collections.emptySet();

		final Collection<AnagramClassModel> anagramClasses = new LinkedHashSet<>();

		System.out.println("************ Reading anagram classes from files ************");
		count = 0;

		/**
		 *  Afterwards, we rebuild anagram information by reading the temporary files
		 *  one by one.
		 */
		for(final File tempFile : tempDir.listFiles())
		{
			count++;
			if(count % 10000 == 0)
				System.out.println("************ " + count + " files read ************");

			final AnagramClassModel targetAnagramClass = createAnagramClassFromFile(tempFile);

			if(targetAnagramClass != null)
			{
				anagramClasses.add(targetAnagramClass);
			}
			tempFile.delete();
		}

		tempDir.deleteOnExit();
		
		return anagramClasses;
	}

	private void putStringOnFile(final String line)
	{
		final char[] sortedString = line.toCharArray();
		Arrays.sort(sortedString);
		final String sortedLine = new String(sortedString);

		synchronized (sortedLine.intern())
		{
			final String tempFileName = TEMP_DIR + File.separator + sortedLine;

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFileName, true)))
			{
				writer.write(line);
				writer.newLine();
			}
			catch (final IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private AnagramClassModel createAnagramClassFromFile(final File tempFile) throws IOException
	{
		AnagramClassModel targetAnagramClass = null;

		try(BufferedReader reader = new BufferedReader(new FileReader(tempFile)))
		{
			while(true)
			{
				final String line = reader.readLine();

				if (line == null) {
					// EOF reached
					break;
				}
				if(targetAnagramClass == null)
				{
					targetAnagramClass = new AnagramClassModel(line);
				}
				else
				{
					targetAnagramClass.getWords().add(line);
				}
			}
		}

		return targetAnagramClass;
	}
}
