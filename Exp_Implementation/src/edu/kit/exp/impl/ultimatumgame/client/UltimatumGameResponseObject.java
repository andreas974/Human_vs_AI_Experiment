/**
 * 
 */
package edu.kit.exp.impl.ultimatumgame.client;

import edu.kit.exp.client.gui.screens.Screen.ParamObject;
import edu.kit.exp.client.gui.screens.Screen.ResponseObject;

/**
 * The class UltimatumGameParamObject contains parameters for the UltimatumGame
 * screen.
 * 
 * @see ParamObject
 */
public class UltimatumGameResponseObject extends ResponseObject {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3631582980858931663L;

	private String inputValue;
	private Boolean actionPerformed;

	/**
	 * This method gets an input value.
	 * 
	 * @return the input value
	 */
	public String getInputValue() {
		return inputValue;
	}

	/**
	 * This method sets a new input value.
	 * 
	 * @param inputValue
	 *            A String that contains the new input value.
	 */
	public void setInputValue(String inputValue) {
		this.inputValue = inputValue;
	}

	/**
	 * This method gets a value that indicates if an action was performed (true)
	 * or not (false).
	 * 
	 * @return a variable which shows if an action was performed (true) or not
	 *         (false)
	 */
	public Boolean getActionPerformed() {
		return actionPerformed;
	}

	/**
	 * This method sets a value that indicates if an action was performed (true)
	 * or not (false).
	 * 
	 * @param actionPerformed
	 *            A Boolean variable which shows if an action was performed
	 *            (true) or not (false).
	 */
	public void setActionPerformed(Boolean actionPerformed) {
		this.actionPerformed = actionPerformed;
	}

}
