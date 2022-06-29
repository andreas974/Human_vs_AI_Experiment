package edu.kit.exp.common;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Properties;

/**
 * A Class for constants used by client and server.
 */
public class Constants {

	/**
	 * The Constant SERVER_PORT.
	 */
	public static final Integer SERVER_PORT = 7778;

	/**
	 * The Constant SERVER_RMI_OBJECT_NAME.
	 */
	public static final String SERVER_RMI_OBJECT_NAME = "ServerCommunicationObject";

	/**
	 * The Constant TIME_STAMP_FORMAT.
	 */
	public static final String TIME_STAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	
	/**
	 * The Constant BROWNIE_VERSION.
	 */
	public static final Double BROWNIE_VERSION = 1.0;
	
	/**
	 * The Constant IISM_HOMEPAGE_URL.
	 */
	public static final String IISM_HOMEPAGE_URL = "https://im.iism.kit.edu/";

	/**
	 * The Constant COMPUTERNAME.
	 */
	private static String COMPUTERNAME = null;
	
	/* read from properties files */
	private static final String SERVER = "server";
	private static final String FILEDIR = "filedir";
	private static final String DEBUGMODE = "debugmode";
	
	/**
	 * The system debug mode.
	 */
	private static boolean systemDebugMode;

	/**
	 * The server name.
	 */
	private static String serverName;

	/**
	 * The file directory for the FileManager
	 */
	private static String fileDirectory = "";

	/**
	 * The File Prefix for Server files
	 */
	public static final String FILE_PREFIX_SERVER = "Server_";
	
	/**
	 * Get the debug state of the server.
	 *
	 * @return A boolean that indicates whether the server is in debug mode.
	 */
	public static boolean isSystemDebugMode() {
		return systemDebugMode;
	}

	/**
	 * Get the server name.
	 *
	 * @return A String which contains the server name.
	 */
	public static String getServerName() {
		return serverName;
	}

	public static String getFileDirectory() {
		return fileDirectory;
	}
	
	public static String getComputername() {
		if (COMPUTERNAME == null) {
            try {
                COMPUTERNAME = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                LogHandler.printException(e);
            }
        }
	    return COMPUTERNAME;
	}

	static {
		addSystemProperties(new String[] { "META-INF/systemdefault.properties", "META-INF/system.properties" });
	}

	/**
	 * Sets the server name and the state of the debug mode.
	 *
	 * @param propertyFiles a String[] which contains property files.
	 * @see #loadProperties(String[])
	 */
	public static void addSystemProperties(String[] propertyFiles) {
		Properties systemProperties = loadProperties(propertyFiles);

		if (systemProperties.containsKey(DEBUGMODE))
			systemDebugMode = Boolean.valueOf((String) systemProperties.get(DEBUGMODE));
		if (systemProperties.containsKey(SERVER))
			serverName = systemProperties.getProperty(SERVER);
		if (systemProperties.containsKey(FILEDIR))
			fileDirectory = systemProperties.getProperty(FILEDIR);
	}

	/**
	 * * Reads multiple property files parsed by <code>propertyFiles</code>.
	 * First file read first. Identically named properties are overwritten by
	 * later read property files.
	 *
	 * @param propertyFiles a String[] which contains property files.
	 * @return Properties read out of property files.
	 */
	public static Properties loadProperties(String[] propertyFiles) {
		Properties returnProperties = new Properties();
		for (String propertyFile : propertyFiles) {
			try {
				Properties tmpProperties = new Properties();
				tmpProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(propertyFile));
				for (Iterator<Object> iterator = tmpProperties.keySet().iterator(); iterator.hasNext(); ) {
					String key = (String) iterator.next();
					returnProperties.put(key, tmpProperties.get(key));
				}
			} catch (Exception e) {
				System.out.println("Property file not found: " + propertyFile);
			}
		}

		return returnProperties;
	}
}