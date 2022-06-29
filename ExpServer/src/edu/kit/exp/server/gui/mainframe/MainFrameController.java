package edu.kit.exp.server.gui.mainframe;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import edu.kit.exp.common.LogHandler;
import edu.kit.exp.common.communication.ConnectionException;
import edu.kit.exp.server.gui.DataInputException;
import edu.kit.exp.server.gui.structuretab.StructureTabController;
import edu.kit.exp.server.jpa.entity.Cohort;
import edu.kit.exp.server.jpa.entity.Experiment;
import edu.kit.exp.server.jpa.entity.Session;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.run.ExistingDataException;
import edu.kit.exp.server.run.RunManager;
import edu.kit.exp.server.run.SessionCreator;
import edu.kit.exp.server.run.SessionDoneException;
import edu.kit.exp.server.run.SessionRunException;
import edu.kit.exp.server.structure.SessionManagement;
import edu.kit.exp.server.structure.StructureManagementException;

/**
 * MainFrame controller class
 * 
 */
public class MainFrameController {

	private static MainFrameController instance = new MainFrameController();

	private MainFrameController() {

	}

	/**
	 * Returns the only instance of this class.
	 * 
	 * @return instance
	 */
	public static MainFrameController getInstance() {

		return instance;
	}

	public void initApplication() {
		MainFrame.getInstance().setVisible(true);
	}

	public void switchToTab(int index) {
		MainFrame.getInstance().getTabbedPane().setSelectedIndex(index);
	}

	/**
	 * Checks if the given session is ready to be run.
	 * 
	 * @throws DataInputException
	 * @throws SessionRunException
	 * @throws StructureManagementException
	 */
	public void checkSessionConfiguration() throws DataInputException, SessionRunException, StructureManagementException {

		Experiment e = StructureTabController.getInstance().getCurrentExperiment();

		if (e != null) {
			Session selectedSession = StructureTabController.getInstance().getSelectedSession();
			SessionCreator.getInstance().checkIfRunConditionsMet(selectedSession);
		} else {
			throw new DataInputException("Please load an Experiment!");
		}
	}

	/**
	 * Starts the selected session!
	 *
	 * @throws DataInputException
	 * @throws SessionRunException
	 * @throws ConnectionException
	 * @throws ExistingDataException
	 * @throws StructureManagementException
	 * @throws SessionDoneException
	 */
	public void runSession() throws DataInputException, SessionRunException, ConnectionException, ExistingDataException, StructureManagementException, SessionDoneException {

        LogHandler.printInfo("Running the session!");

		Experiment e = StructureTabController.getInstance().getCurrentExperiment();

		if (e != null) {
			Session selectedSession = StructureTabController.getInstance().getSelectedSession();

			if (!selectedSession.getDone()) {
				RunManager.getInstance().initializeSession(selectedSession, false);
			} else {
				throw new SessionDoneException();
			}

		} else {
			throw new DataInputException("Please load an Experiment!");
		}

	}

	/**
	 * Reset selected Session.
	 * 
	 * @throws DataInputException
	 * @throws SessionRunException
	 * @throws ExistingDataException
	 * @throws ConnectionException
	 * @throws StructureManagementException
	 */
	public void resetSession() throws DataInputException, SessionRunException, ExistingDataException, ConnectionException, StructureManagementException {

	    LogHandler.printInfo("Resetting the session!");

		Experiment e = StructureTabController.getInstance().getCurrentExperiment();

		if (e != null) {
			Session selectedSession = StructureTabController.getInstance().getSelectedSession();
			Session updatedSession = SessionManagement.getInstance().findSession(selectedSession.getIdSession());
			updatedSession.setStarted(false);
			updatedSession.setDone(false);
			updatedSession = SessionManagement.getInstance().updateSession(updatedSession);

			RunManager.getInstance().initializeSession(updatedSession, false);

		} else {
			throw new DataInputException("Please load an Experiment!");
		}

	}

	/**
	 * Continue session.
	 * 
	 * @throws DataInputException
	 * @throws StructureManagementException
	 * @throws SessionRunException
	 * @throws ExistingDataException
	 * @throws ConnectionException
	 */
	public void continueSession() throws DataInputException, StructureManagementException, SessionRunException, ExistingDataException, ConnectionException {

        LogHandler.printInfo("Continuing the session");

		Experiment e = StructureTabController.getInstance().getCurrentExperiment();

		if (e != null) {

			Session selectedSession = StructureTabController.getInstance().getSelectedSession();
			Session updatedSession = SessionManagement.getInstance().findSession(selectedSession.getIdSession());
			RunManager.getInstance().initializeSession(updatedSession, true);

		} else {
			throw new DataInputException("Please load an Experiment!");
		}

	}

	/**
	 * Returns a list of all subjects of the selected session.
	 * 
	 * @return
	 * @throws DataInputException
	 * @throws StructureManagementException
	 */
	public List<Subject> getSubjects() throws DataInputException, StructureManagementException {

		Session selectedSession = StructureTabController.getInstance().getSelectedSession();
		Session updatedSession = SessionManagement.getInstance().findSession(selectedSession.getIdSession());
		Vector<Cohort> cohorts = new Vector<>(updatedSession.getCohorts());
		List<Subject> result = new ArrayList<>();

		for (Cohort cohort : cohorts) {
			result.addAll(cohort.getSubjects());
		}

		return result;

	}

	/**
	 * Opens the payOffDialog.
	 */
	public void openPayoffDialog() {

		PayoffDialog payoffDialog = new PayoffDialog();
		payoffDialog.setVisible(true);

	}

}
