package it.alagna.anagrams.strategy.impl;

import it.alagna.anagrams.strategy.AnagramStrategy;

import java.util.Arrays;

/**
 * Basic implementation of the {@link AnagramStrategy} class.
 * @author falagna
 */
public class BasicAnagramStrategy implements AnagramStrategy
{
	public boolean isAnagram(String string1, String string2)
	{
		if(string1 == null || string2 == null)
		{
			return false;
		}
		
		if(string1.length() != string2.length())
		{
			return false;
		}
		
		char[] sortedString1 = string1.toCharArray();
		char[] sortedString2 = string2.toCharArray();
		
		Arrays.sort(sortedString1);
		Arrays.sort(sortedString2);
				
		for(int i = 0; i < sortedString1.length; i++)
		{
			if(sortedString1[i] != sortedString2[i])
			{
				return false;
			}
		}
		
		return true;
	}
}
