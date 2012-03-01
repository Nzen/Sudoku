
// programmer: Nicholas Prado
// created 11 5 24
// Description: for reading files until I research stringReader

// updated 11 5 24, unit tests ok

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.IllegalStateException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class File4Stream_intAr
{
	private Scanner input;

	public void open( String fileName )
	{
		try
		{
			input = new Scanner( new File( fileName ) );
		}
		catch ( FileNotFoundException fileNotFoundException )
		{
			System.err.println( "Error opening file." );
			System.exit( 1 ); // or try again
		}
	}

	public int readInt( ) // MAKE SURE THIS IS WHAT YOU WANT
	{
		try
		{
			while ( input.hasNext( ) )
			{
				return input.nextInt( );
			}
		}
		catch ( NoSuchElementException elementException )
		{
			System.err.println( "File data improperly formed." );
			input.close();
			System.exit( 1 );
		}
		catch ( IllegalStateException stateException )
		{
			System.err.println( "Error reading from file." );
			System.exit( 1 );
		}
	}

	public void close( )
	{
	if ( input != null ) // ie, if it hasn't been closed
		input.close();
	}
}