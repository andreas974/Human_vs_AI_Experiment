package edu.kit.exp.impl.gaugemeter.server;

import java.io.Serializable;

import edu.kit.exp.server.microeconomicsystem.Environment;
import edu.kit.exp.server.microeconomicsystem.RoleMatcher;
import edu.kit.exp.server.microeconomicsystem.SingleRoleGroupMatcher;
import edu.kit.exp.server.microeconomicsystem.SingleRoleRoleMatcher;
import edu.kit.exp.server.microeconomicsystem.SubjectGroupMatcher;

public class GaugeMeterExperimentEnvironment extends Environment implements Serializable {
	private static final long serialVersionUID = -7893037084477956472L;

	public GaugeMeterExperimentEnvironment() {
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
