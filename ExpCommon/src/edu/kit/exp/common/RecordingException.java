package edu.kit.exp.common;

public class RecordingException extends Exception {

	private static final long serialVersionUID = -6820777160909843318L;
	
	private String message;

	public RecordingException(String msg) {

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
