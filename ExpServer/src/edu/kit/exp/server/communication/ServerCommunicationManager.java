package edu.kit.exp.server.communication;

import edu.kit.exp.common.communication.CommunicationManager;

/**
 * The class ServerCommunicationManager creates a communication manager based on
 * the abstract class <code>CommunicationManager</code>. It manages the
 * communication by creating an instance of a ServerMessageReceiver and a
 * ServerMessageSender to make communication with the client via ClientMessages
 * possible.
 * 
 * @see edu.kit.exp.common.commuication.CommunicationManager
 * @see ServerImpl
 * @see ServerMessageReceiver
 * @see ServerMessageSender
 * 
 */
public class ServerCommunicationManager extends CommunicationManager<ClientMessage, ServerMessageReceiver, ServerMessageSender> {

	/** The instance. */
	private static ServerCommunicationManager instance;

	/**
	 * This method gets the single instance of ServerCommunicationManager.
	 * 
	 * @return single instance of ServerCommunicationManager
	 */
	public synchronized static ServerCommunicationManager getInstance() {

		if (instance == null) {
			instance = new ServerCommunicationManager();
			instance.messageSender = new ServerMessageSender();
			instance.messageReceiver = new ServerMessageReceiver();
		}
		return instance;
	}

	/**
	 * This constructor instantiates a new server communication manager.
	 */
	private ServerCommunicationManager() {
	}
}
