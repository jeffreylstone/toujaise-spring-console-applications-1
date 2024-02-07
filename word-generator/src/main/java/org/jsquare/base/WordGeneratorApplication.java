/**
 * WordGeneratorApplication.java
 * 
 * jeffrey.l.stone@gmail.com
 * 2024mmdd
 */
package org.jsquare.base;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.jsquare.base.service.DictionaryService;
import org.jsquare.base.service.GeneratorService;
import org.jsquare.base.service.UserInterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;

import lombok.extern.slf4j.Slf4j;

/**
 * WordGeneratorApplication
 */
@SpringBootApplication
@Slf4j
public class WordGeneratorApplication implements CommandLineRunner {

	@Autowired
	ConfigurableEnvironment environment;
	
	@Autowired
	UserInterfaceService userInterface;
	
	@Autowired
	DictionaryService dictionary;
	
	@Autowired
	GeneratorService generator;
	
    public static void main(String[] args) {
        log.info("STARTING THE APPLICATION");
        SpringApplication.run(WordGeneratorApplication.class, args);
        log.info("APPLICATION FINISHED");
    }
 
    @Override
    public void run(String... args) {
        log.info("EXECUTING : command line runner");
 
        for (int i = 0; i < args.length; ++i) {
            log.info("args[{}]: {}", i, args[i]);
        }

        // load affixes specified in configuration
        dictionary.loadAffixes(environment.getProperty("affix.filename", "index.aff"));
        
        // load dictionary specified in configuration
        dictionary.loadDictionary(environment.getProperty("dictionary.filename", "index.dic"));
        
//        // TEST
//        dictionary.loadDictionarySampleTest();
        
        // Start user loop
        Boolean getNextSeed = Boolean.TRUE;
        
        while (getNextSeed) {
        	
        	String seedChars = userInterface.getNextCharacterString();
        	int nbrOfChars = seedChars.length();
        	
        	if (!seedChars.equalsIgnoreCase("/q")) {
        		// do stuff

        		List<String> generatedStrings = generator.generateWords(seedChars);
        		Map<Integer, Set<String>> validWordsByLength = new TreeMap<>();
        		Set<String> validWords = new TreeSet<>();
        		
        		for (String current : generatedStrings) {
        			
        			for (int i = 3; i <= nbrOfChars; i++) {
        				String testString = current.substring(0, i);
        				if (dictionary.isDictionaryWord(testString)) {
//        					System.out.println(testString);
        					validWords.add(testString);
        					if (validWordsByLength.containsKey(i)) {
        						validWordsByLength.get(i).add(testString);
        					}
        					else {
        						Set<String> validWordsForLength = new TreeSet<>();
        						validWordsForLength.add(testString);
        						validWordsByLength.put(i, validWordsForLength);
        					}
        				}
        			}
        		}
        		
//        		System.out.println("----------------------------------------");
//        		for (String goodWord : validWords) {
//        			System.out.println(goodWord);
//        		}

        		System.out.println("----------------------------------------");
        		for (Integer currentLength : validWordsByLength.keySet()) {
        			System.out.println(currentLength + ": ");
        			int lineLength = 0;
        			for (String currentWord : validWordsByLength.get(currentLength)) {
        				System.out.print(currentWord);
        				System.out.print("  ");
        				lineLength += currentWord.length() + 2;
        				if (lineLength > 72) {
        					System.out.println("");
        					lineLength = 0;
        				}
        			}
        			System.out.println("");
        		}
        	}
        	else {
        		getNextSeed = Boolean.FALSE;
        	}
        }
    }
}