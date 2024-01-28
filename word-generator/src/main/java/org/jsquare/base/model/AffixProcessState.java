/**
 * AffixProcessState.java
 * 
 * jeffrey.l.stone@gmail.com
 * 20240124
 * 
 */
package org.jsquare.base.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * AffixProcessState
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AffixProcessState {
	
	
	private boolean inAffix;
	
	private int nbrOfRules;
	
	public void decrementRuleCounter() {
		if (0 < this.nbrOfRules) {
			this.nbrOfRules--;
		}
	}
}
