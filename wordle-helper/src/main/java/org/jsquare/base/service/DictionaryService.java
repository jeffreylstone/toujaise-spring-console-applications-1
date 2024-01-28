/**
 * DictionaryService.java
 * 
 * jeffrey.l.stone@gmail.com
 * 202312xx
 * 
 */
package org.jsquare.base.service;

import java.util.Set;

import org.jsquare.base.model.MatchRule;

/**
 * DictionaryService
 */
public interface DictionaryService {
	
	/**
	 * @return (Boolean) True if dictionary has been loaded, False otherwise
	 */
	public Boolean isDictionaryLoaded();
	
	/**
	 * @param filename File Name of valid dictionary file
	 */
	public void loadDictionary(String filename);
	
	/**
	 * @param filename File Name of valid dictionary file
	 */
	public void reloadDictionary(String filename);
	
	/**
	 * @param rule Matching rule to use for matching/excluding dictionary words
	 */
	public void matchOrExcludeWords(MatchRule rule);
	
	/**
	 * @return (Set<String>) Collection of remaining words in Wordle dictionary (after aggregated rule exclusions)
	 */
	public Set<String> getRemainingWords();
}
