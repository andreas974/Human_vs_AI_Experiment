package edu.kit.exp.common.sensor;

import java.util.EventListener;

public abstract class LBFUpdateEvent implements EventListener {
	
	/**
	 * Every time a new LBF-value is generated, this method is called
	 * 
	 * @param value Latest LBF-Value
	 */
	public abstract void LBFValueUpdate(Object value);
	
}
