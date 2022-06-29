package edu.kit.exp.impl.prisonersdilemmagame.client;

import edu.kit.exp.client.gui.screens.Screen.ParamObject;

public class PrisonersDilemmaGameParamObject extends ParamObject {

	private static final long serialVersionUID = -2015807062666089285L;

	private String message;
	private String input;

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
	public String getInput() {
		return input;
	}

	/**
	 * @param input
	 *            the input to set
	 */
	public void setInput(String input) {
		this.input = input;
	}
}
