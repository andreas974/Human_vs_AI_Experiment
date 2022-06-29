package edu.kit.exp.server.structure;

/**
 * The Class StructureManagementException defines an exception if something went
 * wrong in the structure management layer.
 */
public class StructureManagementException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7245168988709666841L;

	/** The message. */
	private String message;

	/**
	 * This constructor instantiates a new structure management exception.
	 * 
	 * @param msg
	 *            the String msg to be set as an error message
	 */
	public StructureManagementException(String msg) {

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
