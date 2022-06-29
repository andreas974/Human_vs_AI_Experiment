package edu.kit.exp.common.sensor;

import java.io.Serializable;

public class SensorStatus implements Serializable{

	private static final long serialVersionUID = 71175094167140446L;
	
	private String sensorName;
	private String message;
	private boolean statusOk;

	public String getMessage() {
		return message;
	}

	public boolean isStatusOk() {
		return statusOk;
	}
	
	public String getSensorName(){
		return this.sensorName;
	}

	public SensorStatus(String sensorName) {
		this.sensorName = sensorName;
		setSensorStatus(false, "");
	}
	
	public void setSensorStatus(boolean statusOk) {
		setSensorStatus(statusOk, getMessage());
	}
	
	public void setSensorStatus(String message) {
		setSensorStatus(isStatusOk(), message);
	}
	
	public void setSensorStatus(boolean statusOk, String message){
		this.statusOk = statusOk;
		this.message= message;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass().equals(this.getClass())){
			return this.getSensorName().equals(((SensorStatus)obj).getSensorName());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
	    return getSensorName().hashCode();
	}
}
