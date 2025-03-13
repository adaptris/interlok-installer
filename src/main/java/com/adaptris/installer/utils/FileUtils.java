package com.adaptris.installer.utils;

import com.adaptris.installer.helpers.LogHelper;
import org.gradle.internal.impldep.org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * File Utility class hosting methods related to File related classes
 *
 */
public class FileUtils {

    private static LogHelper log = LogHelper.getInstance();
    private FileUtils() {}

    /**
     * Reads content from files embedded in Jar file matching with the search keys passed into the method.
     *
     * @param jarFilePath
     * @param filePathInJar
     * @param searchKeys
     *
     * @return
     * @throws IOException
     */
    public static String readFileFromJar(String jarFilePath, String filePathInJar, String... searchKeys) throws IOException {
        String line;
        StringBuilder content = new StringBuilder();
        try (JarFile jarFile = new JarFile(jarFilePath)) {
            // Get the JAR entry for the specified file
            JarEntry entry = jarFile.getJarEntry(filePathInJar);
            if (entry != null) {
                // Open an input stream to read the file
                try (InputStream inputStream = jarFile.getInputStream(entry);
                     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    // Read the file line by line
                    while ((line = reader.readLine()) != null) {
                        //If found starting with searchKey then append the value in the line
                        for(String searchKey : searchKeys)
                            if (line.startsWith(searchKey)) {
                                content.append(line.trim());
                                content.append(",");
                            }
                    }
                }
            }
        }
        return content.toString();
    }

    /**
     * Filters list of Jar files to return selected Jars based on matched contained file names
     *
     * @param jarFiles
     * @param fileNamePattern
     *
     * @return list of matched jar files
     */
    public static List<File> filterJarsByContainedFile(List<File> jarFiles, String fileNamePattern) {
        List<File> filteredJars = new ArrayList<>();

        for (File jarFile : jarFiles) {
            try (JarFile jar = new JarFile(jarFile)) {
                Enumeration<JarEntry> entries = jar.entries();

                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String entryName = entry.getName();

                    //Check if directory and file name matches
                    if (!entry.isDirectory() && entryName.contains(fileNamePattern)) {
                        filteredJars.add(jarFile);
                        break;
                    }
                }
            } catch (IOException e) {
                log.info("Error processing JAR file: " + jarFile.getName());
                log.info(e.getMessage());
            }
        }
        return filteredJars;
    }

    /**
     * Gets list of all files under a directory with an extension value, eg. .jar, .war, .java files
     *
     * @param directoryPath
     * @param extension
     *
     * @return List<File>
     */
    public static List<File> getFilesListFromDirectory(String directoryPath, String extension) {
        List<File> filesList = new ArrayList<>();
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(extension));
            if (files != null) {
                filesList.addAll(Arrays.asList(files));
            }
        }

        return filesList;
    }
}
