package sudoku;
import java.util.Scanner;

public class TestSudoku {

	public static void main( String[] args )
	{
		Sudoku puzzleEnviron = new Sudoku( "puzz.txt" );
		puzzleEnviron.run( );
		//testNextCell( puzzleEnviron );
		//testPreviousCell( puzzleEnviron );
		//testUpperLeftCorner( puzzleEnviron );
		//handTestUpperLeftCorner( puzzleEnviron );
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
		// see if you can go from 0,0 to 8,8; when 9x9
		int xx = 3;
		int yy = 3;
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
	
	static void handTestUpperLeftCorner( Sudoku eng )
	{
		Scanner input = new Scanner( System.in );
		boolean lim = true;
		while ( lim )
		{
			System.out.print( "xx -- " );
			int xx = input.nextInt();
			System.out.print( "yy -- " );
			int yy = input.nextInt();
			eng.upperLeftCorner( xx, yy );
			System.out.printf( "uL ( %d, %d )\n", eng.getNx(), eng.getNy() );
			System.out.print( "0 to quit -- " );
			lim = ( input.nextInt( ) > 0 );
		}
	}

	static void testUpperLeftCorner( Sudoku eng ) // mostly 12 3 14
	{
		System.out.println( "test of upperLeftCorner" );
		for ( int row = 0; row <= 3; row++ )
		{
			for ( int cell = 0; cell <= 3; cell++ )
			{
				eng.upperLeftCorner( row, cell );
				System.out.printf( "(%d,%d) ", eng.getNx(), eng.getNy() );
				if ( cell == 1 )
					System.out.print( "- " );
			}
			System.out.println( );
		}
	}
}
