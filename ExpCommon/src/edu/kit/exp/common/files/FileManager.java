package edu.kit.exp.common.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import edu.kit.exp.common.Constants;
import edu.kit.exp.common.LogHandler;

/**
 * FileManager which returns files with standardized file-names. Files who are
 * created by the manager are transfered to the servers database at the end of
 * the experiment.
 *
 * @author Tonda
 */
public class FileManager {
	private static final String SPLIT_CHAR = "_";
	private static final String FILE_ENDING = ".expdata";

	private static FileManager instance = null;
	private static String fileDirectory = "";
	private List<IFileWriterEntry<FileWriter>> fileWriterList = new ArrayList<IFileWriterEntry<FileWriter>>();
	private List<IFileWriterEntry<FileOutputStream>> fileOutputStreamList = new ArrayList<IFileWriterEntry<FileOutputStream>>();

	private FileManager() {

	}

	static {
		fileDirectory = Constants.getFileDirectory();
	}

	/**
	 * Singleton pattern
	 *
	 * @return instance of the file manager
	 */
	public static FileManager getInstance() {
		if (instance == null) {
			instance = new FileManager();
		}
		return instance;
	}

	/**
	 * returns file writer for the current client and adds it to the list
	 *
	 * @param key
	 * @return FileWriter
	 */
	public FileWriter getFileWriter(String key) {
		return getFileWriter(key, Constants.getComputername());
	}

	/**
	 * returns file writer and adds it to the list
	 *
	 * @param key
	 * @param clientID
	 * @return FileWriter
	 */
	public FileWriter getFileWriter(String key, String clientID) {
		String fileName = getFileName(key, clientID);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(new File(fileName));
			fileWriterList.add(new FileWriterEntry(fileName, fileWriter));
		} catch (IOException e) {
			LogHandler.printException(e);
		}
		return fileWriter;
	}

	/**
	 * returns file output stream for the current client and adds it to the list
	 *
	 * @param key
	 * @return FileWriter
	 */
	public FileOutputStream getFileOutputStream(String key) {
		return getFileOutputStream(key, Constants.getComputername());
	}

	/**
	 * returns file output stream and adds it to the list
	 *
	 * @param key
	 * @param clientID
	 * @return FileWriter
	 */
	public FileOutputStream getFileOutputStream(String key, String clientID) {
		String fileName = getFileName(key, clientID);
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(new File(fileName));
			fileOutputStreamList.add(new FileOutputStreamEntry(fileName, fileOutputStream));
		} catch (IOException e) {
			LogHandler.printException(e);
		}
		return fileOutputStream;
	}

	/**
	 * replaces the time stamp of the relating file writer in the file manager with a new time stamp. Purpose of this method is to be able to update time stamps in case the file manager with an up to date time stamps if writing starts considerably after getting the file writer.
	 *
	 * @param writer
	 */
	public void updateStartTime(FileWriter writer) {
		for (IFileWriterEntry<FileWriter> entry : fileWriterList) {
			if (writer == entry.getFileWriter()) {
				entry.setStarted();
			}
		}
	}

	/**
	 * replaces the time stamp of the relating output stream in the file manager with a new time stamp. Purpose of this method is to be able to update time stamps in case the file manager with an up to date time stamps if writing starts considerably after getting the output stream.
	 *
	 * @param outputStream
	 */
	public void updateStartTime(FileOutputStream outputStream) {
		for (IFileWriterEntry<FileOutputStream> entry : fileOutputStreamList) {
			if (outputStream == entry.getFileWriter()) {
				entry.setStarted();
			}
		}
	}

	/**
	 * FileList with alle files of a client
	 *
	 * @return List<File> fileList with all files corresponding to the client id
	 * @throws IOException
	 */
	public List<IFileWriterEntry<?>> getFileList() throws IOException {
		closeFileWriter();
		List<IFileWriterEntry<?>> fileWriterEntries = new ArrayList<>();
		fileWriterEntries.addAll(fileWriterList);
		fileWriterEntries.addAll(fileOutputStreamList);
		return fileWriterEntries;
	}

	private void closeFileWriter() throws IOException {
		for (IFileWriterEntry<FileWriter> metafileWriter : fileWriterList) {
			metafileWriter.setFinished();
			FileWriter fileWriter = metafileWriter.getFileWriter();
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (Exception e) {
			}
		}
		for (IFileWriterEntry<FileOutputStream> outputStreamEntry : fileOutputStreamList) {
			outputStreamEntry.setFinished();
			FileOutputStream fileOutputStream = outputStreamEntry.getFileWriter();
			try {
				fileOutputStream.flush();
				fileOutputStream.close();
			} catch (Exception e) {
			}
		}
	}

	public static List<File> retrieveFileList(List<IFileWriterEntry<?>> fileWriterEntries) {
		List<File> fileList = new ArrayList<>();
		for (IFileWriterEntry<?> entry : fileWriterEntries) {
			fileList.add(new File(entry.getFileName()));
		}
		return fileList;

	}

	private static String getFileName(String key, String clientID) {
		return fileDirectory + key + SPLIT_CHAR + clientID + SPLIT_CHAR + Calendar.getInstance().getTimeInMillis() + SPLIT_CHAR + UUID.randomUUID() + FILE_ENDING;
	}
}
