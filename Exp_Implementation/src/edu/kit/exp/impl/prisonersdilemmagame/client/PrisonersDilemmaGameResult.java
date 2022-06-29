package edu.kit.exp.impl.prisonersdilemmagame.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import edu.kit.exp.client.gui.screens.Screen;

/**
 * Screen showing the result of a period
 */
public class PrisonersDilemmaGameResult extends Screen {

	private static final long serialVersionUID = 4492716312907812187L;

	private String message;
	private JLabel jlabel;

	public PrisonersDilemmaGameResult(String gameId, PrisonersDilemmaGameParamObject parameter, String screenId,
			Long showUpTime) {
		super(gameId, parameter, screenId, showUpTime);
		setLogDisplayEvent(true);

		message = parameter.getMessage();
		setLayout(new BorderLayout(0, 0));
		jlabel = new JLabel(message);
		jlabel.setFont(new Font("Tahoma", Font.BOLD, 17));

		setBorder(new EmptyBorder(10, 10, 10, 10));
		setBackground(Color.WHITE);

		setLayout(new BorderLayout(0, 0));

		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		infoPanel.setBackground(Color.WHITE);
		add(infoPanel, BorderLayout.CENTER);
		infoPanel.setLayout(new BorderLayout(0, 0));
		infoPanel.add(jlabel);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
	}

}
