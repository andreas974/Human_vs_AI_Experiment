package edu.kit.exp.common.sensor;

/**
 * Created by tondaroder on 12.08.16.
 */
public class SensorControlInitialize implements ISensorControlObject {

	private static final long serialVersionUID = 7712439451133728863L;
	
	ISensorRecorderConfiguration configuration;
	String sensorName;

	public SensorControlInitialize(ISensorRecorderConfiguration configuration, String sensorName) {
		setConfiguration(configuration);
		setSensorName(sensorName);
	}

	public ISensorRecorderConfiguration getConfiguration() {
		return configuration;
	}

	private void setConfiguration(ISensorRecorderConfiguration configuration) {
		this.configuration = configuration;
	}

	public String getSensorName() {
		return sensorName;
	}

	private void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}

	@Override public SensorControlCommand returnCommand() {
		return SensorControlCommand.CONFIGURE;
	}

}
