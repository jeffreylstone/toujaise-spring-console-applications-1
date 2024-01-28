/**
 * Affix.java
 * 
 * jeffrey.l.stone@gmail.com
 * 20240123
 * 
 */
package org.jsquare.base.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//PFX flag cross_product number
//
//PFX flag stripping prefix [condition [morphological_fields...]]
//
//SFX flag cross_product number
//
//SFX flag stripping suffix [condition [morphological_fields...]]
//       An affix is either a prefix or a suffix attached to root words to make other words.
//       We  can  define affix classes with arbitrary number affix rules.  Affix classes are
//       signed with affix flags. The first line of an affix class definition is the header.
//       The fields of an affix class header:
//
//       (0) Option name (PFX or SFX)
//
//       (1) Flag (name of the affix class)
//
//       (2)  Cross product (permission to combine prefixes and suffixes).  Possible values:
//       Y (yes) or N (no)
//
//       (3) Line count of the following rules.
//
//       Fields of an affix rules:
//
//       (0) Option name
//
//       (1) Flag
//
//       (2) stripping characters from beginning (at prefix rules) or end (at suffix  rules)
//       of the word
//
//       (3) affix (optionally with flags of continuation classes, separated by a slash)
//
//       (4) condition.
//
//       Zero  stripping or affix are indicated by zero. Zero condition is indicated by dot.
//       Condition is a simplified, regular  expression-like  pattern,  which  must  be  met
//       before  the  affix can be applied. (Dot signs an arbitrary character. Characters in
//       braces sign an arbitrary character from  the  character  subset.  Dash  hasn't  got
//       special  meaning,  but  circumflex  (^)  next the first brace sets the complementer
//       character set.)
//
//       (5) Optional morphological fields separated by spaces or tabulators.

/**
 * Affix
 */
@NoArgsConstructor
@Getter
@Setter
public class Affix {
	
	public static final int AFFIX_PREFIX = 0;
	public static final int AFFIX_SUFFIX = 1;
	
	private String id;
	
	private int affixType;
	
	private boolean crossProductFlag;
	
	private List<AffixRule> affixRules;

}
