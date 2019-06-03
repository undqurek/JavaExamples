package example.gui;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main
{
	public static void main( String[] args )
	{
		SwingUtilities.invokeLater( new Runnable()
		{
			@Override
			public void run()
			{
				final BoardFrame frame = new BoardFrame();
				
				{
					final Frontend frontend = frame;
					
					frame.addKeyListener( new KeyAdapter()
					{
						short x = 0;
						short y = 0;
						
						@Override
						public void keyTyped( KeyEvent arg )
						{
							Point movement = GuiUtils.getMovementVector( arg.getKeyChar(), 30 );
							
							if ( movement == null )
								return;
							
							this.x += movement.getX();
							this.y += movement.getY();
							
							frontend.movePlayer( 0, this.x, this.y );
						}
					} );
	
					frontend.registerPlayer( 0, "Name", Color.GREEN, (short)0, (short)0 );
				}

				frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
				frame.setSize( 600, 400 );
				frame.setVisible( true );
			}
		} );
	}
}
