package edu.kit.exp.server.run;

import edu.kit.exp.server.communication.ClientDataPropertiesMessage;
import edu.kit.exp.server.communication.ClientDataTransmissionMessage;
import edu.kit.exp.server.communication.ClientMessage;
import edu.kit.exp.server.communication.ClientStatusMessage;

/**
 * This class manages instances of different queues for incoming client
 * messages.
 * 
 */
public class QueueManager {

	/** The instances. */
	private static LoggedQueue<ClientMessage> sessionQueue;
	private static LoggedQueue<ClientDataTransmissionMessage> dataTransmissionQueue;
	private static LoggedQueue<ClientDataPropertiesMessage> clientDataPropertiesMessageQueue;
	private static LoggedQueue<ClientStatusMessage> clientStatusMessageQueue;

	/**
	 * This method gets the single instance of SessionQueue.
	 * 
	 * @return a single instance of SessionQueue
	 */
	public static LoggedQueue<ClientMessage> getSessionQueueInstance() {

		if (sessionQueue == null) {
			sessionQueue = new LoggedQueue<>();
		}
		return sessionQueue;
	}

	/**
	 * This method gets the single instance of DataTransmissionQueue.
	 * 
	 * @return a single instance of DataTransmissionQueue
	 */
	public static LoggedQueue<ClientDataTransmissionMessage> getDataTransmissionQueueInstance() {

		if (dataTransmissionQueue == null) {
			dataTransmissionQueue = new LoggedQueue<>();
		}
		return dataTransmissionQueue;
	}
	
	/**
	 * This method gets the single instance of DataPropertiesQueue.
	 * 
	 * @return a single instance of DataTransmissionQueue
	 */
	public static LoggedQueue<ClientDataPropertiesMessage> getClientDataPropertiesQueueInstance() {

		if (clientDataPropertiesMessageQueue == null) {
			clientDataPropertiesMessageQueue = new LoggedQueue<>();
		}
		return clientDataPropertiesMessageQueue;
	}

	/**
	 * This method gets the single instance of SensorStatusQueue.
	 * 
	 * @return a single instance of SensorStatusQueue
	 */
	public static LoggedQueue<ClientStatusMessage> getClientStatusQueueInstance() {
		if (clientStatusMessageQueue == null) {
			clientStatusMessageQueue = new LoggedQueue<>();
		}
		return clientStatusMessageQueue;
	}
}
