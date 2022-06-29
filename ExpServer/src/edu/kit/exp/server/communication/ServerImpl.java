package edu.kit.exp.server.communication;

import edu.kit.exp.common.*;
import edu.kit.exp.common.communication.IncommingMessageQueue;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * This class is an implementation of the Server interface (IServer). It
 * provides methods for clients to communicate with the server.
 * 
 * @see edu.kit.exp.common.IServer
 */
public class ServerImpl extends UnicastRemoteObject implements IServer {

	/** The message queue. */
	private IncommingMessageQueue<ClientMessage> messageQueue;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 105295106180455257L;

	/* Singleton Pattern */
	/** The instance. */
	private static ServerImpl instance = null;

	/**
	 * This constructor instantiates a new server implementation.
	 * 
	 * @throws RemoteException
	 *             If connection failed.
	 */
	private ServerImpl() throws RemoteException {
		messageQueue = ServerCommunicationManager.getInstance().getIncommingMessageQueue();
		registerServer();
	}

	/**
	 * This method gets a single instance of ServerImpl. If there is no existing
	 * implementation, a new one is created.
	 * 
	 * @return a single instance of ServerImpl
	 * @throws RemoteException
	 *             If connection failed.
	 */
	public static ServerImpl getInstance() throws RemoteException {

		if (instance == null) {
			instance = new ServerImpl();
		}

		return instance;
	}

	/**
	 * Call this method to register this server. It is registered with name and
	 * port.
	 * 
	 * @throws RemoteException
	 *             If connection failed.
	 * @see for Name: <code>Constants.SERVER_RMI_OBJECT_NAME</code> and
	 *      <code>Constants.SERVER_PORT</code>
	 */
	private synchronized void registerServer() throws RemoteException {

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		Registry registry = LocateRegistry.getRegistry(Constants.SERVER_PORT);

		boolean bound = false;

		for (int i = 0; !bound && i < 2; i++) {

			try {
				registry.rebind(Constants.SERVER_RMI_OBJECT_NAME, this);
				bound = true;
                LogHandler.printInfo(Constants.SERVER_RMI_OBJECT_NAME + " bound to registry, port " + Constants.SERVER_PORT + ".");
			} catch (RemoteException e) {
				LogHandler.printInfo("Rebinding " + Constants.SERVER_RMI_OBJECT_NAME + " failed, retrying ...");
				registry = LocateRegistry.createRegistry(Constants.SERVER_PORT);
				LogHandler.printInfo("Registry started on port " + Constants.SERVER_PORT + ".");
			}
		}

		LogHandler.printInfo("Server ready.");
	}

	@Override
	public synchronized void registerClient(IClient clientRemoteObject, String clientId) throws RemoteException {
		ClientRegistrationMessage clientRegistrationMessage = new ClientRegistrationMessage(clientId, clientRemoteObject);
		messageQueue.push(clientRegistrationMessage);
	}

	@Override
	public void receiveClientResponseMessage(String clientId, IScreenParamObject parameters, String gameId, String screenId, Long clientTimeStamp) {

		Date date = new Date();
		Long serverTimeStamp = date.getTime();
		ClientResponseMessage clientResponseMessage = new ClientResponseMessage(clientId, parameters, gameId, screenId, clientTimeStamp, serverTimeStamp);
		messageQueue.push(clientResponseMessage);

	}

	@Override
	public void receiveTrialLogMessage(String clientId, String gameId, String ValueName, String screenName, String value, Long clientTimeStamp) throws RemoteException {

		Date date = new Date();
		Long serverTimeStamp = date.getTime();

		ClientTrialLogMessage clientTrialLogMessage = new ClientTrialLogMessage(clientId, gameId, ValueName, screenName, value, clientTimeStamp, serverTimeStamp);
		messageQueue.push(clientTrialLogMessage);

	}

	@Override
	public void receiveQuizProtocol(String clientId, Boolean passed, String quizSolution) {

		QuizProtocolMessage quizProtocolMessage = new QuizProtocolMessage(clientId, passed, quizSolution);
		messageQueue.push(quizProtocolMessage);

	}

	@Override
	public void receiveQuestionnaireProtocol(String clientId, boolean isLast, String question, String questionResponse, long questionResponseTime) {

		QuestionnaireProtocolMessage questionnaireProtocolMessage = new QuestionnaireProtocolMessage(clientId, isLast, question, questionResponse, questionResponseTime);
		messageQueue.push(questionnaireProtocolMessage);

	}

	@Override
	public void receiveDataTransmissionPackage(String clientId, byte[] data, int fileNumber, int packageNumber) throws RemoteException {
		ClientDataTransmissionMessage dataPropertiesMessage = new ClientDataTransmissionMessage(clientId, data, fileNumber, packageNumber);
		messageQueue.push(dataPropertiesMessage);
	}

	@Override
	public void receiveTransmissionPropertiesPackage(String clientId, long[] fileSizes, String[] fileNames, long[] startTimeStamps, long[] stopTimeStamps, int numberOfFiles, int bytesPerPackage) {
		ClientDataPropertiesMessage dataPropertiesMessage = new ClientDataPropertiesMessage(clientId, fileSizes, fileNames, startTimeStamps, stopTimeStamps, numberOfFiles, bytesPerPackage);
		messageQueue.push(dataPropertiesMessage);
	}

	@Override
	public void receiveClientStatus(ClientStatus clientStatus) throws RemoteException {
		ClientStatusMessage sensorStatusMessage = new ClientStatusMessage(clientStatus);
		messageQueue.push(sensorStatusMessage);
	}

}
