package edu.kit.exp.client.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import edu.kit.exp.client.gui.screens.DefaultEndScreen;

public class WindowClosingAdapter extends WindowAdapter {
	
	private static final String WINDOW_TITEL = "Exit";
	private static final String EXIT_MESSAGE_DURING_TRANSMISSION = "Transmission is not finished yet. Really exit?";
	private static final String EXIT_MESSAGE_DEFAULT = "Really exit?";

	@Override
	public void windowClosing(WindowEvent e) {
		if (isTransmissionFinished()) {
			closeClientWindow(e);
		} else {
			String exitMessage = EXIT_MESSAGE_DEFAULT;
			if (isDefaultEndScreenShowing()) {
				exitMessage = EXIT_MESSAGE_DURING_TRANSMISSION;
			}
			closeClientWindowWithDialog(e, exitMessage);
		}
	}

	private boolean isTransmissionFinished() {
		if (isDefaultEndScreenShowing() == false) { 
			return false;
		} else {
			return ((DefaultEndScreen.ParamObject) MainFrame.getCurrentScreen().getParameter()).isTransmissionFinished();
		}
	}

	private boolean isDefaultEndScreenShowing() {
		return MainFrame.getCurrentScreen().getClass().equals(DefaultEndScreen.class);
	}

	private void closeClientWindowWithDialog(WindowEvent e, String exitMessage) {
		int isYes = JOptionPane.showConfirmDialog(e.getWindow(), exitMessage, WINDOW_TITEL, JOptionPane.YES_NO_OPTION);
		if (isYes == JOptionPane.YES_OPTION) {
			closeClientWindow(e);
		}
	}

	private void closeClientWindow(WindowEvent e) {
		e.getWindow().dispose();
		System.exit(0);
	}
}