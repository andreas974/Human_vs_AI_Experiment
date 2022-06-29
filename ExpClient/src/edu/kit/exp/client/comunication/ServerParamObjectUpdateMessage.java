package edu.kit.exp.client.comunication;

import edu.kit.exp.common.IScreenParamObject;

/**
 * The class ServerParamObjectUpdateMessage provides a ServerMessage for
 * updating screen parameters.
 * 
 * @see IScreenParamObject
 */
public class ServerParamObjectUpdateMessage extends ServerMessage {

	/** The parameter object update. */
	IScreenParamObject parameterObjectUpdate;

	/**
	 * This method gets the update of the parameters.
	 * 
	 * @return the parameter object update
	 */
	public IScreenParamObject getParameterObjectUpdate() {
		return parameterObjectUpdate;
	}

	/**
	 * This method sets the new parameters.
	 * 
	 * @param parameterObjectUpdate
	 *            The IScreenParamObject variable with the new parameters.
	 */
	public void setParameterObjectUpdate(IScreenParamObject parameterObjectUpdate) {
		this.parameterObjectUpdate = parameterObjectUpdate;
	}

	/**
	 * This constructor instantiates a new server param object update message.
	 * 
	 * @param parameter
	 *            the IScreenParamObject screen parameters
	 */
	public ServerParamObjectUpdateMessage(IScreenParamObject parameter) {
		parameterObjectUpdate = parameter;
	}

}
