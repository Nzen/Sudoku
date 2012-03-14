package sudoku;
//import java.lang.String;

public class Sudoku
{
	private int valLIMIT; // a single limit implies a square puzzle
	private int clueOFFSET = 20; // also, ALLCAPS is ugly, I'm trying this out
	private int sqXLIMIT;
	private int sqYLIMIT; // used in squareConflict( ), assumes rectangular
	private int temp;
	private int nX; // vars for next or previous cell; changed by solve( )
	private int nY;
	private int guess[ ][ ];
	private boolean success;
	
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
		success = true;
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
	
	void outputSolution( )
	{
		System.out.print( "Check if I solved it" );
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

	public void run( ) // ready 12 3 3
	{	// change to display & interactive mode when solve works
		boolean newline = true;
		if ( solve( 0, 0 ) ) //finalAnswerSuccess )
			outputSolution( );
		else 
			System.out.print( "I didn't solve it" );
	}
	
	/**
	 * Expecting guess has only 0s & clues above clueOFFSET
	 * Time/Space, time bartered for now: lots of math 
	 **/
	boolean solve( int x, int y ) // ready 12 3 6
	{
		if ( clueCell( x, y ) )
			if ( thisNotLastCell( x, y ) )
			{	// move to next
				nextCell( x, y );
				return solve( nX, nY ); // IE nextCell
			}
			else
				return success; // last cell was a clue
		else
		{	// cell open for guessing
			guess[ x ][ y ] += 1;
			if ( guess[ x ][ y ] <= valLIMIT ) // time to test if this guess works
				if ( conflicts( x, y ) )
					return solve( x, y ); // guess a higher number, or move on
				else // guess WORKED, no conflict
					if ( thisNotLastCell( x, y ) )
					{ // solve next
						nextCell( x, y );
						return solve( nX, nY ); // IE nextCell
					}
					else
						return success; // this guess was last, nothing conflicts
			else // exhausted possibilities in this cell
				if ( thisNotFirstCell( x, y ) )
				{
					guess[ x ][ y ] = 0; // reset this cell
					previousCell( x, y );
					return solve( nX, nY ); // IE previousCell
				}
				else // backtracked to start
					return !success; // everything tested, nothing worked
		}
	}

	/**
	Calculates the new cell coordinates and stores in nX & nY for immediate use
	Takes care not to overflow to the right or bottom.
	**/
	void nextCell( int x, int y ) // ready 12 3 3
	{
		if ( x < valLIMIT - 1 )
			nX = x + 1;
			nY = y;
		else
			if ( y < valLIMIT - 1 )
				nX = 0;
				nY = y + 1;
	}

	/**
	Calculates the new cell coordinates and stores in nX & nY for immediate use
	Takes care not to overflow to the right or bottom.
	**/
	void previousCell( int x, int y ) // ready 12 3 3
	{
		if ( x > 0 )
			nX = x - 1;
			nY = y;
		else
			if ( y > 0 )
				nX = valLIMIT; // x wraps back to valLimit
				nY = y - 1;
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
			return !conflictFound; // success
	}
	
	boolean aRowConflict( int focusX, int focusY ) // ready 12 3 3
	{
		boolean conflictFound = true;
		int candidate = guess[ focusX ][ focusY ];
		int compare = 0;
		for ( int cell = 0; cell < valLIMIT; cell++ )
			if ( cell == focusY )
				continue;
			else
				if ( clueCell( cell, focusY ) )
					compare = guess[ focusX ][ cell ] - clueOFFSET;
				else
					compare = guess[ focusX ][ cell ];
				if ( compare == candidate )
					return conflictFound;
		return !conflictFound;
	}
	
	boolean aVerticalConflict( int focusX, int focusY ) // ready 12 3 3
	{
		boolean conflictFound = true;
		int candidate = guess[ focusX ][ focusY ];
		int compare = 0; // garbage
		for ( int row = 0; row < valLIMIT; row++ )
			if ( row == focusY )
				continue;
			else
				if ( clueCell( row, focusY ) )
					compare = guess[ row ][ focusY ] - clueOFFSET;
				else
					compare = guess[ row ][ focusY ];
				if ( compare == candidate )
					return conflictFound;
		return !conflictFound;
	}
	
	boolean aSquareConflict( int focusX, int focusY ) // ready 12 3 3
	{
		boolean conflictFound = true;
		int candidate = guess[ focusX ][ focusY ];
		int compare = 0;
		upperLeftCorner( focusX, focusY );
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
		return !conflictFound; // success
	}
	
	void upperLeftCorner( int x, int y ) // ready 12 3 2
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
	}
}





















