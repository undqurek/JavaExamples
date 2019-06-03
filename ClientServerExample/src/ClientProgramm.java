import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import example.client.ExampleClient;

public class ClientProgramm
{
	private static Scanner scanner = new Scanner( System.in );
	
	/**
	 * @param args
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public static void main( String[] args ) throws UnknownHostException, IOException
	{
		ExampleClient client = new ExampleClient();
		
		InetAddress address = Inet4Address.getByName( "192.168.1.10" );
		int port = 8080;
		
		try
		{
			client.connect( address, port );
			System.out.println( "Client connected." );
		}
		catch ( IOException ex )
		{
			System.out.println( "Client connection problem. Check address, port or server status." );
			return;
		}
		
		System.out.println( "Type:\n - 'x' to disconnect client and close application\n - 'r' to reconnect\n" );
		
		while ( client.isConnected() )
		{
			String line = scanner.nextLine();
			
			if ( "x".equals( line ) )
			{
				client.disconnect();
				System.out.println( "Client disconnected." );
				
				break;
			}
			
			if ( "r".equals( line ) )
			{
				client.disconnect();
				System.out.println( "Client disconnected." );
				
				client.disconnect();
				
				try
				{
					client.connect( address, port );
					System.out.println( "Client connected." );
				}
				catch ( IOException ex )
				{
					System.out.println( "Client connection problem. Check address, port or server status." );
					break;
				}
			}
		}
		
		System.out.println( "Application closed." );
	}
	
}
