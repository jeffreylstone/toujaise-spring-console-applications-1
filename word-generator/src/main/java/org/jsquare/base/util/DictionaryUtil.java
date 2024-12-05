package org.jsquare.base.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;

import org.jsquare.base.model.Affix;
import org.jsquare.base.model.AffixProcessState;
import org.jsquare.base.model.AffixRule;

public class DictionaryUtil {
	
	public static Map<String, Affix> loadAffixes(String filename) {
		Map<String, Affix> affixes = new TreeMap<>();
		
		// open file
		try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
			String line = null;
			AffixProcessState state = new AffixProcessState(false, 0);

			// read lines
			while (null != (line = in.readLine())) {
				
				// parse line
				String[] affixElements = parseAffixLine(line);
				
				if (null != affixElements) {
					// process state
					processState(affixElements, state, affixes);
				}
			}
		}
		catch (IOException ioe) {
			throw new IllegalStateException("DictionaryUtil");
		}

		return affixes;
	}

	private static void processState(String[] affixElements, AffixProcessState state, Map<String, Affix> affixes) {
		
		if (state.isInAffix()) {
			// parse as rule
			AffixRule affixRule = parseAffixRule(affixElements);
			
			// add rule to affix tree node
			Affix currentAffix = affixes.get(affixElements[1]);
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
			Affix affix = parseAffix(affixElements);
			
			affixes.put(affix.getId(), affix);
			
			// set rule counter
			state.setNbrOfRules(Integer.valueOf(affixElements[3]));
			
			// set inAffix
			state.setInAffix(true);
		}

	}

	private static Affix parseAffix(String[] affixElements) {
		
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

	private static AffixRule parseAffixRule(String[] affixElements) {
		
		AffixRule rule = new AffixRule(affixElements[2], affixElements[3], affixElements[4]);
		
		return rule;
	}

	private static String[] parseAffixLine(String line) {
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
	
	public static List<String> baseAndDerivedWords(String base, String affixString, Map<String, Affix> affixes) {
		List<String> baseAndDerivedWords = new ArrayList<>();
		
		baseAndDerivedWords.add(base);
		
		List<Affix[]> affixCombos = affixCombinations(affixString, affixes);

		for (Affix[] currentAffixCombo : affixCombos) {
			String next = null;
			if (currentAffixCombo.length == 2) {
				next = applyPrefix(base, currentAffixCombo[0]);
				next = applySuffix(next, currentAffixCombo[1]);
			}
			else if (currentAffixCombo.length == 1) {
				// is affix Prefix or Suffix
				if (Affix.AFFIX_PREFIX == currentAffixCombo[0].getAffixType()) {
					next = applyPrefix(base, currentAffixCombo[0]);
				}
				else {
					next = applySuffix(base, currentAffixCombo[0]);
				}
			}
			baseAndDerivedWords.add(next);
		}
		
		return baseAndDerivedWords;
	}

	private static String applySuffix(String base, Affix affix) {
		
		//String lastChar = String.valueOf(base.charAt(base.length() - 1));
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
				//if (matcher.matches()) {
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

	private static String applyPrefix(String base, Affix affix) {
		//String firstChar = String.valueOf(base.charAt(0));
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
				//if (matcher.matches()) {
				if (matcher.lookingAt()) {
					// if match occurs at beginning of word (base)
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
	
	private static List<Affix[]> affixCombinations(String affixString, Map<String, Affix> affixes) {
		List<Affix[]> combinations = new ArrayList<>();
		List<Affix> suffixablePrefixes = new ArrayList<>();
		List<Affix> prefixableSuffixes = new ArrayList<>();

		// read thru affixString, one class at a time
		for (int i = 0; i < affixString.length(); i++) {
			String key = String.valueOf(affixString.charAt(i));
			Affix affix = affixes.get(key);
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
	
	private DictionaryUtil() {
		
	}

}
