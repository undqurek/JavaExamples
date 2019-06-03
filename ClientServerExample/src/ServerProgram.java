import java.io.IOException;
import java.util.Scanner;

import example.server.ExampleServer;

public class ServerProgram
{
	private static Scanner scanner = new Scanner( System.in );
	
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main( String[] args ) throws IOException
	{
		int port = 8080;
		
		ExampleServer server = new ExampleServer();
		
		try
		{
			server.strat( port );
			System.out.println( "Server started." );
		}
		catch ( IOException ex )
		{
			System.out.println( "Server starting problem. Check port availability." );
			return;
		}
		
		System.out.println( "Type:\n - 'x' to stop server and close application\n - 'r' to restart serwer\n" );
		
		while ( server.isStarted() )
		{
			String line = scanner.nextLine();
			
			if ( "x".equals( line ) )
			{
				server.stop();
				System.out.println( "Server stopped." );
				
				break;
			}
			
			if ( "r".equals( line ) )
			{
				server.stop();
				System.out.println( "Server stopped." );
				
				try
				{
					server.strat( port );
					System.out.println( "Server started." );
				}
				catch ( IOException ex )
				{
					System.out.println( "Server starting problem. Check port availability." );
					break;
				}
			}
		}
		
		System.out.println( "Application closed." );
	}
	
}
