package edu.kit.exp.server.communication;

import edu.kit.exp.common.IScreenParamObject;

// TODO: Auto-generated Javadoc
/**
 * This class is used for client responses.
 * 
 */
public class ClientResponseMessage extends ClientMessage {

	/** The parameters. */
	private IScreenParamObject parameters;

	/** The game id. */
	private String gameId;

	/** The screen id. */
	private String screenId;

	/** The client timestamp. */
	private Long clientTimeStamp;

	/** The server timestamp. */
	private Long serverTimeStamp;

	/**
	 * This method instantiates a new client response message.
	 * 
	 * @param clientId
	 *            A String that contains the client ID.
	 */
	private ClientResponseMessage(String clientId) {
		super(clientId);
	}

	/**
	 * This method instantiates a new client response message.
	 * 
	 * @param clientId
	 *            A String that contains the client ID.
	 * @param parameters
	 *            The IScreenParamObject Parameters send by the client screen.
	 *            Those must be known by the receiving instance.
	 * @param gameId
	 *            The String ID of the running game which has to receive and
	 *            process this message.
	 * @param screenId
	 *            The String ID of the screen that triggered this message.
	 * @param clientTimeStamp
	 *            A Long which contains the time in milliseconds at which a
	 *            screen triggered this message (i.e. pressing a button).
	 * @param serverTimeStamp
	 *            A Long which contains the time in milliseconds at which this
	 *            message was received by the server.
	 */
	public ClientResponseMessage(String clientId, IScreenParamObject parameters, String gameId, String screenId, Long clientTimeStamp, Long serverTimeStamp) {
		super(clientId);
		this.setParameters(parameters);
		this.gameId = gameId;
		this.screenId = screenId;
		this.clientTimeStamp = clientTimeStamp;
		this.setServerTimeStamp(serverTimeStamp);
	}

	/**
	 * This method gets the parameters.
	 * 
	 * @param <T>
	 *            A generic type.
	 * @return the parameters
	 */
	@SuppressWarnings("unchecked")
	public <T extends IScreenParamObject> T getParameters() {
		return (T) parameters;
	}

	/**
	 * This method sets the parameters.
	 * 
	 * @param parameters
	 *            A IScreenParamObject which defines the new parameters.
	 */
	public void setParameters(IScreenParamObject parameters) {
		this.parameters = parameters;
	}

	/**
	 * This method gets the game ID.
	 * 
	 * @return the game ID
	 */
	public String getGameId() {
		return gameId;
	}

	/**
	 * This method sets the game ID.
	 * 
	 * @param messageQueueId
	 *            A String that contains the new game ID.
	 */
	public void setGameId(String messageQueueId) {
		this.gameId = messageQueueId;
	}

	/**
	 * This method gets the screen ID.
	 * 
	 * @return the screen ID
	 */
	public String getScreenId() {
		return screenId;
	}

	/**
	 * This method sets the screen ID.
	 * 
	 * @param screenId
	 *            A String that contains the new screen ID.
	 */
	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

	/**
	 * This method gets the client time stamp.
	 * 
	 * @return the client time stamp
	 */
	public Long getClientTimeStamp() {
		return clientTimeStamp;
	}

	/**
	 * This method sets the client time stamp.
	 * 
	 * @param clientTimeStamp
	 *            A Long that represents the new client time stamp.
	 */
	public void setClientTimeStamp(Long clientTimeStamp) {
		this.clientTimeStamp = clientTimeStamp;
	}

	/**
	 * This method hashes the <code>clientTimpeStamp</code>, the
	 * <code>gameId</code>, the <code>parameters</code> and the
	 * <code>screenId</code>.
	 * 
	 * @return some hashed parameters
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((clientTimeStamp == null) ? 0 : clientTimeStamp.hashCode());
		result = prime * result + ((gameId == null) ? 0 : gameId.hashCode());
		result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
		result = prime * result + ((screenId == null) ? 0 : screenId.hashCode());
		return result;
	}

	/**
	 * Compares an <code>Object</code> to the current
	 * <code>ClientResponseMessage</code> object.
	 * 
	 * @param obj
	 *            A Object parameter.
	 * @return <code>true</code> if the given object is this
	 *         <code>ClientResponseMessage</code>,<code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientResponseMessage other = (ClientResponseMessage) obj;
		if (clientTimeStamp == null) {
			if (other.clientTimeStamp != null)
				return false;
		} else if (!clientTimeStamp.equals(other.clientTimeStamp))
			return false;
		if (gameId == null) {
			if (other.gameId != null)
				return false;
		} else if (!gameId.equals(other.gameId))
			return false;
		if (parameters == null) {
			if (other.parameters != null)
				return false;
		} else if (!parameters.equals(other.parameters))
			return false;
		if (screenId == null) {
			if (other.screenId != null)
				return false;
		} else if (!screenId.equals(other.screenId))
			return false;
		return true;
	}

	/**
	 * This method prints out the <code>parameters</code>, the
	 * <code>gameId</code>, the <code>screenId</code>, the
	 * <code>clientTimeStamp</code> and the <code>clientId</code>.
	 * 
	 * @return a String with some parameters
	 */
	@Override
	public String toString() {
		return "ClientResponseMessage [parameters=" + parameters + ", gameId=" + gameId + ", screenId=" + screenId + ", clientTimeStamp=" + clientTimeStamp + ", clientId=" + clientId + "]";
	}

	/**
	 * This method gets the server timestamp.
	 * 
	 * @return the server time stamp
	 */
	public Long getServerTimeStamp() {
		return serverTimeStamp;
	}

	/**
	 * This method sets the server timestamp.
	 * 
	 * @param serverTimeStamp
	 *            A Long that contains the new server timestamp.
	 */
	public void setServerTimeStamp(Long serverTimeStamp) {
		this.serverTimeStamp = serverTimeStamp;
	}

}
