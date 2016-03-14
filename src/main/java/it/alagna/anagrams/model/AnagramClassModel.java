package it.alagna.anagrams.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Base model for an anagram class. An anagram class is a collection of string,
 * all of which are anagrams. These strings is identified by a key, which may be
 * any of the words inside the class. 
 * @author falagna
 */
public class AnagramClassModel
{
	/**
	 * The key which identifies the anagram class.
	 */
	private String key;
	
	/**
	 * A list of words belonging to this anagram class.
	 */
	private List<String> words;
	
	public AnagramClassModel(String key)
	{
		this.key = key;
		this.words = new LinkedList<String>();
		this.words.add(key);
	}
	
	public String getKey()
	{
		return key;
	}
	
	public List<String> getWords()
	{
		return words;
	}
}
