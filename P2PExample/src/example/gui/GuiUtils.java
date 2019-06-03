package example.gui;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.plaf.OptionPaneUI;

import example.common.Communicator;
import example.common.GamerEvent;
import example.common.MovementType;

public class GuiUtils
{
	public static int getMovementType( int keyChar )
	{
		switch ( keyChar )
		{
			case 'w':
				return MovementType.UP;
			case 's':
				return MovementType.DOWN;
			case 'a':
				return MovementType.LEFT;
			case 'd':
				return MovementType.RIGHT;
		}
		
		return MovementType.NONE;
	}
	
	public static Point getMovementVector( int keyChar, int speed )
	{
		switch ( keyChar )
		{
			case 'w':
				return new Point( 0, -speed );
			case 's':
				return new Point( 0, +speed );
			case 'a':
				return new Point( -speed, 0 );
			case 'd':
				return new Point( +speed, 0 );
		}
		
		return null;
	}
	
	public static Frontend createFrame( final String frameName, final Communicator communicator )
	{
		final BoardFrame frame = new BoardFrame();
		
		frame.addKeyListener( new KeyAdapter() // event z klawiatury
		{
			@Override
			public void keyTyped( KeyEvent arg )
			{
				int movementType = GuiUtils.getMovementType( arg.getKeyChar() );
				
				if ( movementType == MovementType.NONE )
					return;
				
				communicator.sendMovementFrame( movementType );
			}
		} );
		
		communicator.setEvent( new GamerEvent() // event z serwera
		{
			@Override
			public void userLogged( int id, String login, short x, short y )
			{
				SwingUtilities.invokeLater( ( ) -> frame.registerPlayer( id, login, Color.GREEN, x, y ) );
				
				System.out.println( "User logged! id: " + id + " login: " + login );
			}
			
			@Override
			public void userLeft( int id )
			{
				SwingUtilities.invokeLater( ( ) -> frame.deregisterPlayer( id ) );
				
				System.out.println( "User left! id: " + id );
			}
			
			@Override
			public void movementReceived( int userId, short userX, short userY )
			{
				SwingUtilities.invokeLater( ( ) -> frame.movePlayer( userId, userX, userY ) );
			}
			
			@Override
			public void messageReceived( int userId, String userMessage )
			{
				System.out.println( "Message received! user_id: " + userId + " message: " + userMessage );
			}
			
			@Override
			public void connectionError()
			{
				SwingUtilities.invokeLater( ( ) ->
				{
					JOptionPane.showMessageDialog( null, "Connection error!" );
				
					frame.clearPlayers();
				} );
				
				System.out.println( "Connection error!" );
			}
		} );
		
		SwingUtilities.invokeLater( ( ) ->
		{
			frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
			frame.setTitle( frameName );
			frame.setSize( 600, 400 );
			frame.setVisible( true );
		} );
		
		return frame;
	}
}
