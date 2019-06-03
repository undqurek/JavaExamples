package example.clinet;

import java.io.IOException;
import java.util.Random;

import example.gui.GuiUtils;

public class Main
{
	public static void main( String[] args ) throws IOException
	{
		Client client = new Client();
		
		Random randomizer = new Random();
		String userLogin = "user-" + ( (long) randomizer.nextInt() - (long) Integer.MIN_VALUE );
		
		GuiUtils.createFrame( "Client(" + userLogin + ")", client );
		
		if ( client.connect( "127.0.0.1", 5000, userLogin ) )
		{
			// /TEST:
			client.sendMessageFrame( "Fake message!" );
			client.sendMessageFrame( "Fake message!" );
			client.sendMessageFrame( "Fake message!" );
			client.sendMessageFrame( "Fake message!" );
			client.sendMessageFrame( "Fake message!" );
			client.sendMessageFrame( "Fake message!" );
			
			
			System.out.println( "Press any key to exit." );
			System.in.read();
			
			client.disconnect();
		}
		
		System.exit( 0 );
	}
}
