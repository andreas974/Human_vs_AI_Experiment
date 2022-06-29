package edu.kit.exp.common.files;

/**
 * Created by tondaroder on 21.07.16.
 */
public interface IFileWriterEntry<FileWriterType> {
	void setFinished();

	void setStarted();

	String getFileName();

	void setFileName(String fileName);

	FileWriterType getFileWriter();

	void setFileWriter(FileWriterType fileWriter);

	long getTimeStampStopWriting();

	long getTimeStampStartWriting();
}
