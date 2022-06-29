package edu.kit.exp.server.microeconomicsystem;

import java.util.List;

import edu.kit.exp.server.jpa.entity.Period;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.jpa.entity.SubjectGroup;

/**
 * The SubjectGroupMatcher matches subjects with roles to SubjectGroups and has
 * to be defined in every Environment. If a Session is run,
 * <code>setupSubjectGroups</code> is called for Period 1 in the first
 * TreatmentBlock and for all following periods and TreatmentBlocks
 * <code>rematch</code> is called. If
 * environment.getResetMatchersAfterTreatmentBlocks == true
 * <code>setupSubjectGroups</code> will be called in Period 1 of each
 * TreatmentBlock.
 * 
 */
public abstract class SubjectGroupMatcher {

	/** The roles. */
	protected List<String> roles;

	/**
	 * This method creates a SubjectGroupMatcher that matches Subjects
	 * independent of roles.
	 * 
	 * @param roles
	 *            A String list which contains the roles.
	 */
	public SubjectGroupMatcher(List<String> roles) {
		this.roles = roles;
	}

	/**
	 * Creates the initial mapping of subjects with roles to SubjectGroups. The
	 * Attribute
	 * <code>{@link edu.kit.exp.server.jpa.entity.Subject Subject}.setRole(String)</code>
	 * has to be set via a RoleMatcher first.
	 * 
	 * @param period
	 *            The Period the matching should be done for.
	 * @param subjects
	 *            A list of Subjects of a Cohort. They are the subjects to be
	 *            matched.
	 * 
	 * @return the list of SubjectGroups for the first Period
	 * 
	 * @throws Exception
	 */
	public abstract List<SubjectGroup> setupSubjectGroups(Period period, List<Subject> subjects) throws Exception;

	/**
	 * This method reallocates (or not) the Subjects in each period.
	 * 
	 * @param period
	 *            The Period the reallocation should be done for.
	 * @param subjects
	 *            A list of Subjects of a Cohort. They are the subjects to be
	 *            reallocated.
	 * 
	 * @return the list of SubjectGroups after the rematching
	 * 
	 * @throws Exception
	 */
	public abstract List<SubjectGroup> rematch(Period period, List<Subject> subjects) throws Exception;

}
