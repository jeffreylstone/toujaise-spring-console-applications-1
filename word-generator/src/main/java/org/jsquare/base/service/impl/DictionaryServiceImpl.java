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
	
//	private JLanguageTool languageTool;
	
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
					// eliminate rules (after / , if exists)
					if (-1 == (separatorPosition = line.indexOf("/"))) {
						// insert into HashSet
						this.dictionary.add(line.trim());
					}
					else {
						// insert into HashSet
						String base = line.substring(0, separatorPosition).trim();
						String affixString = line.substring(separatorPosition + 1).trim();
						
						this.dictionary.add(line.substring(0, separatorPosition).trim());
						
						// apply prefixes/suffixes per rules and add to dictionary...
						
						
						
						
					}
				}
				else {
					// read first line, get capacity
					capacity = Integer.valueOf(line.trim());
					capacityRead = Boolean.TRUE;
				}
			}
		}
		catch (IOException ioe) {
			throw new IllegalStateException("DictionaryServiceImpl");
		}

		System.out.println("Loaded!");

//		if (null == this.languageTool) {
//			this.languageTool = new JLanguageTool(Languages.getLanguageForShortCode("en-US"));
//		}
	}

	@Override
	public Boolean isDictionaryWord(String testString) {
		// TODO Auto-generated method stub
		Boolean isWord = Boolean.FALSE;
		
		if (null != this.dictionary) {
			if (this.dictionary.contains(testString)) {
				isWord = Boolean.TRUE;
			}
		}
		
//		if (null != this.languageTool) {
//			try {
//				List<RuleMatch> ruleMatches = this.languageTool.check(testString);
//				
//				if (ruleMatches.isEmpty()) {
//					isWord = Boolean.TRUE;
//				}
//			}
//			catch (IOException ioe) {
//				throw new IllegalStateException("1");
//			}
//		}
//		else {
//			throw new IllegalStateException("Dictionary NOT Loaded!");
//		}
		
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
				
				System.out.println("--------------------");
				System.out.println(base);
				System.out.println(affixString);
				
				for (int i = 0; i < affixString.length(); i++) {
					String key = String.valueOf(affixString.charAt(i));
					Affix affix = this.affixes.get(key);
					
					String next = null;
					// is affix Prefix or Suffix
					if (Affix.AFFIX_PREFIX == affix.getAffixType()) {
						next = this.applyPrefix(base, affix);
					}
					else {
						next = this.applySuffix(base, affix);
					}

					// 
					System.out.println(next);
				}
			}
		}
	}

	private String applySuffix(String base, Affix affix) {
		
		String lastChar = String.valueOf(base.charAt(base.length() - 1));
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
				Matcher matcher = rule.getConditionPattern().matcher(lastChar);
				if (matcher.matches()) {
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

		return buffer.toString();
	}

	private String applyPrefix(String base, Affix affix) {
		// TODO Auto-generated method stub
		String firstChar = String.valueOf(base.charAt(0));
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
				Matcher matcher = rule.getConditionPattern().matcher(firstChar);
				if (matcher.matches()) {
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
		
//		buffer.append(affix.getId()).append("-prefixed-").append(base);
		
		return buffer.toString();
	}

}
