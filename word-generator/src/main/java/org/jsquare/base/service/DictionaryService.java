/**
 * 
 */
package org.jsquare.base.service;

/**
 * 
 */
public interface DictionaryService {
	
	public Boolean isDictionaryLoaded();
	
	public void loadDictionary(String filename);
	
	public void loadAffixes(String filename);
	
	public void loadDictionarySampleTest();
	
	public Boolean isDictionaryWord(String testString);

}
