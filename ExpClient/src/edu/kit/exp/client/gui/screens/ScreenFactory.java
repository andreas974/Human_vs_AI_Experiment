package edu.kit.exp.client.gui.screens;

import edu.kit.exp.common.IScreenParamObject;

/**
 * This class provides functionality to to create screens.
 */
public class ScreenFactory {

	/**
	 * This constructor instantiates a new screen factory.
	 */
	private ScreenFactory() {

	}

	/**
	 * This factory method creates an instance of a screen according to the
	 * given screenId.
	 * 
	 * @param <T>
	 *            a generic type which extends
	 *            {@link edu.kit.exp.client.gui.screens.Screen Screen}.
	 * @param screenId
	 *            A String which contains the ID of the screen to be created.
	 * @param parameters
	 *            A IScreenParamObject which provides parameters for the screen.
	 * @param gameId
	 *            A String which contains the ID of the running game.
	 * @param showUpTime
	 *            A Long which contains the time span in milliseconds the screen
	 *            is shown to a client.
	 * @return the screen
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Screen> T createScreen(String screenId, IScreenParamObject parameters, String gameId, Long showUpTime) {
		if (screenId.equals("")) {
			screenId = DefaultInfoScreen.class.getName();
		}

		try {
			Class<T> newScreen = (Class<T>) Class.forName(screenId);
			return (T) (newScreen.getConstructors()[0].newInstance(gameId, parameters, screenId, showUpTime));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
