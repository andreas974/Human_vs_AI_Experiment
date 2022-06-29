package edu.kit.exp.server.gui.treatment;

import edu.kit.exp.common.LogHandler;
import edu.kit.exp.common.ReflectionPackageManager;
import edu.kit.exp.server.gui.DataInputException;
import edu.kit.exp.server.gui.structuretab.StructureTabController;
import edu.kit.exp.server.jpa.entity.Treatment;
import edu.kit.exp.server.microeconomicsystem.Environment;
import edu.kit.exp.server.microeconomicsystem.Institution;
import edu.kit.exp.server.structure.StructureManagementException;
import edu.kit.exp.server.structure.TreatmentManagement;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * This class is a controller for the treatment management dialog frame.</br>
 * It provides functionalities like creating, updating, adding treatments,...
 */
public class TreatmentManagementDialogController extends Observable {

		private static final String EMPTY = "";
		private static final String DOT = "\\.";
		private static final String[] ENVIRONMENT_EXPRESSIONS = new String[] { "Environment", "environment" };
		private static final String[] INSTITUTION_EXPRESSIONS = new String[] { "Institution", "institution" };
		private static final String NEW_TREATMENT_BUTTON_LABEL = "New Treatment";
		/**
		 * The path where the classes implementing the treatments are
		 */
		private static final String EXP_IMPLEMENTATION_PATH = "edu.kit.exp.impl";
		private static final String ERROR_IDENTIFIER = "treatment_institution_key_unique_index";
		private static final String ERROR_IDENTIFIER_TWO = "treatment_environment_key_unique_index";
		public static final String ERROR_MESSAGE = "The institution factory key already exists! Please choose another one.";
		private static final String ERROR_MESSAGE_TWO = "The environment factory key already exists! Please choose another one.";
		private static final String DATA_INPUT_EXCPETION_MESSAGE = "Please select a treatment.";
		private static final String CHANGE_TREATMENT_LABEL = "Change Treatment";
		private static final String SELECT_TREATMENT_EXCEPTION_MESSAGE = "Please select an treatment.";
		private static final String UNIQUE_CONSTRAINT_IDENTIFIER = "Unique-Constraint";
		private static final String ALREADY_ADDED_EXCEPTION_MESSAGE = "Treatment already added.";

		/**
		 * The instance.
		 */
		private static TreatmentManagementDialogController instance = new TreatmentManagementDialogController();

		/**
		 * The treatment management.
		 */
		private TreatmentManagement treatmentManagement = TreatmentManagement.getInstance();

		/**
		 * The list of all treatments.
		 */
		private List<Treatment> listOfAllTreatments;

		/**
		 * This constructor instantiates a new treatment management dialog
		 * controller.
		 */
		private TreatmentManagementDialogController() {

		}

		/**
		 * This method returns the only instance of this class.
		 *
		 * @return a single instance of TreatmentManagementDialogController
		 */
		public static TreatmentManagementDialogController getInstance() {

				return instance;
		}

		/**
		 * This method adds an observer to this controller.
		 *
		 * @param o the Obeserver
		 */
		@Override public void addObserver(Observer o) {
				super.addObserver(o);
		}

		/**
		 * This method gets all treatments from the DB and the path
		 *
		 * @return all treatments
		 * @throws StructureManagementException If the targeted Treatment could not be found.
		 */
        List<Treatment> getAllTreatments() throws StructureManagementException, DataInputException {

            if (listOfAllTreatments == null) {
                listOfAllTreatments = treatmentManagement.findAllTreatments();
            }

            return listOfAllTreatments;
		}

		/**
		 * This method creates a new treatment.
		 *
		 * @param treatment A {@link edu.kit.exp.server.jpa.entity.Treatment Treatment}
		 *                  variable which contains a treatment.
		 * @throws DataInputException           If the institution key already exists.
		 * @throws StructureManagementException
		 */
        void createTreatment(Treatment treatment) throws DataInputException, StructureManagementException {

            Treatment result;

            try {
                result = treatmentManagement.createNewTreatment(treatment);
            } catch (StructureManagementException e) {

                if (e.getMessage().contains(ERROR_IDENTIFIER)) {
                    throw new DataInputException(ERROR_MESSAGE);
                }
                if (e.getMessage().contains(ERROR_IDENTIFIER_TWO)) {
                    throw new DataInputException(ERROR_MESSAGE_TWO);
                }

                throw e;
            }

            listOfAllTreatments.add(result);

            if (countObservers() > 0) {
                setChanged();
                notifyObservers(listOfAllTreatments);
            }
		}

		/**
		 * This method updates a treatment.
		 *
		 * @param treatment A {@link edu.kit.exp.server.jpa.entity.Treatment Treatment}
		 *                  variable which contains a treatment.
		 * @throws DataInputException           If the institution key already exists.
		 * @throws StructureManagementException
		 */
		public void updateTreatment(Treatment treatment) throws DataInputException, StructureManagementException {

            try {
                treatmentManagement.updateTreatment(treatment);

                listOfAllTreatments = treatmentManagement.findAllTreatments();

                if (countObservers() > 0) {
                    setChanged();
                    notifyObservers(listOfAllTreatments);
                }

            } catch (StructureManagementException e) {

                if (e.getMessage().contains(ERROR_IDENTIFIER)) {
                    throw new DataInputException(ERROR_MESSAGE);
                } else {
                    if (e.getMessage().contains(ERROR_IDENTIFIER_TWO)) {
                        throw new DataInputException(ERROR_MESSAGE_TWO);
                    } else {

                        throw e;
                    }
                }
            }
		}

		/**
		 * This method shows the treatment management dialog.
		 */
		public void showTreatmentManagementDialog() throws StructureManagementException, DataInputException {
            if (countObservers() > 0) {
                setChanged();
                notifyObservers(listOfAllTreatments);
            }
            TreatmentManagementDialog instance = TreatmentManagementDialog.getInstance();
            instance.setVisible(true);
		}

		/**
		 * This method shows the treatment creation dialog.
		 */
		public void showTreatmentCreationDialog() {

            TreatmentUpdateDialog td = new TreatmentUpdateDialog(new Treatment(), NEW_TREATMENT_BUTTON_LABEL);
            td.setVisible(true);
		}

		/**
		 * This method shows the treatment update dialog.
		 *
		 * @param t A {@link edu.kit.exp.server.jpa.entity.Treatment Treatment}
		 *          variable which contains a treatment.
		 * @throws DataInputException If no treatment was selected.
		 */
		public void showTreatmentUpdateDialog(Treatment t) throws DataInputException, StructureManagementException {

            if (t == null) {
                throw new DataInputException(DATA_INPUT_EXCPETION_MESSAGE);
            }

            TreatmentUpdateDialog dialog = new TreatmentUpdateDialog(t, CHANGE_TREATMENT_LABEL);
            dialog.setVisible(true);
		}

		/**
		 * This method deletes a treatment.
		 *
		 * @param treatment A {@link edu.kit.exp.server.jpa.entity.Treatment Treatment}
		 *                  variable which contains a treatment.
		 * @throws DataInputException           If no treatment was selected.
		 * @throws StructureManagementException If the targeted Treatment could not be found.
		 */
		public void deleteTreatment(Treatment treatment) throws DataInputException, StructureManagementException {

            if (treatment == null) {
                DataInputException e = new DataInputException(SELECT_TREATMENT_EXCEPTION_MESSAGE);
                throw e;
            }

            treatmentManagement.deleteTreatment(treatment);

            listOfAllTreatments = treatmentManagement.findAllTreatments();

            if (countObservers() > 0) {
                setChanged();
                notifyObservers(listOfAllTreatments);
            }
		}

		/**
		 * This method adds a treatment to a treatment block.
		 *
		 * @param t A {@link edu.kit.exp.server.jpa.entity.Treatment Treatment}
		 *          variable which contains a treatment.
		 * @throws DataInputException           If the treatment was already added.
		 * @throws StructureManagementException
		 */
        void addTreatmentToTreatmentBlock(Treatment t) throws DataInputException, StructureManagementException {

            try {
                    StructureTabController.getInstance().addTreatmentToSelectedTreatmentBlock(t);
            } catch (StructureManagementException e) {
                if (e.getMessage().contains(UNIQUE_CONSTRAINT_IDENTIFIER)) {
                    throw new DataInputException(ALREADY_ADDED_EXCEPTION_MESSAGE);
                } else
                    throw e;
            }
            if (countObservers() > 0) {
                setChanged();
                notifyObservers(listOfAllTreatments);
            }
		}

		/**
		 * This method removes a treatment from a treatment block.
		 *
		 * @param t A {@link edu.kit.exp.server.jpa.entity.Treatment Treatment}
		 *          variable which contains a treatment.
		 * @throws DataInputException           If the treatment was already removed.
		 * @throws StructureManagementException
		 */
        void removeTreatmentFromTreatmentBlock(Treatment t) throws DataInputException, StructureManagementException {

            try {
                StructureTabController.getInstance().removeTreatmentFromSelectedTreatmentBlock(t);
            } catch (StructureManagementException e) {
                if (e.getMessage().contains(UNIQUE_CONSTRAINT_IDENTIFIER)) {
                    throw new DataInputException(ALREADY_ADDED_EXCEPTION_MESSAGE);
                } else
                    throw e;
            }
            if (countObservers() > 0) {
                setChanged();
                notifyObservers(listOfAllTreatments);
            }
		}

		/**
		 * Tells if the treatment is already contained in the treatment block
		 *
		 * @param t
		 * @return boolean value if treatment is contained
		 * @throws DataInputException
		 * @throws StructureManagementException
		 */
		public boolean isTreatmentOfTreatmentBlock(Treatment t) throws DataInputException, StructureManagementException {
            return StructureTabController.getInstance().isTreatmentOfSelectedTreatmentBlock(t);
		}

		public void automaticallyLoadTreatments() throws StructureManagementException, DataInputException {
			List<String> environmentKeys = null;
			List<String> institutionKeys = null;
			try {
				environmentKeys = ReflectionPackageManager.getExtendingClassNames(EXP_IMPLEMENTATION_PATH, Environment.class);
				institutionKeys = ReflectionPackageManager.getExtendingClassNames(EXP_IMPLEMENTATION_PATH, Institution.class);
			} catch (URISyntaxException | IOException | ClassNotFoundException e) {
				LogHandler.printException(e);
			}

            List<Treatment> treatments = getAllTreatments();

            for (String environmentKey : environmentKeys) {
                for (String institutionKey : institutionKeys) {
                    String environmentName = extractName(environmentKey, ENVIRONMENT_EXPRESSIONS);
                    String institutionName = extractName(institutionKey, INSTITUTION_EXPRESSIONS);
                    boolean namesFit = environmentName.equals(institutionName);
                    boolean environmentIsUsed = checkIfEnvironmentIsUsed(treatments, environmentKey);
                    boolean institutionIsUsed = checkIfInstitutionIsUsed(treatments, institutionKey);
                    if (namesFit && environmentIsUsed == false && institutionIsUsed == false) {
                        Treatment treatment = new Treatment();
                        treatment.setName(environmentName);
                        treatment.setEnvironmentFactoryKey(EXP_IMPLEMENTATION_PATH + "." + environmentKey);
                        treatment.setInstitutionFactoryKey(EXP_IMPLEMENTATION_PATH + "." + institutionKey);
                        createTreatment(treatment);
                    }
                }
            }
		}

		private boolean checkIfEnvironmentIsUsed(List<Treatment> treatments, String environmentKey) {
            for (Treatment treatment : treatments) {
                if (treatment.getEnvironmentFactoryKey().equals(EXP_IMPLEMENTATION_PATH + "." + environmentKey)) {
                    return true;
                }
            }
            return false;
		}

		private boolean checkIfInstitutionIsUsed(List<Treatment> treatments, String institutionKey) {
            for (Treatment treatment : treatments) {
                if (treatment.getInstitutionFactoryKey().equals(EXP_IMPLEMENTATION_PATH + "." + institutionKey)) {
                    return true;
                }
            }
            return false;
		}

		private String extractName(String key, String[] namesToBeDeleted) {
            String[] elements = key.split(DOT);
            String name = "";
            if (elements.length > 0) {
                name = elements[elements.length - 1];
            }
            for (String delete : namesToBeDeleted) {
                name = name.replace(delete, EMPTY);
            }
            return name;
		}
}
