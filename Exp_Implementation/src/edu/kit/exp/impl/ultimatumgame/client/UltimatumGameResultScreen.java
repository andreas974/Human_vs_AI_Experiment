package edu.kit.exp.impl.ultimatumgame.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import edu.kit.exp.client.gui.screens.Screen;

/**
 * The ultimatum game response screen provides a <code>Screen</code> for
 * displaying the result of an ultimatum game to clients.
 * 
 * @see Screen
 */
public class UltimatumGameResultScreen extends Screen {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5584915447530697658L;

	/** The message. */
	private String message;

	/** The info label. */
	private JLabel labelInfo;

	/**
	 * This constructor creates am instance of DefaultInfoScreen.
	 * 
	 * @param queueId
	 *            The String ID of the running game, whose institution triggered
	 *            that screen to be shown at the client.
	 * @param parameter
	 *            A UltimatumGameParamObject list of all parameters used in this
	 *            screen: Index: 0=result message, 1=show up time
	 * @param screenId
	 *            A String which contains the global screen ID that has to be
	 *            given for a complete trial entry at server side.
	 * @param showUpTime
	 *            A Long variable which contains the time the screen will be
	 *            shown to a client.
	 */
	public UltimatumGameResultScreen(String queueId, UltimatumGameParamObject parameter, String screenId,
			Long showUpTime) {
		super(queueId, parameter, screenId, showUpTime);

		setLogDisplayEvent(true);

		this.message = (String) parameter.getInfoText();
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
		lblLogo.setIcon(new ImageIcon(
				UltimatumGameResultScreen.class.getResource("/edu/kit/exp/common/resources/kit_logo.png")));
		panel.add(lblLogo, BorderLayout.EAST);
	}

	/**
	 * This method sets the custom screen parameters.
	 * 
	 * @param parameters
	 *            a list of new custom screen parameters
	 * 
	 * @throws RemoteException
	 *             If there is no connection to the server.
	 */
	public void setCustomScreenParameter(ArrayList<Object> parameters) throws RemoteException {

		// welcome message text from server
		this.message = (String) parameters.get(0);

	}
}
