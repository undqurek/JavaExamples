package example.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import example.Utils;
import example.protocol.Frame;
import example.protocol.LoginFrame;
import example.protocol.MessageFrame;
import example.protocol.ResultFrame;

public class ExampleServer extends BasicServer
{
	private class Logic
	{
		private ObjectInputStream inputStream = null;
		private ObjectOutputStream outputStream = null;
		
		public Logic( Socket clientSocket ) throws IOException
		{
			try
			{
				this.outputStream = Utils.createOutputStream( clientSocket );
				this.inputStream = Utils.createInputStream( clientSocket );
			}
			catch ( IOException ex )
			{
				if ( this.outputStream != null )
					Utils.close( this.outputStream );
				
				throw ex;
			}
		}
		
		public void close()
		{
			Utils.close( this.inputStream );
			Utils.close( this.outputStream );
		}
		
		public Frame readFrame() throws ClassNotFoundException, IOException
		{
			return (Frame) this.inputStream.readObject();
		}
		
		public void interpretLoginFrame( LoginFrame loginFrame ) throws IOException
		{
			ResultFrame resultFrame = new ResultFrame();
			
			if ( "root".equals( loginFrame.getUsername() ) && "root".equals( loginFrame.getPassword() ) )
			{
				System.out.println( "Login success." );
				resultFrame.setCorrect( true );
			}
			else
			{
				System.out.println( "Login failed." );
				resultFrame.setCorrect( false );
			}
			
			this.outputStream.writeObject( resultFrame );
		}
		
		public void interpretMessageFrame( MessageFrame messageFrame )
		{
			System.out.println( "Message from client: " + messageFrame.getMessage() );
		}
		
		public void sendMessage( String message ) throws IOException
		{
			MessageFrame messageFrame = new MessageFrame();
			messageFrame.setMessage( message );
			
			this.outputStream.writeObject( messageFrame );
		}
	}
	
	@Override
	protected void performClient( Socket clientSocket ) // dla kazdego kolejnego podlaczonego klienta wykonywana jest ta metoda
	{
		Logic logic = null; // na potrzeby kazdego klienta wymagane jest utworzenie osobnej logiki

		try
		{
			logic = new Logic( clientSocket );
		}
		catch ( IOException ex )
		{
			System.out.println( "Streams creation error." );
			Utils.close( clientSocket );
			
			return;
		}
		
		finish :
		try
		{
			while ( this.isLooped() ) // jesli serwer jest uruchomiony to podtrzymujemy wszystkie petle
			{
				// if ( this.inputStream.available() > 0 )
				{
					Frame frame = logic.readFrame(); // odbieramy ramke od klienta
					
					switch ( frame.getType() )
					{
						case Login:
							logic.interpretLoginFrame( (LoginFrame) frame );
							
							break;
						
						case Message:
							logic.interpretMessageFrame( (MessageFrame) frame );
							Utils.sleep( 5000 );
							logic.sendMessage( "I am server." );
							
							break;
						
						case Disconnect:
						default:
							break finish;
					}
				}
				// else
				// Utils.sleep( 100 );
			}
		}
		catch ( IOException | ClassNotFoundException ex )
		{
			System.out.println( "Communication error." );
		}
		
		logic.close();
		Utils.close( clientSocket );
	}
}
