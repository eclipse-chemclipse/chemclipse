/*******************************************************************************
 * Copyright (c) 2016, 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Dr. Alexander Kerner - implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.identifier;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IScan;

public class PenaltyCalculationSupport {

	private static final double NO_PENALTY = 0.0d;

	/**
	 * Calculate and apply the penalty on demand.
	 * 
	 * @param unknown
	 * @param reference
	 * @param comparisonResult
	 * @param penaltyCalculationSettings
	 */
	public static void applyPenalty(IScan unknown, IScan reference, IComparisonResult comparisonResult, IPenaltyCalculationSettings penaltyCalculationSettings) {

		int retentionTimeUnknown = unknown.getRetentionTime();
		float retentionIndexUnknown = unknown.getRetentionIndex();
		int retentionTimeReference = reference.getRetentionTime();
		float retentionIndexReference = reference.getRetentionIndex();
		applyPenalty(retentionTimeUnknown, retentionIndexUnknown, retentionTimeReference, retentionIndexReference, comparisonResult, penaltyCalculationSettings);
	}

	/**
	 * Calculate and apply the penalty on demand.
	 * 
	 * @param retentionTimeUnknown
	 * @param retentionIndexUnknown
	 * @param retentionTimeReference
	 * @param retentionIndexReference
	 * @param comparisonResult
	 * @param penaltyCalculationSettings
	 */
	public static void applyPenalty(int retentionTimeUnknown, float retentionIndexUnknown, int retentionTimeReference, float retentionIndexReference, IComparisonResult comparisonResult, IPenaltyCalculationSettings penaltyCalculationSettings) {

		final float penalty;
		switch(penaltyCalculationSettings.getPenaltyCalculation()) {
			case RETENTION_TIME_MS:
				penalty = (float)calculatePenalty(retentionTimeUnknown, retentionTimeReference, penaltyCalculationSettings.getPenaltyWindow(), penaltyCalculationSettings.getPenaltyLevelFactor(), penaltyCalculationSettings.getMaxPenalty());
				break;
			case RETENTION_TIME_MIN:
				penalty = (float)calculatePenalty(retentionTimeUnknown / IChromatogram.MINUTE_CORRELATION_FACTOR, retentionTimeReference / IChromatogram.MINUTE_CORRELATION_FACTOR, penaltyCalculationSettings.getPenaltyWindow(), penaltyCalculationSettings.getPenaltyLevelFactor(), penaltyCalculationSettings.getMaxPenalty());
				break;
			case RETENTION_INDEX:
				penalty = (float)calculatePenalty(retentionIndexUnknown, retentionIndexReference, penaltyCalculationSettings.getPenaltyWindow(), penaltyCalculationSettings.getPenaltyLevelFactor(), penaltyCalculationSettings.getMaxPenalty());
				break;
			default:
				penalty = 0.0f;
				break;
		}
		/*
		 * Apply the penalty on demand.
		 */
		if(penalty != 0.0f) {
			comparisonResult.setPenalty(penalty);
		}
	}

	/**
	 * Calculates the penalty for the given values.
	 * 
	 * @param valueUnknown
	 * @param valueReference
	 * @param valueWindow
	 * @param penaltyCalculationLevelFactor
	 * @param maxPenalty
	 * @return double
	 */
	public static double calculatePenalty(double valueUnknown, double valueReference, double valueWindow, double penaltyCalculationLevelFactor, double maxPenalty) {

		/*
		 * Checks
		 */
		if(Double.isNaN(valueUnknown) || valueUnknown < 0) {
			return NO_PENALTY;
		}
		//
		if(Double.isNaN(valueReference) || valueReference < 0) {
			return NO_PENALTY;
		}
		//
		if(Double.isNaN(valueWindow) || valueWindow <= 0) {
			return NO_PENALTY;
		}
		//
		if(Double.isNaN(penaltyCalculationLevelFactor) || penaltyCalculationLevelFactor <= 0) {
			return NO_PENALTY;
		}
		//
		if(Double.isNaN(maxPenalty) || maxPenalty <= IPenaltyCalculationSettings.MIN_PENALTY_MATCH_FACTOR || maxPenalty > IPenaltyCalculationSettings.MAX_PENALTY_MATCH_FACTOR) {
			return NO_PENALTY;
		}
		/*
		 * Calculation
		 */
		final double windowRangeCount = Math.abs((valueUnknown - valueReference) / valueWindow);
		if(windowRangeCount <= 1.0f) {
			return NO_PENALTY;
		} else {
			final double result = (windowRangeCount - 1.0f) * penaltyCalculationLevelFactor;
			return (result > maxPenalty) ? maxPenalty : result;
		}
	}
}
