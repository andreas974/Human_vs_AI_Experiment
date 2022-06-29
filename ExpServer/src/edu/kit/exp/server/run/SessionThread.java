package edu.kit.exp.server.run;

import edu.kit.exp.client.gui.screens.DefaultEndScreen;
import edu.kit.exp.client.gui.screens.DefaultInfoScreen;
import edu.kit.exp.common.LogHandler;
import edu.kit.exp.common.sensor.SensorControlStartRecording;
import edu.kit.exp.common.sensor.SensorControlStopRecording;
import edu.kit.exp.server.communication.*;
import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.entity.*;
import edu.kit.exp.server.microeconomicsystem.Environment;
import edu.kit.exp.server.microeconomicsystem.EnvironmentFactory;
import edu.kit.exp.server.microeconomicsystem.Institution;
import edu.kit.exp.server.microeconomicsystem.InstitutionFactory;
import edu.kit.exp.server.structure.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.*;

/**
 * This class is the central coordination facility for running a session and is
 * started at the beginning.</br>
 * It runs sequentially through the elements (SequenceElements) of a session.
 */
public class SessionThread extends Thread {

	/**
	 * The period queue.
	 */
	private LoggedQueue<ClientMessage> periodQueue;

	/**
	 * The game map.
	 */
	private HashMap<String, Institution<? extends Environment>> gameMap = new HashMap<String, Institution<? extends Environment>>();

	/**
	 * The subject group management.
	 */
	private SubjectGroupManagement subjectGroupManagement = SubjectGroupManagement.getInstance();

	/**
	 * The message sender.
	 */
	private ServerMessageSender messageSender = ServerCommunicationManager.getInstance().getMessageSender();

	/**
	 * The trial management.
	 */
	private TrialManagement trialManagement = TrialManagement.getInstance();

	/**
	 * The period management.
	 */
	private PeriodManagement periodManagement = PeriodManagement.getInstance();

	/**
	 * The sequence element management.
	 */
	private SequenceElementManagement sequenceElementManagement = SequenceElementManagement.getInstance();

	/**
	 * The data transmission manager
	 */
	private ServerDataTransmissionManagement serverDataTransmissionManagement = ServerDataTransmissionManagement.getInstance();

	/**
	 * The run flag.
	 */
	private Boolean runFlag = true;

	/**
	 * The session.
	 */
	private Session session;

	/**
	 * The running quiz.
	 */
	private Quiz runningQuiz;

	/**
	 * The running quiz.
	 */
	private Questionnaire runningQuestionnaire;

	/**
	 * The cohort vector.
	 */
	private Vector<Cohort> cohortVector;

	/**
	 * The subject vector.
	 */
	private Vector<Subject> subjectVector;

	/**
	 * The quiz finished counter.
	 */
	private int quizFinishedCounter = 0;

	/**
	 * The questionnaire finished counter.
	 */
	private int questionnaireFinishedCounter = 0;

	/**
	 * The number of subjects.
	 */
	private int numberOfSubjects = 0;

	/**
	 * The clean unfinished period.
	 */
	private Boolean cleanUnfinishedPeriod;

	// For sending number of Periods
	private int numberOfPeriods = 0;

	/**
	 * This constructor instantiates a new session thread.
	 *
	 * @param name                the String name of the session thread
	 * @param session             A {@link edu.kit.exp.server.jpa.entity.Session Session}
	 *                            variable which contains a given session.
	 * @param periodQueue         A {@link edu.kit.exp.server.run.LoggedQueue LoggedQueue} for
	 *                            communicating with clients.
	 * @param continueSessionFlag A boolean variable which indicates if a session is
	 *                            continued(true) or not (false).
	 * @param dataQueue
	 */
	public SessionThread(String name, Session session, LoggedQueue<ClientMessage> periodQueue, Boolean continueSessionFlag, LoggedQueue<ClientDataTransmissionMessage> dataQueue) {
		super(name);
		this.periodQueue = periodQueue;
		this.session = session;
		this.cleanUnfinishedPeriod = continueSessionFlag;
		start();
	}

	/**
	 * Run Session.
	 */
	@Override public void run() {

		Vector<SequenceElement> sequenceElementVector = new Vector<>(session.getSequenceElements());
		Collections.sort(sequenceElementVector);
		Vector<Period> periodVector;

		try {
			startSensorRecording();
		} catch (ClassNotFoundException | RemoteException e) {
			RunStateLogger.getInstance().createServerErrorMessage(e);
            LogHandler.printException(e);
		}

        for (SequenceElement sequenceElement : sequenceElementVector) {

            if (!sequenceElement.getDone()) {
                RunStateLogger.getInstance().createServerOutputMessage(RunStateLogEntry.ServerEvent.SESSIONUPDATE, "Starting", sequenceElement.toString());

                if (sequenceElement.getClass().equals(Pause.class)) {

                    Pause pause = (Pause) sequenceElement;

                    try {
                        startPause(pause);
                    } catch (RemoteException e) {
                        RunStateLogger.getInstance().createServerErrorMessage(e);
                        LogHandler.printException(e, "Could not start pause");
                    }
                }

                if (sequenceElement.getClass().equals(Quiz.class)) {

                    runningQuiz = (Quiz) sequenceElement;

                    try {
                        startQuiz(runningQuiz);
                    } catch (RemoteException e) {
                        RunStateLogger.getInstance().createServerErrorMessage(e);
                        LogHandler.printException(e, "Could not start quiz");
                    }
                }

				if (sequenceElement.getClass().equals(Questionnaire.class)) {

					runningQuestionnaire = (Questionnaire) sequenceElement;

					try {
						startQuestionnaire(runningQuestionnaire);
					} catch (RemoteException e) {
						RunStateLogger.getInstance().createServerErrorMessage(e);
						LogHandler.printException(e, "Could not start questionnaire");
					}
				}

                if (sequenceElement.getClass().equals(TreatmentBlock.class)) {

                    TreatmentBlock treatmentBlock = (TreatmentBlock) sequenceElement;
                    // if Period number was not set before set it by taking the
                    // size of the period vector
                    periodVector = new Vector<>(treatmentBlock.getPeriods());
                    if (numberOfPeriods == 0) {
                        numberOfPeriods = periodVector.size();
                    }
                    Collections.sort(periodVector);

                    for (int j = 0; j < periodVector.size(); j++) {
                        Period period = periodVector.get(j);

                        if (!period.getDone()) {

                            if (cleanUnfinishedPeriod) {
                                deleteTrials(period);
                            }

                            try {
                                RunStateLogger.getInstance().createServerOutputMessage(RunStateLogEntry.ServerEvent.SESSIONUPDATE, "Starting", period.toString());

                                startPeriod(period, numberOfPeriods);

                                period.setDone(true);

                                periodManagement.updatePeriod(period);

                            } catch (Exception e) {
                                RunStateLogger.getInstance().createServerErrorMessage(e);
                                LogHandler.printException(e, "Could not update period");
                            }
                        } else {
                            RunStateLogger.getInstance().createServerOutputMessage(RunStateLogEntry.ServerEvent.SESSIONUPDATE, "Skipping", period.toString() + " (ALREADY DONE)");
                        }
                    }
                }

                sequenceElement.setDone(true);
                try {
                    sequenceElementManagement.updateSequenceElement(sequenceElement);
                } catch (StructureManagementException e) {
                    RunStateLogger.getInstance().createServerErrorMessage(e);
                    LogHandler.printException(e, "Could not update sequenceElement");
                }

            } else {
                RunStateLogger.getInstance().createServerOutputMessage(RunStateLogEntry.ServerEvent.SESSIONUPDATE, "Skipping", sequenceElement.toString() + " (ALREADY DONE)");
            }
        }

		try {
			messageSender.sendToALL(DefaultEndScreen.class.getName(), new DefaultEndScreen.ParamObject(false));
		} catch (RemoteException e) {
			RunStateLogger.getInstance().createServerErrorMessage(e);
            LogHandler.printException(e, "Could not send end screen message");
		}

		RunStateLogger.getInstance().createServerOutputMessage(RunStateLogEntry.ServerEvent.FINISHING, "Session state", "End of session");

		// Session finished set DONE!
		session.setDone(true);
		try {
			SessionManagement.getInstance().updateSession(session);
		} catch (StructureManagementException e) {
			LogHandler.printException(e, "Could not update session");
			RunStateLogger.getInstance().createServerErrorMessage(e);
		}
		try {
			stopSensors();
		} catch (RemoteException e) {
			LogHandler.printException(e, "Could not stop sensors");
			RunStateLogger.getInstance().createServerErrorMessage(e);
		}
		try {
			transmitData();
		} catch (TransmissionException | StructureManagementException | DataManagementException | IOException | SQLException e) {
            LogHandler.printException(e, "Could not transmit data");
			RunStateLogger.getInstance().createServerErrorMessage(e);
		}

		RunStateLogger.getInstance().createServerOutputMessage(RunStateLogEntry.ServerEvent.FINISHING, "Session state", "All done!");
	}

	private void stopSensors() throws RemoteException {
		messageSender.sendSensorCommandToALL(new SensorControlStopRecording());
		RunStateLogger.getInstance().createServerOutputMessage(RunStateLogEntry.ServerEvent.FINISHING, "Sensor", "Stop Recording");
	}

	private void startSensorRecording() throws ClassNotFoundException, RemoteException {
		messageSender.sendSensorCommandToALL(new SensorControlStartRecording());
		RunStateLogger.getInstance().createServerOutputMessage(RunStateLogEntry.ServerEvent.INITIALIZING, "Sensor", "Initialized Recording");
	}

	private void transmitData() throws TransmissionException, StructureManagementException, DataManagementException, IOException, SQLException {
		List<Subject> subjectList = findSubjectsOfCurrentSession();
		for (Subject subject : subjectList) {
			messageSender.sendTransmissionCommandToClient(subject.getIdClient());
			serverDataTransmissionManagement.receive(subject.getIdClient(), subject.getIdSubject().toString());
			messageSender.sendGeneralScreenMessage(subject, DefaultEndScreen.class.getName(), new DefaultEndScreen.ParamObject(true));
		}
	}

	private List<Subject> findSubjectsOfCurrentSession() throws StructureManagementException {
		List<Subject> subjectList = new ArrayList<Subject>();
		for (Cohort cohort : session.getCohorts()) {
			for (Subject subject : cohort.getSubjects()) {
				subjectList.add(subject);
			}
		}
		return subjectList;
	}

	/**
	 * This method starts a pause.
	 *
	 * @param pause A {@link edu.kit.exp.server.jpa.entity.Pause Pause} variable
	 *              which defines the properties for the pause.
	 * @throws RemoteException If connection failed.
	 */
	private void startPause(Pause pause) throws RemoteException {

		messageSender.sendToALLWithDeadLine(DefaultInfoScreen.class.getName(), new DefaultInfoScreen.ParamObject(pause.getMessage()), pause.getTime());

	}

	/**
	 * This method starts a quiz.
	 *
	 * @param quiz A {@link edu.kit.exp.server.jpa.entity.Quiz Quiz} variable
	 *             which defines the properties for the quiz.
	 * @throws RemoteException If connection failed.
	 */
	private void startQuiz(Quiz quiz) throws RemoteException {

		runFlag = true;
		quizFinishedCounter = 0;
		String screenId = quiz.getQuizFactoryKey();
		// Quiz has no parameters, but parameters should not be null
		messageSender.sendToALL(screenId, null);

		waitForClientMessages();
	}

	private void startQuestionnaire(Questionnaire questionnaire) throws RemoteException {

		runFlag = true;
		questionnaireFinishedCounter = 0;
		String screenId = questionnaire.getQuestionnaireFactoryKey();
		// Quiz has no parameters, but parameters should not be null
		messageSender.sendToALL(screenId, null);

		waitForClientMessages();
	}

	/**
	 * This method starts a period.
	 *
	 * @param period A {@link edu.kit.exp.server.jpa.entity.Period Period} variable
	 *               which defines the properties for the period.
	 * @throws Exception
	 */
	private void startPeriod(Period period, int numberOfPeriods) throws Exception {

		runFlag = true;
		int numberOfGroups = period.getSubjectGroups().size();

		// Start one game for every subject group
		for (Integer index = 0; index < numberOfGroups; index++) {

			SubjectGroup subjectGroup = period.getSubjectGroups().get(index);
			String gameId = subjectGroup.getIdSubjectGroup().toString();

			Environment environment = EnvironmentFactory.createEnvironment(period.getTreatment().getEnvironmentFactoryKey());
			Institution<? extends Environment> institution = InstitutionFactory.createInstitution(period.getTreatment().getInstitutionFactoryKey(), environment, subjectGroup.getMemberships(), messageSender, gameId);
			institution.setCurrentTreatment(period.getTreatment());
			institution.setCurrentPeriod(period);
			gameMap.put(gameId, institution);
			environment.setNumberOfPeriods(numberOfPeriods);
			institution.startPeriod();

		}

		ClientMessage msg;

		// Wait for client messages
		waitForClientMessages();
	}

	private void waitForClientMessages() {
		ClientMessage msg;
		while (runFlag) {
			msg = periodQueue.pop();
			try {
				processMessage(msg);
			} catch (Exception e) {
				RunStateLogger.getInstance().createServerErrorMessage(e);
                LogHandler.printException(e);
			}
		}
	}

	/**
	 * This method processes client messages.
	 *
	 * @param message A {@link edu.kit.exp.server.communication.ClientMessage
	 *                ClientMessage} variable which contains the client message.
	 * @throws Exception
	 */
	public void processMessage(ClientMessage message) throws Exception {

		// Process client response messages
		if (message.getClass().equals(ClientResponseMessage.class)) {

			ClientResponseMessage clientResponseMessage = (ClientResponseMessage) message;
			int finishedCounter = 0;
			String gameId = clientResponseMessage.getGameId();
			Institution<? extends Environment> institution = gameMap.get(gameId);
			institution.processMessage(clientResponseMessage);

			for (Map.Entry<String, Institution<? extends Environment>> entry : gameMap.entrySet()) {

				Institution<? extends Environment> inst = entry.getValue();

				if (inst.isFinished()) {
					finishedCounter++;
				}
			}

			if (finishedCounter == gameMap.size()) {
				runFlag = false;
			}
		}

		// Process a trial log message
		if (message.getClass().equals(ClientTrialLogMessage.class)) {

			ClientTrialLogMessage logMsg = (ClientTrialLogMessage) message;
			String subjectGroupId = logMsg.getGameId();

			SubjectGroup subjectGroup = subjectGroupManagement.findSubjectGroup(Long.valueOf(subjectGroupId));

			Subject subject = null;

			for (Membership membership : subjectGroup.getMemberships()) {
				if (membership.getSubject().getIdClient().equals(logMsg.getClientId())) {
					subject = membership.getSubject();
				}
			}

			Trial trial = logMsg.getTrial();
			trial.setSubject(subject);
			trial.setSubjectGroup(subjectGroup);
			TrialManagement.getInstance().createNewTrial(trial);
		}

		// Process quiz messages
		if (message.getClass().equals(QuizProtocolMessage.class)) {

			QuizProtocolMessage quizProtocolMessage = (QuizProtocolMessage) message;

			createCohortAndSubjectVectorIfNecessary();

			QuizProtocol protocol = new QuizProtocol();
			protocol.setPassed(quizProtocolMessage.getPassed());
			protocol.setQuiz(runningQuiz);
			protocol.setSolution(quizProtocolMessage.getQuizSolution());
			Subject subject = null;

			for (Subject s : subjectVector) {
				if (s.getIdClient().equals(quizProtocolMessage.getClientId())) {
					subject = s;
				}
			}

			protocol.setSubject(subject);
			ProtocolManagement.getInstance().createNewQuizProtocol(protocol);
			quizFinishedCounter++;

			if (quizFinishedCounter == numberOfSubjects) {
				runFlag = false;
			}
		}

		// Process questionnaire messages
		if (message.getClass().equals(QuestionnaireProtocolMessage.class)) {

			QuestionnaireProtocolMessage questionnaireProtocolMessage = (QuestionnaireProtocolMessage) message;

			createCohortAndSubjectVectorIfNecessary();

			if (((QuestionnaireProtocolMessage) message).isLast()) {
				questionnaireFinishedCounter++;
				if (questionnaireFinishedCounter == numberOfSubjects) {
					runFlag = false;
				}
			} else {
				saveQuestionnaireProtocolToDatabase((QuestionnaireProtocolMessage) message, questionnaireProtocolMessage);
			}
		}
	}

	private void saveQuestionnaireProtocolToDatabase(QuestionnaireProtocolMessage message, QuestionnaireProtocolMessage questionnaireProtocolMessage) throws StructureManagementException {
		QuestionnaireProtocol protocol = new QuestionnaireProtocol();
		protocol.setQuestion(message.getQuestion());
		protocol.setQuestionResponse(message.getQuestionResponse());
		protocol.setQuestionResponseTime(message.getQuestionResponseTime());
		protocol.setQuestionnaire(runningQuestionnaire);
		Subject subject = null;

		for (Subject s : subjectVector) {
			if (s.getIdClient().equals(questionnaireProtocolMessage.getClientId())) {
				subject = s;
			}
		}

		protocol.setSubject(subject);
		ProtocolManagement.getInstance().createNewQuestionnaireProtocol(protocol);
	}

	private void createCohortAndSubjectVectorIfNecessary() {
		if (cohortVector == null) {

			cohortVector = new Vector<>();
			cohortVector.addAll(session.getCohorts());
			subjectVector = new Vector<>();

			for (Cohort cohort : cohortVector) {
				numberOfSubjects += cohort.getSize();
				subjectVector.addAll(cohort.getSubjects());
			}
		}
	}

	/**
	 * This method deletes trials of a given period.
	 *
	 * @param period A {@link edu.kit.exp.server.jpa.entity.Period Period}, a trial
	 *               has to be deleted from.
	 */
	private void deleteTrials(Period period) {

		Vector<SubjectGroup> groups = new Vector<>(period.getSubjectGroups());

		for (SubjectGroup subjectGroup : groups) {

			Vector<Trial> trials = new Vector<>(subjectGroup.getTrials());
			for (Trial t : trials) {
				try {
					trialManagement.deleteTrial(t.getIdTrial());
				} catch (StructureManagementException e) {
					RunStateLogger.getInstance().createServerErrorMessage(e);
                    LogHandler.printException(e, "Could not delete trial");
				}
			}
		}

	}

}
