package edu.kit.exp.common;

import edu.kit.exp.common.sensor.ISensorControlObject;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * A remote interface that defines all methods the client provides to the
 * server.
 *
 * @see Remote
 */
public interface IClient extends Remote {

		/**
		 * Request the client to try a reconnect because of a failed Login.
		 *
		 * @throws RemoteException If connection failed.
		 */
		void loginFailed(String errorMessage) throws RemoteException;

		/**
		 * This method shows a screen at the client. The gameId allows the server to
		 * route this message to the right game. This method should be used by
		 * default to play periods.
		 *
		 * @param globalScreenId the String ID of the screen to be shown at the client.
		 * @param parameter      the IScreenParamObject custom parameters for that screen.
		 * @param gameId         the String ID of the running game, the screen belongs to.
		 * @throws RemoteException If connection failed.
		 */
		void showScreen(String globalScreenId, IScreenParamObject parameter, String gameId) throws RemoteException;

		/**
		 * This method shows the defined screen for the defined showUpDuration and
		 * then switches to default waiting screen!.
		 *
		 * @param globalScreenId String ID of the screen to be shown at the client.
		 * @param parameter      the IScreenParamobject custom parameters for that screen.
		 * @param gameId         the String ID of the running game, the screen belongs to.
		 * @param showUpTime     the long period of time the screen will be visible at the
		 *                       client. In milliseconds!
		 * @throws RemoteException If connection failed.
		 */
		void showScreenWithDeadLine(String globalScreenId, IScreenParamObject parameter, String gameId, Long showUpTime) throws RemoteException;

		/**
		 * This method shows a screen at the client. There is NO gameId, so this
		 * method should only be used for general client interactions like login. Do
		 * NOT use this method to show screens in a running session!
		 *
		 * @param globalScreenId the screen to be shown at the client.
		 * @param parameter      the IScreenParamObject custom parameters for that screen.
		 * @throws RemoteException If connection failed.
		 */
		void showGeneralScreen(String globalScreenId, IScreenParamObject parameter) throws RemoteException;

		/**
		 * Send param object update.
		 *
		 * @param parameter the IScreenParamObject parameters that are updated.
		 * @throws RemoteException If connection failed.
		 */
		void sendParamObjectUpdate(IScreenParamObject parameter) throws RemoteException;

		/**
		 * Execute sensor command.
		 *
		 * @throws RemoteException If connection failed.
		 */
		void executeSensorCommand(ISensorControlObject object) throws RemoteException;

		/**
		 * @throws RemoteException
		 */
		void executeTransmissionCommand() throws RemoteException;

}
