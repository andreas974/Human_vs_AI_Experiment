package edu.kit.exp.impl.publicgoodsgames.server;

import java.util.List;

import edu.kit.exp.server.jpa.entity.Period;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.microeconomicsystem.RoleMatcher;

public class PublicGoodsGameRoleMatcher extends RoleMatcher {

	public PublicGoodsGameRoleMatcher(List<String> roles) {
		super(roles);
	}

	@Override
	public List<Subject> setupSubjectRoles(List<Subject> subjects) {
		return subjects;
	}

	@Override
	public List<Subject> rematch(Period period, List<Subject> subjects) {
		return subjects;
	}

}
