package edu.kit.exp.impl.prisonersdilemmagame.client;

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

import edu.kit.exp.client.gui.screens.Screen;

/**
 * Screen shown to subject for input of whether to betray or to stay silent
 */
public class PrisonersDilemmaGameScreen extends Screen {

	private static final long serialVersionUID = -1695030941667215770L;

	private String message;

	// Buttons
	private JButton silent;
	private JButton betray;

	public PrisonersDilemmaGameScreen(String gameId, PrisonersDilemmaGameParamObject parameter, String screenId,
			Long showUpTime) {
		super(gameId, parameter, screenId, showUpTime);

		message = parameter.getMessage();

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

		// Display for Buttons
		JPanel inputPanel = new JPanel(new GridBagLayout());
		inputPanel.setPreferredSize(panelSize);
		inputPanel.setBackground(Color.LIGHT_GRAY);

		GridBagConstraints constraints2 = new GridBagConstraints();
		constraints2.ipadx = 10;
		constraints2.ipady = 10;
		this.add(inputPanel, BorderLayout.SOUTH);

		// Betray-Button Implementation
		betray = new JButton("Betray");
		constraints.insets = new Insets(0, 100, 0, 0);
		constraints.gridx = 4;
		constraints.gridy = 0;
		inputPanel.add(betray, constraints);
		betray.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				PrisonersDilemmaGameParamObject inputinfo = new PrisonersDilemmaGameParamObject();
				inputinfo.setInput("BETRAY");
				sendResponseAndWait(inputinfo);

			}

		});

		// Silent-Button Implementation
		silent = new JButton("Stay Silent");
		constraints.insets = new Insets(0, 100, 0, 0);
		constraints.gridx = 12;
		constraints.gridy = 0;
		inputPanel.add(silent, constraints);
		silent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				PrisonersDilemmaGameParamObject inputinfo = new PrisonersDilemmaGameParamObject();
				inputinfo.setInput("SILENT");
				sendResponseAndWait(inputinfo);

			}

		});
	}

}
