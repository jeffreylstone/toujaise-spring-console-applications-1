/**
 * Constants.java
 * 
 * Jeff Stone (jeffrey.l.stone@gmail.com)
 * 
 */
package org.jsquare.ss.constants;

/**
 * @author jeffrey.l.stone
 *
 */
public class Constants {
	
	public static final int SUDOKU_MAX_ROWS = 9;
	public static final int SUDOKU_MAX_COLUMNS = 9;
	public static final int SUDOKU_MAX_ELEMENTS = SUDOKU_MAX_ROWS * SUDOKU_MAX_COLUMNS;
	public static final int SUDOKU_MAX_SQUARE_SET_ROWS = 3;
	public static final int[] SUDOKU_SQUARE_SET_START_ROWS = {0,3,6};
	public static final int[] SUDOKU_SQUARE_SET_END_ROWS = {2,5,8};
	public static final int SUDOKU_MAX_SQUARE_SET_COLUMNS = 3;
	public static final int[] SUDOKU_SQUARE_SET_START_COLUMNS = {0,3,6};
	public static final int[] SUDOKU_SQUARE_SET_END_COLUMNS = {2,5,8};
	
	public static final String NEWLINE = "\n";
	
	public static final String INPUT_FILENAME_PROPERTY_NAME = "sudoku.input.fileName";
	public static final String DEFAULT_INPUT_FILENAME = "defaultInputFile";
	public static final String OUTPUT_FILENAME_PROPERTY_NAME = "sudoku.output.fileName";
	public static final String DEFAULT_OUTPUT_FILENAME = "defaultOutputFile";
				
	/**
	 * 
	 */
	private Constants() {
		// Cannot instantiate this "Utility" class!
	}
}
