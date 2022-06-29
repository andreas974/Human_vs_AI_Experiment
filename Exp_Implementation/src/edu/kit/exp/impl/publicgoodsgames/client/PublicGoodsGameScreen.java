package edu.kit.exp.impl.publicgoodsgames.client;

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

/**
 * Screen shown to subject for input of amount of money subject wants to
 * contribute
 */
public class PublicGoodsGameScreen extends Screen {

	private static final long serialVersionUID = 3376365698157056572L;
	private double money;
	private String message;

	private JTextField constributMoney;

	private JButton accept;

	public PublicGoodsGameScreen(String gameId, PublicGoodsGameParamObject parameter, String screenId,
			Long showUpTime) {
		super(gameId, parameter, screenId, showUpTime);

		message = parameter.getMessage();
		money = parameter.getMoney();

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

		constributMoney = new JTextField(5);
		constraints2.gridx = 1;
		constraints2.gridy = 0;
		inputPanel.add(constributMoney, constraints2);

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
				String input = constributMoney.getText();
				try {

					double inputval = Double.parseDouble(input);

					// maximal 2 decimal places are permitted
					String[] parts = input.split("\\.");
					if (parts.length == 2) {
						if (parts[1].length() > 2) {
							throw new NumberFormatException();
						}
					}

					// input must be in the interval of 0 and the amount of
					// money the user has
					if (inputval > money || inputval < 0) {
						throw new Exception();
					}

					PublicGoodsGameParamObject inputinfo = new PublicGoodsGameParamObject();
					inputinfo.setInput(inputval);
					sendResponseAndWait(inputinfo);

				} catch (NumberFormatException e) {
					LogHandler.popupException(e,
							"You are only allowed to enter numbers and it musn't have more than 2 decimal places!");
				} catch (Exception e) {
					LogHandler.popupException(e, "Your input needs to be in the interval of 0 and " + money + " !");
				}

			}

		});

	}

}
