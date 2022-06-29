package edu.kit.exp.server.run;

public class TransmissionException extends Exception {

	private static final long serialVersionUID = 7882811437776605109L;
	
	/** The message. */
	private String message;

	/**
	 * This constructor instantiates a new existing transmission exception.
	 * 
	 * @param msg
	 *            the error message
	 */
	public TransmissionException(String msg) {

		this.message = msg;

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
