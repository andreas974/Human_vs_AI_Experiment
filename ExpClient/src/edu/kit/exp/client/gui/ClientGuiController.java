package edu.kit.exp.client.gui;

import edu.kit.exp.client.comunication.ClientCommunicationManager;
import edu.kit.exp.client.comunication.ClientMessageSender;
import edu.kit.exp.client.gui.screens.DefaultWaitingScreen;
import edu.kit.exp.client.gui.screens.Screen;
import edu.kit.exp.common.ClientStatus;
import edu.kit.exp.common.IScreenParamObject;
import edu.kit.exp.common.communication.ConnectionException;

import java.rmi.RemoteException;
import java.util.Date;

/**
 * This controller coordinates all GUI interactions on the client side.
 * 
 */
public class ClientGuiController {

	/** The instance. */
	private static ClientGuiController instance;

	/** The message sender. */
	private ClientMessageSender messageSender = ClientCommunicationManager.getInstance().getMessageSender();

	/** The main frame. */
	private MainFrame mainFrame;

	/** The current screen. */
	private Screen currentScreen = null;
	
	/** The ID of the client. */
	private String clientId = null;
	
	/** This method gets the ID of the client. 
	 * 
	 * @ return the ID of the client
	 * */

	public String getClientId () {
		return this.clientId;
	}

	/**
	 * This method gets the main frame.
	 * 
	 * @return the main frame
	 */
	public MainFrame getMainFrame() {
		return mainFrame;
	}

	/**
	 * This method sets the main frame.
	 * 
	 * @param mainFrame
	 *            The new MainFram.
	 * @see MainFrame
	 */
	public void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	/** The counter. */
	private int counter = 0;

	/** The default waiting screen. */
	private DefaultWaitingScreen defaultWaitingScreen;

	/**
	 * This constructor instantiates a new client gui controller.
	 */
	private ClientGuiController() {
		mainFrame = MainFrame.getInstance();
	}

	/**
	 * This method gets the single instance of ClientGuiController.
	 * 
	 * @return a single instance of ClientGuiController
	 */
	public static ClientGuiController getInstance() {

		if (instance == null) {
			instance = new ClientGuiController();
		}

		return instance;
	}

	/**
	 * This method allows a client to login on the server.
	 * 
	 * @param clientId
	 *            A String which contains the ID of the client that wants to
	 *            login.
	 * @param serverIP
	 *            A String which contains the ID of the server where the client
	 *            wants to login.
	 * @throws ConnectionException
	 *             If the connection failed.
	 */
	public void login(String clientId, String serverIP) throws ConnectionException {
		
		this.clientId = clientId;
		
		if (counter < 1) {
			messageSender.registerAtServer(clientId, serverIP);
			counter++;
		} else {
			messageSender.reconnect(clientId);
		}

	}

	/**
	 * This method sends a message to the server.
	 * 
	 * @param parameters
	 *            The IScreenParamObject parameters of the screen.
	 * @param gameId
	 *            A String which contains the ID of the running game.
	 * @param screenId
	 *            A String, representing the ID of the screen that calls this
	 *            method.
	 */
	public void sendClientResponse(IScreenParamObject parameters, String gameId, String screenId)  {
		Date date = new Date();
		Long timeStamp = date.getTime();
		messageSender.sendMessage(parameters, gameId, screenId, timeStamp);

	}

	/**
	 * This Method sends a message to the server and displays the default
	 * waiting screen at this client.
	 * 
	 * @param parameters
	 *            A IScreenParamObject list of all additional parameters used in
	 *            the institution.
	 * @param gameId
	 *            A String which contains the ID of the running game.
	 * @param screenId
	 *            A String, representing the ID of the screen that calls this
	 *            method.
	 */
	public void sendClientResponseMessageAndWait(IScreenParamObject parameters, String gameId, String screenId) {

		Date date = new Date();
		Long timeStamp = date.getTime();

		if (defaultWaitingScreen == null) {
			defaultWaitingScreen = new DefaultWaitingScreen(null, new DefaultWaitingScreen.ParamObject(), DefaultWaitingScreen.class.getName(), 0L);
		}

		messageSender.sendMessage(parameters, gameId, screenId, timeStamp);
		// showScreen(defaultWaitingScreen);

	}

	/**
	 * This method shows a screen at this client.
	 * 
	 * @param screen
	 *            the {@link edu.kit.exp.client.gui.screens.Screen Screen} to be
	 *            shown.
	 */
	public void showScreen(Screen screen) {

		currentScreen = screen;
		mainFrame.showScreen(currentScreen);
	}

	/**
	 * This method is used to send trialLogMessages to the server. With this
	 * method, events on the client side, which are not observable by the
	 * server, can be logged. On the server side a trial will be created for the
	 * corresponding subject and subject group, with the given parameters.
	 * 
	 * @param gameId
	 *            A String which contains the ID of the running game.
	 * @param event
	 *            A String which contains the event you want to log.
	 * @param screenName
	 *            A String which contains the name of the screen corresponding
	 *            to that event.
	 * @param value
	 *            A String which contains the value of that event.
	 * @param clientTimeStamp
	 *            A Long variable, representing the time the event happened on
	 *            the client side.
	 */
	public void sendTrialLogMessage(String gameId, String event, String screenName, String value, Long clientTimeStamp) {
		messageSender.sendTrialLogMessage(gameId, event, screenName, value, clientTimeStamp);
	}

	/**
	 * This method logs trial information for the current screenName, gameid,
	 * and time, if available.
	 * 
	 * @param event
	 *            A String which contains the event you want to log.
	 * @param value
	 *            A String which contains the value of that event.
	 * @throws RemoteException 
	 * @see #sendTrialLogMessage(String, String, String, String, Long)
	 */
	public void sendTrialLogMessage(String event, String value) throws RemoteException {
		if (currentScreen != null) {
			sendTrialLogMessage(currentScreen.getGameId(), event, currentScreen.getScreenId(), value, new Date().getTime());
		} else {
			sendTrialLogMessage("[unknown gameId]", event, "[unknown screenName]", value, new Date().getTime());
		}
	}

	/**
	 * This method shows a screen for the time span defined in
	 * <code>Screen.getShowUpTime()</code>. After that the a waiting screen is
	 * shown automatically.
	 * 
	 * @param screen
	 *            the Screen to be shown.
	 */
	public void showScreenWithDeadLine(Screen screen) {

		if (defaultWaitingScreen == null) {
			defaultWaitingScreen = new DefaultWaitingScreen(null, new DefaultWaitingScreen.ParamObject(), DefaultWaitingScreen.class.getName(), 0L);
		}

		showScreen(screen);

		try {
			Thread.sleep(screen.getShowUpTime());
		} catch (InterruptedException e) {
			// Do nothing... Program will continue
		}
		showScreen(defaultWaitingScreen);
	}

	/**
	 * This method sends a quiz protocol to the server and shows a waiting
	 * screen.
	 * 
	 * @param passed
	 *            A Boolean which indicates if a quiz was passed (true) or not
	 *            (false).
	 * @param quizSolution
	 *            A String which contains the solution of the quiz.
	 */
	public void sendQuizProtocolAndWait(Boolean passed, String quizSolution) {

		if (defaultWaitingScreen == null) {
			defaultWaitingScreen = new DefaultWaitingScreen(null, new DefaultWaitingScreen.ParamObject(), DefaultWaitingScreen.class.getName(), 0L);
		}

		messageSender.sendQuizProtocol(passed, quizSolution);
		showScreen(defaultWaitingScreen);
	}

	/**
	 * This method sends a questionnaire protocol to the server and shows a waiting
	 * screen.
	 *            A Boolean which indicates if a questionnaire was passed (true) or not
	 *            (false).
	 * @param questionText
	 * @param questionnaireSolution
	 */
	public void sendQuestionnaireProtocolAndWait(boolean isLast, String questionText, String questionnaireSolution, long questionResponseTime) {

		if (isLast == true) {
			if (defaultWaitingScreen == null) {
				defaultWaitingScreen = new DefaultWaitingScreen(null, new DefaultWaitingScreen.ParamObject(), DefaultWaitingScreen.class.getName(), 0L);
			}
			showScreen(defaultWaitingScreen);
		}

		messageSender.sendQuestionnaireProtocol(isLast, questionText, questionnaireSolution, questionResponseTime);
	}

	/**
	 * This method processes a <code>ParamOject</code> update.
	 * 
	 * @param param
	 *            The IScreenParamObject parameters which are updated.
	 */
	public void processParamObjectUpdate(IScreenParamObject param) {
		if (currentScreen != null) {
			currentScreen.processParamObjectUpdate(param);
		}
	}
	
	
	public void sendClientStatus(ClientStatus clientStatus){
		messageSender.sendClientStatus(clientStatus);
	}
}
