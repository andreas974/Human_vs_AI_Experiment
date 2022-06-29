package edu.kit.exp.common.communication;

/**
 * This abstract Class defines methods to manage the communication between a
 * message sender and a message receiver. It is used by the client and the
 * server.
 * 
 * @param <T>
 *            the generic type
 * @param <U>
 *            the generic type
 * @param <V>
 *            the value type
 * @see edu.kit.exp.client.comunication.ClientMessageSender
 * @see edu.kit.exp.server.communication.ServerMessageSender
 */
public abstract class CommunicationManager<T, U extends MessageReceiver<T>, V extends MessageSender> {

	/** The incoming message queue. */
	protected IncommingMessageQueue<T> incommingMessageQueue;

	/** The message receiver. */
	protected U messageReceiver;

	/** The message sender. */
	protected V messageSender;

	/**
	 * Constructor for creating a communication manager.
	 */
	public CommunicationManager() {
		incommingMessageQueue = new IncommingMessageQueue<T>();
	}

	/**
	 * Gets the incoming message queue.
	 * 
	 * @return The queue that manages the incoming messages.
	 */
	public IncommingMessageQueue<T> getIncommingMessageQueue() {
		return incommingMessageQueue;
	}

	/**
	 * Gets the message receiver.
	 * 
	 * @return The receiver of the message.
	 */
	public U getMessageReceiver() {
		return messageReceiver;
	}

	/**
	 * Gets the message sender.
	 * 
	 * @return The sender of the message.
	 */
	public V getMessageSender() {
		return messageSender;
	}
}
