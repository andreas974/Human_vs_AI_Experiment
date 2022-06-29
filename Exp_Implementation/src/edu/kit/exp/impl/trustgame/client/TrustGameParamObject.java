package edu.kit.exp.impl.trustgame.client;

import edu.kit.exp.client.gui.screens.Screen.ParamObject;

public class TrustGameParamObject extends ParamObject {

	private static final long serialVersionUID = 6414993119329286078L;

	private String message;
	private int input;

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the input
	 */
	public int getInput() {
		return input;
	}

	/**
	 * @param input
	 *            the input to set
	 */
	public void setInput(int input) {
		this.input = input;
	}

}
