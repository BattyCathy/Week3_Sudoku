import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SudokuBoard {
    int SEED_DIMENSION = 3;
    private int SIZE = (SEED_DIMENSION*SEED_DIMENSION);
    char[][] cells;

    public SudokuBoard(String filePath) throws FileNotFoundException {
        cells = new char[SIZE][SIZE];
        Scanner file = new Scanner(new File(filePath));
        int lineNum = 0;

        while(file.hasNextLine()) {
            String line = file.nextLine();
            for (int i = 0; i < SIZE; i++) {
                cells[lineNum][i] = line.charAt(i);
            }
            lineNum++;
        }
    }

    public String toString() {
        String ret = "Sudoku Board: \n";
        int lineChars = (3*(SEED_DIMENSION + 1)) + (3*SIZE);
        for (int i = 0; i < SIZE; i++) {

            for (int j = 0; j < lineChars; j++) {
                if (i % SEED_DIMENSION == 0) {
                    ret += "W";
                } else {
                    ret += "=";
                }
            }
            ret += '\n';

            for (int j = 0; j < SIZE; j++) {
                if (j % SEED_DIMENSION == 0) {
                    ret += "WWW";

                }
                ret += "(" + cells[i][j] + ")";
            }
            ret += "WWW" + '\n';

        }
        for (int i = 0; i < lineChars; i++) {
            ret += "W";
        }


        return ret;
    }

}