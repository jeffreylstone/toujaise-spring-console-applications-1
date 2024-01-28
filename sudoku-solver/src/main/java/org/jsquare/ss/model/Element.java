/**
 * 
 */
package org.jsquare.ss.model;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * @author rock0
 *
 */
@Getter
@Setter
public class Element {
	
	private Boolean resolved;
	private ArrayList<Integer> candidateValues;
	

	/**
	 * 
	 */
	public Element() {

		candidateValues = new ArrayList<>();
		candidateValues.add(1);
		candidateValues.add(2);
		candidateValues.add(3);
		candidateValues.add(4);
		candidateValues.add(5);
		candidateValues.add(6);
		candidateValues.add(7);
		candidateValues.add(8);
		candidateValues.add(9);
		this.resolved = Boolean.FALSE;
	}

	public Element(Integer initialValue) {
		
		candidateValues = new ArrayList<>();
		candidateValues.add(initialValue);
		this.resolved = Boolean.TRUE;
	}
	
	public Boolean remove(Integer candidateValue) {
		Boolean resolved = Boolean.FALSE;
		
		Boolean removed = this.getCandidateValues().remove((Integer) candidateValue);
		
		if (removed) {
			if (1 == this.candidateValues.size())
			{
				resolved = Boolean.TRUE;
				this.setResolved(resolved);
			}
		}
		
		return resolved;
	}
	
}
