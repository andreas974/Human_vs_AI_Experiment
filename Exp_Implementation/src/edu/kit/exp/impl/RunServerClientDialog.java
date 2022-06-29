package edu.kit.exp.impl;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import edu.kit.exp.client.ClientMain;
import edu.kit.exp.client.gui.MainFrame;
import edu.kit.exp.common.LogHandler;
import edu.kit.exp.server.ServerMain;

public class RunServerClientDialog extends JFrame {

	private static final long serialVersionUID = 6804044437454555192L;

	public RunServerClientDialog() {
		this.setBounds(50, 50, 299, 177);
		this.setTitle("brownie");
		this.setIconImage(
				new ImageIcon(MainFrame.class.getResource("/edu/kit/exp/common/resources/brownie_logo_noname.png"))
						.getImage());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setLayout(new GridLayout(0, 2));

		JButton buttonServer = new JButton("Start Server");
		buttonServer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ServerMain.main(new String[0]);
					dispose();
				} catch (RemoteException e1) {
					LogHandler.printException(e1);
				}
			}
		});
		this.add(buttonServer);

		JButton buttonClient = new JButton("Start Client");
		buttonClient.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ClientMain.main(new String[0]);
					dispose();
				} catch (RemoteException e1) {
					LogHandler.printException(e1);
				}
			}
		});
		this.add(buttonClient);
	}

}
