package passcrambler;

import javax.swing.JFrame;

import javax.swing.SwingUtilities;
public class Main
{	
	static String version = "0.1";
	
	public static void main(String[] args) 
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new MainFrame("passcrambler GUI");

				frame.setTitle(frame.getTitle()+ " "+ version);
				frame.setSize(600, 300);
				frame.setResizable(false);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
			
		});

	}
}
