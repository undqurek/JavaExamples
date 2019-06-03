package example.gui;

import java.awt.Color;
import java.awt.Graphics;

import example.common.MovementType;

public class Player
{
	private int id;
	private String name;
	private Color color;
	
	private int x = 0;
	private int y = 0;
	private int width = 30;
	private int height = 30;
	
	private long updated = 0;
	private long delay = 1;
	
	public int getId()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Player( int id, String name, Color color, int x, int y )
	{
		this.id = id;
		this.name = name;
		this.color = color;
		
		this.x = x;
		this.y = y;
	}
	
	public void draw( Graphics g )
	{
		g.setColor( this.color );
		g.fillRect( this.x, this.y, this.width, this.height );
	}
	
	public void move( short x, short y )
	{
		long tmp = System.currentTimeMillis();
		
		if ( updated < tmp - delay ) // spowolnienie rekacji
		{
			this.x = x;
			this.y = y;
			
			updated = tmp;
		}
	}
}
