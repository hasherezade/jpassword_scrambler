package passcrambler;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class MainFrame extends JFrame implements ClipboardOwner{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7773267864086231191L;

	JFileChooser fc;
	
	protected File file;
	protected final JTextField documentField;
	final JTextField loginField;
	final JPasswordField passField;
	final JPasswordField resultField;
	
	Scrambler.ScramblerSettings settings;
	
	public void setClipboardContents(String aString){
		StringSelection stringSelection = new StringSelection(aString);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, this);
	}
	  
	protected void openFile() {
		if (this.fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {  
	    	this.file = fc.getSelectedFile();
	    	this.documentField.setText(file.getName());
	    }
	}
	
	protected void generateLongpass() 
			throws IOException, InvalidKeyException, 
			NoSuchAlgorithmException, NoSuchPaddingException, 
			InvalidAlgorithmParameterException, 
			IllegalBlockSizeException, BadPaddingException
	{
		if (this.file == null) {
			return;
		}
		DataInputStream dataInput = new DataInputStream(
			    new BufferedInputStream(
			            new FileInputStream(this.file)));
		
		int size = (int) MainFrame.this.file.length();
		byte[] bytesArr = new byte[size];
		dataInput.read(bytesArr, 0, size);
		MainFrame.this.resultField.setText("ready...");
		String login = MainFrame.this.loginField.getText();
		String password = MainFrame.this.passField.getText();
		
		
		String longpass = Scrambler.generateLongpass(login, password, bytesArr, settings);
		MainFrame.this.resultField.setText(longpass);
	}
	
	public MainFrame(String title)
	{
		super(title);
		this.file = null;
		this.settings = new Scrambler().new ScramblerSettings("_&#", 30);
		//layout
		
		GridLayout myLayout = new GridLayout(0,2);
		setLayout(myLayout);
		this.fc = new JFileChooser();
		this.documentField = new JTextField();
		this.documentField.setEditable(false);
		this.loginField = new JTextField();
		this.passField = new JPasswordField();
		this.resultField = new JPasswordField();
		this.resultField.setEditable(false);
		this.resultField.setBackground(new Color( 0xff, 0xee, 0xbb));
		
		final JButton buttonGenerate = new JButton("Generate!");
		final JButton buttonClear = new JButton("Clear all");
		final JButton buttonClip = new JButton("To Clipboard!");
		final JButton buttonUncover = new JButton("Uncover");
		
		Container c = getContentPane();
		JButton buttonDocument = new JButton("Open document...");
		c.add(buttonDocument);
		c.add(documentField);
		JLabel loginLabel = new JLabel("login@domain:");
		c.add(loginLabel);
		c.add(loginField);
		
		c.add(new JLabel("easy password:"));
		c.add(passField);
		c.add(new JLabel("final password:"));
		c.add(resultField);
		
		c.add(buttonGenerate);
		c.add(buttonClear);
		c.add(buttonUncover);
		c.add(buttonClip);
		
		buttonDocument.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			    //Handle open button action.
				MainFrame.this.openFile();
			}
		});
		
		buttonGenerate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (MainFrame.this.file == null|| !MainFrame.this.file.canRead()) {
					JOptionPane.showMessageDialog(MainFrame.this,
						    "Set a file for the generation base",
						    "Error!",
						    JOptionPane.WARNING_MESSAGE);
					return;
				}
				if (MainFrame.this.loginField == null|| MainFrame.this.loginField.getText().length() == 0) {
					JOptionPane.showMessageDialog(MainFrame.this,
						    "Fill the login field",
						    "Error!",
						    JOptionPane.WARNING_MESSAGE);
					return;
				}
				if (MainFrame.this.passField == null|| MainFrame.this.loginField.getText().length() == 0) {
					JOptionPane.showMessageDialog(MainFrame.this,
						    "Fill the password field",
						    "Error!",
						    JOptionPane.WARNING_MESSAGE);
					return;
				}
				try {
					MainFrame.this.generateLongpass();
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(MainFrame.this,
						    e.getMessage(),
						    "Error!",
						    JOptionPane.WARNING_MESSAGE);

				}
			}
		});
		
		buttonClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				loginField.setText("");
				passField.setText("");
				resultField.setText("");
			}
		});
		
		buttonUncover.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				boolean covered = true;
				if (resultField.getEchoChar() == 0) {
					covered = false;
				}
				if (covered) {
					resultField.setEchoChar((char) 0);
					buttonUncover.setText("Cover");
				} else {
					resultField.setEchoChar('*');
					buttonUncover.setText("Uncover");
				}
			}
		});
		
		buttonClip.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setClipboardContents(resultField.getText());
			}
		});
	}

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		// TODO Auto-generated method stub
		
	}
}
