package edu.kit.exp.server.gui;

/**
 * This class provides an exception for wrong data input.
 * 
 */
public class DataInputException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7245168988709666841L;

	/** The message. */
	private String message;

	/**
	 * This constructor instantiates a new data input exception.
	 * 
	 * @param msg
	 *            A String which is used for sending error messages.
	 */
	public DataInputException(String msg) {

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
