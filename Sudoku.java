package sudoku;

public class Sudoku
{

	private int valLIMIT = 9;
	private int temp;
	private int nX;
	private int nY;
	private int guess[ ][ ];
	
	public Sudoku( String clueFile, String gridType )
	{
		guess = new int[ valLIMIT ][ valLIMIT ];
		// open clueFile try catch
		// open gridType
	}

	public void run( )
	{
		if ( solve( 0, 0 ) )
			System.out.print( "solved it" );
		else 
			System.out.print( "didn't solve it" );
	}
	
	/*
	 * Expecting guess has only 0s & clues above 20
	 * Time/Space, time bartered for now: lots of math 
	 */
	boolean solve( int x, int y )
	{
		if ( clueCell( x, y ) )
		{
			if ( thisNotLastCell( x, y ) )
			{
				temp = nextCell( x, y );
				nX = xOfNew( temp ); // this is what I wanted to avoid with python
				nY = yOfNew( temp ); // should have been 14 lines
				solve( nX, nY ); // IE nextCell
			}
			else
				return true;
		}
		else if ( guess[ x ][ y ] < valLIMIT )
		{
			guess[ x ][ y ] += 1;
			if ( conflicts( x, y ) )
				solve( x, y ); // try next value
			else // WORKED
				if ( thisNotLastCell( x, y ) )
				{
					temp = nextCell( x, y );
					nX = xOfNew( temp );
					nY = yOfNew( temp );
					solve( nX, nY ); // IE nextCell
				}
				else
					return true;
		}
		else // exhausted possibilities in this cell
			if ( thisNotFirstCell( x, y ) )
			{
				guess[ x ][ y ] = 0; // reset
				temp = previousCell( x, y );
				nX = xOfNew( temp ); //
				nY = yOfNew( temp );
				solve( nX, nY ); // IE previousCell
			}
			else
				return false;
		return false; // assert unreachable, only for eclipse's benefit
	}
	
	int nextCell( int x, int y )
	{
		// here's something I'll miss from python, multiple return values
		// for the moment, I will cludge up the same by embedding them as digits
		return 1010;
	}
	
	int xOfNew( int unsplit )
	{
		return unsplit / 100;
	}
	
	int yOfNew( int unsplit )
	{
		return unsplit % 100;
	}
	
	int previousCell( int x, int y )
	{
		// here's something I'll miss from python, multiple return values
		// for the moment, I will cludge up the same by embedding them as digits
		return 1010;
	}
	
	boolean thisNotLastCell( int x, int y )
	{
		return x < valLIMIT || y < valLIMIT;
	}
	
	boolean thisNotFirstCell( int x, int y )
	{
		return x > 0 || y > 0;
	}
	
	boolean clueCell( int x, int y )
	{
		return false;
	}
	
	boolean conflicts( int x, int y )
	{
		return true;
	}
}