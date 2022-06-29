package edu.kit.exp.impl.prisonersdilemmagame.server;

import edu.kit.exp.server.microeconomicsystem.Environment;
import edu.kit.exp.server.microeconomicsystem.PartnerGroupMatcher;
import edu.kit.exp.server.microeconomicsystem.PartnerRoleMatcher;
import edu.kit.exp.server.microeconomicsystem.RoleMatcher;
import edu.kit.exp.server.microeconomicsystem.SubjectGroupMatcher;

public class PrisonersDilemmaGameEnvironment extends Environment {

	// roles
	private static String USER_A = "User A";
	private static String USER_B = "User B";

	// time in prison (in years)
	private int betrayboth = 2; // if both subjects choose to betray
	private int onebetray = 3; // if only one subject gets betrayed
	private int silentboth = 1; // if both subjects choose to stay silent

	public PrisonersDilemmaGameEnvironment() {
		roles.add(USER_A);
		roles.add(USER_B);
		this.roleMatcher = new PartnerRoleMatcher(roles);
		this.subjectGroupMatcher = new PartnerGroupMatcher(roles);
	}

	@Override
	public RoleMatcher getRoleMatcher() {
		return roleMatcher;
	}

	@Override
	public SubjectGroupMatcher getSubjectGroupMatcher() {
		return subjectGroupMatcher;
	}

	/**
	 * @return the betrayboth
	 */
	public int getBetrayboth() {
		return betrayboth;
	}

	/**
	 * @return the onebetray
	 */
	public int getOnebetray() {
		return onebetray;
	}

	/**
	 * @return the silentboth
	 */
	public int getSilentboth() {
		return silentboth;
	}

}
