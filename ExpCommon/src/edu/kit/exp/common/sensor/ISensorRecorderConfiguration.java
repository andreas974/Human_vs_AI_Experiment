package edu.kit.exp.common.sensor;

import java.io.Serializable;

public abstract class ISensorRecorderConfiguration implements Serializable {

	private static final long serialVersionUID = -9201688494635933166L;

	public ISensorRecorderConfiguration(){
		setDefaultValues();
	}
	
	public abstract void setDefaultValues();
}