package edu.kit.exp.server.gui.infotab;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import edu.kit.exp.common.Constants;
import edu.kit.exp.common.LogHandler;

public class InfoTab extends JPanel {

	private static final long serialVersionUID = 1736348150136510143L;

	public InfoTab() {
		setBackground(UIManager.getColor("Button.background"));
		setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(InfoTab.class.getResource("/edu/kit/exp/common/resources/brownie_logo.png")));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				goToHomepage();
			}
		});
		lblNewLabel_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		add(lblNewLabel_1, BorderLayout.CENTER);

		JLabel lblNewLabel = new JLabel("<html><center>brownie v" + String.format("%1$.1f", Constants.BROWNIE_VERSION) + "<br>Visit us at <i>" + Constants.IISM_HOMEPAGE_URL + "</i></center></html>");
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				goToHomepage();
			}
		});
		lblNewLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		add(lblNewLabel, BorderLayout.SOUTH);

	}

	private void goToHomepage() {
		try {
			Desktop.getDesktop().browse(new URI(Constants.IISM_HOMEPAGE_URL));
		} catch (IOException | URISyntaxException e) {
			LogHandler.printException(e);
		}
	}

}
