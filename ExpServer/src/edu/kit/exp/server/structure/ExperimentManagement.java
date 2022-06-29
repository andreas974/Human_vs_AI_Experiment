package edu.kit.exp.server.structure;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.dao.DefaultDAO;
import edu.kit.exp.server.jpa.entity.Experiment;

import java.util.List;

/**
 * This class provides all persistence functions for experiments.</br> An
 * Experiment is a collection of observations in a controlled environment.
 * 
 */
public class ExperimentManagement {

	/** The instance. */
	private static ExperimentManagement instance;

	/** The experiment dao. */
	private DefaultDAO<Experiment> experimentDAO = new DefaultDAO<>(Experiment.class);

	/**
	 * This method returns an instance of the ExperimentManagement.
	 * 
	 * @return a single instance of ExperimentManagement
	 */
	public static ExperimentManagement getInstance() {

		if (instance == null) {
			instance = new ExperimentManagement();
		}

		return instance;
	}

	/**
	 * This constructor instantiates a new experiment management.
	 */
	private ExperimentManagement() {

	}

	/**
	 * This method finds an experiment in the DB with the experiment ID.
	 * 
	 * @param experimentId
	 *            An Integer which contains the ID of an experiment.
	 * 
	 * @return the experiment
	 * 
	 * @throws StructureManagementException
	 *             If the experiment could not be loaded.
	 */
	public Experiment findExperiment(Integer experimentId) throws StructureManagementException {

		Experiment experiment;

		try {
			experiment = experimentDAO.findById(experimentId);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Experiment could not be loaded. Cause:");
			throw ex;
		}

		return experiment;
	}


	/**
	 * This method creates a new experiment.
	 *
	 * @param name
	 *            A String which contains the name of the experiment to be
	 *            created.
	 * @param description
	 *            A String which contains a description for the new experiment.
	 *
	 * @return the newly created experiment
	 *
	 * @throws StructureManagementException
	 *             If an experiment could not be created.
	 */
	public Experiment createExperiment(String name, String description) throws StructureManagementException {

		Experiment experiment = new Experiment();
		experiment.setName(name);
		experiment.setDescription(description);

		Experiment result;

		try {
			result = experimentDAO.create(experiment);
		} catch (DataManagementException ex) {
			StructureManagementException e = new StructureManagementException("Experiment could not be created. Cause:");
			throw e;
		}

		SessionManagement.getInstance().createNewSession(experiment);

		return result;

	}

	/**
	 * This method gets all experiments from the DB.
	 * 
	 * @return a list of the experiments
	 * 
	 * @throws StructureManagementException
	 *             If no experiments could be found.
	 */
	public List<Experiment> findAllExperiments() throws StructureManagementException {

		List<Experiment> list;
		try {
			list = experimentDAO.findAll();
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Experiments could not be found. Cause:");
			throw ex;
		}

		return list;
	}

	/**
	 * This method deletes an experiment from the DB.
	 * 
	 * @param idExperiment
	 *            the Integer ID of the experiment to be deleted
	 * 
	 * @throws StructureManagementException
	 *             If the experiment could not be found.
	 */
	public void deleteExperiment(Integer idExperiment) throws StructureManagementException {

		Experiment exp;
		try {
			exp = experimentDAO.findById(idExperiment);
			experimentDAO.delete(exp);
		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Experiments could not be found. Cause:");
			throw ex;
		}
	}

	/**
	 * This method updates an experiment in the DB.
	 * 
	 * @param experiment
	 *            the experiment to be updated
	 * 
	 * @return the experiment after the update
	 * 
	 * @throws StructureManagementException
	 *             If no experiments could be found.
	 */
	public Experiment updateExperiment(Experiment experiment) throws StructureManagementException {

		Experiment exp;
		try {
			exp = experimentDAO.update(experiment);

		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Experiments could not be found. Cause:");
			throw ex;
		}

		return exp;

	}
}
