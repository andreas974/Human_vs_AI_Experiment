package edu.kit.exp.server.structure;

import java.util.List;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.dao.DefaultDAO;
import edu.kit.exp.server.jpa.entity.SubjectGroup;

/**
 * This class provides all persistence functions of SubjectGroups. A
 * SubjectGroup defines for every Period which Subjects in which roles are going
 * to interact.
 * 
 */
public class SubjectGroupManagement {

	/** The instance. */
	private static SubjectGroupManagement instance;

	/** The subject group dao. */
	private DefaultDAO<SubjectGroup> subjectGroupDAO = new DefaultDAO<>(SubjectGroup.class);

	/**
	 * This method returns an instance of the SubjectGroupManagement.
	 * 
	 * @return a single instance of SubjectGroupManagement
	 */
	public static SubjectGroupManagement getInstance() {

		if (instance == null) {
			instance = new SubjectGroupManagement();
		}

		return instance;
	}

	/**
	 * This constructor instantiates a new subject group management.
	 */
	private SubjectGroupManagement() {

	}

	/**
	 * This method finds a SubjectGroup in the DB.
	 * 
	 * @param subjectGroupId
	 *            The Long ID of the SubjectGroup to be found.
	 * 
	 * @return the subject group
	 * 
	 * @throws StructureManagementException
	 *             If the SubjectGroup was not found.
	 */
	public SubjectGroup findSubjectGroup(Long subjectGroupId) throws StructureManagementException {

		SubjectGroup subjectGroup;

		try {
			subjectGroup = subjectGroupDAO.findById(subjectGroupId);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("SubjectGroup could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return subjectGroup;
	}

	/**
	 * This method creates a new SubjectGroup.
	 * 
	 * @param subjectGroup
	 *            The new SubjectGroup to be created.
	 * 
	 * @return the subject group
	 * 
	 * @throws StructureManagementException
	 *             If the SubjectGroup could not be created.
	 */
	public SubjectGroup createNewSubjectGroup(SubjectGroup subjectGroup) throws StructureManagementException {

		SubjectGroup result;

		try {
			result = subjectGroupDAO.create(subjectGroup);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("SubjectGroup could not be created. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}

	/**
	 * This method gets all SubjectGroups from the DB.
	 * 
	 * @return a list of all subject groups
	 * 
	 * @throws StructureManagementException
	 *             If no SubjectGrooups were found.
	 */
	public List<SubjectGroup> findAllSubjectGroups() throws StructureManagementException {

		List<SubjectGroup> list;
		try {
			list = subjectGroupDAO.findAll();
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("SubjectGroups could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return list;
	}

	/**
	 * This method removes a SubjectGroup from the DB.
	 * 
	 * @param idSubjectGroup
	 *            The Long ID of the SubjectGroup to be removed.
	 * 
	 * @throws StructureManagementException
	 *             If the SubjectGroup could not be found.
	 */
	public void deleteSubjectGroup(Long idSubjectGroup) throws StructureManagementException {

		SubjectGroup subjectGroup;
		try {
			subjectGroup = subjectGroupDAO.findById(idSubjectGroup);
			subjectGroupDAO.delete(subjectGroup);
		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("SubjectGroups could not be found. Cause: " + e.getMessage());
			throw ex;
		}
	}

	/**
	 * This method updates a SubjectGroup.
	 * 
	 * @param subjectGroup
	 *            The SubjectGroup to be updated.
	 * 
	 * @return the updated subject group
	 * 
	 * @throws StructureManagementException
	 *             If the SubjectGroup could not be updated.
	 */
	public SubjectGroup updateSubjectGroup(SubjectGroup subjectGroup) throws StructureManagementException {

		SubjectGroup result;

		try {
			result = subjectGroupDAO.update(subjectGroup);

		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("SubjectGroups could not be updated. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}
}
