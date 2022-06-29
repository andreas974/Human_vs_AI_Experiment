package edu.kit.exp.server.communication;

/**
 * The class ClientDataTransmissionMessage represents a <code>ClientMessage</code>
 * which contains physio data of a client.
 * 
 * @see ClientMessage
 */
public class ClientDataTransmissionMessage extends ClientMessage {
	
	private byte[] data;
	private int packageNumber;
	private int fileNumber;

	protected ClientDataTransmissionMessage(String clientId, byte[] data, int fileNumber, int packageNumber) {
		super(clientId);
		setData(data);
		setFileNumber(fileNumber);
		setPackageNumber(packageNumber);
	}

	public int getPackageNumber() {
		return packageNumber;
	}

	public void setPackageNumber(int packageNumber) {
		this.packageNumber = packageNumber;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public int getFileNumber() {
		return fileNumber;
	}

	private void setFileNumber(int fileNumber) {
		this.fileNumber = fileNumber;
	}

}
