package edu.kit.exp.server.microeconomicsystem;

import java.util.Collections;
import java.util.List;

import edu.kit.exp.server.jpa.entity.Period;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.run.RandomGeneratorException;

/**
 * The RoleMatcher for single role experiments, i.e., experiments with no
 * interactions or experiments where every subjects are equal and have the same
 * role.
 * 
 */
public class SingleRoleRoleMatcher extends RoleMatcher {

	public SingleRoleRoleMatcher() {
		this("DefaultRole");
	}

	public SingleRoleRoleMatcher(List<String> roles) {
		this(roles.get(0));
	}

	public SingleRoleRoleMatcher(String role) {
		super(Collections.singletonList(role));
	}

	@Override
	public List<Subject> setupSubjectRoles(List<Subject> subjects) throws RandomGeneratorException {
		for (int i = 0; i < getRoles().size(); i++) {
			subjects.get(i).setRole(getRoles().get(0));
		}
		return subjects;
	}

	@Override
	public List<Subject> rematch(Period period, List<Subject> subjects) throws RandomGeneratorException {
		return setupSubjectRoles(subjects);
	}

}
