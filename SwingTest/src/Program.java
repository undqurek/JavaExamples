import java.awt.EventQueue;

public class Program {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainFrame frame = new MainFrame();
				
				frame.setSize(600, 400);
				frame.setVisible(true);
				
			}
		});
	}

}
