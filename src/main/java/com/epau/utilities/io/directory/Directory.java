package com.epau.utilities.io.directory;


import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.IO.println;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.isDirectory;
import static java.nio.file.Files.newDirectoryStream;
import static java.nio.file.Files.walk;
import static java.util.logging.Logger.getLogger;
import static java.util.stream.Collectors.toSet;

public class Directory {

	private final Logger log = getLogger(Directory.class.getName());

	private final Path directory;

	public Directory(Path directory) {
		this.directory = directory;

		if (!isDirectory(directory)) {
			throw new IllegalArgumentException("The specified path is not a directory");
		}
		if (!exists(directory)) {
			throw new IllegalArgumentException("The specified directory does not exist: '" + directory + "'");
		}
	}

	/// Gets all files and directories below the specified directory.
	///
	/// **Notes:**
	/// - Directories have trailing slashes.
	/// - The starting directory itself is not included.
	/// - Returned paths are relative to the specified directory and do not start with a slash.
	///
	/// @return all files and directories below the specified directory
	/// @throws IOException if an I/O error occurs while traversing the directory
	public Set<String> getDescendants() throws IOException {
		try (var descendants = walk(directory)) {
			return descendants.map(file -> {
				String path = directory.relativize(file).toString();
				if (isDirectory(file) && !path.isEmpty()) {
					path += "/";
				}
				return path;
			}).filter(path -> !path.isEmpty()).collect(toSet());
		}
	}

	public boolean isEmpty() {
		try (var entries = newDirectoryStream(directory)) {
			return !entries.iterator().hasNext();
		} catch (IOException e) {
			log.log(Level.WARNING, "Could not check if directory '" + directory + "' is empty", e);
			return false;
		}
	}

	// For testing
	static void main() throws IOException {
		var path      = Path.of("");
		var directory = new Directory(path);
		println("Path: " + path);
		println("Descendants ↓");
		directory.getDescendants().forEach(IO::println);
		println("Empty? → " + directory.isEmpty());
	}
}