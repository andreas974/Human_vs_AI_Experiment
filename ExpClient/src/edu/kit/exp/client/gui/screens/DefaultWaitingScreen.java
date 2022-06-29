package edu.kit.exp.client.gui.screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * A class that provides a simple waiting screen that can be shown to a client!
 * 
 * Parameters: Index=0: waiting message.
 * 
 * @see Screen
 */
public class DefaultWaitingScreen extends Screen {

	/**
	 * The Class ParamObject.
	 */
	public static class ParamObject extends Screen.ParamObject {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 3195465641657601676L;

		/** The message. */
		private String message;

		/**
		 * This method gets a waiting message.
		 * 
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * This method sets a new waiting message.
		 * 
		 * @param message
		 *            The new String message.
		 */
		public void setMessage(String message) {
			this.message = message;
		}
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3873228381802962074L;

	/** The message. */
	private String message;

	/** The label wait message. */
	private JLabel labelWaitMessage;

	/**
	 * This constructor creates a new waiting screen.
	 * 
	 * @param gameId
	 *            A String which contains the ID of the running game.
	 * @param parameter
	 *            A ParamObject screen parameter. In this case: Index 0 =
	 *            waiting message
	 * @param screenId
	 *            A String which contains the global screen ID of the waiting
	 *            screen.
	 * @param showUpTime
	 *            A Long which indicates how long the Screen will be shown to a
	 *            client.
	 */
	public DefaultWaitingScreen(String gameId, ParamObject parameter, String screenId, Long showUpTime) {
		super(gameId, parameter, screenId, showUpTime);

		this.message = parameter.getMessage();
		setLayout(new BorderLayout(0, 0));
		this.labelWaitMessage = new JLabel("<html><body><h1>Please wait...</h1></body></html>");
		labelWaitMessage.setFont(new Font("Tahoma", Font.BOLD, 17));

		setBorder(new EmptyBorder(10, 10, 10, 10));
		setBackground(Color.WHITE);

		setLayout(new BorderLayout(0, 0));

		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		infoPanel.setBackground(Color.WHITE);
		add(infoPanel, BorderLayout.CENTER);
		infoPanel.setLayout(new BorderLayout(0, 0));
		infoPanel.add(labelWaitMessage);
		JLabel lblMessage;

		if (message != null) {
			lblMessage = new JLabel(message);
		} else {
			lblMessage = new JLabel();
		}
		lblMessage.setFont(new Font("Tahoma", Font.BOLD, 14));
		infoPanel.add(lblMessage, BorderLayout.SOUTH);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		JLabel lblLogo = new JLabel("");
		lblLogo.setIcon(new ImageIcon(getClass().getResource("/edu/kit/exp/common/resources/kit_logo.png")));
		panel.add(lblLogo, BorderLayout.EAST);
	}

	// public void setCustomScreenParameter(ArrayList<Object> parameters) throws
	// RemoteException {
	//
	// // welcome message text from server
	// this.message = (String) parameters.get(0);
	//
	// }

}
