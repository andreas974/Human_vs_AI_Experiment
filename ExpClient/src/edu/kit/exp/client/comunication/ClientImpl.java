package edu.kit.exp.client.comunication;

import edu.kit.exp.client.gui.LoginScreen;
import edu.kit.exp.client.gui.MainFrame;
import edu.kit.exp.common.IClient;
import edu.kit.exp.common.IScreenParamObject;
import edu.kit.exp.common.communication.IncommingMessageQueue;
import edu.kit.exp.common.communication.MessageDeliveryThread;
import edu.kit.exp.common.sensor.ISensorControlObject;

import javax.swing.*;
import java.rmi.RemoteException;

/**
 * This class implements the functionality provided to the ExpServer! The
 * ExpServer triggers the methods of this class to control the client.</br> It
 * represents the client side implementation of the IClient Interface for the
 * use of RMI.
 *
 * @see IClient
 */
public class ClientImpl implements IClient {

	/**
	 * The message queue.
	 */
	private IncommingMessageQueue<ServerMessage> messageQueue;

	/**
	 * The instance.
	 */
	private static ClientImpl instance;

	/**
	 * The message delivery thread.
	 */
	@SuppressWarnings("unused")
	private MessageDeliveryThread<ServerMessage> messageDeliveryThread;

	/**
	 * This method gets the single instance of ClientImpl.
	 *
	 * @return the single instance of ClientImpl
	 */
	public static ClientImpl getInstance() {
		if (instance == null) {
			instance = new ClientImpl();
		}

		return instance;
	}

	/**
	 * This constructor instantiates a new client implementation.
	 */
	private ClientImpl() {
		messageQueue = ClientCommunicationManager.getInstance().getIncommingMessageQueue();
		setMessageDeliveryThread(new MessageDeliveryThread<ServerMessage>("CLIENT MessageDeliveryThread", messageQueue, ClientCommunicationManager.getInstance().getMessageReceiver()));
	}

	@Override public void showGeneralScreen(String globalScreenId, IScreenParamObject parameters) throws RemoteException {
		messageQueue.push(new ServerExperimentMessage(ServerExperimentMessage.SHOW_GENERAL_SCREEN, globalScreenId, parameters, null));
	}

	@Override public void loginFailed(String errorMessage) throws RemoteException {
		MainFrame.getInstance().dispose();
		LoginScreen.getInstance().setVisible(true);
		JOptionPane.showMessageDialog(LoginScreen.getInstance(), errorMessage, "Login error", JOptionPane.ERROR_MESSAGE);
	}

	@Override public void showScreen(String globalScreenId, IScreenParamObject parameters, String gameId) throws RemoteException {
		messageQueue.push(new ServerExperimentMessage(ServerExperimentMessage.SHOW_SCREEN, globalScreenId, parameters, gameId));
	}

	@Override public void showScreenWithDeadLine(String globalScreenId, IScreenParamObject parameters, String gameId, Long showUpDuration) throws RemoteException {
		messageQueue.push(new ServerExperimentMessage(ServerExperimentMessage.SHOW_SCREEN_WITH_DEADLINE, globalScreenId, parameters, gameId, showUpDuration));
	}

	@Override public void executeTransmissionCommand() throws RemoteException {
		messageQueue.push(new ServerTransmissionMessage());
	}

	@Override public void sendParamObjectUpdate(IScreenParamObject parameter) throws RemoteException {
		messageQueue.push(new ServerParamObjectUpdateMessage(parameter));
	}

	@Override public void executeSensorCommand(ISensorControlObject object) throws RemoteException {
		messageQueue.push(new ServerSensorCommandMessage(object));
	}

	public MessageDeliveryThread<ServerMessage> getMessageDeliveryThread() {
		return messageDeliveryThread;
	}
	/**
	 * This method sets the message delivery thread for server messages.
	 *
	 * @param messageDeliveryThread The new MessageDeliveryThread for delivering incoming server
	 *                              messages.
	 * @see MessageDeliveryThread
	 * @see ServerMessage
	 */
	public void setMessageDeliveryThread(MessageDeliveryThread<ServerMessage> messageDeliveryThread) {
		this.messageDeliveryThread = messageDeliveryThread;
	}

}