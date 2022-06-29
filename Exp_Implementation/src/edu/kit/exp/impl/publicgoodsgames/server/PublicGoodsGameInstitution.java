package edu.kit.exp.impl.publicgoodsgames.server;

import java.util.Date;
import java.util.List;

import edu.kit.exp.client.gui.screens.DefaultWaitingScreen;
import edu.kit.exp.impl.publicgoodsgames.client.PublicGoodsGameParamObject;
import edu.kit.exp.impl.publicgoodsgames.client.PublicGoodsGameResult;
import edu.kit.exp.impl.publicgoodsgames.client.PublicGoodsGameScreen;
import edu.kit.exp.server.communication.ClientResponseMessage;
import edu.kit.exp.server.communication.ServerMessageSender;
import edu.kit.exp.server.jpa.entity.Membership;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.jpa.entity.Trial;
import edu.kit.exp.server.microeconomicsystem.Institution;
import edu.kit.exp.server.structure.SubjectManagement;
import edu.kit.exp.server.structure.TrialManagement;

public class PublicGoodsGameInstitution extends Institution<PublicGoodsGameEnvironment> {

	private int count = 0; // counts how many subjects have given their input
	private Subject subject;
	private double distributedmoney; // money after bucket money is equally
										// split
	private String screenId;

	public PublicGoodsGameInstitution(PublicGoodsGameEnvironment environment, List<Membership> memberships,
			ServerMessageSender messageSender, String gameId) {
		super(environment, memberships, messageSender, gameId);
	}

	@Override
	public void startPeriod() throws Exception {

		System.out.println("Institution started");

		PublicGoodsGameParamObject pggParamObject = new PublicGoodsGameParamObject();
		pggParamObject.setMoney(environment.getStartmoney());
		pggParamObject.setMessage(String.valueOf("<html><bod><h1>You have " + environment.getStartmoney()
				+ " Euro.</h1>" + "You can contribute between 0 and " + environment.getStartmoney()
				+ " Euro.<br>How much do you want to contribute?"));

		for (Membership membership : memberships) {
			showScreen(membership.getSubject(), PublicGoodsGameScreen.class, pggParamObject);
		}

	}

	@Override
	public void processMessage(ClientResponseMessage msg) throws Exception {

		// read message
		Long clientTimeStamp = msg.getClientTimeStamp();
		Long serverTimeStamp = msg.getServerTimeStamp();
		String clientId = msg.getClientId();
		PublicGoodsGameParamObject parameter = msg.getParameters();
		screenId = msg.getScreenId();

		System.out.println("Institution received message: " + msg.toString());

		count++;

		// search for subject
		for (Membership membership : memberships) {
			if (membership.getSubject().getIdClient().equals(clientId)) {
				this.subject = membership.getSubject();
			}
		}

		environment.setBucketmoney(environment.getBucketmoney() + parameter.getInput());

		// Create new Trial and save it in DB
		Trial trial = new Trial();
		trial.setSubject(subject);
		trial.setSubjectGroup(getSubjectGroup());
		trial.setScreenName(screenId);
		trial.setClientTime(clientTimeStamp);
		trial.setServerTime(serverTimeStamp);
		trial.setData("User action", String.valueOf(parameter.getInput()));
		trial.setPrintToServerTab(true);
		TrialManagement.getInstance().createNewTrial(trial);

		// check if every subject is done
		int subjects = memberships.size();
		if (count == subjects) {
			endPeriod();
		} else {
			showScreen(subject, DefaultWaitingScreen.class, new DefaultWaitingScreen.ParamObject());
		}

	}

	// specifies which screen is displayed at the end of a period
	@Override
	protected void endPeriod() throws Exception {

		// Calculate evenly distributed bucketmoney
		distributedmoney = (environment.getBucketmoney() * environment.getMultiplier()) / memberships.size();

		// cut number after 2 decimal places
		distributedmoney = Math.floor(distributedmoney * 100) / 100;

		for (Membership membership : memberships) {
			subject = membership.getSubject();

			// get subjects input
			int index = subject.getTrials().size() - 1;
			Trial last = subject.getTrials().get(index);

			double input = Double.parseDouble(last.getValue());
			double subjectmoney = distributedmoney - input;

			// non negative pay-off
			if (subjectmoney < 0) {
				subjectmoney = 0;
			}

			subject.setPayoff(subject.getPayoff() + subjectmoney);

			SubjectManagement.getInstance().updateSubject(subject);

			// Create new Trials to log the pay-offs in DB
			Trial trial = new Trial();
			trial.setSubject(subject);
			trial.setSubjectGroup(getSubjectGroup());
			trial.setScreenName(screenId);
			trial.setServerTime(new Date().getTime());
			trial.setData("PAYOFFS", subject.getIdSubject().toString(), subject.getPayoff().toString());
			TrialManagement.getInstance().createNewTrial(trial);

			PublicGoodsGameParamObject resultparameters = new PublicGoodsGameParamObject();
			resultparameters.setMessage(
					String.valueOf("<html><body><h1>Result</h1>The amount of money contributed to the bucket is: "
							+ environment.getBucketmoney() + " Euro.<br>You contributed: " + input
							+ " Euro.</body></html>"));
			showScreenWithDeadLine(subject.getIdClient(), PublicGoodsGameResult.class, resultparameters, 10000L);

		}
		this.finished = true;
	}

}
