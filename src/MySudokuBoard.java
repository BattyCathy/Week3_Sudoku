
/**
 * @author Austin Roach
 * CS143
 * Thu, Feb 3, 2021
 *
 * Sudoku: Week 2 of 3
 *
 * This program reads data from a text file representing a Sudoku game and formats the data into a more ergonomic reading
 * experience for humans.
 *
 * The game is analyzed to determine whether it isValid, and if so, whether it isSolved
 *
 * Solution bot to come.
 *
 * Core Topics: Collections, Set/HashSet, Map/HashMap, Multidimensional Arrays, File Processing, UX/Accessibility
 */

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.lang.Math.sqrt;

public class MySudokuBoard {
    //2D Array representing our Sudoku board
    private final char[][] CELLS;
    //Side length of our Sudoku board. Square root of total number of cells. A traditional 9 x 9 Sudoku board has 81
    //cells. sqrt(81) = 9, or the SIDE_LENGTH of a 9 x 9 Sudoku Board
    private int SIDE_LENGTH;
    //Root dimension of our board. Side length of our sub-squares. Square root of SIDE_LENGTH.
    private int ROOT;
    //String used to read and analyze data from our text file
    private String boardString;

    //Set of possible answers for our board - populated with values [1...SIDE_LENGTH]
    private Set<String> answerSet = new HashSet<>();
    //Set of characters present on the board. Used for validation and filtering junk data
    private Set<String> boardChars = new HashSet<>();


    //Constructor
    public MySudokuBoard(String filePath) throws FileNotFoundException {
        //A bit of confusion here - I originally wrote populateMatrix with a void return, but found that it was not
        //actually assigning values to CELLS matrix. All other variables could be assigned without a return. Changing
        //populateMatrix to return char[][], and using it to assign CELLS worked, but I don't know exactly why - this
        //solution was the product of messy trial and error.
        CELLS = populateMatrix(filePath);
    }



    //Majority of our Constructor is contained in this method. Accepts String filePath parameter, reads to String
    //boardString, and assigns characters to their respective indices in CELLS
    private char[][] populateMatrix(String filePath) throws FileNotFoundException {
        //Read file to String
        boardString = getString(filePath);
        //Now that we have a String, we can count the letters. The square root of the number of letters in boardString is
        //SIDE_LENGTH
        SIDE_LENGTH = (int) sqrt(boardString.length());
        //We can then take the square root of SIDE_LENGTH to calculate ROOT
        ROOT = (int) sqrt(SIDE_LENGTH);

        //Declare new 2d array of char with dimensions [SIDE_LENGTH][SIDE_LENGTH]
        char[][] CELLS = new char[SIDE_LENGTH][SIDE_LENGTH];

        //Populate answer set with values starting at 1, up to and including SIDE_LENGTH
        for (int i = 1; i <= SIDE_LENGTH; i++) {
            answerSet.add(String.valueOf(i));
        }


        //Now that we've assigned and declared necessary variables, we are ready to copy values from our boardString
        //to our CELLS array

        //Outer for loop iterates row value
        for (int row = 0; row < SIDE_LENGTH; row++) {
            //Inner for loop iterates column value
            for (int column = 0; column < SIDE_LENGTH; column++) {
                //This is where we actually assign values from boardString to CELLS
                //Take note of the parameter used in the charAt method - this is a generically useful formula in
                //moving between different numerical bases and/or numbers of dimensions
                CELLS[row][column] = boardString.charAt( (row * SIDE_LENGTH) + column );
                //Here we check to see if we've seen this character before. If not, we add it to the set of characters
                //present on our board
                if (! boardChars.contains(String.valueOf(CELLS[row][column]))) {
                    boardChars.add(String.valueOf(CELLS[row][column]));
                }
            }

        }
        return CELLS;
    }

    //Helper method for reading from a text file and returning text as a String
    @NotNull
    private String getString(String filePath) throws FileNotFoundException {
        Scanner file = new Scanner(new File(filePath));
        //Empty String to store lines from our file
        String board = "";
        //store all lines from file into board String
        while (file.hasNextLine()) {
            board += file.nextLine();
        }
        return board;
    }



    //Parent validation method
    public boolean isValid() {


        if (
                //Condition to check that the number of different characters on the board does not exceed the number of
                //possible answers plus whatever character we are using to represent unsolved cells
                boardChars.size() > answerSet.size() + 1 ||
                        //Because we cast these values from doubles to ints, if our board has an invalid number of
                        // characters, the lossy data conversion from double to int should make one or both of these
                        // mathematical statements incorrect
                        (ROOT*ROOT) != SIDE_LENGTH ||
                (SIDE_LENGTH*SIDE_LENGTH) != boardString.length()
        ) {
            return false;
        }

        //Validate rows, columns, subSquares SIDE_LENGTH number of times
        for (int i = 0; i < SIDE_LENGTH; i++) {
            if (!isValidRow(i) || !isValidColumn(i) || !isValidSubSquare(i)) {
                return false;
            }
        }

        return true;
    }

    //row, column, and subsquare validation methods all primarily house logic for reading a given form from our 2d array
    //into a 1d array, which may then be passed to a generic validateSet method

    //Row validation algorithm
    private boolean isValidRow(int rowIndex) {
        //Thanks again to Prof. Piper for showing me that an entire row can be represented by omitting column index
        char[] rowToTest = CELLS[rowIndex];
        return validSet(rowToTest);
    }

    //Column validation algorithm
    private boolean isValidColumn(int columnIndex) {
        //Very similar to original implementation of above method. The difference brought up some questions for me
        //regarding the structure of multidimensional arrays - are they hierarchical? Is CELLS[row][column] really an
        //array of arrays, with row being a parent array of column array children? Does this inform the way people decide
        // to pivot their data tables?
        char[] column = new char[SIDE_LENGTH];
        for (int i = 0; i < column.length; i++) {
            column[i] = CELLS[i][columnIndex];
        }
        return validSet(column);
    }

    //subSquare validation method
    private boolean isValidSubSquare(int subSquareIndex) {
        char[] subSquare = new char[SIDE_LENGTH];
        int index = 0;
        //subSquare are the form where ROOT is most useful
        //Outer for loop iterates row value. Each subSquare has ROOT rows, starting at 0 incrementing ROOT times.
        for (int i = 0; i < ROOT; i++) {

            //Starting row increases by ROOT every ROOT subSquare index
            int row = subSquareIndex - (subSquareIndex % ROOT) + i;
            //Inner for loop iterates column value. Same basic logic as outer for
            for (int j = 0; j < ROOT; j++) {
                //Starting column increases by ROOT at every subSquare index.
                //Once every ROOT times, column is reset to 0
                int column = (subSquareIndex % ROOT) * ROOT + j;
                subSquare[index] = CELLS[row][column];
                index++;
            }
        }
        return validSet(subSquare);

    }

    //Base validation logic for a 1d array - columns, rows, and subSquares all follow the same rules when interpreted as
    //a 1d array
    private boolean validSet(char[] array) {

        //Create new empty set to test our input array
        Set<String> testSet = new HashSet<>();

        //for each cell in our array
        for (char cell : array) {
            //if the cell is contained in our answer set. This check is used to overlook rulebreaking duplicates in
            //unsolved cell symbols.
            if (answerSet.contains(String.valueOf(cell))) {
                //check to see if we've already added it to our test set
                if (testSet.contains(String.valueOf(cell))) {
                    //if we've seen it before, this array is not valid
                    return false;
                }
            }
            //After verifying that cell is indeed a unique value, we add it to our testSet and run the test again.
            testSet.add(String.valueOf(cell));
        }
        return true;
    }

    //check to see if the board isSolved
    public boolean isSolved() {

        //Declare an empty Map containing answer characters and their integer count
        Map<Character, Integer> map = new HashMap<>(SIDE_LENGTH);

        //I've done this before. Should probably extract to a method.
        for (int row = 0; row < SIDE_LENGTH; row++) {
            for (int column = 0; column < SIDE_LENGTH; column++) {
                //if the map already contains the character key, increment the int value by one
                if (map.containsKey(CELLS[row][column])) {

                    map.put(CELLS[row][column], (map.get(CELLS[row][column]) + 1));
                } else {
                    //if the key is new, add it with a value of one
                    map.put(CELLS[row][column], 1);
                }

            }
        }
        //Once we've built our map, check to see that each value in our map is equal to the number of possible answers.
        for (int val:
                map.values()) {
            if (val != answerSet.size()) {
                return false;
            }
        }
        //Once we find that all answers have been entered the proper number of times, we should double check to see that
        //answers are indeed valid.
        return isValid();
    }



    //toString methods, one overloaded for custom toString parameters

    public String toString() {
        return toString("","`~,","[","]", "=");
    }
    public String toString(String subRowWall, String wallTile, String lhsCellWall, String rhsCellWall, String NIL_CHAR ) {
        int lineLength = ( SIDE_LENGTH * cellToString("1", lhsCellWall, rhsCellWall).length() ) +
                ( (ROOT + 1) * wallTile.length() );

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

        for (int row = 0; row < SIDE_LENGTH; row++) {
            //outer for loop iterates our "row" value


            if (row % ROOT == 0) {
                ret += subRowWall;
                ret+='\n';
            }

            //Once we've checked to see whether we need to add a subRowWall (and done so if needed), it is time
            //to add our formatted cells and wallTiles (you might call them "subColumnWalls"
            for (int column = 0; column < SIDE_LENGTH; column++) {
                //Similar logic to the above loop - every ROOT indices, place a wallTile between cells to indicate
                //separation between sub squares.

                //Short and simple, but slightly redundant. Unsure whether it makes more sense to extract as a method,
                //or leave hardcoded as is
                if (column % ROOT == 0) {
                    ret += wallTile;
                }
                //After (maybe) adding a wallTile, cell is added to the String
                //cellToString() is called on cell to add cellWalls for improved legibility
                String cell = String.valueOf(CELLS[row][column]);
                if (!answerSet.contains(cell)) {
                    cell = NIL_CHAR;
                }
                ret += cellToString(cell, lhsCellWall, rhsCellWall);
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



    //toString helpers
    public String formatCenter(String s, int lineLength) {
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
    public String cellToString(String cell) {
        return cellToString(cell,"[","]");
    }

    //Overloaded implementation offering custom parameters for lhs and rhs
    public String cellToString(String cell, String lhs, String rhs) {
        return lhs + cell + rhs;
    }


}
