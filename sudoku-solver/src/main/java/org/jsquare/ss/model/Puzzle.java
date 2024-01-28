/**
 * Puzzle.java
 * 
 * Jeff Stone (jeffrey.l.stone@gmail.com)
 * 20230809
 * 
 */
package org.jsquare.ss.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.jsquare.ss.constants.Constants;
import org.jsquare.ss.util.CellUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * @author rock0
 *
 */
@Getter
@Setter
public class Puzzle {
	
	private Element[] elements;
	private ArrayList<Integer> currentRow;
	private ArrayList<Integer> currentColumn;
	private ArrayList<Integer> currentSquareSet;
	private Queue<Integer> resolveQueue;
	private Integer iterationCounter;

	/**
	 * 
	 */
	public Puzzle() {

		this.elements = new Element[Constants.SUDOKU_MAX_ROWS * Constants.SUDOKU_MAX_COLUMNS];
		this.currentRow = new ArrayList<>();
		this.currentColumn = new ArrayList<>();
		this.currentSquareSet = new ArrayList<>();
		this.resolveQueue = new LinkedList<>();
		this.iterationCounter = Integer.valueOf(0);
	}
	

	// initialize from array
	public int initialize(Integer[] puzzleCellValues) {return 0;}
	
	public int initialize(int[] puzzleCellValues) {
		
		for (int i = 0; i < Constants.SUDOKU_MAX_ROWS*Constants.SUDOKU_MAX_COLUMNS; i++) {
			if (i >= puzzleCellValues.length 
					|| 0 >= puzzleCellValues[i] 
					|| Constants.SUDOKU_MAX_COLUMNS < puzzleCellValues[i]) {
				this.elements[i] = new Element();
			}
			else {
				this.elements[i] = new Element(puzzleCellValues[i]);
			}
		}
		return 0;
	}
	
	// load 
	// load array from file, then initialize()
	public int load(String filename) throws IOException {
		
		try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
			String line = null;
			StringBuilder buffer = new StringBuilder();
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			
			String[] puzzleCellTextValues = buffer.toString().split(",");
			int[] puzzleCellValues = new int[puzzleCellTextValues.length];
			int index = 0;
			
			for (String current : puzzleCellTextValues) {
				String testText = current.trim();
				int cellValue = -1;
				if (!testText.isEmpty()) {
					try {
						cellValue = Integer.valueOf(testText);
					}
					catch (NumberFormatException nfe) {
						// log nfe
						cellValue = -1;
					}
				}
				puzzleCellValues[index++] = cellValue;
			}
			
			this.initialize(puzzleCellValues);
		}
		catch (IOException ioe) {
			// log ioe
			throw ioe;
		}
		
		return 0;
	}
	
	
	public String renderPuzzle(Boolean debug) {
		StringBuilder buffer = new StringBuilder(1500);
		buffer.append(gridLine()).append(Constants.NEWLINE);
		for (int i = 0; i < Constants.SUDOKU_MAX_ROWS; i++) {
			buffer.append(spaceLine()).append(Constants.NEWLINE);
			buffer.append(debugValueLine(i, debug)).append(Constants.NEWLINE);
			buffer.append(spaceLine()).append(Constants.NEWLINE);
			buffer.append(gridLine()).append(Constants.NEWLINE);
		}
		
		return buffer.toString();
	}

	public String renderPuzzle() {
		return renderPuzzle(Boolean.FALSE);
	}

	private String gridLine() {
		StringBuilder buffer = new StringBuilder(40);
		buffer.append("+");
		for (int i = 0; i < Constants.SUDOKU_MAX_COLUMNS; i++) {
			buffer.append("---+");
		}
		
		return buffer.toString();
	}
	
	private String spaceLine() {
		StringBuilder buffer = new StringBuilder(40);
		buffer.append("|");
		for (int i = 0; i < Constants.SUDOKU_MAX_COLUMNS; i++) {
			buffer.append("   |");
		}
		
		return buffer.toString();
	}
	
	private String debugValueLine(int rowNumber, Boolean debug) {
		
		StringBuilder buffer = new StringBuilder(40);
		buffer.append("|");
		for (int i = 0; i < Constants.SUDOKU_MAX_COLUMNS; i++) {
			if (this.elements[CellUtil.elementNumber(rowNumber, i)].getResolved()) {
				buffer.append(" ").append(this.elements[CellUtil.elementNumber(rowNumber, i)].getCandidateValues().get(0)).append(" |");
			}
			else {
				if (debug) {
					ArrayList<Integer> values = this.elements[CellUtil.elementNumber(rowNumber, i)].getCandidateValues();
					for (Integer current : values) {
						buffer.append(current).append(",");
					}
					buffer.append("|");
				}
				else {
					buffer.append("   |");
				}
			}
		}
		
		return buffer.toString();
	}

	public int resolveRows() {
		
		for (int i = 0; i < Constants.SUDOKU_MAX_ROWS; i++) {
			this.resolveRowForRowNumber(i);
		}
		
		return 0;
	}
	
	public int resolveRow(Integer elementNumber) {
		return this.resolveRowForRowNumber(CellUtil.rowNumber(elementNumber));
	}
	
	private int resolveRowForRowNumber(Integer rowNumber) {
		this.currentRow.clear();

		// load resolved values into currentRow array
		for (int i = 0; i < Constants.SUDOKU_MAX_COLUMNS; i++) {
			Element currentElement = this.elements[CellUtil.elementNumber(rowNumber, i)];
			if (currentElement.getResolved()) {
				int value = currentElement.getCandidateValues().get(0);
				this.currentRow.add(value);
			}
		}

		// iterate over this row's elements, removing already resolved values
		for (int i = 0; i < Constants.SUDOKU_MAX_COLUMNS; i++) {
			Element currentElement = this.elements[CellUtil.elementNumber(rowNumber, i)];
			if (!currentElement.getResolved()) {
				for (Integer current : this.currentRow) {
					currentElement.remove(current);
				}
				// If element is resolved following removals, add the element to the resolveQueue
				//  to trigger additional resolution attempts
				if (currentElement.getResolved()) {
					// add (elementNumber) to resolve queue
					this.resolveQueue.add(CellUtil.elementNumber(rowNumber, i));
				}
			}
		}
		
		return 0;
	}
	
	public int resolveColumns() {
		
		for (int i = 0; i < Constants.SUDOKU_MAX_COLUMNS; i++) {
			this.resolveColumnForColumnNumber(i);
		}
		
		return 0;
	}
		
	public int resolveColumn(Integer elementNumber) {
		return this.resolveColumnForColumnNumber(CellUtil.columnNumber(elementNumber));
	}
		
	private int resolveColumnForColumnNumber(Integer columnNumber) {
		this.currentColumn.clear();

		// load resolved values into currentColumn array
		for (int i = 0; i < Constants.SUDOKU_MAX_ROWS; i++) {
			Element currentElement = this.elements[CellUtil.elementNumber(i, columnNumber)];
			if (currentElement.getResolved()) {
				int value = currentElement.getCandidateValues().get(0);
				this.currentColumn.add(value);
			}
		}
		
		// iterate over this column's elements, removing already resolved values
		for (int i = 0; i < Constants.SUDOKU_MAX_ROWS; i++) {
			Element currentElement = this.elements[CellUtil.elementNumber(i, columnNumber)];
			if (!currentElement.getResolved()) {
				for (Integer current : this.currentColumn) {
					currentElement.remove(current);
				}
				// If element is resolved following removals, add the element to the resolveQueue
				//  to trigger additional resolution attempts
				if (currentElement.getResolved()) {
					// add (elementNumber) to resolve queue
					this.resolveQueue.add(CellUtil.elementNumber(i, columnNumber));
				}
			}
		}
		
		return 0;
	}
		
	public int resolveSquareSets() {
		for (int i = 0; i < Constants.SUDOKU_MAX_SQUARE_SET_ROWS; i++) {
			for (int j = 0; j < Constants.SUDOKU_MAX_SQUARE_SET_COLUMNS; j++) {
				this.resolveSquareSetForRowAndColumn(Constants.SUDOKU_SQUARE_SET_START_ROWS[i], Constants.SUDOKU_SQUARE_SET_START_COLUMNS[j]);
			}
		}
		
		return 0;
	}
	
	public int resolveSquareSet(Integer elementNumber) {
		return this.resolveSquareSetForRowAndColumn(CellUtil.rowNumber(elementNumber), CellUtil.columnNumber(elementNumber));
	}

	private int resolveSquareSetForRowAndColumn(Integer rowNumber, Integer columnNumber) {
		this.currentSquareSet.clear();
		
		int startRow = CellUtil.squareSetMinimumRowNumber(CellUtil.elementNumber(rowNumber, columnNumber));
		int endRow = CellUtil.squareSetMaximumRowNumber(CellUtil.elementNumber(rowNumber, columnNumber));
		int startColumn = CellUtil.squareSetMinimumColumnNumber(CellUtil.elementNumber(rowNumber, columnNumber));
		int endColumn = CellUtil.squareSetMaximumColumnNumber(CellUtil.elementNumber(rowNumber, columnNumber));
		
		// load resolved values into the currentSquareSet array
		for (int i = startRow; i <= endRow; i++) {
			for (int j = startColumn; j <= endColumn; j++) {
				Element currentElement = this.elements[CellUtil.elementNumber(i, j)];
				if (currentElement.getResolved()) {
					int value = currentElement.getCandidateValues().get(0);
					this.currentSquareSet.add(value);
				}
			}
		}
		
		// iterate over this square set's elements, removing already resolved values
		for (int i = startRow; i <= endRow; i++) {
			for (int j = startColumn; j <= endColumn; j++) {
				Element currentElement = this.elements[CellUtil.elementNumber(i, j)];
				if (!currentElement.getResolved()) {
					for (Integer current : this.currentSquareSet) {
						currentElement.remove(current);
					}
					// If element is resolved following removals, add the element to the resolveQueue
					//  to trigger additional resolution attempts
					if (currentElement.getResolved()) {
						// add (elementNumber) to resolve queue
						this.resolveQueue.add(CellUtil.elementNumber(i, j));
					}
				}
			}
		}
		
		return 0;
	}
	
	
	/**
	 * @return
	 */
	public int solve() {
		
        this.resolveRows();
        this.resolveColumns();
        this.resolveSquareSets();
        
        this.iterationCounter++;
        
        while (!this.getResolveQueue().isEmpty()) {
        	Integer elementNumber = this.getResolveQueue().remove();
        	this.resolveRow(elementNumber);
        	this.resolveColumn(elementNumber);
        	this.resolveSquareSet(elementNumber);
        	
        	if (++(this.iterationCounter) > Constants.SUDOKU_MAX_ELEMENTS) {
        		// log "too many iterations"
        		//System.out.println("Out of Control!");
        		break;
        	}
        }
		
        return 0;
	}
	
	// output
	// open file for output, renderPuzzle(), write String to output file
	public int output(String filename, Boolean append) throws IOException {
		
		try (PrintWriter out = new PrintWriter(new FileWriter(filename, append))) {
			String puzzleRenderedText = this.renderPuzzle();
			out.write(puzzleRenderedText);
		}
		catch (IOException ioe) {
			// log ioe
			throw ioe;
		}
		
		return 0;
	}

	/**
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public int output(String filename) throws IOException {
		
		return this.output(filename, Boolean.FALSE);
	}
}
