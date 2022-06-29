package edu.kit.exp.server.communication;

public class ClientDataPropertiesMessage extends ClientMessage {
	long[] fileSizes;
	String[] fileNames;

	long[] startTimeStamps;
	long[] stopTimeStamps;


	int numberOfFiles;
	int bytesPerPackage;
	
	protected ClientDataPropertiesMessage(String clientId, long[] fileSizes, String[] fileNames, long[] startTimeStamps, long[] stopTimeStamps, int numberOfFiles, int bytesPerPackage) {
		super(clientId);
		setFileSizes(fileSizes);
		setNumberOfFiles(numberOfFiles);
		setStartTimeStamps(startTimeStamps);
		setStopTimeStamps(stopTimeStamps);
		setBytesPerPackage(bytesPerPackage);
		setFileNames(fileNames);
	}

	public String[] getFileNames() {
		return fileNames;
	}
	
	private void setFileNames(String[] fileNames) {
		this.fileNames = fileNames;
	}
	
	public long[] getFileSizes() {
		return fileSizes;
	}

	private void setFileSizes(long[] fileSizes) {
		this.fileSizes = fileSizes;
	}

	public int getBytesPerPackage() {
		return bytesPerPackage;
	}

	private void setBytesPerPackage(int bytesPerPackage) {
		this.bytesPerPackage = bytesPerPackage;
	}

	public int getNumberOfFiles() {
		return numberOfFiles;
	}

	public void setNumberOfFiles(int numberOfFiles) {
		this.numberOfFiles = numberOfFiles;
	}

	public long[] getStopTimeStamps() {
		return stopTimeStamps;
	}

	public void setStopTimeStamps(long[] stopTimeStamps) {
		this.stopTimeStamps = stopTimeStamps;
	}

	public void setStartTimeStamps(long[] startTimeStamps) {
		this.startTimeStamps = startTimeStamps;
	}

	public long[] getStartTimeStamps() {
		return startTimeStamps;
	}

}
