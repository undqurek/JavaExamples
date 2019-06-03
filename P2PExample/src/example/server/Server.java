package example.server;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import example.common.FrameType;
import example.common.Communicator;
import example.common.GamerEvent;

public class Server implements Communicator
{
	private Object locker = new Object(); // chroni dostêpu do obiektów wykorzystywanych na poziomie wielu w¹tków
	
	private boolean connected = false; // informuje o stanie otwartych socketów logikê zewnêtrzn¹
	private boolean looped = false; // podtrzymuje wszystkie pêtle
	
	private GamerEvent event; // przekazuje zdarzenia na zewn¹trz logiki klasy serwera
	
	private Player serverPlayer; // gracz serwer
	private Player clientPlayer; // gracz pod³¹czony klient
	
	private Thread thread;
	
	private ServerSocket serverSocket = null;
	private Socket clientSocket = null;
	
	private DataInputStream inputStream = null; // wejœciowy strumieñ danych od klienta
	private DataOutputStream outputStream = null; // wyjœciowy strumieñ danych od klienta
	
	@Override
	public void setEvent( GamerEvent event )
	{
		//TODO: ochrona przed nag³¹ zmian¹ stanów serwera
		
		this.event = event;
	}
	
	public int getUserId()
	{
		return this.serverPlayer.getId();
	}
	
	public String getUserLogin()
	{
		return this.serverPlayer.getLogin();
	}
	
	public boolean start( int port, String login )
	{
		if ( this.connected )
			return false;
		
		try
		{
			this.serverSocket = new ServerSocket( port );
		}
		catch ( IOException e )
		{
			return false;
		}
		
		this.serverPlayer = new Player( 1, login, (short)0, (short)0 );
		
		this.thread = new Thread( ( ) ->
		{
			while ( this.looped )
			{
				try
				{
					this.clientSocket = this.serverSocket.accept();
					
					this.inputStream = new DataInputStream( this.clientSocket.getInputStream() );
					this.outputStream = new DataOutputStream( this.clientSocket.getOutputStream() );
					
					while ( this.looped && this.makeIteration() );
				}
				catch ( IOException ex )
				{
					this.event.userLeft( this.clientPlayer.getId() ); // jeœli dojdzie do zerwania polaczenia z klientem trzeba powiadomiæ zewnêtrzna logikê klasy serwera o odejœciu klienta
				}
				finally
				{
					this.closeClient(); // po ka¿dym kliencie trzeba posprz¹taæ
				}
			}
		} );
		
		this.looped = true;
		
		this.thread.setName( "Client thread" );
		this.thread.start();
		
		this.connected = true;
		
		this.event.userLogged( this.serverPlayer.getId(), this.serverPlayer.getLogin(), this.serverPlayer.getX(), this.serverPlayer.getY() );
		
		return true;
	}
	
	public void disconnect()
	{
		if ( this.connected == false )
			return;
		
		this.looped = false;
		
		this.closeObjects();
		this.joinThread();
		
		this.connected = false;
		
		this.event.userLeft( this.serverPlayer.getId() );
	}
	
	@Override
	public boolean sendMovementFrame( int movementType )
	{
		if ( this.connected == false )
			return false;
		
		this.serverPlayer.move( movementType );
		
		try
		{
			// outputStream jest wykorzystywany przez wiele w¹tków wiec trzeba go chroniæ
			synchronized ( this.locker )
			{
				if( this.outputStream != null )
				{
					this.outputStream.writeByte( FrameType.MOVEMENT_FRAME );
					this.outputStream.writeInt( this.serverPlayer.getId() );
					this.outputStream.writeShort( this.serverPlayer.getX() );
					this.outputStream.writeShort( this.serverPlayer.getY() );
				}
			}
			
			this.event.movementReceived( this.serverPlayer.getId(), this.serverPlayer.getX(), this.serverPlayer.getY() );
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
			// outputStream jest wykorzystywany przez wiele w¹tków wiec trzeba go chroniæ
			synchronized ( this.locker )
			{
				if( this.outputStream != null )
				{
					this.outputStream.writeByte( FrameType.MESSAGE_FRAME );
					this.outputStream.writeInt( this.serverPlayer.getId() );
					this.outputStream.writeUTF( message );
				}
			}
			
			this.event.messageReceived( this.serverPlayer.getId(), message );
		}
		catch ( IOException e )
		{
			return false;
		}
		
		return true;
	}
	
	private boolean interpretLoginFrame() throws IOException
	{
		String login = this.inputStream.readUTF();
		
		// outputStream jest wykorzystywany przez wiele w¹tków wiec trzeba go chroniæ
		synchronized ( this.locker )
		{
			if ( login.equals( this.serverPlayer.getLogin() ) )
			{
				this.outputStream.writeBoolean( false );
				
				return false;
			}
			
			this.clientPlayer = new Player( 2, login, (short)0, (short)0 );
			
			// odpowiedŸ serwera na próbê zalogowania klienta
			this.outputStream.writeBoolean( true );
			this.outputStream.writeInt( this.clientPlayer.getId() );
			this.outputStream.writeShort( this.clientPlayer.getX() );
			this.outputStream.writeShort( this.clientPlayer.getY() );
			
			// po zalogowaniu serwer przedstawia sie klientowi
			this.outputStream.writeByte( FrameType.LOGIN_FRAME );
			this.outputStream.writeInt( this.serverPlayer.getId() );
			this.outputStream.writeUTF( this.serverPlayer.getLogin() );
			this.outputStream.writeShort( this.serverPlayer.getX() );
			this.outputStream.writeShort( this.serverPlayer.getY() );
		}
		
		this.event.userLogged( this.clientPlayer.getId(), this.clientPlayer.getLogin(), this.clientPlayer.getX(), this.clientPlayer.getY() );
	
		return true;
	}
	
	private boolean interpretLogoutFrame() throws IOException
	{
		this.event.userLeft( this.clientPlayer.getId() );
		
		return true;
	}
	
	private boolean interpretMovementFrame() throws IOException
	{
		this.clientPlayer.move( this.inputStream.readByte() );
		
		// outputStream jest wykorzystywany przez wiele w¹tków wiec trzeba go chroniæ
		synchronized ( this.locker ) 
		{
			// serwer kontroluje logikê gry wiec po sukcesywnej operacji move klienta powinna zostaæ odes³ana ramka z nowa pozycja gracza
			this.outputStream.writeByte( FrameType.MOVEMENT_FRAME );
			this.outputStream.writeInt( this.clientPlayer.getId() );
			this.outputStream.writeShort( this.clientPlayer.getX() );
			this.outputStream.writeShort( this.clientPlayer.getY() );
		}
		
		// trzeba powiadomiæ logikê zewnêtrzn¹ klienta o zdarzeniu
		this.event.movementReceived( this.clientPlayer.getId(), this.clientPlayer.getX(), this.clientPlayer.getY() );
		
		return true;
	}
	
	private boolean interpretMessageFrame() throws IOException
	{
		String message = this.inputStream.readUTF();
		
		// outputStream jest wykorzystywany przez wiele w¹tków wiec trzeba go chroniæ
		synchronized ( this.locker ) 
		{
			// ten wariant mo¿na uproœciæ tzn. nie odsy³aæ wiadomoœæ z powrotem
			this.outputStream.writeByte( FrameType.MESSAGE_FRAME );
			this.outputStream.writeInt( this.clientPlayer.getId() );
			this.outputStream.writeUTF( message );
		}
		
		// trzeba powiadomiæ logikê zewnêtrzn¹ klienta o zdarzeniu
		this.event.messageReceived( this.clientPlayer.getId(), message );
		
		return true;
	}
	
	private boolean makeIteration() throws IOException
	{
		// pierwszy byte wysy³anej ramki przez klienta to typ komunikatu
		int frameType = this.inputStream.readByte();
		
		switch ( frameType )
		{
			case FrameType.LOGIN_FRAME:
				return this.interpretLoginFrame();

			case FrameType.LOGOUT_FRAME:
				return this.interpretLogoutFrame();

			case FrameType.MOVEMENT_FRAME:
				return this.interpretMovementFrame();

			case FrameType.MESSAGE_FRAME:
				return this.interpretMessageFrame();
			
			default:
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
	
	private void closeClient()
	{
		this.closeObject( this.outputStream );
		this.closeObject( this.inputStream );
		this.closeObject( this.clientSocket );
		
		this.clientPlayer = null;
	}
	
	private void closeObjects()
	{
		this.closeClient();
		this.closeObject( this.serverSocket );
		
		this.serverPlayer = null;
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
	
	public void stop()
	{
		this.looped = false; // przerywanie pracy serwera polega na przerwaniu wszystkich pêtli
		
		this.closeObjects(); // nale¿y zamkn¹æ wszystkie sockety i strumienie, aby przerwaæ blokuj¹ce metody read
		this.joinThread(); // trzeba poczekaæ a¿ watek serwera zakoñczy swoje dzia³anie
		
		this.connected = false;
	}
}
