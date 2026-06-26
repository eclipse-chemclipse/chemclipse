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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.comparison.supplier.entropy.comparator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.model.identifier.ComparisonResult;
import org.eclipse.chemclipse.model.identifier.IComparisonResult;
import org.eclipse.chemclipse.model.identifier.MatchConstraints;
import org.eclipse.chemclipse.msd.identifier.comparison.AbstractMassSpectrumComparator;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.xic.ExtractedIonSignal;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignal;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;

/*
 * Li, Y., Kind, T., Folz, J. et al.
 * Spectral entropy outperforms MS/MS dot product similarity for small-molecule compound identification.
 * Nat Methods 18, 1524-1531 (2021).
 * https://doi.org/10.1038/s41592-021-01331-z.
 */
public class EntropyComparator extends AbstractMassSpectrumComparator {

	@Override
	public IProcessingInfo<IComparisonResult> compare(IScanMSD unknown, IScanMSD reference, MatchConstraints matchConstraints) {

		IProcessingInfo<IComparisonResult> processingInfo = super.validate(unknown, reference);
		if(!processingInfo.hasErrorMessages()) {
			/*
			 * Get the match and reverse match factor.
			 * Internally it's normalized to 1, but a percentage value is used by the MS methods.
			 */
			IExtractedIonSignal unknownSignal = unknown.getExtractedIonSignal();
			IExtractedIonSignal referenceSignal = reference.getExtractedIonSignal();
			IExtractedIonSignal virtualSignal = getVirtualSignal(unknownSignal, referenceSignal);
			/*
			 * Unit Vector
			 */
			unknownSignal.normalizeVector(1.0f);
			referenceSignal.normalizeVector(1.0f);
			virtualSignal.normalizeVector(1.0f);
			/*
			 * Spectral Entropy
			 */
			double spectralEntropyUnkown = getSpectralEntropy(unknownSignal, true);
			double spectralEntropyReference = getSpectralEntropy(referenceSignal, true);
			double spectralEntropyVirtual = getSpectralEntropy(virtualSignal, true);
			/*
			 * Match
			 */
			// float entropyDistance = calculateEntropyDistance(spectralEntropyUnkown, spectralEntropyReference, spectralEntropyVirtual);
			float matchFactor = calculateMatchFactor(spectralEntropyUnkown, spectralEntropyReference, spectralEntropyVirtual);
			IComparisonResult comparisonResult = new ComparisonResult(matchFactor);
			processingInfo.setProcessingResult(comparisonResult);
		}
		return processingInfo;
	}

	/**
	 * If flag normalized is set to true, then the signal must be normalized via signal.normalizeVector(1.0f);
	 * 
	 * @param extractedIonSignal
	 * @param isNormalized
	 * @return double
	 */
	public double getSpectralEntropy(IExtractedIonSignal extractedIonSignal, boolean isNormalized) {

		double sum = 0;
		/*
		 * Normalization is a hard requirement.
		 */
		if(!isNormalized) {
			extractedIonSignal.normalizeVector(1.0f);
		}
		/*
		 * Calculate
		 */
		int start = extractedIonSignal.getStartIon();
		int stop = extractedIonSignal.getStopIon();
		for(int ion = start; ion <= stop; ion++) {
			double intensity = extractedIonSignal.getAbundance(ion);
			if(intensity > 0) {
				sum += (intensity * Math.log(intensity));
			}
		}

		return -sum;
	}

	public IExtractedIonSignal getVirtualSignal(IExtractedIonSignal unknownSignal, IExtractedIonSignal referenceSignal) {

		List<IIon> ions = new ArrayList<>();
		int start = Math.min(unknownSignal.getStartIon(), referenceSignal.getStartIon());
		int stop = Math.max(unknownSignal.getStopIon(), referenceSignal.getStopIon());
		for(int ion = start; ion <= stop; ion++) {
			float intensity = unknownSignal.getAbundance(ion) + referenceSignal.getAbundance(ion);
			if(intensity > 0) {
				ions.add(new Ion(ion, intensity));
			}
		}

		return new ExtractedIonSignal(ions);
	}

	public float calculateEntropyDistance(double spectralEntropyUnkown, double spectralEntropyReference, double spectralEntropyVirtual) {

		return (float)((spectralEntropyVirtual - spectralEntropyUnkown) + (spectralEntropyVirtual - spectralEntropyReference));
	}

	/*
	 * TODO: 0 - 100%
	 */
	public float calculateMatchFactor(double spectralEntropyUnkown, double spectralEntropyReference, double spectralEntropyVirtual) {

		double unweightedEntropySimilarity = 1 - ((2 * spectralEntropyVirtual - spectralEntropyUnkown - spectralEntropyReference) / Math.log(4));
		return (float)(unweightedEntropySimilarity * 100);
	}
}
