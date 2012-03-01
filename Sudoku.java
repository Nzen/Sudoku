//package sudoku;
//import java.lang.String;

public class Sudoku
{
	private int valLIMIT; // a single limit implies a square puzzle
	private const int clueOFFSET = 20; // also, ALLCAPS is ugly, I'm trying this out
	private const int newValReturnOFFSET = 100;
	private int sqXLIMIT;
	private int sqYLIMIT; // used in squareConflict( ), assumes rectangular
	private int temp;
	private int nX; // vars for next or previous cell; changed by solve( )
	private int nY;
	private int guess[ ][ ];
	
	public Sudoku( String clueFile ) // ready 12 2 29
	{
		File4Stream clues = new File4Stream( );
		clues.open( File4Stream );
		valLIMIT = clues.readInt( );
		sqXLIMIT = clues.readInt( );
		sqYLIMIT = clues.readInt( );
		// fill puzzle
		guess = new int[ valLIMIT ][ valLIMIT ];
		for ( int row = 0; row < valLIMIT; row++ )
			for ( int cell = 0; cell < valLIMIT; cell++ )
				inputClues( row, cell, clues.readInt( ) );
		clues.close( );
	}

	void inputClues( int row, int cell, int val ) // ready 12 2 29
	{
		guess[ row ][ column ] = ( val > 0 ) ? val + clueOFFSET : val; // 0 OR clue + offset
	}

	public void run( ) // ready 12 2 29
	{	// change to display & interactive mode when solve works
		if ( solve( 0, 0 ) )
			System.out.print( "solved it" );
			// display or export guess[][]
		else 
			System.out.print( "didn't solve it" );
	}
	
	/*
	 * Expecting guess has only 0s & clues above clueOFFSET
	 * Time/Space, time bartered for now: lots of math 
	 */
	boolean solve( int x, int y ) // ready 12 2 29
	{
		if ( clueCell( x, y ) )
			if ( thisNotLastCell( x, y ) )
			{	// move to next
				temp = nextCell( x, y );
				nX = xOfNew( temp ); // this is what I wanted to avoid with python
				nY = yOfNew( temp ); // solve(x,y) should have been 15 lines
				solve( nX, nY ); // IE nextCell
			}
			else
				return true; // finished successfully
		else
		{		// cell open for guessing
			guess[ x ][ y ] += 1;
			if ( guess[ x ][ y ] <= valLIMIT ) // time to test if this guess works
				if ( conflicts( x, y ) )
					solve( x, y ); // guess a higher number
				else // guess WORKED, no conflict
					if ( thisNotLastCell( x, y ) )
					{ // solve next
						temp = nextCell( x, y );
						nX = xOfNew( temp );
						nY = yOfNew( temp );
						solve( nX, nY ); // IE nextCell
					}
					else
						return true;
			else // exhausted possibilities in this cell
				if ( thisNotFirstCell( x, y ) )
				{
					guess[ x ][ y ] = 0; // reset this cell
					temp = previousCell( x, y );
					nX = xOfNew( temp ); //
					nY = yOfNew( temp );
					solve( nX, nY ); // IE previousCell
				}
				else // backtracked to start
				{
					System.out.println( "No solution possible, check if clues entered correctly" );
					return false; // everything tested, nothing worked
				}
		}
		return false; // assert unreachable, only for eclipse's benefit
	}
	
	int nextCell( int x, int y ) // ready 12 2 29
	{
		// here's something I'll miss from python: multiple return values
		// for the moment, I will kludge up the same by embedding them as digits

		if ( x < valLIMIT )
			return ( x + 1 ) * newValReturnOFFSET + y; // to be broken for parts later
		else
			if ( y < valLIMIT )
				return y + 1; // x is 0
			else // unreachable in normal operation
				return 0;
	}
	
	int xOfNew( int unsplit ) // ready 12 2 29
	{
		return unsplit / newValReturnOFFSET;
	}
	
	int yOfNew( int unsplit ) // ready 12 2 29
	{
		return unsplit % newValReturnOFFSET;
	}
	
	int previousCell( int x, int y ) // ready 12 2 29
	{
		// for the moment, I will cludge up the same by embedding them as digits

		if ( x > 0 )
			return ( x - 1 ) * newValReturnOFFSET + y; // to be broken for parts later
		else
			if ( y > 0 )
				return valLIMIT * newValReturnOFFSET + y - 1; // x wraps back to valLimit
			else // unreachable in normal operation
				return ( valLIMIT - 1 ) * newValReturnOFFSET + valLIMIT - 1;
	}
	
	boolean thisNotLastCell( int x, int y ) // ready 12 2 29
	{
		return x < valLIMIT - 1 || y < valLIMIT - 1;
	}
	
	boolean thisNotFirstCell( int x, int y ) // ready 12 2 29
	{
		return x > 0 || y > 0;
	}
	
	boolean clueCell( int x, int y ) // ready 12 2 29
	{
		return guess[ x ][ y ] > clueOFFSET;
	}
	
	boolean conflicts( int x, int y ) // resolve stubs 12 2 29
	{
		boolean conflictFound = true;
		//
		if ( aRowConflict( x, y ) )
			return conflictFound;
		else if ( aVerticalConflict( x, y ) )
			return conflictFound;
		else if ( aSquareConflict( x, y ) )
			return conflictFound;
		else
			return !conflictFound; // success
	}
	
	boolean aRowConflict( int focusX, int focusY ) // stub 12 2 29
	{
		// for row
				//if r, c == foX & foY
			//		continue
		//		else
	//				if val = candidate
//						return conflictFound
		return true;
	}
	
	boolean aVerticalConflict( int focusX, int focusY ) // stub 12 2 29
	{
		return true;
	}
	
	boolean aSquareConflict( int focusX, int focusY ) // stub 12 2 29
	{
		/*
		get to upper left corner
		for rows < sqLim + offset
			for chars < sqLim + offset
				if r, c == foX & foY
					continue
				else
					if val = candidate
						return conflictFound;
		return ! conflictFound; // success
		*/
		return true;
	}
}
