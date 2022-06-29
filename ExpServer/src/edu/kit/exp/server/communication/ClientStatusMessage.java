package edu.kit.exp.server.communication;

import edu.kit.exp.common.ClientStatus;

public class ClientStatusMessage extends ClientMessage {

	private ClientStatus status;
	
	protected ClientStatusMessage(ClientStatus status) {
		super(status.getClientId());
		setStatus(status);
	}

	public ClientStatus getClientStatus() {
		return status;
	}

	public void setStatus(ClientStatus status) {
		this.status = status;
	}

}
