package it.alagna.anagrams.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import it.alagna.anagrams.strategy.AnagramStrategy;
import it.alagna.anagrams.strategy.impl.BasicAnagramStrategy;

import org.junit.Before;
import org.junit.Test;

public class BasicAnagramStrategyUnitTest
{
	private static final String BASE_STRING = "care";
	private static final String REAL_ANAGRAM = "race";
	private static final String NOT_ANAGRAM = "cart";
	private static final String LONGER_AND_CONTAINING = "caret";
	private static final String NULL_STRING = null;
	private static final String EMPTY_STRING = "";
	
	protected AnagramStrategy strategy;
	
	@Before
	public void setUp() throws Exception
	{
		this.strategy = new BasicAnagramStrategy();
	}

	@Test
	public void isAnagramTest_OK()
	{
		// GIVEN
		String string1 = BASE_STRING;
		String string2 = REAL_ANAGRAM;
		
		// WHEN
		boolean result = strategy.isAnagram(string1, string2);
		
		// THEN
		assertTrue(String.format("String '%s' and '%s' are actually anagrams!", string1, string2), result);
	}
	
	@Test
	public void isAnagramTest_NotAnagrams()
	{
		// GIVEN
		String string1 = BASE_STRING;
		String string2 = NOT_ANAGRAM;
		
		// WHEN
		boolean result = strategy.isAnagram(string1, string2);
		
		// THEN
		assertFalse(String.format("String '%s' and '%s' are not anagrams!", string1, string2), result);
	}
	
	@Test
	public void isAnagramTest_LongerAndContaining()
	{
		// GIVEN
		String string1 = BASE_STRING;
		String string2 = LONGER_AND_CONTAINING;
		
		// WHEN
		boolean result = strategy.isAnagram(string1, string2);
		
		// THEN
		assertFalse(String.format("String '%s' and '%s' are not anagrams!", string1, string2), result);
	}
	
	@Test
	public void isAnagramTest_CompareWithNull()
	{
		// GIVEN
		String string1 = BASE_STRING;
		String string2 = NULL_STRING;
		
		// WHEN
		boolean result = strategy.isAnagram(string1, string2);
		
		// THEN
		assertFalse(String.format("String '%s' and '%s' are not anagrams!", string1, string2), result);
	}
	
	@Test
	public void isAnagramTest_CompareWithEmptyString()
	{
		// GIVEN
		String string1 = BASE_STRING;
		String string2 = EMPTY_STRING;
		
		// WHEN
		boolean result = strategy.isAnagram(string1, string2);
		
		// THEN
		assertFalse(String.format("String '%s' and '%s' are not anagrams!", string1, string2), result);
	}
	
	@Test
	public void isAnagramTest_TwoNullStringsAreNotAnagrams()
	{
		// GIVEN
		String string1 = NULL_STRING;
		String string2 = NULL_STRING;
		
		// WHEN
		boolean result = strategy.isAnagram(string1, string2);
		
		// THEN
		assertFalse(String.format("String '%s' and '%s' are not anagrams!", string1, string2), result);
	}
	
	@Test
	public void isAnagramTest_TwoEmptyStringsAreAnagrams()
	{
		// GIVEN
		String string1 = EMPTY_STRING;
		String string2 = EMPTY_STRING;
		
		// WHEN
		boolean result = strategy.isAnagram(string1, string2);
		
		// THEN
		assertTrue(String.format("String '%s' and '%s' are actually anagrams!", string1, string2), result);
	}
}
