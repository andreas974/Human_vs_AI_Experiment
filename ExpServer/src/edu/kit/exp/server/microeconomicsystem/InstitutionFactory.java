package edu.kit.exp.server.microeconomicsystem;

import java.util.List;

import edu.kit.exp.server.communication.ServerMessageSender;
import edu.kit.exp.server.jpa.entity.Membership;

/**
 * Institution Factory Class - This Class is used to create Institution objects
 * of different {@link edu.kit.exp.server.microeconomicsystem.Institution
 * Institution} subclasses.
 * 
 * @see Institution
 */
public class InstitutionFactory {

	/**
	 * This constructor instantiates a new institution factory.
	 */
	private InstitutionFactory() {

	}

	/**
	 * This method is a factory method to create an Institution.
	 * 
	 * @param <T>
	 *            A generic variable which extends Institution and Environment.
	 * @param institutionFactoryKey
	 *            A String variable which contains the unique factory key of the
	 *            Institution to be created.
	 * @param environment
	 *            The Environment which is associated with the Institution.
	 * @param memberships
	 *            A {@link edu.kit.exp.server.jpa.entity.Membership Membership}
	 *            variable which contains the list of memberships for given
	 *            subjects.
	 * @param messageSender
	 *            A ServerMessageSender for sending messages to clients
	 * @param queueId
	 *            A String which represents the id of a message queue.
	 * 
	 * @return the new Institution
	 * 
	 * @see Institution
	 * @see SeverMessageSender
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Institution<? extends Environment>> T createInstitution(String institutionFactoryKey, Environment environment, List<Membership> memberships, ServerMessageSender messageSender, String queueId) {
		try {
			// return
			// (T)(Class.forName(institutionFactoryKey).getConstructor(Environment.class,
			// List.class, ServerMessageSender.class,
			// String.class).newInstance(environment, memberships,
			// messageSender, queueId));
			// return
			// (T)(Class.forName(institutionFactoryKey).getConstructor(??GenericParameter??,
			// List.class, ServerMessageSender.class,
			// String.class).newInstance(environment, memberships,
			// messageSender, queueId));
			return (T) (Class.forName(institutionFactoryKey).getConstructors()[0].newInstance(environment, memberships, messageSender, queueId));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
