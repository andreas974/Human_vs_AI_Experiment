package edu.kit.exp.server.run;

import edu.kit.exp.common.LogHandler;
import edu.kit.exp.common.communication.IncommingMessageQueue;

/**
 * Queue which logs errors in case of an InterruptedException
 * 
 * @author Tonda
 *
 * @param <T> Generic parameter for message-type
 */
public class LoggedQueue<T> extends IncommingMessageQueue<T> {
	
	private RunStateLogger runStateLogger = RunStateLogger.getInstance();
	
	/**
	 * This method removes an item from the queue (fifo).
	 * 
	 * @return the oldest object
	 */
	public synchronized T pop() {
		while (isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				runStateLogger.createServerErrorMessage(e);
                LogHandler.printException(e);
			}
		}
		T obj = queue.get(0);
		queue.remove(obj);
		notify();
		return obj;
	}
}
