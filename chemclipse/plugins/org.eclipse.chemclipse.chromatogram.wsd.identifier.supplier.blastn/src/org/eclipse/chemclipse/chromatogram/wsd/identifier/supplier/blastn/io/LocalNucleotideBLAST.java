/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v1.BlastOutput;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v1.Hit;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v1.Hsp;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v1.Iteration;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.settings.IdentifierSettings;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.identifier.ComparisonResult;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.identifier.LibraryInformation;
import org.eclipse.chemclipse.model.implementation.IdentificationTarget;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.xml.sax.SAXException;

import jakarta.xml.bind.JAXBException;

public class LocalNucleotideBLAST {

	private static final Logger logger = Logger.getLogger(LocalNucleotideBLAST.class);

	public static void run(IChromatogramWSD chromatogram, IdentifierSettings settings) throws IOException, InterruptedException {

		File fasta = File.createTempFile(chromatogram.getSampleName() + "_", ".fsa");
		writeFASTA(chromatogram, fasta);
		File xml = File.createTempFile(chromatogram.getSampleName() + "_", ".xml");
		ProcessBuilder processBuilder = buildProcess(settings, fasta, xml);
		Process process = processBuilder.start();
		process.getErrorStream().transferTo(loggerErrorStream());
		int exitCode = process.waitFor();
		if(exitCode == 0) {
			try {
				transferTargets(chromatogram, xml);
			} catch(SAXException | IOException | JAXBException
					| ParserConfigurationException e) {
				logger.error(e);
				throw new IOException("Failed to read XML.");
			}
		} else {
			throw new IOException("blastn exited with errors.");
		}
	}

	private static void writeFASTA(IChromatogramWSD chromatogram, File fasta) throws FileNotFoundException {

		try (PrintWriter printWriter = new PrintWriter(fasta)) {
			printWriter.println("> " + chromatogram.getSampleName());
			printWriter.println(chromatogram.getMiscInfo());
			printWriter.flush();
		}
	}

	private static ProcessBuilder buildProcess(IdentifierSettings settings, File fasta, File xml) {

		ProcessBuilder processBuilder = new ProcessBuilder("blastn");
		processBuilder.command().add("-db");
		processBuilder.command().add(settings.getDatabase());
		processBuilder.command().add("-query");
		processBuilder.command().add(fasta.getAbsolutePath());
		processBuilder.command().add("-task");
		processBuilder.command().add(settings.getTask().value());
		processBuilder.command().add("-outfmt");
		processBuilder.command().add("5");
		processBuilder.command().add("-out");
		processBuilder.command().add(xml.getAbsolutePath());
		processBuilder.environment().put("BLASTDB", settings.getDatabaseFolder().getAbsolutePath());
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

	private static void transferTargets(IChromatogramWSD chromatogram, File xml) throws SAXException, IOException, JAXBException, ParserConfigurationException {

		BlastOutput blastOutput = XmlReaderVersion1.getBlastOutput(xml);
		for(Iteration iteration : blastOutput.getIterations().getIteration()) {
			for(Hit hit : iteration.getHits().getHit()) {
				ILibraryInformation libraryInformation = new LibraryInformation();
				libraryInformation.setName(hit.getDef());
				libraryInformation.setDatabase(blastOutput.getDb());
				libraryInformation.setMiscellaneous(hit.getId());
				libraryInformation.setComments(hit.getAccession());
				for(Hsp hsp : hit.getHsps().getHsp()) {
					ComparisonResult comparisionResult = new ComparisonResult((float)hsp.getBitScore(), (float)hsp.getScore(), (float)hsp.getEvalue(), hsp.getIdentity().floatValue()); // TODO: wrong model
					IdentificationTarget identificationTarget = new IdentificationTarget(libraryInformation, comparisionResult);
					identificationTarget.setIdentifier(blastOutput.getVersion());
					chromatogram.getTargets().add(identificationTarget);
				}
			}
		}
	}
}
