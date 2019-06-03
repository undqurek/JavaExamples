package example;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Utils
{
	public static boolean close( Closeable object )
	{
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
	
	public static boolean join( Thread thread )
	{
		try
		{
			thread.join();
			
			return true;
		}
		catch ( InterruptedException e )
		{
			return false;
		}
	}
	
	public static boolean join( Thread thread, long milliseconds )
	{
		try
		{
			thread.join( milliseconds );
			
			return true;
		}
		catch ( InterruptedException e )
		{
			return false;
		}
	}
	
	public static boolean sleep( long milliseconds )
	{
		try
		{
			Thread.sleep( milliseconds );
			
			return true;
		}
		catch ( InterruptedException e )
		{
			return false;
		}
	}
	
	public static ObjectInputStream createInputStream( Socket socket ) throws IOException
	{
		InputStream stream = socket.getInputStream();
		BufferedInputStream buffer = null;
		
		try
		{
			buffer = new BufferedInputStream( stream );
			
			return new ObjectInputStream( buffer );
		}
		catch ( IOException ex )
		{
			if ( buffer != null )
				Utils.close( buffer );
			
			stream.close();
			
			throw ex;
		}
	}
	
	public static ObjectOutputStream createOutputStream( Socket socket ) throws IOException
	{
		OutputStream stream = socket.getOutputStream();
		
		try
		{
			return new ObjectOutputStream( stream );
		}
		catch ( IOException ex )
		{
			stream.close();
			
			throw ex;
		}
	}
}
