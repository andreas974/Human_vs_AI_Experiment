package edu.kit.exp.sensor.bioplux;

import edu.kit.exp.common.sensor.ISensorRecorderConfiguration;
import edu.kit.exp.common.sensor.SensorConfigurationElement;

public class BiopluxRecorderConfiguration extends ISensorRecorderConfiguration {

	private static final long serialVersionUID = 3569410039370411644L;

	@SensorConfigurationElement(name = "Sampling Rate", description = "Value in Hz. Max value is 1000.")
	public int samplingRate;

	@SensorConfigurationElement(name = "Sampling Frame Size", description = "frame buffer size that is transfered from the device to the pc per request (in bits). Max value is 1000.")
	public int frameSize = 1000;
	
	@SensorConfigurationElement(name = "Measurement Resulution", description = "Value rage of measurements in bits. E.g. 12 = 12 bit = measurement data range of 4096. Max value is 12.")
	public int resolution;
	
	public final short channels = 0xFF; // binary on/off
	public final int channelsCount = 8; // must equal the representation of _channels
	
	public void setDefaultValues() {
		samplingRate = 1000;
		resolution = 12;
	}
}
