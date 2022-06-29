package edu.kit.exp.server.communication;

import edu.kit.exp.server.jpa.entity.Trial;

/**
 * This class provides a wrapper method to transmit client side decisions and
 * data, as trial objects. All trial objects, along with client timestamps are
 * persisted on the server side.
 * 
 * @see ClientMessage
 */
public class ClientTrialLogMessage extends ClientMessage {

	/** The trial. */
	private Trial trial;

	/** The game id. */
	private String gameId;

	/**
	 * This constructor instantiates a new client trial log message.
	 * 
	 * @param clientId
	 *            the client ID
	 */
	protected ClientTrialLogMessage(String clientId) {
		super(clientId);

	}

	/**
	 * This constructor instantiates a new client trial log message.
	 * 
	 * @param clientId
	 *            A String which contains the client ID.
	 * @param gameId
	 *            A String which contains the game ID.
	 * @param screenName
	 *            A String which contains the screen name.
	 * @param valueName
	 *            A String which contains the event.
	 * @param value
	 *            A String which contains the value.
	 * @param clientTimeStamp
	 *            A Long which contains the client timestamp.
	 * @param serverTimeStamp
	 *            A Long which contains the server timestamp.
	 */
	public ClientTrialLogMessage(String clientId, String gameId, String valueName, String screenName, String value, Long clientTimeStamp, Long serverTimeStamp) {
		super(clientId);

		this.setGameId(gameId);
		this.trial = new Trial();
		trial.setClientTime(clientTimeStamp);
		trial.setServerTime(serverTimeStamp);
		trial.setScreenName(screenName);
		trial.setValueName(valueName);
		trial.setValue(value);

	}

	/**
	 * This methods gets the game ID.
	 * 
	 * @return the game ID
	 */
	public String getGameId() {
		return gameId;
	}

	/**
	 * This method sets the game ID.
	 * 
	 * @param gameId
	 *            A String that represents the new game ID.
	 */
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	/**
	 * This method gets the trial.
	 * 
	 * @return the trial
	 */
	public Trial getTrial() {
		return trial;
	}

	/**
	 * This method sets the trial.
	 * 
	 * @param trial
	 *            A Trial that contains the new trial.
	 */
	public void setTrial(Trial trial) {
		this.trial = trial;
	}

	/**
	 * This method hashes the <code>gameId</code> and the <code>trial</code> .
	 * 
	 * @return some hashed parameters
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((gameId == null) ? 0 : gameId.hashCode());
		result = prime * result + ((trial == null) ? 0 : trial.hashCode());
		return result;
	}

	/**
	 * Compares an <code>Object</code> to the current
	 * <code>ClientTrialLogMessage</code> object.
	 * 
	 * @param obj
	 *            A Object parameter.
	 * @return <code>true</code> if the given object is this
	 *         <code>ClientTrialLogMessage</code>,<code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientTrialLogMessage other = (ClientTrialLogMessage) obj;
		if (gameId == null) {
			if (other.gameId != null)
				return false;
		} else if (!gameId.equals(other.gameId))
			return false;
		if (trial == null) {
			if (other.trial != null)
				return false;
		} else if (!trial.equals(other.trial))
			return false;
		return true;
	}

	/**
	 * This method prints out the <code>trial</code>, the <code>gameId</code>
	 * and the <code>clientId</code>.
	 * 
	 * @return a String with some parameters
	 */
	@Override
	public String toString() {
		return "ClientTrialLogMessage [trial=" + trial + ", gameId=" + gameId + ", clientId=" + clientId + "]";
	}

}
