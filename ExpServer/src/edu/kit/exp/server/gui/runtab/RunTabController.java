package edu.kit.exp.server.gui.runtab;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;

import edu.kit.exp.common.LogHandler;
import edu.kit.exp.server.gui.mainframe.MainFrame;
import edu.kit.exp.server.run.RunManager;
import edu.kit.exp.server.run.RunStateLogger;
import edu.kit.exp.server.run.SessionRunException;

/**
 * This class represents the controller for the run tab. It extends Observable
 * and implements Observer.
 * 
 * @see Observable
 * @see Observer
 */
public class RunTabController extends Observable implements Observer {

	/** The instance. */
	private static RunTabController instance;

	/** The run manager. */
	private RunManager runManager;

	/** The run state logger. */
	private RunStateLogger runStateLogger;

	/** The state message. */
	private String stateMessage;

	/**
	 * This constructor instantiates a new run tab controller.
	 */
	private RunTabController() {
		runManager = RunManager.getInstance();
		runStateLogger = RunStateLogger.getInstance();
		runStateLogger.addObserver(this);
	}

	/**
	 * This method returns the only instance of this class.
	 * 
	 * @return a single instance of RunTabController
	 */
	public static RunTabController getInstance() {
		if (instance == null) {
			instance = new RunTabController();
		}
		return instance;
	}

	/**
	 * This method adds an observer to this controller.
	 * 
	 * @param o
	 *            The Observer.
	 */
	@Override
	public void addObserver(Observer o) {
		super.addObserver(o);
	}

	/**
	 * This method notifies observers of this class with the appended message.
	 * 
	 * @param message
	 *            A String message.
	 */
	public void appendMessage(String message) {

		stateMessage = message;

		if (countObservers() > 0) {
			setChanged();
			notifyObservers(stateMessage);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (countObservers() > 0) {
			setChanged();
			notifyObservers(arg);
		}
	}

	/**
	 * This method starts a session if all subjects are connected to the server.
	 * 
	 * @throws SessionRunException
	 *             If the session configuration is incomplete.
	 * @return
	 */
	public boolean startSession() throws SessionRunException {

		if (RunStateLogger.getInstance().getAllSubjectsConnected()) {
			runManager.startSession();
			return true;
		} else {
			JOptionPane.showMessageDialog(MainFrame.getInstance(), "Session can not be started until all subjects are connected!", "Please Wait", JOptionPane.WARNING_MESSAGE);
            LogHandler.printInfo("Session can not be started until all subjects are connected!");
            return false;
		}
	}
}
