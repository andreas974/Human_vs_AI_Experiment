package edu.kit.exp.server.microeconomicsystem;

import java.util.ArrayList;
import java.util.List;

import edu.kit.exp.server.jpa.entity.Period;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.microeconomicsystem.RoleMatcher;
import edu.kit.exp.server.run.RandomGeneratorException;
import edu.kit.exp.server.run.RandomNumberGenerator;

/**
 * This class matches the 2 given roles (requester, responder) in an ultimatum
 * game to subjects randomly. The roles will be switched every period.
 * 
 * @see RoleMatcher
 */
public class PartnerRoleMatcher extends RoleMatcher {

	/** The random number generator. */
	private RandomNumberGenerator randomNumberGenerator = RandomNumberGenerator.getInstance();

	/**
	 * This constructor instantiates a new role matcher.
	 * 
	 * @param roles
	 *            A String list which contains the roles.
	 */
	public PartnerRoleMatcher(List<String> roles) {
		super(roles);

	}

	/**
	 * This method matches the 2 given roles to subjects randomly.
	 * 
	 * @param subjects
	 *            A Subject list which contains the <code>Subjects</code> to be
	 *            matched.
	 * 
	 * @return a list which contains subjects matched to roles
	 * 
	 * @throws RandomGeneratorException
	 *             If there was an error during the random number generation
	 *             process.
	 */
	@Override
	public List<Subject> setupSubjectRoles(List<Subject> subjects) throws RandomGeneratorException {

		int numberOfSubjects = subjects.size();
		int max = numberOfSubjects - 1;
		int min = 0;
		int numberOfRoles = getRoles().size();

		int copiesPerRole = numberOfSubjects / numberOfRoles;

		ArrayList<String> bowle = new ArrayList<String>();

		// Add roles to bowle
		for (String role : getRoles()) {

			for (int i = 0; i < copiesPerRole; i++) {
				bowle.add(role);
			}
		}

		ArrayList<Integer> randomNumbers = randomNumberGenerator.generateNonRepeatingIntegers(min, max);

		Subject subject = null;
		int roleCopyNumber;

		for (int index = 0; index < subjects.size(); index++) {

			subject = subjects.get(index);
			roleCopyNumber = randomNumbers.get(index);
			subject.setRole(bowle.get(roleCopyNumber));
		}

		return subjects;

	}

	/**
	 * This method switches the roles of all subjects.
	 * 
	 * @param period
	 *            The Period within which the roles should be switched.
	 * @param subjects
	 *            A List of Subjects to be switched.
	 * 
	 * @return the list of subjects with switched roles
	 * 
	 * @throws RandomGeneratorException
	 *             If there was an error during the random number generation
	 *             process.
	 */
	@Override
	public List<Subject> rematch(Period period, List<Subject> subjects) throws RandomGeneratorException {

		for (Subject subject : subjects) {

			if (subject.getRole().equals(getRoles().get(0))) {
				subject.setRole(getRoles().get(1));
			} else {
				subject.setRole(getRoles().get(0));
			}
		}

		return subjects;
	}

}
