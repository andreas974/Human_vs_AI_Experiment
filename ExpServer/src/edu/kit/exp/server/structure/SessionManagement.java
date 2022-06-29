package edu.kit.exp.server.structure;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.dao.DefaultDAO;
import edu.kit.exp.server.jpa.entity.Experiment;
import edu.kit.exp.server.jpa.entity.Session;
import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

/**
 * This class provides all persistence functions of sessions.</br> A Session
 * contains one or more SequenceElements.
 * 
 */
public class SessionManagement {

	/** The instance. */
	private static SessionManagement instance;

	/** The session dao. */
	private DefaultDAO<Session> sessionDAO = new DefaultDAO<>(Session.class);

	/**
	 * This method returns an instance of the SessionManagement.
	 * 
	 * @return a single instance of SessionManagement
	 */
	public static SessionManagement getInstance() {

		if (instance == null) {
			instance = new SessionManagement();
		}

		return instance;
	}

	/**
	 * This constructor instantiates a new session management.
	 */
	private SessionManagement() {

	}

	/**
	 * This method finds a Session in the DB.
	 * 
	 * @param sessionId
	 *            The Integer ID of the Session to be found.
	 * 
	 * @return the session
	 * 
	 * @throws StructureManagementException
	 *             If the Session could not be found.
	 */
	public Session findSession(Integer sessionId) throws StructureManagementException {

		Session session;

		try {
			session = sessionDAO.findById(sessionId);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Session could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return session;
	}

	/**
	 * This method creates a new Session for the given Experiment.
	 * 
	 * @param experiment
	 *            The Experiment where a new Session should be created.
	 * 
	 * @return the session
	 * 
	 * @throws StructureManagementException
	 *             If the Session could not be created.
	 */
	public Session createNewSession(Experiment experiment) throws StructureManagementException {

		Session session = new Session();
		Integer number = experiment.getSessions().size() + 1;
		session.setName("Session " + number.toString());
		session.setExperiment(experiment);
		DateTime date = new DateTime(Calendar.getInstance().get(Calendar.YEAR), 01, 01, 00, 00);
		Timestamp plannedDate = new Timestamp(date.getMillis());
		session.setPlannedDate(plannedDate);

		Session result;

		try {
			result = sessionDAO.create(session);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Session could not be created. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}

	// creat a copysession
	public Session copySession(Integer sessionId, Experiment experiment) throws StructureManagementException {

		Session session = null;
		try {
			session = sessionDAO.findById(sessionId);
		} catch (DataManagementException e1) {

			e1.printStackTrace();
		}

		session.setIdSession(null);
		Integer number = experiment.getSessions().size() + 1;
		session.setName("Session " + number.toString());
		session.setExperiment(experiment);
		DateTime date = new DateTime(Calendar.getInstance().get(Calendar.YEAR), 01, 01, 00, 00);
		Timestamp plannedDate = new Timestamp(date.getMillis());
		session.setPlannedDate(plannedDate);

		Session result;

		try {
			result = sessionDAO.create(session);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Session could not be created. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}

	/**
	 * This method gets all sessions from the DB.
	 * 
	 * @return A list of all the Sessions.
	 * 
	 * @throws StructureManagementException
	 *             If no Sessions were found.
	 */
	public List<Session> findAllSessions() throws StructureManagementException {

		List<Session> list;
		try {
			list = sessionDAO.findAll();
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Sessions could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return list;
	}

	/**
	 * This method removes a session from the DB.
	 * 
	 * @param idSession
	 *            The Integer ID of the Session to be removed.
	 * 
	 * @throws StructureManagementException
	 *             If the Session could not be found.
	 */
	public void deleteSession(Integer idSession) throws StructureManagementException {

		Session session;
		try {
			session = sessionDAO.findById(idSession);
			sessionDAO.delete(session);
		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Sessions could not be found. Cause: " + e.getMessage());
			throw ex;
		}
	}

	/**
	 * This method updates a Session.
	 * 
	 * @param session
	 *            The Session to be updated.
	 * 
	 * @return the updated Session
	 * 
	 * @throws StructureManagementException
	 *             If the Session could not be updated.
	 */
	public Session updateSession(Session session) throws StructureManagementException {

		Session result;

		try {
			result = sessionDAO.update(session);

		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Sessions could not be updated. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}


}
