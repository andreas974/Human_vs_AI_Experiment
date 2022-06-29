package edu.kit.exp.server.run;

/**
 * The Class SessionDoneException provides an Exception for cases where a
 * session was already successfully finished, but the experimenter tries to run
 * it again.
 */
public class SessionDoneException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8663429093529523397L;

	/** The message. */
	private String message;

	/**
	 * This constructor instantiates a new session done exception.
	 */
	public SessionDoneException() {

		message = "<html><body>Session was successfully finished before! Do you want to run int again?" + "<br>YES: Reinitialize session and start again! <b>All existing data will be lost!</b>" + "<br>NO: Session will not be run again!</html></body>";

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
