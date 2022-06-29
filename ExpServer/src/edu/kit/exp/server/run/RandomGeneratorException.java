package edu.kit.exp.server.run;

/**
 * This class provides an Exception for errors during the number generation
 * process.
 * 
 */
public class RandomGeneratorException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2413739602716260398L;

	/** The message. */
	private String message;

	/**
	 * This constructor instantiates a new random generator exception.
	 * 
	 * @param msg
	 *            the error message
	 */
	public RandomGeneratorException(String msg) {

		this.message = "Can not create random numbers! Cause: " + msg;

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
