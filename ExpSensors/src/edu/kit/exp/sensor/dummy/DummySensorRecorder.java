package edu.kit.exp.sensor.dummy;

import java.io.FileWriter;
import java.io.IOException;

import edu.kit.exp.common.LogHandler;
import edu.kit.exp.common.RecordingException;
import edu.kit.exp.common.files.FileManager;
import edu.kit.exp.common.sensor.ISensorRecorder;
import edu.kit.exp.common.sensor.LBFManager;

public class DummySensorRecorder extends ISensorRecorder<DummySensorConfiguration> {

	private FileWriter fileWrite = null;

	@Override
	public String getMenuText() {
		return "Dummy Sensor";
	}

	@Override
	public boolean configureRecorder(DummySensorConfiguration configuration) {
		// Open output file
		if (configuration.writeToFile) {
			try {
				fileWrite = FileManager.getInstance().getFileWriter("DummySensor");
				fileWrite.write("Dummy Sensor Values\n");

			} catch (IOException e) {
				LogHandler.printException(e, "Can't access output file");
				return false;
			}
		}
		
		return true;
	}

	@Override
	public void Recording() throws RecordingException {
		// While recording is active
		while (this.isCapturingActive()) {

			// Generate random sensor value
			Double dummyValue = Math.random();

			// Write value to file
			if (this.getSensorRecorderConfiguration().writeToFile) {
				try {
					fileWrite.write(dummyValue.toString() + "\n");
					fileWrite.flush();
				} catch (IOException e) {
					LogHandler.printException(e);
					getSensorStatus().setSensorStatus(false, "Can't write file header");
				}
			}

			//Here, e.g., xAffect Calculations....
			//dummyValue = Result from xAffect
			
			// Add Value to LBFManager. Use name of this class as value-key.
			LBFManager.getInstance().updateLBFValue(DummySensorRecorder.class.getName(), dummyValue);

			// Wait to generate next value
			try {
				Thread.sleep(this.getSensorRecorderConfiguration().sleepTime);
			} catch (InterruptedException e) {
				LogHandler.printException(e);
				getSensorStatus().setSensorStatus(false);
			}
		}
	}

	@Override
	public void cleanupRecorder() {
		// Close output file
		if (this.getSensorRecorderConfiguration().writeToFile) {
			try {
				fileWrite.close();
			} catch (IOException e) {
				LogHandler.printException(e);
			}
		}
	}

}
