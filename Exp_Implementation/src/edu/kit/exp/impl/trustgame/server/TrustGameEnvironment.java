package edu.kit.exp.impl.trustgame.server;

import edu.kit.exp.server.microeconomicsystem.Environment;
import edu.kit.exp.server.microeconomicsystem.PartnerGroupMatcher;
import edu.kit.exp.server.microeconomicsystem.PartnerRoleMatcher;
import edu.kit.exp.server.microeconomicsystem.RoleMatcher;
import edu.kit.exp.server.microeconomicsystem.SubjectGroupMatcher;

public class TrustGameEnvironment extends Environment {

	// roles
	private static String RECEIVER = "Receiver";
	private static String CONTRIBUTOR = "Contributor";

	private int startmoney = 20; // money at the start each subject gets
	private int offermoney; // money given to contributor to be split
	private int multiplier = 3;

	public TrustGameEnvironment() {
		roles.add(RECEIVER);
		roles.add(CONTRIBUTOR);
		this.roleMatcher = new PartnerRoleMatcher(roles);
		this.subjectGroupMatcher = new PartnerGroupMatcher(roles);
		this.setResetMatchersAfterTreatmentBlocks(false);

	}

	@Override
	public RoleMatcher getRoleMatcher() {
		return this.roleMatcher;
	}

	@Override
	public SubjectGroupMatcher getSubjectGroupMatcher() {
		return this.subjectGroupMatcher;
	}

	/**
	 * @return the startmoney
	 */
	public int getStartmoney() {
		return startmoney;
	}

	/**
	 * TODO: probably not needed
	 * 
	 * @param startmoney
	 *            the startmoney to set
	 */
	public void setStartmoney(int startmoney) {
		this.startmoney = startmoney;
	}

	/**
	 * @return the offermoney
	 */
	public int getOffermoney() {
		return offermoney;
	}

	/**
	 * @param offermoney
	 *            the offermoney to set
	 */
	public void setOffermoney(int offermoney) {
		this.offermoney = offermoney;
	}

	/**
	 * @return the multiplier
	 */
	public int getMultiplier() {
		return multiplier;
	}

}
