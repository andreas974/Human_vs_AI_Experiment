package edu.kit.exp.client.comunication;

import edu.kit.exp.common.communication.CommunicationManager;

/**
 * The class ClientCommunicationManager.</br> It manages the communication by
 * creating an instance of a ClientMessageReceiver and a ClientMessageSender to
 * make communication with the server via ServerMessages possible.
 * 
 * @see ServerMessage
 * @see ClientMessageReceiver
 * @See ClientMessageSender
 */
public class ClientCommunicationManager extends CommunicationManager<ServerMessage, ClientMessageReceiver, ClientMessageSender> {

	/** The instance. */
	private static ClientCommunicationManager instance;

	/**
	 * This method gets the single instance of ClientCommunicationManager.
	 * 
	 * @return a single instance of ClientCommunicationManager
	 */
	public static ClientCommunicationManager getInstance() {

		if (instance == null) {
			instance = new ClientCommunicationManager();
			instance.messageSender = new ClientMessageSender();
			instance.messageReceiver = new ClientMessageReceiver();
		}
		return instance;
	}

	/**
	 * This constructor instantiates a new client communication manager.
	 */
	private ClientCommunicationManager() {
		
	}
}
