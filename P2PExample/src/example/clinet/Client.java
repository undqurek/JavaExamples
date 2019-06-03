package example.clinet;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import example.common.FrameType;
import example.common.Communicator;
import example.common.GamerEvent;

public class Client implements Communicator
{
	private boolean connected = false;
	private boolean looped = false;
	
	private GamerEvent event;
	
	private int userId;
	private String userLogin;
	
	private Socket socket = null;
	private Thread thread = null;
	
	private DataInputStream inputStream = null;
	private DataOutputStream outputStream = null;
	
	@Override
	public int getUserId()
	{
		return this.userId;
	}
	
	@Override
	public String getUserLogin()
	{
		return this.userLogin;
	}
	
	@Override
	public void setEvent( GamerEvent event )
	{
		this.event = event;
	}
	
	public boolean connect( String host, int port, String login )
	{
		if ( this.connected )
			return false;
		
		try
		{
			this.socket = new Socket( host, port );
			
			this.inputStream = new DataInputStream( this.socket.getInputStream() );
			this.outputStream = new DataOutputStream( this.socket.getOutputStream() );
			
			this.outputStream.writeByte( FrameType.LOGIN_FRAME );
			this.outputStream.writeUTF( login );
			
			if ( this.inputStream.readBoolean() ) // sprawdzenie wyniku logowania
			{
				// pobieranie danych zalogowanego u¿ytkownika
				this.userId = this.inputStream.readInt(); 
				this.userLogin = login;
				
				short userX = this.inputStream.readShort();
				short userY = this.inputStream.readShort();
				
				this.connected = true;
				this.looped = true;
				
				this.event.userLogged( this.userId, login, userX, userY );
				
				this.runThread();
				
				return true;
			}
			else
				this.closeObjects();
		}
		catch ( IOException e )
		{
			this.closeObjects();
		}
		
		return false;
	}
	
	public void disconnect()
	{
		if ( this.connected == false )
			return;
		
		this.looped = false;
		
		this.closeObjects();
		this.joinThread();
		
		this.connected = false;
	}
	
	@Override
	public boolean sendMovementFrame( int movementType )
	{
		if ( this.connected == false )
			return false;
		
		try
		{
			this.outputStream.writeByte( FrameType.MOVEMENT_FRAME );
			this.outputStream.writeByte( movementType );
		}
		catch ( IOException e )
		{
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean sendMessageFrame( String message )
	{
		if ( this.connected == false )
			return false;
		
		try
		{
			this.outputStream.writeByte( FrameType.MESSAGE_FRAME );
			this.outputStream.writeUTF( message );
		}
		catch ( IOException e )
		{
			return false;
		}
		
		return true;
	}
	
	private void interpretLoginFrame() throws IOException
	{
		int userId = this.inputStream.readInt();
		String userLogin = this.inputStream.readUTF();
		short userX = this.inputStream.readShort();
		short userY = this.inputStream.readShort();
		
		this.event.userLogged( userId, userLogin, userX, userY );
	}
	
	private void interpretLogoutFrame() throws IOException
	{
		int userId = this.inputStream.readInt();
		
		this.event.userLeft( userId );
	}
	
	private void interpretMovementFrame() throws IOException
	{
		int userId = this.inputStream.readInt();
		short userX = this.inputStream.readShort();
		short userY = this.inputStream.readShort();
		
		this.event.movementReceived( userId, userX, userY );
	}
	
	private void interpretMessageFrame() throws IOException
	{
		int userId = this.inputStream.readInt();
		String userMessage = this.inputStream.readUTF();
		
		this.event.messageReceived( userId, userMessage );
	}
	
	private void makeIteration() throws IOException
	{
		int frameType = this.inputStream.readByte();
		
		switch ( frameType )
		{
			case FrameType.LOGIN_FRAME:
				this.interpretLoginFrame();
				break;
			
			case FrameType.LOGOUT_FRAME:
				this.interpretLogoutFrame();
				break;
			
			case FrameType.MOVEMENT_FRAME:
				this.interpretMovementFrame();
				break;
			
			case FrameType.MESSAGE_FRAME:
				this.interpretMessageFrame();
				break;
			
			default:
				break;
		}
	}
	
	private void runThread()
	{
		this.thread = new Thread(() ->
		{
			boolean error = false;
			
			try
			{
				while ( this.looped )
					this.makeIteration();
			}
			catch ( IOException e )
			{
				error = true;
			}
			finally
			{
				this.closeObjects();
				
				this.looped = false;
				this.connected = false;
				
				if ( error )
					this.event.connectionError();
			}
		});
		
		this.thread.setName( "Client thread" );
		this.thread.start();
	}
	
	private boolean joinThread()
	{
		try
		{
			this.thread.join();
			
			return true;
		}
		catch ( InterruptedException e )
		{
			return false;
		}
	}
	
	private boolean closeObject( Closeable object )
	{
		if ( object == null )
			return false;
		
		try
		{
			object.close();
			
			return true;
		}
		catch ( IOException e )
		{
			return false;
		}
	}
	
	private synchronized void closeObjects()
	{
		this.closeObject( this.outputStream );
		this.closeObject( this.inputStream );
		this.closeObject( this.socket );
	}
}
