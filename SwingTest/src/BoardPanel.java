import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

public class BoardPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8102024888799179149L;

	private String text = null;

	private boolean up = false;
	private boolean down = false;
	private boolean left = false;
	private boolean right = false;

	private double x = 50.0;
	private double y = 50.0;

	private double speed = 1.0;

	public BoardPanel() {
		this.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent arg) {

			}

			@Override
			public void keyReleased(KeyEvent arg) {
				switch (arg.getKeyChar()) {
				case 'w':
					BoardPanel.this.up = false;
					break;

				case 's':
					BoardPanel.this.down = false;
					break;

				case 'a':
					BoardPanel.this.left = false;
					break;

				case 'd':
					BoardPanel.this.right = false;
					break;

				default:
					break;
				}
			}

			@Override
			public void keyPressed(KeyEvent arg) {
				switch (arg.getKeyChar()) {
				case 'w':
					BoardPanel.this.up = true;
					break;

				case 's':
					BoardPanel.this.down = true;
					break;

				case 'a':
					BoardPanel.this.left = true;
					break;

				case 'd':
					BoardPanel.this.right = true;
					break;

				default:
					break;
				}
			}
		});

		Thread thread = new Thread() {
			@Override
			public void run() {
				// TODO: wstawic zmienn¹ podtrzymuj¹c¹
				while (true) {
					StringBuilder builder = new StringBuilder();

					if (BoardPanel.this.up) {
						builder.append("[up]");
						BoardPanel.this.goUp();
					}

					if (BoardPanel.this.down) {
						builder.append("[down]");
						BoardPanel.this.goDown();
					}

					if (BoardPanel.this.left) {
						builder.append("[left]");
						BoardPanel.this.goLeft();
					}

					if (BoardPanel.this.right) {
						builder.append("[right]");
						BoardPanel.this.goRight();
					}

					BoardPanel.this.text = builder.toString();
					BoardPanel.this.repaint();

					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
						return;
					}
				}
			}
		};
		thread.start();
	}

	@Override
	public void paint(Graphics g) {
		if (this.text == null)
			return;

		g.setColor(Color.red);
		g.clearRect(0, 0, BoardPanel.this.getWidth(),
				BoardPanel.this.getHeight());
		g.setColor(Color.green);
		g.fillRect((int) this.x, (int) this.y, 20, 20);
		g.setColor(Color.red);
		g.drawString("Use [up: w], [down: s], [left: a] or [right: d] key.",
				10, 15);
		g.setColor(Color.blue);
		g.drawString("Pressed: " + this.text, 10, 30);
	}

	private final void goUp() {
		this.y -= this.speed;
	}

	public final void goDown() {
		this.y += this.speed;
	}

	public final void goLeft() {
		this.x -= this.speed;
	}

	public final void goRight() {
		this.x += this.speed;
	}
}
