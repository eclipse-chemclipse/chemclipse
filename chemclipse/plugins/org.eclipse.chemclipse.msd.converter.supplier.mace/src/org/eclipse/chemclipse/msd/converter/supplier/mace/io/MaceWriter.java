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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mace.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.eclipse.chemclipse.converter.exceptions.FileIsNotWriteableException;
import org.eclipse.chemclipse.converter.l10n.ConverterMessages;
import org.eclipse.chemclipse.model.identifier.IColumnIndexMarker;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.msd.converter.io.AbstractMassSpectraWriter;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IRegularLibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.implementation.MassSpectra;
import org.eclipse.chemclipse.support.model.SeparationColumnType;
import org.eclipse.core.runtime.IProgressMonitor;

public class MaceWriter extends AbstractMassSpectraWriter {

	private static final String CRLF = "\r\n";
	private static final int IONS_PER_LINE = 5;

	@Override
	public void write(File file, IScanMSD massSpectrum, boolean append, IProgressMonitor monitor) throws FileIsNotWriteableException, IOException {

		IMassSpectra massSpectra = new MassSpectra();
		massSpectra.addMassSpectrum(massSpectrum);
		write(file, massSpectra, append, monitor);
	}

	@Override
	public void write(File file, IMassSpectra massSpectra, boolean append, IProgressMonitor monitor) throws FileIsNotWriteableException, IOException {

		try (FileWriter fileWriter = new FileWriter(file, append)) {
			monitor.beginTask(ConverterMessages.writeMassSpectra, massSpectra.size());
			for(int i = 1; i <= massSpectra.size(); i++) {
				IScanMSD massSpectrum = massSpectra.getMassSpectrum(i);
				if(massSpectrum != null && !massSpectrum.isEmpty()) {
					writeMassSpectrum(fileWriter, massSpectrum);
				}
				monitor.worked(1);
			}
			monitor.done();
		}
	}

	private void writeMassSpectrum(FileWriter fileWriter, IScanMSD massSpectrum) throws IOException {

		ILibraryInformation libraryInformation = null;
		if(massSpectrum instanceof IRegularLibraryMassSpectrum libraryMassSpectrum) {
			libraryInformation = libraryMassSpectrum.getLibraryInformation();
		}
		String name = (libraryInformation != null) ? libraryInformation.getName() : massSpectrum.getIdentifier();
		fileWriter.write("Name: " + name + CRLF);
		if(libraryInformation != null) {
			String inchiKey = libraryInformation.getInChIKey();
			if(!inchiKey.isEmpty()) {
				fileWriter.write("InChIKey: " + inchiKey + CRLF);
			}
		}
		String retentionIndexLine = (libraryInformation != null) ? buildRetentionIndexLine(libraryInformation) : "";
		if(!retentionIndexLine.isEmpty()) {
			fileWriter.write("Retention_index: " + retentionIndexLine + CRLF);
		}
		if(libraryInformation != null) {
			String formula = libraryInformation.getFormula();
			if(!formula.isEmpty()) {
				fileWriter.write("Formula: " + formula + CRLF);
			}
			double molWeight = libraryInformation.getMolWeight();
			if(molWeight > 0) {
				fileWriter.write("MW: " + (int)molWeight + CRLF);
			}
			double exactMass = libraryInformation.getExactMass();
			if(exactMass > 0) {
				fileWriter.write("ExactMass: " + exactMass + CRLF);
			}
			String casNumber = libraryInformation.getCasNumber();
			if(!casNumber.isEmpty()) {
				fileWriter.write("CAS#: " + casNumber + CRLF);
			}
			fileWriter.write("DB#: " + Integer.toString(libraryInformation.getDatabaseIndex()) + CRLF);
			String comments = libraryInformation.getComments();
			if(!comments.isEmpty()) {
				fileWriter.write("Comments: " + comments + CRLF);
			}
			Set<String> synonyms = libraryInformation.getSynonyms();
			for(String synonym : synonyms) {
				if(!synonym.isEmpty()) {
					fileWriter.write("Synon: " + synonym + CRLF);
				}
			}
		}
		fileWriter.write("Num Peaks: " + massSpectrum.getNumberOfIons() + CRLF);
		fileWriter.write(getIonsMspec(massSpectrum));
		fileWriter.write(CRLF);
		fileWriter.flush();
	}

	private String buildRetentionIndexLine(ILibraryInformation libraryInformation) {

		StringBuilder builder = new StringBuilder();
		for(IColumnIndexMarker marker : libraryInformation.getColumnIndexMarkers()) {
			float retentionIndex = marker.getRetentionIndex();
			if(retentionIndex > 0) {
				String key = mapSeparationColumnType(marker.getSeparationColumn().getSeparationColumnType());
				if(key != null) {
					if(!builder.isEmpty()) {
						builder.append(" ");
					}
					builder.append(key);
					builder.append("=");
					builder.append((int)retentionIndex);
				}
			}
		}
		return builder.toString();
	}

	private String mapSeparationColumnType(SeparationColumnType columnType) {

		return switch(columnType) {
			case SEMI_POLAR -> "SemiStdNP";
			case NON_POLAR -> "StdNP";
			case POLAR -> "StdPolar";
			case DEFAULT -> "User";
		};
	}

	private String getIonsMspec(IScanMSD massSpectrum) {

		StringBuilder builder = new StringBuilder();
		List<IIon> ions = massSpectrum.getIons();
		int count = 0;
		for(IIon ion : ions) {
			builder.append((int)ion.getIon());
			builder.append(" ");
			builder.append((int)ion.getAbundance());
			builder.append("; ");
			count++;
			if(count % IONS_PER_LINE == 0) {
				builder.append(CRLF);
			}
		}
		if(count % IONS_PER_LINE != 0) {
			builder.append(CRLF);
		}
		return builder.toString();
	}
}
