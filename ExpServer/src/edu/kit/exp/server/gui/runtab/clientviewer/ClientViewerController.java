package edu.kit.exp.server.gui.runtab.clientviewer;

import edu.kit.exp.common.ClientStatus;

import java.util.Collection;
import java.util.HashMap;

/**
 * Controller for the client viewer
 */
public class ClientViewerController {
	private static ClientViewerController instance;
	private ClientViewerThread clientViewerThread;
	private HashMap<String, ClientStatus> clientList;

	private ClientViewerController(){
		clientList = new HashMap<>();
		
		clientViewerThread = new ClientViewerThread(this);
		clientViewerThread.start();
	}

	public static ClientViewerController getInstance() {
		if (instance == null) {
			instance = new ClientViewerController();
		}
		return instance;
	}

	public void updateClientStatus(ClientStatus clientStatus){
		synchronized (clientList) {
			clientList.put(clientStatus.getClientId(), clientStatus);
		}
	}
	
	public Collection<ClientStatus> getAllClientStatus(){
		synchronized (clientList) {
			return clientList.values();			
		}
	}
}
