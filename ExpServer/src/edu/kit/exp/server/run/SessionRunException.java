package edu.kit.exp.server.run;

/**
 * The Class SessionRunException provides an Exception which triggers if there
 * is an error while running a session.
 */
public class SessionRunException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 507339651445598747L;

	/** The message. */
	private String message;

	/**
	 * This constructor instantiates a new session run exception.
	 * 
	 * @param msg
	 *            the error message
	 */
	public SessionRunException(String msg) {

		this.message = "Error while running session! Cause: " + msg;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {

		return this.message;
	}

}
