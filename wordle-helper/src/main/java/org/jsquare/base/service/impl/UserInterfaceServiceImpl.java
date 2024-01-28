/**
 * UserInterfaceServiceImpl.java
 * Implementation of UserInterfaceService interface for this application
 * jeffrey.l.stone@gmail.com
 * 20240102
 * 
 */
package org.jsquare.base.service.impl;

import java.io.IOException;

import org.jsquare.base.service.UserInterfaceService;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * UserInterfaceServiceImpl
 */
@Service
@Slf4j
public class UserInterfaceServiceImpl implements UserInterfaceService {

	@Override
	public String getNextCharacterString() {
		// TODO Auto-generated method stub
		String seedChars = null;
		
		System.out.println("\nEnter next set of character/condition results (or /q to quit, /r to reset):  ");
		
		byte[] seedBytes = new byte[160];
		
		try { 
			
			int charsRead = System.in.read(seedBytes);
			
			seedChars = new String(seedBytes, 0, charsRead);
			
			int position = 0;
			
			if (-1 != (position = seedChars.indexOf("\n"))) {
				seedChars = seedChars.substring(0, position);
				if (-1 != (position = seedChars.indexOf("\r"))) {
					seedChars = seedChars.substring(0, position);
				}
			}
			
			// temp logging
			System.out.println("\nYou entered:  " + seedChars);
		}
		catch (IOException ioe) {
			throw new IllegalStateException("1");
		}

		return seedChars;
	}

}
