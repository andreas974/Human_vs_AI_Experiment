package edu.kit.exp.common.communication;

import java.awt.Frame;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * This Class manages the queue which saves all the incoming messages. The
 * client as well as the server implementation use it to process messages
 * 
 * @param <T>
 *            type T of the queue.
 */
public class IncommingMessageQueue<T> {

	/** The queue. */
	protected ArrayList<T> queue;

	/**
	 * Constructor to create a new queue.
	 */
	protected IncommingMessageQueue() {
		queue = new ArrayList<T>();
	}

	/**
	 * Remove an item from the queue (fifo).
	 * 
	 * @return A type T object from the queue.
	 */
	public synchronized T pop() {

		while (isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
				if (Frame.getFrames().length > 0) {
					JOptionPane.showMessageDialog(Frame.getFrames()[0], e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		T obj = queue.get(0);
		queue.remove(obj);
		notify();
		return obj;
	}

	/**
	 * Add an item to the queue.
	 * 
	 * @param message
	 *            the type T item to be added to the queue.
	 */
	public synchronized void push(T message) {


		queue.add(message);
		notify();
	}

	/**
	 * Checks if the queue is empty.
	 * 
	 * @return A boolean that indicates if the queue is empty or not.
	 */
	public boolean isEmpty() {

		return queue.isEmpty();

	}
}
