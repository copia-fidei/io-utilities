package com.epau.utilities.io.file.tar.gz;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

public class TarGzFile {

	private static final Logger LOG = getLogger(TarGzFile.class.getName());

	private final InputStream input;

	public TarGzFile(InputStream input) { this.input = input; }

	public TarGzFile(URL input) throws IOException {
		this(input.openStream());
	}

	/**
	 * @return all archive entries
	 */
	public Set<String> getEntries() throws IOException {
		var entries = new HashSet<String>();
		try (var tarGzArchive = newTarGzArchiveInputStream()) {
			TarArchiveEntry entry;
			while ((entry = tarGzArchive.getNextEntry()) != null) {
				entries.add(entry.getName());
			}
		} catch (IOException e) {
			LOG.log(Level.WARNING, "Failed to read tar.gz file", e);
			throw e;
		}
		return entries;
	}

	/**
	 * @return all archive entries without the top level directory, e.g. "foo" instead of "my-app/foo".
	 * This is intended for archives with a single root directory.
	 */
	public Set<String> getEntriesWithoutTopLevelDirectory() throws IOException {
		var entries = new HashSet<String>();
		try (var tarGzArchive = newTarGzArchiveInputStream()) {
			TarArchiveEntry entry;
			while ((entry = tarGzArchive.getNextEntry()) != null) {
				entries.add(getEntryWithoutTopLevelDirectory(entry));
			}
		} catch (IOException e) {
			LOG.log(Level.WARNING, "Failed to read tar.gz file", e);
			throw e;
		}
		return entries;
	}

	private TarArchiveInputStream newTarGzArchiveInputStream() throws IOException {
		return new TarArchiveInputStream(new GzipCompressorInputStream(new BufferedInputStream(input)));
	}

	private static String getEntryWithoutTopLevelDirectory(TarArchiveEntry entry) {
		String name  = entry.getName();
		int    slash = name.indexOf('/');
		if (slash >= 0) {
			name = name.substring(slash + 1);
		}
		return name;
	}
}
