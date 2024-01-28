/**
 * DictionaryServiceImpl.java
 * Implementation of DictionaryService interface for this application
 * jeffrey.l.stone@gmail.com
 * 20240102
 * 
 */
package org.jsquare.base.service.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsquare.base.model.MatchRule;
import org.jsquare.base.service.DictionaryService;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * DictionaryServiceImpl
 */
@Service
@Slf4j
public class DictionaryServiceImpl implements DictionaryService {
	
	private HashSet<String> dictionary = null;

	@Override
	public Boolean isDictionaryLoaded() {
		Boolean result = Boolean.FALSE;
		
		if (null != this.dictionary
			&& !this.dictionary.isEmpty()) {
			result = Boolean.TRUE;
		}
		
		return result;
	}

	@Override
	public void loadDictionary(String filename) {

		System.out.println("Loading " + filename + " ...");
		
		// open file
		try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
			String line = null;
			
			// read lines
			while (null != (line = in.readLine())) {
					// lazy initialization
				if (null == this.dictionary) {
					this.dictionary = new HashSet<>(32768);
				}

				// insert into HashSet
				this.dictionary.add(line.trim().toUpperCase());
			}
		}
		catch (IOException ioe) {
			throw new IllegalStateException("DictionaryServiceImpl");
		}

		System.out.println("Loaded!");

	}

	@Override
	public void reloadDictionary(String filename) {
		
		if (this.isDictionaryLoaded()) {
			this.dictionary.clear();
		}
		
		this.loadDictionary(filename);
		
	}
	
	@Override
	public void matchOrExcludeWords(MatchRule rule) {
		switch (rule.getRuleNumber()) {
			case MatchRule.THIS_CHARACTER_NOT_CONTAINED_IN_SOLUTION:
				this.excludeWords(String.valueOf(rule.getMatchCharacter()));
				break;
			case MatchRule.THIS_CHARACTER_CONTAINED_IN_SOLUTION_AT_DIFFERENT_POSITION:
				this.matchWords(rule);
				break;
			case MatchRule.THIS_CHARACTER_CONTAINED_IN_SOLUTION_AT_THIS_POSITION:
				this.matchWords(rule);
				break;
			default:
				throw new IllegalStateException("invalid rule number!");
		}
	}

	private void matchWords(MatchRule rule) {
		HashSet<String> matchingWords = new HashSet<>();
		
		for (String current : this.dictionary) {
			if (this.matchByRule(current, rule)) {
				matchingWords.add(current);
			}
		}
		
		if (!matchingWords.isEmpty()) {
			this.dictionary.clear();
			this.dictionary.addAll(matchingWords);
		}
		else {
			// log error
		}
		
	}

	private boolean matchByRule(String testWord, MatchRule rule) {
		boolean result = false;
	
		for (int i = 0; i < testWord.length() && !result; i++) {
			if (testWord.charAt(i) == rule.getMatchCharacter()) {
				if (MatchRule.THIS_CHARACTER_CONTAINED_IN_SOLUTION_AT_DIFFERENT_POSITION == rule.getRuleNumber()) {
					if ((i + 1) != rule.getMatchPosition()) {
						result = true;
					}
				}
				else if (MatchRule.THIS_CHARACTER_CONTAINED_IN_SOLUTION_AT_THIS_POSITION == rule.getRuleNumber()) {
					if ((i + 1) == rule.getMatchPosition()) {
						result = true;
					}
				}
			}
		}
		
		return result;
	}

	private void excludeWords(String matchChar) {
		
		List<String> wordsToExclude = new ArrayList<>();
		
		for (String current : this.dictionary) {
			if (this.excludeByChar(current, matchChar)) {
				wordsToExclude.add(current);
			}
		}

		this.dictionary.removeAll(wordsToExclude);
		
		if (this.dictionary.isEmpty()) {
			// log error
		}
	}

	private boolean excludeByChar(String testWord, String matchChar) {
		boolean result = false;
		
		if (0 <= testWord.indexOf(matchChar)) {
			result = true;
		}
		
		return result;
	}

	@Override
	public Set<String> getRemainingWords() {
		Set<String> remainingWords;
		
		if (null == this.dictionary) {
			remainingWords = new HashSet<>();
		}
		else {
			remainingWords = this.dictionary;
		}
		
		return remainingWords;
	}

}
