package edu.kit.exp.server.gui.runtab.clientviewer;

import java.util.Calendar;

import edu.kit.exp.server.communication.ClientStatusMessage;
import edu.kit.exp.server.run.LoggedQueue;
import edu.kit.exp.server.run.QueueManager;

/**
 * Thread that asks clients for live status information
 */
public class ClientViewerThread extends Thread {

	private ClientViewerController controller;
	private LoggedQueue<ClientStatusMessage> clientStatusQueue;
	private boolean running = true;

	public ClientViewerThread(ClientViewerController clientViewerController) {
		controller = clientViewerController;
		clientStatusQueue = QueueManager.getClientStatusQueueInstance();
	}

	@Override
	public void run() {
		ClientStatusMessage msg;
		
		while (running) {
			msg = clientStatusQueue.pop();
			msg.getClientStatus().setStatusReceiveTime(Calendar.getInstance().getTimeInMillis());
			controller.updateClientStatus(msg.getClientStatus());
		}
	}

	public void stopThread() {
		running = false;
	}
}
