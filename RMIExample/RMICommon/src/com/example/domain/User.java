package com.example.domain;

import java.io.Serializable;

public class User implements Serializable
{
	private static final long serialVersionUID = 3981382297330388213L;
	
	private long id;
	private String login;
	
	public User()
	{
		
	}
	
	public User(long id, String login)
	{
		this.id = id;
		this.login = login;
	}
	
	public long getId()
	{
		return this.id;
	}
	
	public void setId( long id )
	{
		this.id = id;
	}
	
	public String getLogin()
	{
		return this.login;
	}
	
	public void setLogin( String login )
	{
		this.login = login;
	}
}
