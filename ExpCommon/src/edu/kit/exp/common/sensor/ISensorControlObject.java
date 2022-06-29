package edu.kit.exp.common.sensor;

import java.io.Serializable;

/**
 *
 * generic PhysioControlObject for Server-to-Client communication
 *
 * Created by tondaroder on 12.08.16.
 */
public interface ISensorControlObject extends Serializable {

	SensorControlCommand returnCommand();

}
