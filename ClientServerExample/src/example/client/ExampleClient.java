package example.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

import example.Utils;
import example.protocol.Frame;
import example.protocol.LoginFrame;
import example.protocol.MessageFrame;
import example.protocol.ResultFrame;

public class ExampleClient extends BasicClient
{
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	
	private static Random random = new Random();
	private long id;
	
	public ExampleClient()
	{
		this.id = (long) random.nextInt() - (long) Integer.MIN_VALUE;
	}
	
	private void prepareStreams( Socket clientSocket ) throws IOException
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
	
	private void interpretMessageFrame( MessageFrame messageFrame )
	{
		System.out.println( "Message from server: " + messageFrame.getMessage() );
	}
	
	private boolean sendLoginFrame( String username, String password ) throws IOException, ClassNotFoundException
	{
		LoginFrame loginFrame = new LoginFrame();
		
		loginFrame.setUsername( username );
		loginFrame.setPassword( password );
		
		this.outputStream.writeObject( loginFrame );
		
		// Utils.sleep( 1000 );
		
		// if ( this.inputStream.available() > 0 )
		{
			ResultFrame resultFrame = (ResultFrame) this.inputStream.readObject();
			
			if ( resultFrame.isCorrect() )
			{
				System.out.println( "Login success." );
				return true;
			}
		}
		
		System.out.println( "Login failed." );
		return false;
	}
	
	private void sendMessageFrame( String message ) throws IOException
	{
		MessageFrame messageFrame = new MessageFrame();
		messageFrame.setMessage( message );
		
		this.outputStream.writeObject( messageFrame );
	}
	
	@Override
	protected void performClient( Socket clientSocket )
	{
		try
		{
			this.prepareStreams( clientSocket );
		}
		catch ( IOException e )
		{
			System.out.println( "Streams creation error." );
			return;
		}
		
		finish :
		try
		{
			if ( this.sendLoginFrame( "root", "root" ) == false )
				break finish;
			
			this.sendMessageFrame( "Welcome! My ID is " + this.id );
			
			while ( ExampleClient.this.isLooped() )
			{
				// if ( this.inputStream.available() > 0 )
				{
					Frame frame = (Frame) this.inputStream.readObject();
					
					switch ( frame.getType() )
					{
						case Message:
							this.interpretMessageFrame( (MessageFrame) frame );
							this.sendMessageFrame( "[ID: " + this.id + "]: I am client." );
							
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
		
		Utils.close( this.inputStream );
		Utils.close( this.outputStream );
	}
}
