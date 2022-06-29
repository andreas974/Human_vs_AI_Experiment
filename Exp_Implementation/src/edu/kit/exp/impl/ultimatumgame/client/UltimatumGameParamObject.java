package edu.kit.exp.impl.ultimatumgame.client;

import edu.kit.exp.client.gui.screens.Screen.ParamObject;

public class UltimatumGameParamObject extends ParamObject {

	private static final long serialVersionUID = 5545747155672141767L;

	private String infoText;

	/**
	 * This method gets an info text.
	 * 
	 * @return the info text
	 */
	public String getInfoText() {
		return infoText;
	}

	/**
	 * This method sets the info text.
	 * 
	 * @param infoText
	 *            A String that contains the new info text.
	 */
	public void setInfoText(String infoText) {
		this.infoText = infoText;
	}
}
