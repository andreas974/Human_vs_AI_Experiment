package edu.kit.exp.client.gui.screens;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;

import edu.kit.exp.client.gui.ClientGuiController;
import edu.kit.exp.client.gui.MainFrame;
import edu.kit.exp.common.IScreenParamObject;

/**
 * This abstract class represents all screens that can be displayed at the
 * client side. All new/custom screens have to extend this class.
 * 
 */
public abstract class Screen extends JPanel {

	/**
	 * The class ParamObject. ParamObjects log all parameters used in the
	 * screen.
	 * 
	 * @see IScreenParamObject
	 */
	public static class ParamObject implements IScreenParamObject {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 6756327287510841042L;

		/** The log text enter. */
		private String logTextEnter;

		/** The log text exit. */
		private String logTextExit;

		/** The log value enter. */
		private String logValueEnter;

		/** The log value exit. */
		private String logValueExit;

		/** The log time enter. */
		private Long logTimeEnter;

		/**
		 * This constructor instantiates a new param object.
		 */
		public ParamObject() {
			this.logTextEnter = "EVENT_SCREEN_ENTER";
			this.logTextExit = "EVENT_SCREEN_EXIT";
			this.logValueEnter = null;
			this.logValueExit = null;
			this.logTimeEnter = 0L;
		}

		/**
		 * This method gets the text which was possibly logged when a client
		 * enters the screen.
		 * 
		 * @return the enter text
		 */
		public String getLogTextEnter() {
			return logTextEnter;
		}

		/**
		 * This method sets the text which was possibly logged when a client
		 * enters the screen.
		 * 
		 * @param logTextEnter
		 *            A String which contains the text.
		 */
		public void setLogTextEnter(String logTextEnter) {
			this.logTextEnter = logTextEnter;
		}

		/**
		 * This method gets the text which was possibly logged when a client
		 * exits the screen.
		 * 
		 * @return the exit text
		 */
		public String getLogTextExit() {
			return logTextExit;
		}

		/**
		 * This method sets the text which was possibly logged when a client
		 * exits the screen.
		 * 
		 * @param logTextExit
		 *            A String which contains the text.
		 */
		public void setLogTextExit(String logTextExit) {
			this.logTextExit = logTextExit;
		}

		/**
		 * This method gets the value which was possibly logged when a client
		 * enters the screen.
		 * 
		 * @return the enter value
		 */
		public String getLogValueEnter() {
			return logValueEnter;
		}

		/**
		 * This method sets the value which was possibly logged when a client
		 * enters the screen.
		 * 
		 * @param logValueEnter
		 *            A String which contains the value.
		 */
		public void setLogValueEnter(String logValueEnter) {
			this.logValueEnter = logValueEnter;
		}

		/**
		 * This method gets the value which was possibly logged when a client
		 * exits the screen.
		 * 
		 * @return the exit value
		 */
		public String getLogValueExit() {
			return logValueExit;
		}

		/**
		 * This method sets the value which was possibly logged when a client
		 * exits the screen.
		 * 
		 * @param logValueExit
		 *            A String which contains the value.
		 */
		public void setLogValueExit(String logValueExit) {
			this.logValueExit = logValueExit;
		}

		/**
		 * This method gets the time when a client entered the screen.
		 * 
		 * @return the logged time
		 */
		public Long getLogTimeEnter() {
			return logTimeEnter;
		}

		/**
		 * This method the time when a client entered the screen.
		 * 
		 * @param logTimeEnter
		 *            A Long which represents the enter time.
		 */
		public void setLogTimeEnter(Long logTimeEnter) {
			this.logTimeEnter = logTimeEnter;
		}
	}

	/**
	 * The class ResponseObject. It implements the IScreenParamObject, so the
	 * class can be used to wrap parameters for a response to the server.
	 */
	public static class ResponseObject implements IScreenParamObject {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = -4251351589318484239L;

	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7881637153570029242L;

	/** The game id. */
	private String gameId;

	/** The main frame. */
	protected MainFrame mainFrame;

	/** The gui controller. */
	private ClientGuiController guiController;

	/** The parameter. */
	private ParamObject parameter;

	/** The screen id. */
	private String screenId;

	/** The log display event. */
	private Boolean logDisplayEvent = false;

	/** The show up time. */
	private Long showUpTime = 0l;

	/**
	 * The Screen itself so it can be used in ActionListeners such as Buttons
	 */
	protected Screen thisScreen;

	/**
	 * This method creates an instance of Screen.
	 * 
	 * @param gameId
	 *            A String which contains the ID of the running game, whose
	 *            institution triggered that screen to be shown at the client.
	 * @param parameter
	 *            A ParamObject list of all parameters used in this screen. i.e.
	 *            text.
	 * @param screenId
	 *            A String which contains the global screen ID. It has to be
	 *            given for a complete trial entry at the server side.
	 * @param showUpTime
	 *            A Long which indicates how long the Screen will be shown to a
	 *            client.
	 */
	public Screen(String gameId, ParamObject parameter, String screenId, Long showUpTime) {
		this.mainFrame = MainFrame.getInstance();
		this.guiController = ClientGuiController.getInstance();
		this.gameId = gameId;
		this.parameter = parameter == null ? new ParamObject() : parameter;
		this.screenId = screenId;
		this.showUpTime = showUpTime;
		this.thisScreen = this;
	}

	/**
	 * This method gets the default button.
	 * 
	 * @return the default button
	 */
	public JButton getDefaultButton() {
		return mainFrame.getRootPane().getDefaultButton();
	}

	/**
	 * This method sets the default button.
	 * 
	 * @param screenDefaultButton
	 *            The new default button.
	 */
	public void setDefaultButton(JButton screenDefaultButton) {
		mainFrame.getRootPane().setDefaultButton(screenDefaultButton);
	}

	/**
	 * This method gets the game ID.
	 * 
	 * @return the game ID
	 */
	public String getGameId() {
		return gameId;
	}

	/**
	 * This method sets the game ID.
	 * 
	 * @param gameId
	 *            A String which contains the ID of the running game.
	 */
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	/**
	 * This method gets the list of parameters of the screen.
	 * 
	 * @return the parameter(s)
	 */
	public ParamObject getParameter() {
		return parameter;
	}

	/**
	 * This method sets the parameters of the screen.
	 * 
	 * @param parameter
	 *            The new ParamObject parameters.
	 */
	public void setParameter(ParamObject parameter) {
		this.parameter = parameter;
	}

	/**
	 * This method gets the screen ID.
	 * 
	 * @return the screen ID
	 */
	public String getScreenId() {
		return screenId;
	}

	public Dimension getScreenSize(){
		return mainFrame.getSize();
	}
	
	/**
	 * This method sets the screen ID.
	 * 
	 * @param screenId
	 *            A String which contains the global screen ID.
	 */
	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

	/**
	 * This method gets the log display event.
	 * 
	 * @return the log display event
	 */
	public Boolean getLogDisplayEvent() {
		return logDisplayEvent;
	}

	/**
	 * This method gets the show up time.
	 * 
	 * @return the show up time
	 */
	public Long getShowUpTime() {
		return showUpTime;
	}

	/**
	 * This method sets the show up time.
	 * 
	 * @param showUpTime
	 *            A Long which indicates how long the Screen will be shown to a
	 *            client.
	 */
	public void setShowUpTime(Long showUpTime) {
		this.showUpTime = showUpTime;
	}

	/**
	 * This method gets the text which was possibly logged when a client enters
	 * the screen.
	 * 
	 * @return logValueEnter the text
	 */
	public String getLogTextEnter() {
		return parameter.getLogTextEnter();
	}

	/**
	 * This method gets the text which was possibly logged when a client exits
	 * the screen.
	 * 
	 * @return the exit text
	 */
	public String getLogTextExit() {
		return parameter.getLogTextExit();
	}

	/**
	 * This method gets the value which was possibly logged when a client enters
	 * the screen.
	 * 
	 * @return the enter text
	 */
	public String getLogValueEnter() {
		return parameter.getLogTextEnter();
	}

	/**
	 * This method gets the text which was possibly logged when a client exits
	 * the screen.
	 * 
	 * @return the exit text
	 */
	public String getLogValueExit() {
		return parameter.getLogValueExit();
	}

	/**
	 * This method sends a ResponseObject back to the server.
	 * 
	 * @param response
	 *            A ResponseObject variable which contains parameters for the
	 *            answer to the server.
	 */
	public void sendResponse(IScreenParamObject response) {
		guiController.sendClientResponse(response, getGameId(), getScreenId());
	}

	/**
	 * This method sends a ResponseObject back to the server and shows the
	 * default waiting screen.
	 * 
	 * @param response
	 *            A ResponseObject variable which contains parameters for the
	 *            answer to the server.
	 */
	public void sendResponseAndWait(IScreenParamObject response) {
		guiController.sendClientResponseMessageAndWait(response, getGameId(), getScreenId());
	}

	/**
	 * If true, the time span the screen is visible for a client will be logged
	 * as trial with SCREEN VISIBLE EVENT.
	 * 
	 * @param logDisplayEvent
	 *            A Boolean which indicates if the time will be logged (true) or
	 *            not (false).
	 */
	public void setLogDisplayEvent(Boolean logDisplayEvent) {
		this.logDisplayEvent = logDisplayEvent;
	}

	/**
	 * This method is called when an updated ParamObject is received from the
	 * server.
	 * 
	 * @param newParamObject
	 *            the new param object
	 */
	public void processParamObjectUpdate(IScreenParamObject newParamObject) {

	}

	/**
	 * This method is called before a screen is replaced by another screen.
	 */
	public void extiScreen() {

	}
}
