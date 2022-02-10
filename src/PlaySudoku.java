import java.io.FileNotFoundException;

public class PlaySudoku {
    public static void main(String[] args) throws FileNotFoundException {
        MySudokuBoard newBoard = new MySudokuBoard("boards/data1.sdk");
        System.out.println(newBoard);
    }
}
/*
PROGRAM OUTPUT:
          9 x 9 Sudoku Board
`~,`~,`~,`~,`~,`~,`~,`~,`~,`~,`~,`~,`~,
`~,[2][=][=]`~,[1][=][5]`~,[=][=][3]`~,
`~,[=][5][4]`~,[=][=][=]`~,[7][1][=]`~,
`~,[=][1][=]`~,[2][=][3]`~,[=][8][=]`~,
`~,`~,`~,`~,`~,`~,`~,`~,`~,`~,`~,`~,`~,
`~,[6][=][2]`~,[8][=][7]`~,[3][=][4]`~,
`~,[=][=][=]`~,[=][=][=]`~,[=][=][=]`~,
`~,[1][=][5]`~,[3][=][9]`~,[8][=][6]`~,
`~,`~,`~,`~,`~,`~,`~,`~,`~,`~,`~,`~,`~,
`~,[=][2][=]`~,[7][=][1]`~,[=][6][=]`~,
`~,[=][8][1]`~,[=][=][=]`~,[2][4][=]`~,
`~,[7][=][=]`~,[4][=][2]`~,[=][=][1]`~,
`~,`~,`~,`~,`~,`~,`~,`~,`~,`~,`~,`~,`~,
 */
