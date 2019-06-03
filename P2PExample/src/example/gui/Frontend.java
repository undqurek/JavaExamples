package example.gui;

import java.awt.Color;

public interface Frontend
{
	public boolean registerPlayer( int id, String name, Color color, short x, short y );
	
	public void deregisterPlayer( int id );
	
	public void movePlayer( int id, short x, short y );
	
	public void clearPlayers();
}
