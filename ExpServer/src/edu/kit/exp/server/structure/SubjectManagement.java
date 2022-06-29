package edu.kit.exp.server.structure;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.dao.DefaultDAO;
import edu.kit.exp.server.jpa.entity.Cohort;
import edu.kit.exp.server.jpa.entity.Subject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides all persistence functions of subjects.</br> A Subject
 * represents a client and can be associated with Trials and Memberships.
 * Furthermore, each Subject is assigned to a Cohort.
 * 
 */
public class SubjectManagement {

	/** The instance. */
	private static SubjectManagement instance;

	/** The subject dao. */
	private DefaultDAO<Subject> subjectDAO = new DefaultDAO<>(Subject.class);

	/**
	 * This method returns an instance of the SubjectManagement.
	 * 
	 * @return a single instance of SubjectManagement
	 */
	public static SubjectManagement getInstance() {

		if (instance == null) {
			instance = new SubjectManagement();
		}

		return instance;
	}

	/**
	 * This constructor instantiates a new subject management.
	 */
	private SubjectManagement() {

	}

	/**
	 * This method finds a Subject in the DB.
	 * 
	 * @param subjectId
	 *            The Integer ID of the Subject.
	 * 
	 * @return the subject
	 * 
	 * @throws StructureManagementException
	 *             If the Subject could not be found.
	 */
	public Subject findSubject(Integer subjectId) throws StructureManagementException {

		Subject treatmentBlock;

		try {
			treatmentBlock = subjectDAO.findById(subjectId);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Subject could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return treatmentBlock;
	}

	/**
	 * This method creates new Subjects for the given Cohort.
	 * 
	 * @param cohort
	 *            The Cohort where the Subjects will be allocated.
	 * @param number
	 *            The number of Subjects to be created.
	 * 
	 * @return the list of new subjects
	 * 
	 * @throws StructureManagementException
	 *             If the Subject could not be created.
	 */
	public List<Subject> createNewSubjects(Cohort cohort, int number) throws StructureManagementException {

		List<Subject> resultList = new ArrayList<Subject>();

		for (int i = 0; i < number; i++) {

			Subject subject = new Subject();
			subject.setCohort(cohort);

			Subject result;

			try {
				result = subjectDAO.create(subject);
			} catch (DataManagementException e) {
				StructureManagementException ex = new StructureManagementException("Subject could not be created. Cause: " + e.getMessage());
				throw ex;
			}
			resultList.add(result);
		}
		return resultList;
	}

	/**
	 * This method gets all Subjects from the DB.
	 * 
	 * @return the list of subjects
	 * 
	 * @throws StructureManagementException
	 *             If no Subjects could be found.
	 */
	public List<Subject> findAllSubjects() throws StructureManagementException {

		List<Subject> list;
		try {
			list = subjectDAO.findAll();
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Subjects could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return list;
	}

	/**
	 * This method removes a Subject from the DB.
	 * 
	 * @param subject
	 *            the Subject to be removed
	 * 
	 * @throws StructureManagementException
	 *             If the Subject could not be removed.
	 */
	public void deleteSubject(Subject subject) throws StructureManagementException {

		try {
			subjectDAO.delete(subject);
		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Subject could not be removed. Cause: " + e.getMessage());
			throw ex;
		}
	}

	/**
	 * This method deletes subjects from a cohort.
	 * 
	 * @param cohort
	 *            The Cohort where the subjects should be deleted.
	 * @param rem
	 *            A number which indicates how many subjects should be removed.
	 * 
	 * @throws StructureManagementException
	 *             If a Subject could not be removed.
	 */
	public void deleteSubjects(Cohort cohort, int rem) throws StructureManagementException {

		List<Subject> list = cohort.getSubjects();

		for (int i = 0; i < rem; i++) {
			try {
				subjectDAO.delete(list.get(i));
			} catch (Exception e) {
				StructureManagementException ex = new StructureManagementException("Subject could not be removed. Cause: " + e.getMessage());
				throw ex;
			}
		}

	}

	/**
	 * This method updates a given Subject.
	 * 
	 * @param subject
	 *            The Subject to be updated.
	 * 
	 * @return the updated subject
	 * 
	 * @throws StructureManagementException
	 *             If the Subject could not be found.
	 */
	public Subject updateSubject(Subject subject) throws StructureManagementException {

		Subject result;
		try {
			result = subjectDAO.update(subject);

		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Subject could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}

}
