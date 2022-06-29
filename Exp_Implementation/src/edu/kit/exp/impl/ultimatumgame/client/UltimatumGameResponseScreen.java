package edu.kit.exp.impl.ultimatumgame.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import edu.kit.exp.client.gui.screens.Screen;

/**
 * The ultimatum game response screen provides a <code>Screen</code> for the
 * response in an ultimatum game.
 * 
 * @see Screen
 */
public class UltimatumGameResponseScreen extends Screen {

	/** The answer parameters. */
	private UltimatumGameResponseObject answerParameters = new UltimatumGameResponseObject();

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8649592944692042312L;

	/**
	 * Constructor.
	 * 
	 * @param queueId
	 *            The String ID of the running game, whose institution triggered
	 *            that screen to be shown at the client.
	 * @param parameter
	 *            UltimateGameParamObject list of all parameters used in this
	 *            screen: Index 0= offer text (Like: "The requester offers
	 *            you... Do you accept?" HTML can be used for formating)
	 * @param screenId
	 *            A String which contains the global screen ID that has to be
	 *            given for a complete trial entry at server side.
	 * @param showUpTime
	 *            A Long variable which contains the time the screen will be
	 *            shown to a client.
	 */
	public UltimatumGameResponseScreen(String queueId, UltimatumGameParamObject parameter, String screenId,
			Long showUpTime) {
		super(queueId, parameter, screenId, showUpTime);

		String info = String.valueOf(parameter.getInfoText());

		setLayout(new BorderLayout(0, 0));

		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setBackground(Color.LIGHT_GRAY);
		add(contentPanel);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		infoPanel.setBackground(Color.WHITE);
		contentPanel.add(infoPanel, BorderLayout.CENTER);
		infoPanel.setLayout(new BorderLayout(0, 0));

		JLabel lblInfo = new JLabel(info);
		infoPanel.add(lblInfo, BorderLayout.CENTER);

		JPanel inputPanel = new JPanel();
		inputPanel.setBackground(Color.WHITE);
		contentPanel.add(inputPanel, BorderLayout.SOUTH);

		/* Accept Button */
		JButton btnAccept = new JButton("Accept");
		btnAccept.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				answerParameters.setActionPerformed(Boolean.valueOf(true));
				thisScreen.sendResponseAndWait(answerParameters);

			}
		});
		inputPanel.add(btnAccept);

		/* Decline Button */
		JButton btnDecline = new JButton("Decline");
		btnDecline.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				answerParameters.setActionPerformed(Boolean.valueOf(false));
				thisScreen.sendResponse(answerParameters);

			}
		});

		inputPanel.add(btnDecline);
	}
}
