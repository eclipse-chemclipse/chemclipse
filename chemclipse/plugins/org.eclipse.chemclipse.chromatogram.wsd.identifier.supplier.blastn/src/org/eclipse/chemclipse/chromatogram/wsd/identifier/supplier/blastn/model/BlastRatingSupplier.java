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
package org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model;

import java.util.OptionalDouble;

import org.eclipse.chemclipse.model.identifier.AbstractComparisonRatingSupplier;
import org.eclipse.chemclipse.model.identifier.IComparisonResult;

/**
 * Normalizes the metrics of a BLAST hit to a score between 0 and 100, so that
 * a BLAST hit and a spectral match stay comparable when they are listed in one
 * table.
 *
 * The percent identity carries the score, the e-value decides how much of it
 * is trusted. An alignment that could easily occur by chance is worth little,
 * even if the aligned region matches perfectly.
 */
public class BlastRatingSupplier extends AbstractComparisonRatingSupplier {

	/**
	 * Renew the UUID on change.
	 */
	private static final long serialVersionUID = 6114130570311935061L;

	private static final String ADVISE_LOW_IDENTITY = "Low Identity (Distant Match)";
	private static final String ADVISE_NOT_SIGNIFICANT = "Not Significant (High E-Value)";

	/*
	 * An e-value of 1e-10 or lower is trusted completely, an e-value of 1 or
	 * higher is not trusted at all.
	 */
	private static final double EVALUE_EXPONENT_SIGNIFICANT = 10.0d;
	private static final double EVALUE_NOT_SIGNIFICANT = 1.0d;
	private static final double MIN_IDENTITY = 70.0d;

	@Override
	public float getScore() {

		IComparisonResult comparisonResult = getComparisonResult();
		OptionalDouble identity = comparisonResult.getMetric(BlastMetrics.IDENTITY);
		if(identity.isEmpty()) {
			return Float.NaN;
		}

		return (float)(identity.getAsDouble() * getConfidence(comparisonResult));
	}

	@Override
	public String getAdvise() {

		IComparisonResult comparisonResult = getComparisonResult();
		OptionalDouble identity = comparisonResult.getMetric(BlastMetrics.IDENTITY);
		if(identity.isEmpty()) {
			return "";
		}

		if(getConfidence(comparisonResult) < 1.0d) {
			return ADVISE_NOT_SIGNIFICANT;
		} else if(identity.getAsDouble() < MIN_IDENTITY) {
			return ADVISE_LOW_IDENTITY;
		}

		return "";
	}

	/**
	 * Returns how much of the identity is trusted, 0.0 to 1.0, derived from the
	 * e-value. A hit without an e-value is trusted completely.
	 */
	private static double getConfidence(IComparisonResult comparisonResult) {

		OptionalDouble evalue = comparisonResult.getMetric(BlastMetrics.EVALUE);
		if(evalue.isEmpty()) {
			return 1.0d;
		}

		double value = evalue.getAsDouble();
		if(value <= 0.0d) {
			/*
			 * BLAST reports 0 for an alignment that cannot occur by chance.
			 */
			return 1.0d;
		} else if(value >= EVALUE_NOT_SIGNIFICANT) {
			return 0.0d;
		}

		double exponent = -Math.log10(value);
		return Math.min(1.0d, exponent / EVALUE_EXPONENT_SIGNIFICANT);
	}
}
