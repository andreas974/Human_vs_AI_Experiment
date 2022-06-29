package edu.kit.exp.server.run;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import edu.kit.exp.common.LogHandler;
import edu.kit.exp.server.gui.structuretab.StructureTabController;
import edu.kit.exp.server.gui.treatment.TreatmentDrawType;
import edu.kit.exp.server.jpa.entity.*;
import edu.kit.exp.server.microeconomicsystem.Environment;
import edu.kit.exp.server.microeconomicsystem.EnvironmentFactory;
import edu.kit.exp.server.microeconomicsystem.RoleMatcher;
import edu.kit.exp.server.microeconomicsystem.SubjectGroupMatcher;
import edu.kit.exp.server.structure.PeriodManagement;
import edu.kit.exp.server.structure.SequenceElementManagement;
import edu.kit.exp.server.structure.SessionManagement;
import edu.kit.exp.server.structure.StructureManagementException;
import edu.kit.exp.server.structure.SubjectGroupManagement;
import edu.kit.exp.server.structure.SubjectManagement;

/**
 * This class is used in order to run a session. It provides methods to check
 * and complete the structure of a session.
 * 
 */
public class SessionCreator {

	/** The instance. */
	private static SessionCreator instance;

	/** The subject group management. */
	private SubjectGroupManagement subjectGroupManagement = SubjectGroupManagement.getInstance();

	/** The subject management. */
	private SubjectManagement subjectManagement = SubjectManagement.getInstance();

	/** The session management. */
	private SessionManagement sessionManagement = SessionManagement.getInstance();

	/** The sequence element management. */
	private SequenceElementManagement sequenceElementManagement = SequenceElementManagement.getInstance();

	/** The period management. */
	private PeriodManagement periodManagement = PeriodManagement.getInstance();

	/** The random number generator. */
	private RandomNumberGenerator randomNumberGenerator = RandomNumberGenerator.getInstance();

	/** The sequence element vector. */
	private Vector<SequenceElement> sequenceElementVector;

	/** The period vector. */
	private Vector<Period> periodVector;

	/** The treatment block counter. */
	private int treatmentBlockCounter = 0;
	StructureTabController guiController = StructureTabController.getInstance();

	/**
	 * Gets the single instance of SessionCreator.
	 * 
	 * @return single instance of SessionCreator
	 */
	public static SessionCreator getInstance() {

		if (instance == null) {
			instance = new SessionCreator();
		}

		return instance;
	}

	/**
	 * This constructor instantiates a new session creator.
	 */
	private SessionCreator() {

	}

	/**
	 * This method checks if the given session is ready to be run.It checks if
	 * the necessary precondictions are met for the session to be run.</br> It
	 * checks for example if all treatment blocks contain min 1 session and 1
	 * treatment.
	 * 
	 * @param session
	 *            A {@link edu.kit.exp.server.jpa.entity.Session Session}
	 *            variable which contains a given session.
	 * 
	 * @throws SessionRunException
	 *             If the session configuration is incomplete.
	 */
	public void checkIfRunConditionsMet(Session session) throws SessionRunException {

        List<Cohort> cohortList = session.getCohorts();
        List<SequenceElement> sequenceElementList = session.getSequenceElements();

        int treatmentBlockCounter = 0;

        // Check Cohorts
        if (cohortList.size() < 1) {
            throw new SessionRunException("No cohorts defined for session! " + session.toString());
        }

        // Check TreatmentBlocks and SequenceElements
        if (sequenceElementList.size() < 1) {
            throw new SessionRunException("No Sequence Elements defined for session! " + session.toString());
        }

        for (int i = 0; i < sequenceElementList.size(); i++) {

            SequenceElement sequenceElement = sequenceElementList.get(i);

            // Treatment block
            if (sequenceElement instanceof TreatmentBlock) {

                treatmentBlockCounter++;
                TreatmentBlock treatmentBlock = (TreatmentBlock)sequenceElement;

                // Check if treatment is set
                if (treatmentBlock.getTreatments().isEmpty()) {
                    throw new SessionRunException("No Treatment defined for TreatmentBlock: " + treatmentBlock.toString());
                }

                // Check if treatment ids are set correctly
                for (Treatment treatment: treatmentBlock.getTreatments()) {
                    try {
                        Class.forName(treatment.getEnvironmentFactoryKey());
                    } catch (ClassNotFoundException e) {
                        throw new SessionRunException("Environment factory key is not correct: " + treatment.getEnvironmentFactoryKey());
                    }
                    try {
                        Class.forName(treatment.getInstitutionFactoryKey());
                    } catch (ClassNotFoundException e) {
                        throw new SessionRunException("Institution factory key is not correct: " + treatment.getInstitutionFactoryKey());
                    }
                }

                // Check if periods are defined
                if (treatmentBlock.getPeriods().size() < 1) {
                    throw new SessionRunException("No Periods defined for TreatmentBlock: " + treatmentBlock.toString());
                }
            }

            // Quiz
            if (sequenceElement instanceof Quiz) {

                Quiz quiz = (Quiz) sequenceElement;

                // Check if screen id is empty
                if (quiz.getQuizFactoryKey() == null || quiz.getQuizFactoryKey().equals("")) {
                    throw new SessionRunException("No quiz factory key defined: " + quiz.toString());
                }

                // Check if screen id is set correctly
                try {
                    Class.forName(quiz.getQuizFactoryKey());
                } catch (ClassNotFoundException e) {
                    throw new SessionRunException("Quiz factory key is not correct: " + quiz.getQuizFactoryKey());
                }
            }

			// Questionnaire
			if (sequenceElement instanceof Questionnaire) {

				Questionnaire questionnaire = (Questionnaire) sequenceElement;

				// Check if screen id is empty
				if (questionnaire.getQuestionnaireFactoryKey() == null || questionnaire.getQuestionnaireFactoryKey().equals("")) {
					throw new SessionRunException("No Questionnaire factory key defined: " + questionnaire.toString());
				}

				// Check if screen id is set correctly
				try {
					Class.forName(questionnaire.getQuestionnaireFactoryKey());
				} catch (ClassNotFoundException e) {
					throw new SessionRunException("Questionnaire factory key is not correct: " + questionnaire.getQuestionnaireFactoryKey());
				}
			}
        }

        if (treatmentBlockCounter < 1) {
            throw new SessionRunException("No TreatmentBlocks defined for Session: " + session.toString());
        }
	}

	/**
	 * This method creates the Subjects for each cohort according to the size
	 * and number of cohorts.
	 * 
	 * @param session
	 *            A {@link edu.kit.exp.server.jpa.entity.Session Session}
	 *            variable which contains a given session.
	 * 
	 * @return the session
	 * 
	 * @throws SessionRunException
	 *             If the subjects can not be created.
	 */
	public Session createSubjects(Session session) throws SessionRunException {

		try {

			for (Cohort cohort : session.getCohorts()) {

				List<Subject> subjects = cohort.getSubjects();

				for (Subject subject : subjects) {
					subjectManagement.deleteSubject(subject);
				}

				subjectManagement.createNewSubjects(cohort, cohort.getSize());
				session = sessionManagement.findSession(session.getIdSession());
			}

		} catch (StructureManagementException e) {
			throw new SessionRunException("Can't create Subjects! " + e.getMessage());
		}

		return session;
	}

	/**
	 * For every treatment block in a session the periods are automatically
	 * matched to treatments.</br> If there is more than one treatment for a
	 * block with multiple periods, the treatments are assigned through a
	 * randomization procedure.
	 * 
	 * @param session
	 *            A {@link edu.kit.exp.server.jpa.entity.Session Session}
	 *            variable which contains a given session.
	 * 
	 * @return the session
	 * 
	 * @throws SessionRunException
	 *             If the periods can not be matched to treatments
	 */
	public Session matchPeriodsToTreatments(Session session) throws SessionRunException {

		sequenceElementVector = new Vector<SequenceElement>(session.getSequenceElements());

		try {

			for (SequenceElement sequenceElement : sequenceElementVector) {

				// Possible: Cross-Over-Design, Between-Subject-Design or
				// Within-Subject-Design
				if (sequenceElement.getClass().equals(TreatmentBlock.class)) {

					TreatmentBlock treatmentBlock = (TreatmentBlock) sequenceElement;

					if (treatmentBlock.getRandomization() == null && treatmentBlock.getTreatments().size() == 1) {

						periodVector = new Vector<Period>(treatmentBlock.getPeriods());

						for (Period period : periodVector) {
							period.setTreatment(treatmentBlock.getTreatments().get(0));
							periodManagement.updatePeriod(period);
						}
					}

					// Check for illegal state
					if (treatmentBlock.getRandomization() == null && treatmentBlock.getTreatments().size() > 1) {
						throw new SessionRunException("Can not match periods to treatments. TreatmentBlock has more than 1 Treatment but there is no randomization procedure defined!");
					}

					// Check for illegal state
					if (treatmentBlock.getRandomization() != null && treatmentBlock.getTreatments().size() == 1) {
						throw new SessionRunException("Can not match periods to treatments randomly. TreatmentBlock has only 1 Treatment!");
					}

					// Possible: Completely-Randomized-Design or
					// Factorial-Design
					if (treatmentBlock.getRandomization() != null && treatmentBlock.getTreatments().size() > 1) {

						if (treatmentBlock.getRandomization().equals(TreatmentDrawType.DRAW_WITH_REPLACEMENT.toString())) {
							matchPeriodsToTreatmentsCR(treatmentBlock);
						}

						if (treatmentBlock.getRandomization().equals(TreatmentDrawType.DRAW_WITHOUT_REPLACEMENT.toString())) {
							matchPeriodsToTreatmentsFactorial(treatmentBlock);
						}
					}

					// sequence element is not a treatment block
				} else {
					continue;
				}
			}

			session = sessionManagement.findSession(session.getIdSession());

		} catch (StructureManagementException e) {
			throw new SessionRunException("Can not match periods to treatments. " + e.getMessage());
		}

		return session;
	}

	/**
	 * This method matches the periods of a block to its defined
	 * treatments.</br> The matching corresponds to a draw from a bowl with all
	 * possible treatments with replacement.
	 * 
	 * @param treatmentBlock
	 *            The {edu.kit.exp.server.jpa.entity.TreatmentBlock
	 *            TreatmentBlock} whose periods need to be matched.
	 * 
	 * @throws StructureManagementException
	 *             If the selected Period was not found.
	 * @throws SessionRunException
	 *             If the periods can not be matched to treatments
	 */
	private void matchPeriodsToTreatmentsCR(TreatmentBlock treatmentBlock) throws StructureManagementException, SessionRunException {

		int num = treatmentBlock.getPeriods().size();
		int max = treatmentBlock.getTreatments().size() - 1;
		int min = 0;

		ArrayList<Integer> randomNumbers;
		List<Treatment> treatments = treatmentBlock.getTreatments();

		try {
			randomNumbers = randomNumberGenerator.generateRepeatingIntegers(num, min, max, 10);
		} catch (RandomGeneratorException e) {
			throw new SessionRunException("Can not match periods to treatments. " + e.getMessage());
		}

		periodVector = new Vector<Period>(treatmentBlock.getPeriods());
		Period period = null;
		int randomTreatmentNumber;

		for (int index = 0; index < periodVector.size(); index++) {

			period = periodVector.get(index);
			randomTreatmentNumber = randomNumbers.get(index);
			period.setTreatment(treatments.get(randomTreatmentNumber));
			Period result = periodManagement.updatePeriod(period);
			System.out.println("bam..." + result.getTreatment().toString());
		}
	}

	/**
	 * This method matches the periods of a block to its defined
	 * treatments.</br> The matching corresponds to a draw from a bowl with all
	 * possible treatments and their copies without replacement.
	 * 
	 * @param treatmentBlock
	 *            The {edu.kit.exp.server.jpa.entity.TreatmentBlock
	 *            TreatmentBlock} whose periods need to be matched.
	 * 
	 * @throws SessionRunException
	 *             If the number of periods is not a multiple of the number of
	 *             treatments.
	 * @throws StructureManagementException
	 *             If the selected Period was not found.
	 */
	private void matchPeriodsToTreatmentsFactorial(TreatmentBlock treatmentBlock) throws SessionRunException, StructureManagementException {

		int numberOfPeriods = treatmentBlock.getPeriods().size();
		int max = numberOfPeriods - 1;
		int min = 0;
		int numberOfTreatments = treatmentBlock.getTreatments().size();

		// Check for illegal state
		int rest = numberOfPeriods % numberOfTreatments;
		if (rest != 0) {
			throw new SessionRunException("Can not match periods to treatments. Number of periods has to be a multiple of the number of treatments! Caused by:" + treatmentBlock.getName() + ", Periods: " + treatmentBlock.getPeriods().size());
		}

		int periodsPerTreatment = numberOfPeriods / numberOfTreatments;

		ArrayList<Object> pot = new ArrayList<Object>();
		List<Treatment> treatments = treatmentBlock.getTreatments();

		// Add treatment copies to pot
		for (Treatment treatment : treatments) {

			for (int i = 0; i < periodsPerTreatment; i++) {
				pot.add(treatment);
			}
		}

		ArrayList<Integer> randomNumbers;

		try {
			randomNumbers = randomNumberGenerator.generateNonRepeatingIntegers(min, max);
		} catch (RandomGeneratorException e) {
			throw new SessionRunException("Can not match periods to treatments. " + e.getMessage());
		}

		periodVector = new Vector<Period>(treatmentBlock.getPeriods());
		Period period = null;
		int treatmentCopyNumber;

		for (int index = 0; index < periodVector.size(); index++) {

			period = periodVector.get(index);
			treatmentCopyNumber = randomNumbers.get(index);
			period.setTreatment((Treatment) pot.get(treatmentCopyNumber));
			@SuppressWarnings("unused")
			Period result = periodManagement.updatePeriod(period);
		}
	}

	/**
	 * This method takes the cohorts of a session and defines which subject of a
	 * cohort interact in a certain period (SubjectGroup creation).
	 * 
	 * @param session
	 *            A {@link edu.kit.exp.server.jpa.entity.Session Session}
	 *            variable which contains a given session.
	 * 
	 * @return the session
	 * 
	 * @throws ExistingDataException
	 *             If there is already existing subject group data.
	 * @throws SessionRunException
	 *             If there is an error during the subject group creation.
	 */
	public Session defineSubjectGroups(Session session) throws ExistingDataException, SessionRunException {

		try {
			deleteOldSubjectGroups(session);
		} catch (StructureManagementException e1) {
			throw new SessionRunException("Can not delete old subject groups. " + e1.getCause());
		}

		sequenceElementVector = new Vector<SequenceElement>(session.getSequenceElements());

		Environment environment;
		RoleMatcher roleMatcher = null;
		SubjectGroupMatcher subjectGroupMatcher = null;

		for (Cohort cohort : session.getCohorts()) {

			treatmentBlockCounter = 0;

			try {

				for (SequenceElement sequenceElement : sequenceElementVector) {

					if (sequenceElement.getClass().equals(TreatmentBlock.class)) {

						treatmentBlockCounter++;
						TreatmentBlock treatmentBlock = (TreatmentBlock) sequenceElement;

						periodVector = new Vector<>(treatmentBlock.getPeriods());
						// Random matching can destroy order!
						Collections.sort(periodVector);

						@SuppressWarnings("unused")
						Treatment treatment;
						List<Subject> subjectList;
						List<Subject> matchedSubjects;
						List<SubjectGroup> resultList = null;

						for (int i = 0; i < periodVector.size(); i++) {
							resultList = null;
							Period period = periodVector.get(i);

							treatment = period.getTreatment();

							environment = EnvironmentFactory.createEnvironment(period.getTreatment().getEnvironmentFactoryKey());

							if ((period.getSequenceNumber() == 1 && treatmentBlockCounter == 1) || (environment.getResetMatchersAfterTreatmentBlocks())) {
								roleMatcher = environment.getRoleMatcher();
								subjectGroupMatcher = environment.getSubjectGroupMatcher();
							}

							subjectList = cohort.getSubjects();

							// setup only at period 1 in treatmentBlock 1 Or
							// every period 1 if flag is set
							if ((period.getSequenceNumber() == 1 && treatmentBlockCounter == 1) || (period.getSequenceNumber() == 1 && environment.getResetMatchersAfterTreatmentBlocks())) {
								matchedSubjects = roleMatcher.setupSubjectRoles(subjectList);
								resultList = subjectGroupMatcher.setupSubjectGroups(period, matchedSubjects);
							} else {
								matchedSubjects = roleMatcher.rematch(period, subjectList);
								resultList = subjectGroupMatcher.rematch(period, matchedSubjects);
							}
							if (resultList != null) {
								for (SubjectGroup subjectGroup : resultList) {
									subjectGroupManagement.createNewSubjectGroup(subjectGroup);

								}
							}
						}
					}
				}

			} catch (Exception e) {
				LogHandler.printException(e, "Error during SubjectGroup definition! Check the factory keys!");
				throw new SessionRunException("Error during SubjectGroup definition! Check the factory keys! " + e.getMessage());
			}
		}

		return session;

	}

	/**
	 * This method deletes old subjects of a session that should be
	 * reinitialized.
	 * 
	 * @param session
	 *            A {@link edu.kit.exp.server.jpa.entity.Session Session}
	 *            variable which contains a given session.
	 * 
	 * @throws StructureManagementException
	 *             If the SubjectGroup could not be found.
	 */
	private void deleteOldSubjectGroups(Session session) throws StructureManagementException {

		Vector<SequenceElement> sequenceElementVector = new Vector<SequenceElement>(session.getSequenceElements());
		Collections.sort(sequenceElementVector);
		Vector<Period> periodVector;

		for (SequenceElement sequenceElement : sequenceElementVector) {

			if (sequenceElement.getClass().equals(TreatmentBlock.class)) {

				TreatmentBlock treatmentBlock = (TreatmentBlock) sequenceElement;

				periodVector = new Vector<>(treatmentBlock.getPeriods());

				for (Period period : periodVector) {

					Vector<SubjectGroup> subjectGroupVector = new Vector<>(period.getSubjectGroups());

					for (SubjectGroup subjectGroup : subjectGroupVector) {
						subjectGroupManagement.deleteSubjectGroup(subjectGroup.getIdSubjectGroup());
					}

				}
			}
		}

	}

	/**
	 * This method resets all doneFlags! In case of a session reset all done
	 * flags have to be reseted.
	 * 
	 * @param session
	 *            A {@link edu.kit.exp.server.jpa.entity.Session Session}
	 *            variable which contains a given session.
	 * 
	 * @return the session witch the done flags reset
	 * 
	 * @throws StructureManagementException
	 *             If the Session, SequenceElement or Period could not be found.
	 */
	public Session resetTreatmentElementFlags(Session session) throws StructureManagementException {

		Vector<SequenceElement> sequenceElementVector = new Vector<>(session.getSequenceElements());
		Collections.sort(sequenceElementVector);
		Vector<Period> periodVector;
		@SuppressWarnings("unused")
		SequenceElement result = null;

		for (SequenceElement sequenceElement : sequenceElementVector) {
			sequenceElement.setDone(false);
			result = sequenceElementManagement.updateSequenceElement(sequenceElement);

			if (sequenceElement.getClass().equals(TreatmentBlock.class)) {

				TreatmentBlock treatmentBlock = (TreatmentBlock) sequenceElement;

				periodVector = new Vector<>(treatmentBlock.getPeriods());

				for (Period period : periodVector) {
					period.setDone(false);
					periodManagement.updatePeriod(period);
				}
			}

		}

		session = sessionManagement.findSession(session.getIdSession());

		return session;
	}
}
