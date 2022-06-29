package edu.kit.exp.client.comunication;

import edu.kit.exp.common.IScreenParamObject;

/**
 * The class ServerExperimentMessage provides a ServerMessage for communicating
 * experiment variables.
 */
public class ServerExperimentMessage extends ServerMessage {

	/** The Constant SHOW_SCREEN. */
	public static final Integer SHOW_SCREEN = 0;

	/** The Constant SHOW_GENERAL_SCREEN. */
	public static final Integer SHOW_GENERAL_SCREEN = 1;

	/** The Constant RECONNECT. */
	public static final Integer RECONNECT = 2;

	/** The Constant SHOW_SCREEN_WITH_DEADLINE. */
	public static final Integer SHOW_SCREEN_WITH_DEADLINE = 3;

	/** The type. */
	private Integer type;

	/** The global screen id. */
	private String globalScreenId;

	/** The parameters. */
	private IScreenParamObject parameters;

	/** The game id. */
	private String gameId;

	/** The show up time. */
	private Long showUpTime;

	/**
	 * This constructor instantiates a new server experiment message.
	 * 
	 * @param type
	 *            an Integer type
	 * @param globalScreenId
	 *            A String which contains the global screen ID.
	 * @param parameters
	 *            An IScreenParamObject variable which contains screen
	 *            parameters.
	 * @param gameId
	 *            A String which represents the ID of the game.
	 */
	public ServerExperimentMessage(Integer type, String globalScreenId, IScreenParamObject parameters, String gameId) {
		this(type, globalScreenId, parameters, gameId, 0l);
	}

	/**
	 * This constructor instantiates a new server experiment message.
	 * 
	 * @param type
	 *            an Integer type
	 * @param globalScreenId
	 *            A String which contains the global screen ID.
	 * @param parameters
	 *            An IScreenParamObject variable which contains screen
	 *            parameters.
	 * @param gameId
	 *            A String which represents the ID of the game
	 * @param showUpDuration
	 *            A Long which indicates the time a screen will be shown for.
	 */
	public ServerExperimentMessage(Integer type, String globalScreenId, IScreenParamObject parameters, String gameId, Long showUpDuration) {
		super();
		this.type = type;
		this.globalScreenId = globalScreenId;
		this.parameters = parameters;
		this.gameId = gameId;
		this.setShowUpTime(showUpDuration);
	}

	/**
	 * This method gets the type.
	 * 
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * This method sets the type.
	 * 
	 * @param type
	 *            The new type.
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * This method gets the global screen ID.
	 * 
	 * @return the global screen ID
	 */
	public String getGlobalScreenId() {
		return globalScreenId;
	}

	/**
	 * This method sets the global screen ID.
	 * 
	 * @param globalScreenId
	 *            The new String global screen ID.
	 */
	public void setGlobalScreenId(String globalScreenId) {
		this.globalScreenId = globalScreenId;
	}

	/**
	 * This method gets the parameters.
	 * 
	 * @return the parameters
	 */
	public IScreenParamObject getParameters() {
		return parameters;
	}

	/**
	 * This method sets the parameters.
	 * 
	 * @param parameters
	 *            The new IScreenParamObject parameters.
	 */
	public void setParameters(IScreenParamObject parameters) {
		this.parameters = parameters;
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
	 * @param queueId
	 *            The new String game ID.
	 */
	public void setGameId(String queueId) {
		this.gameId = queueId;
	}

	@Override
	public String toString() {
		return "ServerMessage [type=" + type + ", globalScreenId=" + globalScreenId + ", parameters=" + parameters + ", queueId=" + gameId + "]";
	}

	/**
	 * This method gets the show up time of the screen.
	 * 
	 * @return the show up time
	 */
	public Long getShowUpTime() {
		return showUpTime;
	}

	/**
	 * This method sets the show up time of the screen.
	 * 
	 * @param showUpTime
	 *            The new Long variable which contains the time the screen shows
	 *            up.
	 */
	public void setShowUpTime(Long showUpTime) {
		this.showUpTime = showUpTime;
	}
}
