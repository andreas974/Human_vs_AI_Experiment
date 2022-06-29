package edu.kit.exp.common.sensor;

/**
 * Created by tondaroder on 12.08.16.
 */
public class SensorControlStartRecording implements ISensorControlObject {

	private static final long serialVersionUID = 4032359002854057600L;

	@Override public SensorControlCommand returnCommand() {
		return SensorControlCommand.START_RECORDING;
	}
}
