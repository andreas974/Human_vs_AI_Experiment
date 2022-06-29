package edu.kit.exp.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This remote interface defines all methods that the server provides to the
 * client(s).
 * 
 * @see Remote
 */
public interface IServer extends Remote {

	/**
	 * Login at server.
	 * 
	 * @param client
	 *            the IClient client remote handler object, which is used by the
	 *            server to communicate with the client.
	 * @param clientId
	 *            the String ID of the client (entered at login).
	 * @throws RemoteException
	 *             If connection failed.
	 */
	void registerClient(IClient client, String clientId) throws RemoteException;

	/**
	 * Clients can send a response messages to the server by using this method.
	 * 
	 * @param clientId
	 *            the String ID of the client (entered at login)
	 * @param parameters
	 *            a IScreenParamObject list of parameters you want to send to
	 *            the server.
	 * @param gameId
	 *            the String ID of the running game, whose institution triggered
	 *            the client.
	 * @param screenId
	 *            the String global screen ID has to be given for a complete
	 *            trial entry at server side.
	 * @param clientTimeStamp
	 *            a long that indicates the time at which a screen calls the
	 *            guiController (i.e. the time a button is pressed) in
	 *            milliseconds.
	 * @throws RemoteException
	 *             If connection failed.
	 */
	void receiveClientResponseMessage(String clientId, IScreenParamObject parameters, String gameId, String screenId, Long clientTimeStamp) throws RemoteException;

	/**
	 * Creates a trial.
	 * 
	 * @param clientId
	 *            the String ID of the client (entered at login)
	 * @param gameId
	 *            the ID of the running game, whose institution triggered the
	 *            client.
	 * @param event
	 *            the event you want to log.
	 * @param screenName
	 *            tThe global screen id has to be given for a complete trial
	 *            entry at server side.
	 * @param value
	 *            the value of the event.
	 * @param clientTimeStamp
	 *            the time at which a screen calls the guiController (i.e. the
	 *            time a button is pressed) in milliseconds
	 * @throws RemoteException
	 *             If connection failed.
	 */
	void receiveTrialLogMessage(String clientId, String gameId, String event, String screenName, String value, Long clientTimeStamp) throws RemoteException;

	/**
	 * Creates and processes a quiz protocol.
	 *
	 * @param clientId
	 *            the String ID of the client (entered at login).
	 * @param passed
	 *            A Boolean that is true if subject has passed the quiz.
	 * @param quizSolution
	 *            the String answers of the subject as CSV-Sting.
	 * @throws RemoteException
	 *             If connection failed.
	 */
	void receiveQuizProtocol(String clientId, Boolean passed, String quizSolution) throws RemoteException;

	void receiveQuestionnaireProtocol(String clientId, boolean isLast, String question, String questionResponse, long questionResponseTime) throws RemoteException;

	/**
	 * Receives data transmission package from client.
	 * 
	 * @param clientId
	 *            the String client ID.
	 * @param data
	 *            the data in bytes
	 * @param fileNumber
	 *            tells how many files shall be transmitted
	 * @param packageNumber
	 *            number of the transmitted package      
	 * 	 * @throws RemoteException
	 *             If connection failed.
	 */
	void receiveDataTransmissionPackage(String clientId, byte[] data, int fileNumber, int packageNumber) throws RemoteException;
	
	/**
	 * Receives package with transmission data from client.
	 * 
	 * @param clientId
	 *            the String client ID.
	 * @param fileSizes
	 * 			sizes of the files in bytes
	 * @param fileNames
	 * 			names of the files
	 * @param numberOfFiles
	 * @throws RemoteException
	 *             If connection failed.
	 */
	void receiveTransmissionPropertiesPackage(String clientId, long[] fileSizes, String[] fileNames, long[] startTimeStamps, long[] stopTimeStamps, int numberOfFiles, int bytesPerPackage) throws RemoteException;

	/**
	 * Receives a sensor status update
	 *
	 * @param clientStatus
	 * 					sensor status
	 */
	void receiveClientStatus(ClientStatus clientStatus) throws RemoteException;

}
