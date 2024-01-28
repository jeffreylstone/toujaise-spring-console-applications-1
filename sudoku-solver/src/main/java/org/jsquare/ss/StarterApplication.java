/**
 * StarterApplication.java
 * Spring Boot Starter Application for StockQuoteRecommendation job
 * Jeff Stone (jeffrey.l.stone@gmail.com)
 * 20191006
 */
package org.jsquare.ss;



import java.io.IOException;

import org.jsquare.ss.constants.Constants;
import org.jsquare.ss.model.Puzzle;
import org.jsquare.ss.util.EnvironmentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author jeffrey.l.stone
 *
 */
@SpringBootApplication
public class StarterApplication 
  implements CommandLineRunner {
	
	
	private static int[] testArray = { 
			-1, 3, 5, -1, 1, 2, -1, 8, 9,
			-1, 8, -1, -1, -1, 6, 2, 3, -1,
			-1, 2, -1, 8, -1, -1, 5, 6, 7,
			-1, -1, -1, 4, 2, -1, -1, 1, -1,
			1, -1, -1, -1, -1, -1, -1, -1, 5,
			-1, 6, -1, -1, 5, 8, -1, -1, -1,
			8, 9, 6, -1, -1, 7, -1, 5, -1,
			-1, 5, 3, 9, -1, -1, -1, 4, -1,
			7, 1, -1, 2, 6, -1, 8, 9, -1
	};
	

	private static int[] testSolutionArray = { 
			-1, 3, 5, -1, 1, 2, -1, 8, 9,
			-1, 8, -1, -1, -1, 6, 2, 3, -1,
			-1, 2, -1, 8, -1, -1, 5, 6, 7,
			-1, -1, -1, 4, 2, -1, -1, 1, -1,
			1, -1, -1, -1, -1, -1, -1, -1, 5,
			-1, 6, -1, -1, 5, 8, -1, -1, -1,
			8, 9, 6, -1, -1, 7, -1, 5, -1,
			-1, 5, 3, 9, -1, -1, -1, 4, -1,
			7, 1, -1, 2, 6, -1, 8, 9, -1
	};
	
	private static Logger LOGGER = LoggerFactory
      .getLogger(StarterApplication.class);
    
    @Autowired
    private ConfigurableEnvironment environment;
    
    public static void main(String[] args) {
        LOGGER.info("STARTING THE APPLICATION");
        SpringApplication.run(StarterApplication.class, args);
        LOGGER.info("APPLICATION FINISHED");
    }
  
    @Override
    public void run(String... args) {
        LOGGER.info("EXECUTING : command line runner");
        
        System.out.println("-----Command Line-----");
        for (String arg : args) {
        	System.out.println(arg);
        }
        System.out.println("----------------------");
        
        /*
        System.out.println("+---+---+---+---+---+---+---+---+---+");
        System.out.println("|   |   |   |   |   |   |   |   |   |");
        System.out.println("|   |   |   |   |   |   |   |   |   |");
        System.out.println("|   |   |   |   |   |   |   |   |   |");
        System.out.println("+---+---+---+---+---+---+---+---+---+");
        System.out.println("|   |   |   |   |   |   |   |   |   |");
        System.out.println("|   |   |   |   |   |   |   |   |   |");
        System.out.println("|   |   |   |   |   |   |   |   |   |");
        System.out.println("+---+---+---+---+---+---+---+---+---+");
        System.out.println("|   |   |   |   |   |   |   |   |   |");
        System.out.println("|   |   |   |   |   |   |   |   |   |");
        System.out.println("|   |   |   |   |   |   |   |   |   |");
        System.out.println("+---+---+---+---+---+---+---+---+---+");
        System.out.println("|   |   |   |   |   |   |   |   |   |");
        System.out.println("|   |   |   |   |   |   |   |   |   |");
        System.out.println("|   |   |   |   |   |   |   |   |   |");
        System.out.println("+---+---+---+---+---+---+---+---+---+");
        System.out.println("|   |   |   |   |   |   |   |   |   |");
        System.out.println("|   |   |   |   |   |   |   |   |   |");
        System.out.println("|   |   |   |   |   |   |   |   |   |");
        System.out.println("+---+---+---+---+---+---+---+---+---+");
        System.out.println("|   |   |   |   |   |   |   |   |   |");
        System.out.println("|   |   |   |   |   |   |   |   |   |");
        System.out.println("|   |   |   |   |   |   |   |   |   |");
        System.out.println("+---+---+---+---+---+---+---+---+---+");
        System.out.println("|   |   |   |   |   |   |   |   |   |");
        System.out.println("|   |   |   |   |   |   |   |   |   |");
        System.out.println("|   |   |   |   |   |   |   |   |   |");
        System.out.println("+---+---+---+---+---+---+---+---+---+");
        System.out.println("|   |   |   |   |   |   |   |   |   |");
        System.out.println("|   |   |   |   |   |   |   |   |   |");
        System.out.println("|   |   |   |   |   |   |   |   |   |");
        System.out.println("+---+---+---+---+---+---+---+---+---+");
        System.out.println("|   |   |   |   |   |   |   |   |   |");
        System.out.println("|   |   |   |   |   |   |   |   |   |");
        System.out.println("|   |   |   |   |   |   |   |   |   |");
        System.out.println("+---+---+---+---+---+---+---+---+---+");

        
        System.out.println("Element at 3,4 = " + CellUtil.elementNumber(3, 4));
        System.out.println("Element at 5,7 = " + CellUtil.elementNumber(5, 7));
        System.out.println("Element at 6,8 = " + CellUtil.elementNumber(6, 8));
        System.out.println("Element at 0,3 = " + CellUtil.elementNumber(0, 3));

        System.out.println("Element 31 = " + CellUtil.rowNumber(31) + ", " + CellUtil.columnNumber(31));
        System.out.println("Element 52 = " + CellUtil.rowNumber(52) + ", " + CellUtil.columnNumber(52));
        System.out.println("Element 62 = " + CellUtil.rowNumber(62) + ", " + CellUtil.columnNumber(62));
        System.out.println("Element 3 = " + CellUtil.rowNumber(3) + ", " + CellUtil.columnNumber(3));
        
        System.out.println("Element 31 = " + CellUtil.squareSetMinimumRowNumber(31) + ", " + CellUtil.squareSetMinimumColumnNumber(31) + " to " + CellUtil.squareSetMaximumRowNumber(31) + ", " + CellUtil.squareSetMaximumColumnNumber(31));
        System.out.println("Element 52 = " + CellUtil.squareSetMinimumRowNumber(52) + ", " + CellUtil.squareSetMinimumColumnNumber(52) + " to " + CellUtil.squareSetMaximumRowNumber(52) + ", " + CellUtil.squareSetMaximumColumnNumber(52));
        System.out.println("Element 62 = " + CellUtil.squareSetMinimumRowNumber(62) + ", " + CellUtil.squareSetMinimumColumnNumber(62) + " to " + CellUtil.squareSetMaximumRowNumber(62) + ", " + CellUtil.squareSetMaximumColumnNumber(62));
        System.out.println("Element 3 = " + CellUtil.squareSetMinimumRowNumber(3) + ", " + CellUtil.squareSetMinimumColumnNumber(3) + " to " + CellUtil.squareSetMaximumRowNumber(3) + ", " + CellUtil.squareSetMaximumColumnNumber(3));
        
        
        Element testElement = new Element();
        
        for (int i = 1; i <= 9; i++) {
        	if (testElement.remove(i)) {
        		System.out.println("Resolved - remaining element = " + testElement.getCandidateValues().get(0));
        	}
        	else {
        		System.out.println("Removed " + i + ".  " + testElement.getCandidateValues().size() + " elements left.");
        	}
        }
        
        */

        
        Puzzle testPuzzle = new Puzzle();
        
        try {
        	testPuzzle.load(environment.getProperty(Constants.INPUT_FILENAME_PROPERTY_NAME, Constants.DEFAULT_INPUT_FILENAME));
        	testPuzzle.output(environment.getProperty(Constants.OUTPUT_FILENAME_PROPERTY_NAME, Constants.DEFAULT_OUTPUT_FILENAME));
        	testPuzzle.solve();
        	testPuzzle.output(environment.getProperty(Constants.OUTPUT_FILENAME_PROPERTY_NAME, Constants.DEFAULT_OUTPUT_FILENAME), Boolean.TRUE);
        }
        catch (IOException ioe) {
        	// log error
        	
        }
        
        
/*        
        
        testPuzzle.load(testArray);
        
        System.out.println(testPuzzle.renderPuzzle());
        
        testPuzzle.resolveRows();
        testPuzzle.resolveColumns();
        testPuzzle.resolveSquareSets();
        
//        System.out.println(testPuzzle.renderPuzzle(Boolean.TRUE));
//        
//        for (Integer current : testPuzzle.getResolveQueue()) {
//        	System.out.println(current);
//        }
        
        int iterationCounter = 1;
        while (!testPuzzle.getResolveQueue().isEmpty()) {
        	Integer elementNumber = testPuzzle.getResolveQueue().remove();
        	testPuzzle.resolveRow(elementNumber);
        	testPuzzle.resolveColumn(elementNumber);
        	testPuzzle.resolveSquareSet(elementNumber);
        	if (++iterationCounter > Constants.SUDOKU_MAX_ELEMENTS) {
        		System.out.println("Out of Control!");
        		break;
        	}
        }
        
        System.out.println("iterations = " + iterationCounter);
        System.out.println(testPuzzle.renderPuzzle(Boolean.TRUE));
  
        
*/        
        System.out.println("-----Environment-----");
     //   environment.
        EnvironmentUtil.printProperties(environment);
        
        
        System.out.println("test-argument = " + environment.getProperty("test-argument"));
        System.out.println("spring.main.web-application-type = " + environment.getProperty("spring.main.web-application-type"));
        System.out.println("simulation = " + environment.getProperty("simulation"));
        System.out.println("---------------------");
   
   }
}