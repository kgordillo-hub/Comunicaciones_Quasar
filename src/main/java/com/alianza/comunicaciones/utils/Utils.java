package com.alianza.comunicaciones.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import com.alianza.comunicaciones.exceptions.SystemException;

/**
 * Class which contains all utilities functions needed in the main program
 * 
 * @author ksgor
 *
 */
public final class Utils {

	private Utils() {
	}

	/**
	 * Method to read properties file
	 * 
	 * @param Properties name
	 * @param Property   name
	 * @return Property Value
	 * @throws SystemException, if cannot read the properties or property not found
	 */
	public static String readProperty(final String propertiesName, final String propertyName) throws SystemException {
		try (final InputStream input = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(propertiesName)) {
			Properties prop = new Properties();
			// load a properties file
			prop.load(input);
			final String value = prop.getProperty(propertyName);
			if (value != null) {
				return value;
			} else {
				throw new SystemException(Constants.PROPERTY_NOT_FOUND.replace("{p_name}", propertyName)
						.replace("{f_name}", propertiesName));
			}

		} catch (final IOException ex) {
			throw new SystemException(Constants.ERROR_READING_PROPERTIES.replace("{f_name}", propertiesName));
		} catch (final NullPointerException npe) {
			throw new SystemException(Constants.ERROR_READING_PROPERTIES.replace("{f_name}", propertiesName));
		}
	}

	/**
	 * Method to store a Java object in a file
	 * 
	 * @param object
	 * @param path
	 * @param fileName
	 * @throws SystemException, if something goes wrong
	 */
	public final static void saveObjectInFile(Object object, final String path, final String fileName)
			throws SystemException {
		final File directory = new File(path);
		directory.mkdirs();
		final String filePath = path.endsWith("/") ? path + fileName : path + "/" + fileName;
		if (!Files.exists(Paths.get(filePath))) {
			try (final FileOutputStream fos = new FileOutputStream(filePath);
					final ObjectOutputStream oos = new ObjectOutputStream(fos)) {
				oos.writeObject(object);
				oos.flush();
			} catch (final FileNotFoundException e) {
				throw new SystemException(Constants.FILE_NOT_FOUND.replace("{file_path}", filePath), e);
			} catch (final IOException e) {
				throw new SystemException("Exception while writting object in file path", e);

			}
		}
	}

	/**
	 * Method to read Java object from a File
	 * 
	 * @param path
	 * @param fileName
	 * @return The Object read from the file
	 * @throws SystemException, if something goes wrong
	 */
	public final static Object getObjectFromFile(final String path, final String fileName) throws SystemException {
		final String filePath = path.endsWith("/") ? path + fileName : path + "/" + fileName;
		if (Files.exists(Paths.get(filePath))) {
			try (FileInputStream fis = new FileInputStream(filePath);
					final ObjectInputStream ois = new ObjectInputStream(fis)) {
				return ois.readObject();
			} catch (FileNotFoundException e) {
				throw new SystemException(Constants.FILE_NOT_FOUND.replace("{file_path}", filePath), e);
			} catch (IOException e) {
				throw new SystemException(Constants.IO_EXCEPTION, e);
			} catch (ClassNotFoundException e) {
				throw new SystemException("Class not found", e);
			}
		} else {
			throw new SystemException(Constants.FILE_NOT_FOUND.replace("{file_path}", filePath));
		}
	}

	/**
	 * Method to sort files in a folder. Decent order
	 * 
	 * @param directoryPath
	 * @return the array of files in decent order
	 */
	public static File[] getSortedFiles(final String directoryPath) {
		final File directory = new File(directoryPath);
		directory.mkdirs();
		final File[] dataObjects = directory.listFiles();
		Arrays.sort(dataObjects, Comparator.comparingLong(File::lastModified));
		return dataObjects;
	}

	/**
	 * Method to move files to another folder
	 * 
	 * @param originPath
	 * @param targetPath
	 * @param fileNames, list of files to move
	 * @throws SystemException, if something fails
	 */
	public static void moveFiles(String originPath, String targetPath, final List<String> fileNames)
			throws SystemException {
		originPath = originPath.endsWith("/") ? originPath : originPath + "/";
		targetPath = targetPath.endsWith("/") ? targetPath : targetPath + "/";
		try {
			Files.createDirectories(Paths.get(targetPath));
			for (final String fileName : fileNames) {
				final Path sourcePath = Paths.get(originPath + fileName);
				final Path destinationePath = Paths.get(targetPath + fileName);
				Files.move(sourcePath, destinationePath, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (final IOException e) {
			throw new SystemException(Constants.IO_EXCEPTION, e);
		}
	}
}
