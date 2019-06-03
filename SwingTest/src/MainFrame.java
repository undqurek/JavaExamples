import javax.swing.JFrame;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = -2697372672378215128L;

	public MainFrame() {
		BoardPanel panel = new BoardPanel();

		panel.setLocation(0, 0);
		panel.setFocusable(true);

		this.add(panel);
		this.pack();
	}
}
