package edu.kit.exp.server.structure;

import java.util.List;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.dao.DefaultDAO;
import edu.kit.exp.server.jpa.entity.Trial;
import edu.kit.exp.server.run.RunStateLogEntry;
import edu.kit.exp.server.run.RunStateLogger;

/**
 * This class provides all persistence functions of trials.</br> A Trial
 * corresponds to an action of a subject or a result (A price or an allocation).
 * Therefore, trials have to be assigned to a SubjectGroup.
 * 
 */
public class TrialManagement {

	/** The instance. */
	private static TrialManagement instance;

	/** The trial dao. */
	private DefaultDAO<Trial> trialDAO = new DefaultDAO<>(Trial.class);

	/**
	 * This method returns an instance of the TrialManagement.
	 * 
	 * @return a single instance of TrialManagement
	 */
	public static TrialManagement getInstance() {

		if (instance == null) {
			instance = new TrialManagement();
		}

		return instance;
	}

	/**
	 * This constructor instantiates a new trial management.
	 */
	private TrialManagement() {

	}

	/**
	 * This method finds a Trial in the DB.
	 * 
	 * @param trialId
	 *            The Long ID of a given Trial.
	 * 
	 * @return the trial
	 * 
	 * @throws StructureManagementException
	 *             If the Trial was not found.
	 */
	public Trial findTrial(Long trialId) throws StructureManagementException {

		Trial trial;

		try {
			trial = trialDAO.findById(trialId);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Trial could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return trial;
	}

	/**
	 * This method creates a new trial for the given Experiment.
	 * 
	 * @param trial
	 *            The new Trial to be created.
	 * 
	 * @return the trial
	 * 
	 * @throws StructureManagementException
	 *             If no Trial could be created.
	 */
	public Trial createNewTrial(Trial trial) throws StructureManagementException {

		Trial result;

		try {
			result = trialDAO.create(trial);
			
			if (trial.isPrintToServerTab()){
				RunStateLogger.getInstance().createOutputMessage(new RunStateLogEntry(trial.getSubject().getIdClient(), trial.getValueEvent(), trial.getValueName(), trial.getValue()));
			}
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Trial could not be created. Cause: " + e.getMessage());
			throw ex;
		}
	
		return result;

	}
	
	/**
	 * This method creates a new trial for the given Experiment,
	 * but valueName and value are set to the passed parameter
	 * before the trail is created. 
	 * 
	 * @param trial
	 *            The new Trial to be created.
	 * @param valueName
	 *            The new valueName of this trial
	 * @param value
	 *            The new value of this trial
	 * 
	 * @return the trial
	 * 
	 * @throws StructureManagementException
	 *             If no Trial could be created.
	 */
	public Trial createNewTrial(Trial trial, String valueName, String value) throws StructureManagementException{
		trial.setData(valueName, value);
		return createNewTrial(trial);
	}
	
	/**
	 * This method creates a new trial for the given Experiment,
	 * but valueEvent, valueName, and value are set to the 
	 * passed parameter before the trail is created. 
	 * 
	 * @param trial
	 *            The new Trial to be created.
	 * @param valueEvent
	 *            The new valueEvent of this trial
	 * @param valueName
	 *            The new valueName of this trial
	 * @param value
	 *            The new value of this trial
	 * 
	 * @return the trial
	 * 
	 * @throws StructureManagementException
	 *             If no Trial could be created.
	 */
	public Trial createNewTrial(Trial trial, String valueEvent, String valueName, String value) throws StructureManagementException{
		trial.setData(valueEvent, valueName, value);
		return createNewTrial(trial);
	}

	/**
	 * This method gets all trials from the DB.
	 * 
	 * @return a list of trials
	 * 
	 * @throws StructureManagementException
	 *             If no Trials were found.
	 */
	public List<Trial> findAllTrials() throws StructureManagementException {

		List<Trial> list;
		try {
			list = trialDAO.findAll();
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Trials could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return list;
	}

	/**
	 * This method removes a Trial from the DB.
	 * 
	 * @param idTrial
	 *            The Long Id of the Trial to be removed.
	 * 
	 * @throws StructureManagementException
	 *             If the Trial was not found.
	 */
	public void deleteTrial(Long idTrial) throws StructureManagementException {

		Trial trial;
		try {
			trial = trialDAO.findById(idTrial);
			trialDAO.delete(trial);
		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Trials could not be found. Cause: " + e.getMessage());
			throw ex;
		}
	}

	/**
	 * This method updates a given Trial.
	 * 
	 * @param trial
	 *            The Trial to be updated.
	 * 
	 * @return the updated trial
	 * 
	 * @throws StructureManagementException
	 *             If the Trial could not be updated.
	 */
	public Trial updateTrial(Trial trial) throws StructureManagementException {

		Trial result;

		try {
			result = trialDAO.update(trial);

		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Trials could not be updated. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}
}
