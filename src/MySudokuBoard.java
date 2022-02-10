

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.lang.Math.sqrt;

/**
 * @author Austin Roach
 * CS143
 * Thu, Feb 3, 2022
 *
 * Sudoku: Week 1 of 3
 *
 * This program reads data from a text file representing a Sudoku game and formats the data into a more ergonomic reading
 * experience for humans.
 *
 * Game logic and solution bot to come.
 *
 * Core Topics: Collections, Set/HashSet, Map/HashMap, Multidimensional Arrays, File Processing, UX/Accessibility
 */

public class MySudokuBoard {
    //2D Array [row][column] of cells on our Sudoku Board
    private char[][] CELLS;
    //Number of cells along the edges of our board
    private final int SIDE_LENGTH;
    //Sidelength of our sub squares, number of sub squares per side, fourth root of the total number of cells on our board
    private final int ROOT;

    //Character used to indicate unsolved cells. Completely cosmetic, change it as you wish. Does not affect game logic.
    private final char NIL_CHAR = '0';

    //Constructor method. Takes filePath String parameter and reads file into board.

    public MySudokuBoard(String filePath) throws FileNotFoundException {
        String board = getString(filePath);
        //A sudoku board has side lengths equal to the square root of the total number of cells

        //Sidelength of a sub square. The fourth root of the total number of squares.

        //2D array of cells. Indices are row and column respectively. Because a sudoku game must be "super-square",
        //the same size parameter may be passed to both fields on construction.

            SIDE_LENGTH = (int) sqrt(board.length());
            CELLS = populateMatrix(board);
            ROOT = (int) sqrt(SIDE_LENGTH);



        //Create new set of possible numbers for our Sudoku game - equal to ROOT^2, sqrt(total number of cells), or SIDE_LENGTH
        //Currently, this is being used to sort "answered" cells from null cells. It will likely be useful later on when
        //implementing game logic and testing for valid boards


    }

    private char[][] populateMatrix(String board) {
        //Set used because each number is unique
        Set<Character> numSet = new HashSet<>();
        char[][] CELLS = new char[SIDE_LENGTH][SIDE_LENGTH];

        //We want the set of integers starting at 1 up to and including SIDE_LENGTH.
        for (int i = SIDE_LENGTH; i > 0; i--) {
            numSet.add((char) (i));
        }

        //Copy values from board String to CELLS 2D char array

        //i is our "row" iterator
        for (int i = 0; i < SIDE_LENGTH; i++) {
            //j is our "column" iterator
            for (int j = 0; j < SIDE_LENGTH; j++) {
                //Our board String is a one-dimensional array of char. Using the iterators from our nested for loop
                //we can organize this data into our two-dimensional array. Each "column" (j) increment of our 2D array
                //represents one index increment in our board array of char. Once we've advanced SIDE_LENGTH "columns",
                //we advance to the next row.

                //This logic is achieved by the following: 2dArray[row][column] = 1dArray[(row * COLUMN_MAX_VALUE) + column]
                char cell = board.charAt( (i * SIDE_LENGTH) + j );
                if (numSet.contains(cell)) {
                    CELLS[i][j] = cell;
                } else {
                    CELLS[i][j] = NIL_CHAR;
                }

                //If the newly added character is not  contained in our number set (i.e. 0, !, *, c, etcetera), we will
                //replace it with our *cosmetic* NIL_CHAR

            }

        }
        return CELLS;
    }

    @NotNull
    private String getString(String filePath) throws FileNotFoundException {
        //New Scanner object to read our text file
        Scanner file = new Scanner(new File(filePath));
        //Empty String to store lines from our file
        String string = "";
        //store all lines from file into board String
        while (file.hasNextLine()) {
            string += file.nextLine();
        }
        return string;
    }


    public String toString() {
        //declare empty String for the (ROOT + 1) barriers between our ROOT number of sub squares.
        //Assignment is dependent on the calculation of our lineLength value
        String subRowWall = "";

        //Basic pattern tiled to populate the space of our sub square grid
        String wallTile = "`~,";

        //lhs and rhs formatting partitions to visually encapsulate cells
        String lhsCellWall = "[";
        String rhsCellWall = "]";
        //Calculate total number of characters in one line of formatted toString output. Later used to populate the
        //subRowWall with the correct number of wallTile characters
        int lineLength = ( SIDE_LENGTH * cellToString('1', lhsCellWall, rhsCellWall).length() ) +
                ( (ROOT + 1) * wallTile.length() );

        //While the subRowWall has fewer characters than the "lineLength" of a formatted line, add characters from the
        //array of characters in the wallTile String
        while (subRowWall.length() < lineLength) {
            for (int i = 0; i < wallTile.length() ; i++) {
                //Use of the same condition for my if and while loops feels messy/un-D.R.Y. Is there a better way?
                if (subRowWall.length() < lineLength) {
                    subRowWall += wallTile.charAt(i);
                }
            }
        }

        //Write board header
        String header = (SIDE_LENGTH +" x " + SIDE_LENGTH + " Sudoku Board");
        //Center our board header
        String ret = formatCenter(header, lineLength) + "\n";

        for (int i = 0; i < SIDE_LENGTH; i++) {
            //outer for loop iterates our "row" value

            int row = i;

            //We want to create a partition across an entire row every three rows starting at row 0. This is achieved
            //through use of the modulus operand. Let's say that ROOT = 3. Our first row is index 0. 0 % 3 (or any number
            //for that matter) is equal to zero, thus satisfying our if condition and adding the subRowWall to our String.

            //The next row is at index 1. 1 % 3 is not equal to 0, so the condition does not execute. Our next index, 2,
            //also does not satisfy the condition, as 2 % 3 != 0.

            //It isn't until we reach index 3 (our fourth index) that we again satisfy the condition and add another
            //subRowWall to our String
            if (row % ROOT == 0) {
                ret += subRowWall;
                ret+='\n';
            }

            //Once we've checked to see whether we need to add a subRowWall (and done so if needed), it is time
            //to add our formatted cells and wallTiles (you might call them "subColumnWalls"
            for (int j = 0; j < SIDE_LENGTH; j++) {
                //Similar logic to the above loop - every ROOT indices, place a wallTile between cells to indicate
                //separation between sub squares.

                //Short and simple, but slightly redundant. Unsure whether it makes more sense to extract as a method,
                //or leave hardcoded as is
                if (j % ROOT == 0) {
                    ret += wallTile;
                }
                //After (maybe) adding a wallTile, cell is added to the String
                //cellToString() is called on cell to add cellWalls for improved legibility
                int col = j;
                ret += cellToString(CELLS[row][col], lhsCellWall, rhsCellWall);
            }
            //Once j, or col have reached lineLength, we have completed our current row and exited our inner for loop
            //The last step is to append one more piece of wall tile to the end - a "capstone" if you will - and finish
            //the line off with a linebreak
            ret += wallTile;
            ret += '\n';
        }
        //Now that we have exited our outer for loop, we are done reading data from our array.
        //The final step is to add one more "capstone" a subRowWall to seal the bottom of our grid.
        ret += subRowWall;

        //Signed, sealed, returned :)
        return ret;
    }

    //Helper method for centering the board title.
    //Accepts a String to center, and a lineLength in which to center the String
    //Adds whitespace margins on either side equivalent to half the difference between the lineLength and the String
    //Returns new centered string

    //If String is larger than lineLength, the method does nothing, simply returning the original String

    private String formatCenter(String s, int lineLength) {
        int diff = lineLength - s.length();
        if (diff > 0) {

            for (int i = 0; i < (diff / 2); i++) {
                s = " " + s + " ";
            }
        }
        return s;
    }

    //toString for formatting individual cells.

    //Original implementation with default parameters for lhs "[" and rhs "]"
    private String cellToString(char cell) {
        return cellToString(cell, "[", "]");
    }

    //Overloaded implementation offering custom parameters for lhs and rhs
    private String cellToString(char cell, String lhs, String rhs) {
        return lhs + cell + rhs;
    }

    public boolean isValidStringBoard(String board) {
        if ((ROOT^2) == (SIDE_LENGTH) && ((SIDE_LENGTH^2) == board.length())) {
            return true;
        }
        return false;
    }

    public boolean isValid() {
        for (int i = 0; i < SIDE_LENGTH; i++) {
            if (!isValidRow(i) || !isValidColumn(i) || !isValidSubSquare(i)) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidRow(int rowIndex) {
        char[] row = new char[SIDE_LENGTH];
        for (int i = 0; i < row.length; i++) {
            row[i] = CELLS[rowIndex][i];
        }
        return validSet(row);
    }

    private boolean isValidColumn(int columnIndex) {
        char[] column = new char[SIDE_LENGTH];
        for (int i = 0; i < column.length; i++) {
            column[i] = CELLS[i][columnIndex];
        }
        return validSet(column);
    }

    private boolean isValidSubSquare(int subSquareIndex) {
//        char[] subSquare = new char[SIDE_LENGTH];
//        int startRow = (subSquareIndex -  ((subSquareIndex % ROOT) / ROOT));
//        int startCol = ((subSquareIndex % ROOT) / ROOT );
//        for (int i = startRow; i < (startRow + ROOT); i++) {
//            for (int j = startCol; j < (startCol + ROOT); j++) {
//                subSquare[( ( i  * ROOT) + j)  ] = CELLS[i][j];
//            }
//        }
//        return validSet(subSquare);
        return true;
    }

    private boolean validSet(char[] array) {
        Set<Character> controlSet = new HashSet<>();
        Set<Character> testSet = new HashSet<>();
        for (int i = 0; i < array.length; i++) {
            controlSet.add((char) (i+1));
        }
        for (int i = 0; i < array.length; i++) {
            if (controlSet.contains(array[i])){
                if (testSet.contains(array[i])) {
                    return false;
                }
            }
            testSet.add(array[i]);
        }
        return true;
    }

    public boolean isSolved() {


        for (int i = 0; i < SIDE_LENGTH; i++) {
            for (int j = 0; j < SIDE_LENGTH; j++) {
                if (CELLS[i][j] == NIL_CHAR) {
                    return false;
                }
            }
        }
        return isValid();
    }


}
