package edu.kit.exp.server.communication;

import edu.kit.exp.common.IClient;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class ClientState {

	private static HashMap<String, ClientState> instances = new HashMap<String, ClientState>();
	
	public static synchronized ClientState getClient(String clientId) {
		if (!instances.containsKey(clientId)){
			instances.put(clientId, new ClientState());
		}
		return instances.get(clientId);
	}
	
	public static synchronized Set<Entry<String, ClientState>> getClients() {
		return instances.entrySet();
	}
	
	
	private IClient clientRemoteObject;
	private String currentScreen;
	
	private ClientState(){
		clientRemoteObject = null;
		currentScreen = "";
	}

	public IClient getClientRemoteObject() {
		return clientRemoteObject;
	}

	public void setClientRemoteObject(IClient clientRemoteObject) {
		this.clientRemoteObject = clientRemoteObject;
	}

	public String getCurrentScreen() {
		return currentScreen;
	}

	public void setCurrentScreen(String currentScreen) {
		this.currentScreen = currentScreen;
	}
	
}
