package edu.kit.exp.server.structure;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import edu.kit.exp.server.communication.ClientDataPropertiesMessage;
import edu.kit.exp.server.communication.ClientDataTransmissionMessage;
import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.dao.MeasurementDataDAO;
import edu.kit.exp.server.jpa.entity.MeasurementData;
import edu.kit.exp.server.run.LoggedQueue;
import edu.kit.exp.server.run.QueueManager;
import edu.kit.exp.server.run.RunStateLogEntry;
import edu.kit.exp.server.run.RunStateLogger;
import edu.kit.exp.common.Constants;

/**
 * Handels receipt of a data transmission by popping messages from the
 * DataTransmissionQueue
 *
 * @author Tonda
 */
public class ServerDataTransmissionManagement {

	private static final String DATABASE_OUTPUT_MESSAGE = "Write to Database";
	private static final String CLIENT_AND_FILE_NUMBER_FORMAT = "%1$s (%2$s/%3$s)";

	private static ServerDataTransmissionManagement instance = null;

	private LoggedQueue<ClientDataPropertiesMessage> dataPropertiesQueue = QueueManager.getClientDataPropertiesQueueInstance();
	private LoggedQueue<ClientDataTransmissionMessage> dataMessageQueue = QueueManager.getDataTransmissionQueueInstance();
	private RunStateLogger runStateLogger = RunStateLogger.getInstance();

	private MeasurementDataDAO measurementDataDao = new MeasurementDataDAO();

	private FileOutputStream fileOutputStream;
	private TransmissionLogger logger;
	private String clientId;
	private long totalBytesRead;
	private long fileSizeInBytes;
	private int fileIndex;
	private int totalNumberOfFiles;

	private ServerDataTransmissionManagement() {

	}

	/**
	 * return Singleton instance
	 */
	public static ServerDataTransmissionManagement getInstance() {
		if (instance == null) {
			instance = new ServerDataTransmissionManagement();
		}
		return instance;
	}

	/**
	 * Starts the receipt of messages before saving it to the database
	 *
	 * @throws DataManagementException
	 * @throws IOException
	 * @throws SQLException
	 */
	public void receive(String clientId, String subjectId) throws DataManagementException, IOException, SQLException {
		this.clientId = clientId;
		ClientDataPropertiesMessage properties = receiveDataProperties();
		String[] fileNames = properties.getFileNames();
		totalNumberOfFiles = fileNames.length;
		for (fileIndex = 0; fileIndex < totalNumberOfFiles; fileIndex++) {
			calculateInitialValues(properties);
			long startTimeStamp = properties.getStartTimeStamps()[fileIndex];
			long stopTimeStamp = properties.getStopTimeStamps()[fileIndex];
			receiveSingleFile(fileNames[fileIndex]);
			saveFileToDatabase(subjectId, fileNames[fileIndex], startTimeStamp, stopTimeStamp);
		}
	}

	private ClientDataPropertiesMessage receiveDataProperties() {
		ClientDataPropertiesMessage properties = null;
		while (properties == null) {
			properties = dataPropertiesQueue.pop();
		}
		return properties;
	}

	private void saveFileToDatabase(String subjectId, String fileName, long startTimeStamp, long stopTimeStamp) throws DataManagementException, IOException, SQLException {
		RunStateLogEntry logEntry = new RunStateLogEntry(clientId, DATABASE_OUTPUT_MESSAGE, String.format(CLIENT_AND_FILE_NUMBER_FORMAT, shortenFileName(fileName), fileIndex + 1, totalNumberOfFiles), "In Progress");
		runStateLogger.createOutputMessage(logEntry);
		measurementDataDao.createMeasurementData(createMeasurementDataObject(fileName, subjectId, startTimeStamp, stopTimeStamp), createFileInputStream(fileName));
		logEntry = new RunStateLogEntry(clientId, DATABASE_OUTPUT_MESSAGE, String.format(CLIENT_AND_FILE_NUMBER_FORMAT, shortenFileName(fileName), fileIndex + 1, totalNumberOfFiles), "Finished");
		logEntry.setOverwriteLatestEntry(true);
		runStateLogger.createOutputMessage(logEntry);
	}

	public static String shortenFileName(String fileName) {
		if (fileName.length() >= 10) {
			fileName = fileName.substring(0, 7) + "...";
		}
		return fileName;
	}

	private MeasurementData createMeasurementDataObject(String fileName, String subjectId, long startTimeStamp, long stopTimeStamp) {
		return new MeasurementData(fileName, clientId, subjectId, startTimeStamp, stopTimeStamp);
	}

	private FileInputStream createFileInputStream(String fileName) {
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(new File(Constants.FILE_PREFIX_SERVER + fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return fileInputStream;
	}

	private void receiveSingleFile(String fileName) throws IOException {
		logger = new TransmissionLogger(this, fileName);
		logger.start();
		int packageNumber = 1;
		boolean isLast = false;
		while (isLast == false) {
			ClientDataTransmissionMessage message = dataMessageQueue.pop();
			if (message.getPackageNumber() == -1) {
				writeBytesToFile(message.getData());
				isLast = true;
			} else if (message.getFileNumber() == (fileIndex + 1) && message.getPackageNumber() == packageNumber) {
				writeBytesToFile(message.getData());
				packageNumber++;
			}
		}
		fileOutputStream.flush();
		fileOutputStream.close();
		logger.end();
	}

	private void calculateInitialValues(ClientDataPropertiesMessage properties) throws FileNotFoundException {
		fileSizeInBytes = properties.getFileSizes()[fileIndex];
		String fileName = properties.getFileNames()[fileIndex];
		fileOutputStream = new FileOutputStream(new File(Constants.FILE_PREFIX_SERVER + fileName));
	}

	private void writeBytesToFile(byte[] data) throws IOException {
		totalBytesRead += data.length;
		fileOutputStream.write(data);
		fileOutputStream.flush();
	}

	public long getTotalBytesRead() {
		return totalBytesRead;
	}

	public long getFileSizeInBytes() {
		return fileSizeInBytes;
	}

	public String getClientId() {
		return clientId;
	}

	public int getFileNumber() {
		return fileIndex;
	}

	public int getTotalNumberOfFiles() {
		return totalNumberOfFiles;
	}

}
