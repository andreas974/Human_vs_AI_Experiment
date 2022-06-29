package edu.kit.exp.common.communication;

/**
 * This generic Class provides the <code>routeMessage()</code> method to
 * implement a message receiver. The client and the server use this class to
 * receive messages.
 * 
 * @param <T>
 *            type of the received value
 * @see edu.kit.exp.client.comunication.ClientMessageSender
 * @see edu.kit.exp.server.communication.ServerMessageSender
 * @see CommunicationManager
 * @see MessageDeliveryThread
 */
public abstract class MessageReceiver<T> {

	/**
	 * Abstract method to route a message.
	 * 
	 * @param msg
	 *            a type T message.
	 * @throws Exception
	 *             the exception
	 */
	public abstract void routeMessage(T msg) throws Exception;
}
