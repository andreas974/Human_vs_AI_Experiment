package edu.kit.exp.sensor.audio;

import edu.kit.exp.common.sensor.ISensorRecorderConfiguration;
import edu.kit.exp.common.sensor.SensorConfigurationElement;

public class AudioRecorderConfiguration extends ISensorRecorderConfiguration {

	private static final long serialVersionUID = 2938755541521538490L;

	@SensorConfigurationElement(name = "Sampling Rate", description = "Value in kHz.")
	public int sampleRate;

	@SensorConfigurationElement(name = "Sampling Size", description = "Value in Bits.")
	public int sampleSize;

	int channels = 2;
	boolean signed = true;
	boolean bigEndian = true;

	@Override
	public void setDefaultValues() {
		sampleRate = 16000;
		sampleSize = 8;
	}
}
