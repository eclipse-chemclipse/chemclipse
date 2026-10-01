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
package org.eclipse.chemclipse.msd.report.txt.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.chemclipse.model.identifier.IComparisonResult;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.IStandaloneMassSpectrum;
import org.eclipse.chemclipse.support.text.ValueFormat;

public class StandaloneMassSpectrumReportWriter {

	private static final String NO_VALUE = "--";

	private SimpleDateFormat dateFormat = ValueFormat.getDateFormatEnglish(ValueFormat.FULL_DATE_PATTERN);
	private DecimalFormat decimalFormat = ValueFormat.getDecimalFormatEnglish("0.0##");

	public void generate(File file, boolean append, IMassSpectra massSpectra) throws IOException {

		try (PrintWriter printWriter = new PrintWriter(new FileWriter(file, append))) {
			for(IScanMSD massSpectrum : massSpectra.getList()) {
				reportMassSpectrum(printWriter, massSpectrum);
				printWriter.println("");
			}
		}
	}

	private void reportMassSpectrum(PrintWriter printWriter, IScanMSD massSpectrum) {

		if(massSpectrum instanceof IStandaloneMassSpectrum standaloneMassSpectrum) {
			printMetadata(printWriter, standaloneMassSpectrum);
		}
		printTargets(printWriter, massSpectrum);
	}

	private void printMetadata(PrintWriter printWriter, IStandaloneMassSpectrum massSpectrum) {

		printLine(printWriter, "Name", massSpectrum.getName());
		printLine(printWriter, "File", getFileValue(massSpectrum.getFile()));
		printLine(printWriter, "Sample Name", massSpectrum.getSampleName());
		printLine(printWriter, "Plate", massSpectrum.getPlate());
		printLine(printWriter, "Position", massSpectrum.getPosition());
		printLine(printWriter, "Description", massSpectrum.getDescription());
		printLine(printWriter, "Operator", massSpectrum.getOperator());
		printLine(printWriter, "Date", getDateValue(massSpectrum.getDate()));
		printLine(printWriter, "Instrument", massSpectrum.getInstrument());
	}

	private void printLine(PrintWriter printWriter, String key, String value) {

		printWriter.print(key);
		printWriter.print(": ");
		printWriter.println(value == null || value.isEmpty() ? NO_VALUE : value);
	}

	private String getFileValue(File file) {

		return file != null ? file.getAbsolutePath() : NO_VALUE;
	}

	private String getDateValue(Date date) {

		return date != null ? dateFormat.format(date) : NO_VALUE;
	}

	private void printTargets(PrintWriter printWriter, IScanMSD massSpectrum) {

		for(IIdentificationTarget identificationTarget : massSpectrum.getTargets()) {
			ILibraryInformation libraryInformation = identificationTarget.getLibraryInformation();
			if(libraryInformation == null) {
				continue;
			}
			printLine(printWriter, "Identification", libraryInformation.getName());
			printComparisonResult(printWriter, identificationTarget.getComparisonResult());
			printLine(printWriter, "Database", libraryInformation.getDatabase());
			printLine(printWriter, "Identifier", identificationTarget.getIdentifier());
			printLine(printWriter, "Contributor", libraryInformation.getContributor());
		}
	}

	private void printComparisonResult(PrintWriter printWriter, IComparisonResult comparisonResult) {

		if(comparisonResult == null) {
			return;
		}
		printLine(printWriter, "Match Factor", decimalFormat.format(comparisonResult.getMatchFactor()));
	}
}
