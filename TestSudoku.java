package sudoku;

public class TestSudoku {

	public static void main( String[] args )
	{
		Sudoku puzzleEnviron = new Sudoku( "puzz.txt" );
		//testPreviousCell( puzzleEnviron );
		puzzleEnviron.run( );
	}
	
	static void testNextCell( Sudoku engine ) // confirmed 12 3 3
	{
		System.out.println( "test of nextCell" );
		// see if you can go from 00 to 88
		int xx = 0;
		int yy = 0;
		System.out.printf( "(%d,%d) ", xx, yy );
		int temp = 0;
		while ( temp < 808 )
		{
			temp = engine.nextCell( xx, yy );
			xx = engine.xOfNew( temp );
			yy = engine.yOfNew( temp );
			if ( xx + 1 % 3 == 0 )
				System.out.print( "\t" );
			else if ( xx % 8 == 0 )
				System.out.println( );
			System.out.printf( "(%d,%d) ", xx, yy );
		}
		
	}
	
	static void testPreviousCell( Sudoku engine ) // fix 12 3 3
	{
		System.out.println( "test of nextCell" );
		// see if you can go from 0,0 to 8,8
		int xx = 8;
		int yy = 8;
		System.out.printf( "(%d,%d) ", xx, yy );
		int temp = 808;
		while ( temp > 0 )
		{
			temp = engine.previousCell( xx, yy );
			xx = engine.xOfNew( temp );
			yy = engine.yOfNew( temp );
			if ( xx + 1 % 3 == 0 )
				System.out.print( "\t" );
			else if ( xx % 8 == 0 )
				System.out.println( );
			System.out.printf( "(%d,%d) ", xx, yy );
		}
	}

}
