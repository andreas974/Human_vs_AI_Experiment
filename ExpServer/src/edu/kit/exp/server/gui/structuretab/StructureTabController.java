package edu.kit.exp.server.gui.structuretab;

import java.sql.Timestamp;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import edu.kit.exp.server.gui.DataInputException;
import edu.kit.exp.server.gui.treatment.TreatmentManagementDialogController;
import edu.kit.exp.server.jpa.entity.Cohort;
import edu.kit.exp.server.jpa.entity.Experiment;
import edu.kit.exp.server.jpa.entity.Period;
import edu.kit.exp.server.jpa.entity.SensorEntry;
import edu.kit.exp.server.jpa.entity.SequenceElement;
import edu.kit.exp.server.jpa.entity.Session;
import edu.kit.exp.server.jpa.entity.Treatment;
import edu.kit.exp.server.jpa.entity.TreatmentBlock;
import edu.kit.exp.server.structure.CohortManagement;
import edu.kit.exp.server.structure.ExperimentManagement;
import edu.kit.exp.server.structure.PeriodManagement;
import edu.kit.exp.server.structure.SensorEntryManagement;
import edu.kit.exp.server.structure.SequenceElementManagement;
import edu.kit.exp.server.structure.SessionManagement;
import edu.kit.exp.server.structure.StructureManagementException;

/**
 * This class represents the controller for the start tab.</br> It extends
 * Observable.
 *
 * @see Observable
 */
public class StructureTabController extends Observable {

    /**
     * The instance.
     */
    private static StructureTabController instance = new StructureTabController();

    /**
     * The experiment management.
     */
    private ExperimentManagement experimentManagement = ExperimentManagement.getInstance();

    /**
     * The cohort management.
     */
    private CohortManagement cohortManagement = CohortManagement.getInstance();

    /**
     * The current experiment.
     */
    private Experiment currentExperiment;

    /**
     * The session management.
     */
    private SessionManagement sessionManagement = SessionManagement.getInstance();

    /**
     * The sensor entry management.
     */
    private SensorEntryManagement sensorEntryManagement = SensorEntryManagement.getInstance();

    /**
     * The period management.
     */
    private PeriodManagement periodManagement = PeriodManagement.getInstance();

    /**
     * The date time formatter.
     */
    private DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");

    /**
     * The sequence element management.
     */
    private SequenceElementManagement sequenceElementManagement = SequenceElementManagement.getInstance();

    /**
     * Constructor.
     */
    private StructureTabController() {

    }

    /**
     * This method returns the only instance of this class.
     *
     * @return a single instance of StructureTabController
     */
    public static StructureTabController getInstance() {

            return instance;
    }

    /**
     * This method adds an observer to this controller.
     *
     * @param o an Observer
     */
    @Override public void addObserver(Observer o) {
            super.addObserver(o);
    }

    /**
     * This method sets the experiment that should be displayed in
     * ExperimentBuilder.
     *
     * @param experiment An Experiment variable which contains the new current
     *                   experiment.
     * @throws StructureManagementException If an experiment could not be found.
     */
    public void setCurrentExperiment(Experiment experiment) throws StructureManagementException {

        Experiment experimentToLoad = null;

        if (currentExperiment == null) {
            experimentToLoad = experiment;
        } else {
            experimentToLoad = experimentManagement.findExperiment(experiment.getIdExperiment());
        }

        currentExperiment = experimentToLoad;

        if (countObservers() > 0) {
            setChanged();
            notifyObservers(experiment);
        }
    }

    /**
     * This method gets the current experiment.
     *
     * @return the current experiment
     */
    public Experiment getCurrentExperiment() {
            return currentExperiment;
    }

    /**
     * This method creates a new session for the current experiment.
     *
     * @throws StructureManagementException If the Session could not be created.
     */
    public void createNewSession() throws StructureManagementException {

        Session result = sessionManagement.createNewSession(currentExperiment);
        currentExperiment.getSessions().add(result);

        if (countObservers() > 0) {
            setChanged();
            notifyObservers(currentExperiment);
        }
    }

    /**
     * This method creates a new sensor entry for the current experiment.
     *
     * @throws StructureManagementException If the SensorEntry could not be created.
     */
    public void createNewSensorEntry(SensorEntry sensorEntry) throws StructureManagementException {

        SensorEntry result = sensorEntryManagement.createNewSensorEntry(currentExperiment, sensorEntry);
        currentExperiment.getUsedSensors().add(result);

        if (countObservers() > 0) {
            setChanged();
            notifyObservers(currentExperiment);
        }
    }

    /**
     * Copies the session
     *
     * @throws StructureManagementException
     */

    public void copySession(Integer sessionId) throws StructureManagementException {

        Session result = sessionManagement.copySession(sessionId, currentExperiment);
        currentExperiment.getSessions().add(result);

        if (countObservers() > 0) {
            setChanged();
            notifyObservers(currentExperiment);
        }
    }

    /**
     * This method creates a new treatment block for the given session.
     *
     * @param session  A {@link edu.kit.exp.server.jpa.entity.Session Session}
     *                 variable which contains the given session.
     * @param practice A boolean variable which indicates if the session is a
     *                 practice session.
     * @throws StructureManagementException If a TreatmentBlock could not be created.
     */
    public void createNewTreatmentBlock(Session session, boolean practice) throws StructureManagementException {

        SequenceElement result = sequenceElementManagement.createNewTreatmentBlock(session, practice);
        currentExperiment = result.getSession().getExperiment();

        if (countObservers() > 0) {
            setChanged();
            notifyObservers(currentExperiment);
        }
    }

    /**
     * Copies the treatment block
     *
     * @throws StructureManagementException
     */

    public void copyTreatmentBlock(Integer Idtreatmentblock) throws StructureManagementException {

        SequenceElement result = sequenceElementManagement.copyTreatmentBlock(Idtreatmentblock, currentExperiment);
        currentExperiment = result.getSession().getExperiment();

        if (countObservers() > 0) {
            setChanged();
            notifyObservers(currentExperiment);
        }
    }

    /**
     * This method creates a new quiz for a given session.
     *
     * @param session A {@link edu.kit.exp.server.jpa.entity.Session Session}
     *                variable which contains the given session.
     * @throws StructureManagementException If a quiz could not be created.
     */
    public void createNewQuestionnaire(Session session) throws StructureManagementException {

        SequenceElement result = sequenceElementManagement.createNewQuestionnaire(session);
        currentExperiment = result.getSession().getExperiment();

        if (countObservers() > 0) {
            setChanged();
            notifyObservers(currentExperiment);
        }

    }

    /**
     * This method creates a new quiz for a given session.
     *
     * @param session A {@link edu.kit.exp.server.jpa.entity.Session Session}
     *                variable which contains the given session.
     * @throws StructureManagementException If a quiz could not be created.
     */
    public void createNewQuiz(Session session) throws StructureManagementException {

        SequenceElement result = sequenceElementManagement.createNewQuiz(session);
        currentExperiment = result.getSession().getExperiment();

        if (countObservers() > 0) {
            setChanged();
            notifyObservers(currentExperiment);
        }

    }

    /**
     * This method creates a new pause for a given session.
     *
     * @param session A {@link edu.kit.exp.server.jpa.entity.Session Session}
     *                variable which contains the given session.
     * @throws StructureManagementException If the pause could not be created.
     */
    public void createNewPause(Session session) throws StructureManagementException {

        SequenceElement result = sequenceElementManagement.createNewPause(session);
        currentExperiment = result.getSession().getExperiment();

        if (countObservers() > 0) {
            setChanged();
            notifyObservers(currentExperiment);
        }

    }

    /**
     * This method creates a new period for a given treatment block.
     *
     * @param treatmentBlock  A {@link edu.kit.exp.server.jpa.entity.TreatmentBlock
     *                        TreatmentBlock} variable which contains the current treatment
     *                        block.
     * @param numberOfPeriods An int variable which counts the number of periods.
     * @throws StructureManagementException If a period could not be created.
     */
    public void createNewPeriods(TreatmentBlock treatmentBlock, int numberOfPeriods) throws StructureManagementException {

        List<Period> result = periodManagement.createNewPeriods(treatmentBlock, numberOfPeriods);
        currentExperiment = result.get(0).getTreatmentBlock().getSession().getExperiment();

        if (countObservers() > 0) {
            setChanged();
            notifyObservers(currentExperiment);
        }

    }

    /**
     * This method updates a given experiment.
     *
     * @param experiment An {@link edu.kit.exp.server.jpa.entity.Experiment Experiment}
     *                   variable which contains a given experiment.
     * @throws StructureManagementException If no Experiments could be found.
     */
    public void updateExperiment(Experiment experiment) throws StructureManagementException {

        currentExperiment = experimentManagement.updateExperiment(experiment);

        if (countObservers() > 0) {
            setChanged();
            notifyObservers(experiment);
        }
    }

    /**
     * This method updates a Session.
     *
     * @param session       A {@link edu.kit.exp.server.jpa.entity.Session Session}
     *                      variable which contains the given session.
     * @param cohorts       the number of cohorts
     * @param cohortSize    the size of a cohort
     * @throws StructureManagementException If the Session was not found.
     * @throws DataInputException           If the data input was not correct.
     */
    public void updateSession(Session session, Integer cohorts, Integer cohortSize) throws StructureManagementException, DataInputException {

        Session result = sessionManagement.updateSession(session);

        if (cohorts != session.getCohorts().size() || cohortSize != session.getCohorts().get(0).getSize()) {

            for (Cohort cohort : result.getCohorts()) {
                cohortManagement.deleteCohort(cohort);
            }

            Cohort cohort = null;

            for (int i = 0; i < cohorts; i++) {
                cohort = cohortManagement.createNewCohort(result, cohortSize);
            }

            result = sessionManagement.updateSession(cohort.getSession());

        }

        currentExperiment = result.getExperiment();

        if (countObservers() > 0) {
            setChanged();
            notifyObservers(currentExperiment);
        }
    }

    /**
     * This method updates a SequenceElement.
     *
     * @param sequenceElement The {@link edu.kit.exp.server.jpa.entity.SequenceElement
     *                        SequenceElement} to be updated.
     * @throws StructureManagementException If the given SequenceElement could not be found.
     * @throws DataInputException           If the data input was not correct.
     */
    public void updateSequenceElement(SequenceElement sequenceElement) throws StructureManagementException, DataInputException {

        if (sequenceElement.getClass().equals(TreatmentBlock.class)) {
            TreatmentBlock treatmentBlock = (TreatmentBlock) sequenceElement;

            // Check for illegal state
            if (treatmentBlock.getRandomization() == null && treatmentBlock.getTreatments().size() > 1) {
                throw new DataInputException("TreatmentBlock has more than 1 Treatment but there is no randomization procedure defined!");
            }

            // Check for illegal state
            if (treatmentBlock.getRandomization() != null && treatmentBlock.getTreatments().size() == 1) {
                throw new DataInputException("Random matching is selected, but TreatmentBlock has only 1 Treatment!");
            }
        }

        SequenceElement result = sequenceElementManagement.updateSequenceElement(sequenceElement);
        currentExperiment = result.getSession().getExperiment();

        if (countObservers() > 0) {
            setChanged();
            notifyObservers(currentExperiment);
        }

    }

    /**
     * This method updates a period.
     *
     * @param period The {@link edu.kit.exp.server.jpa.entity.Period Period} to be
     *               updated.
     * @throws StructureManagementException If the selected Period was not found.
     */
    public void updateSensorEntry(SensorEntry sensorEntry) throws StructureManagementException {

        sensorEntryManagement.updateSensorEntry(sensorEntry);

    }

    /**
     * Change the sequenceNumber for Session or SequenceElement
     * @param object
     * @param sequenceNumber
     * @throws StructureManagementException
     * @throws IllegalArgumentException
     */
    public void changeSequenceNumber(Object object, int sequenceNumber) throws StructureManagementException, IllegalArgumentException {

        if (object.getClass().equals(Period.class)) {
            Period period = (Period)object;
            period.setSequenceNumber(sequenceNumber);
            periodManagement.updatePeriod(period);
        } else if (SequenceElement.class.isAssignableFrom(object.getClass())) {
            SequenceElement sequenceElement = (SequenceElement)object;
            sequenceElement.setSequenceNumber(sequenceNumber);
            sequenceElementManagement.updateSequenceElement(sequenceElement);
        } else {
            throw new IllegalArgumentException("Can only change sequenceNumber for Session and SequenceElement - not " + object.getClass().getName());
        }

        if (countObservers() > 0) {
            setChanged();
            notifyObservers(currentExperiment);
        }
    }

    /**
     * This method updates a SensorEntries.
     *
     * @param sensorEntry The {@link edu.kit.exp.server.jpa.entity.SensorEntry SensorEntry} to be
     *                    updated.
     * @throws StructureManagementException If the selected SensorEntry was not found.
     */
    public void updatePeriod(Period period) throws StructureManagementException {

        Period result = periodManagement.updatePeriod(period);
        currentExperiment = result.getTreatmentBlock().getSession().getExperiment();

        if (countObservers() > 0) {
            setChanged();
            notifyObservers(currentExperiment);
        }

    }

    /**
     * This method shows the treatment management dialog.
     */
    public void showTreatmentDialog() throws StructureManagementException, DataInputException {
        TreatmentManagementDialogController.getInstance().showTreatmentManagementDialog();

    }

    /**
     * This method deletes a session.
     *
     * @param session A {@link edu.kit.exp.server.jpa.entity.Session Session}
     *                variable which contains the session to be deleted.
     * @throws StructureManagementException If no Session could be removed or the Experiment could not be
     *                                      loaded.
     */
    public void removeSession(Session session) throws StructureManagementException {

        sessionManagement.deleteSession(session.getIdSession());
        currentExperiment = experimentManagement.findExperiment(currentExperiment.getIdExperiment());

        if (countObservers() > 0) {
            setChanged();
            notifyObservers(currentExperiment);
        }
    }

    /**
     * This method deletes a session.
     *
     * @param sensorEntry variable which contains the sensorEntry to be deleted.
     * @throws StructureManagementException If no SensorEntry could be removed or the Experiment could not be
     *                                      loaded.
     */
    public void removeSensorEntry(SensorEntry sensorEntry) throws StructureManagementException {

        sensorEntryManagement.deleteSensorEntry(sensorEntry);
        currentExperiment = experimentManagement.findExperiment(currentExperiment.getIdExperiment());

        if (countObservers() > 0) {
            setChanged();
            notifyObservers(currentExperiment);
        }
    }

    /**
     * This method deletes a sequence element.
     *
     * @param sequenceElement The {@link edu.kit.exp.server.jpa.entity.SequenceElement
     *                        SequenceElement} to be deleted.
     * @throws StructureManagementException If no SequenceElement could be removed or the Experiment
     *                                      could not be loaded.
     */
    public void removeSequenceElement(SequenceElement sequenceElement) throws StructureManagementException {

        sequenceElementManagement.deleteSequenceElement(sequenceElement);
        currentExperiment = experimentManagement.findExperiment(currentExperiment.getIdExperiment());

        if (countObservers() > 0) {
            setChanged();
            notifyObservers(currentExperiment);
        }
    }

    /**
     * This method removes a period.
     *
     * @param period The {@link edu.kit.exp.server.jpa.entity.Period Period} to be
     *               updated.
     * @throws StructureManagementException If the Period could not be deleted or the experiment could
     *                                      not be loaded.
     */
    public void removePeriod(Period period) throws StructureManagementException {

        periodManagement.deletePeriod(period);
        currentExperiment = experimentManagement.findExperiment(currentExperiment.getIdExperiment());

        if (countObservers() > 0) {
            setChanged();
            notifyObservers(currentExperiment);
        }
    }

    /**
     * This method parses a String to a timestamp.
     *
     * @param timeStamp A String variable which contains the timestamp.
     * @return a timestamp
     * @throws DataInputException If the date could not be parsed.
     * @see Timestamp
     */
    public Timestamp parseDateString(String timeStamp) throws DataInputException {

        try {
             long millies = fmt.parseMillis(timeStamp);
            Timestamp result = new Timestamp(millies);
            return result;

        } catch (Exception e) {
            throw new DataInputException("Date could not be parsed. Format: " + fmt.getParser().toString());
        }
    }

    /**
     * This method returns the selected session.
     *
     * @return a selected session from the database
     * @throws DataInputException           If no session was selected.
     * @throws StructureManagementException
     */
    public Session getSelectedSession() throws DataInputException, StructureManagementException {

        Session selectedSession = StructureTreeBuilder.getInstance().getSelectedSession();

        // Make sure to get actual data
        return sessionManagement.findSession(selectedSession.getIdSession());
    }

    /**
     * This method adds a treatment to the selected session.
     *
     * @param newTreatment The new {@link edu.kit.exp.server.jpa.entity.Treatment
     *                     Treatment} that will be added to the session.
     * @throws DataInputException           If no treatment block was selected.
     * @throws StructureManagementException If the given SequenceElement could not be found.
     */
    public void addTreatmentToSelectedTreatmentBlock(Treatment newTreatment) throws DataInputException, StructureManagementException {

        TreatmentBlock treatmentBlock = StructureTreeBuilder.getInstance().getSelectedTreatmentBlock();
        treatmentBlock.getTreatments().add(newTreatment);

        TreatmentBlock result = (TreatmentBlock) sequenceElementManagement.updateSequenceElement(treatmentBlock);

        currentExperiment = result.getSession().getExperiment();

        if (countObservers() > 0) {
            setChanged();
            notifyObservers(currentExperiment);
        }
    }

    /**
     * This method removes a treatment from the selected session.
     *
     * @param treatment The {@link edu.kit.exp.server.jpa.entity.Treatment Treatment}
     *                  that will be removed from the session.
     * @throws DataInputException           If no treatment block was selected.
     * @throws StructureManagementException If the given SequenceElement could not be found.
     */
    public void removeTreatmentFromSelectedTreatmentBlock(Treatment treatment) throws DataInputException, StructureManagementException {

        TreatmentBlock treatmentBlock = StructureTreeBuilder.getInstance().getSelectedTreatmentBlock();
        treatmentBlock.getTreatments().remove(treatment);

        TreatmentBlock result = (TreatmentBlock) sequenceElementManagement.updateSequenceElement(treatmentBlock);

        currentExperiment = result.getSession().getExperiment();

        if (countObservers() > 0) {
            setChanged();
            notifyObservers(currentExperiment);
        }
    }

    /**
     * Compares treatment to treatments of the block to tell if the block already contains this treatment
     *
     * @param treatment treatment to be compared
     * @return
     * @throws DataInputException
     */
    public boolean isTreatmentOfSelectedTreatmentBlock(Treatment treatment) throws DataInputException {
        TreatmentBlock treatmentBlock = StructureTreeBuilder.getInstance().getSelectedTreatmentBlock();
        for (Treatment treatmentOfBlock : treatmentBlock.getTreatments()) {
            if (treatment.equals(treatmentOfBlock)) {
                return true;
            }
        }
        return false;
    }
}
