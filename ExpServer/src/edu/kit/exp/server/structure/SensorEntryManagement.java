package edu.kit.exp.server.structure;

import java.util.List;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.dao.DefaultDAO;
import edu.kit.exp.server.jpa.entity.Experiment;
import edu.kit.exp.server.jpa.entity.SensorEntry;

/**
 * Created by tondaroder on 06.07.16.
 */
public class SensorEntryManagement {
	/** The instance. */
	private static SensorEntryManagement instance;

	/** The sensor entry dao. */
	private DefaultDAO<SensorEntry> sensorEntryDAO = new DefaultDAO<>(SensorEntry.class);

	/**
	 * This method returns an instance of the SensorEntryManagement.
	 *
	 * @return a single instance of SenseorEntryManagement
	 */
	public static SensorEntryManagement getInstance() {

		if (instance == null) {
			instance = new SensorEntryManagement();
		}

		return instance;
	}

	/**
	 * This constructor instantiates a new sensor entry management.
	 */
	private SensorEntryManagement() {

	}

	/**
	 * This method finds a SensorEntry in the DB.
	 *
	 * @param name
	 *            name of the sensor entry
	 *
	 * @return the sensorEntry
	 *
	 * @throws StructureManagementException
	 *             If the SensorEntry could not be found.
	 */
	public SensorEntry findSensorEntry(String name) throws StructureManagementException {

		SensorEntry sensorEntry;

		try {
			sensorEntry = sensorEntryDAO.findById(null);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("SensorEntry could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return sensorEntry;
	}

	/**
	 * This method creates a new SensorEntry for the given Experiment.
	 *
	 * @param experiment
	 *            The Experiment where a new SensorEntry should be created.
	 *
	 * @return the sensorEntry
	 *
	 * @throws StructureManagementException
	 *             If the SensorEntry could not be created.
	 */
	public SensorEntry createNewSensorEntry(Experiment experiment, SensorEntry sensorEntry) throws StructureManagementException {

		sensorEntry.setExperiment(experiment);

		SensorEntry result;

		try {
			result = sensorEntryDAO.create(sensorEntry);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("SensorEntry could not be created. Cause: " + e.getMessage());
			e.printStackTrace();
			throw ex;
		}

		return result;

	}

	/**
	 * This method gets all sensorEntrys from the DB.
	 *
	 * @return A list of all the SensorEntrys.
	 *
	 * @throws StructureManagementException
	 *             If no SensorEntrys were found.
	 */
	public List<SensorEntry> findAllSensorEntrysByExperiment(Experiment experiment) throws StructureManagementException {

		List<SensorEntry> list;
		try {
			list = sensorEntryDAO.findAllBy("SensorEntry.findByExperiment","experiment",experiment);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("SensorEntrys could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return list;
	}

	/**
	 * This method removes a sensorEntry from the DB.
	 *
	 * @param name
	 *            The name of the SensorEntry to be removed.
	 *
	 * @throws StructureManagementException
	 *             If the SensorEntry could not be found.
	 */
	public void deleteSensorEntry(SensorEntry sensorEntry) throws StructureManagementException {

		try {
			SensorEntry result = sensorEntryDAO.findById(sensorEntry.getId());
			sensorEntryDAO.delete(result);
		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("SensorEntrys could not be found. Cause: " + e.getMessage());
			throw ex;
		}
	}

	/**
	 * This method updates a SensorEntry.
	 *
	 * @param sensorEntry
	 *            The SensorEntry to be updated.
	 *
	 * @return the updated SensorEntry
	 *
	 * @throws StructureManagementException
	 *             If the SensorEntry could not be updated.
	 */
	public SensorEntry updateSensorEntry(SensorEntry sensorEntry) throws StructureManagementException {

		SensorEntry result;

		try {
			result = sensorEntryDAO.update(sensorEntry);

		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("SensorEntrys could not be updated. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}
}
