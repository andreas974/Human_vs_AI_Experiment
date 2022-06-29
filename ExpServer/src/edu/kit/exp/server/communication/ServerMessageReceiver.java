package edu.kit.exp.server.communication;

import edu.kit.exp.common.communication.MessageReceiver;
import edu.kit.exp.server.run.*;

import javax.naming.CommunicationException;

/**
 * This class gets all client messages and routes them to their receiver.
 * 
 * @see edu.kit.exp.common.communication.MessageReceiver
 */
public class ServerMessageReceiver extends MessageReceiver<ClientMessage> {

	/** The message sender. */
	private ServerMessageSender messageSender;

	/** The subject registration. */
	private SubjectRegistration subjectRegistration;

	/** The session queue. */
	private LoggedQueue<ClientMessage> sessionQueue;
	
	private LoggedQueue<ClientDataPropertiesMessage> propertiesQueue;
	
	private LoggedQueue<ClientDataTransmissionMessage> dataTransmissionQueue;
	
	private LoggedQueue<ClientStatusMessage> clientStatusQueue;
	
	/**
	 * This constructor instantiates a new server message receiver.
	 */
	ServerMessageReceiver() {
		messageSender = ServerCommunicationManager.getInstance().getMessageSender();
		subjectRegistration = SubjectRegistration.getInstance();
		sessionQueue = QueueManager.getSessionQueueInstance();
		propertiesQueue = QueueManager.getClientDataPropertiesQueueInstance();
		dataTransmissionQueue = QueueManager.getDataTransmissionQueueInstance();
		clientStatusQueue = QueueManager.getClientStatusQueueInstance();
	}

	/**
	 * This method routes a client message to its destination. If
	 * <code>msg</code> is a <code>ClientRegistrationMessage</code> the
	 * subjectRegistration is called. If <code>msg</code> is is a
	 * <code>ClientResponseMessage</code> it is routed to the addressed message
	 * queue.
	 * 
	 * @param msg
	 *            A client message.
	 * @throws Exception
	 */
	@Override
	public void routeMessage(ClientMessage msg) throws Exception {

		if (msg.getClass().equals(ClientRegistrationMessage.class)) {

			ClientRegistrationMessage clientRegistrationMessage = (ClientRegistrationMessage) msg;

			String clientId = clientRegistrationMessage.getClientId();

			try {
				subjectRegistration.registerSubject(clientRegistrationMessage);

			} catch (CommunicationException exception) {
				RunStateLogger.getInstance().createServerOutputMessage(RunStateLogEntry.ServerEvent.CLIENTUPDATE, "Login error", clientId);
				messageSender.sendLoginError(((ClientRegistrationMessage) msg).getClientRemoteObject(),exception);

			}

		}


		else if (msg.getClass().equals(ClientResponseMessage.class) || msg.getClass().equals(ClientTrialLogMessage.class) || msg.getClass().equals(QuizProtocolMessage.class) || msg.getClass().equals(QuestionnaireProtocolMessage.class)) {
			sessionQueue.push(msg);
		}
		
		else if (msg.getClass().equals(ClientDataTransmissionMessage.class)) {
			dataTransmissionQueue.push((ClientDataTransmissionMessage) msg);
		}
		
		else if (msg.getClass().equals(ClientDataPropertiesMessage.class)) {
			propertiesQueue.push((ClientDataPropertiesMessage) msg);
		}
		
		else if (msg.getClass().equals(ClientStatusMessage.class)) {
			clientStatusQueue.push((ClientStatusMessage) msg);
		}
	}

}
