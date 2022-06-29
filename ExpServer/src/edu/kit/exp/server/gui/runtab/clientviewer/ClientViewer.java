package edu.kit.exp.server.gui.runtab.clientviewer;

import edu.kit.exp.common.ClientStatus;
import edu.kit.exp.server.gui.mainframe.MainFrame;
import edu.kit.exp.server.gui.mainframe.MainFrameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.Timer;

/**
 * Client Viewer element for the run tab that shows certain live information of
 * the client
 */
public class ClientViewer extends JPanel {

	private static final long serialVersionUID = 1814496710698003106L;
	private static final URL IMAGE_RESOURCE = MainFrame.class.getResource("/edu/kit/exp/server/resources/calculate_playoffs_1.jpg");

	private static ClientViewer instance = new ClientViewer();
	private ClientViewerController controller;
	private Timer tableUpdater;
	private JPanel elementsPanel;

	public static ClientViewer getInstance() {
		return instance;
	}

	private ClientViewer() {
		controller = ClientViewerController.getInstance();
		tableUpdater = new Timer(true);
		tableUpdater.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				updateTable();

			}
		}, 1000, 1000);

		setLayout(new BorderLayout(0, 0));
		elementsPanel = new JPanel();
		elementsPanel.setLayout(new BoxLayout(elementsPanel, BoxLayout.PAGE_AXIS));
		JScrollPane elementsScrollPane = new JScrollPane(elementsPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(elementsScrollPane, BorderLayout.CENTER);
		
		JButton buttonCalcPayoff = new JButton("Calculate Payoffs");
		buttonCalcPayoff.setIcon(new ImageIcon(IMAGE_RESOURCE));
		buttonCalcPayoff.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrameController.getInstance().openPayoffDialog();

			}
		});
		this.add(buttonCalcPayoff, BorderLayout.SOUTH);
	}

	private void updateTable() {
		elementsPanel.removeAll();

		List<ClientStatus> stati = new ArrayList<ClientStatus>(controller.getAllClientStatus());
		Collections.sort(stati);
		for (ClientStatus clientStatus : stati) {
			elementsPanel.add(new ClientViewerElement(clientStatus));
			elementsPanel.add(Box.createRigidArea(new Dimension(1, 1)));
		}
		
		elementsPanel.updateUI();
	}
}
