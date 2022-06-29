package edu.kit.exp.impl.gaugemeter.server;

import edu.kit.exp.impl.gaugemeter.client.GaugeMeterScreen;
import edu.kit.exp.impl.ultimatumgame.client.UltimatumGameParamObject;
import edu.kit.exp.server.communication.ClientResponseMessage;
import edu.kit.exp.server.communication.ServerMessageSender;
import edu.kit.exp.server.jpa.entity.Membership;
import edu.kit.exp.server.microeconomicsystem.Institution;

import java.io.Serializable;
import java.util.List;

public class GaugeMeterExperimentInstitution extends Institution<GaugeMeterExperimentEnvironment>
		implements Serializable {
	private static final long serialVersionUID = 739920533886094576L;

	public GaugeMeterExperimentInstitution(GaugeMeterExperimentEnvironment environment, List<Membership> memberships,
			ServerMessageSender messageSender, String gameId) {
		super(environment, memberships, messageSender, gameId);
	}

	@Override
	public void startPeriod() throws Exception {
		for (Membership membership : memberships) {
			showScreenWithDeadLine(membership.getSubject(), GaugeMeterScreen.class, new UltimatumGameParamObject(), 50000L);
		}
	}

	@Override
	public void processMessage(ClientResponseMessage msg) throws Exception {
		endPeriod();
	}

	@Override
	protected void endPeriod() {
		this.finished = true;
	}
}
