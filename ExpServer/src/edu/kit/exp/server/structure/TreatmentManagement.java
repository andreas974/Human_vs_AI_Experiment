package edu.kit.exp.server.structure;

import java.util.List;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.dao.DefaultDAO;
import edu.kit.exp.server.jpa.entity.Treatment;

/**
 * This class provides all persistence functions of treatments.</br> A Treatment
 * can be assigned to multiple Periods and TreatmentBlocks.
 * 
 */
public class TreatmentManagement {

	/** The instance. */
	private static TreatmentManagement instance;

	/** The treatment dao. */
	private DefaultDAO<Treatment> treatmentDAO = new DefaultDAO<>(Treatment.class);

	/**
	 * This method returns an instance of the TreatmentManagement.
	 * 
	 * @return a single instance of TreatmentManagement
	 */
	public static TreatmentManagement getInstance() {

		if (instance == null) {
			instance = new TreatmentManagement();
		}

		return instance;
	}

	/**
	 * This constructor instantiates a new treatment management.
	 */
	private TreatmentManagement() {

	}

	/**
	 * This method finds a treatment in the DB.
	 * 
	 * @param treatmentId
	 *            An Integer variable which contains the ID of the Treatment.
	 * 
	 * @return the treatment
	 * 
	 * @throws StructureManagementException
	 *             If the Treatment could not be found.
	 */
	public Treatment findTreatment(Integer treatmentId) throws StructureManagementException {

		Treatment treatment;

		try {
			treatment = treatmentDAO.findById(treatmentId);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Treatment could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return treatment;
	}

	/**
	 * This method creates a new treatment in the DB.
	 * 
	 * @param treatment
	 *            The new Treatment to be created.
	 * 
	 * @return the treatment
	 * 
	 * @throws StructureManagementException
	 *             If the Treatment could not be created.
	 */
	public Treatment createNewTreatment(Treatment treatment) throws StructureManagementException {

		// Treatment t = new Treatment();
		// long number = new Date().getTime();
		//
		// t.setName("New Treatment");
		// t.setInstitutionFactoryKey("CHANGE THIS ENTRY! "+number);
		// t.setEnvironmentFactoryKey("CHANGE THIS ENTRY! "+number);

		Treatment result;

		try {
			result = treatmentDAO.create(treatment);
		} catch (DataManagementException e) {

			StructureManagementException ex = new StructureManagementException("Treatment could not be created. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}

	/**
	 * This method gets all treatments from the DB.
	 * 
	 * @return the list of all treatments
	 * 
	 * @throws StructureManagementException
	 *             If Treatments could not be found.
	 */
	public List<Treatment> findAllTreatments() throws StructureManagementException {

		List<Treatment> list;
		try {
			list = treatmentDAO.findAll();
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Treatments could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return list;
	}

	/**
	 * This method removes a treatment from the DB.
	 * 
	 * @param treatment
	 *            The Treatment to be removed.
	 * 
	 * @throws StructureManagementException
	 *             If the targeted Treatment could not be found.
	 */
	public void deleteTreatment(Treatment treatment) throws StructureManagementException {

		try {
			// treatment = treatmentDAO.findTreatmentById(idTreatment);
			treatmentDAO.delete(treatment);
		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Treatment could not be found. Cause: " + e.getMessage());
			throw ex;
		}
	}

	/**
	 * This method updates a given Treatment.
	 * 
	 * @param treatment
	 *            The Treatment to be updated.
	 * 
	 * @return the treatment
	 * 
	 * @throws StructureManagementException
	 *             If the given Treatment could not be found.
	 */
	public Treatment updateTreatment(Treatment treatment) throws StructureManagementException {

		Treatment result;
		try {
			result = treatmentDAO.update(treatment);

		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Treatments could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}
}
