package edu.kit.exp.sensor.audio;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import edu.kit.exp.common.LogHandler;
import edu.kit.exp.common.RecordingException;
import edu.kit.exp.common.files.FileManager;
import edu.kit.exp.common.sensor.ISensorRecorder;

public class AudioRecorder extends ISensorRecorder<AudioRecorderConfiguration> {

	private AudioFormat audioFormat;
	private TargetDataLine targetDataLine;
	private DataLine.Info info;
	private AudioFileFormat.Type audioFileFormatType = AudioFileFormat.Type.AU;

	public AudioRecorder() {

	}

	@Override
	public String getMenuText() {
		return "Audio";
	}

	@Override
	public boolean configureRecorder(AudioRecorderConfiguration configuration) {
		info = new DataLine.Info(TargetDataLine.class, audioFormat);
		// checks if system supports the data line
		if (!AudioSystem.isLineSupported(info)) {
			LogHandler.printException(new Exception("Audio Line not supported"));
			return false;
		}

		audioFormat = new AudioFormat(configuration.sampleRate, configuration.sampleSize, configuration.channels, configuration.signed, configuration.bigEndian);
		return true;
	}

	@Override
	public void Recording() throws RecordingException {
		try {
			targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
			targetDataLine.open(audioFormat);
			// start capturing
			targetDataLine.start();
			AudioInputStream audioInputStream = new AudioInputStream(targetDataLine);
			
			// start recording
			FileOutputStream fileOutputStream = FileManager.getInstance().getFileOutputStream(getMenuText());
			AudioSystem.write(audioInputStream, audioFileFormatType, fileOutputStream);

		} catch (LineUnavailableException | IOException e) {
			LogHandler.printException(e);
		}
	}

	@Override
	public void cleanupRecorder() {
		targetDataLine.stop();
		targetDataLine.close();
	}

}
