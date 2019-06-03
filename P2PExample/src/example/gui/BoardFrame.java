package example.gui;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class BoardFrame extends JFrame implements Frontend
{
	private static final long serialVersionUID = -2697372672378215128L;
	
	private BoardPanel panel;

	public BoardFrame()
	{
		this.panel = new BoardPanel();
		
		this.add( this.panel );
		this.pack();
	}
	
	@Override
	public boolean registerPlayer( int id, String name, Color color, short x, short y )
	{
		return this.panel.registerPlayer( id, name, color, x, y );
	}
	
	@Override
	public void deregisterPlayer( int id )
	{
		this.panel.deregisterPlayer( id );
	}
	
	@Override
	public void movePlayer( int id, short x, short y )
	{
		this.panel.movePlayer( id, x, y );
	}
	
	@Override
	public void clearPlayers()
	{
		this.panel.clearPlayers();
	}
}
