/**
 * CellUtil.java
 * 
 * Jeff Stone (jeffrey.l.stone@gmail.com)
 * 
 */
package org.jsquare.ss.util;

import org.jsquare.ss.constants.Constants;

public class CellUtil {
	
	public static Integer elementNumber(Integer row, Integer column) {
		Integer elementNumber = -1;
		
		if (row < 0 || row >= Constants.SUDOKU_MAX_ROWS) {
			throw new IndexOutOfBoundsException("row must be zero or greater less than " + Constants.SUDOKU_MAX_ROWS + "!");
		}
		if (column < 0 || column >= Constants.SUDOKU_MAX_COLUMNS) {
			throw new IndexOutOfBoundsException("column must be zero or greater and less than " + Constants.SUDOKU_MAX_ROWS + "!");
		}
		
		elementNumber = row * Constants.SUDOKU_MAX_ROWS + column;
		
		return elementNumber;
	}
	
	public static Integer rowNumber(Integer elementNumber) {
		Integer rowNumber = -1;
		
		if (elementNumber < 0 || elementNumber >= Constants.SUDOKU_MAX_ELEMENTS) {
			throw new IndexOutOfBoundsException("elementNumber must be zero or greater and less than " + Constants.SUDOKU_MAX_ELEMENTS + "!");
		}
		
		rowNumber = elementNumber / Constants.SUDOKU_MAX_COLUMNS;
		
		return rowNumber;
	}
	
	public static Integer columnNumber(Integer elementNumber) {
		Integer columnNumber = -1;
		
		if (elementNumber < 0 || elementNumber >= Constants.SUDOKU_MAX_ELEMENTS) {
			throw new IndexOutOfBoundsException("elementNumber must be zero or greater and less than " + Constants.SUDOKU_MAX_ELEMENTS + "!");
		}
		
		columnNumber = elementNumber % Constants.SUDOKU_MAX_COLUMNS;
		
		return columnNumber;
	}
	
	public static Integer squareSetMinimumColumnNumber(Integer elementNumber) {
		Integer squareSetColumnNumber = -1;
		
		if (elementNumber < 0 || elementNumber >= Constants.SUDOKU_MAX_ELEMENTS) {
			throw new IndexOutOfBoundsException("elementNumber must be zero or greater and less than " + Constants.SUDOKU_MAX_ELEMENTS + "!");
		}

		int columnNumber = columnNumber(elementNumber);
		
		for (int i = 0; i < Constants.SUDOKU_MAX_SQUARE_SET_COLUMNS; i++) {
			if (columnNumber >= Constants.SUDOKU_SQUARE_SET_START_COLUMNS[i]) {
				squareSetColumnNumber = Constants.SUDOKU_SQUARE_SET_START_COLUMNS[i];
			}
			else {
				break;
			}
		}
		
		return squareSetColumnNumber;
	}
	
	public static Integer squareSetMaximumColumnNumber(Integer elementNumber) {
		Integer squareSetColumnNumber = -1;
		
		if (elementNumber < 0 || elementNumber >= Constants.SUDOKU_MAX_ELEMENTS) {
			throw new IndexOutOfBoundsException("elementNumber must be zero or greater and less than " + Constants.SUDOKU_MAX_ELEMENTS + "!");
		}

		int columnNumber = columnNumber(elementNumber);
		
		for (int i = 0; i < Constants.SUDOKU_MAX_SQUARE_SET_COLUMNS; i++) {
			if (columnNumber >= Constants.SUDOKU_SQUARE_SET_START_COLUMNS[i]) {
				squareSetColumnNumber = Constants.SUDOKU_SQUARE_SET_END_COLUMNS[i];
			}
			else {
				break;
			}
		}
		
		return squareSetColumnNumber;
	}
	
	public static Integer squareSetMinimumRowNumber(Integer elementNumber) {
		Integer squareSetRowNumber = -1;
		
		if (elementNumber < 0 || elementNumber >= Constants.SUDOKU_MAX_ELEMENTS) {
			throw new IndexOutOfBoundsException("elementNumber must be zero or greater and less than " + Constants.SUDOKU_MAX_ELEMENTS + "!");
		}

		int rowNumber = rowNumber(elementNumber);

		for (int i = 0; i < Constants.SUDOKU_MAX_SQUARE_SET_ROWS; i++) {
			if (rowNumber >= Constants.SUDOKU_SQUARE_SET_START_ROWS[i]) {
				squareSetRowNumber = Constants.SUDOKU_SQUARE_SET_START_ROWS[i];
			}
			else {
				break;
			}
		}

		return squareSetRowNumber;
	}
	
	public static Integer squareSetMaximumRowNumber(Integer elementNumber) {
		Integer squareSetRowNumber = -1;
		
		if (elementNumber < 0 || elementNumber >= Constants.SUDOKU_MAX_ELEMENTS) {
			throw new IndexOutOfBoundsException("elementNumber must be zero or greater and less than " + Constants.SUDOKU_MAX_ELEMENTS + "!");
		}

		int rowNumber = rowNumber(elementNumber);

		for (int i = 0; i < Constants.SUDOKU_MAX_SQUARE_SET_ROWS; i++) {
			if (rowNumber >= Constants.SUDOKU_SQUARE_SET_START_ROWS[i]) {
				squareSetRowNumber = Constants.SUDOKU_SQUARE_SET_END_ROWS[i];
			}
			else {
				break;
			}
		}

		return squareSetRowNumber;
	}

	public CellUtil() {
		// TODO Auto-generated constructor stub
	}

}
