package edu.kit.exp.client.comunication;

import edu.kit.exp.common.sensor.ISensorControlObject;

public class ServerSensorCommandMessage extends ServerMessage {
	private ISensorControlObject object;


	public ServerSensorCommandMessage(ISensorControlObject object) {
		setObject(object);
	}

	public ISensorControlObject getObject() {
		return object;
	}

	private void setObject(ISensorControlObject object) {
		this.object = object;
	}
}
