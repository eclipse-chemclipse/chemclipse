/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.Base64;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.List;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.io.WebNucleotideBLAST;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Downloads the preformatted NCBI BLAST databases like update_blastdb.pl does,
 * see https://ftp.ncbi.nlm.nih.gov/blast/documents/blastdb.html
 */
public class BlastDatabaseDownloader {

	private static final Logger logger = Logger.getLogger(BlastDatabaseDownloader.class);

	private static final String METADATA = "blastdb-metadata-1-1.json";
	private static final String METADATA_NUCLEOTIDE = "-nucl-metadata.json";
	private static final String LATEST_DIR = "latest-dir";
	private static final String TYPE_NUCLEOTIDE = "Nucleotide";
	private static final String TAXDB = "taxdb";
	private static final String MD5 = ".md5";
	private static final String INDEX = ".ndb";
	private static final String INDEX_TAXDB = ".btd";
	private static final int BUFFER_SIZE = 64 * 1024;
	private static final long PROGRESS_INTERVAL = 1000L;

	private static final ObjectMapper objectMapper = new ObjectMapper();

	private BlastDatabaseDownloader() {

	}

	public static List<BlastDatabase> getNucleotideDatabases(Mirror mirror) throws IOException {

		try (CloseableHttpClient client = createClient()) {
			String location = mirror.url();
			if(mirror.isCloud()) {
				location += client.execute(createRequest(location + LATEST_DIR), new BasicHttpClientResponseHandler()).trim() + "/";
			}
			String json = client.execute(createRequest(location + METADATA), new BasicHttpClientResponseHandler());
			List<BlastDatabase> databases = objectMapper.readValue(json, new TypeReference<List<BlastDatabase>>() {
			});
			return databases.stream() //
							.filter(d -> TYPE_NUCLEOTIDE.equals(d.type()) || TAXDB.equals(d.name())) //
							.sorted(Comparator.comparing(BlastDatabase::name, String.CASE_INSENSITIVE_ORDER)) //
							.toList();
		}
	}

	public static InstallationStatus getStatus(BlastDatabase database, File folder) {

		File metadata = new File(folder, database.name() + METADATA_NUCLEOTIDE);
		if(metadata.exists()) {
			try {
				/*
				 * Cloud mirrors lag a few days behind the FTP mirrors. ISO dates compare as text.
				 */
				BlastDatabase installed = objectMapper.readValue(metadata, BlastDatabase.class);
				if(database.lastUpdated() != null && installed.lastUpdated() != null && installed.lastUpdated().compareTo(database.lastUpdated()) >= 0) {
					return InstallationStatus.UP_TO_DATE;
				}
			} catch(IOException e) {
				logger.warn(e);
			}
			return InstallationStatus.UPDATE_AVAILABLE;
		}
		/*
		 * update_blastdb.pl --decompress keeps neither the metadata nor the checksums,
		 * but the extracted files carry the modification time of the archive entries.
		 */
		File index = new File(folder, database.name() + (TAXDB.equals(database.name()) ? INDEX_TAXDB : INDEX));
		if(!index.exists()) {
			return InstallationStatus.NOT_INSTALLED;
		}
		if(database.lastUpdated() != null) {
			try {
				LocalDate installed = LocalDate.ofInstant(Instant.ofEpochMilli(index.lastModified()), ZoneOffset.UTC);
				LocalDate available = LocalDateTime.parse(database.lastUpdated()).toLocalDate();
				if(!installed.isBefore(available)) {
					return InstallationStatus.UP_TO_DATE;
				}
			} catch(DateTimeParseException e) {
				logger.warn(e);
			}
		}
		return InstallationStatus.UPDATE_AVAILABLE;
	}

	/**
	 * Downloads, verifies and extracts all volumes of the database into the folder.
	 * Volumes whose checksum did not change since the last download are skipped.
	 * Cloud mirrors offer the decompressed files without checksum files, so they are always downloaded.
	 */
	public static void download(BlastDatabase database, File folder, Mirror mirror, IProgressMonitor monitor) throws IOException {

		SubMonitor subMonitor = SubMonitor.convert(monitor, "Download " + database.name(), database.files().size());
		Files.createDirectories(folder.toPath());
		try (CloseableHttpClient client = createClient()) {
			for(String file : database.files()) {
				subMonitor.checkCanceled();
				String url = mirror.resolve(file);
				String fileName = url.substring(url.lastIndexOf('/') + 1);
				subMonitor.subTask(fileName);
				if(mirror.isCloud()) {
					downloadFile(client, url, fileName, folder.toPath(), subMonitor.split(1));
					continue;
				}
				String checksum = getChecksum(client, url + MD5);
				File checksumFile = new File(folder, fileName + MD5);
				if(checksumFile.exists() && checksum.equals(readChecksum(checksumFile))) {
					subMonitor.worked(1);
					continue;
				}
				downloadVolume(client, url, fileName, checksum, folder.toPath(), subMonitor.split(1));
				Files.writeString(checksumFile.toPath(), checksum + "  " + fileName + "\n", StandardCharsets.US_ASCII);
			}
		}
		objectMapper.writeValue(new File(folder, database.name() + METADATA_NUCLEOTIDE), database);
	}

	private static void downloadVolume(CloseableHttpClient client, String url, String fileName, String checksum, Path folder, IProgressMonitor monitor) throws IOException {

		SubMonitor subMonitor = SubMonitor.convert(monitor, 10);
		Path archive = Files.createTempFile(folder, "blastdb", ".tar.gz");
		try {
			HttpGet request = createRequest(url);
			String actualChecksum = client.execute(request, response -> {
				if(response.getCode() != HttpStatus.SC_OK) {
					throw new IOException("Failed to download " + url + ": " + response.getCode() + " " + response.getReasonPhrase());
				}
				return save(request, response.getEntity(), archive, fileName, subMonitor.split(9));
			});
			if(!checksum.equalsIgnoreCase(actualChecksum)) {
				throw new IOException("Checksum mismatch for " + url);
			}
			subMonitor.subTask("Extract " + fileName);
			extract(archive, folder, subMonitor.split(1));
		} finally {
			Files.deleteIfExists(archive);
		}
	}

	/**
	 * Downloads next to the target first, so that an installed database stays usable until the file is complete.
	 */
	private static void downloadFile(CloseableHttpClient client, String url, String fileName, Path folder, IProgressMonitor monitor) throws IOException {

		Path download = Files.createTempFile(folder, "blastdb", ".part");
		try {
			HttpGet request = createRequest(url);
			client.execute(request, response -> {
				if(response.getCode() != HttpStatus.SC_OK) {
					throw new IOException("Failed to download " + url + ": " + response.getCode() + " " + response.getReasonPhrase());
				}
				String checksum = getCloudChecksum(response);
				String actualChecksum = save(request, response.getEntity(), download, fileName, monitor);
				if(checksum != null && !checksum.equalsIgnoreCase(actualChecksum)) {
					throw new IOException("Checksum mismatch for " + url);
				}
				return null;
			});
			Files.move(download, folder.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
		} finally {
			Files.deleteIfExists(download);
		}
	}

	/**
	 * Google Cloud Storage sends the MD5 as x-goog-hash: md5=base64. Amazon S3 sends it as ETag,
	 * except for multipart uploads, whose ETag ends in -parts and is no MD5.
	 */
	private static String getCloudChecksum(HttpResponse response) {

		for(Header header : response.getHeaders("x-goog-hash")) {
			for(String hash : header.getValue().split(",")) {
				String value = hash.trim();
				if(value.startsWith("md5=")) {
					return HexFormat.of().formatHex(Base64.getDecoder().decode(value.substring(4)));
				}
			}
		}
		Header eTag = response.getFirstHeader("ETag");
		if(eTag != null) {
			String value = eTag.getValue().replace("\"", "");
			if(value.matches("[0-9a-fA-F]{32}")) {
				return value;
			}
		}
		return null;
	}

	private static String save(HttpGet request, HttpEntity entity, Path archive, String fileName, IProgressMonitor monitor) throws IOException {

		long size = entity.getContentLength();
		SubMonitor subMonitor = SubMonitor.convert(monitor, (int)Math.min(Integer.MAX_VALUE, Math.max(1, size / 1024)));
		MessageDigest messageDigest = getMessageDigest();
		try (InputStream inputStream = new DigestInputStream(entity.getContent(), messageDigest);
				OutputStream outputStream = Files.newOutputStream(archive)) {
			byte[] buffer = new byte[BUFFER_SIZE];
			long bytes = 0;
			long reported = 0;
			long intervalStart = System.currentTimeMillis();
			long intervalBytes = 0;
			int read;
			while((read = inputStream.read(buffer)) != -1) {
				if(subMonitor.isCanceled()) {
					/*
					 * Abort the connection, otherwise closing the stream reads the remaining content.
					 */
					request.cancel();
					throw new OperationCanceledException();
				}
				outputStream.write(buffer, 0, read);
				bytes += read;
				subMonitor.worked((int)(bytes / 1024 - reported));
				reported = bytes / 1024;
				long elapsed = System.currentTimeMillis() - intervalStart;
				if(elapsed >= PROGRESS_INTERVAL) {
					long speed = (bytes - intervalBytes) * 1000L / elapsed;
					subMonitor.subTask(fileName + ": " + formatSize(bytes) + " of " + formatSize(size) + " (" + formatSize(speed) + "/s)");
					intervalStart += elapsed;
					intervalBytes = bytes;
				}
			}
		}
		return HexFormat.of().formatHex(messageDigest.digest());
	}

	public static String formatSize(long bytes) {

		if(bytes >= 1_000_000_000L) {
			return String.format("%.1f GB", bytes / 1e9);
		}
		return String.format("%.1f MB", bytes / 1e6);
	}

	private static void extract(Path archive, Path folder, IProgressMonitor monitor) throws IOException {

		SubMonitor subMonitor = SubMonitor.convert(monitor, IProgressMonitor.UNKNOWN);
		Path root = folder.toAbsolutePath().normalize();
		try (TarArchiveInputStream tarInputStream = new TarArchiveInputStream(new GzipCompressorInputStream(new BufferedInputStream(Files.newInputStream(archive))))) {
			TarArchiveEntry entry;
			while((entry = tarInputStream.getNextEntry()) != null) {
				if(!entry.isFile()) {
					continue;
				}
				Path target = root.resolve(entry.getName()).normalize();
				if(!target.startsWith(root)) {
					throw new IOException("Invalid archive entry: " + entry.getName());
				}
				Files.createDirectories(target.getParent());
				try (OutputStream outputStream = Files.newOutputStream(target)) {
					byte[] buffer = new byte[BUFFER_SIZE];
					int read;
					while((read = tarInputStream.read(buffer)) != -1) {
						subMonitor.checkCanceled();
						outputStream.write(buffer, 0, read);
					}
				}
				Files.setLastModifiedTime(target, entry.getLastModifiedTime());
			}
		}
	}

	private static String getChecksum(CloseableHttpClient client, String url) throws IOException {

		return parseChecksum(client.execute(createRequest(url), new BasicHttpClientResponseHandler()));
	}

	private static String readChecksum(File file) {

		try {
			return parseChecksum(Files.readString(file.toPath(), StandardCharsets.US_ASCII));
		} catch(IOException e) {
			logger.warn(e);
			return "";
		}
	}

	/**
	 * md5sum format: "checksum filename"
	 */
	private static String parseChecksum(String content) {

		return content.trim().split("\\s+")[0].toLowerCase();
	}

	private static MessageDigest getMessageDigest() throws IOException {

		try {
			return MessageDigest.getInstance("MD5");
		} catch(NoSuchAlgorithmException e) {
			throw new IOException(e);
		}
	}

	/**
	 * Mirrors like FUNET send "Content-Encoding: x-gzip" for every file with .gz in its name,
	 * which must not be decompressed as the checksums are calculated for the archive.
	 */
	private static CloseableHttpClient createClient() {

		return HttpClients.custom().disableContentCompression().build();
	}

	private static HttpGet createRequest(String url) {

		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("User-Agent", WebNucleotideBLAST.getUserAgent());
		return httpGet;
	}
}
