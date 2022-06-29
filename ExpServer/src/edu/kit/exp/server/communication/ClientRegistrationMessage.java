package edu.kit.exp.server.communication;

import edu.kit.exp.common.IClient;

/**
 * The class <code>ClientRegistrationMessage</code> is used to instantiate a
 * <code>ClientMessage</code>. This Message is used to identify a client
 * registration at the server.
 * 
 * @see ClientMessage
 */
public class ClientRegistrationMessage extends ClientMessage {

	/** The client remote object. */
	private IClient clientRemoteObject;

	/**
	 * This method instantiates a new client registration message.
	 * 
	 * @param clientId
	 *            A String that represents a client ID.
	 */
	private ClientRegistrationMessage(String clientId) {
		super(clientId);
	}

	/**
	 * This method instantiates a new client registration message.
	 * 
	 * @param clientId
	 *            A String that represents a client ID.
	 * @param clientRemoteObject
	 *            The clientRemoteObject remote handler send with the
	 *            registration request.
	 */
	public ClientRegistrationMessage(String clientId, IClient clientRemoteObject) {
		super(clientId);
		this.setClientRemoteObject(clientRemoteObject);
	}

	/**
	 * This method gets the client remote object.
	 * 
	 * @return the client remote object
	 */
	public IClient getClientRemoteObject() {
		return clientRemoteObject;
	}

	/**
	 * This method sets the client remote object.
	 * 
	 * @param clientRemoteObject
	 *            The IClient new client remote object that is to be set.
	 */
	public void setClientRemoteObject(IClient clientRemoteObject) {
		this.clientRemoteObject = clientRemoteObject;
	}

	/**
	 * This method shows the id of the client which is connected to the
	 * <code>ClientRegistrationMessage</code>.
	 * 
	 * @return the client ID.
	 */
	@Override
	public String toString() {
		return "ClientRegistrationMessage [clientId=" + clientId + "]";
	}

	/**
	 * This method returns a hashed <code>clientRemoteObject</code>.
	 * 
	 * @return a hashed <code>clientRemoteObject</code>.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((clientRemoteObject == null) ? 0 : clientRemoteObject.hashCode());
		return result;
	}

	/**
	 * This method compares an <code>Object</code> to the current
	 * <code>ClientRegistrationMessage</code> object.
	 * 
	 * @param obj
	 *            A Object parameter.
	 * @return <code>true</code> if the given object is this
	 *         <code>ClientRegistrationMessage</code>,<code>false</code>
	 *         otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientRegistrationMessage other = (ClientRegistrationMessage) obj;
		if (clientRemoteObject == null) {
			if (other.clientRemoteObject != null)
				return false;
		} else if (!clientRemoteObject.equals(other.clientRemoteObject))
			return false;
		return true;
	}

}
