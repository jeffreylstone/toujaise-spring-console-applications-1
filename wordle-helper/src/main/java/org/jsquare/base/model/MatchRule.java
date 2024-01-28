/**
 * MatchRule.java
 * 
 * jeffrey.l.stone@gmail.com
 * 20240105
 * 
 */
package org.jsquare.base.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * MatchRule
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MatchRule {
	
	public static final int THIS_CHARACTER_NOT_CONTAINED_IN_SOLUTION = 0;
	public static final int THIS_CHARACTER_CONTAINED_IN_SOLUTION_AT_DIFFERENT_POSITION = 1;
	public static final int THIS_CHARACTER_CONTAINED_IN_SOLUTION_AT_THIS_POSITION = 2;
	
	private int ruleNumber; 		// 0, 1, or 2
	private	char matchCharacter;	// a-z
	private int matchPosition;		// 1-5
	

	/**
 	 * @return
	 */
	public String toString() {
		StringBuilder result = new StringBuilder();
		
		result.append(this.matchCharacter).append("|").append(this.matchPosition).append("|rule:").append(this.ruleNumber); 
		
		return result.toString();
	}
	
	/**
	 * @param ruleElement
	 */
	public void parseRule(String ruleElement) {
		
		if (null == ruleElement) {
			throw new IllegalArgumentException("ruleElement MUST NOT be null!");
		}
		
		String[] elements = ruleElement.split(":");
		
		if (2 != elements.length) {
			throw new IllegalStateException("ruleElement not properly constructed!");
		}
		
		if (0 > "-012345".indexOf(elements[1].trim().charAt(0))
			|| ('-' == elements[1].trim().charAt(0)
				&& 0 > "012345".indexOf(elements[1].trim().charAt(1)))) {
			throw new IllegalArgumentException("Invalid matchRule!");
		}
		
		this.matchCharacter = elements[0].trim().toUpperCase().charAt(0);
		
		if ('-' == elements[1].trim().charAt(0)) {
			if ('0' == elements[1].trim().charAt(1)) {
				this.ruleNumber = 0;
				this.matchPosition = -1;
			}
			else {
				this.ruleNumber = 1;
				this.matchPosition = Integer.valueOf(String.valueOf(elements[1].trim().charAt(1)));
			}
		}
		else {
			if ('0' == elements[1].trim().charAt(0)) {
				this.ruleNumber = 0;
				this.matchPosition = -1;
			}
			else {
				this.ruleNumber = 2;
				this.matchPosition = Integer.valueOf(String.valueOf(elements[1].trim().charAt(0)));
			}
		}
	}
	
	/**
	 * @param ruleElement
	 * @return
	 */
	public static MatchRule parse(String ruleElement) {
		MatchRule rule = new MatchRule();
		
		rule.parseRule(ruleElement);
		
		return rule;
	}
	
	/**
	 * @param rulesText
	 * @return
	 */
	public static List<MatchRule> parseRuleSetToList(String rulesText) {
		List<MatchRule> ruleSet = new ArrayList<>();
		
		if (null == rulesText) {
			throw new IllegalArgumentException("rulesText MUST NOT be null!");
		}

		String[] elements = rulesText.split(",");
		
		for (String current : elements) {
			MatchRule rule = new MatchRule();
			rule.parseRule(current);
			ruleSet.add(rule);
		}
		
		return ruleSet;
	}
	
	/**
	 * @param rulesText
	 * @return
	 */
	public static Map<Integer, List<MatchRule>> parseRuleSet(String rulesText) {
		Map<Integer, List<MatchRule>> rulesCollection = new TreeMap<>();
		
		if (null == rulesText) {
			throw new IllegalArgumentException("rulesText MUST NOT be null!");
		}

		String[] elements = rulesText.split(",");
		
		for (String current : elements) {
			MatchRule rule = new MatchRule();
			rule.parseRule(current);
			
			if (rulesCollection.containsKey(rule.getRuleNumber())) {
				List<MatchRule> ruleList = rulesCollection.get(rule.getRuleNumber());
				ruleList.add(rule);
			}
			else {
				List<MatchRule> ruleList = new ArrayList<>();
				ruleList.add(rule);
				rulesCollection.put(rule.getRuleNumber(), ruleList);
			}
		}
		
		return rulesCollection;
	}
	
}
