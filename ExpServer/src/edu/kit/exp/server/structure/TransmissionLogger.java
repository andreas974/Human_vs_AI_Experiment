package edu.kit.exp.server.structure;

import java.text.DecimalFormat;

import edu.kit.exp.server.run.RunStateLogEntry;
import edu.kit.exp.server.run.RunStateLogger;

/**
 * TransmissionLogger which keeps the info on the RunTab regarding the data transmission up to date
 *
 * @author Tonda
 */
public class TransmissionLogger extends Thread {

	/**
	 * Refresh time in milliseconds
	 */
	private static final int REFRESH_TIME = 500;
	private static final int NUMBER_OF_PLACES_SHOWN = 2;
	private static final String TRANSMISSION = "Transmission";
	private static final String CLIENT_AND_FILE_NUMBER_FORMAT = "%1$s (%2$s/%3$s)";
	private static final String ONE_HUNDRED = "100.00";
	private static final String ZERO = "0.00";
	private static final String TRANSMISSION_STATUS_FORMAT = "Status: %1s%% completed";
	private final String fileName;
	private ServerDataTransmissionManagement manager;
	private RunStateLogger runStateLogger;
	private boolean isRunning = true;

	/**
	 * Initializes a TransmissionLogger using the data transmission manager
	 *
	 * @param manager Manager is used to extract meta data during runtime
	 */
	public TransmissionLogger(ServerDataTransmissionManagement manager, String fileName) {
		this.fileName = fileName;
		setManager(manager);
		runStateLogger = RunStateLogger.getInstance();
		RunStateLogEntry logEntry = new RunStateLogEntry(manager.getClientId(), TRANSMISSION, String.format(CLIENT_AND_FILE_NUMBER_FORMAT, ServerDataTransmissionManagement.shortenFileName(fileName), manager.getFileNumber() + 1, manager.getTotalNumberOfFiles()), String.format(TRANSMISSION_STATUS_FORMAT, ZERO));
		runStateLogger.createOutputMessage(logEntry);
	}

	/**
	 * Run method to keep RunTab-Data up to date during runtime
	 */
	@Override public void run() {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(NUMBER_OF_PLACES_SHOWN);
		while (isRunning) {
			RunStateLogEntry logEntry = new RunStateLogEntry(manager.getClientId(), TRANSMISSION, String.format(CLIENT_AND_FILE_NUMBER_FORMAT, ServerDataTransmissionManagement.shortenFileName(fileName), manager.getFileNumber() + 1, manager.getTotalNumberOfFiles()), String.format(TRANSMISSION_STATUS_FORMAT, calculatePercentage(df)));
			logEntry.setOverwriteLatestEntry(true);
			runStateLogger.createOutputMessage(logEntry);
			try {
				Thread.sleep(REFRESH_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private String calculatePercentage(DecimalFormat df) {
		return df.format(((double) manager.getTotalBytesRead()) * 100 / ((double) manager.getFileSizeInBytes()));
	}

	private void setManager(ServerDataTransmissionManagement manager) {
		this.manager = manager;
	}

	public void end() {
		isRunning = false;
		RunStateLogEntry logEntry = new RunStateLogEntry(manager.getClientId(), TRANSMISSION, String.format(CLIENT_AND_FILE_NUMBER_FORMAT, ServerDataTransmissionManagement.shortenFileName(fileName), manager.getFileNumber() + 1, manager.getTotalNumberOfFiles()), String.format(TRANSMISSION_STATUS_FORMAT, ONE_HUNDRED));
		logEntry.setOverwriteLatestEntry(true);
		runStateLogger.createOutputMessage(logEntry);
	}

}
