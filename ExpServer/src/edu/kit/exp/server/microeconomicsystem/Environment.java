package edu.kit.exp.server.microeconomicsystem;

import java.util.ArrayList;
import java.util.List;

/**
 * This abstract class represents a microeconomic environment.</br> All
 * environments implemented by an experimenter have to inherit from this class.
 * 
 */
public abstract class Environment {

	/** The subject group matcher. */
	protected SubjectGroupMatcher subjectGroupMatcher;

	/** The role matcher. */
	protected RoleMatcher roleMatcher;

	/** The roles. */
	protected List<String> roles = new ArrayList<>();

	private Boolean resetMatchersAfterTreatmentBlocks = false;

	private int numberOfPeriods;

	/**
	 * This method has to return a customized RoleMatcher, which matches
	 * subjects to roles for each period.
	 * 
	 * @return the role matcher
	 */
	public abstract RoleMatcher getRoleMatcher();

	/**
	 * This method has to return a SubjectGroupMatcher (i.e. PartnerMatcher,
	 * StrangerMatcher) which is used to allocate the different roles in a
	 * SubjectGroup for each period.
	 * 
	 * @return the subject group matcher
	 */
	public abstract SubjectGroupMatcher getSubjectGroupMatcher();

	/**
	 * This method gets the roles.
	 * 
	 * @return a list of the roles
	 */
	public List<String> getRoles() {
		return roles;
	}

	/**
	 * This method sets the roles.
	 * 
	 * @param roles
	 *            the new roles
	 */
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	/**
	 * This method returns true if the matchers will reset after a treatment
	 * block.
	 * 
	 * @return the reset matchers after treatment blocks
	 */
	public Boolean getResetMatchersAfterTreatmentBlocks() {
		return resetMatchersAfterTreatmentBlocks;
	}

	/**
	 * This method sets the parameter which indicates if the matchers will be
	 * reset(true) after a treatment block or not (false).
	 * 
	 * @param resetMatchersAfterTreatmentBlocks
	 *            A Boolean that indicates if the matchers will be reset(
	 *            <code>true</code>) or not(<code>false</code>).
	 */
	public void setResetMatchersAfterTreatmentBlocks(Boolean resetMatchersAfterTreatmentBlocks) {
		this.resetMatchersAfterTreatmentBlocks = resetMatchersAfterTreatmentBlocks;
	}

	/**
	 * This method sets the SubjectGroupMatcher.
	 * 
	 * @param subjectGroupMatcher
	 *            The new SubjectGroupMatcher.
	 * @see SubjectGroupMatcher
	 */
	public void setSubjectGroupMatcher(SubjectGroupMatcher subjectGroupMatcher) {
		this.subjectGroupMatcher = subjectGroupMatcher;
	}

	/**
	 * This method sets the RoleMatcher.
	 * 
	 * @param roleMatcher
	 *            The new RoleMatcher.
	 * @see RoleMatcher
	 */
	public void setRoleMatcher(RoleMatcher roleMatcher) {
		this.roleMatcher = roleMatcher;
	}

	public void setNumberOfPeriods(int number) {
		numberOfPeriods = number;
	}

	public int getNumberOfPeriods() {
		return numberOfPeriods;
	}

	public void refreshRoles() {
		// TODO Auto-generated method stub
	}

}
