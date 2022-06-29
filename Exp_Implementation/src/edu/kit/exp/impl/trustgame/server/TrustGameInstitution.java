package edu.kit.exp.impl.trustgame.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.kit.exp.client.gui.screens.DefaultWaitingScreen;
import edu.kit.exp.impl.trustgame.client.TrustGameParamObject;
import edu.kit.exp.impl.trustgame.client.TrustGameResult;
import edu.kit.exp.impl.trustgame.client.TrustGameScreen;
import edu.kit.exp.server.communication.ClientResponseMessage;
import edu.kit.exp.server.communication.ServerMessageSender;
import edu.kit.exp.server.jpa.entity.Membership;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.jpa.entity.Trial;
import edu.kit.exp.server.microeconomicsystem.Institution;
import edu.kit.exp.server.structure.SubjectManagement;
import edu.kit.exp.server.structure.TrialManagement;

public class TrustGameInstitution extends Institution<TrustGameEnvironment> {

	private static String RECEIVER = "Receiver";
	private Subject receiver;
	private Subject contributor;

	// input
	private int rec_input; // money given to contributor
	private int con_input; // money shared to receiver

	private String screenId;

	public TrustGameInstitution(TrustGameEnvironment environment, List<Membership> memberships,
			ServerMessageSender messageSender, String gameId) {
		super(environment, memberships, messageSender, gameId);
	}

	@Override
	public void startPeriod() throws Exception {
		System.out.println("Institution started");

		// Find receiver and contributor
		for (Membership membership : memberships) {
			if (membership.getRole().equals(RECEIVER)) {
				receiver = membership.getSubject();
			} else {
				contributor = membership.getSubject();
			}
		}

		TrustGameParamObject paramobject = new TrustGameParamObject();
		paramobject.setMessage(String.valueOf("<html><bod><h1>You have " + environment.getStartmoney()
				+ " Euro and the multiplier is " + environment.getMultiplier()
				+ " .<br>How much do you want to send to the other participant?"));
		paramobject.setInput(environment.getStartmoney());

		showScreen(receiver, TrustGameScreen.class, paramobject);
		showScreen(contributor, DefaultWaitingScreen.class, new DefaultWaitingScreen.ParamObject());
	}

	@Override
	public void processMessage(ClientResponseMessage msg) throws Exception {

		// read message
		Long clientTimeStamp = msg.getClientTimeStamp();
		Long serverTimeStamp = msg.getServerTimeStamp();
		String clientId = msg.getClientId();
		TrustGameParamObject parameter = msg.getParameters();
		screenId = msg.getScreenId();

		System.out.println("Institution received message: " + msg.toString());

		// check who send the message
		if (clientId.equals(receiver.getIdClient())) {
			rec_input = parameter.getInput();
			environment.setOffermoney(parameter.getInput() * environment.getMultiplier());

			// Create new Trial and save it in DB
			Trial trial = new Trial();
			trial.setSubject(receiver);
			trial.setSubjectGroup(getSubjectGroup());
			trial.setScreenName(screenId);
			trial.setClientTime(clientTimeStamp);
			trial.setServerTime(serverTimeStamp);
			trial.setData("User action", String.valueOf(parameter.getInput()));
			trial.setPrintToServerTab(true);
			TrialManagement.getInstance().createNewTrial(trial);

			showScreen(receiver, DefaultWaitingScreen.class, new DefaultWaitingScreen.ParamObject());

			TrustGameParamObject tg = new TrustGameParamObject();
			tg.setMessage(String.valueOf("<html><bod><h1> You get " + rec_input
					+ " Euro from the other subject.<br> Multiplied with the number of " + environment.getMultiplier()
					+ ", you now have " + environment.getOffermoney()
					+ " Euro.</h1><br>How much do you want to pay back to the other subject?"));
			tg.setInput(environment.getOffermoney());
			showScreen(contributor, TrustGameScreen.class, tg);

		} else {

			con_input = parameter.getInput();

			// Create new Trial and save it in DB
			Trial trial = new Trial();
			trial.setSubject(contributor);
			trial.setSubjectGroup(getSubjectGroup());
			trial.setScreenName(screenId);
			trial.setClientTime(clientTimeStamp);
			trial.setServerTime(serverTimeStamp);
			trial.setData("User action", String.valueOf(parameter.getInput()));
			trial.setPrintToServerTab(true);
			TrialManagement.getInstance().createNewTrial(trial);

			endPeriod();
		}

	}

	@Override
	protected void endPeriod() throws Exception {

		// receiver payoff
		int rec_pay = con_input - rec_input;
		if (rec_pay < 0) {
			rec_pay = 0;
		}
		receiver.setPayoff(receiver.getPayoff() + rec_pay);
		SubjectManagement.getInstance().updateSubject(receiver);

		Trial trial_a = new Trial();
		trial_a.setSubject(receiver);
		trial_a.setSubjectGroup(getSubjectGroup());
		trial_a.setScreenName(screenId);
		trial_a.setServerTime(new Date().getTime());
		trial_a.setData("PAYOFFS", "receiver payoff", receiver.getPayoff().toString());
		TrialManagement.getInstance().createNewTrial(trial_a);

		// contributor payoff
		int con_payoff = environment.getOffermoney() - con_input;
		contributor.setPayoff(contributor.getPayoff() + con_input);
		SubjectManagement.getInstance().updateSubject(contributor);

		Trial trial_b = new Trial();
		trial_b.setSubject(contributor);
		trial_b.setSubjectGroup(getSubjectGroup());
		trial_b.setScreenName(screenId);
		trial_b.setServerTime(new Date().getTime());
		trial_b.setData("PAYOFFS", "contributor payoff", contributor.getPayoff().toString());
		TrialManagement.getInstance().createNewTrial(trial_b);

		TrustGameParamObject tg = new TrustGameParamObject();
		tg.setMessage(String.valueOf("<html><body><h1>Result</h1>You send " + rec_input
				+ " Euro.<br> The other subject gave you " + con_input + " Euro."));
		showScreenWithDeadLine(receiver, TrustGameResult.class, tg, 10000L);

		tg.setMessage(String.valueOf("<html><body><h1>Result</h1>You get " + con_payoff + " Euro."));
		showScreenWithDeadLine(contributor, TrustGameResult.class, tg, 10000L);

		List<Subject> subjects = new ArrayList<Subject>();
		subjects.add(receiver);
		subjects.add(contributor);

		this.finished = true;

	}

}
