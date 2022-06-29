package edu.kit.exp.impl.browserExperiment.server;

import java.io.Serializable;

import edu.kit.exp.server.microeconomicsystem.Environment;
import edu.kit.exp.server.microeconomicsystem.RoleMatcher;
import edu.kit.exp.server.microeconomicsystem.SingleRoleGroupMatcher;
import edu.kit.exp.server.microeconomicsystem.SingleRoleRoleMatcher;
import edu.kit.exp.server.microeconomicsystem.SubjectGroupMatcher;

/**
 * The Class BrowserExperimentEnvironment provides an implementation of the
 * necessary microeconomic environment for running a BrowserExperiment.
 * 
 * @see Environment
 */
public class BrowserExperimentEnvironment extends Environment implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7893037084477956472L;

	/**
	 * This constructor instantiates a new browser experiment environment. </br>
	 * A single Role, User is defined and a SingleRoleMatcher and a
	 * BrowserExperimentRoleMatcher are instantiated.
	 */
	public BrowserExperimentEnvironment() {
		super();
		this.roles.add("User");
		this.roleMatcher = new SingleRoleRoleMatcher(roles);
		this.subjectGroupMatcher = new SingleRoleGroupMatcher(roles);
		this.setResetMatchersAfterTreatmentBlocks(false);
	}

	@Override
	public RoleMatcher getRoleMatcher() {
		return roleMatcher;
	}

	@Override
	public SubjectGroupMatcher getSubjectGroupMatcher() {
		return subjectGroupMatcher;
	}
}
