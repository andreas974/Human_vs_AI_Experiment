package edu.kit.exp.server.communication;

import edu.kit.exp.client.gui.screens.DefaultWelcomeScreen;
import edu.kit.exp.client.gui.screens.Screen.ParamObject;
import edu.kit.exp.common.IClient;
import edu.kit.exp.common.IScreenParamObject;
import edu.kit.exp.common.LogHandler;
import edu.kit.exp.common.communication.MessageSender;
import edu.kit.exp.common.sensor.ISensorControlObject;
import edu.kit.exp.server.jpa.entity.Subject;

import javax.naming.CommunicationException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This Class provides functionalities to send messages to clients.
 */
public class ServerMessageSender extends MessageSender {

	private Map<String, IClient> clients = new HashMap<String, IClient>();

	ServerMessageSender() {

	}

	/**
	 * A client has to be registered to sent messages to it.
	 *
	 * @param clientRegistrationMessage
	 */
	public synchronized void registerClient(ClientRegistrationMessage clientRegistrationMessage) {
		String clientId = clientRegistrationMessage.getClientId();
		IClient clientRemoteObject = clientRegistrationMessage.getClientRemoteObject();
		clients.put(clientId, clientRemoteObject);
	}

	public synchronized void sendGeneralScreenMessage(Subject updatedSubject, String globalScreenId, ParamObject parameter) throws RemoteException {
		IClient clientRemoteObject = clients.get(updatedSubject.getIdClient());
		clientRemoteObject.showGeneralScreen(globalScreenId, parameter);
	}

	public synchronized void sendLoginError(IClient clientRemoteObject, CommunicationException exception) {
		try {
			clientRemoteObject.loginFailed(exception.getMessage());
		} catch (RemoteException e) {
            LogHandler.printException(e, "Unknown client is not reachable ... ignore");
		}
	}

	public synchronized void sendShowScreenMessage(String clientId, String globalScreenId, IScreenParamObject parameter, String gameId) throws RemoteException {
		IClient clientRemoteObject = clients.get(clientId);
		clientRemoteObject.showScreen(globalScreenId, parameter, gameId);
	}

	public synchronized void sendShowScreenMessage(String clientId, String screenId, IScreenParamObject parameter, String gameId, Long showUpTime) throws RemoteException {
		IClient clientRemoteObject = clients.get(clientId);
		clientRemoteObject.showScreenWithDeadLine(screenId, parameter, gameId, showUpTime);
	}

	public synchronized void sendToALLWithDeadLine(String screenId, IScreenParamObject parameter, Long showUpTime) throws RemoteException {
		for (Entry<String, IClient> entry : clients.entrySet()) {
			IClient clientRemoteObject = entry.getValue();
			clientRemoteObject.showScreenWithDeadLine(screenId, parameter, "NoGameID", showUpTime);
		}
	}

	public synchronized void sendToALL(String screenId, IScreenParamObject parameters) throws RemoteException {
		for (Entry<String, IClient> entry : clients.entrySet()) {
			IClient clientRemoteObject = entry.getValue();
			clientRemoteObject.showGeneralScreen(screenId, parameters);
		}
	}

	public synchronized void sendParamObjectUpdate(String clientId, IScreenParamObject parameter) throws RemoteException {
		IClient clientRemoteObject = clients.get(clientId);
		clientRemoteObject.sendParamObjectUpdate(parameter);
	}

	public synchronized void sendSensorCommandToALL(ISensorControlObject object) throws RemoteException {
		for (Entry<String, IClient> entry : clients.entrySet()) {
			IClient clientRemoteObject = entry.getValue();
			clientRemoteObject.executeSensorCommand(object);
		}
	}

	public synchronized void sendTransmissionCommandToClient(String clientId) throws RemoteException {
		IClient clientRemoteObject = clients.get(clientId);
		clientRemoteObject.executeTransmissionCommand();
	}

}
