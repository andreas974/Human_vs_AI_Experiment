package edu.kit.exp.common.files;

import java.io.FileWriter;
import java.util.Calendar;

/**
 * Meta data file for the file manager
 *
 * @author Tonda
 */
public class FileWriterEntry implements IFileWriterEntry<FileWriter> {
	private String fileName;
	private FileWriter fileWriter;
	private long timeStampStartWriting;
	private long timeStampStopWriting;

	public FileWriterEntry(String fileName, FileWriter fileWriter) {
		setFileName(fileName);
		setFileWriter(fileWriter);
		setStarted();
	}

	public void setStarted() {
		timeStampStartWriting = Calendar.getInstance().getTimeInMillis();
	}

	public void setFinished() {
		timeStampStopWriting = Calendar.getInstance().getTimeInMillis();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public FileWriter getFileWriter() {
		return fileWriter;
	}

	public void setFileWriter(FileWriter fileWriter) {
		this.fileWriter = fileWriter;
	}

	public long getTimeStampStopWriting() {
		return timeStampStopWriting;
	}

	public long getTimeStampStartWriting() {
		return timeStampStartWriting;
	}
}
