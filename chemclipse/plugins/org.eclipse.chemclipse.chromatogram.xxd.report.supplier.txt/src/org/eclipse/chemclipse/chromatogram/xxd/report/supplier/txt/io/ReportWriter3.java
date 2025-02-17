/*******************************************************************************
 * Copyright (c) 2019, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.report.supplier.txt.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.report.supplier.txt.settings.ReportSettings3;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.core.IPeakModel;
import org.eclipse.chemclipse.model.core.SignalSupport;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.quantitation.IQuantitationEntry;
import org.eclipse.chemclipse.support.text.ValueFormat;
import org.eclipse.core.runtime.IProgressMonitor;

public class ReportWriter3 {

	private static final String DELIMITER = "\t";
	//
	private DecimalFormat decimalFormatRetentionTime = ValueFormat.getDecimalFormatEnglish("0.00");
	private DecimalFormat decimalFormatAreaNormal = ValueFormat.getDecimalFormatEnglish("0.0#E0");
	private DecimalFormat decimalFormatConcentration = ValueFormat.getDecimalFormatEnglish("0.000");
	private DecimalFormat decimalFormatTraces = ValueFormat.getDecimalFormatEnglish("0");
	private DateFormat dateFormat = ValueFormat.getDateFormatEnglish();

	public void generate(File file, boolean append, List<IChromatogram> chromatograms, ReportSettings3 reportSettings, IProgressMonitor monitor) throws IOException {

		try (PrintWriter printWriter = new PrintWriter(new FileWriter(file, append))) {
			for(IChromatogram chromatogram : chromatograms) {
				if(reportSettings.isPrintHeader()) {
					printHeader(printWriter, chromatogram);
					printWriter.println("");
				}
				printQuantitationResults(printWriter, chromatogram, reportSettings);
				printWriter.println("");
			}
			printWriter.flush();
		}
	}

	private void printHeader(PrintWriter printWriter, IChromatogram chromatogram) {

		printWriter.println("Filename: " + chromatogram.getName());
		printWriter.println("Sample Name: " + chromatogram.getDataName());
		printWriter.println("Additional Info: " + chromatogram.getDetailedInfo() + " " + chromatogram.getMiscInfo()); // Don't change without team feedback.
		printWriter.println("Acquisition Date: " + dateFormat.format(chromatogram.getDate()));
		printWriter.println("Operator: " + chromatogram.getOperator());
		printWriter.println("Miscellaneous: " + chromatogram.getMiscInfo());
	}

	private void printQuantitationResults(PrintWriter printWriter, IChromatogram chromatogram, ReportSettings3 reportSettings) {

		printWriter.print("#");
		printWriter.print(DELIMITER);
		printWriter.print("Identification");
		printWriter.print(DELIMITER);
		printWriter.print("Substance");
		printWriter.print(DELIMITER);
		printWriter.print("CAS#");
		printWriter.print(DELIMITER);
		printWriter.print("Reference Identifier");
		printWriter.print(DELIMITER);
		printWriter.print("RT");
		printWriter.print(DELIMITER);
		printWriter.print("RI");
		printWriter.print(DELIMITER);
		printWriter.print("Area");
		printWriter.print(DELIMITER);
		printWriter.print("Conc.");
		printWriter.print(DELIMITER);
		printWriter.print("Unit");
		printWriter.print(DELIMITER);
		printWriter.print("Description");
		printWriter.print(DELIMITER);
		printWriter.print("Chemical Class");
		printWriter.print(DELIMITER);
		printWriter.print("Cross Zero");
		printWriter.print(DELIMITER);
		printWriter.print("Calibration Method");
		printWriter.print(DELIMITER);
		printWriter.print("Traces");
		printWriter.print(DELIMITER);
		printWriter.print("Quantitation Flag");
		printWriter.println("");
		/*
		 * Sort the peaks
		 */
		int i = 1;
		List<IPeak> sortedPeaks = new ArrayList<>();
		sortedPeaks.addAll(chromatogram.getPeaks());
		Collections.sort(sortedPeaks, (p1, p2) -> Integer.compare(p1.getPeakModel().getRetentionTimeAtPeakMaximum(), p2.getPeakModel().getRetentionTimeAtPeakMaximum()));
		//
		for(IPeak peak : sortedPeaks) {
			IPeakModel peakModel = peak.getPeakModel();
			float retentionIndex = peakModel.getPeakMaximum().getRetentionIndex();
			ILibraryInformation libraryInformation = IIdentificationTarget.getLibraryInformation(peak);
			String identification = (libraryInformation != null) ? libraryInformation.getName() : "";
			String casNumber = (libraryInformation != null) ? libraryInformation.getCasNumber() : "";
			String referenceIdentifier = (libraryInformation != null) ? libraryInformation.getReferenceIdentifier() : "";
			String retentionTime = decimalFormatRetentionTime.format(peakModel.getRetentionTimeAtPeakMaximum() / IChromatogramOverview.MINUTE_CORRELATION_FACTOR);
			/*
			 * Sort the quantitation entries.
			 */
			List<IQuantitationEntry> sortedQuantitationEntries = new ArrayList<>();
			sortedQuantitationEntries.addAll(peak.getQuantitationEntries());
			Collections.sort(sortedQuantitationEntries, (q1, q2) -> q1.getName().compareTo(q2.getName()));
			//
			for(IQuantitationEntry quantitationEntry : sortedQuantitationEntries) {
				printWriter.print("P" + i);
				printWriter.print(DELIMITER);
				printWriter.print(identification);
				printWriter.print(DELIMITER);
				printWriter.print(quantitationEntry.getName());
				printWriter.print(DELIMITER);
				printWriter.print(casNumber);
				printWriter.print(DELIMITER);
				printWriter.print(referenceIdentifier);
				printWriter.print(DELIMITER);
				printWriter.print(retentionTime);
				printWriter.print(DELIMITER);
				printWriter.print(retentionIndex);
				printWriter.print(DELIMITER);
				printWriter.print(decimalFormatAreaNormal.format(quantitationEntry.getArea()));
				printWriter.print(DELIMITER);
				printWriter.print(decimalFormatConcentration.format(quantitationEntry.getConcentration()));
				printWriter.print(DELIMITER);
				printWriter.print(quantitationEntry.getConcentrationUnit());
				printWriter.print(DELIMITER);
				printWriter.print(quantitationEntry.getDescription());
				printWriter.print(DELIMITER);
				printWriter.print(quantitationEntry.getChemicalClass());
				printWriter.print(DELIMITER);
				printWriter.print(quantitationEntry.getUsedCrossZero());
				printWriter.print(DELIMITER);
				printWriter.print(quantitationEntry.getCalibrationMethod());
				printWriter.print(DELIMITER);
				printWriter.print(SignalSupport.asText(quantitationEntry.getSignals(), decimalFormatTraces));
				printWriter.print(DELIMITER);
				printWriter.print(quantitationEntry.getQuantitationFlag().shortcut());
				printWriter.println("");
			}
			i++;
		}
	}
}
