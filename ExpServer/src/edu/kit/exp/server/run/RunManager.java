package edu.kit.exp.server.run;

import edu.kit.exp.common.LogHandler;
import edu.kit.exp.common.communication.ConnectionException;
import edu.kit.exp.common.communication.MessageDeliveryThread;
import edu.kit.exp.server.communication.ClientMessage;
import edu.kit.exp.server.communication.ServerCommunicationManager;
import edu.kit.exp.server.communication.ServerImpl;
import edu.kit.exp.server.jpa.entity.Session;
import edu.kit.exp.server.structure.SessionManagement;
import edu.kit.exp.server.structure.StructureManagementException;

import java.rmi.RemoteException;

/**
 * This class handles the run of a session.
 * 
 */
public class RunManager {

	/** The instance. */
	private static RunManager instance;

	/** The session creator. */
	private SessionCreator sessionCreator;

	/** The subject registration. */
	private SubjectRegistration subjectRegistration;

	/** The session management. */
	private SessionManagement sessionManagement;

	/** The current session. */
	private Session currentSession;

	/** The continue session flag. */
	private Boolean continueSessionFlag;

	/**
	 * This method gets the single instance of RunManager.
	 * 
	 * @return a single instance of RunManager
	 */
	public static RunManager getInstance() {

		if (instance == null) {
			instance = new RunManager();
		}

		return instance;
	}

	/**
	 * This constructor instantiates a new run manager.
	 */
	private RunManager() {
		sessionCreator = SessionCreator.getInstance();
		subjectRegistration = SubjectRegistration.getInstance();
		sessionManagement = SessionManagement.getInstance();
	}

	/* Methods */

	/**
	 * This is the main method to start a Session.
	 * 
	 * @param session
	 *            A {@link edu.kit.exp.server.jpa.entity.Session Session}
	 *            variable which contains the current session.
	 * @param continueSession
	 *            A boolean that indicates if the session is continued or if a
	 *            new session has to be started.
	 * @throws SessionRunException
	 *             If the session configuration is incomplete.
	 * @throws ExistingDataException
	 *             If the session was started before.
	 * @throws ConnectionException
	 *             If the network sever can not be started.
	 */
	public void initializeSession(Session session, Boolean continueSession) throws SessionRunException, ExistingDataException, ConnectionException {

		this.continueSessionFlag = continueSession;

		if (session.getStarted() && !continueSession) {
			throw new ExistingDataException("Session was started before!");
		}

		if (continueSession) {
			RunStateLogger.getInstance().setContinueSessionFlag(true);
		}

		// Interrupted session has to be continued
		if (!session.getStarted()) {

			// Reset doneFlag on SequenceElements
			try {
				session = sessionCreator.resetTreatmentElementFlags(session);
			} catch (StructureManagementException e) {
				throw new SessionRunException(e.getMessage());
			}

			// Create new session data
			sessionCreator.checkIfRunConditionsMet(session);
			session = sessionCreator.createSubjects(session);
			session = sessionCreator.matchPeriodsToTreatments(session);
			session = sessionCreator.defineSubjectGroups(session);
		}

		RunStateLogger.getInstance().createServerOutputMessage(RunStateLogEntry.ServerEvent.INITIALIZING, "Session state", "Session successfully initialised!");
        LogHandler.printInfo("Session successfully initialised!");

		startNetworkServer(session);

		RunStateLogger.getInstance().createServerOutputMessage(RunStateLogEntry.ServerEvent.INITIALIZING, "Session state", "Server ready and waiting for Client connections!");
        LogHandler.printInfo("Server ready and waiting for Client connections!");

		currentSession = session;
	}

	/**
	 * A call to this method enables the server application to communicate via
	 * the network and enables the clients to connect to the server. This method
	 * starts the RMI interface, a queue for incoming messages and the
	 * MessageDeliveryThread.
	 * 
	 * @param session
	 *            A {@link edu.kit.exp.server.jpa.entity.Session Session}
	 *            variable which contains the current session.
	 * @throws ConnectionException
	 *             If the network server can not be started.
	 */
	private void startNetworkServer(Session session) throws ConnectionException {

		try {

			subjectRegistration.prepareClientToSubjectMatching(session);

			// Needed for RMI settings
			System.setProperty("java.security.policy", "file:./java.policy");

			// The messageDeliveryThread will rout all client messages to its
			// destination objects. It is implemented as daemon thread so it
			// will terminate at
			// application end.
			@SuppressWarnings("unused")
			MessageDeliveryThread<ClientMessage> messageDeliveryThread = new MessageDeliveryThread<>("QueueThread", ServerCommunicationManager.getInstance().getIncommingMessageQueue(), ServerCommunicationManager.getInstance().getMessageReceiver());

			// Start the RMI Service of the server
			@SuppressWarnings("unused")
			ServerImpl serverImpl = ServerImpl.getInstance();

		} catch (RandomGeneratorException e) {
			throw new ConnectionException(e.getMessage());
		} catch (RemoteException e) {
		    LogHandler.printException(e, "Can not start network server");
			throw new ConnectionException("Can not start network server. " + e.getMessage());
		}
	}

	/**
	 * This methods starts a session by starting a new SessionThread.
	 * 
	 * @throws SessionRunException
	 *             If the session configuration is incomplete.
	 */
	public void startSession() throws SessionRunException {

		// Make sure to have the most actual data and set session started!
		try {
			currentSession = sessionManagement.findSession(currentSession.getIdSession());
			currentSession.setStarted(true);
			currentSession = sessionManagement.updateSession(currentSession);
		} catch (StructureManagementException e) {
			throw new SessionRunException(e.getMessage());
		}

		/* Start session thread */
		@SuppressWarnings("unused")
		SessionThread sessionThread = new SessionThread("PeriodThread", currentSession, QueueManager.getSessionQueueInstance(), continueSessionFlag, QueueManager.getDataTransmissionQueueInstance());
		RunStateLogger.getInstance().createServerOutputMessage(RunStateLogEntry.ServerEvent.INITIALIZING, "Session state", "Session sucsessfully started!");
	}

	public Session getCurrentSession() {
		return currentSession;
	}

}
