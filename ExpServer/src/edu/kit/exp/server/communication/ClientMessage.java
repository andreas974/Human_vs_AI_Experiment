package edu.kit.exp.server.communication;

/**
 * This class is an abstract class for creating client messages which are placed
 * in message queues.
 * 
 */
public abstract class ClientMessage {

	/** The client ID. */
	protected String clientId;

	/**
	 * This method instantiates a new client message.
	 * 
	 * @param clientId
	 *            The String client ID.
	 */
	protected ClientMessage(String clientId) {

		this.clientId = clientId;
	}

	/**
	 * This method gets the client ID.
	 * 
	 * @return the client ID.
	 */
	public String getClientId() {
		return clientId;
	}

	/**
	 * This method sets the client ID.
	 * 
	 * @param clientId
	 *            A String that contains the new client ID.
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	/**
	 * This method shows the id of the client which is connected to the
	 * <code>ClientMessage</code>.
	 * 
	 * @return the client ID.
	 */
	@Override
	public String toString() {
		return "ClientMessage [clientId=" + clientId + "]";
	}

	/**
	 * This method returns a hashed ID.
	 * 
	 * @return a hashed client ID.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clientId == null) ? 0 : clientId.hashCode());
		return result;
	}

	/**
	 * O Compares an <code>Object</code> to the current
	 * <code>ClientMessage</code> object.
	 * 
	 * @param obj
	 *            A Object parameter.
	 * @return <code>true</code> if the given object is this
	 *         <code>ClientMessage</code>,<code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientMessage other = (ClientMessage) obj;
		if (clientId == null) {
			if (other.clientId != null)
				return false;
		} else if (!clientId.equals(other.clientId))
			return false;
		return true;
	}

}
