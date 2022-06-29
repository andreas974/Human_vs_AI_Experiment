package edu.kit.exp.common;

import edu.kit.exp.common.sensor.SensorStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClientStatus implements Serializable, Comparable<ClientStatus> {

	private static final long serialVersionUID = -9102297680843260215L;

	private String clientId = "";
	private List<SensorStatus> sensors;
	private Long statusReceiveTime;
	private String currentScreenName;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getHostName() {
		return Constants.getComputername();
	}

	public List<SensorStatus> getSensors() {
		return sensors;
	}

	public String getCurrentScreenName() {
		return currentScreenName;
	}

	public void setCurrentScreenName(String currentScreenName) {
		this.currentScreenName = currentScreenName;
	}

	public void setSensors(List<SensorStatus> sensors) {
		this.sensors = sensors;
	}

	public ClientStatus() {
		sensors = new ArrayList<>();
	}

	public void addSensorStatus(SensorStatus sensorStatus) {
		if (!getSensors().contains(sensorStatus)) {
			getSensors().add(sensorStatus);
		}
	}

	public Long getStatusReceiveTime() {
		return statusReceiveTime;
	}

	public void setStatusReceiveTime(Long statusTime) {
		this.statusReceiveTime = statusTime;
	}

	public boolean isUpToDate() {
		return Calendar.getInstance().getTimeInMillis() - getStatusReceiveTime() < 1500;
	}
	
	public boolean allSensorsOk(){
		for (SensorStatus sensorStatus : sensors) {
			if (!sensorStatus.isStatusOk()){
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return clientId + (this.isUpToDate() ? " (NO CONNECTION)" : "");
	}

	@Override
	public int compareTo(ClientStatus o) {
		String thisName = (this.isUpToDate() && this.allSensorsOk() ? "1" : "0") + this.getClientId();
		String oName = (o.isUpToDate() && o.allSensorsOk() ? "1" : "0") + o.getClientId();
		return thisName.compareTo(oName);
	}
}
