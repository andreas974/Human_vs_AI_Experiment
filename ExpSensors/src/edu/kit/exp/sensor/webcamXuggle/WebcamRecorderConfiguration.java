package edu.kit.exp.sensor.webcamXuggle;

import edu.kit.exp.common.sensor.ISensorRecorderConfiguration;
import edu.kit.exp.common.sensor.SensorConfigurationElement;

public class WebcamRecorderConfiguration extends ISensorRecorderConfiguration {

	private static final long serialVersionUID = 2260389310508757419L;

    @SensorConfigurationElement(name = "Resolution", description = "Resolution standard.")
    public WebcamRecorderResolution resolution;

    @SensorConfigurationElement(name = "Frame rate", description = "(Maximum) frame rate in fps.")
    public int fps;

    @SensorConfigurationElement(name = "Codec", description = "Encoding codec.")
    public WebcamRecorderCodec codec;

    @Override
    public void setDefaultValues() {
        resolution = WebcamRecorderResolution.VGA;
        fps = 30;
        codec = WebcamRecorderCodec.THEORA;
    }
}