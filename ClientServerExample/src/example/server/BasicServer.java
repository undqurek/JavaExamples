package example.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import example.Utils;

public abstract class BasicServer
{
	private boolean started = false;
	private boolean looped = false;
	
	private HashMap<InetAddress, Socket> clientsSockets = new HashMap<InetAddress, Socket>();
	
	private ServerSocket serverSocket;
	private Thread serverThread;
	
	public boolean isStarted()
	{
		return this.started;
	}
	
	public boolean isLooped()
	{
		return this.looped;
	}
	
	protected abstract void performClient( Socket clientSocket );
	
	private void prepareClient( final Socket clientSocket )
	{
		Thread thread = new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					BasicServer.this.performClient( clientSocket );
				}
				finally
				{
					BasicServer.this.releaseClient( clientSocket );
				}
			}
		};
		thread.start();
		
		synchronized ( this.clientsSockets )
		{
			this.clientsSockets.put( clientSocket.getInetAddress(), clientSocket );
		}
	}
	
	private void releaseClient( Socket clientSocket )
	{
		Utils.close( clientSocket );
		
		synchronized ( this.clientsSockets )
		{
			this.clientsSockets.remove( clientSocket.getInetAddress() );
		}
	}
	
	private void finalizeClients()
	{
		synchronized ( this.clientsSockets )
		{
			for ( Socket el : this.clientsSockets.values() )
				Utils.close( el );
		}
		
		while ( true )
		{
			synchronized ( this.clientsSockets )
			{
				if ( this.clientsSockets.size() == 0 )
					break;
			}
			
			Utils.sleep( 100 );
		}
	}
	
	public void strat( int port ) throws IOException
	{
		if ( this.started )
			throw new IOException( "Server is already started." );
		
		this.serverSocket = new ServerSocket( port );
		this.serverThread = new Thread()
		{
			@Override
			public void run()
			{
				while ( BasicServer.this.looped )
				{
					Socket clientSocket = null;
					
					try
					{
						clientSocket = BasicServer.this.serverSocket.accept();
					}
					catch ( IOException e )
					{
						BasicServer.this.looped = false;
						Utils.close( BasicServer.this.serverSocket );
						
						break;
					}
					
					BasicServer.this.prepareClient( clientSocket );
				}
				
				BasicServer.this.finalizeClients();
				BasicServer.this.started = false;
			}
		};
		
		this.looped = true;
		
		this.serverThread.start();
		this.started = true;
	}
	
	public void stop()
	{
		if ( this.started )
		{
			this.looped = false;
			
			Utils.close( this.serverSocket );
			Utils.join( this.serverThread );
		}
	}
}
