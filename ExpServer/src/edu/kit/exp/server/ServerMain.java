package edu.kit.exp.server;

import java.rmi.RemoteException;

import edu.kit.exp.server.gui.mainframe.MainFrameController;

/**
 * The class ServerMain initializes the server GUI.
 */
public class ServerMain {

	/**
	 * This method instantiates a Controller for the GUI and initializes it by
	 * calling the {@link MainFrameController#initApplication()} method.
	 * 
	 * @param args
	 *            String[]
	 * @throws RemoteException
	 *             If connection failed.
	 */
	public static void main(String[] args) throws RemoteException {
		MainFrameController guiController = MainFrameController.getInstance();
		guiController.initApplication();
	}
}