package edu.kit.exp.server.gui.starttab;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import edu.kit.exp.server.gui.DataInputException;
import edu.kit.exp.server.gui.mainframe.MainFrameController;
import edu.kit.exp.server.gui.structuretab.StructureTabController;
import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.dao.DefaultDAO;
import edu.kit.exp.server.jpa.entity.Experiment;
import edu.kit.exp.server.structure.ExperimentManagement;
import edu.kit.exp.server.structure.StructureManagementException;

/**
 * This class represents the controller for the start tab.</br> It extends
 * Observable.
 * 
 * @see Observable
 */
public class StartTabController extends Observable {

	/** The instance. */
	private static StartTabController instance = new StartTabController();

	/** The experiment dao. */
	private DefaultDAO<Experiment> experimentDao = new DefaultDAO<>(Experiment.class);

	/** The experiment management. */
	private ExperimentManagement experimentManagement = ExperimentManagement.getInstance();

	/** The list of all experiments. */
	private List<Experiment> listOfAllExperiments;

	/**
	 * This constructor instantiates a new start tab controller.
	 */
	private StartTabController() {

	}

	/**
	 * This method returns the only instance of this class.
	 * 
	 * @return a single instance of StartTabController
	 */
	public static StartTabController getInstance() {

		return instance;
	}

	/**
	 * This method adds an observer to this controller.
	 * 
	 * @param o
	 *            the Observer
	 */
	@Override
	public void addObserver(Observer o) {
		super.addObserver(o);
	}

	/**
	 * This method allows the experimenter to open an experiment in order to
	 * show it in the experiment builder. This means that the GUI switches to
	 * the structure tab.
	 * 
	 * @param idExperiment
	 *            the ID of the experiment that should be opened
	 * @throws StructureManagementException
	 *             If an experiment could not be found.
	 * @throws DataInputException
	 *             If no experiment was selected.
	 */
	public void openExperiment(Integer idExperiment) throws StructureManagementException, DataInputException {

		if (idExperiment == null) {
			DataInputException e = new DataInputException("Please select an experiment.");
			throw e;
		}

		Experiment experiment = experimentManagement.findExperiment(idExperiment);
		StructureTabController.getInstance().setCurrentExperiment(experiment);
		MainFrameController.getInstance().switchToTab(1);

	}

	/**
	 * This method creates a new experiment.
	 * 
	 * @param name
	 *            the String name of the experiment
	 * @param description
	 *            the String description of the experiment
	 * @throws DataInputException
	 *             If no experiment name was entered.
	 * @throws StructureManagementException
	 *             If an experiment could not be created.
	 */
	public void createNewExperiment(String name, String description) throws DataInputException, StructureManagementException {

		if (name.equals("")) {
			DataInputException e = new DataInputException("Please enter an experiment name.");
			throw e;
		}

		Experiment result = experimentManagement.createExperiment(name, description);
		listOfAllExperiments.add(result);

		if (countObservers() > 0) {
			setChanged();
			notifyObservers(listOfAllExperiments);
		}
	}

	/**
	 * This method deletes an experiment.
	 * 
	 * @param idExperiment
	 *            the ID of the experiment
	 * @throws DataInputException
	 *             If no experiment was selected.
	 * @throws StructureManagementException
	 *             If no experiments could be found.
	 */
	public void deleteExperiment(Integer idExperiment) throws DataInputException, StructureManagementException {

		if (idExperiment == null) {
			DataInputException e = new DataInputException("Please select an experiment.");
			throw e;
		}

		experimentManagement.deleteExperiment(idExperiment);

		listOfAllExperiments = experimentManagement.findAllExperiments();

		if (countObservers() > 0) {
			setChanged();
			notifyObservers(listOfAllExperiments);
		}
	}

	/**
	 * This method returns a list of all experiments.
	 * 
	 * @return a list of all experiments
	 * @throws DataManagementException
	 *             If there is something wrong with the data management.
	 */
	public List<Experiment> getAllExperiments() throws DataManagementException {

		if (listOfAllExperiments == null) {
			listOfAllExperiments = experimentDao.findAll();
		}

		return listOfAllExperiments;
	}

}
