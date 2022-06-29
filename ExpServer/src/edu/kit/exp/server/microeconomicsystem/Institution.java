package edu.kit.exp.server.microeconomicsystem;

import java.rmi.RemoteException;
import java.util.List;

import edu.kit.exp.client.gui.screens.Screen;
import edu.kit.exp.common.IScreenParamObject;
import edu.kit.exp.server.communication.ClientResponseMessage;
import edu.kit.exp.server.communication.ServerMessageSender;
import edu.kit.exp.server.jpa.entity.Membership;
import edu.kit.exp.server.jpa.entity.Period;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.jpa.entity.SubjectGroup;
import edu.kit.exp.server.jpa.entity.Treatment;
import edu.kit.exp.server.run.RunStateLogEntry;
import edu.kit.exp.server.run.RunStateLogger;

/**
 * This abstract class represents an economic institution.</br> An institution
 * defines the concrete procedure how a session is run.</br> During each period,
 * an instance of Institution is created for each SubjectGroup.</br> The
 * concrete implementation of an Institution has to be done by the experimenter.
 * 
 * @param <T>
 *            a generic variable which extends Environment
 * @see Environment
 */
public abstract class Institution<T extends Environment> {

	/** The environment. */
	protected T environment;

	/** The finished. */
	protected boolean finished = false;

	/** The memberships. */
	protected List<Membership> memberships;

	/** The subject group. */
	protected SubjectGroup subjectGroup;

	/** The message sender. */
	private ServerMessageSender messageSender;

	/** The game id. */
	private String gameId;

	/** The current treatment. */
	private Treatment currentTreatment;

	/** The current period. */
	private Period currentPeriod;

	/**
	 * This method creates an Institution.
	 * 
	 * @param environment
	 *            The environment the economic institution interacts with within
	 *            the economic system.
	 * @param memberships
	 *            A list of memberships.
	 * @param messageSender
	 *            A ServerMessageSender for sending messages to clients.
	 * @param gameId
	 *            A String which contains the ID of the game. This helps the
	 *            server to route messages to the right SubjectGroup.
	 * @see edu.kit.exp.server.jpa.entity.Membership
	 */
	public Institution(T environment, List<Membership> memberships, ServerMessageSender messageSender, String gameId) {
		super();
		this.environment = environment;
		this.memberships = memberships;
		this.setMessageSender(messageSender);
		this.gameId = gameId;

		this.setSubjectGroup(memberships.get(0).getSubjectGroup());
	}

	/**
	 * This method is called to start an institution.
	 * 
	 * @throws Exception
	 */
	public abstract void startPeriod() throws Exception;

	/**
	 * This method processes client response messages.
	 * 
	 * @param msg
	 *            A ClientResponseMessage which should be processes by the
	 *            server.
	 * @throws Exception
	 */
	public abstract void processMessage(ClientResponseMessage msg) throws Exception;

	/**
	 * This method ends the institution.
	 * 
	 * @throws Exception
	 */
	protected abstract void endPeriod() throws Exception;

	/**
	 * This method triggers, that a given screen is shown to the specified
	 * client.</br> The Institution will wait until the subject has entered some
	 * requested input.</br> Make sure the screen takes some input from the
	 * subject
	 * 
	 * @param subject
	 *            The Subject that should see the screen.
	 * @param screenId
	 *            A Screen variable which contains the global screen ID.
	 * @param parameter
	 *            The IScreenParamObject custom parameters of that screen.
	 * @throws Exception
	 */
	protected void showScreen(Subject subject, Class<? extends Screen> screenId, IScreenParamObject parameter) throws Exception {
		showScreen(subject.getIdClient(), screenId, parameter);
	}

	/**
	 * This method shows the defined screens to subjects.
	 * 
	 * @param clientId
	 *            A String which contains the ID of a client where the screen
	 *            should be shown.
	 * @param screenId
	 *            A Screen variable which contains the global screen ID.
	 * @param parameter
	 *            The IScreenParamObject custom parameters of that screen.
	 * @throws Exception
	 */
	protected void showScreen(String clientId, Class<? extends Screen> screenId, IScreenParamObject parameter) throws Exception {
		getMessageSender().sendShowScreenMessage(clientId, screenId.getName(), parameter, gameId);
	}

	/**
	 * Shows the a screen at the client for the defined time.
	 * 
	 * @param subject
	 *            A Subject variable which represents the Subject where the
	 *            screen should be shown.
	 * @param screenId
	 *            A Screen variable which contains the global screen ID.
	 * @param parameter
	 *            The IScreenParamObject custom parameters of that screen.
	 * @param showUpTime
	 *            A Long variable which shows the duration the defined screen
	 *            will be visible. <b>IN MILLISECONDS</b>
	 * @throws RemoteException
	 *             If
	 */
	protected void showScreenWithDeadLine(Subject subject, Class<? extends Screen> screenId, IScreenParamObject parameter, Long showUpTime) throws RemoteException {
		showScreenWithDeadLine(subject.getIdClient(), screenId, parameter, showUpTime);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.kit.exp.server.microeconomicsystem.Institution.showScreenWithDeadLine
	 * (Subject subject, Class<? extends Screen> screenId, IScreenParamObject
	 * parameter, Long showUpTime)
	 */
	/**
	 * This method shows screen with a dead line.
	 * 
	 * @param clientId
	 *            A String which contains the ID of a client where the screen
	 *            should be shown.
	 * @param screenId
	 *            A Screen variable which contains the global screen ID.
	 * @param parameter
	 *            The IScreenParamObject custom parameters of that screen.
	 * @param showUpTime
	 *            A Long variable which shows the duration the defined screen
	 *            will be visible. <b>IN MILLISECONDS</b>
	 * @throws RemoteException
	 */
	protected void showScreenWithDeadLine(String clientId, Class<? extends Screen> screenId, IScreenParamObject parameter, Long showUpTime) throws RemoteException {
		getMessageSender().sendShowScreenMessage(clientId, screenId.getName(), parameter, gameId, showUpTime);
	}

	/**
	 * This method sends a ParamObject to a client without changing the current
	 * screen.
	 * 
	 * @param subject
	 *            A Subject variable which represents the Subject where the
	 *            screen should be shown.
	 * @param parameter
	 *            The IScreenParamObject custom parameters of that screen.
	 * @throws RemoteException
	 */
	protected void updateParamObject(Subject subject, IScreenParamObject parameter) throws RemoteException {
		updateParamObject(subject.getIdClient(), parameter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.kit.exp.server.microeconomicsystem.Institution.updateParamParameter
	 * (Subject subject, IScreenParamObject parameter)
	 */
	/**
	 * This method updates a param object.
	 * 
	 * @param clientId
	 *            A String which contains the ID of a client where the screen
	 *            should be shown.
	 * @param parameter
	 *            The IScreenParamObject custom parameters of that screen.
	 * @throws RemoteException
	 */
	protected void updateParamObject(String clientId, IScreenParamObject parameter) throws RemoteException {
		getMessageSender().sendParamObjectUpdate(clientId, parameter);
	}

	/**
	 * TODO
	 * 
	 */
	protected void updateRunStateLogger(String clientId, IScreenParamObject parameter) {
		RunStateLogger.getInstance().createOutputMessage(new RunStateLogEntry(clientId, RunStateLogEntry.ServerEvent.CLIENTUPDATE.toString(), "Screen update",  parameter.toString()));
	}

	/**
	 * This method checks if the Session is finished.
	 * 
	 * @return true, if the Session is finished, false otherwise
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * This method gets the SubjectGroup.
	 * 
	 * @return the subject group
	 */
	public SubjectGroup getSubjectGroup() {
		return subjectGroup;
	}

	/**
	 * This method sets the SubjectGroup.
	 * 
	 * @param subjectGroup
	 *            The new SubjectGroup.
	 * @see SubjectGroup
	 */
	public void setSubjectGroup(SubjectGroup subjectGroup) {
		this.subjectGroup = subjectGroup;
	}

	/**
	 * This method gets the environment.
	 * 
	 * @return the environment
	 */
	public T getEnvironment() {
		return environment;
	}

	/**
	 * This method sets the environment.
	 * 
	 * @param environment
	 *            The new environment.
	 */
	public void setEnvironment(T environment) {
		this.environment = environment;
	}

	/**
	 * This method gets the memberships.
	 * 
	 * @return the memberships
	 */
	public List<Membership> getMemberships() {
		return memberships;
	}

	/**
	 * This method sets the memberships.
	 * 
	 * @param memberships
	 *            The new memberships.
	 */
	public void setMemberships(List<Membership> memberships) {
		this.memberships = memberships;
	}

	/**
	 * This method gets the game ID.
	 * 
	 * @return the game ID
	 */
	public String getGameId() {
		return gameId;
	}

	/**
	 * This method sets the game ID.
	 * 
	 * @param gameId
	 *            The new game ID.
	 */
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	/**
	 * This method sets the current treatment.
	 * 
	 * @param currentTreatment
	 *            The new current treatment.
	 */
	public void setCurrentTreatment(Treatment currentTreatment) {
		this.currentTreatment = currentTreatment;
	}

	/**
	 * This method gets the current period.
	 * 
	 * @return the current period
	 */
	public Period getCurrentPeriod() {
		return currentPeriod;
	}

	/**
	 * This method sets the current period.
	 * 
	 * @param currentPeriod
	 *            The new current period.
	 */
	public void setCurrentPeriod(Period currentPeriod) {
		this.currentPeriod = currentPeriod;
	}

	/**
	 * This method gets the current treatment.
	 * 
	 * @return the current treatment
	 */
	public Treatment getCurrentTreatment() {
		return currentTreatment;
	}

	/**
	 * This method sets the message sender.
	 * 
	 * @param messageSender
	 *            The new ServerMessageSender.
	 */
	public void setMessageSender(ServerMessageSender messageSender) {
		this.messageSender = messageSender;
	}

	/**
	 * This method gets the message sender.
	 * 
	 * @return the message sender
	 */
	public ServerMessageSender getMessageSender() {
		return messageSender;
	}

}
