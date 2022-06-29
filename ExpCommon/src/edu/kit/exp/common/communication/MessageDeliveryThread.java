package edu.kit.exp.common.communication;

import java.awt.Frame;

import javax.swing.JOptionPane;

/**
 * This Class inherits from <code>Thread</code> and delivers an incoming message
 * to a message receiver.
 * 
 * @param <T>
 *            the generic type
 * @see MessageReceiver
 * @see edu.kit.exp.client.comunication.ClientMessageSender
 * @see edu.kit.exp.server.communication.ServerMessageSender
 * @see CommunicationManager
 * 
 */
public class MessageDeliveryThread<T> extends Thread {

	/** The message queue. */
	private IncommingMessageQueue<T> messageQueue;

	/** The message receiver. */
	private MessageReceiver<T> messageReceiver;

	/**
	 * Class constructor. It needs a name for the thread, an incoming message of
	 * a certain type and a receiver for this type to start the thread.
	 * 
	 * @param name
	 *            A String that contains the name of the Thread.
	 * @param queue
	 *            A <code>IncommingMessageQueue</code> of type T.
	 * @param receiver
	 *            A <code>MessageReceiver</code> of the same type T.
	 * @see IncommingMessageQueue
	 */
	public MessageDeliveryThread(String name, IncommingMessageQueue<T> queue, MessageReceiver<T> receiver) {
		super(name);
		this.messageQueue = queue;
		this.messageReceiver = receiver;
		setDaemon(true);
		start();
	}

	/**
	 * As long as the thread is running this method pops a message from the
	 * queue and routes it to a receiver.
	 */
	@Override
	public void run() {
		T msg = null;

		while (true) {
			msg = messageQueue.pop();
			try {
				messageReceiver.routeMessage(msg);
			} catch (Exception e) {
				if (Frame.getFrames().length > 0) {
					JOptionPane.showMessageDialog(Frame.getFrames()[0], e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				e.printStackTrace();
			}

		}
	}

}
