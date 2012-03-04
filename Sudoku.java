package sudoku;
//import java.lang.String;

public class Sudoku
{
	private int valLIMIT; // a single limit implies a square puzzle
	private int clueOFFSET = 20; // also, ALLCAPS is ugly, I'm trying this out
	private int newValReturnOFFSET = 100;
	private int sqXLIMIT;
	private int sqYLIMIT; // used in squareConflict( ), assumes rectangular
	private int temp;
	private int nX; // vars for next or previous cell; changed by solve( )
	private int nY;
	private int guess[ ][ ];
	private boolean finalAnswerSuccess;
	
	public Sudoku( String clueFile ) // ready 12 3 3
	{
		File4Stream clues = new File4Stream( );
		clues.open( clueFile ); // "puzz.txt" ); //
		valLIMIT = clues.readInt( );
		sqXLIMIT = clues.readInt( );
		sqYLIMIT = clues.readInt( );
		checkLimits( );
		// fill puzzle
		guess = new int[ valLIMIT ][ valLIMIT ];
		for ( int row = 0; row < valLIMIT; row++ )
			for ( int cell = 0; cell < valLIMIT; cell++ )
				inputClue( row, cell, clues.readInt( ) );
		clues.close( );
		finalAnswerSuccess = false;
	}

	void checkLimits( ) // ready 12 2 29
	{
		if ( valLIMIT != sqXLIMIT * sqYLIMIT )
		{
			System.out.print( "Invalid puzzle file, size and square limits conflict." );
			System.exit( 1 );
		}
	}

	void inputClue( int row, int cell, int val ) // ready 12 2 29
	{
		guess[ row ][ cell ] = ( val > 0 ) ? val + clueOFFSET : val; // 0 OR clue + offset
		// aren't ints initialized to 0?
	}

	public void run( ) // ready 12 3 3
	{	// change to display & interactive mode when solve works
		boolean newline = true;
		solve( 0, 0 );
		if ( finalAnswerSuccess )
		{
			System.out.print( "solved it" );
			Stream2File printPort = new Stream2File( );
			printPort.openFile( "solution.txt" );
			for( int rows[ ] : guess )
			{
				for( int guessVal : rows )
					if ( clueVal( guessVal ) )
						printPort.output( Integer.toString( guessVal - clueOFFSET ), !newline );
					else // print normally
						printPort.output( Integer.toString( guessVal ), !newline );
				printPort.output( "", newline );
			}	
			printPort.closeFile( );
		}
		else 
			System.out.print( "didn't solve it" );
	}
	
	/*
	 * Expecting guess has only 0s & clues above clueOFFSET
	 * Time/Space, time bartered for now: lots of math 
	 */
	void solve( int x, int y ) // ready 12 3 3
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
				finalAnswerSuccess = true;
		else
		{		// cell open for guessing
			guess[ x ][ y ] += 1;
			if ( guess[ x ][ y ] <= valLIMIT ) // time to test if this guess works
				if ( conflicts( x, y ) )
					solve( x, y ); // guess a higher number, or move on
				else // guess WORKED, no conflict
					if ( thisNotLastCell( x, y ) )
					{ // solve next
						temp = nextCell( x, y );
						nX = xOfNew( temp ); // or would the JVM optimize these assignments for me?
						nY = yOfNew( temp );
						solve( nX, nY ); // IE nextCell
					}
					else
						finalAnswerSuccess = true;
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
					finalAnswerSuccess = false; // everything tested, nothing worked
				}
		}
	}
	
	int nextCell( int x, int y ) // ready 12 3 3
	{
		// here's something I'll miss from python: multiple return values
		// for the moment, I will kludge up the same by embedding them as digits

		if ( x < valLIMIT - 1 )
			return ( x + 1 ) * newValReturnOFFSET + y; // to be broken for parts later
		else
			if ( y < valLIMIT - 1 )
				return y + 1; // x returns to 0
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
	
	int previousCell( int x, int y ) // ready 12 3 3
	{
		// for the moment, I will kludge up the coordinates by embedding them as digits
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
	
	boolean clueVal( int guess ) // ready 12 3 3
	{
		return guess > clueOFFSET;
	}
	
	boolean conflicts( int x, int y ) // ready 12 2 29
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
			return ! conflictFound; // success
	}
	
	boolean aRowConflict( int focusX, int focusY ) // ready 12 3 3
	{
		boolean conflictFound = true;
		int candidate = guess[ focusX ][ focusY ];
		int compare = 0; // garbage
		for ( int row = 0; row < valLIMIT; row++ )
			if ( row == focusX )
				continue;
			else
				if ( clueCell( row, focusY ) )
					compare = guess[ row ][ focusY ] - clueOFFSET;
				else
					compare = guess[ row ][ focusY ];
				if ( compare == candidate )
					return conflictFound;
		return ! conflictFound;
	}
	
	boolean aVerticalConflict( int focusX, int focusY ) // ready 12 3 3
	{
		boolean conflictFound = true;
		int candidate = guess[ focusX ][ focusY ];
		int compare = 0; // garbage
		for ( int cell = 0; cell < valLIMIT; cell++ )
			if ( cell == focusY )
				continue;
			else
				if ( clueCell( focusX, cell ) )
					compare = guess[ focusX ][ cell ] - clueOFFSET;
				else
					compare = guess[ focusX ][ cell ];
				if ( compare == candidate )
					return conflictFound;
		return ! conflictFound;
	}
	
	boolean aSquareConflict( int focusX, int focusY ) // ready 12 3 3
	{
		boolean conflictFound = true;
		int candidate = guess[ focusX ][ focusY ];
		int compare = 0;
		temp = upperLeftCorner( focusX, focusY );
		nX = xOfNew( temp );
		nY = yOfNew( temp );
		for ( int row = nX; row < sqXLIMIT + nX; row++ )
			for ( int cell = nY; cell < sqYLIMIT + nY; cell++ )
				if ( row == focusX && cell == focusY )
					continue;
				else
					if ( clueCell( row, cell ) )
						compare = guess[ row ][ cell ] - clueOFFSET;
					else
						compare = guess[ row ][ cell ];
					if ( compare == candidate )
						return conflictFound;
		return ! conflictFound; // success
	}
	
	int upperLeftCorner( int x, int y ) // ready 12 3 2
	{
		// find the x distance from nearest multiple
		for ( int multiple = 1; multiple <= sqXLIMIT; multiple++ )
		{
			if ( ( multiple * sqXLIMIT ) > x ) // 3 > 2
				nX = ( multiple - 1 ) * sqXLIMIT; // 1-1 * 3 = 0
		}
		// find the y distance from nearest multiple
		for ( int multiple = 1; multiple <= sqYLIMIT; multiple++ )
		{
			if ( ( multiple * sqYLIMIT ) > x ) // 3 > 2
				nY = ( multiple - 1 ) * sqYLIMIT; // 1-1 * 3 = 0
		}
		return nX * newValReturnOFFSET + nY;
	}
}





















