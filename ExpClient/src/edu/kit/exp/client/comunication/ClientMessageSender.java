package edu.kit.exp.client.comunication;

import edu.kit.exp.client.gui.MainFrame;
import edu.kit.exp.common.ClientStatus;
import edu.kit.exp.common.Constants;
import edu.kit.exp.common.IScreenParamObject;
import edu.kit.exp.common.IServer;
import edu.kit.exp.common.communication.ConnectionException;
import edu.kit.exp.common.communication.MessageSender;

import javax.swing.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.regex.Pattern;

/**
 * This class provides functionalities for a client to send messages to the
 * server.
 */
public class ClientMessageSender extends MessageSender {

	/** The server remote object. */
	private IServer serverRemoteObject;

	/** The client id. */
	private String clientId;

	/** The client remote object. */
	private ClientImpl clientRemoteObject;

	private static final String ipv4Pattern = "(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
	private static final String ipv6Pattern = "([0-9a-f]{1,4}:){7}([0-9a-f]){1,4}";

	/**
	 * This constructor instantiates a new client message sender.
	 */
	ClientMessageSender() {

	}

	/**
	 * This method connects a client to the ExpServer. The Server can be found
	 * with name <code>Constants.SERVER_RMI_OBJECT_NAME</code> and port
	 * <code>Constants.SERVER_PORT</code></br>
	 * The client is identified by his client ID.
	 * 
	 * @param clientId
	 *            A String which contains the ID of the client to be registered.
	 * @param serverIP
	 *            The String ID of the server where the client should be
	 *            registered.
	 * @throws ConnectionException
	 *             If the connection failed.
	 */
	public void registerAtServer(String clientId, String serverIP) throws ConnectionException {

		System.setProperty("java.security.policy", "file:./java.policy");

		this.clientId = clientId;

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		try {
			
			// Check serverIP
			String rmiConncetionString = "";
			Pattern VALID_IPV6_PATTERN = Pattern.compile(ipv6Pattern, Pattern.CASE_INSENSITIVE);
			Pattern VALID_IPV4_PATTERN = Pattern.compile(ipv4Pattern, Pattern.CASE_INSENSITIVE);
			if(VALID_IPV6_PATTERN.matcher(serverIP).matches())
			{
				rmiConncetionString = "rmi://[" + serverIP + "]:" + Constants.SERVER_PORT + "/" + Constants.SERVER_RMI_OBJECT_NAME;
			}
			else if (VALID_IPV4_PATTERN.matcher(serverIP).matches())
			{
				rmiConncetionString = "rmi://" + serverIP + ":" + Constants.SERVER_PORT + "/" + Constants.SERVER_RMI_OBJECT_NAME;
			} else {
				throw new Exception("server IP not valid: " + serverIP);
			}

			// Find server
			serverRemoteObject = (IServer) Naming.lookup(rmiConncetionString);

			// Create remote control object of this client for server
			clientRemoteObject = ClientImpl.getInstance();

			// Register at server and send remote handler of client
			UnicastRemoteObject.exportObject(clientRemoteObject, 0);
			serverRemoteObject.registerClient(clientRemoteObject, clientId);
			
		} catch (Exception e) {

			ConnectionException ex = new ConnectionException(e.getMessage());
			throw ex;
		}

		//Start ClientStatus Thread
		ClientStatusManager.getInstance().start();
	}

	/**
	 * This method sends a reconnect message after a failed login.
	 * 
	 * @param clientId
	 *            A String which contains the ID of the client that should have
	 *            been registered.
	 * @throws ConnectionException
	 *             If the connection failed.
	 */
	public void reconnect(String clientId) throws ConnectionException {

		this.clientId = clientId;
		try {
			serverRemoteObject.registerClient(clientRemoteObject, clientId);
		} catch (RemoteException e) {
			ConnectionException ex = new ConnectionException(e.getMessage());
			throw ex;
		}

		//Start ClientStatus Thread
		ClientStatusManager.getInstance().start();
	}

	/**
	 * This method sends a message to the server.
	 * 
	 * @param parameters
	 *            A IScreenParamObject list of parameters you want to send to
	 *            the server.
	 * @param gameId
	 *            A String which contains the ID of the running game, whose
	 *            institution triggered the client.
	 * @param screenId
	 *            A String which contains the global screen ID. It has to be
	 *            given for a complete trial entry at server side.
	 * @param clientTimeStamp
	 *            A Long variable which contains the time at which a screen
	 *            calls the guiController (i.e. the time a button is pressed) in
	 *            milliseconds.
	 */
	public void sendMessage(IScreenParamObject parameters, String gameId, String screenId, Long clientTimeStamp) {

		try {
			serverRemoteObject.receiveClientResponseMessage(clientId, parameters, gameId, screenId, clientTimeStamp);
		} catch (RemoteException e) {
			JOptionPane.showInternalMessageDialog(MainFrame.getInstance(), e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

	}

	/**
	 * This method creates a trial on the server side.
	 * 
	 * @param gameId
	 *            A String which contains the ID of the running game, whose
	 *            institution triggered the client.
	 * @param event
	 *            A String that contains the name of the event.
	 * @param screenId
	 *            A String which contains the global screen ID. It has to be
	 *            given for a complete trial entry at server side.
	 * @param value
	 *            A String variable which contains the value of the event.
	 * @param clientTimeStamp
	 *            A Long timestamp which contains the time at which a screen
	 *            calls the guiController (i.e. the time a button is pressed) in
	 *            milliseconds.
	 */
	public void sendTrialLogMessage(String gameId, String event, String screenId, String value, Long clientTimeStamp) {

		try {
			serverRemoteObject.receiveTrialLogMessage(clientId, gameId, event, screenId, value, clientTimeStamp);
		} catch (RemoteException e) {
			JOptionPane.showInternalMessageDialog(MainFrame.getInstance(), e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * This method creates a quiz protocol on the server side.
	 * 
	 * @param passed
	 *            A boolean which shows if the quiz was passed (true) or not
	 *            (false).
	 * @param quizSolution
	 *            A String which contains the solution of the quiz.
	 */
	public void sendQuizProtocol(Boolean passed, String quizSolution) {

		try {
			serverRemoteObject.receiveQuizProtocol(clientId, passed, quizSolution);
		} catch (RemoteException e) {
			JOptionPane.showInternalMessageDialog(MainFrame.getInstance(), e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

	}

	/**
	 * This method creates a questionnaire protocol on the server side.
	 *
	 * @param questionnaireSolution
	 *            A String which contains the solution of the questionnaire.
	 */
	public void sendQuestionnaireProtocol(boolean isLast, String question, String questionResponse, long questionResponseTime) {

		try {
			serverRemoteObject.receiveQuestionnaireProtocol(clientId, isLast, question, questionResponse, questionResponseTime);
		} catch (RemoteException e) {
			JOptionPane.showInternalMessageDialog(MainFrame.getInstance(), e.getMessage(), "Error",
				JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

	}

	/**
	 * This method sends a data package to the server.
	 * 
	 * @param data
	 *            part of the file which is supposed to be sent to the server
	 * @param packageNumber
	 *            A HashMap<String, Double> which contains the physio data.
	 * @param fileNumber
	 */
	public void sendDataTransmissionPackage(byte[] data, int fileNumber, int packageNumber) {
		try {
			serverRemoteObject.receiveDataTransmissionPackage(clientId, data, fileNumber, packageNumber);
		} catch (RemoteException e) {
			JOptionPane.showInternalMessageDialog(MainFrame.getInstance(), e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * This method sends a properties regarding the data transmission to the
	 * server.
	 *
	 * @param fileNames
	 * @param bytesPerPackage
	 */
	public void sendDataPropertiesPackage(long[] data, String[] fileNames, long[] timeStampsStartWriting, long[] timeStampsStopWriting, int numberOfFiles, int bytesPerPackage) {
		try {
			serverRemoteObject.receiveTransmissionPropertiesPackage(clientId, data, fileNames, timeStampsStartWriting, timeStampsStopWriting, numberOfFiles, bytesPerPackage);
		} catch (RemoteException e) {
			JOptionPane.showInternalMessageDialog(MainFrame.getInstance(), e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	public void sendClientStatus(ClientStatus clientStatus) {
		try {
			serverRemoteObject.receiveClientStatus(clientStatus);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}