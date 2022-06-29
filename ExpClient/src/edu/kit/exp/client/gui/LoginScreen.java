package edu.kit.exp.client.gui;

import edu.kit.exp.client.comunication.ClientStatusManager;
import edu.kit.exp.common.Constants;
import edu.kit.exp.server.jpa.PersistenceUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class provides a login screen where clients can login on the
 * server.</br> A login dialog is shown, where the client can enter his client
 * Id and the IP of the server.
 * 
 */
public class LoginScreen extends JFrame {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2162644827002542056L;

	/** The instance. */
	private static LoginScreen instance;

	/** The text field client id. */
	private JTextField textFieldClientId;

	/** The text field server. */
	private JTextField textFieldServer;

	/** The input panel. */
	private JPanel inputPanel;

	/** The button panel. */
	private JPanel buttonPanel;

	// JControls
	/** The button login. */
	private JButton buttonLogin;

	/**
	 * This constructor instantiates a new login screen.
	 * 
	 * @throws HeadlessException
	 */
	private LoginScreen() throws HeadlessException {
		this.setBounds(50, 50, 299, 177);
		this.setTitle("Login Screen");
		this.setIconImage(new ImageIcon(MainFrame.class.getResource("/edu/kit/exp/common/resources/brownie_logo_noname_reverse.png")).getImage());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		init();
	}

	/**
	 * This method gets the single instance of LoginScreen.
	 * 
	 * @return the single instance of LoginScreen
	 */
	public static LoginScreen getInstance() {

		if (instance == null) {
			instance = new LoginScreen();
		}

		return instance;
	}

	/**
	 * This method initializes the login screen.
	 */
	private void init() {

		// text fields
		textFieldClientId = new JTextField(25);
		try {
			textFieldClientId.setText(Constants.getComputername());
		} catch (Exception ex) {
			textFieldClientId.setText("Client001");
		}

		// input panel
		inputPanel = new JPanel();
		inputPanel.setBorder(new EmptyBorder(10, 10, 0, 10));
		inputPanel.setLayout(new MigLayout());

		inputPanel.add(new JLabel("Client ID:"));
		inputPanel.add(textFieldClientId, "span, grow, wrap");

		inputPanel.add(new JLabel("Server:"));
		textFieldServer = new JTextField();
		textFieldServer.setText(Constants.getServerName());
		inputPanel.add(textFieldServer, "span, grow, wrap");

		// button
		buttonLogin = new JButton("Login");
		getRootPane().setDefaultButton(buttonLogin);
		buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
		buttonPanel.add(buttonLogin, BorderLayout.CENTER);
		buttonLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tryLogin();
			}
		});

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(inputPanel, BorderLayout.NORTH);
		this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

	}

	/**
	 * This method tries to login on the server.
	 */
	public void tryLogin() {
		buttonLogin.setEnabled(false);
		ClientStatusManager.getInstance().stop();

		try {
			PersistenceUtil.setDatabaseIP(textFieldServer.getText());

			ClientGuiController.getInstance().login(textFieldClientId.getText(), textFieldServer.getText());
			MainFrame.getInstance().setClientId(textFieldClientId.getText());
			MainFrame.getInstance().setVisible(true);
			MainFrame.getInstance().setTitle("brownie - " + textFieldClientId.getText());
			dispose();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(LoginScreen.getInstance(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

		buttonLogin.setEnabled(true);
	}

	/**
	 * This method sets the client ID.
	 * 
	 * @param name
	 *            The String which contains the new client ID.
	 */
	public void setClientId(String name) {
		textFieldClientId.setText(name);
	}

	/**
	 * This method sets the server IP.
	 * 
	 * @param ip
	 *            The String which contains the new server IP.
	 */
	public void setServerIp(String ip) {
		textFieldServer.setText(ip);
	}
}