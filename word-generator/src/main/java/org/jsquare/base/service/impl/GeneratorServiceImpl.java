/**
 * 
 */
package org.jsquare.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.jsquare.base.service.GeneratorService;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Service
@Slf4j
public class GeneratorServiceImpl implements GeneratorService {

	@Override
	public List<String> generateWords(String seedChars) {
		int nbrOfChars = seedChars.length();
		List<String> stringList = new ArrayList<>();
		
		for (int i = 0; i < nbrOfChars; i++) {
			// recurse
			doX("", i, seedChars, stringList);
		}
		
		// TODO Auto-generated method stub
		return stringList;
	}
	
	// recursion (?)
	
	private void doX(String currentString, int currentIndex, String seedChars, List<String> outList) {

		StringBuilder buffer = new StringBuilder();

		// recursive condition
		if (1 == seedChars.length()) {
			buffer.append(currentString).append(seedChars);
			outList.add(buffer.toString());
		}
		else {
			// recurse
			buffer.append(currentString).append(seedChars.charAt(currentIndex));
			String nextString = buffer.toString();

			// remove character at current index
			String nextSeedChars = this.splitAndJoin(seedChars, currentIndex);

			for (int i = 0; i < nextSeedChars.length(); i++) {
				doX(nextString, i, nextSeedChars, outList);
			}
		}
	}

	private String splitAndJoin(String seedChars, int currentIndex) {

		StringBuilder buffer = new StringBuilder();
		
		if (0 < currentIndex) {
			buffer.append(seedChars.substring(0, currentIndex));
		}
		
		if (seedChars.length() > currentIndex) {
			buffer.append(seedChars.substring(currentIndex + 1, seedChars.length()));
		}
		
		return buffer.toString();
	}

}
