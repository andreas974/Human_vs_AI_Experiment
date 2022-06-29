package edu.kit.exp.impl.publicgoodsgames.client;

import edu.kit.exp.client.gui.screens.Screen.ParamObject;

public class PublicGoodsGameParamObject extends ParamObject {

	private static final long serialVersionUID = -350404968241509835L;

	private String message;
	private double money; // amount of money of subject
	private double input; // input of the subject

	/**
	 * @return the input
	 */
	public double getInput() {
		return input;
	}

	/**
	 * @param input
	 *            the input to set
	 */
	public void setInput(double input) {
		this.input = input;
	}

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
	 * @return the money
	 */
	public double getMoney() {
		return money;
	}

	/**
	 * @param money
	 *            the money to set
	 */
	public void setMoney(double money) {
		this.money = money;
	}

}
