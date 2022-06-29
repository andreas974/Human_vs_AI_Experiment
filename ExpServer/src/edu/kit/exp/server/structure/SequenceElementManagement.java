package edu.kit.exp.server.structure;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.dao.DefaultDAO;
import edu.kit.exp.server.jpa.entity.*;

import java.util.List;

/**
 * This class provides all persistence functions of SequenceElements.</br>
 * SequenceElementsrepresent different elements which can occur during a Session
 * and are not part of a Period, e.g. a pause, quiz or treatment block.
 * 
 */
public class SequenceElementManagement {

	/** The instance. */
	private static SequenceElementManagement instance;

	/** The sequence element dao. */
	private DefaultDAO<SequenceElement> sequenceElementDAO = new DefaultDAO<>(SequenceElement.class);

	/**
	 * This method returns an instance of the SequenceElementManagement.
	 * 
	 * @return a single instance of SequenceElementManagement
	 */
	public static SequenceElementManagement getInstance() {

		if (instance == null) {
			instance = new SequenceElementManagement();
		}

		return instance;
	}

	/**
	 * This constructor instantiates a new sequence element management.
	 */
	private SequenceElementManagement() {

	}

	/**
	 * This method finds a SequenceElement in DB.
	 * 
	 * @param treatmentBlockId
	 *            An Integer that contains the ID of the SequenceElement to
	 *            search for.
	 * 
	 * @return the sequence element
	 * 
	 * @throws StructureManagementException
	 *             If the SequenceElement could not be found.
	 */
	public SequenceElement findSequenceElement(Integer treatmentBlockId) throws StructureManagementException {

		SequenceElement treatmentBlock;

		try {
			treatmentBlock = sequenceElementDAO.findById(treatmentBlockId);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("SequenceElement could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return treatmentBlock;
	}

	/**
	 * This method creates the SequenceElement TreatmentBlock in a given
	 * Session.
	 * 
	 * @param session
	 *            The Session where a TreatmentBlock should be created.
	 * @param practice
	 *            A boolean variable which shows if the TreatmentBlock is
	 *            practice(true) or not(false).
	 * 
	 * @return the sequence element
	 * 
	 * @throws StructureManagementException
	 *             If a TreatmentBlock could not be created.
	 */
	public SequenceElement createNewTreatmentBlock(Session session, boolean practice) throws StructureManagementException {

		TreatmentBlock treatmentBlock = new TreatmentBlock();
		Integer sequenceNumber = session.getSequenceElements().size() + 1;
		treatmentBlock.setName("TreatmentBlock " + sequenceNumber.toString());
		treatmentBlock.setSession(session);
		treatmentBlock.setSequenceNumber(sequenceNumber);

		treatmentBlock.setPractice(practice);

		TreatmentBlock result; // FIXME WIRD DIE METHODE GENUTZT EVTL TB
								// MANAGEMENT L�SCHEN

		try {
			result = (TreatmentBlock) sequenceElementDAO.create(treatmentBlock);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("TreatmentBlock could not be created. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}

	// create a copytreatment_block

	// Liang
	public SequenceElement copyTreatmentBlock(Integer treatmentblock, Experiment experiment) throws StructureManagementException {

		DefaultDAO<TreatmentBlock> treatmentBlockDAO = new DefaultDAO<>(TreatmentBlock.class);
		TreatmentBlock treatmentBlock = null;
		try {
			treatmentBlock = treatmentBlockDAO.findById(treatmentblock);
		} catch (DataManagementException e1) {

			e1.printStackTrace();
		}

		treatmentBlock.setIdsequenceElement(null);
		Integer sequenceNumber = treatmentBlock.getSequenceNumber() + 1;
		treatmentBlock.setName("TreatmentBlock " + sequenceNumber.toString());
		treatmentBlock.setSequenceNumber(sequenceNumber);

		// treatmentBlock.setPractice(practice);

		SequenceElement result; // FIXME WIRD DIE METHODE GENUTZT EVTL TB
								// MANAGEMENT L�SCHEN

		try {
			result = (SequenceElement) sequenceElementDAO.create(treatmentBlock);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("TreatmentBlock could not be created. Cause: " + e.getMessage());
			throw ex;
		}

		return result;
	}

	/**
	 * This method creates the SequenceElement Quiz in a given Session.
	 *
	 * @param session
	 *            The Session where a Quiz should be created.
	 *
	 * @return the questionnaire
	 *
	 * @throws StructureManagementException
	 *             If the Quiz could not be created.
	 */
	public Questionnaire createNewQuestionnaire(Session session) throws StructureManagementException {

		Questionnaire questionnaire = new Questionnaire();
		questionnaire.setSession(session);
		Integer sequenceNumber = session.getSequenceElements().size() + 1;
		questionnaire.setSequenceNumber(sequenceNumber);

		Questionnaire result;

		try {
			result = (Questionnaire) sequenceElementDAO.create(questionnaire);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Questionnaire could not be created. Cause: " + e.getMessage());
			throw ex;
		}

		return result;
	}

	/**
	 * This method creates the SequenceElement Quiz in a given Session.
	 * 
	 * @param session
	 *            The Session where a Quiz should be created.
	 * 
	 * @return the quiz
	 * 
	 * @throws StructureManagementException
	 *             If the Quiz could not be created.
	 */
	public Quiz createNewQuiz(Session session) throws StructureManagementException {

		Quiz quiz = new Quiz();
		quiz.setSession(session);
		Integer sequenceNumber = session.getSequenceElements().size() + 1;
		quiz.setSequenceNumber(sequenceNumber);

		Quiz result;

		try {
			result = (Quiz) sequenceElementDAO.create(quiz);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Quiz could not be created. Cause: " + e.getMessage());
			throw ex;
		}

		return result;
	}

	/**
	 * This method creates the SequenceElement Pause in a given Session.
	 * 
	 * @param session
	 *            The Session where a Pause should be created.
	 * 
	 * @return the pause
	 * 
	 * @throws StructureManagementException
	 *             If the Pause could not be created.
	 */
	public Pause createNewPause(Session session) throws StructureManagementException {

		Pause pause = new Pause();
		pause.setSession(session);
		Integer sequenceNumber = session.getSequenceElements().size() + 1;
		pause.setSequenceNumber(sequenceNumber);
		pause.setTime(60000L); // 60000 milli sec
		pause.setMessage("Break... Please wait!");

		Pause result;

		try {
			result = (Pause) sequenceElementDAO.create(pause);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Pause could not be created. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}

	/**
	 * This method gets all the SequenceElements from the DB.
	 * 
	 * @return A list that contains all SequenceElements.
	 * 
	 * @throws StructureManagementException
	 *             If no SequenceElements were found.
	 */
	public List<SequenceElement> findAllTreatmentBlocks() throws StructureManagementException {

		List<SequenceElement> list;
		try {
			list = sequenceElementDAO.findAll();
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("SequenceElements could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return list;
	}

	/**
	 * This method removes a SequenceElement from the DB.
	 * 
	 * @param sequenceElement
	 *            The SequenceElement to be removed.
	 * 
	 * @throws StructureManagementException
	 *             If no SequenceElement could be removed.
	 */
	public void deleteSequenceElement(SequenceElement sequenceElement) throws StructureManagementException {

		try {
			sequenceElementDAO.delete(sequenceElement);
		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("SequenceElement could not be found. Cause: " + e.getMessage());
			throw ex;
		}
	}

	/**
	 * This method updates a given SequenceElement in the database.
	 * 
	 * @param sequenceElements
	 *            the SequenceElements to be updated
	 * 
	 * @return the updated sequence element
	 * 
	 * @throws StructureManagementException
	 *             If the given SequenceElement could not be found.
	 */
	public SequenceElement updateSequenceElement(SequenceElement sequenceElements) throws StructureManagementException {

		SequenceElement result;
		try {
			result = sequenceElementDAO.update(sequenceElements);

		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("SequenceElement could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}

}
