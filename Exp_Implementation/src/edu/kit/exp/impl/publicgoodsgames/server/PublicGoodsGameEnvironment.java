package edu.kit.exp.impl.publicgoodsgames.server;

import edu.kit.exp.server.microeconomicsystem.Environment;
import edu.kit.exp.server.microeconomicsystem.RoleMatcher;
import edu.kit.exp.server.microeconomicsystem.SubjectGroupMatcher;

public class PublicGoodsGameEnvironment extends Environment {

	// roles
	private static String USER = "User";

	private double bucketmoney; // money in public bucket
	private double multiplier = 1.5; // constant to be multiplied with bucket
										// money
	private double startmoney = 20; // amount each gets at the start of the
									// period

	public PublicGoodsGameEnvironment() {
		roles.add(USER);
		this.roleMatcher = new PublicGoodsGameRoleMatcher(roles);
		this.subjectGroupMatcher = new PublicGoodsGameSubjectGroupMatcher(roles);
		setResetMatchersAfterTreatmentBlocks(false);
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
	 * @return the startmoney
	 */
	public double getStartmoney() {
		return startmoney;
	}

	/**
	 * @param startmoney
	 *            the start money to set
	 */
	public void setStartmoney(double startmoney) {
		this.startmoney = startmoney;
	}

	/**
	 * @return the bucket money
	 */
	public double getBucketmoney() {
		return bucketmoney;
	}

	/**
	 * @param d
	 *            the bucket money to set
	 */
	public void setBucketmoney(double d) {
		this.bucketmoney = d;
	}

	/**
	 * @return the multiplier
	 */
	public double getMultiplier() {
		return multiplier;
	}

	/**
	 * @param multiplier
	 *            the multiplier to set
	 */
	public void setMultiplier(double multiplier) {
		this.multiplier = multiplier;
	}

}
