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
import java.math.BigInteger;
import java.util.regex.Pattern;

import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.BlastMetrics;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.BlastOutput2;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Hit;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.HitDescr;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Hsp;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Report;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model.xml.v2.Search;
import org.eclipse.chemclipse.model.identifier.ComparisonResult;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.identifier.LibraryInformation;
import org.eclipse.chemclipse.model.implementation.IdentificationTarget;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;

public abstract class AbstractNucleotideBLAST {

	private static final Pattern ENDS_WITH_DOT_NUMBER = Pattern.compile("\\.\\d+$");

	public static void transferTargets(IChromatogramWSD chromatogram, BlastOutput2 blastOutput) {

		if(blastOutput == null) {
			return;
		}

		Report report = blastOutput.getReport().getReport();
		Search search = report.getResults().getResults().getSearch().getSearch();
		for(Hit hit : search.getHits().getHit()) {
			ILibraryInformation libraryInformation = new LibraryInformation();
			HitDescr description = hit.getDescription().getHitDescr().getFirst();
			libraryInformation.setName(description.getTitle());
			String db = report.getSearchTarget().getTarget().getDb();
			File dbPath = new File(db);
			if(dbPath.getParentFile().exists()) {
				libraryInformation.setDatabase(stripDotNumberSuffix(dbPath.getName()));
			} else {
				libraryInformation.setDatabase(db); // web
			}
			libraryInformation.setGenBankAccesion(description.getAccession());
			libraryInformation.setReferenceIdentifier(description.getId());
			libraryInformation.setTaxonomyIdentifierNCBI(description.getTaxid().intValue());
			for(Hsp hsp : hit.getHsps().getHsp()) {
				IdentificationTarget identificationTarget = new IdentificationTarget(libraryInformation, createComparisonResult(hsp, search));
				identificationTarget.setIdentifier(report.getVersion());
				chromatogram.getTargets().add(identificationTarget);
			}
		}
	}

	private static ComparisonResult createComparisonResult(Hsp hsp, Search search) {

		ComparisonResult comparisonResult = new ComparisonResult(BlastMetrics.ALGORITHM_BLASTN);
		comparisonResult.setMetric(BlastMetrics.IDENTITY, getPercentIdentity(hsp));
		comparisonResult.setMetric(BlastMetrics.COVERAGE, getQueryCoverage(hsp, search));
		comparisonResult.setMetric(BlastMetrics.EVALUE, hsp.getEvalue());
		comparisonResult.setMetric(BlastMetrics.BIT_SCORE, hsp.getBitScore());
		comparisonResult.setMetric(BlastMetrics.SCORE, hsp.getScore());
		if(hsp.getGaps() != null) {
			comparisonResult.setMetric(BlastMetrics.GAPS, hsp.getGaps().doubleValue());
		}

		return comparisonResult;
	}

	/**
	 * How many aligned positions are identical?
	 */
	private static double getPercentIdentity(Hsp hsp) {

		BigInteger identity = hsp.getIdentity();
		BigInteger alignLength = hsp.getAlignLen();
		if(identity == null || alignLength == null || alignLength.signum() <= 0) {
			return 0.0d;
		}

		return 100.0d * identity.doubleValue() / alignLength.doubleValue();
	}

	/**
	 * Percentage of the query sequence that is included
	 * in the alignment to the subject sequence
	 */
	private static double getQueryCoverage(Hsp hsp, Search search) {

		if(hsp == null || search.getQueryLen() == null || search.getQueryLen().intValue() <= 0) {
			return 0.0d;
		}

		BigInteger queryFrom = hsp.getQueryFrom();
		BigInteger queryTo = hsp.getQueryTo();

		if(queryFrom == null || queryTo == null) {
			return 0.0d;
		}

		int covered = Math.abs(queryTo.intValue() - queryFrom.intValue()) + 1;

		return 100.0d * covered / search.getQueryLen().intValue();
	}

	// treat multi-volume databases as one
	private static String stripDotNumberSuffix(String value) {

		if(ENDS_WITH_DOT_NUMBER.matcher(value).find()) {
			return ENDS_WITH_DOT_NUMBER.matcher(value).replaceFirst("");
		}
		return value;
	}
}
