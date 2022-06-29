package edu.kit.exp.common.sensor;

/**
 * Created by tondaroder on 12.08.16.
 */
public class SensorControlStopRecording implements ISensorControlObject {

	private static final long serialVersionUID = -7719557574403413591L;

	@Override public SensorControlCommand returnCommand() {
		return SensorControlCommand.STOP_RECORDING;
	}
}
