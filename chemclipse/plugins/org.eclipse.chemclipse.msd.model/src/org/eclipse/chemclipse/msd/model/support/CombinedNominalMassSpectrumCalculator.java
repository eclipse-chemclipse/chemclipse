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
import org.eclipse.chemclipse.msd.model.core.AbstractIon;
import org.eclipse.chemclipse.msd.model.core.ICombinedMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.implementation.CombinedMassSpectrum;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.util.MarkedTracesSupportMSD;
import org.eclipse.chemclipse.support.traces.ITrace;

public class CombinedNominalMassSpectrumCalculator extends CombinedMassSpectrumCalculator {

	private Map<Integer, List<Double>> combinedMassSpectrum = new HashMap<>();

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
		int key = AbstractIon.getIon(ion);
		/*
		 * Add the abundance if still a ion exists, otherwise still add the ion.
		 */
		List<Double> intensities = combinedMassSpectrum.get(key);
		if(intensities == null) {
			intensities = new ArrayList<>();
			combinedMassSpectrum.put(key, intensities);
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
			int mz = AbstractIon.getIon(ion.getIon());
			if(!excludedIonsNominal.contains(mz)) {
				addIon(ion.getIon(), ion.getAbundance());
			}
		}
	}

	@Override
	public void removeIon(double ion) {

		int key = AbstractIon.getIon(ion);
		combinedMassSpectrum.remove(key);
	}

	@Override
	public void removeIons(IMarkedTraces<ITrace> excludedIons) {

		for(int ion : MarkedTracesSupportMSD.getTracesAsInteger(excludedIons)) {
			combinedMassSpectrum.remove(ion);
		}
	}

	@Override
	public ICombinedMassSpectrum createMassSpectrum(CalculationType calculationType) {

		ICombinedMassSpectrum massSpectrum = new CombinedMassSpectrum();
		for(Integer ion : combinedMassSpectrum.keySet()) {
			float intensity = (float)getAbundance(ion, calculationType);
			if(intensity > IIon.ZERO_INTENSITY) {
				massSpectrum.addIon(new Ion(ion, intensity));
			}
		}
		return massSpectrum;
	}

	public Map<Integer, List<Double>> getValues() {

		return Collections.unmodifiableMap(combinedMassSpectrum);
	}

	private double getAbundance(double ion, CalculationType calculationType) {

		int key = AbstractIon.getIon(ion);
		return calculateSumIntensity(combinedMassSpectrum.get(key), calculationType);
	}
}