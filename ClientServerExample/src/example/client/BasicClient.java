package example.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import example.Utils;

public abstract class BasicClient
{
	private boolean connected = false;
	private boolean looped = false;
	
	private Socket clientSocket;
	private Thread clientThread;
	
	public boolean isConnected()
	{
		return this.connected;
	}
	
	public boolean isLooped()
	{
		return this.looped;
	}
	
	protected abstract void performClient( Socket clientSocket );
	
	public void connect( InetAddress address, int port ) throws IOException
	{
		if ( this.connected )
			throw new IOException( "Client is already connected." );;
		
		this.clientSocket = new Socket( address, port );
		this.clientThread = new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					BasicClient.this.performClient( BasicClient.this.clientSocket );
				}
				finally
				{
					Utils.close( BasicClient.this.clientSocket );
					BasicClient.this.connected = false;
				}
			}
		};
		
		this.looped = true;
		
		this.clientThread.start();
		this.connected = true;
	}
	
	public void disconnect()
	{
		if ( this.connected )
		{
			this.looped = false;
			
			Utils.close( this.clientSocket );
			Utils.join( this.clientThread );
		}
	}
}
