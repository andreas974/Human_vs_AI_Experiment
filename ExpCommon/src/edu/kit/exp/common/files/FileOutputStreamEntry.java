package edu.kit.exp.common.files;

import java.io.FileOutputStream;
import java.util.Calendar;

/**
 * 
 * Meta data file for the file manager
 * 
 * @author Tonda
 *
 */
public class FileOutputStreamEntry implements IFileWriterEntry<FileOutputStream> {
	private String fileName;
	private FileOutputStream fileWriter;
	private long timeStampStartWriting;
	private long timeStampStopWriting;

	public FileOutputStreamEntry(String fileName, FileOutputStream fileWriter) {
		setFileName(fileName);
		setFileWriter(fileWriter);
		setStarted();
	}

	public void setFinished() {
		timeStampStopWriting = Calendar.getInstance().getTimeInMillis();
	}

	@Override public void setStarted() {
		timeStampStartWriting = Calendar.getInstance().getTimeInMillis();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public FileOutputStream getFileWriter() {
		return fileWriter;
	}

	public void setFileWriter(FileOutputStream fileWriter) {
		this.fileWriter = fileWriter;
	}

	public long getTimeStampStopWriting() {
		return timeStampStopWriting;
	}

	public long getTimeStampStartWriting() {
		return timeStampStartWriting;
	}
}
