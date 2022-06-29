package edu.kit.exp.client.comunication;

import java.io.IOException;

import edu.kit.exp.client.gui.ClientGuiController;
import edu.kit.exp.client.gui.screens.Screen;
import edu.kit.exp.client.gui.screens.ScreenFactory;
import edu.kit.exp.client.sensors.SensorManagement;
import edu.kit.exp.common.RecordingException;
import edu.kit.exp.common.communication.MessageReceiver;

/**
 * This class is used for processing incoming server messages.
 *
 * @see MessageReceiver
 * @see ServerMessage
 */
public class ClientMessageReceiver extends MessageReceiver<ServerMessage> {

		private ClientGuiController clientGuiController;

		private ClientDataTransmissionManagement clientDataTransmissionManagement;

		private SensorManagement sensorManagement;

		public ClientMessageReceiver() {
				clientGuiController = ClientGuiController.getInstance();
				clientDataTransmissionManagement = ClientDataTransmissionManagement.getInstance();
				sensorManagement = SensorManagement.getInstance();
		}

		/**
		 * This method processes a server message dependent on it�s type.
		 *
		 * @param message A ServerMessage which should be routed.
		 * @throws IOException
		 */
		@Override public void routeMessage(ServerMessage message) throws IOException, IllegalAccessException, ClassNotFoundException, InstantiationException, RecordingException {
				Class<? extends ServerMessage> messageClass = message.getClass();
				if (messageClass.equals(ServerParamObjectUpdateMessage.class)) {
						ServerParamObjectUpdateMessage msg = (ServerParamObjectUpdateMessage) message;
						clientGuiController.processParamObjectUpdate(msg.getParameterObjectUpdate());
				} else if (messageClass.equals(ServerSensorCommandMessage.class)) {
						sensorManagement.processSensorCommandMessage((ServerSensorCommandMessage) message);
				} else if (messageClass.equals(ServerTransmissionMessage.class)) {
						clientDataTransmissionManagement.processCommand();
				} else if (messageClass.equals(ServerExperimentMessage.class)) {
						ServerExperimentMessage msg = (ServerExperimentMessage) message;
						if (msg.getType().equals(ServerExperimentMessage.SHOW_SCREEN)) {
								Screen screen = ScreenFactory.createScreen(msg.getGlobalScreenId(), msg.getParameters(), msg.getGameId(), msg.getShowUpTime());
								clientGuiController.showScreen(screen);
						} else if (msg.getType().equals(ServerExperimentMessage.SHOW_GENERAL_SCREEN)) {
								Screen screen = ScreenFactory.createScreen(msg.getGlobalScreenId(), msg.getParameters(), null, msg.getShowUpTime());
								clientGuiController.showScreen(screen);
						} else if (msg.getType().equals(ServerExperimentMessage.SHOW_SCREEN_WITH_DEADLINE)) {
								Screen screen = ScreenFactory.createScreen(msg.getGlobalScreenId(), msg.getParameters(), msg.getGameId(), msg.getShowUpTime());
								clientGuiController.showScreenWithDeadLine(screen);
						}
				}
		}
}
