/**
 * DictionaryServiceImpl.java
 * 
 * jeffrey.l.stone@gmail.com
 * 2024mmdd
 * 
 */
package org.jsquare.base.service.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;

import org.jsquare.base.model.Affix;
import org.jsquare.base.model.AffixProcessState;
import org.jsquare.base.model.AffixRule;
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
	private TreeMap<String, Affix> affixes = null;

	@Override
	public Boolean isDictionaryLoaded() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadDictionary(String filename) {

		System.out.println("Loading " + filename + " ...");
		
		// open file
		try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
			String line = null;
			int capacity = 0;
			Boolean capacityRead = Boolean.FALSE;
			
			// read lines
			while (null != (line = in.readLine())) {
				if (capacityRead) {
					// lazy initialization
					if (null == this.dictionary) {
						this.dictionary = new HashSet<>(capacity);
					}

					int separatorPosition = 0;
					// Find rules (after / , if exists)
					if (-1 == (separatorPosition = line.indexOf("/"))) {
						// insert into HashSet
						this.dictionary.add(line.trim());
					}
					else {
						// divide into base and affix string
						String base = line.substring(0, separatorPosition).trim();
						String affixString = line.substring(separatorPosition + 1).trim();
						
						// apply prefixes/suffixes per rules and add to dictionary...
						List<String> baseAndDerivedWords = this.baseAndDerivedWords(base, affixString);
						for (String currentWord : baseAndDerivedWords) {
							this.dictionary.add(currentWord);
						}
					}
				}
				else {
					// read first line, get capacity
					capacity = Integer.valueOf(line.trim());
					capacityRead = Boolean.TRUE;
				}
			}
			
			System.out.println("Initial Capacity: " + capacity);
			System.out.println("Actual Size (entries): " + this.dictionary.size());
		}
		catch (IOException ioe) {
			throw new IllegalStateException("DictionaryServiceImpl");
		}

		System.out.println("Loaded!");
	}

	@Override
	public Boolean isDictionaryWord(String testString) {
		Boolean isWord = Boolean.FALSE;
		
		if (null != this.dictionary) {
			if (this.dictionary.contains(testString)) {
				isWord = Boolean.TRUE;
			}
			else {
				String secondTestString = String.valueOf(testString.charAt(0)).toUpperCase() + testString.substring(1);
				if (this.dictionary.contains(secondTestString)) {
					isWord = Boolean.TRUE;
				}
			}
		}
		
		return isWord;
	}

	@Override
	public void loadAffixes(String filename) {

		System.out.println("Loading " + filename + " ...");
		
		// open file
		try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
			String line = null;
			AffixProcessState state = new AffixProcessState(false, 0);

			// read lines
			while (null != (line = in.readLine())) {
				
				// parse line
				String[] affixElements = this.parseAffixLine(line);
				
				if (null != affixElements) {
					// process state
					this.processState(affixElements, state);
				}
			}
		}
		catch (IOException ioe) {
			throw new IllegalStateException("DictionaryServiceImpl");
		}

		System.out.println("Loaded!");

	}

	private void processState(String[] affixElements, AffixProcessState state) {
		
		if (state.isInAffix()) {
			// parse as rule
			AffixRule affixRule = this.parseAffixRule(affixElements);
			
			// add rule to affix tree node
			Affix currentAffix = this.affixes.get(affixElements[1]);
			if (null == currentAffix.getAffixRules()) {
				List<AffixRule> currentRules = new ArrayList<>();
				currentRules.add(affixRule);
				currentAffix.setAffixRules(currentRules);
			}
			else {
				currentAffix.getAffixRules().add(affixRule);
			}
			
			// decrement rule counter
			state.decrementRuleCounter();
			
			// if rule counter == 0, clear inAffix
			if (0 == state.getNbrOfRules()) {
				state.setInAffix(false);
			}
		}
		else {
			// parse as affix
			Affix affix = this.parseAffix(affixElements);
			
			// create new affix tree node
			// Lazy initialization
			if (null == this.affixes) {
				this.affixes = new TreeMap<>();
			}
			this.affixes.put(affix.getId(), affix);
			
			// set rule counter
			state.setNbrOfRules(Integer.valueOf(affixElements[3]));
			
			// set inAffix
			state.setInAffix(true);
		}

	}

	private Affix parseAffix(String[] affixElements) {
		
		Affix affix = new Affix();
		
		if ("SFX".equals(affixElements[0])) {
			affix.setAffixType(Affix.AFFIX_SUFFIX);
		}
		else if ("PFX".equals(affixElements[0])) {
			affix.setAffixType(Affix.AFFIX_PREFIX);
		}
		
		affix.setId(affixElements[1]);
		
		if ("Y".equalsIgnoreCase(affixElements[2])) {
			affix.setCrossProductFlag(true);
		}
		else {
			affix.setCrossProductFlag(false);
		}
			
		return affix;
	}

	private AffixRule parseAffixRule(String[] affixElements) {
		
		AffixRule rule = new AffixRule(affixElements[2], affixElements[3], affixElements[4]);
		
		return rule;
	}

	private String[] parseAffixLine(String line) {
		String[] elements = null;
		
		if (null != line) {
			elements = line.split("\\s+");
			if (1 < elements.length) {
				for (int i = 0; i < elements.length; i++) {
					elements[i] = elements[i].trim();
				}
				
				if ("SFX".equals(elements[0])
					|| "PFX".equals(elements[0])) {
					return elements;
				}
				else {
					elements = null;
				}
			}
			else {
				elements = null;
			}
		}
		
		return elements;
	}

	@Override
	public void loadDictionarySampleTest() {
		
		String[] dictionarySamples = {
			"tire/AGDS",
			"work/ADJSG",
			"wrong/STGMPDRY",
			"administrate/XDSGNV",
			"advise/LDRSZGB",
			"act/ASDGV",
			"lapse/AKGMSD",
			"short/XTGMDNRYSP"
		};

		for (String current : dictionarySamples) {

			int separatorPosition = -1;
			if (0 <= (separatorPosition = current.indexOf("/"))) {
				String base = current.substring(0, separatorPosition).trim();
				String affixString = current.substring(separatorPosition + 1).trim();
				
				List<String> baseAndDerivedWords = this.baseAndDerivedWords(base, affixString);
				
				System.out.println("--------------------");
				System.out.println(base);
				System.out.println(affixString);

				for (String currentWord : baseAndDerivedWords) {
					System.out.println(currentWord);
				}
				
//				System.out.println("--------------------");
//				System.out.println(base);
//				System.out.println(affixString);
//				List<Affix[]> affixCombos = this.affixCombinations(affixString);
//				for (Affix[] currentAffixCombo : affixCombos) {
//					if (currentAffixCombo.length == 2) {
//						System.out.print(currentAffixCombo[0].getId());
//						System.out.print(currentAffixCombo[1].getId());
//						System.out.print(" ");
//					}
//					else if (currentAffixCombo.length == 1) {
//						System.out.print(currentAffixCombo[0].getId());
//						System.out.print(" ");
//					}
//				}
//				System.out.println("");
				
//				for (int i = 0; i < affixString.length(); i++) {
//					String key = String.valueOf(affixString.charAt(i));
//					Affix affix = this.affixes.get(key);
//					
//					String next = null;
//					// is affix Prefix or Suffix
//					if (Affix.AFFIX_PREFIX == affix.getAffixType()) {
//						next = this.applyPrefix(base, affix);
//					}
//					else {
//						next = this.applySuffix(base, affix);
//					}
//
//					// 
//					System.out.println(next);
//				}
				
//				for (Affix[] currentAffixCombo : affixCombos) {
//					String next = null;
//					if (currentAffixCombo.length == 2) {
//						next = this.applyPrefix(base, currentAffixCombo[0]);
//						next = this.applySuffix(next, currentAffixCombo[1]);
//					}
//					else if (currentAffixCombo.length == 1) {
//						// is affix Prefix or Suffix
//						if (Affix.AFFIX_PREFIX == currentAffixCombo[0].getAffixType()) {
//							next = this.applyPrefix(base, currentAffixCombo[0]);
//						}
//						else {
//							next = this.applySuffix(base, currentAffixCombo[0]);
//						}
//					}
//					System.out.println(next);
//				}
			}
		}
	}

	private String applySuffix(String base, Affix affix) {
		
		int lastIndex = base.length();
		StringBuilder buffer = new StringBuilder();
		
		boolean matched = false;
		for (int i = 0; i < affix.getAffixRules().size() && !matched; i++) {
			AffixRule rule = affix.getAffixRules().get(i);
			if (AffixRule.AFFIX_NO_CONDITION.equals(rule.getCondition())) {
				if (base.endsWith(rule.getStrippingCharacters())) {
					int index = base.lastIndexOf(rule.getStrippingCharacters());
					buffer.append(base.substring(0, index)).append(rule.getAffix());
				}
				else {
					buffer.append(base).append(rule.getAffix());
				}
				matched = true;
			}
			else {
				Matcher matcher = rule.getConditionPattern().matcher(base);
				while (matcher.find() && !matched) {
					// if match occurs at end of word (base)
					if (lastIndex == matcher.end()) {
						if (base.endsWith(rule.getStrippingCharacters())) {
							int index = base.lastIndexOf(rule.getStrippingCharacters());
							buffer.append(base.substring(0, index)).append(rule.getAffix());
						}
						else {
							buffer.append(base).append(rule.getAffix());
						}
						matched = true;
					}
				}
			}
		}

		return buffer.toString();
	}

	private String applyPrefix(String base, Affix affix) {
		StringBuilder buffer = new StringBuilder();
		
		boolean matched = false;
		for (int i = 0; i < affix.getAffixRules().size() && !matched; i++) {
			AffixRule rule = affix.getAffixRules().get(i);
			if (AffixRule.AFFIX_NO_CONDITION.equals(rule.getCondition())) {
				if (base.startsWith(rule.getStrippingCharacters())) {
					buffer.append(rule.getAffix()).append(base.substring(rule.getStrippingCharacters().length()));
				}
				else {
					buffer.append(rule.getAffix()).append(base);
				}
				matched = true;
			}
			else {
				Matcher matcher = rule.getConditionPattern().matcher(base);
				if (matcher.lookingAt()) {
					if (0 == matcher.start()) {
						if (base.startsWith(rule.getStrippingCharacters())) {
							buffer.append(rule.getAffix()).append(base.substring(rule.getStrippingCharacters().length()));
						}
						else {
							buffer.append(rule.getAffix()).append(base);
						}
						matched = true;
					}
				}
			}
		}
		
		return buffer.toString();
	}
	
	private List<Affix[]> affixCombinations(String affixString) {
		List<Affix[]> combinations = new ArrayList<>();
		List<Affix> suffixablePrefixes = new ArrayList<>();
		List<Affix> prefixableSuffixes = new ArrayList<>();

		// read thru affixString, one class at a time
		for (int i = 0; i < affixString.length(); i++) {
			String key = String.valueOf(affixString.charAt(i));
			Affix affix = this.affixes.get(key);
			if (null != affix) {
				Affix[] affixSingle = new Affix[1];
				affixSingle[0] = affix;
				combinations.add(affixSingle);
				
				// if crossProduct == Y, 
				if (affix.isCrossProductFlag()) {
					// if prefix, add class to suffixablePrefixes
					if (Affix.AFFIX_PREFIX == affix.getAffixType()) {
						suffixablePrefixes.add(affix);
					}
					// else (suffix) add class to prefixableSuffixes
					else {
						prefixableSuffixes.add(affix);
					}
				}
			}
			else {
				//System.out.println("No affix for: " + key);
				// letters in affix string not found as affixes may be 
				//  compound rules or other non-prefix/non-suffix rules...
			}
		}
		
		// for non-empty suffixablePrefixes, prefixableSuffixes, form possible combinations, add to combinations
		for (Affix currentPrefix : suffixablePrefixes) {
			for (Affix currentSuffix : prefixableSuffixes) {
				Affix[] affixDouble = new Affix[2];
				affixDouble[0] = currentPrefix;
				affixDouble[1] = currentSuffix;
				combinations.add(affixDouble);
			}
		}
		
		return combinations;
	}
	
	private List<String> baseAndDerivedWords(String base, String affixString) {
		List<String> baseAndDerivedWords = new ArrayList<>();
		
		baseAndDerivedWords.add(base);
		
		List<Affix[]> affixCombos = this.affixCombinations(affixString);

		for (Affix[] currentAffixCombo : affixCombos) {
			String next = null;
			if (currentAffixCombo.length == 2) {
				next = this.applyPrefix(base, currentAffixCombo[0]);
				next = this.applySuffix(next, currentAffixCombo[1]);
			}
			else if (currentAffixCombo.length == 1) {
				// is affix Prefix or Suffix
				if (Affix.AFFIX_PREFIX == currentAffixCombo[0].getAffixType()) {
					next = this.applyPrefix(base, currentAffixCombo[0]);
				}
				else {
					next = this.applySuffix(base, currentAffixCombo[0]);
				}
			}
			baseAndDerivedWords.add(next);
		}
		
		return baseAndDerivedWords;
	}
}
