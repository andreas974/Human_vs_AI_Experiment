/*
package edu.kit.exp.server.run;

import edu.kit.exp.client.gui.screens.DefaultWelcomeScreen;
import edu.kit.exp.common.LogHandler;
import edu.kit.exp.common.sensor.SensorControlInitialize;
import edu.kit.exp.server.communication.ClientRegistrationMessage;
import edu.kit.exp.server.communication.ServerCommunicationManager;
import edu.kit.exp.server.communication.ServerMessageSender;
import edu.kit.exp.server.jpa.entity.*;
import edu.kit.exp.server.structure.SensorEntryManagement;
import edu.kit.exp.server.structure.StructureManagementException;
import edu.kit.exp.server.structure.SubjectManagement;

import javax.naming.CommunicationException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

*/
/**
 * This class matches clients to subjects.
 *
 *//*


// !!! This class has been modified to allow for human/agent matching in
// UpstreamCompetition experiment !!!
public class SubjectRegistration_old {

	*/
/** The random number generator. *//*

	private RandomNumberGenerator randomNumberGenerator;

	*/
/** The run state logger. *//*

	private RunStateLogger runStateLogger;

	*/
/** The subject management. *//*

	private SubjectManagement subjectManagement;

	private RunManager runManager;

	*/
/** The instance. *//*

	private static SubjectRegistration_old instance;

	*/
/** The subject list. *//*

	private ArrayList<Subject> subjectList;

	*/
/** The number of subjects. *//*

	private int numberOfSubjects;

	*/
/** The number of registered subjects. *//*

	private int numberOfRegisteredSubjects;

	*/
/** The random subject numbers. *//*

	private ArrayList<Integer> randomSubjectNumbers;

	*/
/** The list of connected clients. *//*

	private ArrayList<String> listOfConnectedClients;

	*/
/** The list of connected clients. *//*

	public ArrayList<String> getListOfConnectedClients() {
		return listOfConnectedClients;
	}

	*/
/**
	 * This constructor instantiates a new subject registration.
	 *//*

	private SubjectRegistration_old() {
		randomNumberGenerator = RandomNumberGenerator.getInstance();
		runStateLogger = RunStateLogger.getInstance();
		subjectManagement = SubjectManagement.getInstance();
		listOfConnectedClients = new ArrayList<>();
	}

	*/
/**
	 * This method gets the single instance of SubjectRegistration.
	 *
	 * @return the single instance of SubjectRegistration
	 *//*

	public synchronized static SubjectRegistration_old getInstance() {
		if (instance == null) {
			instance = new SubjectRegistration_old();
		}
		return instance;
	}

	*/
/**
	 * This method prepares the matching by e.g. checking if all subjects are
	 * connected.
	 *
	 * @param session
	 *            A {@link Session Session}
	 *            variable which contains the current session information.
	 *
	 * @throws RandomGeneratorException
	 *             If there were errors during the number generation process.
	 *//*

	void prepareClientToSubjectMatching(Session session) throws RandomGeneratorException {

		RunStateLogger.getInstance().setAllSubjectsConnected(false);
		subjectList = new ArrayList<>();

		for (Cohort cohort : session.getCohorts()) {
			subjectList.addAll(cohort.getSubjects());
		}

		numberOfSubjects = subjectList.size();

		if (!runStateLogger.getContinueSessionFlag()) {

			randomSubjectNumbers = randomNumberGenerator.generateNonRepeatingIntegers(0, numberOfSubjects - 1);
		}

	}

	*/
/**
	 * This method matches connecting clients to subjects.</br> If the session
	 * is (re)started all subjects are matched randomly.</br> If a session
	 * should be continued, all clients are matched to the same subject again.
	 *
	 * @param clientRegistrationMessage
	 *            A
	 *            {@link ClientRegistrationMessage
	 *            ClientRegistrationMessage} which registers a client.
	 *
	 * @throws CommunicationException
	 *             If the client registration failed or a client is unknown.
	 *//*

	public void registerSubject(ClientRegistrationMessage clientRegistrationMessage) throws CommunicationException {

		ServerMessageSender messageSender = ServerCommunicationManager.getInstance().getMessageSender();
		if (!runStateLogger.getContinueSessionFlag() && !runStateLogger.getAllSubjectsConnected()) {

			registerNewClient(clientRegistrationMessage, messageSender);

		} else {

			reconnectOrContinue(clientRegistrationMessage, messageSender);

		}
	}

	private void reconnectOrContinue(ClientRegistrationMessage clientRegistrationMessage, ServerMessageSender messageSender) throws CommunicationException {
		String clientId = clientRegistrationMessage.getClientId();
		boolean knownClient = false;
		Subject knownSubject = null;

		for (Subject subject : subjectList) {
			if (subject.getIdClient().equals(clientId)) {
				knownSubject = subject;
				knownClient = true;
				break;
			}
		}

		if (!knownClient) {
			throw new CommunicationException("Unknown client id!");
		} else {

			// Known Subject

			messageSender.registerClient(clientRegistrationMessage);

			if (listOfConnectedClients.contains(clientId)) {
				RunStateLogger.getInstance().createServerOutputMessage(RunStateLogEntry.ServerEvent.CLIENTUPDATE, "Client reconnected", clientId);
			} else {
				listOfConnectedClients.add(clientId);
				numberOfRegisteredSubjects++;
				RunStateLogger.getInstance().createServerOutputMessage(RunStateLogEntry.ServerEvent.INITIALIZING, "Client " + String.valueOf(numberOfRegisteredSubjects) + "/" + String.valueOf(numberOfSubjects) + " connected!", clientId);
			}

			if (numberOfRegisteredSubjects == numberOfSubjects) {
				RunStateLogger.getInstance().setAllSubjectsConnected(true);
			}

			try {
				messageSender.sendGeneralScreenMessage(knownSubject, DefaultWelcomeScreen.class.getName(), new DefaultWelcomeScreen.ParamObject());
			} catch (RemoteException e) {
				LogHandler.printException(e, "Client registration failed");
				throw new CommunicationException("Client registration failed! Cause:" + e.getMessage());
			}

		}
	}

	private void registerNewClient(ClientRegistrationMessage clientRegistrationMessage, ServerMessageSender messageSender) throws CommunicationException {
		String clientId = clientRegistrationMessage.getClientId();

		checkIfClientIdAlreadyRegistered(clientId);

		messageSender.registerClient(clientRegistrationMessage);

		int randomNumber = randomSubjectNumbers.get(numberOfRegisteredSubjects);
		Subject subject = subjectList.get(randomNumber);

		subject.setIdClient(clientId);
		Subject updatedSubject;
		try {
			updatedSubject = subjectManagement.updateSubject(subject);
		} catch (StructureManagementException e) {
			LogHandler.printException(e, "Client registration failed");
			throw new CommunicationException("Client registration failed! Cause:" + e.getMessage());
		}

		numberOfRegisteredSubjects++;
		RunStateLogger.getInstance().createServerOutputMessage(RunStateLogEntry.ServerEvent.INITIALIZING, "Client " + String.valueOf(numberOfRegisteredSubjects) + "/" + String.valueOf(numberOfSubjects) + " connected!", clientId);

		if (numberOfRegisteredSubjects == numberOfSubjects) {
			RunStateLogger.getInstance().setAllSubjectsConnected(true);
			RunStateLogger.getInstance().createServerOutputMessage(RunStateLogEntry.ServerEvent.INITIALIZING, "Client registration", "All Subjects connected! Press continue to start session.");
		}

		try {
			messageSender.sendGeneralScreenMessage(updatedSubject, DefaultWelcomeScreen.class.getName(), new DefaultWelcomeScreen.ParamObject());
		} catch (RemoteException e) {
			LogHandler.printException(e, "Client registration failed");
			throw new CommunicationException("Client registration failed! Cause:" + e.getMessage());
		}

		listOfConnectedClients.add(clientId);

		initializeSensors(messageSender);
	}

	private void checkIfClientIdAlreadyRegistered(String newClient) throws CommunicationException {
		for (String connectedClient : listOfConnectedClients) {
			if (connectedClient.equals(newClient)) {
				CommunicationException exception = new CommunicationException("Client with this ID already registered");
				LogHandler.printException(exception);
				throw exception;
			}
		}
	}

	private void initializeSensors(ServerMessageSender messageSender) {
		// Initialize sensors by sending the configuration object
		List<SensorEntry> sensorEntries = new ArrayList<>();
		if (runManager == null) {
			runManager = RunManager.getInstance();
		}
		Experiment experiment = runManager.getCurrentSession().getExperiment();
		SensorEntryManagement entryManagement = SensorEntryManagement.getInstance();
		try {
			sensorEntries = entryManagement.findAllSensorEntrysByExperiment(experiment);
		} catch (StructureManagementException e) {
			LogHandler.printException(e, "Could not find sensor entries");
		}
		for (SensorEntry entry : sensorEntries) {
			try {
				messageSender.sendSensorCommandToALL(new SensorControlInitialize(entry.getConfiguration(), entry.getFullyQualifiedName()));
			} catch (RemoteException e) {
				LogHandler.printException(e, "Could not send configuration message");
			}
		}
	}
}
*/
