/**
 * WordleHelperApplication.java
 * 
 * jeffrey.l.stone@gmail.com
 * 20240102
 * 
 */
package org.jsquare.base;

import java.util.List;
import java.util.Map;

import org.jsquare.base.model.MatchRule;
import org.jsquare.base.service.DictionaryService;
//import org.jsquare.base.service.GeneratorService;
import org.jsquare.base.service.UserInterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;

import lombok.extern.slf4j.Slf4j;

/**
 * WordleHelperApplication
 */
@SpringBootApplication
@Slf4j
public class WordleHelperApplication implements CommandLineRunner {

	@Autowired
	ConfigurableEnvironment environment;
	
	@Autowired
	UserInterfaceService userInterface;
	
	@Autowired
	DictionaryService dictionary;
	
//	@Autowired
//	GeneratorService generator;
	
    /**
     * @param args
     */
    public static void main(String[] args) {
        log.info("STARTING THE APPLICATION");
        SpringApplication.run(WordleHelperApplication.class, args);
        log.info("APPLICATION FINISHED");
    }
 
    @Override
    public void run(String... args) {
        log.info("EXECUTING : command line runner");
 
        for (int i = 0; i < args.length; ++i) {
            log.info("args[{}]: {}", i, args[i]);
        }
        
        // Load dictionary
        // display message, "Loading Dictionary..."
        
        // load dictionary specified in configuration
        dictionary.loadDictionary(environment.getProperty("dictionary.filename", "valid-wordle-words.txt"));
        
        // Start user loop
        Boolean getNextSeed = Boolean.TRUE;
        
        while (getNextSeed) {
        	
        	String seedChars = userInterface.getNextCharacterString();
        	seedChars = seedChars.trim();
        	int nbrOfChars = seedChars.length();
        	
        	if (seedChars.equalsIgnoreCase("/q")) {
        		getNextSeed = Boolean.FALSE;
        	}
        	else if (seedChars.equalsIgnoreCase("/r")) {
        		// reset/re-initialize
        		dictionary.reloadDictionary(environment.getProperty("dictionary.filename", "valid-wordle-words.txt"));
        	}
        	else {
        		// do stuff
        		
        		Map<Integer, List<MatchRule>> rulesCollection = MatchRule.parseRuleSet(seedChars);
        		this.displayRules(rulesCollection);
        		this.matchOrExcludeWords(rulesCollection);
        		

        		
        		
        		

//        		List<String> generatedStrings = generator.generateWords(seedChars);
//        		Map<Integer, Set<String>> validWordsByLength = new TreeMap<>();
//        		Set<String> validWords = new TreeSet<>();
//        		
//        		for (String current : generatedStrings) {
//        			
//        			for (int i = 3; i <= nbrOfChars; i++) {
//        				String testString = current.substring(0, i);
//        				if (dictionary.isDictionaryWord(testString)) {
////        					System.out.println(testString);
//        					validWords.add(testString);
//        					if (validWordsByLength.containsKey(i)) {
//        						validWordsByLength.get(i).add(testString);
//        					}
//        					else {
//        						Set<String> validWordsForLength = new TreeSet<>();
//        						validWordsForLength.add(testString);
//        						validWordsByLength.put(i, validWordsForLength);
//        					}
//        				}
//        			}
//        		}
        		
        		// display words (highest prob to lowest) 8 per line (word, 5 spaces, word, 5 spaces,...)
        		
        		this.displayRemainingWords();
        		
        		
//        		System.out.println("----------------------------------------");
//        		for (String goodWord : validWords) {
//        			System.out.println(goodWord);
//        		}
//
//        		System.out.println("----------------------------------------");
//        		for (Integer currentLength : validWordsByLength.keySet()) {
//        			System.out.println(currentLength + ": ");
//        			for (String currentWord : validWordsByLength.get(currentLength)) {
//        				System.out.println(currentWord);
//        			}
//        		}
        	}
        }
    }
    
	private void matchOrExcludeWords(Map<Integer, List<MatchRule>> rulesCollection) {
    	for (Map.Entry<Integer, List<MatchRule>> current : rulesCollection.entrySet()) {
        	for (MatchRule currentRule : current.getValue()) {
        		dictionary.matchOrExcludeWords(currentRule);
        	}
    	}
	}

	private void displayRemainingWords() {
    	int currentWordCountForLine = 0;
    	for (String current : this.dictionary.getRemainingWords()) {
    		System.out.print(current);
    		if (8 == ++currentWordCountForLine) {
        		System.out.println("     ");
        		currentWordCountForLine = 0;
    		}
    		else {
        		System.out.print("     ");
    		}
    	}
    	
    	System.out.println("");
    }
    
    private void displayRules(Map<Integer, List<MatchRule>> rulesCollection) {
    	for (Map.Entry<Integer, List<MatchRule>> current : rulesCollection.entrySet()) {
        	for (MatchRule currentRule : current.getValue()) {
        		System.out.print(currentRule.toString());
        		System.out.print("  ");
        	}
    	}
    	
    	System.out.println("");
    }

    
    
    
}