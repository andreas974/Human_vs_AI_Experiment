package edu.kit.exp.server.structure;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.dao.DefaultDAO;
import edu.kit.exp.server.jpa.entity.QuestionnaireProtocol;
import edu.kit.exp.server.jpa.entity.QuizProtocol;

import java.util.List;

/**
 * This class provides all persistence functions of protocols.</br> A QuizProtocol
 * shows the result of a Subject for a Quiz.
 * 
 */
public class ProtocolManagement {

	/** The instance. */
	private static ProtocolManagement instance;

	/** The quiz protocol dao. */
	private DefaultDAO<QuizProtocol> quizProtocolDAO = new DefaultDAO<>(QuizProtocol.class);

	/** The questionnaire protocol dao. */
	private DefaultDAO<QuestionnaireProtocol> questionnaireProtocolDAO = new DefaultDAO<>(QuestionnaireProtocol.class);

	/**
	 * This method returns an instance of the ProtocolManagement.
	 * 
	 * @return a single instance of ProtocolManagement
	 */
	public static ProtocolManagement getInstance() {

		if (instance == null) {
			instance = new ProtocolManagement();
		}

		return instance;
	}

	/**
	 * This constructor instantiates a new protocol management.
	 */
	private ProtocolManagement() {

	}

	/**
	 * This method finds a quiz protocol by its ID in the DB.
	 * 
	 * @param protocolId
	 *            An Integer which contains the ID of the protocol to be found.
	 * 
	 * @return the protocol
	 * 
	 * @throws StructureManagementException
	 *             If the protocol was not found.
	 */
	public QuizProtocol findQuizProtocol(Integer protocolId) throws StructureManagementException {

		QuizProtocol quizProtocol;

		try {
			quizProtocol = quizProtocolDAO.findById(protocolId);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("QuizProtocol could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return quizProtocol;
	}

	/**
	 * This method finds a quiz protocol by its ID in the DB.
	 *
	 * @param protocolId
	 *            An Integer which contains the ID of the protocol to be found.
	 *
	 * @return the protocol
	 *
	 * @throws StructureManagementException
	 *             If the protocol was not found.
	 */
	public QuestionnaireProtocol findQuestionnaireProtocol(Integer protocolId) throws StructureManagementException {

		QuestionnaireProtocol questionnaireProtocol;

		try {
			questionnaireProtocol = questionnaireProtocolDAO.findById(protocolId);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("QuestionnaireProtocol could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return questionnaireProtocol;
	}

	/**
	 * This method creates a new quiz protocol in the database for the given
	 * experiment.
	 * 
	 * @param protocol
	 *            the new QuizProtocol
	 * 
	 * @return the protocol
	 * 
	 * @throws StructureManagementException
	 *             If the QuizProtocol could not be created.
	 */
	public QuizProtocol createNewQuizProtocol(QuizProtocol protocol) throws StructureManagementException {

		QuizProtocol result;

		try {
			result = quizProtocolDAO.create(protocol);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("QuizProtocol could not be created. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}

	/**
	 * This method creates a new questionnaire protocol in the database for the given
	 * experiment.
	 *
	 * @param protocol
	 *            the new QuestionnaireProtocol
	 *
	 * @return the protocol
	 *
	 * @throws StructureManagementException
	 *             If the QuestionnaireProtocol could not be created.
	 */
	public QuestionnaireProtocol createNewQuestionnaireProtocol(QuestionnaireProtocol protocol) throws StructureManagementException {

		QuestionnaireProtocol result;

		try {
			result = questionnaireProtocolDAO.create(protocol);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("QuestionnaireProtocol could not be created. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}

	/**
	 * This method gets all protocols from the DB.
	 * 
	 * @return the list of Protocols
	 * 
	 * @throws StructureManagementException
	 *             If no protocols could be found.
	 */
	public List<QuizProtocol> findAllQuizProtocols() throws StructureManagementException {

		List<QuizProtocol> list;
		try {
			list = quizProtocolDAO.findAll();
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Quiz Protocols could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return list;
	}

	/**
	 * This method gets all protocols from the DB.
	 *
	 * @return the list of Protocols
	 *
	 * @throws StructureManagementException
	 *             If no protocols could be found.
	 */
	public List<QuestionnaireProtocol> findAllQuestionnaireProtocols() throws StructureManagementException {

		List<QuestionnaireProtocol> list;
		try {
			list = questionnaireProtocolDAO.findAll();
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Questionnaire Protocols could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return list;
	}

	/**
	 * This method removes a protocol from the DB.
	 * 
	 * @param idProtocol
	 *            The Integer Id of the protocol to remove.
	 * 
	 * @throws StructureManagementException
	 *             If no protocols were found.
	 */
	public void deleteQuizProtocol(Integer idProtocol) throws StructureManagementException {

		QuizProtocol protocol;
		try {
			protocol = quizProtocolDAO.findById(idProtocol);
			quizProtocolDAO.delete(protocol);
		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Quiz Protocols could not be found. Cause: " + e.getMessage());
			throw ex;
		}
	}

	/**
	 * This method removes a protocol from the DB.
	 *
	 * @param idProtocol
	 *            The Integer Id of the protocol to remove.
	 *
	 * @throws StructureManagementException
	 *             If no protocols were found.
	 */
	public void deleteQuestionnaireProtocol(Integer idProtocol) throws StructureManagementException {

		QuestionnaireProtocol protocol;
		try {
			protocol = questionnaireProtocolDAO.findById(idProtocol);
			questionnaireProtocolDAO.delete(protocol);
		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Questionnaire Protocols could not be found. Cause: " + e.getMessage());
			throw ex;
		}
	}

	/**
	 * This method updates a protocol.
	 * 
	 * @param protocol
	 *            QuizProtocol to be updated.
	 * 
	 * @return the updated protocol
	 * 
	 * @throws StructureManagementException
	 *             If the protocols could not be updated.
	 */
	public QuizProtocol updateQuizProtocol(QuizProtocol protocol) throws StructureManagementException {

		QuizProtocol result;

		try {
			result = quizProtocolDAO.update(protocol);

		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Protocols could not be updated. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}

	/**
	 * This method updates a protocol.
	 *
	 * @param protocol
	 *            QuizProtocol to be updated.
	 *
	 * @return the updated protocol
	 *
	 * @throws StructureManagementException
	 *             If the protocols could not be updated.
	 */
	public QuestionnaireProtocol updateQuestionnaireProtocol(QuestionnaireProtocol protocol) throws StructureManagementException {

		QuestionnaireProtocol result;

		try {
			result = questionnaireProtocolDAO.update(protocol);

		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Questionnaire Protocols could not be updated. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}
}
