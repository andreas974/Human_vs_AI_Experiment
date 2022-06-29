package edu.kit.exp.impl.publicgoodsgames.server;

import java.util.ArrayList;
import java.util.List;

import edu.kit.exp.server.jpa.entity.Membership;
import edu.kit.exp.server.jpa.entity.Period;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.jpa.entity.SubjectGroup;
import edu.kit.exp.server.microeconomicsystem.SubjectGroupMatcher;

public class PublicGoodsGameSubjectGroupMatcher extends SubjectGroupMatcher {

	public PublicGoodsGameSubjectGroupMatcher(List<String> roles) {
		super(roles);
	}

	@Override
	public List<SubjectGroup> setupSubjectGroups(Period period, List<Subject> subjects) throws Exception {

		List<SubjectGroup> subjectgroups = new ArrayList<SubjectGroup>();
		SubjectGroup subjectgroup = new SubjectGroup();
		subjectgroup.setPeriod(period);

		for (Subject subject : subjects) {
			Membership membership = new Membership();
			membership.setSubject(subject);
			membership.setSubjectGroup(subjectgroup);
			membership.setRole(subject.getRole());
			subjectgroup.getMemberships().add(membership);

		}
		subjectgroups.add(subjectgroup);

		return subjectgroups;
	}

	@Override
	public List<SubjectGroup> rematch(Period period, List<Subject> subjects) throws Exception {
		return setupSubjectGroups(period, subjects);
	}

}
