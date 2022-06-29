package edu.kit.exp.server.microeconomicsystem;

/**
 * This class is used to create environment objects of different
 * {@link edu.kit.exp.server.microeconomicsystem.Environment Environment}
 * subclasses.
 * 
 * @see Environment
 */
public class EnvironmentFactory {

	/**
	 * This constructor instantiates a new environment factory.
	 */
	private EnvironmentFactory() {

	}

	/**
	 * This method is a factory method to create an Environment.
	 * 
	 * @param <T>
	 *            A generic variable which extends Environment.
	 * @param environmentFactoryKey
	 *            A String variable which contains the unique factory key of the
	 *            Environment to be created.
	 * @return the environment
	 * @see Environment
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Environment> T createEnvironment(String environmentFactoryKey) {
		try {
			return (T) (Class.forName(environmentFactoryKey).getConstructor().newInstance());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
