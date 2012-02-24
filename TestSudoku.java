package sudoku;

public class TestSudoku {

	public static void main(String[] args)
	{
		Sudoku puzzleEnviron = new Sudoku( "puzz.txt", "grid.txt" ); // eventually get
		puzzleEnviron.run( );
	}

}
