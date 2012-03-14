package sudoku;

public class TestSudoku {

	public static void main( String[] args )
	{
		Sudoku puzzleEnviron = new Sudoku( "puzz.txt" );
		puzzleEnviron.run( );
		//testNextCell( puzzleEnviron );
		//testPreviousCell( puzzleEnviron );
		//testUpperLeftCorner( puzzleEnviron );
	}
	
	static void testNextCell( Sudoku engine ) // confirmed 12 3 14
	{
		System.out.println( "test of nextCell" );
		// see if you can go from 00 to 88
		int xx = 0;
		int yy = 0;
		System.out.printf( "(%d,%d) ", xx, yy );
		while ( xx < 8 && yy <= 8 )
		{
			engine.nextCell( xx, yy );
			xx = engine.getNx( );
			yy = engine.getNy( );
			if ( ( xx + 1 ) % 3 == 0 )
				System.out.print( "\t" );
			else if ( xx % 8 == 0 )
				System.out.println( );
			System.out.printf( "(%d,%d) ", xx, yy );
		}
		
	}
	
	static void testPreviousCell( Sudoku engine ) // maybe 12 3 14
	{
		System.out.println( "test of nextCell" );
		// see if you can go from 0,0 to 8,8
		int xx = 8;
		int yy = 8;
		System.out.printf( "(%d,%d) ", xx, yy );
		while ( xx > 0 && yy >= 0 )
		{
			engine.previousCell( xx, yy );
			xx = engine.getNx( );
			yy = engine.getNy( );
			if ( ( xx + 1 ) % 3 == 0 )
				System.out.print( "\t" );
			else if ( xx % 8 == 0 )
				System.out.println( );
			System.out.printf( "(%d,%d) ", xx, yy );
		}
	}

	static void testUpperLeftCorner( Sudoku eng ) // mostly 12 3 14
	{
		System.out.println( "test of upperLeftCorner" );
		int xx = 0;
		int yy = 0;
		eng.upperLeftCorner(xx, yy);
		System.out.printf( "(%d,%d) ", eng.getNx(), eng.getNy() );
		while ( xx < 8 && yy <= 8 ) // I should make this less restrictive so it tests the whole grid
		{
			eng.nextCell( xx, yy );
			xx = eng.getNx();
			yy = eng.getNy();
			eng.upperLeftCorner(xx, yy);
			System.out.printf( "(%d,%d) ", eng.getNx(), eng.getNy() );
		}
	}
}
