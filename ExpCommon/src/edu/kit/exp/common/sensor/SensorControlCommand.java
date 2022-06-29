package edu.kit.exp.common.sensor;

/**
 * 
 * @author Tonda
 *
 */
public enum SensorControlCommand {

	/** The physio control gets configured. */
	CONFIGURE,

	/** The recording starts. */
	START_RECORDING,

	/** The recording stops. */
	STOP_RECORDING, 

	/** A sensor specific command */
	SENSOR_SPECIFIC
}
