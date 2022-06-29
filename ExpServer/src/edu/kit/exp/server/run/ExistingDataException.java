package edu.kit.exp.server.run;

/**
 * The class provides an Exception in case there is already existing data.
 */
public class ExistingDataException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1094560226687570266L;

	/** The message. */
	private String message;

	/**
	 * This constructor instantiates a new existing data exception.
	 * 
	 * @param msg
	 *            the error message
	 */
	public ExistingDataException(String msg) {

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
