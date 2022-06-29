package edu.kit.exp.impl.browserExperiment.server;

import java.io.Serializable;
import java.util.List;

import edu.kit.exp.client.gui.screens.Screen;
import edu.kit.exp.impl.browserExperiment.client.BrowserExperimentInitialScreen;
import edu.kit.exp.server.communication.ClientResponseMessage;
import edu.kit.exp.server.communication.ServerMessageSender;
import edu.kit.exp.server.jpa.entity.Membership;
import edu.kit.exp.server.microeconomicsystem.Institution;

/**
 * The class BrowserExperimentInstitution provides a microeconomic institution
 * for a BrowserExperiment.</br>
 * It is based on an environment which is defined in
 * {@link edu.kit.exp.impl.browserExperiment.server.BrowserExperimentEnvironment
 * BrowserExperimentEnvironment}.
 * 
 * @see Institution
 * @see BrowserExperimentEnvironment
 */
public class BrowserExperimentInstitution extends Institution<BrowserExperimentEnvironment> implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 739920533886094576L;

	/**
	 * This constructor instantiates a new BrowserExperimentInstitution.
	 * 
	 * @param environment
	 *            the
	 *            {@link edu.kit.exp.impl.browserExperiment.server.BrowserExperimentEnvironment
	 *            BrowserExperimentEnvironment}
	 * @param memberships
	 *            A Membership list which contains roles for subjects.
	 * @param messageSender
	 *            A ServerMessageSender for sending messages to clients.
	 * @param gameId
	 *            A String variable which contains the ID of the game.
	 */
	public BrowserExperimentInstitution(BrowserExperimentEnvironment environment, List<Membership> memberships,
			ServerMessageSender messageSender, String gameId) {
		super(environment, memberships, messageSender, gameId);
	}

	/**
	 * This method starts a Period by showing the
	 * <code>BrowserExperimentInitialScreen</code> to the clients.
	 * 
	 * @see edu.kit.exp.impl.browserExperiment.server.BrowserExperimentInstitution
	 *      #startPeriod()
	 */
	@Override
	public void startPeriod() throws Exception {
		for (Membership membership : memberships) {
			showScreen(membership.getSubject(), BrowserExperimentInitialScreen.class, new Screen.ParamObject());
		}
	}

	/**
	 * This method calls the <code>endPeriod</code> method to end the period.
	 * 
	 * @param msg
	 *            ClientResponseMessage which contains a response from the
	 *            client.
	 */
	@Override
	public void processMessage(ClientResponseMessage msg) throws Exception {
		endPeriod();
	}

	@Override
	protected void endPeriod() {
		this.finished = true;
	}
}
