/*******************************************************************************
 * Copyright (c) 2022, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.converter.supplier.zip.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.eclipse.chemclipse.container.definition.IFileContentProvider;
import org.eclipse.chemclipse.container.supplier.zip.internal.PathHelper;
import org.eclipse.chemclipse.logging.core.Logger;

public class ZipContainer implements IFileContentProvider {

	private static final int BUFFER = 2048;
	private static final Logger logger = Logger.getLogger(ZipContainer.class);

	@Override
	public long getContentSize(File file) {

		try (ZipFile zipFile = new ZipFile(file)) {
			return zipFile.size();
		} catch(ZipException e) {
			logger.warn(e);
		} catch(IOException e) {
			logger.warn(e);
		}
		return 0;
	}

	@Override
	public File[] getContents(File file) {

		Set<File> contents = new LinkedHashSet<>();
		try (ZipFile zipFile = new ZipFile(file)) {
			Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
			File destinationDirectory = new File(PathHelper.getStoragePathImport(), file.getName());
			destinationDirectory.mkdirs();
			Path destinationPath = destinationDirectory.toPath().toAbsolutePath().normalize();
			while(zipEntries.hasMoreElements()) {
				ZipEntry zipEntry = zipEntries.nextElement();
				Path targetPath = getTargetPath(destinationPath, zipEntry);
				if(targetPath == null) {
					continue;
				}
				if(zipEntry.isDirectory()) {
					targetPath.toFile().mkdirs();
				} else {
					try (InputStream zipInputStream = zipFile.getInputStream(zipEntry)) {
						extractFile(targetPath.toFile(), zipInputStream);
					}
				}
				File topLevelEntry = getTopLevelEntry(destinationPath, targetPath);
				if(topLevelEntry != null) {
					contents.add(topLevelEntry);
				}
			}
		} catch(IOException e) {
			logger.warn(e);
		}
		return contents.toArray(new File[0]);
	}

	private Path getTargetPath(Path destinationPath, ZipEntry zipEntry) {

		Path targetPath;
		try {
			targetPath = destinationPath.resolve(zipEntry.getName()).normalize();
		} catch(InvalidPathException e) {
			logger.warn("Skipped zip entry with an invalid name: " + zipEntry.getName());
			return null;
		}
		if(!targetPath.startsWith(destinationPath)) {
			logger.warn("Skipped zip entry outside of the destination directory: " + zipEntry.getName());
			return null;
		}
		return targetPath;
	}

	private File getTopLevelEntry(Path destinationPath, Path targetPath) {

		Path relativePath = destinationPath.relativize(targetPath);
		if(relativePath.getNameCount() == 0) {
			return null;
		}
		return destinationPath.resolve(relativePath.getName(0)).toFile();
	}

	private void extractFile(File file, InputStream zipInputStream) {

		int count;
		byte[] data = new byte[BUFFER];
		File parentDirectory = file.getParentFile();
		if(parentDirectory != null) {
			parentDirectory.mkdirs();
		}
		try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
			try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream, BUFFER)) {
				while((count = zipInputStream.read(data, 0, BUFFER)) != -1) {
					bufferedOutputStream.write(data, 0, count);
				}
				bufferedOutputStream.flush();
			}
		} catch(FileNotFoundException e) {
			logger.warn(e);
		} catch(IOException e) {
			logger.warn(e);
		}
	}

	@Override
	public boolean hasContainerContents(File file) {

		try (ZipFile zipFile = new ZipFile(file)) {
			return zipFile.entries().hasMoreElements();
		} catch(ZipException e) {
			logger.warn(e);
		} catch(IOException e) {
			logger.warn(e);
		}
		return false;
	}
}
