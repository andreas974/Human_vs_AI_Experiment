package edu.kit.exp.common.sensor;

import java.lang.reflect.ParameterizedType;

import edu.kit.exp.common.LogHandler;
import edu.kit.exp.common.RecordingException;

public abstract class ISensorRecorder<T extends ISensorRecorderConfiguration> implements Runnable {

	private Thread captureThread;
	private boolean capturingActive;
	private Class<T> configurationClass;
	T sensorRecorderConfiguration;
	private SensorStatus sensorStatus;

    private final Object lock = new Object();

	@SuppressWarnings("unchecked")
	public ISensorRecorder() {
		String className = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();
		try {
			configurationClass = (Class<T>) Class.forName(className);
			T newConf = configurationClass.newInstance();
			this.setSensorRecorderConfiguration(newConf);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		captureThread = new Thread(this);
		capturingActive = false;
		sensorStatus = new SensorStatus(getMenuText());
	}

	public boolean isCapturingActive(){
	    synchronized (lock) {
            return capturingActive;
        }
	}
	
	public Class<T> getConfigurationClass() {
		return configurationClass;
	}

	public void setSensorRecorderConfiguration(T configuration) {
		this.sensorRecorderConfiguration = configuration;
	}

	public T getSensorRecorderConfiguration() {
		return this.sensorRecorderConfiguration;
	}
	
	public SensorStatus getSensorStatus(){
		return this.sensorStatus;
	}

	/**
	 * This method is used to start a sensor recording. The implementing object
	 * should handle file types and execution commands.
	 */
	public void startRecording() throws RecordingException {
		LogHandler.printInfo("Starting the recording for: " + getMenuText());
		capturingActive = true;
		captureThread.start();
	}

	@Override
	public void run() {
		try {
			Recording();
		} catch (RecordingException e) {
			LogHandler.printException(e);
			getSensorStatus().setSensorStatus(false, "Recording stopped with error.");
		}
		
		if (isCapturingActive()) {
			getSensorStatus().setSensorStatus(false, "Recording stopped prematurely.");
		}
	}

	/**
	 * This method is used to stop sensor recording. The implementing object
	 * should handle file types and execution commands.
	 */
	public void stopRecording() {
	    LogHandler.printInfo("Stopping the recording for: " + getMenuText());
        synchronized (lock) {
            capturingActive = false;
        }
		try {
			Thread.sleep(1100);
		} catch (InterruptedException e) {
			LogHandler.printException(e);
		}
		cleanupRecorder();
		getSensorStatus().setSensorStatus(false, "Recording completed.");
	}

	////////////////////////////////
	// Abstract methods
	////////////////////////////////

	/**
	 * This method provides the menu text for this sensor, which is shown in the
	 * server gui.
	 *
	 */
	public abstract String getMenuText();

	/**
	 * This method configures the recorder with custom parameters
	 * 
	 * @param configuration
	 * @return return true if configuring was successful. Otherwise false.
	 */
	public abstract boolean configureRecorder(T configuration);

	/**
	 * This method is used to capture sensor recording data in its own thread.
	 * The implementing object should handle file types and execution commands.
	 * 
	 * Only exit this method, when {@link #isCapturingActive() isCapturingActive} is turns false.
	 */
	public abstract void Recording() throws RecordingException;

	/**
	 * This method is used to cleanup sensor recording. The implementing object
	 * should handle file types and execution commands.
	 */
	public abstract void cleanupRecorder();

}
