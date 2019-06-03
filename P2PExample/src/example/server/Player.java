package example.server;

import example.common.MovementType;

public class Player
{
	private int id;
	private String login;
	
	private short x;
	private short y;
	
	private int speed = 30;
	
	public Player( int id, String login, short x, short y )
	{
		this.id = id;
		this.login = login;
		
		this.x = x;
		this.y = y;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public String getLogin()
	{
		return this.login;
	}
	
	public short getX()
	{
		return this.x;
	}
	
	public short getY()
	{
		return this.y;
	}
	
	public void move( int movementType )
	{
		switch ( movementType )
		{
			case MovementType.UP:
				this.y -= this.speed;
				break;
			
			case MovementType.DOWN:
				this.y += this.speed;
				break;
			
			case MovementType.LEFT:
				this.x -= this.speed;
				break;
			
			case MovementType.RIGHT:
				this.x += this.speed;
				break;
		}
	}
}
