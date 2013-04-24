import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ConnectionGUI extends JFrame implements ActionListener, KeyListener
{
	private static final long serialVersionUID = 1L;
	
	JPanel mainPanel = new JPanel();

	JLabel titleLabel = new JLabel("Connection Type");
	
	JButton okButton = new JButton("OK");
	
	String[] dataTypesStrings = { "TCP", "UDP" };
	
	JComboBox dataTypes = new JComboBox(dataTypesStrings);
	
	ConnectionGUI()
	{
		super("Connection Information");
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		setLayout(fl);
		
		createPanel();
		add(mainPanel);
	}
	
	public void createPanel()
	{
		mainPanel.setPreferredSize(new Dimension(300, 350));
		titleLabel.setPreferredSize(new Dimension(300, 20));
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		mainPanel.add(titleLabel);
		
		dataTypes.setPreferredSize(new Dimension(250, 20));
		dataTypes.addKeyListener(this);
		mainPanel.add(dataTypes);
		
		mainPanel.add(okButton);
		mainPanel.addKeyListener(this);
		okButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == okButton)
		{
			Main.transferType = (String)dataTypes.getSelectedItem();
			this.setVisible(false);
			Main.connectionGUIStatus = true;
		}
	}


	@Override
	public void keyPressed(KeyEvent e) 
	{
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			Main.transferType = (String)dataTypes.getSelectedItem();
			this.setVisible(false);
			Main.connectionGUIStatus = true;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}
}