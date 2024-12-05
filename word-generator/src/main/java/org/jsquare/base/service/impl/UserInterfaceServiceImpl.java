/**
 * 
 */
package org.jsquare.base.service.impl;

import java.io.IOException;

import org.jsquare.base.service.UserInterfaceService;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Service
@Slf4j
public class UserInterfaceServiceImpl implements UserInterfaceService {
	
	private Integer permutationsWordLength;

	@Override
	public String getNextCharacterString() {
		// TODO Auto-generated method stub
		String seedChars = null;
		
		this.permutationsWordLength = null;
		
		System.out.println("\nEnter next set of characters (or /q to quit):  ");
		
		byte[] seedBytes = new byte[15];
		
		try { 
			
			int charsRead = System.in.read(seedBytes);
			
			// readAllBytes, then trim to "max nbr of chars" length
			
			seedChars = new String(seedBytes, 0, charsRead);
			
			int position = 0;
			
			if (-1 != (position = seedChars.indexOf("\n"))) {
				seedChars = seedChars.substring(0, position);
				if (-1 != (position = seedChars.indexOf("\r"))) {
					seedChars = seedChars.substring(0, position);
				}
			}
			
			String[] elements = seedChars.split(" ");
			
			if (2 == elements.length) {
				try {
					Integer permsWordLength = Integer.valueOf(elements[1].trim());
					permutationsWordLength = permsWordLength;
					seedChars = elements[0].trim();
				}
				catch (NumberFormatException nfe) {
					// recurse
					System.out.println("\nYou entered:  " + seedChars);
					return getNextCharacterString();
				}
			}
			else if (2 < elements.length) {
				// recurse
				System.out.println("\nYou entered:  " + seedChars);
				return getNextCharacterString();
			}
			
			// temp logging
			System.out.println("\nYou entered:  " + seedChars);
		}
		catch (IOException ioe) {
			throw new IllegalStateException("1");
		}

		return seedChars;
	}

	@Override
	public Integer getPermutationsWordLength() {
		return permutationsWordLength;
	}

}
