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
package org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.BlastMetrics;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.identifier.IComparisonResult;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.support.text.ValueFormat;

public class TaxonomyReportWriter {

	public void generate(File file, boolean append, List<IChromatogram> chromatograms) throws IOException {

		try (PrintWriter printWriter = new PrintWriter(new FileWriter(file, append))) {
			printWriter.println("Taxonomy Report");
			printWriter.println();
			for(IChromatogram chromatogram : chromatograms) {
				printHeader(printWriter, chromatogram);
				printWriter.println();
				reportChromatogram(printWriter, chromatogram);
			}
		}
	}

	private void printHeader(PrintWriter printWriter, IChromatogram chromatogram) {

		printKeyValue(printWriter, "Sample Name", chromatogram.getSampleName());
		printKeyValue(printWriter, "Sample Group", chromatogram.getSampleGroup());
		DateFormat dateFormat = ValueFormat.getDateFormatEnglish();
		printKeyValue(printWriter, "Date", dateFormat.format(chromatogram.getDate()));
	}

	private void printKeyValue(PrintWriter printWriter, String key, String value) {

		printWriter.print(key);
		printWriter.print(": ");
		printWriter.println(value);
	}

	private void reportChromatogram(PrintWriter printWriter, IChromatogram chromatogram) {

		reportBestHit(printWriter, chromatogram);
		printWriter.println();
		reportGroupedHits(printWriter, chromatogram);
	}

	private void reportBestHit(PrintWriter printWriter, IChromatogram chromatogram) {

		Optional<IIdentificationTarget> manuallyVerified = chromatogram.getTargets().stream().filter(t -> t.isVerified()).findAny();
		if(manuallyVerified.isPresent()) {
			reportBestHit(printWriter, manuallyVerified.get());
		} else {
			reportBestHit(printWriter, chromatogram.getTargets().stream().max(Comparator.comparingDouble(target -> target.getComparisonResult().getMetric(BlastMetrics.BIT_SCORE).getAsDouble())).orElse(null));
		}
	}

	private void reportBestHit(PrintWriter printWriter, IIdentificationTarget bestIdentification) {

		if(bestIdentification != null) {
			ILibraryInformation libraryInformation = bestIdentification.getLibraryInformation();
			if(libraryInformation.getSynonyms().iterator().hasNext()) {
				printKeyValue(printWriter, "Best Hit", libraryInformation.getSynonyms().iterator().next());
			} else {
				printKeyValue(printWriter, "Best Hit", libraryInformation.getName());
			}
			IComparisonResult comparisonResult = bestIdentification.getComparisonResult();
			printKeyValue(printWriter, "Bit Score", String.valueOf(comparisonResult.getMetric(BlastMetrics.BIT_SCORE).getAsDouble()));
			printKeyValue(printWriter, "Bit Score", String.valueOf(comparisonResult.getMetric(BlastMetrics.SCORE).getAsDouble()));
			printKeyValue(printWriter, "Coverage", String.valueOf(comparisonResult.getMetric(BlastMetrics.COVERAGE).getAsDouble()) + "%");
			printKeyValue(printWriter, "E value", String.valueOf(comparisonResult.getMetric(BlastMetrics.EVALUE).getAsDouble()));
			printKeyValue(printWriter, "Identity", String.valueOf(comparisonResult.getMetric(BlastMetrics.IDENTITY).getAsDouble()));
			printKeyValue(printWriter, "Gaps", String.valueOf(comparisonResult.getMetric(BlastMetrics.GAPS).getAsDouble()));
			printKeyValue(printWriter, "Accession", libraryInformation.getGenBankAccesion());
			printKeyValue(printWriter, "Tax ID", String.valueOf(libraryInformation.getTaxonomyIdentifierNCBI()));
		}
	}

	private void reportGroupedHits(PrintWriter printWriter, IChromatogram chromatogram) {

		Map<Integer, List<IIdentificationTarget>> groupedResults = chromatogram.getTargets().stream().collect( //
				Collectors.groupingBy(target -> target.getLibraryInformation().getTaxonomyIdentifierNCBI()));

		List<List<IIdentificationTarget>> orderedResults = groupedResults.values().stream().sorted(//
				Comparator.comparingDouble((List<IIdentificationTarget> targets) -> //
				targets.stream().mapToDouble(target -> //
				target.getComparisonResult().getMetric(BlastMetrics.BIT_SCORE).getAsDouble()).max().orElse(0.0)) //
							.reversed()).toList();

		for(List<IIdentificationTarget> targets : orderedResults) {
			double highestBitScore = targets.stream().mapToDouble(target -> //
			target.getComparisonResult().getMetric(BlastMetrics.BIT_SCORE).getAsDouble()).max().orElse(0.0);

			String identifier = "";
			ILibraryInformation libraryInformation = targets.getFirst().getLibraryInformation();
			if(!libraryInformation.getSynonyms().isEmpty()) {
				identifier = libraryInformation.getSynonyms().iterator().next();
			} else {
				identifier = String.valueOf(libraryInformation.getTaxonomyIdentifierNCBI()); // fallback if database is not installed
			}
			printWriter.println(targets.size() + " x " + identifier + ": " + highestBitScore);
		}
	}
}
