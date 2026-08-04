/*******************************************************************************
 * Copyright (c) 2008, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.model.support.CalculationType;
import org.eclipse.chemclipse.msd.model.core.ICombinedMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.implementation.CombinedMassSpectrum;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.util.MarkedTracesSupportMSD;
import org.eclipse.chemclipse.support.traces.ITrace;

public class CombinedHighResolutionMassSpectrumCalculator extends CombinedMassSpectrumCalculator {

	private Map<Double, List<Double>> combinedMassSpectrum = new HashMap<>();

	@Override
	public int size() {

		return combinedMassSpectrum.size();
	}

	@Override
	public void addIon(double ion, double abundance) {

		/*
		 * If the abundance is zero, do nothing and return.
		 */
		if(abundance == 0.0d) {
			return;
		}
		/*
		 * Add the abundance if still a ion exists, otherwise still add the ion.
		 */
		List<Double> intensities = combinedMassSpectrum.get(ion);
		if(intensities == null) {
			intensities = new ArrayList<>();
			combinedMassSpectrum.put(ion, intensities);
		}
		intensities.add(abundance);
	}

	@Override
	public void addIons(List<IIon> ions, IMarkedTraces<ITrace> excludedIons) {

		if(ions == null || excludedIons == null) {
			return;
		}

		Set<Integer> excludedIonsNominal = MarkedTracesSupportMSD.getTracesAsInteger(excludedIons);
		for(IIon ion : ions) {
			int mz = (int)ion.getIon();
			if(!excludedIonsNominal.contains(mz)) {
				addIon(ion.getIon(), ion.getAbundance());
			}
		}
	}

	@Override
	public void removeIon(double ion) {

		combinedMassSpectrum.remove(ion);
	}

	@Override
	public void removeIons(IMarkedTraces<ITrace> excludedIons) {

		if(excludedIons == null) {
			return;
		}
		Set<Integer> excludedIonsNominal = MarkedTracesSupportMSD.getTracesAsInteger(excludedIons);
		List<Double> mzs = new ArrayList<>(combinedMassSpectrum.keySet());
		for(double mz : mzs) {
			int nominal = (int)mz;
			if(excludedIonsNominal.contains(nominal)) {
				removeIon(mz);
			}
		}
	}

	@Override
	public ICombinedMassSpectrum createMassSpectrum(CalculationType calculationType) {

		ICombinedMassSpectrum massSpectrum = new CombinedMassSpectrum();
		for(Double ion : combinedMassSpectrum.keySet()) {
			float intensity = (float)getAbundance(ion, calculationType);
			if(intensity > IIon.ZERO_INTENSITY) {
				massSpectrum.addIon(new Ion(ion, intensity), false); // check is too expensive
			}
		}
		return massSpectrum;
	}

	public Map<Double, List<Double>> getValues() {

		return Collections.unmodifiableMap(combinedMassSpectrum);
	}

	private double getAbundance(double ion, CalculationType calculationType) {

		return calculateSumIntensity(combinedMassSpectrum.get(ion), calculationType);
	}
}
