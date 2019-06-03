package example.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

public class BoardPanel extends JPanel implements Frontend
{
	private static final long serialVersionUID = -2484090054184219073L;
	
	private Map<Integer, Player> players = new HashMap<>();
	
	@Override
	public void paint( Graphics g )
	{
		g.setColor( Color.WHITE );
		g.fillRect( 0, 0, this.getWidth(), this.getHeight() );
		
		if ( this.players.size() > 0 )
		{
			for ( Player el : this.players.values() )
				el.draw( g );
		}
		else
		{
			g.setColor( Color.RED );
			g.drawString( "WAITING...", 10, 20 );
		}
	}
	
	@Override
	public boolean registerPlayer( int id, String name, Color color, short x, short y )
	{
		if ( this.players.containsKey( id ) )
			return false;
		
		this.players.put( id, new Player( id, name, color, x, y ) );
		this.repaint();
		
		return true;
	}
	
	@Override
	public void deregisterPlayer( int id )
	{
		this.players.remove( id );
		this.repaint();
	}
	
	@Override
	public void movePlayer( int id, short x, short y )
	{
		Player player = this.players.get( id );
		
		if ( player == null )
			return;
		
		player.move( x, y );
		this.repaint();
	}
	
	@Override
	public void clearPlayers()
	{
		this.players.clear();
		this.repaint();
	}
}
