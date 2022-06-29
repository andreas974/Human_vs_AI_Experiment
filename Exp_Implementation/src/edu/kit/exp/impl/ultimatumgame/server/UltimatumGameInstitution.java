package edu.kit.exp.impl.ultimatumgame.server;

import java.util.Date;
import java.util.List;

import edu.kit.exp.client.gui.screens.DefaultWaitingScreen;
import edu.kit.exp.impl.ultimatumgame.client.UltimatumGameParamObject;
import edu.kit.exp.impl.ultimatumgame.client.UltimatumGameRequestScreen;
import edu.kit.exp.impl.ultimatumgame.client.UltimatumGameResponseObject;
import edu.kit.exp.impl.ultimatumgame.client.UltimatumGameResponseScreen;
import edu.kit.exp.impl.ultimatumgame.client.UltimatumGameResultScreen;
import edu.kit.exp.server.communication.ClientResponseMessage;
import edu.kit.exp.server.communication.ServerMessageSender;
import edu.kit.exp.server.jpa.entity.Membership;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.jpa.entity.Trial;
import edu.kit.exp.server.microeconomicsystem.Institution;
import edu.kit.exp.server.structure.SubjectManagement;
import edu.kit.exp.server.structure.TrialManagement;

/**
 * This class provides a microeconomic institution for an ultimatum game.</br>
 * It is based on an environment which is defined in
 * {@link edu.kit.exp.impl.ultimatumgame.server.UltimatumGameEnvironment
 * UltimatumGameEnvironment}. </br>
 * This class defines the sequence of an ultimatum game.
 */
public class UltimatumGameInstitution extends Institution<UltimatumGameEnvironment> {

	/** The requester. */
	private Subject requester;

	/** The responder. */
	private Subject responder;

	/** The ultimatum game environment. */
	private UltimatumGameEnvironment ultimatumGameEnvironment;

	/** The requester share. */
	private Integer requesterShare;

	/** The responder share. */
	private Integer responderShare;

	/** The screen id. */
	private String screenId;

	/**
	 * Instantiates a new ultimatum game institution.
	 * 
	 * @param environment
	 *            the
	 *            {@link edu.kit.exp.impl.ultimatumgame.server.UltimatumGameEnvironment
	 *            UltimatumGameEnvironment}
	 * @param memberships
	 *            A Membership list which contains roles for subjects.
	 * @param messageSender
	 *            A ServerMessageSender for sending messages to clients.
	 * @param gameId
	 *            A String variable which contains the ID of the game.
	 */
	public UltimatumGameInstitution(UltimatumGameEnvironment environment, List<Membership> memberships,
			ServerMessageSender messageSender, String gameId) {
		super(environment, memberships, messageSender, gameId);

		this.ultimatumGameEnvironment = environment;

	}

	/**
	 * This method starts a period of an ultimatum game. </br>
	 * The requesters and responders are identified and treated according to
	 * their role. </br>
	 * Requesters get a budget they have to divide. </br>
	 * Responders wait for the requesters to decide.
	 */
	@Override
	public void startPeriod() throws Exception {

		System.out.println("Institution started");

		// Find requester and responder
		for (Membership membership : memberships) {
			if (membership.getRole().equals("Responder")) {
				responder = membership.getSubject();
			} else {
				requester = membership.getSubject();
			}
		}

		// Requester gets a budget which he has to divide. Responder has to
		// wait.
		UltimatumGameParamObject parameterRequester = new UltimatumGameParamObject();
		parameterRequester.setInfoText("<html><bod><h1>You are requester!</h1>You own "
				+ ultimatumGameEnvironment.getMoneyToShare()
				+ " Euro and you have to share it with an other Agent.<br>How much do you offer to the other agent?");

		showScreen(requester, UltimatumGameRequestScreen.class, parameterRequester);
		showScreen(responder, DefaultWaitingScreen.class, new DefaultWaitingScreen.ParamObject());

	}

	/**
	 * This method processes the messages, sent either from a requester or a
	 * responder. </br>
	 * If the message is from a requester, the result is transferred to the
	 * responder and saved in the DB. </br>
	 * If the message is from a requester, the answer is saved in the DB and the
	 * <code>endPeriod()</code> is called.
	 * 
	 */
	@Override
	public void processMessage(ClientResponseMessage msg) throws Exception {

		// read message
		Long clientTimeStamp = msg.getClientTimeStamp();
		Long serverTimeStamp = msg.getServerTimeStamp();
		String clientId = msg.getClientId();
		UltimatumGameResponseObject parameter = msg.getParameters();
		screenId = msg.getScreenId();

		System.out.println("Institution received mesage: " + msg.toString());
		String sender = clientId;

		// Institution received offer requester
		if (sender.equals(requester.getIdClient())) {

			int offer = Integer.valueOf(parameter.getInputValue());

			// Create new Trial of requesters offer and save it in DB
			Trial trial = new Trial();
			trial.setSubject(requester);
			trial.setSubjectGroup(getSubjectGroup());
			trial.setScreenName(screenId);
			trial.setClientTime(clientTimeStamp);
			trial.setServerTime(serverTimeStamp);
			trial.setData("User action", "OFFER", String.valueOf(offer));
			trial.setPrintToServerTab(true);
			TrialManagement.getInstance().createNewTrial(trial);

			responderShare = offer;
			requesterShare = ultimatumGameEnvironment.getMoneyToShare() - offer;
			UltimatumGameParamObject sendParameter = new UltimatumGameParamObject();
			String message = String.valueOf("<html><body><h1>You are responder!</h1>The requester offers you "
					+ String.valueOf(offer) + " Euro of " + ultimatumGameEnvironment.getMoneyToShare()
					+ " Euro! <br>Do you accept this offer?" + "</body></html>");
			sendParameter.setInfoText(message);

			// Send offer to responder (taker)
			showScreen(responder, UltimatumGameResponseScreen.class, sendParameter);
			showScreen(requester, DefaultWaitingScreen.class, new DefaultWaitingScreen.ParamObject());

		} else {

			// Institution received response of responder
			System.out.println("Institution received responder mesage: " + msg.toString());
			Boolean acceptance = ((UltimatumGameResponseObject) msg.getParameters()).getActionPerformed();

			// Create new Trial of responders answer and save it in DB
			Trial trial = new Trial();
			trial.setSubject(responder);
			trial.setSubjectGroup(getSubjectGroup());
			trial.setClientTime(clientTimeStamp);
			trial.setServerTime(serverTimeStamp);
			trial.setScreenName(screenId);
			trial.setData("User action", "RESPONSE", acceptance.toString());
			trial.setPrintToServerTab(true);
			TrialManagement.getInstance().createNewTrial(trial);

			// Did responder accept?
			if (!acceptance) {
				requesterShare = 0;
				responderShare = 0;
			}

			endPeriod();
		}

	}

	/**
	 * This method calculates the payoffs, saves them in the DB and sends them
	 * to the clients. </br>
	 * Afterwards, the period ends.
	 * 
	 */
	@Override
	protected void endPeriod() throws Exception {

		// Calculate total payoffs
		requester.setPayoff(requester.getPayoff() + requesterShare);
		responder.setPayoff(responder.getPayoff() + responderShare);

		SubjectManagement.getInstance().updateSubject(requester);
		SubjectManagement.getInstance().updateSubject(responder);

		// Create new Trials to log the payoffs in DB
		// Group Trail logs by their common event (eventName = "PAYOFFS")
		Trial trialResponder = new Trial();
		trialResponder.setSubject(responder);
		trialResponder.setSubjectGroup(getSubjectGroup());
		trialResponder.setScreenName(screenId);
		trialResponder.setServerTime(new Date().getTime());
		trialResponder.setData("PAYOFFS", "responder payoff", responder.getPayoff().toString());
		TrialManagement.getInstance().createNewTrial(trialResponder);

		Trial trialRequester = new Trial();
		trialRequester.setSubject(requester);
		trialRequester.setSubjectGroup(getSubjectGroup());
		trialRequester.setScreenName(screenId);
		trialRequester.setServerTime(new Date().getTime());
		trialRequester.setData("PAYOFFS", "requester payoff", requester.getPayoff().toString());
		TrialManagement.getInstance().createNewTrial(trialRequester);

		// Result message --> Display time will be logged at client side
		String message = String.valueOf("<html><body><h1>Result</h1>Requester gets: " + requesterShare
				+ " Euro.<br>Responder gets: " + responderShare + " Euro.</body></html>");
		UltimatumGameParamObject sendParameter = new UltimatumGameParamObject();
		sendParameter.setInfoText(message);

		// show result to subjects for 5 seconds
		showScreenWithDeadLine(responder, UltimatumGameResultScreen.class, sendParameter, 5000L);
		showScreenWithDeadLine(requester, UltimatumGameResultScreen.class, sendParameter, 5000L);

		// Wait 1 minute
		message = String.valueOf("Wait 1 minute between periods...");
		sendParameter = new UltimatumGameParamObject();
		sendParameter.setInfoText(message);

		showScreenWithDeadLine(responder, DefaultWaitingScreen.class, new DefaultWaitingScreen.ParamObject(), 3000L);
		showScreenWithDeadLine(requester, DefaultWaitingScreen.class, new DefaultWaitingScreen.ParamObject(), 3000L);

		// End period
		this.finished = true;

	}

}
