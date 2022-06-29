package edu.kit.exp.server.run;

import java.util.Observable;
import java.util.Observer;

import edu.kit.exp.common.Constants;
import edu.kit.exp.server.run.RunStateLogEntry.ServerEvent;
import org.joda.time.DateTime;

/**
 * This thread save class indicates the state of a session. </br> The RunTab GUI
 * observes this class to inform the experimenter about all events in a session
 * run, i.e. errors or progress messages
 *
 */
public class RunStateLogger extends Observable {

	/** The instance. */
	private static RunStateLogger instance;

	/** The all subjects connected. */
	private boolean allSubjectsConnected = false;

	/** The continue session flag. */
	private boolean continueSessionFlag = false;

	private String stateMessage;

	/**
	 * This constructor instantiates a new run state logger.
	 */
	private RunStateLogger() {

	}

	/**
	 * Returns the only instance of this class.
	 *
	 * @return a single instance of RunStateLogger
	 */
	public synchronized static RunStateLogger getInstance() {

		if (instance == null) {
			instance = new RunStateLogger();
		}

		return instance;
	}

	/**
	 * This method adds an observer to this class.
	 *
	 * @param o
	 *            the Observer
	 */
	@Override
	public void addObserver(Observer o) {
		super.addObserver(o);
	}

	/**
	 * This method creates the output message.
	 *
	 * @param logEntry
	 *            A RunStateLogEntry which contains the output message.
	 */
	public synchronized void createOutputMessage(RunStateLogEntry logEntry) {
		if (countObservers() > 0) {
			setChanged();
			notifyObservers(logEntry);
		}

	}

	public synchronized void  createOutputMessage2(String message) {

		DateTime dt = new DateTime();
		stateMessage = dt.toString(Constants.TIME_STAMP_FORMAT)+": "+message + System.lineSeparator();

		if (countObservers() > 0) {
			setChanged();
			notifyObservers(stateMessage);
		}

	}

	/**
	 * This method creates the server output message.
	 *
	 * @param name
	 *            Message containing status information.
	 * @param value
	 *            Message containing log information.
	 */
	public synchronized void createServerOutputMessage(String event, String name, String value) {
		createServerOutputMessage(event, name, value, false);
	}

	/**
	 * This method creates the server output message.
	 *
	 * @param status
	 *            Message containing status information.
	 * @param info
	 *            Message containing log information.
	 */
	public synchronized void createServerOutputMessage(ServerEvent event, String name, String value) {
		createServerOutputMessage(event.toString(), name, value);
	}

	/**
	 * This method creates the server output message.
	 *
	 * @param status
	 *            Message containing status information.
	 * @param info
	 *            Message containing log information.
	 * @param overwriteLastEntry
	 * 			  Last Message is automatically overwritten.
	 */
	public synchronized void createServerOutputMessage(String event, String name, String value, boolean overwriteLastEntry) {
		RunStateLogEntry runStateLogEntry = new RunStateLogEntry("Server", event, name, value);
		runStateLogEntry.setOverwriteLatestEntry(overwriteLastEntry);
		createOutputMessage(runStateLogEntry);
	}

	/**
	 * This method creates the error output message.
	 *
	 * @param message
	 *            Message containing error information.
	 */
	public synchronized void createServerErrorMessage(Exception e) {
		createServerOutputMessage("-- ERROR --", e.getClass().getSimpleName(), e.getMessage());
	}

	/**
	 * This method checks if all subjects are connected.
	 *
	 * @return true, if all subjects successfully connected
	 */
	public synchronized boolean areAllSubjectsConnected() {
		return allSubjectsConnected;
	}

	/**
	 * This method which outputs that all subjects are connected if it is true.
	 *
	 * @param allSubjectsConnected
	 *            A boolean which contains information about whether all
	 *            subjects are connected.
	 */
	public void setAllSubjectsConnected(boolean allSubjectsConnected) {
		this.allSubjectsConnected = allSubjectsConnected;
	}

	/**
	 * This method returns the status of the connection, specifically if all
	 * subjects are connected.
	 *
	 * @return True if all subjects are connected.
	 */
	public synchronized boolean getAllSubjectsConnected() {
		return allSubjectsConnected;
	}

	/**
	 * This method gets a flag which shows if the session can be continued or
	 * not.
	 *
	 * @return the continue session flag
	 */
	public synchronized boolean getContinueSessionFlag() {

		return continueSessionFlag;
	}

	/**
	 * This method sets a flag which shows if the session can be continued or
	 * not.
	 *
	 * @param continueSessionFlag
	 *            A boolean that contains the new continue session flag.
	 */
	public synchronized void setContinueSessionFlag(boolean continueSessionFlag) {
		this.continueSessionFlag = continueSessionFlag;
	}
}
