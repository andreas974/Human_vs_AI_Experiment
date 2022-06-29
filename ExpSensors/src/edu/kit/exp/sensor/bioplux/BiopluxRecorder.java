package edu.kit.exp.sensor.bioplux;

import edu.kit.exp.common.Constants;
import edu.kit.exp.common.LogHandler;
import edu.kit.exp.common.RecordingException;
import edu.kit.exp.common.files.FileManager;
import edu.kit.exp.common.sensor.ISensorRecorder;
import plux.newdriver.bioplux.BPException;
import plux.newdriver.bioplux.Device;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class BiopluxRecorder extends ISensorRecorder<BiopluxRecorderConfiguration> {

	private final static String TEXT_SEPARATOR = ";";
	private String macAddress;
	private Date samplingTime;
	private Date receiveTime;
	
	private FileWriter fileWrite = null;
	private Device dev;

	public BiopluxRecorder() {

	}

	public String getMenuText() {
		return "Bioplux";
	}

	@Override
	public boolean configureRecorder(BiopluxRecorderConfiguration configuration) {
		try {
			this.macAddress = extractMadAdressFromProperties(Constants.getComputername());
		} catch (Exception e) {
			this.macAddress = "demo";
			LogHandler.printException(e);
			return false;
		}
		
		//Initiate Outputfile + write header
		try {
			fileWrite = FileManager.getInstance().getFileWriter(getMenuText());

			fileWrite.write("ReceiveTime");
			fileWrite.write(";SampleTime");
			fileWrite.write(";PlugSeqNo");
			for (int i = 0; i < configuration.channelsCount; i++) {
				fileWrite.write(";Input" + (i + 1));
			}
			fileWrite.write("\n");

		} catch (IOException e) {
			LogHandler.printException(e, "Can't access output file");
			return false;
		}
		
		try {
			dev = new Device(macAddress);
		} catch (Exception e) {
			LogHandler.printException(e, "Can't create BioPlux object");
			return false;
		}
		return true;
	}

	public void Recording() throws RecordingException {
		
		try {
			FileManager.getInstance().updateStartTime(fileWrite);
			
			BiopluxRecorderConfiguration config = this.getSensorRecorderConfiguration();

			LogHandler.printInfo("Connected to: " + macAddress + " - " + dev.GetDescription());

			Device.Frame[] frames = new Device.Frame[config.frameSize];
			for (int i = 0; i < config.frameSize; i++) {
				frames[i] = new Device.Frame();
			}
			samplingTime = new Date();
			dev.BeginAcq(config.samplingRate, config.channels, config.resolution);

			while (this.isCapturingActive()) {
				dev.GetFrames(config.frameSize, frames);
				receiveTime = new Date();

				// System.out.println(GetStringFromFrame(frames[0], " ")); //ShowConsoleOutput

				for (int i = 0; i < config.frameSize; i++) {
					samplingTime.setTime(samplingTime.getTime() + 1);
					fileWrite.write(getStringFromFrame(frames[i]));
					fileWrite.write("\n");
				}

				fileWrite.flush();
			}

		} catch (BPException e) {
			LogHandler.printException(e, "Exception Code: " + e.code);
		} catch (IOException e) {
			LogHandler.printException(e, "Can't write output file");
		}
	}

	public void cleanupRecorder() {
		try {
			dev.Close();
			fileWrite.close();
		} catch (BPException e) {
			LogHandler.printException(e, "Exception Code: " + e.code);
		} catch (IOException e) {
			LogHandler.printException(e);
		}
	}

	private String getStringFromFrame(Device.Frame frame) {
		StringBuffer returnValue = new StringBuffer(128);

		returnValue.append(receiveTime.getTime());
		returnValue.append(TEXT_SEPARATOR);
		returnValue.append(samplingTime.getTime());
		returnValue.append(TEXT_SEPARATOR);
		returnValue.append(String.format("%03d", frame.seq));
		for (int j = 0; j < this.getSensorRecorderConfiguration().channelsCount; j++) {
			returnValue.append(TEXT_SEPARATOR);
			returnValue.append(String.format("%04d", frame.an_in[j]));
		}

		return returnValue.toString();
	}

	private String extractMadAdressFromProperties(String hostName) throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		// Resource path: "/edu/kit/exp/sensor/bioplux/mcaddresses.properties"
		prop.load(getClass().getResourceAsStream("/" + getClass().getPackage().getName().replace('.', '/') + "/mcaddresses.properties"));
		for (String key : prop.stringPropertyNames()) {
			if (key.equalsIgnoreCase(hostName)) {
				return prop.getProperty(key);
			}
		}
		throw new IllegalArgumentException("Hostname could not be found in the macadresses.properties file.");
	}
}
