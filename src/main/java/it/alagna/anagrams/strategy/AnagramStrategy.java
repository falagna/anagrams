package it.alagna.anagrams.strategy;

/**
 * Basic interface for AnagramStrategy implementation
 * @author falagna
 */
public interface AnagramStrategy
{
	/**
	 * Evaluates if two strings are anagrams.
	 * @param string1 The first string to evaluate
	 * @param string2 The second string to evaluate
	 * @return true if both strings are not null and are anagrams; false otherwise.
	 */
	boolean isAnagram(String string1, String string2);
}
