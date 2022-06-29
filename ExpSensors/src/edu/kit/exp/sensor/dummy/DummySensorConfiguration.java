package edu.kit.exp.sensor.dummy;

import edu.kit.exp.common.sensor.ISensorRecorderConfiguration;
import edu.kit.exp.common.sensor.SensorConfigurationElement;

public class DummySensorConfiguration extends ISensorRecorderConfiguration {

	private static final long serialVersionUID = 8915176324075610016L;

	@SensorConfigurationElement(name = "Sleep time", description = "Time in millisecond between two randomly generated values.")
	public int sleepTime;
	
	@SensorConfigurationElement(name = "Write to file", description = "If selected, an output file is written.")
	public boolean writeToFile;
	
	@Override
	public void setDefaultValues() {
		sleepTime = 500;
		writeToFile = false;
	}

}
