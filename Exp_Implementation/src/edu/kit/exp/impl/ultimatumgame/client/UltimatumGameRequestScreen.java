package edu.kit.exp.impl.ultimatumgame.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.kit.exp.client.gui.screens.Screen;

/**
 * This class provides the screen for the request in an ultimatum game. Index of
 * parameters in the parameter list: Index 0 = client info text.
 * 
 * @see Screen
 */
public class UltimatumGameRequestScreen extends Screen {

	/**
	 * Constructor
	 * 
	 * @param gameId
	 *            The String ID of the running game, whose institution triggered
	 *            that screen to be shown at the client.
	 * @param parameter
	 *            A UltimatumGameParamObject list of all parameters used in this
	 *            screen: Index 0= info text (Like: "You own 10$ and have to
	 *            share it..." HTML can be used for formating)
	 * @param screenId
	 *            A String which contains the global screen ID that has to be
	 *            given for a complete trial entry at server side.
	 * @param showUpTime
	 *            A Long variable which contains the time the screen will be
	 *            shown to a client.
	 */
	public UltimatumGameRequestScreen(String gameId, UltimatumGameParamObject parameters, String screenId,
			Long showUpTime) {
		super(gameId, parameters, screenId, showUpTime);
		init();
		setCustomScreenParameter(parameters);

	}

	private static final long serialVersionUID = 7689598783599843247L;

	private String infoText;
	private String inputValue;
	private Dimension screenSize;

	// label
	private JLabel jlabelInformationText;
	private JLabel jLabelInput;
	private JLabel jLabelInput2;

	// textfield
	private JTextField jTextFieldInput;

	// Panels
	private JPanel infoPanel;
	private JPanel inputPanel;

	// Button
	private JButton sendButton;

	private Dimension panelSize;

	private void init() {

		screenSize = thisScreen.getScreenSize();
		panelSize = new Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight() / 2);

		initInfoPanel();
		initInputPanel();
	}

	private void initInputPanel() {

		inputPanel = new JPanel(new GridBagLayout());
		inputPanel.setPreferredSize(panelSize);
		inputPanel.setBackground(Color.LIGHT_GRAY);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.ipadx = 10; // internal Padding
		constraints.ipady = 10;
		// constraints.insets = new Insets(5, 5, 5, 5); external Padding

		jLabelInput = new JLabel("Offer to other Agent:");
		constraints.gridx = 0;
		constraints.gridy = 0;
		inputPanel.add(jLabelInput, constraints);

		jTextFieldInput = new JTextField(5);
		constraints.gridx = 1;
		constraints.gridy = 0;
		inputPanel.add(jTextFieldInput, constraints);

		jLabelInput2 = new JLabel("  Euro.");
		constraints.gridx = 2;
		constraints.gridy = 0;
		inputPanel.add(jLabelInput2, constraints);

		this.add(inputPanel, BorderLayout.SOUTH);

		// button
		sendButton = new JButton("OK");
		constraints.insets = new Insets(0, 100, 0, 0); // external padding
		constraints.gridx = 4;
		constraints.gridy = 0;
		inputPanel.add(sendButton, constraints);

		sendButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				inputValue = jTextFieldInput.getText();
				try {
					Integer.parseInt(inputValue);

					UltimatumGameResponseObject parameters = new UltimatumGameResponseObject();
					parameters.setInputValue(inputValue);
					thisScreen.sendResponseAndWait(parameters);
				} catch (NumberFormatException e2) {
					JOptionPane.showMessageDialog(null, "You are only allowed to enter integers!", "Hint",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

	}

	private void initInfoPanel() {

		infoPanel = new JPanel(new GridBagLayout());
		infoPanel.setPreferredSize(panelSize);
		infoPanel.setBackground(Color.WHITE);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.ipadx = 10;
		constraints.ipady = 10;

		jlabelInformationText = new JLabel(infoText);
		constraints.gridx = 0;
		constraints.gridy = 0;
		infoPanel.add(jlabelInformationText, constraints);

		this.add(infoPanel);

	}

	/**
	 * This method sets the custom screen parameter, an info text that will be
	 * shown to the client.
	 * 
	 * @param parameter
	 *            the UltimatumGameParamObject parameter
	 */
	public void setCustomScreenParameter(UltimatumGameParamObject parameter) {

		this.infoText = (String) parameter.getInfoText();
		jlabelInformationText.setText(infoText);
		jlabelInformationText.revalidate();

	}
}
