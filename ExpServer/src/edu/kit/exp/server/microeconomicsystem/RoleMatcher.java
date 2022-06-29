package edu.kit.exp.server.microeconomicsystem;

import java.util.List;

import edu.kit.exp.server.jpa.entity.Period;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.run.RandomGeneratorException;

/**
 * The RoleMatcher matches roles to subjects and has to be defined in every
 * Environment. If a Session is run, <code>setupSubjectRoles</code> is called
 * for Period 1 in the first TreatmentBlock and for all following periods and
 * TreatmentBlocks <code>rematch</code> is called. If
 * Environment.getResetMatchersAfterTreatmentBlocks == true
 * <code>setupSubjectRoles</code> will be called in Period 1 of each
 * TreatmentBlock.
 * 
 */
public abstract class RoleMatcher {

	/** The roles. */
	protected List<String> roles;

	/**
	 * This method gets the roles.
	 * 
	 * @return the roles
	 */
	public List<String> getRoles() {
		return roles;
	}

	/**
	 * This method sets the roles.
	 * 
	 * @param roles
	 *            A String list which contains the new roles.
	 */
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	/**
	 * This constructor instantiates a new role matcher.
	 * 
	 * @param roles
	 *            A String list which contains the new roles.
	 */
	public RoleMatcher(List<String> roles) {

		this.roles = roles;
	}

	/**
	 * This method creates the initial mapping of subjects and roles. The
	 * Attribute <code>subject.setRole(String)</code> has to be set here.
	 * 
	 * @param subjects
	 *            A Subjects list of a cohort. Those subjects will be mapped.
	 * 
	 * @return a list of mapped subjects
	 * 
	 * @throws RandomGeneratorException
	 *             If something went wrong during the random number creating
	 *             process.
	 * 
	 * @see edu.kit.exp.server.run.RandomNumberGenerator
	 */
	public abstract List<Subject> setupSubjectRoles(List<Subject> subjects) throws RandomGeneratorException;

	/**
	 * This method reallocates the roles for a given period.
	 * 
	 * @param period
	 *            The {@link edu.kit.exp.server.jpa.entity.Period Period} the
	 *            roles will be reallocated for.
	 * @param subjects
	 *            A Subjects list of a cohort. Those subjects will be
	 *            reallocated.
	 * 
	 * @return a list of mapped subjects
	 * 
	 * @throws RandomGeneratorException
	 *             If something went wrong during the random number creating
	 *             process.
	 * 
	 * @see edu.kit.exp.server.run.RandomNumberGenerator
	 */
	public abstract List<Subject> rematch(Period period, List<Subject> subjects) throws RandomGeneratorException;

}
