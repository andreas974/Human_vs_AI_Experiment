package edu.kit.exp.impl.trustgame.client;

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
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.kit.exp.client.gui.screens.Screen;
import edu.kit.exp.common.LogHandler;

public class TrustGameScreen extends Screen {

	private static final long serialVersionUID = -7084145626823072617L;

	private JTextField textfield;
	private JButton accept;

	private String message;
	private int money;

	public TrustGameScreen(String gameId, TrustGameParamObject parameter, String screenId, Long showUpTime) {
		super(gameId, parameter, screenId, showUpTime);

		message = parameter.getMessage();
		money = parameter.getInput();

		Dimension screensize = getScreenSize();
		Dimension panelSize = new Dimension((int) screensize.getWidth(), (int) screensize.getHeight() / 2);

		// Display for Message
		JPanel infoPanel = new JPanel(new GridBagLayout());
		infoPanel.setPreferredSize(panelSize);
		infoPanel.setBackground(Color.WHITE);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.ipadx = 10;
		constraints.ipady = 10;

		JLabel jlabelInformationText = new JLabel(message);
		constraints.gridx = 0;
		constraints.gridy = 0;
		infoPanel.add(jlabelInformationText, constraints);

		this.add(infoPanel);

		// Display for Input
		JPanel inputPanel = new JPanel(new GridBagLayout());
		inputPanel.setPreferredSize(panelSize);
		inputPanel.setBackground(Color.LIGHT_GRAY);

		GridBagConstraints constraints2 = new GridBagConstraints();
		constraints2.ipadx = 10;
		constraints2.ipady = 10;

		JLabel jLabelInput = new JLabel("Amount of money to share: ");
		constraints2.gridx = 0;
		constraints2.gridy = 0;
		inputPanel.add(jLabelInput, constraints2);

		textfield = new JTextField(5);
		constraints2.gridx = 1;
		constraints2.gridy = 0;
		inputPanel.add(textfield, constraints2);

		JLabel jLabelInput2 = new JLabel("  Euro.");
		constraints.gridx = 2;
		constraints.gridy = 0;
		inputPanel.add(jLabelInput2, constraints);

		this.add(inputPanel, BorderLayout.SOUTH);

		// Button Implementation
		accept = new JButton("OK");
		constraints.insets = new Insets(0, 100, 0, 0); // external padding
		constraints.gridx = 4;
		constraints.gridy = 0;
		inputPanel.add(accept, constraints);
		accept.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String input = textfield.getText();
				try {

					int inputval = Integer.parseInt(input);

					if (inputval > money || inputval < 0) {
						throw new Exception();
					}

					TrustGameParamObject inputinfo = new TrustGameParamObject();
					inputinfo.setInput(inputval);
					sendResponseAndWait(inputinfo);

				} catch (NumberFormatException e) {
					LogHandler.popupException(e, "You are only allowed to enter numbers!");
				} catch (Exception e) {
					LogHandler.popupException(e, "Your input needs to be in the interval of 0 and " + money + " !");
				}

			}

		});
	}

}
