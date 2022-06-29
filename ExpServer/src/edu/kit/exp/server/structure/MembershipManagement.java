package edu.kit.exp.server.structure;

import java.util.List;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.dao.DefaultDAO;
import edu.kit.exp.server.jpa.entity.Membership;

/**
 * This class provides all persistence functions of memberships.</br> A
 * Membership shows the role of a Subject in a SubjectGroup.
 * 
 */
public class MembershipManagement {

	/** The instance. */
	private static MembershipManagement instance;

	/** The membership dao. */
	private DefaultDAO<Membership> membershipDAO = new DefaultDAO<>(Membership.class);

	/**
	 * This method returns an instance of the MembershipManagement.
	 * 
	 * @return a single instance of MembershipManagement
	 */
	public static MembershipManagement getInstance() {

		if (instance == null) {
			instance = new MembershipManagement();
		}

		return instance;
	}

	/**
	 * This constructor instantiates a new membership management.
	 */
	private MembershipManagement() {

	}

	/**
	 * This method finds a membership in the DB.
	 * 
	 * @param membershipId
	 *            A Long variable which contains the ID of a membership.
	 * 
	 * @return the requested membership
	 * 
	 * @throws StructureManagementException
	 *             If a Membership could not be found.
	 */
	public Membership findMembership(Long membershipId) throws StructureManagementException {

		Membership membership;

		try {
			membership = membershipDAO.findById(membershipId);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Membership could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return membership;
	}

	/**
	 * This method creates a new memberships for the given TreatmentBlock.
	 * 
	 * @param membership
	 *            the Membership to be created.
	 * 
	 * @return the membership
	 * 
	 * @throws StructureManagementException
	 *             If the Membership could not be created.
	 */
	public Membership createNewMemberships(Membership membership) throws StructureManagementException {

		Membership result;

		try {
			result = membershipDAO.create(membership);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Membership could not be created. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}

	/**
	 * This method gets all Memberships from the DB.
	 * 
	 * @return a list of all the Memberships
	 * 
	 * @throws StructureManagementException
	 *             If no Memberships were found.
	 */
	public List<Membership> findAllMemberships() throws StructureManagementException {

		List<Membership> list;
		try {
			list = membershipDAO.findAll();
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Memberships could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return list;
	}

	/**
	 * This method removes a membership from the DB.
	 * 
	 * @param idMembership
	 *            A Long which contains the Membership to be removed.
	 * 
	 * @throws StructureManagementException
	 *             If the Membership could not be deleted.
	 */
	public void deleteMembership(Long idMembership) throws StructureManagementException {

		Membership membership;
		try {
			membership = membershipDAO.findById(idMembership);
			membershipDAO.delete(membership);
		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Membership could not be deleted. Cause: " + e.getMessage());
			throw ex;
		}
	}

	/**
	 * This method updates a given Membership.
	 * 
	 * @param membership
	 *            the Membership to be updated
	 * 
	 * @return the membership after updating it
	 * 
	 * @throws StructureManagementException
	 *             If the membership could not be updated.
	 */
	public Membership updateMembership(Membership membership) throws StructureManagementException {

		Membership result;
		try {
			result = membershipDAO.update(membership);

		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Membership could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}
}
