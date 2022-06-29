package edu.kit.exp.common.communication;

/**
 * This Exception triggers if a client can not connect to the server.
 * 
 */
public class ConnectionException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3487613197235546928L;

	/** The message. */
	private String message;

	/**
	 * Instantiates a new connection exception.
	 * 
	 * @param msg
	 *            a String which gives further information about the error
	 */
	public ConnectionException(String msg) {
		this.message = "ExpClient could not connect to ExpServer. " + msg;
	}

	/**
	 * Gets an error message.
	 * 
	 * @return a String error message.
	 */
	@Override
	public String getMessage() {
		return this.message;
	}
}
