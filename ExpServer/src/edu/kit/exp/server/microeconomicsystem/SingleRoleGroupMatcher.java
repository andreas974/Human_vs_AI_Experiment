/**
 * 
 */
package edu.kit.exp.server.microeconomicsystem;

import java.util.ArrayList;
import java.util.List;

import edu.kit.exp.server.jpa.entity.Membership;
import edu.kit.exp.server.jpa.entity.Period;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.jpa.entity.SubjectGroup;

/**
 * The class SingleRoleMatcher matches subjects with a single role to
 * SubjectGroups.
 * 
 * @see SubjectGroupMatcher
 * 
 */
public class SingleRoleGroupMatcher extends SubjectGroupMatcher {

	/** The subject group list. */
	private List<SubjectGroup> subjectGroupList = new ArrayList<>();

	/**
	 * This constructor instantiates a new single role matcher.
	 * 
	 * @param roles
	 *            A String list which contains the role.
	 */
	public SingleRoleGroupMatcher(List<String> roles) {
		super(roles);
	}

	@Override
	public List<SubjectGroup> rematch(Period period, List<Subject> subjects) {
		ArrayList<Subject> subjectsRole = new ArrayList<>();
		// Divide Subjects by Role
        subjectsRole.addAll(subjects);

        for (Subject aSubjectsRole : subjectsRole) {

            SubjectGroup subjectGroup = new SubjectGroup();

            Membership m1 = new Membership();
            m1.setSubjectGroup(subjectGroup);
            m1.setSubject(aSubjectsRole);
            if (aSubjectsRole.getRole() != null) {
                m1.setRole(aSubjectsRole.getRole());
            } else {
                m1.setRole(roles.get(0));
            }

            subjectGroup.getMemberships().add(m1);
            subjectGroup.setPeriod(period);
            subjectGroupList.remove(0);
            subjectGroupList.add(subjectGroup);
        }
		return subjectGroupList;
	}

	@Override
	public List<SubjectGroup> setupSubjectGroups(Period period, List<Subject> subjects) throws Exception {

		ArrayList<Subject> subjectsRole = new ArrayList<>();
		// Divide Subjects by Role
        subjectsRole.addAll(subjects);

        for (Subject aSubjectsRole : subjectsRole) {

            SubjectGroup subjectGroup = new SubjectGroup();

            Membership m1 = new Membership();
            m1.setSubjectGroup(subjectGroup);
            m1.setSubject(aSubjectsRole);
            if (aSubjectsRole.getRole() != null) {
                m1.setRole(aSubjectsRole.getRole());
            } else {
                m1.setRole(roles.get(0));
            }

            subjectGroup.getMemberships().add(m1);
            subjectGroup.setPeriod(period);
            subjectGroupList.add(subjectGroup);
        }

		return subjectGroupList;

	}

}
