package edu.kit.exp.server.structure;

import java.util.ArrayList;
import java.util.List;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.dao.DefaultDAO;
import edu.kit.exp.server.jpa.entity.Period;
import edu.kit.exp.server.jpa.entity.TreatmentBlock;

/**
 * This class provides all persistence functions of periods.</br> A Period is a
 * time frame which contains trials.
 * 
 */
public class PeriodManagement {

	/** The instance. */
	private static PeriodManagement instance;

	/** The period dao. */
	private DefaultDAO<Period> periodDAO = new DefaultDAO<>(Period.class);

	/**
	 * This method returns an instance of the PeriodManagement.
	 * 
	 * @return a single instance of PeriodManagement
	 */
	public static PeriodManagement getInstance() {

		if (instance == null) {
			instance = new PeriodManagement();
		}

		return instance;
	}

	/**
	 * This constructor instantiates a new period management.
	 */
	private PeriodManagement() {

	}

	/**
	 * This method finds a period in DB.
	 * 
	 * @param periodId
	 *            the Integer ID of the Period to be found
	 * 
	 * @return the period
	 * 
	 * @throws StructureManagementException
	 *             If the Period could not be found.
	 */
	public Period findPeriod(Integer periodId) throws StructureManagementException {

		Period period;

		try {
			period = periodDAO.findById(periodId);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Period could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return period;
	}

	/**
	 * This method creates new periods for the given TreatmentBlock.
	 * 
	 * @param treatmentBlock
	 *            The specified TreatmentBlock where a new period will be
	 *            created.
	 * @param numberOfPeriods
	 *            the number of periods
	 * 
	 * @return a list of periods
	 * 
	 * @throws StructureManagementException
	 *             If a period could not be created.
	 */
	public List<Period> createNewPeriods(TreatmentBlock treatmentBlock, int numberOfPeriods) throws StructureManagementException {

		List<Period> result = new ArrayList<Period>();
		Integer startSequenceNumber = treatmentBlock.getPeriods().size() + 1;
		int endSequenceNumber = startSequenceNumber + numberOfPeriods;

		for (int i = startSequenceNumber; i < endSequenceNumber; i++) {

			Period period = new Period();
			period.setTreatmentBlock(treatmentBlock);
			period.setSequenceNumber(i);
			period.setPractice(treatmentBlock.getPractice());

			try {
				result.add(periodDAO.create(period));
			} catch (DataManagementException e) {
				StructureManagementException ex = new StructureManagementException("Period could not be created. Cause: " + e.getMessage());
				throw ex;
			}
		}

		return result;

	}

	/**
	 * This method gets all Periods from the DB.
	 * 
	 * @return a list of periods
	 * 
	 * @throws StructureManagementException
	 *             If no Periods could be found.
	 */
	public List<Period> findAllPeriods() throws StructureManagementException {

		List<Period> list;
		try {
			list = periodDAO.findAll();
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Periods could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return list;
	}

	/**
	 * This method removes a period from the DB.
	 * 
	 * @param idPeriod
	 *            The Integer ID of the period which will be removed.
	 * 
	 * @throws StructureManagementException
	 *             If the Period could not be deleted.
	 */
	public void deletePeriod(Period period) throws StructureManagementException {

		try {
			periodDAO.delete(period);
		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Period could not be deleted. Cause: " + e.getMessage());
			throw ex;
		}
	}

	/**
	 * This method updates a given period.
	 * 
	 * @param period
	 *            the Period to be updated
	 * 
	 * @return the updated period
	 * 
	 * @throws StructureManagementException
	 *             If the selected Period was not found.
	 */
	public Period updatePeriod(Period period) throws StructureManagementException {

		Period result;
		try {
			result = periodDAO.update(period);

		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Period could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}
}
