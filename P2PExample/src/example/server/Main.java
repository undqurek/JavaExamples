package example.server;

import java.io.IOException;
import example.gui.GuiUtils;

public class Main
{
	public static void main( String[] args ) throws IOException
	{
		Server server = new Server();
		
		GuiUtils.createFrame( "Server", server );
		
		if ( server.start( 5000, "Server" ) )
		{
			// /TEST:
			server.sendMessageFrame( "Fake message!" );
			server.sendMessageFrame( "Fake message!" );
			server.sendMessageFrame( "Fake message!" );
			server.sendMessageFrame( "Fake message!" );
			server.sendMessageFrame( "Fake message!" );
			server.sendMessageFrame( "Fake message!" );
						
			System.out.println( "Press any key to exit." );
			System.in.read();
			
			server.stop();
		}
		else
			System.out.println( "Server error!" );
		
		System.exit( 0 );
	}
}
