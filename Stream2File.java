// Nicholas Prado
// created 11 3 28
// updated 11 4 19 (now prints a string rather than a specific sequence)

// uses formatter to print an ascii file. upgrade to fileWriter later

package sudoku;
import java.io.FileNotFoundException;
import java.lang.SecurityException;
import java.util.FormatterClosedException;
import java.util.NoSuchElementException;
import java.util.Formatter; // backbone

public class Stream2File
{
	private Formatter dataOut;

	public void openFile( String fileName ) // perhaps in the constructor instead?
	{
		try
		{
			dataOut = new Formatter( fileName ); // open [this] file
		}
		catch ( SecurityException securityException )
		{
			System.err.println( "\nYou do not have write access to this file." );
			System.exit( 1 );
		}
		catch ( FileNotFoundException fileNotFoundException )
		{
			System.err.println( "\nError opening or creating file." );
			System.exit( 1 );
		}
	}

	public void output( String line, boolean newline )
	{
		try
		{
			if ( newline )
				dataOut.format( "%n%s", line );
			else
				dataOut.format( " %s", line );
		}
		catch ( FormatterClosedException formatterClosedException )
		{
			System.err.println( "\nError writing to file." );
			return;
		}
		catch ( NoSuchElementException elementException )
		{
			System.err.println( "\nInvalid input. Please try again." );
			// not really sure what I would do here
		}
	}

	public void closeFile( )
	{
		if ( dataOut != null )
			dataOut.close();
	}
}
