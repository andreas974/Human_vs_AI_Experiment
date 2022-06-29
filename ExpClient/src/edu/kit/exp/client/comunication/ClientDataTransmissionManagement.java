package edu.kit.exp.client.comunication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import edu.kit.exp.common.files.FileManager;
import edu.kit.exp.common.files.IFileWriterEntry;

/**
 * 
 * @author Tonda
 *
 *         looks for files for transmission, divides it in packages and sends it
 *         to the server.
 */

public class ClientDataTransmissionManagement {

	private static final int BYTES_PER_PACKAGE = 1024;
	private static ClientDataTransmissionManagement instance = null;
	private static ClientCommunicationManager communicationManager = ClientCommunicationManager.getInstance();

	private ClientDataTransmissionManagement() {

	}

	/**
	 * Singleton pattern
	 * 
	 * @return returns instance of the ClientDataTransmissionManagement
	 */
	public static ClientDataTransmissionManagement getInstance() {

		if (instance == null) {
			instance = new ClientDataTransmissionManagement();
		}
		return instance;
	}

	/**
	 * starts transmission of a file to the server
	 * 
	 * @throws IOException
	 */
	public void processCommand() throws IOException {
		List<IFileWriterEntry<?>> fileEntryList = getFileList();
		if (fileEntryList.isEmpty()) {
			sendPropertiesWhenNoFilesExist();
		} else {
			List<File> fileList = FileManager.retrieveFileList(fileEntryList);
			sendProperties(fileList, fileEntryList);
			sendAllFiles(fileList);
		}
	}

	private List<IFileWriterEntry<?>> getFileList() throws IOException {
		List<IFileWriterEntry<?>> fileList = FileManager.getInstance().getFileList();
		return fileList;
	}

	private void sendPropertiesWhenNoFilesExist() {
		communicationManager.getMessageSender().sendDataPropertiesPackage(new long[0], new String[0], new long[0], new long[0], 0, 0);
	}

	private void sendProperties(List<File> fileList, List<IFileWriterEntry<?>> entries) {
		communicationManager.getMessageSender().sendDataPropertiesPackage(getFileSizes(fileList), getFileNames(fileList),  getStartTimeStamps(entries), getStopTimeStamps(entries), fileList.size(),
				BYTES_PER_PACKAGE);
	}

	private long[] getStartTimeStamps(List<IFileWriterEntry<?>> entries) {
        long[] timeStampArray = new long[entries.size()];
        for (int index = 0; index < entries.size(); index++) {
            timeStampArray[index] = entries.get(index).getTimeStampStartWriting();
        }
        return timeStampArray;
    }

	private long[] getStopTimeStamps(List<IFileWriterEntry<?>> entries) {
        long[] timeStampArray = new long[entries.size()];
        for (int index = 0; index < entries.size(); index++) {
            timeStampArray[index] = entries.get(index).getTimeStampStopWriting();
        }
        return timeStampArray;
    }

    private void sendAllFiles(List<File> fileList) throws FileNotFoundException, UnknownHostException, IOException {
		int fileIndex = 1;
		for (File file : fileList) {
			sendFile(file, fileIndex);
			fileIndex++;
		}
	}

	private String[] getFileNames(List<File> fileList) {
		int index = 0;
		String[] fileSizes = new String[fileList.size()];
		for (File file : fileList) {
			fileSizes[index] = file.getName();
			index++;
		}
		return fileSizes;
	}

	private long[] getFileSizes(List<File> fileList) {
		int index = 0;
		long[] fileSizes = new long[fileList.size()];
		for (File file : fileList) {
			fileSizes[index] = file.length();
			index++;
		}
		return fileSizes;
	}

	private void sendFile(File file, int fileNumber) throws FileNotFoundException, UnknownHostException, IOException {
		FileInputStream inputStream = new FileInputStream(file);
		sendPackages(inputStream, fileNumber);
		inputStream.close();
	}

	private void sendPackages(FileInputStream inputStream, int fileNumber) throws IOException {
		byte[] data = new byte[BYTES_PER_PACKAGE];
		int index = 1;
		int remainingBytes = 0;
		while ((remainingBytes = inputStream.available()) != 0) {
			inputStream.read(data);
			if (remainingBytes > BYTES_PER_PACKAGE) {
				communicationManager.getMessageSender().sendDataTransmissionPackage(data, fileNumber, index);
			} else {
				byte[] newData = new byte[remainingBytes];
				System.arraycopy(data, 0, newData, 0, remainingBytes);
				communicationManager.getMessageSender().sendDataTransmissionPackage(newData, fileNumber, -1);
			}
			index++;
		}
	}

}
