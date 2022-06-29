package edu.kit.exp.server.gui.sensor;

import edu.kit.exp.common.LogHandler;
import edu.kit.exp.common.ReflectionPackageManager;
import edu.kit.exp.common.sensor.ISensorRecorder;
import edu.kit.exp.common.sensor.ISensorRecorderConfiguration;
import edu.kit.exp.server.jpa.entity.SensorEntry;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class SensorImplementationFinder {

	private static final String SENSOR_IMPLEMENTATION_PATH = "edu.kit.exp.sensor";
	private static SensorImplementationFinder instance;
	private List<ISensorRecorder<ISensorRecorderConfiguration>> sensorInstanceList;
	private List<SensorEntry> sensorEntries;

	public static SensorImplementationFinder getInstance() {
		if (instance == null) {
			instance = new SensorImplementationFinder();
		}
		return instance;
	}

	private SensorImplementationFinder() {
		List<Class<ISensorRecorder<ISensorRecorderConfiguration>>> sensorClassList = new ArrayList<>();
		try {
			sensorClassList = ReflectionPackageManager.getExtendingClasses(SENSOR_IMPLEMENTATION_PATH, ISensorRecorder.class);
		} catch (ClassNotFoundException | IOException | URISyntaxException e) {
			LogHandler.printException(e);
		}
		sensorInstanceList = new ArrayList<>();
		sensorEntries = new ArrayList<>();

		for (Class<ISensorRecorder<ISensorRecorderConfiguration>> currentClass : sensorClassList) {
			try {
				sensorInstanceList.add(currentClass.newInstance());
			} catch (IllegalAccessException | InstantiationException e) {
				LogHandler.printException(e);
			}
		}

		sensorInstanceList.forEach((ISensorRecorder<?> e) -> sensorEntries.add(new SensorEntry(e.getMenuText(), e.getClass().getName(), e.getSensorRecorderConfiguration())));
	}

	public List<ISensorRecorder<ISensorRecorderConfiguration>> getSensorRecorders() {
		return sensorInstanceList;
	}

	public List<SensorEntry> getSensorEntries() {
		return sensorEntries;
	}
}
