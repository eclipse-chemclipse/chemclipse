/*******************************************************************************
 * Copyright (c) 2025, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.io.BlastDatabaseCommandReader.BlastDatabaseInfo;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.BlastOutput2;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.settings.LocalIdentifierSettings;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.core.runtime.IProgressMonitor;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import jakarta.xml.bind.JAXBException;

public class LocalNucleotideBLAST extends AbstractNucleotideBLAST {

	private static final Logger logger = Logger.getLogger(LocalNucleotideBLAST.class);

	public static int run(IChromatogramWSD chromatogram, LocalIdentifierSettings settings, IProgressMonitor monitor) throws IOException, InterruptedException {

		File fasta = File.createTempFile(chromatogram.getSampleName() + "_", ".fsa");
		writeFASTA(chromatogram, fasta);
		File xml = File.createTempFile(chromatogram.getSampleName() + "_", ".xml");
		int numberOfHits = runBLAST(chromatogram, fasta, xml, settings, monitor);
		return numberOfHits;
	}

	private static int runBLAST(IChromatogramWSD chromatogram, File fasta, File xml, LocalIdentifierSettings settings, IProgressMonitor monitor) throws IOException, InterruptedException {

		int numberOfHits = 0;
		BlastDatabaseInfo databaseInfo = readDatabaseInfo(settings);
		if(databaseInfo == null) {
			throw new IOException("Failed to read database information.");
		}
		monitor.beginTask("Query Database", databaseInfo.volumes().size());
		for(String volume : databaseInfo.volumes()) {
			if(monitor.isCanceled()) {
				return numberOfHits;
			}
			ProcessBuilder processBuilder = buildProcessBLAST(volume, databaseInfo.totalBases(), settings, fasta, xml);
			Process process = processBuilder.start();
			process.getErrorStream().transferTo(loggerErrorStream());
			int exitCode = process.waitFor();
			if(exitCode == 0) {
				try {
					InputSource inputSource = new InputSource(new FileInputStream(xml));
					BlastOutput2 blastOutput = XmlReaderVersion2.getBlastOutput(inputSource);
					transferTargets(chromatogram, blastOutput);
					numberOfHits += XmlReaderVersion2.getNumberResults(blastOutput);
				} catch(SAXException | IOException | JAXBException
						| ParserConfigurationException e) {
					logger.error(e);
					throw new IOException("Failed to read XML.");
				}
			} else {
				throw new IOException("blastn exited with errors.");
			}
			monitor.worked(1);
		}
		return numberOfHits;
	}

	private static BlastDatabaseInfo readDatabaseInfo(LocalIdentifierSettings settings) throws IOException, InterruptedException {

		ProcessBuilder processBuilderDatabaseCommand = buildProcessDatabaseCommand(settings);
		Process process = processBuilderDatabaseCommand.start();
		process.getErrorStream().transferTo(loggerErrorStream());
		int exitCode = process.waitFor();
		if(exitCode == 0) {
			String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
			return BlastDatabaseCommandReader.parse(output);
		}
		return null;
	}

	private static ProcessBuilder buildProcessDatabaseCommand(LocalIdentifierSettings settings) {

		String pathPrefix = "";
		if(!PreferenceSupplier.getExecutableFolder().isEmpty() && new File(PreferenceSupplier.getExecutableFolder()).exists()) {
			pathPrefix = PreferenceSupplier.getExecutableFolder() + File.separator;
		}
		ProcessBuilder processBuilder = new ProcessBuilder(pathPrefix + "blastdbcmd");
		processBuilder.environment().put("BLASTDB", PreferenceSupplier.getDatabaseFolder());

		processBuilder.command().add("-db");
		processBuilder.command().add(settings.getDatabase());

		processBuilder.command().add("-info");
		processBuilder.command().add("-exact_length");

		return processBuilder;
	}

	private static void writeFASTA(IChromatogramWSD chromatogram, File fasta) throws FileNotFoundException {

		try (PrintWriter printWriter = new PrintWriter(fasta)) {
			printWriter.println("> " + chromatogram.getSampleName());
			printWriter.println(chromatogram.getMiscInfo());
			printWriter.flush();
		}
	}

	private static ProcessBuilder buildProcessBLAST(String volume, long totalBases, LocalIdentifierSettings settings, File fasta, File xml) {

		String pathPrefix = "";
		if(!PreferenceSupplier.getExecutableFolder().isEmpty() && new File(PreferenceSupplier.getExecutableFolder()).exists()) {
			pathPrefix = PreferenceSupplier.getExecutableFolder() + File.separator;
		}
		ProcessBuilder processBuilder = new ProcessBuilder(pathPrefix + "blastn");
		processBuilder.environment().put("BLASTDB", PreferenceSupplier.getDatabaseFolder());

		processBuilder.command().add("-db");
		processBuilder.command().add(volume);

		processBuilder.command().add("-query");
		processBuilder.command().add(fasta.getAbsolutePath());

		processBuilder.command().add("-task");
		processBuilder.command().add(settings.getTask().value());

		processBuilder.command().add("-dbsize");
		processBuilder.command().add(String.valueOf(totalBases));

		processBuilder.command().add("-outfmt");
		processBuilder.command().add("16");

		processBuilder.command().add("-out");
		processBuilder.command().add(xml.getAbsolutePath());

		return processBuilder;
	}

	private static OutputStream loggerErrorStream() {

		return new OutputStream() {

			@Override
			public void write(int b) throws IOException {

				logger.error(String.valueOf((char)b));
			}

			@Override
			public void write(byte[] b, int off, int len) throws IOException {

				logger.error(new String(b, off, len));
			}
		};
	}
}
