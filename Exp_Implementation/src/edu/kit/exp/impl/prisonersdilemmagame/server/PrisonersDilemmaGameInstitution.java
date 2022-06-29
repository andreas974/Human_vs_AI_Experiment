package edu.kit.exp.impl.prisonersdilemmagame.server;

import java.util.Date;
import java.util.List;

import edu.kit.exp.client.gui.screens.DefaultWaitingScreen;
import edu.kit.exp.impl.prisonersdilemmagame.client.PrisonersDilemmaGameParamObject;
import edu.kit.exp.impl.prisonersdilemmagame.client.PrisonersDilemmaGameResult;
import edu.kit.exp.impl.prisonersdilemmagame.client.PrisonersDilemmaGameScreen;
import edu.kit.exp.server.communication.ClientResponseMessage;
import edu.kit.exp.server.communication.ServerMessageSender;
import edu.kit.exp.server.jpa.entity.Membership;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.jpa.entity.Trial;
import edu.kit.exp.server.microeconomicsystem.Institution;
import edu.kit.exp.server.structure.SubjectManagement;
import edu.kit.exp.server.structure.TrialManagement;

public class PrisonersDilemmaGameInstitution extends Institution<PrisonersDilemmaGameEnvironment> {

	private static String USER_A = "User A";

	private int count = 0; // counts how many subjects have given their input
	private Subject user_a;
	private Subject user_b;
	private String screenId;

	// input
	private String user_a_input;
	private String user_b_input;

	// prison time
	private int user_a_time;
	private int user_b_time;

	public PrisonersDilemmaGameInstitution(PrisonersDilemmaGameEnvironment environment, List<Membership> memberships,
			ServerMessageSender messageSender, String gameId) {
		super(environment, memberships, messageSender, gameId);
	}

	@Override
	public void startPeriod() throws Exception {

		System.out.println("Institution started");

		PrisonersDilemmaGameParamObject paramobject = new PrisonersDilemmaGameParamObject();
		paramobject.setMessage(String.valueOf("<html><bod><h1>You have the option to betray the other person "
				+ "or to stay silent.<br>What do you choose?"));

		// Find requester and responder
		for (Membership membership : memberships) {
			if (membership.getRole().equals(USER_A)) {
				user_a = membership.getSubject();
			} else {
				user_b = membership.getSubject();
			}
		}

		for (Membership membership : memberships) {
			showScreen(membership.getSubject(), PrisonersDilemmaGameScreen.class, paramobject);
		}
	}

	@Override
	public void processMessage(ClientResponseMessage msg) throws Exception {

		// read message
		Long clientTimeStamp = msg.getClientTimeStamp();
		Long serverTimeStamp = msg.getServerTimeStamp();
		String clientId = msg.getClientId();
		PrisonersDilemmaGameParamObject parameter = msg.getParameters();
		screenId = msg.getScreenId();

		System.out.println("Institution received message: " + msg.toString());

		count++;

		Subject subject;
		if (clientId.equals(user_a.getIdClient())) {
			subject = user_a;
			user_a_input = parameter.getInput();
		} else {
			subject = user_b;
			user_b_input = parameter.getInput();
		}

		// Create new Trial and save it in DB
		Trial trial = new Trial();
		trial.setSubject(subject);
		trial.setSubjectGroup(getSubjectGroup());
		trial.setScreenName(screenId);
		trial.setClientTime(clientTimeStamp);
		trial.setServerTime(serverTimeStamp);
		trial.setData("User action", parameter.getInput());
		trial.setPrintToServerTab(true);
		TrialManagement.getInstance().createNewTrial(trial);

		// check if every subject is done
		if (count == 2) {
			endPeriod();
		} else {
			showScreen(subject, DefaultWaitingScreen.class, new DefaultWaitingScreen.ParamObject());
		}

	}

	@Override
	protected void endPeriod() throws Exception {

		int average = environment.getBetrayboth(); // average number for
													// pay-off calculation

		if (user_a_input.equals(user_b_input)) {
			if (user_a_input.equals("BETRAY")) { // both betray each other
				setTime(environment.getBetrayboth(), environment.getBetrayboth());

			} else { // both stay silent
				setTime(environment.getSilentboth(), environment.getSilentboth());
				int addition = average - user_a_time;

				user_a.setPayoff(user_a.getPayoff() + addition);
				user_b.setPayoff(user_b.getPayoff() + addition);
			}

		} else {
			// Subjects don't get negative pay-offs
			if (user_a_input.equals("BETRAY")) { // only one gets betrayed
				setTime(0, environment.getOnebetray());

				user_a.setPayoff(user_a.getPayoff() + average);

			} else {
				setTime(environment.getOnebetray(), 0);

				user_b.setPayoff(user_b.getPayoff() + average);
			}
		}

		SubjectManagement.getInstance().updateSubject(user_a);
		SubjectManagement.getInstance().updateSubject(user_b);

		// Create new Trials to log the payoffs in DB
		Trial trial_a = new Trial();
		trial_a.setSubject(user_a);
		trial_a.setSubjectGroup(getSubjectGroup());
		trial_a.setScreenName(screenId);
		trial_a.setServerTime(new Date().getTime());
		trial_a.setData("PAYOFFS", "A payoff", user_a.getPayoff().toString());
		TrialManagement.getInstance().createNewTrial(trial_a);

		// Create new Trials to log the payoffs in DB
		Trial trial_b = new Trial();
		trial_b.setSubject(user_b);
		trial_b.setSubjectGroup(getSubjectGroup());
		trial_b.setScreenName(screenId);
		trial_b.setServerTime(new Date().getTime());
		trial_b.setData("PAYOFFS", "B payoff", user_b.getPayoff().toString());
		TrialManagement.getInstance().createNewTrial(trial_b);

		// Screen for User A
		PrisonersDilemmaGameParamObject resultparameters = new PrisonersDilemmaGameParamObject();
		resultparameters.setMessage(
				String.valueOf("<html><body><h1>Result</h1>You get a prison time of " + user_a_time + " year(s)."));
		showScreenWithDeadLine(user_a, PrisonersDilemmaGameResult.class, resultparameters, 10000L);

		// Screen for User B
		resultparameters.setMessage(
				String.valueOf("<html><body><h1>Result</h1>You get a prison time of " + user_b_time + " year(s)."));
		showScreenWithDeadLine(user_b, PrisonersDilemmaGameResult.class, resultparameters, 10000L);

		this.finished = true;
	}

	/**
	 * Set prison time of subjects
	 * 
	 * @param time_a
	 *            prison time of user_a
	 * @param time_b
	 *            prison time of user_b
	 */
	private void setTime(int time_a, int time_b) {
		user_a_time = time_a;
		user_b_time = time_b;
	}

}
