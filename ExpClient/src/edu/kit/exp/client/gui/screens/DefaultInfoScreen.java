package edu.kit.exp.client.gui.screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * This class provides a simple information Screen! It can be used i.e. for
 * showing results.
 * 
 * @see Screen
 * 
 */
public class DefaultInfoScreen extends Screen {

	/**
	 * The Class ParamObject.
	 */
	public static class ParamObject extends Screen.ParamObject {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = -7078966659728040885L;

		/** The message. */
		private String message;

		/**
		 * This method gets a info message.
		 * 
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * This method sets a info message.
		 * 
		 * @param message
		 *            The new message.
		 */
		public void setMessage(String message) {
			this.message = message;
		}

		/**
		 * This constructor instantiates a new ParamObject.
		 */
		public ParamObject() {

		}

		/**
		 * This constructor instantiates a new ParamObject with an info message
		 * as a parameter.
		 * 
		 * @param message
		 *            A String which contains a message.
		 */
		public ParamObject(String message) {
			this.message = message;
		}
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5584915447530697658L;

	/** The message. */
	private String message;

	/** The label info. */
	private JLabel labelInfo;

	/**
	 * This method creates am instance of DefaultInfoScreen.
	 * 
	 * @param gameId
	 *            A String which contains the ID of the running game.
	 * @param parameter
	 *            A ParamObject screen parameter. In this case: Index: 0=Info
	 *            message
	 * @param screenId
	 *            A String which contains the global screen ID of the info
	 *            screen.
	 * @param showUpTime
	 *            A Long which indicates how long the Screen will be shown to a
	 *            client.
	 */
	public DefaultInfoScreen(String gameId, ParamObject parameter, String screenId, Long showUpTime) {
		super(gameId, parameter, screenId, showUpTime);

		this.message = parameter.getMessage();
		setLayout(new BorderLayout(0, 0));
		this.labelInfo = new JLabel(message);
		labelInfo.setFont(new Font("Tahoma", Font.BOLD, 17));

		setBorder(new EmptyBorder(10, 10, 10, 10));
		setBackground(Color.WHITE);

		setLayout(new BorderLayout(0, 0));

		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		infoPanel.setBackground(Color.WHITE);
		add(infoPanel, BorderLayout.CENTER);
		infoPanel.setLayout(new BorderLayout(0, 0));
		infoPanel.add(labelInfo);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		JLabel lblLogo = new JLabel("");
		lblLogo.setIcon(new ImageIcon(getClass().getResource("/edu/kit/exp/common/resources/kit_logo.png")));
		panel.add(lblLogo, BorderLayout.EAST);
	}

}
