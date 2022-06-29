package edu.kit.exp.impl.browserExperiment.client;

import edu.kit.exp.client.gui.ClientGuiController;
import edu.kit.exp.client.gui.MainFrame;
import edu.kit.exp.client.gui.components.WebBrowser;
import edu.kit.exp.client.gui.screens.Screen;

import java.awt.*;
import java.rmi.RemoteException;

/**
 * The Class BrowserExperimentInitialScreen provides a screen that is shown to
 * the client initially after an experiment period was started.
 * 
 * @see Screen
 */
public class BrowserExperimentInitialScreen extends Screen {

	/** duration of the screen shown in milliseconds */
	private static final int DURATION_OF_SCREEN = 10000;
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5532903241165693886L;

	/**
	 * Instantiates a new browser experiment initial screen.
	 * 
	 * @param gameId
	 *            A String that represents the ID of the game.
	 * @param parameter
	 *            A ParamObject which contains screen parameters.
	 * @param screenId
	 *            A String which contains the ID of the screen.
	 * @param showUpTime
	 *            A Long variable which indicates how long the screen is shown
	 *            to the client.
	 * @throws RemoteException
	 */

	public BrowserExperimentInitialScreen(String gameId, ParamObject parameter, String screenId, Long showUpTime) {
		super(gameId, parameter, screenId, 30L);

		/* Create WebBrowser component with user input logging */
		// WebBrowser browser = new WebBrowser(true);
		/* Create WebBrowser component without user input logging */
		WebBrowser browser = new WebBrowser(true);
		Rectangle bounds = MainFrame.getCurrentScreen().getBounds();
		browser.setBounds(bounds);

		/* Set screen layout to null => full screen web browser */
		setLayout(null);
		add(browser);

		/* Load initial web page */
		browser.loadURL("www.iism.kit.edu/im");

		/*
		 * Send client respone to end the experiment after a certain amount of
		 * time
		 */
		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				ClientGuiController.getInstance().sendClientResponse(parameter, gameId, screenId);
			}
		}, DURATION_OF_SCREEN);
	}

}