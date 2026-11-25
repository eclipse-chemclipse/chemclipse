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
package org.eclipse.chemclipse.msd.filter.supplier.shift.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.msd.filter.result.IMassSpectrumFilterResult;
import org.eclipse.chemclipse.model.core.IMassSpectrumPeak;
import org.eclipse.chemclipse.model.core.MassSpectrumPeak;
import org.eclipse.chemclipse.msd.filter.supplier.shift.settings.MassSpectrumFilterSettings;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IIonMSn;
import org.eclipse.chemclipse.msd.model.core.IIonTransition;
import org.eclipse.chemclipse.msd.model.core.IStandaloneMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.IonMSn;
import org.eclipse.chemclipse.msd.model.implementation.IonTransition;
import org.eclipse.chemclipse.msd.model.implementation.StandaloneMassSpectrum;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.jupiter.api.Test;

public class MassSpectrumFilter_1_Test {

	private static final double DELTA = 1e-9d;
	private static final double ION_OFFSET = 0.125d;
	private static final double PRECURSOR_OFFSET = -0.25d;
	private static final double PRECURSOR = 500.0d;
	private static final double COLLISION_ENERGY = 30.0d;
	private static final double[] MZS = {100.0d, 200.0d};
	private static final float[] INTENSITIES = {50f, 80f};

	@Test
	public void testTandemMS() {

		IStandaloneMassSpectrum massSpectrum = createMassSpectrum(true);
		IProcessingInfo<IMassSpectrumFilterResult> processingInfo = applyFilter(massSpectrum, ION_OFFSET, PRECURSOR_OFFSET);

		assertFalse(processingInfo.hasErrorMessages());
		assertTandemMS(massSpectrum, ION_OFFSET, PRECURSOR_OFFSET);
	}

	@Test
	public void testTandemMSKeepPrecursor() {

		IStandaloneMassSpectrum massSpectrum = createMassSpectrum(true);
		IProcessingInfo<IMassSpectrumFilterResult> processingInfo = applyFilter(massSpectrum, ION_OFFSET, 0);

		assertFalse(processingInfo.hasErrorMessages());
		assertTandemMS(massSpectrum, ION_OFFSET, 0);
	}

	@Test
	public void testTandemMSKeepIons() {

		IStandaloneMassSpectrum massSpectrum = createMassSpectrum(true);
		IProcessingInfo<IMassSpectrumFilterResult> processingInfo = applyFilter(massSpectrum, 0, PRECURSOR_OFFSET);

		assertFalse(processingInfo.hasErrorMessages());
		assertTandemMS(massSpectrum, 0, PRECURSOR_OFFSET);
	}

	@Test
	public void testSingleMS() {

		IStandaloneMassSpectrum massSpectrum = createMassSpectrum(false);
		IProcessingInfo<IMassSpectrumFilterResult> processingInfo = applyFilter(massSpectrum, -ION_OFFSET, PRECURSOR_OFFSET);

		assertFalse(processingInfo.hasErrorMessages());
		assertEquals(0, massSpectrum.getPrecursorIon());
		List<IIon> ions = massSpectrum.getIons();
		for(int i = 0; i < MZS.length; i++) {
			assertEquals(MZS[i] - ION_OFFSET, ions.get(i).getIon(), DELTA);
			assertFalse(ions.get(i) instanceof IIonMSn);
		}
		List<IMassSpectrumPeak> peaks = massSpectrum.getPeaks();
		for(int i = 0; i < MZS.length; i++) {
			assertEquals(MZS[i] - ION_OFFSET, peaks.get(i).getIon(), DELTA);
		}
	}

	@Test
	public void testNegativeMZ() {

		IStandaloneMassSpectrum massSpectrum = createMassSpectrum(true);
		IProcessingInfo<IMassSpectrumFilterResult> processingInfo = applyFilter(massSpectrum, -150.0d, 0);

		assertTrue(processingInfo.hasErrorMessages());
		assertTandemMS(massSpectrum, 0, 0);
	}

	@Test
	public void testNegativePrecursor() {

		IStandaloneMassSpectrum massSpectrum = createMassSpectrum(true);
		IProcessingInfo<IMassSpectrumFilterResult> processingInfo = applyFilter(massSpectrum, ION_OFFSET, -600.0d);

		assertTrue(processingInfo.hasErrorMessages());
		assertTandemMS(massSpectrum, 0, 0);
	}

	private static void assertTandemMS(IStandaloneMassSpectrum massSpectrum, double ionOffset, double precursorOffset) {

		assertEquals(PRECURSOR + precursorOffset, massSpectrum.getPrecursorIon(), DELTA);
		assertTrue(massSpectrum.isTandemMS());
		List<IIon> ions = massSpectrum.getIons();
		assertEquals(MZS.length, ions.size());
		for(int i = 0; i < MZS.length; i++) {
			IIon ion = ions.get(i);
			assertEquals(MZS[i] + ionOffset, ion.getIon(), DELTA);
			assertEquals(INTENSITIES[i], ion.getAbundance());
			assertEquals(MZS[i] + ionOffset, massSpectrum.getPeaks().get(i).getIon(), DELTA);
			IIonTransition ionTransition = assertInstanceOf(IIonMSn.class, ion).getIonTransition();
			assertEquals(PRECURSOR + precursorOffset, ionTransition.getQ1Ion(), DELTA);
			assertEquals(MZS[i] + ionOffset, ionTransition.getQ3Ion(), DELTA);
			assertEquals(COLLISION_ENERGY, ionTransition.getCollisionEnergy());
		}
	}

	private static IProcessingInfo<IMassSpectrumFilterResult> applyFilter(IStandaloneMassSpectrum massSpectrum, double ionOffset, double precursorOffset) {

		MassSpectrumFilterSettings settings = new MassSpectrumFilterSettings();
		settings.setIonOffset(ionOffset);
		settings.setPrecursorOffset(precursorOffset);
		return new MassSpectrumFilter().applyFilter(List.of(massSpectrum), settings, new NullProgressMonitor());
	}

	private static IStandaloneMassSpectrum createMassSpectrum(boolean tandemMS) {

		IStandaloneMassSpectrum massSpectrum = new StandaloneMassSpectrum();
		massSpectrum.setMassSpectrumType(MassSpectrumType.CENTROID);
		if(tandemMS) {
			massSpectrum.setPrecursorIon(PRECURSOR);
		}
		for(int i = 0; i < MZS.length; i++) {
			if(tandemMS) {
				IIonTransition ionTransition = new IonTransition(PRECURSOR, MZS[i], COLLISION_ENERGY, 1, 1, 0);
				massSpectrum.addIon(new IonMSn(MZS[i], INTENSITIES[i], ionTransition), false);
			} else {
				massSpectrum.addIon(new Ion(MZS[i], INTENSITIES[i]), false);
			}
			IMassSpectrumPeak peak = new MassSpectrumPeak();
			peak.setIon(MZS[i]);
			peak.setAbundance(INTENSITIES[i]);
			massSpectrum.getPeaks().add(peak);
		}
		return massSpectrum;
	}
}
