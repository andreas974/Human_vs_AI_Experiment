package edu.kit.exp.server.structure;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.dao.DefaultDAO;
import edu.kit.exp.server.jpa.entity.Cohort;
import edu.kit.exp.server.jpa.entity.Session;

import java.util.List;

/**
 * This class provides all persistence functions of cohorts. A Cohort contains
 * one or multiple Subjects and belongs to one Session.
 * 
 */
public class CohortManagement {

	/** The instance. */
	private static CohortManagement instance;

	/** The cohort dao. */
	private DefaultDAO<Cohort> cohortDAO = new DefaultDAO<>(Cohort.class);

	/**
	 * This method returns an instance of the CohortManagement.
	 * 
	 * @return a single instance of CohortManagement
	 */
	public static CohortManagement getInstance() {

		if (instance == null) {
			instance = new CohortManagement();
		}

		return instance;
	}

	/**
	 * This constructor instantiates a new cohort management.
	 */
	private CohortManagement() {

	}

	/**
	 * This method finds a Cohort in DB.
	 * 
	 * @param cohortId
	 *            An Integer which contains the cohort ID.
	 * 
	 * @return the cohort
	 * 
	 * @throws StructureManagementException
	 *             If the cohort could not be found.
	 */
	public Cohort findCohort(Integer cohortId) throws StructureManagementException {

		Cohort treatmentBlock;

		try {
			treatmentBlock = cohortDAO.findById(cohortId);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Cohort could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return treatmentBlock;
	}

	/**
	 * This method creates a new cohort for the given session.
	 * 
	 * @param session
	 *            A {@link edu.kit.exp.server.jpa.entity.Session Session} which
	 *            contains the given session.
	 * @param cohortSize
	 *            An Integer that indicates the size of the cohort.
	 * 
	 * @return the cohort
	 * 
	 * @throws StructureManagementException
	 *             If a cohort could not be created.
	 */
	public Cohort createNewCohort(Session session, Integer cohortSize) throws StructureManagementException {

		Cohort cohort = new Cohort();
		cohort.setSession(session);
		cohort.setSize(cohortSize);

		Cohort result;

		try {
			result = cohortDAO.create(cohort);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Cohort could not be created. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}

	/**
	 * This method gets all cohorts from the DB.
	 * 
	 * @return a list of cohorts
	 * 
	 * @throws StructureManagementException
	 *             If no cohorts could be found.
	 */
	public List<Cohort> findAllCohorts() throws StructureManagementException {

		List<Cohort> list;
		try {
			list = cohortDAO.findAll();
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Cohorts could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return list;
	}

	/**
	 * This method removes a cohort from the DB.
	 * 
	 * @param sequenceElement
	 *            the {@link edu.kit.exp.server.jpa.entity.Cohort Cohort} to be
	 *            removed
	 * 
	 * @throws StructureManagementException
	 *             If the cohort was not found.
	 */
	public void deleteCohort(Cohort sequenceElement) throws StructureManagementException {

		try {
			cohortDAO.delete(sequenceElement);
		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Cohort could not be found. Cause: " + e.getMessage());
			throw ex;
		}
	}

	/**
	 * This method updates a given cohort.
	 * 
	 * @param cohort
	 *            the {@link edu.kit.exp.server.jpa.entity.Cohort Cohort} to be
	 *            updated
	 * 
	 * @return the updated cohort
	 * 
	 * @throws StructureManagementException
	 *             If the cohort could not be removed.
	 */
	public Cohort updateCohort(Cohort cohort) throws StructureManagementException {

		Cohort result;
		try {
			result = cohortDAO.update(cohort);

		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Cohort could not be removed. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}

}
