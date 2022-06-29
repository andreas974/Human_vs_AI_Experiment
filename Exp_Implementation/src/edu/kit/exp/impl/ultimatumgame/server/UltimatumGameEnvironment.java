package edu.kit.exp.impl.ultimatumgame.server;

import edu.kit.exp.server.microeconomicsystem.Environment;
import edu.kit.exp.server.microeconomicsystem.PartnerGroupMatcher;
import edu.kit.exp.server.microeconomicsystem.RoleMatcher;
import edu.kit.exp.server.microeconomicsystem.SubjectGroupMatcher;

/**
 * 
 * This Class represents the microeconomic environment in an ultimatum game.
 * </br>
 * It defines the roles of the subjects and the amount of money to share during
 * the game.
 * 
 * @see Environment
 */
public class UltimatumGameEnvironment extends Environment {

	// Roles
	private static String ROLE_REQUESTER = "Requester";
	private static String ROLE_RESPONDER = "Responder";
	// money to share
	private Integer moneyToShare = 10;

	/**
	 * This constructor instantiates a new ultimatum game environment. It adds
	 * roles, a roleMatcher and a subjectGroupMatcher.
	 * 
	 * @see UltimatumGameRoleMatcher
	 * @see PartnerGroupMatcher
	 */
	public UltimatumGameEnvironment() {

		this.roles.add(ROLE_REQUESTER);
		this.roles.add(ROLE_RESPONDER);
		this.roleMatcher = new UltimatumGameRoleMatcher(roles);
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
	 * This method gets the money to share.
	 * 
	 * @return the money to share
	 */
	public Integer getMoneyToShare() {
		return moneyToShare;
	}

	/**
	 * This method sets the money to share.
	 * 
	 * @param moneyToShare
	 *            An Integer variable which contains the money to share.
	 */
	public void setMoneyToShare(Integer moneyToShare) {
		this.moneyToShare = moneyToShare;
	}

}
