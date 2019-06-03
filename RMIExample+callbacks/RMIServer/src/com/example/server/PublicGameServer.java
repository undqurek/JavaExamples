package com.example.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.example.domain.MessageEvent;
import com.example.domain.User;
import com.example.rmi.features.GameServer;

public class PublicGameServer extends UnicastRemoteObject implements GameServer
{
	private static final long serialVersionUID = -2101656626166332642L;
	
	private boolean logged = false;
	private MessageEvent event = null;
	
	public PublicGameServer() throws RemoteException
	{
		super();
	}

	@Override
	public void setMessageEvent( MessageEvent event ) throws RemoteException
	{
		//TODO: wymagana implementacja sessji dla roznych uzytkownikow
		
		this.event = event;		
	}
	
	@Override
	public boolean loginUser( String login, String password ) throws RemoteException
	{
		//TODO: wymagana implementacja sessji dla roznych uzytkownikow
		
		if(this.logged)
			return false;
		
		if("admin".equals( login ) && "admin".equals( password ))
		{
			this.logged = true;
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public void logoutUser() throws RemoteException
	{
		//TODO: wymagana implementacja sessji dla roznych uzytkownikow
		
		this.logged = false;
	}
	
	@Override
	public User[] getUsers() throws RemoteException
	{
		//TODO: wymagana implementacja sessji dla roznych uzytkownikow
		
		if(this.logged == false)
			return null;
		
		User[] users = new User[2];
		
		users[ 0 ] = new User( 1, "Jan" );
		users[ 1 ] = new User( 2, "Denis" );
		
		return users;
	}
	
	public void stop()
	{
		//TODO: logika zatrzymujaca serwer np. przerywanie watkow
		//TODO: wymagana implementacja sessji dla roznych uzytkownikow
	}

	@Override
	public void sendMessages() throws RemoteException
	{
		//TODO: wymagana implementacja sessji dla roznych uzytkownikow
		
		if(this.event == null)
			return;
		
		this.event.messageSended( "Message 1" );
		this.event.messageSended( "Message 2" );
		this.event.messageSended( "Message 3" );
		this.event.messageSended( "Message 4" );
	}
}
