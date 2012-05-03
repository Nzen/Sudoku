package sudoku;

public class Sudoku
{
	private int valLIMIT;
	private int sqXLIMIT; // used in squareConflict( ), important in asymmetric rectangles
	private int sqYLIMIT;
	private int nX; // vars for next or previous cell; changed by solve( ) & upperLeftCorner
	private int nY;
	private int guess[ ][ ];
	private boolean success = true;
	
	public Sudoku( String clueFile )
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
	}

	void checkLimits( )
	{
		if ( valLIMIT != sqXLIMIT * sqYLIMIT )
		{
			System.out.print( "Invalid puzzle file: size and square limits conflict." );
			System.exit( 1 );
		}
	}

	void inputClue( int row, int cell, int val )
	{
		guess[ row ][ cell ] = ( val > 0 ) ? val * -1 : val; // 0 OR -#
		// aren't int arrays already initialized to 0?
	}
	
	void outputSolution( )
	{
		boolean newline = true;
		System.out.print( "Check if I solved it" );
		Stream2File printPort = new Stream2File( );
		printPort.openFile( "solution.txt" );
		for( int rows[ ] : guess )
		{
			for( int guessVal : rows )
				if ( clueVal( guessVal ) )
					printPort.output( Integer.toString( guessVal * -1 ), !newline );
				else // print normally
					printPort.output( Integer.toString( guessVal ), !newline );
			printPort.output( "", newline );
		}	
		printPort.closeFile( );
	}

	public void run( )
	{	// change to display & interactive mode when solve works
		//if ( solve( 0, 0, false ) ) //backtrack
		if ( solveIteratively( ) )
			outputSolution( );
		else 
			System.out.print( "I didn't solve it" );
	}
	
	/**
	 * Time/Space: time bartered for now: lots of math 
	 **/
	boolean solve( int x, int y, boolean backtracking ) // works 12 5 2
	{
		if ( clueCell( x, y ) )
		{
			if ( backtracking )
			{
				if ( thisNotFirstCell( x, y ) )
				{
					previousCell( x, y );
					return solve( nX, nY, backtracking );
				}
				else // backtracked to start
					return !success; // everything else tested, clue can't change
			}
			else if ( thisNotLastCell( x, y ) )
			{	// move to next
				nextCell( x, y );
				return solve( nX, nY, backtracking ); // IE nextCell
			}
			else
				return success; // last cell was a clue
		}
		else
		{	// cell open for guessing
			if ( foundValidGuess( x, y ) )
			{
				backtracking = false;
				if ( thisNotLastCell( x, y ) )
				{
					nextCell( x, y );
					return solve( nX, nY, backtracking );
				}
				else
					return success; // this guess was last, nothing conflicts
			}
			else // exhausted possibilities in this cell
			{
				backtracking = true;
				if ( thisNotFirstCell( x, y ) )
				{
					guess[ x ][ y ] = 0; // reset this cell
					previousCell( x, y );
					return solve( nX, nY, backtracking );
				}
				else // backtracked to start
					return !success; // everything tested, nothing worked
			}
		}
	}
	
	// because recursive solution overflows past 6x6, iterative wins
	boolean solveIteratively( ) // works 12 5 1
	{
		int x = 0;
		int y = 0;
		boolean backtracking = false;
		while ( true )
		{
			if ( clueCell( x, y ) )
			{
				if ( backtracking ) // that's funny, shouldn't I test if this is first cell?
				{
					previousCell( x, y );
					x = nX;
					y = nY;
					continue; // to previous
				}
				else if ( thisNotLastCell( x, y ) )
				{	// move to next
					nextCell( x, y );
					x = nX;
					y = nY;
					continue; // to next
				}
				else
					return success; // last cell was a clue
			}
			else
			{	// cell open for guessing
				if ( foundValidGuess( x, y ) )
				{
					backtracking = false;
					if ( thisNotLastCell( x, y ) )
					{
						nextCell( x, y );
						//showGrid( );
						x = nX;
						y = nY;
						continue; // to next
					}
					else	//{showGrid( );
						return success; // this guess was last, nothing conflicts
				}
				else // exhausted possibilities in this cell
				{
					backtracking = true;
					if ( thisNotFirstCell( x, y ) )
					{
						guess[ x ][ y ] = 0; // reset this cell
						previousCell( x, y );
						x = nX;
						y = nY;
						continue; // to previous
					}
					else // backtracked to start
						return !success; // everything tested, nothing worked
				}
			}
		}
	}
	
	boolean foundValidGuess( int x, int y )
	{
		while ( guess[ x ][ y ] < valLIMIT )
		{
			guess[ x ][ y ] += 1;
			if ( guessConflicts( x, y ) )
				continue;
			else
				return true;
		}
		guess[ x ][ y ] = 0; // reset this cell
		return false;
	}

	/**
	Calculates the new cell coordinates and stores in nX & nY for immediate use
	Takes care not to overflow to the right or bottom.
	**/
	void nextCell( int x, int y )
	{
		if ( x < valLIMIT - 1 )
		{
			nX = x + 1;
			nY = y;
		}
		else
			if ( y < valLIMIT - 1 )
			{
				nX = 0;
				nY = y + 1;
			}
	}

	/**
	Calculates the new cell coordinates and stores in nX & nY for immediate use
	Takes care not to overflow to the right or bottom.
	**/
	void previousCell( int x, int y )
	{
		if ( x > 0 )
		{
			nX = x - 1;
			nY = y;
		}
		else
			if ( y > 0 )
			{
				nX = valLIMIT - 1; // x wraps back to "valLimit"
				nY = y - 1;
			}
		// 0,0 handled by thisNotFirstCell
	}
	
	boolean thisNotLastCell( int x, int y )
	{
		return x < valLIMIT - 1 || y < valLIMIT - 1;
	}
	
	boolean thisNotFirstCell( int x, int y )
	{
		return x > 0 || y > 0;
	}
	
	/** returns true when cell contains a clue **/
	boolean clueCell( int x, int y )
	{
		return guess[ x ][ y ] < 0;
	}
	
	boolean clueVal( int guess )
	{
		return guess < 0;
	}
	
	boolean guessConflicts( int x, int y )
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
	
	boolean aRowConflict( int focusX, int focusY )
	{
		boolean conflictFound = true;
		int candidateVal = guess[ focusX ][ focusY ];
		int compareVal;
		for ( int cell = 0; cell < valLIMIT; cell++ )
		{
			if ( cell == focusY )
				continue; // can't conflict with itself
			else
			{
				if ( clueCell( focusX, cell ) )
					compareVal = guess[ focusX ][ cell ] * -1; // get value
				else
					compareVal = guess[ focusX ][ cell ]; // of the cell
				if ( compareVal == candidateVal )
					return conflictFound;
			}
		}
		return !conflictFound;
	}
	
	boolean aVerticalConflict( int focusX, int focusY )
	{
		boolean conflictFound = true;
		int candidate = guess[ focusX ][ focusY ];
		int compareVal;
		for ( int row = 0; row < valLIMIT; row++ )
		{
			if ( row == focusX )
				continue;
			else
			{
				if ( clueCell( row, focusY ) )
					compareVal = guess[ row ][ focusY ] * -1;
				else
					compareVal = guess[ row ][ focusY ];
				if ( compareVal == candidate )
					return conflictFound;
			}
		}
		return !conflictFound;
	}
	
	boolean aSquareConflict( int focusX, int focusY )
	{
		boolean conflictFound = true;
		int candidate = guess[ focusX ][ focusY ];
		int compareVal;
		upperLeftCorner( focusX, focusY );
		for ( int row = nX; row < sqXLIMIT + nX; row++ )
		{
			for ( int cell = nY; cell < sqYLIMIT + nY; cell++ )
			{
				if ( row == focusX && cell == focusY )
					continue; // cause its the cell
				else
				{
					if ( clueCell( row, cell ) )
						compareVal = guess[ row ][ cell ] * -1;
					else
						compareVal = guess[ row ][ cell ];
					if ( compareVal == candidate )
						return conflictFound;
				}
			}
		}
		return !conflictFound; // success
	}

	void upperLeftCorner( int x, int y )
	{
		nX = ( x / sqXLIMIT ) * sqXLIMIT;
		nY = ( y / sqYLIMIT ) * sqYLIMIT;
	}	// credit to github	mattrajca	sudoku-solver

	int getNx( )
	{	return nX;	}
	
	int getNy( )
	{	return nY;	}
	
	void showGrid( )
	{
		for ( int[] rows : guess )
		{
			for ( int marks : rows )
			{
				System.out.print( marks + "-" );
			}
			System.out.println( );
		}
		System.out.print( "\n==\n" );
	}
}