package edu.kit.exp.common;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.*;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.stream.Stream;

/**
 * A Class containing Methods used for returning names of classes extending a
 * specified class in a specified package.
 */

public class ReflectionPackageManager {

	private static final String CLASS_SUFFIX = ".class";
	private static final String JAR = "jar";
	private static final String JAR_FILE_PREFIX = "jar:file:";
	private static final String JAR_FILE_SUFFIX = "!/";

	/**
	 * gets all classes in the package that is specified in given path, returns
	 * the classes that extend given superclass
	 *
	 * @param path       path of the package which classes are checked
	 * @param superclass superclass, derivation from which is checked
	 * @return List containing qualified names without (path.)
	 */
	public static <T> List<Class<T>> getExtendingClasses(String path, Class<?> superclass) throws ClassNotFoundException, IOException, URISyntaxException {
		List<Class<T>> result = new ArrayList<>();
		@SuppressWarnings("unchecked") Class<T>[] implClasses = (Class<T>[]) getClasses(path);
		for (Class<T> currentClass : implClasses) {
			if (isNonAbstractExtending(currentClass, superclass)) {
				result.add(currentClass);
			}
		}
		return result;
	}

	/**
	 * gets all classes in the package that is specified in given path, returns
	 * qualified names without (path.) of classes that extend given superclass
	 *
	 * @param path       path of the package which classes are checked
	 * @param superclass superclass, derivation from which is checked
	 * @return List containing qualified names without (path.)
	 */
	public static List<String> getExtendingClassNames(String path, Class<?> superclass) throws URISyntaxException, IOException, ClassNotFoundException {
		List<String> result = new ArrayList<>();
		getExtendingClasses(path, superclass).forEach((Class<?> element) -> result.add(element.getName().substring(path.length() + 1)));
		return result;
	}

	/**
	 * recursively checks whether given currentClass can be derived from given
	 * superclass returns true if currentClass is not abstract and can be
	 * derived from superclass with (multiple) extends
	 *
	 * @param currentClass class to be checked for derivation from superclass
	 * @param superclass   superclass, derivation from which is checked
	 * @return boolean
	 */
	private static boolean isNonAbstractExtending(Class<?> currentClass, Class<?> superclass) {
		boolean result = false;
		if (currentClass.getSuperclass() != null) {
			if (superclass.isAssignableFrom(currentClass) && !Modifier.isAbstract(currentClass.getModifiers())) {
				result = true;
			} else {
				result = isNonAbstractExtending(currentClass.getSuperclass(), superclass);
			}
		}
		return result;
	}

	/**
	 * Scans a certain package for classes and returns them. Important is if the
	 * program is started as a jar file or from an IDE as the search process
	 * should be very different. The main reason is that within a jar file there
	 * is nothing like a folder structure.
	 *
	 * @param packageName
	 * @return classes
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @ref http://stackoverflow.com/questions/5193786/how-to-use-classloader-getresources-correctly/5194002#5194002
	 */
	private static Class<?>[] getClasses(String packageName) throws URISyntaxException, IOException, ClassNotFoundException {
		String packageNameWithSlash = packageName.replace('.', '/');
		URI uri = retrieveURIFromPackageName(packageNameWithSlash);
		List<Class<?>> classes = new ArrayList<Class<?>>();
		boolean isJAR = uri.getScheme().equals(JAR);
		if (isJAR) {
			classes.addAll(searchForClassesInJar(packageNameWithSlash, uri));
		} else {
			classes.addAll(searchForClassesInFolders(packageName, packageNameWithSlash));
		}
		return classes.toArray(new Class<?>[0]);
	}

	/**
	 * Retrieves the URI by using the package name
	 *
	 * @param packageNameWithSlash
	 * @return
	 * @throws URISyntaxException
	 */
	private static URI retrieveURIFromPackageName(String packageNameWithSlash) throws URISyntaxException {
		Thread currentThread = Thread.currentThread();
		ClassLoader classLoader = currentThread.getContextClassLoader();
		URL url = classLoader.getResource(packageNameWithSlash);
		URI uri = url.toURI();
		return uri;
	}

	/**
	 * Scans all classes accessible from the context class loader which belong
	 * to the given package and subpackages.
	 *
	 * @param packageNameWithSlash
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private static List<Class<?>> searchForClassesInFolders(String packageName, String packageNameWithSlash) throws IOException, ClassNotFoundException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		Enumeration<URL> resources = classLoader.getResources(packageNameWithSlash);
		List<File> dirs = new ArrayList<>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		List<Class<?>> classes = new ArrayList<>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		return classes;
	}

	/**
	 * Searching for class files in a jar works different therefore we use the features of FileSystems
	 * to create a tree structure for a recursive search of all class files in a certain path.
	 *
	 * @param mainPathAsString
	 * @param uri
	 * @return loaded classes
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws ClassNotFoundException
	 */
	private static List<Class<?>> searchForClassesInJar(String mainPathAsString, URI uri) throws IOException, URISyntaxException, ClassNotFoundException {
		FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
		Path mainPath = fileSystem.getPath(mainPathAsString);
		URLClassLoader classLoader = getUrlToJarFile();
		Stream<Path> walk = Files.walk(mainPath, Integer.MAX_VALUE);
		Iterator<Path> iterator = walk.iterator();
		List<Class<?>> classes = new ArrayList<Class<?>>();
		while (iterator.hasNext()) {
			Path localPath = iterator.next();
			String pathAsString = localPath.toString();
			boolean isClassFile = Files.isRegularFile(localPath) && pathAsString.endsWith(CLASS_SUFFIX);
			if (isClassFile) {
				pathAsString = processPath(pathAsString);
				classes.add(classLoader.loadClass(pathAsString));
			}
		}
		walk.close();
		fileSystem.close();
		return classes;
	}

	/**
	 * This method returns the URL class loader of the url of the jar file which is executed right now.
	 * The URL is created in a single-entry array because it is needed in this format
	 * by the URLclassloader
	 *
	 * @return the url class loader to the jar file of execution
	 * @throws URISyntaxException
	 * @throws MalformedURLException
	 */
	private static URLClassLoader getUrlToJarFile() throws URISyntaxException, MalformedURLException {
		ProtectionDomain protectionDomain = ReflectionPackageManager.class.getProtectionDomain();
		CodeSource codeSource = protectionDomain.getCodeSource();
		URL location = codeSource.getLocation();
		URI uri = location.toURI();
		String stringPath = uri.getPath();
		URL[] urls = new URL[] { new URL(JAR_FILE_PREFIX + stringPath + JAR_FILE_SUFFIX) };
		return URLClassLoader.newInstance(urls);
	}

	/**
	 * Removes the ".class" ending and leading slashes and backslashes.
	 * All other slash and backslash get replaced by points.
	 * Therefore the path has a fitting format for the classloader.
	 *
	 * @param pathAsString
	 * @return processed string
	 */
	private static String processPath(String pathAsString) {
		pathAsString = pathAsString.substring(0, pathAsString.length() - 6);
		pathAsString = pathAsString.replaceAll("^/+", "");
		pathAsString = pathAsString.replaceAll("^\\+", "");
		pathAsString = pathAsString.replace('/', '.');
		pathAsString = pathAsString.replace('\\', '.');
		return pathAsString;
	}

	/**
	 * Recursive method used to find all classes in a given directory and
	 * subdirs.
	 *
	 * @param directory   The base directory
	 * @param packageName The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(CLASS_SUFFIX)) {
				classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}
}