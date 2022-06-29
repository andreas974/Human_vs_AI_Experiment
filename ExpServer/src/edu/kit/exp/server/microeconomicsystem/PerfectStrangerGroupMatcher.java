package edu.kit.exp.server.microeconomicsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.kit.exp.server.jpa.entity.Membership;
import edu.kit.exp.server.jpa.entity.Period;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.jpa.entity.SubjectGroup;

public class PerfectStrangerGroupMatcher extends SubjectGroupMatcher {

	/*
	 * For a given Subject size, For a given group size, number of possible
	 * repetitions are maintained in a hashmap
	 */

	private HashMap<String, Integer> groupingPossibilities;

	private List<SubjectGroup> subjectGroups = new ArrayList<SubjectGroup>();

	public PerfectStrangerGroupMatcher(List<String> roles) {
		super(roles);
		initHashMap();

	}

	@Override
	public List<SubjectGroup> setupSubjectGroups(Period period, List<Subject> subjects) throws Exception {

		int allowableMatches = groupingPossibilities.get(subjects.size() * roles.size() + "|" + roles.size());

		if (period.getIdPeriod() > allowableMatches) {
			throw new IllegalStateException("Number of allowable perfect stranger matches for " + subjects.size() + " and " + roles.size() + " roles is " + allowableMatches);
		} else {
			// divide into subgroups
			// put subjectsize/roleListsize into baskets of each role,
			// allowablematches number of times
			// i.e, create SubjectGroup allowableMatches number of times

			// divide subjects into roles

			ArrayList<ArrayList<Subject>> subjectsRoleList = new ArrayList<>();
			for (int n = 0; n < roles.size(); n++) {
				subjectsRoleList.add(new ArrayList<>());
			}
			for (Subject subject : subjects) {
				// define n subject role lists

				for (int n = 0; n < roles.size(); n++) {
					if (subject.getRole() != null && subject.getRole().equals(roles.get(n))) {
						subjectsRoleList.get(n).add(subject);
					}
				}
			}

			for (int n = 0; n < roles.size(); n++) {
				SubjectGroup subjectGroup = new SubjectGroup();
				for (int k = 0; k < subjectsRoleList.get(n).size(); k++) {
					Subject subject = subjectsRoleList.get(n).get(k);
					Membership m1 = new Membership();
					m1.setSubjectGroup(subjectGroup);
					m1.setSubject(subject);
					m1.setRole(subject.getRole());

					// add other members to the group
				}

				/*
				 * if (numberOfSubjectsRole1 > 1) { partner =
				 * subjectsRole2.get(randomNumbers.get(i)); } else { // there is
				 * only one partner = subjectsRole2.get(0); }
				 */
				/*
				 * Membership m2 = new Membership(); //
				 * m2.setRole(partner.getRole()); // m2.setSubject(partner);
				 * m2.setSubjectGroup(subjectGroup);
				 * subjectGroup.getMemberships().add(m1);
				 * subjectGroup.getMemberships().add(m2);
				 * subjectGroup.setPeriod(period);
				 */
				subjectGroups.add(subjectGroup);
			}

		}
		return subjectGroups;

	}

	@Override
	public List<SubjectGroup> rematch(Period period, List<Subject> subjects) throws Exception {
		return setupSubjectGroups(period, subjects);
	}

	private void initHashMap() {
		groupingPossibilities = new HashMap<>();

		for (int n = 4; n <= 22; n = n + 2) {
			groupingPossibilities.put(n + "|" + 2, n - 1);
		}

		groupingPossibilities.put(6 + "|" + 3, 1);
		groupingPossibilities.put(9 + "|" + 3, 4);
		groupingPossibilities.put(12 + "|" + 3, 3);
		groupingPossibilities.put(15 + "|" + 3, 4);
		groupingPossibilities.put(18 + "|" + 3, 5);
		groupingPossibilities.put(21 + "|" + 3, 7);
		groupingPossibilities.put(24 + "|" + 3, 7);
		groupingPossibilities.put(27 + "|" + 3, 8);
		groupingPossibilities.put(8 + "|" + 4, 1);
		groupingPossibilities.put(12 + "|" + 4, 1);
		groupingPossibilities.put(16 + "|" + 4, 5);
		groupingPossibilities.put(20 + "|" + 4, 4);
		groupingPossibilities.put(24 + "|" + 4, 5);
		groupingPossibilities.put(28 + "|" + 4, 5);
		groupingPossibilities.put(10 + "|" + 5, 1);
		groupingPossibilities.put(15 + "|" + 5, 1);
		groupingPossibilities.put(20 + "|" + 5, 1);
		groupingPossibilities.put(25 + "|" + 5, 3);
		groupingPossibilities.put(12 + "|" + 6, 1);
		groupingPossibilities.put(18 + "|" + 6, 1);
		groupingPossibilities.put(24 + "|" + 6, 1);
		groupingPossibilities.put(14 + "|" + 7, 1);
		groupingPossibilities.put(21 + "|" + 7, 1);
		groupingPossibilities.put(28 + "|" + 7, 1);
		groupingPossibilities.put(16 + "|" + 8, 1);
		groupingPossibilities.put(24 + "|" + 8, 1);
		groupingPossibilities.put(18 + "|" + 9, 1);
		groupingPossibilities.put(27 + "|" + 9, 1);
		groupingPossibilities.put(20 + "|" + 10, 1);
		groupingPossibilities.put(22 + "|" + 11, 1);
		groupingPossibilities.put(24 + "|" + 12, 1);
		groupingPossibilities.put(26 + "|" + 13, 1);
		groupingPossibilities.put(28 + "|" + 14, 1);

	}

}
