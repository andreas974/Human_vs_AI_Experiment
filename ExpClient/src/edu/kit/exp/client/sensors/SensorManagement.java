package edu.kit.exp.client.sensors;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import edu.kit.exp.client.comunication.ClientStatusManager;
import edu.kit.exp.client.comunication.ServerSensorCommandMessage;
import edu.kit.exp.common.RecordingException;
import edu.kit.exp.common.sensor.ISensorControlObject;
import edu.kit.exp.common.sensor.ISensorRecorder;
import edu.kit.exp.common.sensor.ISensorRecorderConfiguration;
import edu.kit.exp.common.sensor.SensorControlCommand;
import edu.kit.exp.common.sensor.SensorControlInitialize;

/**
 * SensorManagement instantiates ISensorRecorder objects which are received from
 * the server
 * 
 * Created by tondaroder on 08.08.16.
 */
public class SensorManagement {

	private static SensorManagement instance;
	private List<ISensorRecorder<?>> recorderList = new ArrayList<ISensorRecorder<?>>();

	public static SensorManagement getInstance() {

		if (instance == null) {
			instance = new SensorManagement();
		}
		return instance;
	}
	
	public void processSensorCommandMessage(ServerSensorCommandMessage message) throws IllegalAccessException, ClassNotFoundException, InstantiationException, RecordingException, RemoteException {
		ISensorControlObject object = message.getObject();
		SensorControlCommand command = object.returnCommand();
		if (command.equals(SensorControlCommand.START_RECORDING)) {
			startSensors();
		} else if (command.equals(SensorControlCommand.STOP_RECORDING)) {
			stopSensors();
		} else if (command.equals(SensorControlCommand.CONFIGURE)) {
			configureSensors((SensorControlInitialize) object);
		}
	}

	public void startSensors() throws RecordingException {
		for (ISensorRecorder<?> recorder : recorderList) {
			recorder.startRecording();
		}
	}

	public void stopSensors() {
		recorderList.forEach(ISensorRecorder::stopRecording);
	}

	@SuppressWarnings({ "unchecked" })
	private void configureSensors(SensorControlInitialize object)
			throws IllegalAccessException, InstantiationException, ClassNotFoundException {
		String fullyQualifiedName = object.getSensorName();
		ISensorRecorderConfiguration configuration = object.getConfiguration();
		Class<ISensorRecorder<ISensorRecorderConfiguration>> recorderClass = (Class<ISensorRecorder<ISensorRecorderConfiguration>>) Class.forName(fullyQualifiedName);
		ISensorRecorder<ISensorRecorderConfiguration> recorder = recorderClass.newInstance();
		recorder.setSensorRecorderConfiguration(configuration);
		boolean configOk = recorder.configureRecorder(recorder.getSensorRecorderConfiguration());
		recorder.getSensorStatus().setSensorStatus(configOk, configOk ? "" : "Configuring sensor faild.");
		recorderList.add(recorder);
		
		ClientStatusManager.getInstance().getClientStatus().addSensorStatus(recorder.getSensorStatus());
	}
}
